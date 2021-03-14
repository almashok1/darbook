package kz.adamant.bookstore.ui.search.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kz.adamant.bookstore.R
import kz.adamant.bookstore.databinding.BookItemBinding
import kz.adamant.bookstore.utils.Comparators.BOOKS_COMPARATOR
import kz.adamant.bookstore.utils.Constants
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.ReadingBook

class BooksListAdapter: ListAdapter<Book, BooksListAdapter.BookViewHolder>(BOOKS_COMPARATOR) {

    private var items: List<Book?> = mutableListOf()

    @JvmName("setReadingBookItems")
    fun setItems(items: List<ReadingBook>) {
        this.items = items.map { it.book }
        notifyDataSetChanged()
    }

    fun setItems(items: List<Book>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_item, parent, false)
        val binding = BookItemBinding.bind(view)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class BookViewHolder(private val binding: BookItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book?) {
            binding.run {
                bookTitle.text = book?.title ?: "No Book"
                bookAuthor.text = book?.author?: ""
                bookImage.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_image))

                bookImage.load(if (book != null) Constants.mockBookUrls[book.id % 3] else null) {
                    crossfade(true)
                    placeholder(R.drawable.ic_books)
                    error(R.drawable.ic_image)
                    fallback(R.drawable.ic_no_image)
                }
            }
        }
    }
}