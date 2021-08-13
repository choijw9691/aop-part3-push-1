package com.example.aop_part3_push

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

/*       private val resultTextView: TextView by lazy {
            findViewById(R.id.resultTextView)
        }

        private val firebaseToken: TextView by lazy {
            findViewById(R.id.firebaseTokenTextView)
        }*/
    private val parentId: LinearLayout by lazy {
        findViewById(R.id.parentId)
    }
    private var mFixedLayoutContainerView: childView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initView()
        mFixedLayoutContainerView?.scaleX=50.5f
        mFixedLayoutContainerView?.scaleY=18.5f
    }

    private fun initView() {
        mFixedLayoutContainerView = childView(
            this,
            100,
            100
        )
        parentId.addView(mFixedLayoutContainerView)

    }
}