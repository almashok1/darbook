package kz.adamant.bookstore.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kz.adamant.bookstore.App
import kz.adamant.bookstore.R
import kz.adamant.bookstore.databinding.FragmentHomeBinding
import kz.adamant.bookstore.utils.BindingFragment
import kz.adamant.bookstore.viewmodels.HomeViewModel
import kz.adamant.bookstore.viewmodels.HomeViewModelFactory
import kz.adamant.domain.models.Book
import kz.adamant.domain.models.Resource

class HomeFragment: BindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private lateinit var viewModel: HomeViewModel

    private lateinit var adapter: BooksListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = BooksListAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVm()
        setUpRecyclerView()
        observeBooks()
    }

    private fun initVm() {
        val app = requireActivity().application as App
        val getAllBooksUseCase = app.getAllBooksUseCase
        val factory = HomeViewModelFactory(
                getAllBooksUseCase = getAllBooksUseCase
        )

        viewModel = ViewModelProvider(this, factory)
            .get(HomeViewModel::class.java)
    }

    private fun setUpRecyclerView() {
        binding.run {
            bookRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
            bookRecyclerView.adapter = adapter
        }
    }

    private fun observeBooks() {
        viewModel.allBooks.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Resource.Success -> setItems(result.data).also { hideProgressBar() }
                is Resource.Loading -> {
                    showProgressBar()
                    if (result.localData != null) {
                        setItems(result.localData!!)
                    }
                }
                is Resource.Error -> hideProgressBar().also{ showErrorSnackbar(result.message) }
                else -> {}
            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.apply {
            show()
        }
    }

    private fun setItems(data: List<Book>) {
        adapter.setItems(data)
    }

    private fun hideProgressBar() {
        binding.progressBar.apply {
            hide()
        }

    }

    private fun showErrorSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.error))
            .show()
    }
}