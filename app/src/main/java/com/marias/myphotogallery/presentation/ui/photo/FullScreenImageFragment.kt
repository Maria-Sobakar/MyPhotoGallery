package com.marias.myphotogallery.presentation.ui.photo

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.marias.myphotogallery.R
import com.marias.myphotogallery.databinding.FullscreenImageFragmentLayoutBinding
import com.marias.myphotogallery.presentation.ui.photolist.PhotoGalleryAdapter.Companion.FULLSCREEN_URL_KEY
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class FullScreenImageFragment : Fragment() {
    private var _binding: FullscreenImageFragmentLayoutBinding? = null
    private val binding get() = _binding!!
    private var isShowed = false
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fullscreen_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
                val urlString = arguments?.getString(FULLSCREEN_URL_KEY)
                if (urlString != null) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    } else {
                        downloadImageNew(urlString)
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

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
        binding.fullscreenProgressBar.isVisible = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.getString(FULLSCREEN_URL_KEY)
        Glide.with(requireContext())
            .load(url)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Snackbar.make(
                        binding.fullScreenCoordinatorLayout,
                        requireContext().getText(R.string.error),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.fullscreenProgressBar.isVisible = false
                    return false
                }

            })
            .into(binding.fullScreenImageView)
        binding.fullScreenImageView.setOnClickListener {
            if (isShowed) {
                (activity as AppCompatActivity).supportActionBar?.hide()
                isShowed = isShowed.not()
            } else {
                (activity as AppCompatActivity).supportActionBar?.show()
                isShowed = isShowed.not()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun downloadImageNew(downloadUrlOfImage: String) {
        try {
            val filename = System.currentTimeMillis().toString()
            val dm = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadUri: Uri = Uri.parse(downloadUrlOfImage)
            val request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + filename + ".jpg"
                )
            dm.enqueue(request)
            Snackbar.make(
                binding.fullScreenCoordinatorLayout,
                requireContext().getText(R.string.image_downloading_started),
                Snackbar.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Snackbar.make(
                binding.fullScreenCoordinatorLayout,
                requireContext().getText(R.string.error),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

}