package kz.adamant.bookstore.ui.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kz.adamant.bookstore.NavGraphDirections
import kz.adamant.bookstore.R
import kz.adamant.bookstore.databinding.FragmentAllBooksBinding
import kz.adamant.bookstore.models.BookDvo
import kz.adamant.bookstore.ui.MainActivity
import kz.adamant.bookstore.ui.search.adapters.BooksListAdapter
import kz.adamant.bookstore.utils.BindingFragment
import kz.adamant.bookstore.utils.setEqualSpacing
import kz.adamant.bookstore.utils.sharedGraphViewModel
import kz.adamant.bookstore.viewmodels.HomeViewModel
import kz.adamant.domain.models.Resource


class AllBooksFragment: BindingFragment<FragmentAllBooksBinding>(FragmentAllBooksBinding::inflate) {

    private val args by navArgs<AllBooksFragmentArgs>()

    companion object {
        const val NOW_READING = 1
        const val NEWLY_ADDED = 2
    }

    private val viewModel by sharedGraphViewModel<HomeViewModel>(R.id.nav_graph)

    private val adapter = BooksListAdapter(::onBookPressed)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar = (requireActivity() as MainActivity).supportActionBar
        when (args.booksType) {
            NOW_READING -> actionBar?.title = getString(R.string.now_reading)
            NEWLY_ADDED -> actionBar?.title = getString(R.string.newly_added)
        }
        setUpRecyclerView()
        observeBooks()
    }

    private fun setUpRecyclerView() {
        binding.run {
            recyclerView.layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            recyclerView.adapter = adapter

            val spacing = resources.getDimensionPixelSize(R.dimen.recyclerSpacing) / 2

            recyclerView.setEqualSpacing(spacing)
        }
    }

    private fun observeBooks() {
        if (args.booksType == NOW_READING)
            viewModel.userReadingBooks.observe(viewLifecycleOwner) { setResourceItems(it) }
        else if (args.booksType == NEWLY_ADDED) {
            viewModel.newlyAddedBooks.observe(viewLifecycleOwner) { setResourceItems(it) }
        }
    }

    private fun setResourceItems(resource: Resource<List<BookDvo?>>) {
        resource.data?.let { items ->
            adapter.setItems(items)
        }
    }

    private fun onBookPressed(book: BookDvo?) {
        if (book == null) return
        navController.navigate(NavGraphDirections.actionGlobalBookDetailFragment(book))
    }
}