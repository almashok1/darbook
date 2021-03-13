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

class FilterDialogAdapter(
    private val selectedGenresId: HashSet<Int>
): ListAdapter<Genre, FilterDialogAdapter.ViewHolder>(GENRE_COMPARATOR)  {

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
        return ViewHolder(binding, selectedGenresId)
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
        private val selectedGenresId: HashSet<Int>
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: Genre) {
            binding.run {
                updateItem(genre)
                genreTitle.text = genre.title
                root.setOnClickListener {
                    if (selectedGenresId.contains(genre.id))
                        selectedGenresId.remove(genre.id)
                    else
                        selectedGenresId.add(genre.id)
                    updateItem(genre)
                }
            }
        }

        private fun updateItem(genre: Genre) {
            binding.run {
                if (!selectedGenresId.contains(genre.id)) {
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
                return oldItem.id == newItem.id
            }
        }
    }
}