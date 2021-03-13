package kz.adamant.bookstore.utils

import androidx.recyclerview.widget.DiffUtil
import kz.adamant.domain.models.Book

object Comparators {
    val BOOKS_COMPARATOR = object : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.title == newItem.title
        }
    }
}