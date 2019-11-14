package com.benhurqs.sumup

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.injection.Injection
import com.benhurqs.sumup.photos.presentation.adapters.UsersAdapter
import com.benhurqs.sumup.splash.domains.entities.User
import com.benhurqs.sumup.user.clients.local.UserLocalDataSource
import com.benhurqs.sumup.user.clients.remote.UserRemoteDataSource
import com.benhurqs.sumup.user.managers.UserRepository
import kotlinx.android.synthetic.main.user_list_content.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.benhurqs.sumup.commons.presentation.adapter.OnClickItemListener
import com.benhurqs.sumup.photos.domains.entities.Album
import com.benhurqs.sumup.photos.domains.entities.Photo
import com.benhurqs.sumup.photos.presentation.adapters.AlbumsAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.user_list_item.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Injection.provideUserRepository(this).getUserList(object: APICallback<List<User>, String>{
            override fun onStart() {
                user_list_progress.visibility = View.VISIBLE
                user_list_content.visibility = View.VISIBLE
                Log.e("start","comecou")
            }

            override fun onError(error: String) {
                user_list_progress.visibility = View.GONE
                Log.e("error","error -> " + error)
            }

            override fun onFinish() {
                user_list_progress.visibility = View.GONE
                Log.e("finish","finish")
            }

            override fun onSuccess(response: List<User>) {
                Log.e("success","chegou - " + response.get(0).name)
                loadUsers(response)
            }
        })

    }

    private fun loadAlbums(userId: Int){
        Injection.provideAlbumRepository(this).getAlbumList(userId, object: APICallback<List<Album>, String>{
            override fun onStart() {
                Log.e("start","comecou")
            }

            override fun onError(error: String) {
                Log.e("error","error -> " + error)
            }

            override fun onFinish() {
                Log.e("finish","finish")
            }

            override fun onSuccess(albumList: List<Album>) {
                album_list.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                album_list.adapter = AlbumsAdapter(albumList, object : OnClickItemListener<Photo>{
                    override fun onClickItem(photo: Photo) {
                        Log.e("Cliquei", "foto")
                        user_list_content.visibility = View.VISIBLE
                    }
                })
            }
        })
    }


    private fun loadUsers(userList: List<User>){
        user_list_close_btn.setOnClickListener {
            Log.e("Cliquei", "fechar")
            user_list_content.visibility = View.GONE
        }

        user_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        user_list.adapter = UsersAdapter(userList, object : OnClickItemListener<User>{
            override fun onClickItem(user: User) {
                Log.e("Cliquei", user.name)
                loadAlbums(user.id)
                user_list_content.visibility = View.GONE


                managerHeader(user)
            }
        })
    }

    private fun managerHeader(userSelected: User){
        user_name.text = userSelected.name

        Glide.with(this@MainActivity)
            .load(userSelected.image)
            .apply(RequestOptions.circleCropTransform())
            .into(user_photo)

        filter.setOnClickListener {
            user_list_content.visibility = View.VISIBLE
        }
    }

}
