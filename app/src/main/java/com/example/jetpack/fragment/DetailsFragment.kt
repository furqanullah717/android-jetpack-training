package com.example.jetpack.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.jetpack.R
import com.example.jetpack.databinding.FragmentDetailsBinding
import com.example.jetpack.utils.getProgresDrawable
import com.example.jetpack.utils.loadImage
import com.example.jetpack.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var view: FragmentDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = DataBindingUtil.inflate<FragmentDetailsBinding>(
            inflater,
            R.layout.fragment_details,
            container,
            false
        )
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel = ViewModelProvider.AndroidViewModelFactory(activity!!.application)
            .create(DetailViewModel::class.java)
        detailViewModel.dogLiveData.observe(this, Observer {

            it?.let {
                this.view.dog = it
            }
            imageView.loadImage(it?.url, getProgresDrawable(imageView.context))
        })

        arguments?.let {
            val id = DetailsFragmentArgs.fromBundle(it).dogUuid
            detailViewModel.fetchFromDb(id)
        }

    }
}
