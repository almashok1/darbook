package kz.adamant.bookstore.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kz.adamant.bookstore.NavGraphDirections
import kz.adamant.bookstore.R
import kz.adamant.bookstore.databinding.FragmentHomeBinding
import kz.adamant.bookstore.models.BookDvo
import kz.adamant.bookstore.ui.home.adapters.HomeBooksAdapter
import kz.adamant.bookstore.utils.BindingFragment
import kz.adamant.bookstore.utils.sharedGraphViewModel
import kz.adamant.bookstore.viewmodels.HomeViewModel
import kz.adamant.domain.models.Resource
import kz.adamant.domain.utils.TOP_N

class HomeFragment: BindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel by sharedGraphViewModel<HomeViewModel>(R.id.nav_graph)

    private val readingBooksAdapter = HomeBooksAdapter(::onBookPressed)
    private val newlyAddedAdapter = HomeBooksAdapter(::onBookPressed)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            nowReadingRecyclerView.adapter = readingBooksAdapter
            newlyAddedRecyclerView.adapter = newlyAddedAdapter
        }

        setUpSwipeRefresh()
        observeReadingBooks()
        observeNewlyAdded()
        binding.seeAllNewlyAdded.setOnClickListener { onClickSeeAllButton(AllBooksFragment.NEWLY_ADDED) }
        binding.seeAllNowReading.setOnClickListener { onClickSeeAllButton(AllBooksFragment.NOW_READING) }
    }

    private fun setResourceItems(
        resource: Resource<List<BookDvo?>>,
        booksType: Int,
        onSetItems: (List<BookDvo?>) -> Unit
    ) {
        resource.data?.let { items ->
            onSetItems(items.take(TOP_N))
            applyVisibilities(items.size, booksType)
        }
        when(resource) {
            is Resource.Success -> {
                setRefreshing(false)
                hideProgressBar()
            }
            is Resource.Loading -> {
                showProgressBar()
            }
            is Resource.Error -> {
                hideProgressBar()
                setRefreshing(false)
                showErrorSnackbar(resource.throwable.localizedMessage ?: "")
            }
            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setForceRefresh()
    }

    private fun observeReadingBooks() {
        viewModel.userReadingBooks.observe(viewLifecycleOwner) {
            setResourceItems(it, AllBooksFragment.NOW_READING) { books ->
                readingBooksAdapter.setItems(books)
            }
        }
    }

    private fun observeNewlyAdded() {
        viewModel.newlyAddedBooks.observe(viewLifecycleOwner) {
            setResourceItems(it, AllBooksFragment.NEWLY_ADDED) { books ->
                newlyAddedAdapter.setItems(books)
            }
        }
    }

    private fun onBookPressed(book: BookDvo?) {
        if (book == null) return
        navController.navigate(NavGraphDirections.actionGlobalBookDetailFragment(book))
    }

    private fun applyVisibilities(size: Int, booksType: Int) {
        val noItemView = getNoItemView(booksType)
        if (size == 0) noItemView.visibility = View.VISIBLE
        else noItemView.visibility = View.INVISIBLE

        setSeeAllButtonVisibility(binding.seeAllNowReading, size)
    }

    private fun getNoItemView(booksType: Int): View {
        return if (booksType == AllBooksFragment.NEWLY_ADDED) binding.noItemsNewlyAdded
        else binding.noItemsNowReading
    }

    private fun setSeeAllButtonVisibility(seeAllBtn: Button, size: Int) {
        if (size > TOP_N) {
            seeAllBtn.visibility = View.VISIBLE
        } else {
            seeAllBtn.visibility = View.INVISIBLE
        }
    }

    private fun onClickSeeAllButton(booksType: Int) {
        navController.navigate(HomeFragmentDirections.actionHomeFragmentToAllBooksFragment(booksType))
    }

    private fun setUpSwipeRefresh() {
        binding.swipeRefreshBooks.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        viewModel.setForceRefresh()
    }

    private fun setRefreshing(value: Boolean) {
        binding.run {
            if (swipeRefreshBooks.isRefreshing != value)
                swipeRefreshBooks.isRefreshing = value
        }
    }

    private fun showProgressBar() {
        binding.progressBar.apply {
            show()
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.apply {
            hide()
        }
    }

}