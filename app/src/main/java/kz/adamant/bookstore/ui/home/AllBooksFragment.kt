package kz.adamant.bookstore.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kz.adamant.bookstore.R
import kz.adamant.bookstore.databinding.FragmentAllBooksBinding
import kz.adamant.bookstore.ui.search.adapters.BooksListAdapter
import kz.adamant.bookstore.utils.BindingFragment
import kz.adamant.bookstore.utils.observeOnce
import kz.adamant.bookstore.utils.sharedGraphViewModel
import kz.adamant.bookstore.viewmodels.AllBooksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AllBooksFragment: BindingFragment<FragmentAllBooksBinding>(FragmentAllBooksBinding::inflate) {

    private val args by navArgs<AllBooksFragmentArgs>()

    companion object {
        const val NOW_READING = 1
        const val NEWLY_ADDED = 2
    }

    private val viewModel by viewModel<AllBooksViewModel>(
        parameters = { parametersOf(args.booksType) }
    )

    private val adapter = BooksListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observeBooks()
    }

    private fun setUpRecyclerView() {
        binding.run {
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            recyclerView.adapter = adapter
        }
    }

    private fun observeBooks() {
        if (args.booksType == NOW_READING)
            viewModel.readingBooks.observeOnce(viewLifecycleOwner) {
                it.data?.let { items ->
                    adapter.setItems(items)
                }
            }
        else if (args.booksType == NEWLY_ADDED) {
            viewModel.newlyAdded.observeOnce(viewLifecycleOwner) {
                it.data?.let { items ->
                    adapter.setItems(items)
                }
            }
        }
    }
}