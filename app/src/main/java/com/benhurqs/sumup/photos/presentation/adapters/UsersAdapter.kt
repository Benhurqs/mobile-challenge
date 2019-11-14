package com.benhurqs.sumup.photos.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.benhurqs.sumup.commons.presentation.adapter.DefaultViewHolder
import com.benhurqs.sumup.commons.presentation.adapter.OnClickItemListener
import com.benhurqs.sumup.splash.domains.entities.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.user_list_item.view.*



class UsersAdapter (private val userList: List<User>, private val listener: OnClickItemListener<User>): RecyclerView.Adapter<DefaultViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DefaultViewHolder(LayoutInflater.from(parent.context).inflate(com.benhurqs.sumup.R.layout.user_list_item, parent, false))

    override fun getItemCount() = userList.size

    override fun onBindViewHolder(holder: DefaultViewHolder, position: Int) {
        val view: View = holder.mView
        val user = userList[position]

        view.user_name.text = user.name

        Glide.with(view.context)
            .load(user.image)
            .apply(RequestOptions.circleCropTransform())
            .into(view.user_photo)

        view.setOnClickListener { listener.onClickItem(user) }
    }
}