package kz.adamant.bookstore.ui.search.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kz.adamant.bookstore.R
import kz.adamant.bookstore.databinding.ItemFilterBottomSheetBinding
import kz.adamant.domain.models.Genre

class FilterDialogAdapter(): ListAdapter<Genre, FilterDialogAdapter.ViewHolder>(GENRE_COMPARATOR)  {

    private var items: List<Genre> = mutableListOf()

    fun setItems(items: List<Genre>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun getItems() = items

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter_bottom_sheet, parent, false)
        val binding = ItemFilterBottomSheetBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = items[position]
        holder.bind(genre)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(
        private val binding: ItemFilterBottomSheetBinding,
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: Genre) {
            binding.run {
                updateItem(genre)
                genreTitle.text = genre.title
                root.setOnClickListener {
                    genre.selected = !genre.selected
                    updateItem(genre)
                }
            }
        }

        private fun updateItem(genre: Genre) {
            binding.run {
                if (!genre.selected) {
                    card.setCardBackgroundColor(ContextCompat.getColor(root.context, android.R.color.transparent))
                    genreTitle.setTextColor(ContextCompat.getColor(root.context, android.R.color.tab_indicator_text))
                } else {
                    card.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.teal_200))
                    genreTitle.setTextColor(ContextCompat.getColor(root.context, R.color.black))
                }
            }
        }
    }



    companion object {
        private val GENRE_COMPARATOR = object : DiffUtil.ItemCallback<Genre>() {
            override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }
}