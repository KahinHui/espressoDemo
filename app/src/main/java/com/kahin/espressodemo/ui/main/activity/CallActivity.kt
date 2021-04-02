package com.kahin.espressodemo.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kahin.espressodemo.R
import com.kahin.espressodemo.databinding.ActivityCallBinding
import com.kahin.espressodemo.ui.main.activity.MainActivity.Companion.NAME_PHONE

class CallActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val PHONE_NUMBER = "987-654-321"
    }

    private lateinit var binding: ActivityCallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            tvNumber.text = PHONE_NUMBER
            btnGot.setOnClickListener(this@CallActivity)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_got -> {
                val resultIntent = Intent()
                resultIntent.putExtra(NAME_PHONE, binding.tvNumber.text)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}