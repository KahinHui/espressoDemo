package com.kahin.espressodemo.ui.main.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import com.kahin.espressodemo.R
import com.kahin.espressodemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val NAME_PHONE = "name_phone"
        const val REQUEST_CODE_PICK = 100
    }

    private lateinit var binding: ActivityMainBinding

    private var mode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnOk.setOnClickListener(this@MainActivity)
            btnShowActionbar.setOnClickListener(this@MainActivity)
            btnHideActionbar.setOnClickListener(this@MainActivity)
            btnCall.setOnClickListener(this@MainActivity)
            btnPickPhone.setOnClickListener(this@MainActivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_PICK -> {
                if (resultCode == RESULT_OK) {
                    binding.etPhone.apply {
                        text?.clear()
                        data?.getStringExtra(NAME_PHONE)?.apply {
                            setText(this)
                            setSelection(this.length)
                        }
                    }
                }
            }
        }
    }

    class TestActionMode(inflater: MenuInflater) : ActionMode.Callback {
        private val menuInflater = inflater
        override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            menuInflater.inflate(R.menu.main_activity_action, p1)
            return true
        }

        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
            return true
        }

        override fun onDestroyActionMode(p0: ActionMode?) {}

    }

    @SuppressLint("SetTextI18n")
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_ok -> binding.tvName.text = "Name: ${binding.etName.text.toString()}"
            R.id.btn_show_actionbar -> mode = startSupportActionMode(TestActionMode(menuInflater))
            R.id.btn_hide_actionbar -> mode?.finish()
            R.id.btn_call -> {
                val callIntent = Intent(ACTION_DIAL)
                val uri = Uri.parse("tel:${binding.etPhone.text.toString()}")
                callIntent.data = uri
                startActivity(callIntent)
            }
            R.id.btn_pick_phone -> {
                val callIntent = Intent(this@MainActivity, CallActivity::class.java)
                startActivityForResult(callIntent, REQUEST_CODE_PICK)
            }
            else -> {}
        }
    }
}