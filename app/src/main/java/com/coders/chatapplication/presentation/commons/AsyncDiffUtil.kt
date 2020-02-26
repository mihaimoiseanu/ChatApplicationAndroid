package com.coders.chatapplication.presentation.commons

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.toImmutableList
import kotlin.coroutines.CoroutineContext


class AsyncDiffUtil<T>(
	private val itemCallback: DiffUtil.ItemCallback<T>,
	private val listUpdateCallback: ListUpdateCallback,
	private val job: Job = Job()
) : CoroutineScope {

	override val coroutineContext: CoroutineContext
		get() = job + Dispatchers.Default

	private var list: MutableList<T>? = null
	private var readOnlyList: List<T> = emptyList()
	private lateinit var operationChannel: Channel<UpdateListOperation>

	val onDiffFinished = MutableLiveData<Unit>()

	constructor(
		adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
		itemCallback: DiffUtil.ItemCallback<T>,
		job: Job = Job()
	) : this(itemCallback, SimpleUpdateCallback(adapter), job)

	internal sealed class UpdateListOperation {
		object Clear : UpdateListOperation()
		data class Update(val newList: MutableList<*>) : UpdateListOperation()
	}

	internal class SimpleUpdateCallback(
		private val adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>
	) : ListUpdateCallback {
		override fun onChanged(position: Int, count: Int, payload: Any?) {
			adapter.notifyItemRangeChanged(position, count, payload)
		}

		override fun onMoved(fromPosition: Int, toPosition: Int) {
			adapter.notifyItemMoved(fromPosition, toPosition)
		}

		override fun onInserted(position: Int, count: Int) {
			adapter.notifyItemRangeInserted(position, count)
		}

		override fun onRemoved(position: Int, count: Int) {
			adapter.notifyItemRangeRemoved(position, count)
		}
	}

	fun current() = readOnlyList

	fun update(newList: MutableList<T>?) {
		if (!::operationChannel.isInitialized) {
			initOperationChannel()
		}
		launch {
			if (newList == null) {
				operationChannel.send(UpdateListOperation.Clear)
			} else {
				operationChannel.send(UpdateListOperation.Update(newList))
			}
		}
	}

	private suspend fun clear(count: Int) {
		withContext(Dispatchers.Main) {
			list = null
			readOnlyList = emptyList()
			listUpdateCallback.onRemoved(0, count)
			onDiffFinished.postValue(Unit)
		}
	}

	@Suppress("UNCHECKED_CAST")
	protected suspend fun insert(newList: List<*>) {
		withContext(Dispatchers.Main) {
			list = newList as MutableList<T>
			readOnlyList = newList.toImmutableList()
			listUpdateCallback.onInserted(0, newList.size)
			onDiffFinished.postValue(Unit)
		}
	}

	@Suppress("UNCHECKED_CAST")
	private suspend fun update(newList: List<*>, callback: DiffUtil.Callback) {
		withContext(Dispatchers.Default) {
			val result = DiffUtil.calculateDiff(callback)
			if (!coroutineContext.isActive) return@withContext
			latch(newList as MutableList<T>, result)
		}
	}

	private suspend fun latch(newList: MutableList<T>, result: DiffUtil.DiffResult) {
		withContext(Dispatchers.Main) {
			list = newList
			readOnlyList = newList.toImmutableList()
			result.dispatchUpdatesTo(listUpdateCallback)
			onDiffFinished.postValue(Unit)
		}
	}

	private fun initOperationChannel() {
		operationChannel = Channel(Channel.CONFLATED)
		operationChannel
			.consumeAsFlow()
			.filter { isActive }
			.onEach {
				val oldList = list

				when (it) {
					UpdateListOperation.Clear -> {
						if (oldList != null) {
							clear(oldList.size)
						}
					}
					is UpdateListOperation.Update -> {
						if (oldList == null) {
							insert(it.newList)
						} else if (oldList != it.newList) {
							val callback =
								diffUtilCallback(oldList, it.newList as List<T>, itemCallback)
							update(it.newList, callback)
						}
					}
				}
			}
			.launchIn(this)
	}

	private fun diffUtilCallback(
		oldList: List<T>,
		newList: List<T>,
		callback: DiffUtil.ItemCallback<T>
	) =
		object : DiffUtil.Callback() {
			override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
				val oldItem = oldList[oldItemPosition]
				val newItem = newList[newItemPosition]
				return if (oldItem != null && newItem != null) {
					callback.areItemsTheSame(oldItem, newItem)
				} else {
					oldItem == null && newItem == null
				}
			}

			override fun getOldListSize(): Int = oldList.size

			override fun getNewListSize(): Int = newList.size

			override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
				val oldItem = oldList[oldItemPosition]
				val newItem = newList[newItemPosition]
				return if (oldItem != null && newItem != null) {
					callback.areContentsTheSame(oldItem, newItem)
				} else if (oldItem == null && newItem == null) {
					return true
				} else {
					throw AssertionError()
				}
			}
		}

}