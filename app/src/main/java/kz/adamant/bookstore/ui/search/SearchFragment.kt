package kz.adamant.bookstore.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kz.adamant.bookstore.App
import kz.adamant.bookstore.R
import kz.adamant.bookstore.databinding.FragmentSearchBinding
import kz.adamant.bookstore.ui.search.adapters.BooksListAdapter
import kz.adamant.bookstore.utils.BindingFragment
import kz.adamant.bookstore.utils.viewModel
import kz.adamant.bookstore.viewmodels.SearchViewModel
import kz.adamant.bookstore.viewmodels.SearchViewModelFactory
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Resource

class SearchFragment: BindingFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate),
    SearchView.OnQueryTextListener {
    private lateinit var viewModel: SearchViewModel

    private lateinit var adapter: BooksListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = BooksListAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initVm()
        setUpRecyclerView()
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


    private fun initVm() {
        val app = requireActivity().application as App
        val factory = SearchViewModelFactory(
            getAllBooksUseCase = app.getAllBooksUseCase,
            getAllGenresUseCase = app.getAllGenresUseCase
        )
        viewModel = navController.viewModel(R.id.nav_graph, factory)
    }

    private fun setUpRecyclerView() {
        binding.run {
            bookRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            bookRecyclerView.adapter = adapter
        }
    }

    private fun setUpFabFilter() {
        binding.fabFilter.setOnClickListener {
            navController.navigate(R.id.action_searchFragment_to_filterBottomSheet)
        }
    }

    private fun observeBooks() {
        viewModel.allBooks.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Resource.Success -> {
                    setItems(result.data).also { hideProgressBar() }
                }
                is Resource.Loading -> {
                    showProgressBar()
                    if (result.localData != null) {
                        setItems(result.localData!!)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    showErrorSnackbar(result.throwable.localizedMessage ?: "")
                    result.data?.let { setItems(it) }
                }
                else -> {}
            }
        }
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

    private fun hideProgressBar() {
        binding.progressBar.apply {
            hide()
        }
    }

    private fun setItems(data: List<Book>) {
        adapter.setItems(data)
    }

    private fun showErrorSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.error))
            .show()
    }
}