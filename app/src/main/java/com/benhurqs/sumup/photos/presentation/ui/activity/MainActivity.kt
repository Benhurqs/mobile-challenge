package com.benhurqs.sumup.photos.presentation.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.benhurqs.sumup.commons.data.APICallback
import com.benhurqs.sumup.injection.Injection
import com.benhurqs.sumup.photos.presentation.adapters.UsersAdapter
import com.benhurqs.sumup.photos.domains.entities.User
import kotlinx.android.synthetic.main.user_list_content.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.benhurqs.sumup.R
import com.benhurqs.sumup.commons.presentation.adapter.OnClickItemListener
import com.benhurqs.sumup.photos.domains.entities.Album
import com.benhurqs.sumup.photos.domains.entities.Photo
import com.benhurqs.sumup.photos.presentation.adapters.AlbumsAdapter
import com.benhurqs.sumup.photos.presentation.ui.contracts.AlbumContract
import com.benhurqs.sumup.photos.presentation.ui.contracts.MainView
import com.benhurqs.sumup.photos.presentation.ui.contracts.UserContract
import com.benhurqs.sumup.photos.presentation.ui.presenter.AlbumPresenter
import com.benhurqs.sumup.photos.presentation.ui.presenter.UserPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header.*


class MainActivity : AppCompatActivity(), MainView, UserContract.View, AlbumContract.View {

    private var userPresenter: UserContract.Presenter? = null
    private var albumPresenter: AlbumContract.Presenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userPresenter = UserPresenter(this, this, Injection.provideUserRepository(this))
        albumPresenter = AlbumPresenter(this, this, Injection.provideAlbumRepository(this))

        user_list_close_btn.setOnClickListener {
            hideUserContent()
        }
    }

    override fun onStart() {
        super.onStart()
        userPresenter?.callUserAPI()
    }

    override fun showLoading() {
        progress.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun isAdded() = !this.isFinishing

    override fun showUserLoading() {
        user_list_progress.visibility = View.VISIBLE
    }

    override fun hideUserLoading() {
        user_list_progress.visibility = View.GONE
    }

    override fun hideUserContent() {
        user_list_content.visibility = View.GONE
    }

    override fun showUserContent() {
        user_list_content.visibility = View.VISIBLE
    }

    override fun loadingUsers(userList: List<User>) {
        user_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        user_list.adapter = UsersAdapter(userList, object : OnClickItemListener<User>{
            override fun onClickItem(user: User) {
                userPresenter?.selectedUser(user)
                albumPresenter?.callAlbumAPI(user.id)
            }
        })
    }

    override fun loadginHeader(userSelected: User) {
        header.visibility = View.VISIBLE
        header_user_name.text = userSelected.name

        Glide.with(this@MainActivity)
            .load(userSelected.image)
            .apply(RequestOptions.circleCropTransform())
            .into(header_user_photo)

        filter.setOnClickListener {
            user_list_content.visibility = View.VISIBLE
            showUserContent()
        }
    }

    override fun loadingAlbums(albumList: List<Album>) {
        album_list.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        album_list.adapter = AlbumsAdapter(albumList, object : OnClickItemListener<Photo>{
            override fun onClickItem(photo: Photo) {
                Log.e("Cliquei", "foto")
            }
        })

    }
}