package kz.adamant.bookstore.utils

import androidx.recyclerview.widget.DiffUtil
import kz.adamant.bookstore.models.BookDvo

object Comparators {
    val BOOKS_COMPARATOR = object : DiffUtil.ItemCallback<BookDvo>() {
        override fun areItemsTheSame(oldItem: BookDvo, newItem: BookDvo): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: BookDvo, newItem: BookDvo): Boolean {
            return oldItem.title == newItem.title
        }
    }
}