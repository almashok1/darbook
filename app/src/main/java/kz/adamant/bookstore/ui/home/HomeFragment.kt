package kz.adamant.bookstore.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kz.adamant.bookstore.R
import kz.adamant.bookstore.databinding.FragmentHomeBinding
import kz.adamant.bookstore.ui.home.adapters.HomeBooksAdapter
import kz.adamant.bookstore.utils.BindingFragment
import kz.adamant.bookstore.utils.sharedGraphViewModel
import kz.adamant.bookstore.viewmodels.HomeViewModel
import kz.adamant.domain.models.Resource
import kz.adamant.domain.utils.TOP_N

class HomeFragment: BindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel by sharedGraphViewModel<HomeViewModel>(R.id.nav_graph)

    private val readingBooksAdapter = HomeBooksAdapter()
    private val newlyAddedAdapter = HomeBooksAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            nowReadingRecyclerView.adapter = readingBooksAdapter
            nowReadingRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


            newlyAddedRecyclerView.adapter = newlyAddedAdapter
            newlyAddedRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        setUpSwipeRefresh()
        observeReadingBooks()
        observeNewlyAdded()
        binding.seeAllNewlyAdded.setOnClickListener { setSeeAllButton(AllBooksFragment.NEWLY_ADDED) }
        binding.seeAllNowReading.setOnClickListener { setSeeAllButton(AllBooksFragment.NOW_READING) }
    }


    private fun observeReadingBooks() {
        viewModel.allReadingBooks.observe(viewLifecycleOwner) {
            it.data?.let { items ->
                readingBooksAdapter.setItems(items)
                if (items.isEmpty()) binding.noItemsNowReading.visibility = View.VISIBLE
                else binding.noItemsNowReading.visibility = View.INVISIBLE
                showSeeAllButtonIfNeeded(binding.seeAllNowReading, items.size)
            }
            when(it) {
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
                    showErrorSnackbar(it.throwable.localizedMessage ?: "")
                }
                else -> {}
            }
        }
    }

    private fun observeNewlyAdded() {
        viewModel.topBooksNewlyAdded.observe(viewLifecycleOwner) {
            it.data?.let { items ->
                newlyAddedAdapter.setItems(items)
                if (items.isEmpty()) binding.noItemsNewlyAdded.visibility = View.VISIBLE
                else binding.noItemsNewlyAdded.visibility = View.INVISIBLE
                showSeeAllButtonIfNeeded(binding.seeAllNewlyAdded, items.size)
            }
            when(it) {
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
                    showErrorSnackbar(it.throwable.localizedMessage ?: "")
                }
                else -> {}
            }
        }
    }

    private fun showSeeAllButtonIfNeeded(seeAllBtn: Button, size: Int) {
        if (size >= TOP_N) {
            seeAllBtn.visibility = View.VISIBLE
        } else {
            seeAllBtn.visibility = View.INVISIBLE
        }
    }

    private fun setSeeAllButton(booksType: Int) {
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