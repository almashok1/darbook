package kz.adamant.bookstore.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import kz.adamant.bookstore.R
import kz.adamant.bookstore.databinding.FragmentSearchBinding
import kz.adamant.bookstore.ui.search.adapters.BooksListAdapter
import kz.adamant.bookstore.utils.BindingFragment
import kz.adamant.bookstore.utils.sharedGraphViewModel
import kz.adamant.bookstore.viewmodels.SearchViewModel
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Resource

class SearchFragment: BindingFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate),
    SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    private val viewModel by sharedGraphViewModel<SearchViewModel>(R.id.nav_graph)

    private lateinit var adapter: BooksListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = BooksListAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setUpRecyclerView()
        setUpSwipeRefresh()
        setUpFabFilter()
        observeBooks()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search) ?: return
        val searchView = search.actionView as SearchView
        searchView.setOnCloseListener { true }
        val searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        searchPlate.hint = "Search"
        val searchPlateView: View =
            searchView.findViewById(androidx.appcompat.R.id.search_plate)
        searchPlateView.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    private fun setUpRecyclerView() {
        binding.run {
            bookRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            bookRecyclerView.adapter = adapter
        }
    }

    private fun setUpSwipeRefresh() {
        binding.swipeRefreshBooks.setOnRefreshListener(this)
    }

    private fun setUpFabFilter() {
        binding.fabFilter.setOnClickListener {
            navController.navigate(R.id.action_searchFragment_to_filterBottomSheet)
        }
    }

    private fun observeBooks() {
        viewModel.allBooks.observe(viewLifecycleOwner) { result ->
            result.data?.let { setItems(it) }
            when(result) {
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
                    showErrorSnackbar(result.throwable.localizedMessage ?: "")
                }
                else -> {}
            }
        }
    }

    override fun onRefresh() {
        viewModel.setForceRefresh()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchQuery(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchQuery(newText)
        return true
    }

    private fun searchQuery(query: String?) {
        viewModel.query.value = query
    }

    private fun showProgressBar() {
        binding.progressBar.apply {
            show()
        }
    }

    private fun setRefreshing(value: Boolean) {
        binding.run {
            if (swipeRefreshBooks.isRefreshing != value)
                swipeRefreshBooks.isRefreshing = value
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.apply {
            hide()
        }
    }

    private fun setItems(data: List<Book>) {
        adapter.setItems(data)
    }
}