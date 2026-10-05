package com.getit.getit.yes.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.getit.getit.yes.R
import com.getit.getit.yes.account.AccountActivity
import com.getit.getit.yes.data.UserRepository
import com.getit.getit.yes.databinding.FragmentMainHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentMainHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.watchVideo.setOnClickListener { playVideo() }
        binding.profileCard.setOnClickListener {
            startActivity(Intent(requireContext(), AccountActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycleScope.launch {
            val profile = runCatching { UserRepository.profile() }.getOrNull()
            _binding?.let {
                it.displayName.text = profile?.name?.takeIf(String::isNotBlank) ?: getString(R.string.profile_name_missing)
                it.place.text = profile?.place.orEmpty()
                val photo = profile?.photo?.let { bytes -> BitmapFactory.decodeByteArray(bytes, 0, bytes.size) }
                if (photo != null) it.profilePic.setImageBitmap(photo) else it.profilePic.setImageResource(R.drawable.user)
            }
        }
    }

    private fun playVideo() {
        val app = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$VIDEO_ID"))
        try {
            startActivity(app)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$VIDEO_ID")))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val VIDEO_ID = "3_c6o6mJaTg"
    }
}
