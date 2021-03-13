package kz.adamant.bookstore.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kz.adamant.bookstore.R
import kz.adamant.bookstore.databinding.BookHomeItemBinding
import kz.adamant.bookstore.utils.Comparators.BOOKS_COMPARATOR
import kz.adamant.bookstore.utils.Constants
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.ReadingBook


class HomeBooksAdapter: ListAdapter<Book, HomeBooksAdapter.BookViewHolder>(BOOKS_COMPARATOR) {

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
            .inflate(R.layout.book_home_item, parent, false)
        val binding = BookHomeItemBinding.bind(view)
//        if (type == HOME_VIEW) {
//            val params = binding.root.layoutParams as RecyclerView.LayoutParams
//            params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
//            params.marginEnd = 32
//            binding.root.layoutParams = params
//        }
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class BookViewHolder(private val binding: BookHomeItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book?) {
            binding.run {
                bookTitle.text = book?.title ?: "No Book"
                bookAuthor.text = book?.author ?: ""
                bookImage.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_image))

                if (book != null)
                    bookImage.load(Constants.mockBookUrls[book.id % 3]) {
                        crossfade(true)
                        placeholder(R.drawable.ic_image)
                        error(R.drawable.ic_no_image)
                    }
            }
        }
    }


//    companion object {
//
//        private val READING_BOOKS_COMPARATOR = object : DiffUtil.ItemCallback<ReadingBook>() {
//            override fun areItemsTheSame(oldItem: ReadingBook, newItem: ReadingBook): Boolean {
//                return oldItem === newItem
//            }
//
//            override fun areContentsTheSame(oldItem: ReadingBook, newItem: ReadingBook): Boolean {
//                return oldItem.book?.title == newItem.book?.title
//            }
//        }
//    }

}