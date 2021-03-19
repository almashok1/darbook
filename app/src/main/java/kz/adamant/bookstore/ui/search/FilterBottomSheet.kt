package kz.adamant.bookstore.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.adamant.bookstore.R
import kz.adamant.bookstore.databinding.FragmentFilterBottomSheetBinding
import kz.adamant.bookstore.ui.search.adapters.FilterDialogAdapter
import kz.adamant.bookstore.utils.sharedGraphViewModel
import kz.adamant.bookstore.viewmodels.SearchViewModel

class FilterBottomSheet: BottomSheetDialogFragment() {

    private var _binding: FragmentFilterBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { FilterDialogAdapter(viewModel.selectedGenresId) }
    private val viewModel by sharedGraphViewModel<SearchViewModel>(R.id.nav_graph)

    private var alreadyApplied = false

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
        binding.run {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
            applyFilterBtn.setOnClickListener { onApplyFilterThenNavigate() }
        }
        binding.recyclerView.post {
            observeVm()
        }
    }

    private fun observeVm() {
        viewModel.genres.observe(viewLifecycleOwner) { result ->
            binding.progressBar.show()
            result.data?.let { adapter.setItems(it) }
            binding.progressBar.hide()
        }
    }

    private fun onApplyFilter() {
        alreadyApplied = true
        viewModel.postSelectedGenresId(viewModel.selectedGenresId)
    }

    private fun onApplyFilterThenNavigate() {
        onApplyFilter()
        findNavController().popBackStack(R.id.searchFragment, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!alreadyApplied) onApplyFilter()
        _binding = null
    }
}