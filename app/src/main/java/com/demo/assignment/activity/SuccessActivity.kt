package com.demo.assignment.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.playablo.school.myapplication.R
import kotlinx.android.synthetic.main.activity_success.*

class SuccessActivity : AppCompatActivity() {
    var fundName= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
        fundName = intent.getStringExtra("fundName")
        tv_fundName.text= fundName

        btn_gotit.setOnClickListener { v->
            finish()

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()


    }
}
