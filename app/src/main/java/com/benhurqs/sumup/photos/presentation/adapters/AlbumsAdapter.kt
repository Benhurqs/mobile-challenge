package com.benhurqs.sumup.photos.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benhurqs.sumup.R
import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.commons.presentation.adapter.DefaultViewHolder
import com.benhurqs.sumup.commons.presentation.adapter.OnClickItemListener
import com.benhurqs.sumup.injection.Injection
import com.benhurqs.sumup.photos.domains.entities.Album
import com.benhurqs.sumup.photos.domains.entities.Photo
import kotlinx.android.synthetic.main.album_content.view.*

class AlbumsAdapter (private val albumList: List<Album>, private val listener: OnClickItemListener<Photo>): RecyclerView.Adapter<DefaultViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DefaultViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.album_content, parent, false))

    override fun getItemCount() = albumList.size

    override fun onBindViewHolder(holder: DefaultViewHolder, position: Int) {
        val view: View = holder.mView
        val album = albumList[position]

        view.album_title.text = album.title

        Injection.providePhotoRepository(view.context).getPhotoList(album.userId, object:
            APICallback<List<Photo>, String> {
            override fun onStart() {
                Log.e("start","comecou")
            }

            override fun onError(error: String) {
                Log.e("error","error -> " + error)
            }

            override fun onFinish() {
                Log.e("finish","finish")
            }

            override fun onSuccess(photoList: List<Photo>) {
                view.photo_list.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
                view.photo_list.adapter = PhotosAdapter(photoList, listener)
            }
        })

    }
}