package com.example.jetpack.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpack.R
import com.example.jetpack.databinding.DogItemBinding
import com.example.jetpack.fragment.ListFragmentDirections
import com.example.jetpack.model.DogBreed

class DogsListAdapter(var dogList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogsListAdapter.DogsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogsViewHolder {

        val v = DataBindingUtil.inflate<DogItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.dog_item,
            parent,
            false
        );
        return DogsViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dogList.size
    }

    override fun onBindViewHolder(holder: DogsViewHolder, position: Int) {
        holder.view.dog = dogList[position]
        holder.view.clickListener = object : DogClickListner {
            override fun onItemClick(v: View, id: Long) {
                val action: NavDirections = ListFragmentDirections.actionDetailsFragment(id)
                Navigation.findNavController(v).navigate(action)
            }
        }
    }

    fun updateList(it: List<DogBreed>) {
        dogList = it as ArrayList<DogBreed>
        notifyDataSetChanged()

    }

    interface DogClickListner {
        fun onItemClick(v: View, id: Long)
    }

    class DogsViewHolder(var view: DogItemBinding) : RecyclerView.ViewHolder(view.root) {

    }

}