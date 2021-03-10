package com.marias.myphotogallery.presentation.ui.photolist


import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.marias.myphotogallery.R
import com.marias.myphotogallery.databinding.PhotoGalleryFragmentLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PhotoGalleryFragment : Fragment() {
    private var _binding: PhotoGalleryFragmentLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PhotoGalleryViewModel by activityViewModels()

    @Inject
    lateinit var adapter: PhotoGalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(queryText: String): Boolean {
                    viewModel.loadSearchedPhotos(queryText)
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(queryText: String): Boolean {
                    return false
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clear -> {
                viewModel.loadPhotos()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PhotoGalleryFragmentLayoutBinding.inflate(layoutInflater, container, false)
        val recyclerView = binding.photoRecyclerView
        val emptyTextView = binding.photoListTextView
        val orientation = requireActivity().resources.configuration.orientation
        val spanCount = if (orientation == Configuration.ORIENTATION_PORTRAIT) 3 else 5
        recyclerView.layoutManager = GridLayoutManager(requireContext(), spanCount)
        recyclerView.adapter = adapter
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                recyclerView.isVisible = false
                emptyTextView.isVisible = true
            } else {
                recyclerView.isVisible = true
                emptyTextView.isVisible = false
            }
        }
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.photoListProgressBar.isVisible = loadStates.refresh is LoadState.Loading

                if (loadStates.refresh is LoadState.Error) {
                    binding.errorLinearLayout.isVisible = true
                    Snackbar.make(
                        binding.photoListCoordinatorLayout,
                        requireContext().getText(R.string.network_error),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.photosLiveData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
        binding.retryButton.setOnClickListener { viewModel.loadPhotos() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}