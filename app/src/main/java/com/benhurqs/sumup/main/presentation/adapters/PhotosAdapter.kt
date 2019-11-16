package com.benhurqs.sumup.main.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.benhurqs.sumup.R
import com.benhurqs.sumup.commons.presentation.adapter.DefaultViewHolder
import com.benhurqs.sumup.main.domains.entities.Photo
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.photo_list_item.view.*

class PhotosAdapter (private val photoList: List<Photo>): RecyclerView.Adapter<DefaultViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DefaultViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_list_item, parent, false))

    override fun getItemCount() = photoList.size

    override fun onBindViewHolder(holder: DefaultViewHolder, position: Int) {
        val view: View = holder.mView
        val photo = photoList[position]

        Glide.with(view.context)
            .load(photo.url)
            .centerCrop()
            .into(view.photo)

    }
}