package kz.adamant.bookstore.utils

import android.graphics.Rect
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kz.adamant.bookstore.R
import kz.adamant.bookstore.models.BookDvo

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

fun ImageView.loadBookImage(book: BookDvo?) {
    this.load(if (book != null) Constants.IMAGE_BASE_URL + book.image else null) {
        crossfade(true)
        placeholder(R.drawable.ic_books)
        error(R.drawable.ic_image)
        fallback(R.drawable.ic_no_image)
    }
}