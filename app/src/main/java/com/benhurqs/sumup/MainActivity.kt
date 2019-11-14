package com.benhurqs.sumup

import android.os.Bundle
import android.util.Log
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


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Injection.provideUserRepository(this).getUserList(object: APICallback<List<User>, String>{
            override fun onStart() {
                Log.e("start","comecou")
            }

            override fun onError(error: String) {
                Log.e("error","error -> " + error)
            }

            override fun onFinish() {
                Log.e("finish","finish")
            }

            override fun onSuccess(response: List<User>) {
                Log.e("success","chegou - " + response.get(0).name)
                loadUsers(response)
            }
        })

    }

    private fun loadUsers(userList: List<User>){
        user_list_close_btn.setOnClickListener {
            Log.e("Cliquei", "fechar")
        }

        user_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        user_list.adapter = UsersAdapter(userList, object : OnClickItemListener<User>{
            override fun onClickItem(item: User) {
                Log.e("Cliquei", item.name)
            }
        })
    }


}
