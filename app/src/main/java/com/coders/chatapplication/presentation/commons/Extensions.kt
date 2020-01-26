package com.coders.chatapplication.presentation.commons

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private fun <T> lazyUnsynchronized(initializer: () -> T): Lazy<T> =
	lazy(LazyThreadSafetyMode.NONE, initializer)

fun <ViewT : View> Activity.bindView(@IdRes idRes: Int): Lazy<ViewT> {
	return lazyUnsynchronized {
		findViewById<ViewT>(idRes)
	}
}

fun <V : View> Fragment.bindView(@IdRes idRes: Int): ReadOnlyProperty<Fragment, V> =
	FragmentBinder(this) { it.view!!.findViewById<V>(idRes) }

private class FragmentBinder<out V : View>(
	val fragment: Fragment,
	val initializer: (Fragment) -> V
) : ReadOnlyProperty<Fragment, V>, DefaultLifecycleObserver {

	private object EMPTY

	private var viewValue: Any = EMPTY

	init {
		fragment.viewLifecycleOwnerLiveData.observe(
			fragment,
			Observer { it.lifecycle.addObserver(this) })
	}

	override fun getValue(thisRef: Fragment, property: KProperty<*>): V {
		if (EMPTY == viewValue) {
			viewValue = initializer(fragment)
		}
		@Suppress("UNCHECKED_CAST")
		return viewValue as V
	}

	override fun onDestroy(owner: LifecycleOwner) {
		viewValue = EMPTY
	}
}

fun View.goneUnless(isVisible: Boolean = false) {
	visibility = if (isVisible) {
		View.VISIBLE
	} else {
		View.GONE
	}
}

fun Context.toastIt(message:String){
	Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}