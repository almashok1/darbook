package kz.adamant.bookstore.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kz.adamant.bookstore.R
import kz.adamant.bookstore.databinding.BookItemBinding
import kz.adamant.domain.models.Book

class BooksListAdapter: ListAdapter<Book, BooksListAdapter.BookViewHolder>(WORDS_COMPARATOR) {

    private var items: List<Book> = mutableListOf()

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
        fun bind(book: Book) {
            binding.run {
                bookName.text = book.title
                bookImage.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_image))

//                if (book.imageUrl != null)
//                    bookImage.load(book.imageUrl) {
//                        crossfade(true)
//                        placeholder(R.drawable.ic_image)
//                        error(R.drawable.ic_no_image)
//                    }
//                else
//                    bookImage.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_image))
            }
        }
    }


    companion object {
        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }

}