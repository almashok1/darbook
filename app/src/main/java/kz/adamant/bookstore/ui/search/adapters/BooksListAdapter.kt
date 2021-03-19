package kz.adamant.bookstore.ui.search.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kz.adamant.bookstore.R
import kz.adamant.bookstore.databinding.BookItemBinding
import kz.adamant.bookstore.models.BookDvo
import kz.adamant.bookstore.utils.Comparators.BOOKS_COMPARATOR
import kz.adamant.bookstore.utils.loadBookImage

class BooksListAdapter(
    private val onBookPressed: (book: BookDvo?) -> Unit
): ListAdapter<BookDvo, BooksListAdapter.BookViewHolder>(BOOKS_COMPARATOR) {

    private var items: List<BookDvo?> = mutableListOf()

    fun setItems(items: List<BookDvo?>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_item, parent, false)
        val binding = BookItemBinding.bind(view)
        return BookViewHolder(binding, onBookPressed)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class BookViewHolder(
        private val binding: BookItemBinding,
        private val onBookPressed: (book: BookDvo?) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(book: BookDvo?) {
            binding.run {
                bookTitle.text = book?.title ?: "No Book"
                bookAuthor.text = book?.author?: ""
                bookImage.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_image))
                bookImage.loadBookImage(book)
                root.setOnClickListener { onBookPressed(book) }
            }
        }
    }
}