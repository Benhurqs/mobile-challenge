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
import com.benhurqs.sumup.photos.presentation.ui.contracts.MainView
import com.benhurqs.sumup.photos.presentation.ui.contracts.UserContract
import com.benhurqs.sumup.photos.presentation.ui.presenter.UserPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header.*


class MainActivity : AppCompatActivity(), MainView, UserContract.View {

    private var userPresenter: UserContract.Presenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userPresenter = UserPresenter(this, this, Injection.provideUserRepository(this))

        user_list_close_btn.setOnClickListener {
            hideUserContent()
        }

    }

    override fun onStart() {
        super.onStart()
        userPresenter?.callUserAPI()
    }

    private fun loadAlbums(userId: Int){
        Injection.provideAlbumRepository(this).getAlbumList(userId, object: APICallback<List<Album>, String>{
            override fun onStart() {
                progress.visibility = View.VISIBLE
            }

            override fun onError(error: String) {
                progress.visibility = View.GONE
                Log.e("error","error -> " + error)
            }

            override fun onFinish() {
                progress.visibility = View.GONE
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

    /**************************** USER CALLBACK ***************************************/

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
                Log.e("Cliquei", user.name)
//                loadAlbums(user.id)
//                user_list_content.visibility = View.GONE
                userPresenter?.selectedUser(user)
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
}
