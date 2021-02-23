package kz.adamant.bookstore.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kz.adamant.bookstore.App
import kz.adamant.bookstore.databinding.FragmentHomeBinding
import kz.adamant.bookstore.utils.BindingFragment

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
        val repository = app.booksRepository
        val factory = HomeViewModelFactory(repository)

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
        viewModel.allBooks.observe(viewLifecycleOwner) {
            adapter.setItems(it)
        }
    }
}