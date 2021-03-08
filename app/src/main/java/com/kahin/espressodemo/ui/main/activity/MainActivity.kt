package com.kahin.espressodemo.ui.main.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import com.kahin.espressodemo.R
import com.kahin.espressodemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mode: ActionMode? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnOk.setOnClickListener {
                tvName.text = "Name: ${etName.text.toString()}"
            }
            btnShowActionbar.setOnClickListener {
                mode = startSupportActionMode(TestActionMode(menuInflater))
            }
            btnHideActionbar.setOnClickListener {
                mode?.finish()
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
}