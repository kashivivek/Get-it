package com.getit.getit.yes.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

/** Static category grid; card clicks are routed to [MainHomeActivity.openMapsMarker]. */
class SectionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(requireArguments().getInt(ARG_LAYOUT), container, false)

    companion object {
        private const val ARG_LAYOUT = "layout"

        fun newInstance(@LayoutRes layout: Int) = SectionFragment().apply {
            arguments = bundleOf(ARG_LAYOUT to layout)
        }
    }
}
