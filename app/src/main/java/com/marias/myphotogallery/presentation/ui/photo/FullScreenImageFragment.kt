package com.marias.myphotogallery.presentation.ui.photo

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.marias.myphotogallery.databinding.FullscreenImageFragmentLayoutBinding
import com.marias.myphotogallery.presentation.ui.photolist.PhotoGalleryAdapter.Companion.FULLSCREEN_URL_KEY

class FullScreenImageFragment : Fragment() {
    private var _binding: FullscreenImageFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FullscreenImageFragmentLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.getString(FULLSCREEN_URL_KEY)
        Glide.with(requireContext())
            .load(url)
            .into(binding.fullScreenImageView)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}