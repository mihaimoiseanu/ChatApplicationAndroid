package com.coders.chatapplication.presentation.ui.rooms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.coders.chatapplication.R
import com.coders.chatapplication.domain.model.RoomModel
import com.coders.chatapplication.presentation.commons.AsyncDiffUtil

class RoomsAdapter(
	private val onItemClicked: (RoomModel) -> Unit
) : RecyclerView.Adapter<RoomsAdapter.ViewHolder>() {

	private val diffUtil = AsyncDiffUtil(this, object : DiffUtil.ItemCallback<RoomModel>() {
		override fun areItemsTheSame(oldItem: RoomModel, newItem: RoomModel): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: RoomModel, newItem: RoomModel): Boolean {
			return oldItem == newItem
		}
	})

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
		)
	}

	override fun getItemCount(): Int = diffUtil.current().size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bindView(diffUtil.current()[position])
	}

	fun update(rooms: List<RoomModel>) {
		diffUtil.update(rooms.toMutableList())
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

		fun bindView(roomModel: RoomModel) {
			(itemView as TextView).text = roomModel.name
			itemView.setOnClickListener {
				onItemClicked(roomModel)
			}
		}

	}
}