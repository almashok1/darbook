package kz.adamant.bookstore.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setEqualSpacing(spacing: Int) {
    setPadding(spacing, spacing, spacing, spacing)
    clipToPadding = false
    clipChildren = false
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.set(spacing, spacing, spacing, spacing)
        }
    })
}