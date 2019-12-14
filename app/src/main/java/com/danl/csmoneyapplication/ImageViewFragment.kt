package com.danl.csmoneyapplication


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_image_view.*

/**
 * A simple [Fragment] subclass.
 */
class ImageViewFragment : Fragment() {

    companion object {

        fun newInstance(count: Int): ImageViewFragment {
            val fragment = ImageViewFragment()
            val args = Bundle()
            args.putInt("position", count)
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when {
            arguments?.getInt("position") == 0 -> {
                imageView.setImageResource(R.drawable.statistics1)
            }
            arguments?.getInt("position") == 1 -> {
                imageView.setImageResource(R.drawable.statistics2)
            }
            arguments?.getInt("position") == 2 -> {
                imageView.setImageResource(R.drawable.statistics3)
            }
            else -> {
                imageView.setImageResource(R.drawable.statistics4)
            }
        }
    }


}
