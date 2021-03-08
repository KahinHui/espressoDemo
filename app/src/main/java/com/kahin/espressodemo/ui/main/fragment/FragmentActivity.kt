package com.kahin.espressodemo.ui.main.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kahin.espressodemo.R

class FragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragmnet)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }
}