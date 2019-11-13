package com.benhurqs.sumup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.benhurqs.sumup.user.managers.UserRepository

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        UserRepository.getInstance().getUserList()
    }
}
