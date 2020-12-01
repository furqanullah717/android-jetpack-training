package com.example.jetpack.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.jetpack.R
import com.example.jetpack.adapters.DogsListAdapter
import com.example.jetpack.viewmodel.DogListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var viewModle: DogListViewModel
    private var dogsAdapter: DogsListAdapter = DogsListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModle = ViewModelProvider.AndroidViewModelFactory(activity!!.application)
            .create(DogListViewModel::class.java)
        viewModle.refresh()
        dogList.apply {
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
            adapter = dogsAdapter
        }
        observViewModel();
        swiper.setOnRefreshListener {
            dogList.visibility = View.VISIBLE
            errorView.visibility = View.GONE
            viewModle.refresh()
            swiper.isRefreshing = false
        }
    }

    private fun observViewModel() {
        viewModle.dogs.observe(this, Observer {
            it?.let {
                dogsAdapter.updateList(it)
                dogList.visibility = View.VISIBLE
            }
        })
        viewModle.loading.observe(this, Observer {
            it?.let {
                val v: Int
                if (it) {
                    dogList.visibility = View.GONE
                    errorView.visibility = View.GONE

                    v = View.VISIBLE
                } else
                    v = View.INVISIBLE
                progressBar.visibility = v
            }
        })
        viewModle.dogLoadError.observe(this, Observer {
            it?.let {
                val v: Int
                if (it)
                    v = View.VISIBLE
                else
                    v = View.INVISIBLE
                errorView.visibility = v
            }
        })

    }
}
