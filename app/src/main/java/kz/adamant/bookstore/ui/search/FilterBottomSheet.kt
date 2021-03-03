package kz.adamant.bookstore.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kz.adamant.bookstore.App
import kz.adamant.bookstore.R
import kz.adamant.bookstore.databinding.FragmentFilterBottomSheetBinding
import kz.adamant.bookstore.ui.search.adapters.FilterDialogAdapter
import kz.adamant.bookstore.viewmodels.SearchViewModel
import kz.adamant.bookstore.viewmodels.SearchViewModelFactory
import kz.adamant.domain.models.Resource

class FilterBottomSheet: BottomSheetDialogFragment() {

    private var _binding: FragmentFilterBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val adapter = FilterDialogAdapter()
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_filter_bottom_sheet, container, false)
        _binding = FragmentFilterBottomSheetBinding.bind(view)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVm()
        binding.run {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
            applyFilterBtn.setOnClickListener { onApplyFilter() }
        }
        binding.recyclerView.post {
            observeVm()
        }
    }

    private fun observeVm() {
        viewModel.genres.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Error -> {
                    binding.progressBar.hide()
                    showErrorSnackbar(it.message)
                }
                is Resource.Loading -> binding.progressBar.show()
                is Resource.Success -> {
                    adapter.setItems(it.data).also { binding.progressBar.hide() }
                }
                else -> {}
            }
        }
    }

    private fun onApplyFilter() {
        val genres = adapter.getItems()
        val selectedGenres = genres.filter { it.selected }
        viewModel.setSelectedGenres(selectedGenres)
        findNavController().navigate(R.id.action_filterBottomSheet_to_searchFragment)
    }

    private fun initVm() {
        val app = requireActivity().application as App
        val factory = SearchViewModelFactory(
            getAllBooksUseCase = app.getAllBooksUseCase,
            getAllGenresUseCase = app.getAllGenresUseCase
        )

        viewModel = ViewModelProvider(requireActivity(), factory)
            .get(SearchViewModel::class.java)
    }

    private fun showErrorSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.error))
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}