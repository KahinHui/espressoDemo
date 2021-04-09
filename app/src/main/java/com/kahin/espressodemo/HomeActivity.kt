package com.kahin.espressodemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kahin.espressodemo.databinding.ActivityHomeBinding
import com.kahin.espressodemo.databinding.ActivityHomeBinding.inflate
import com.kahin.espressodemo.ui.main.activity.MainActivity
import com.kahin.espressodemo.ui.main.activity.WebViewActivity
import com.kahin.espressodemo.ui.main.fragment.FragmentActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnActivity.setOnClickListener {
                startActivity(Intent(this@HomeActivity, MainActivity::class.java))
            }
            btnFragment.setOnClickListener {
                startActivity(Intent(this@HomeActivity, FragmentActivity::class.java))
            }
            btnWebView.setOnClickListener {
                startActivity(Intent(this@HomeActivity, WebViewActivity::class.java))
            }
        }
    }
}