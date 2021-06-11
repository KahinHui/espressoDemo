package com.kahin.espressodemo.ui.main.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.constraintlayout.widget.ConstraintLayout
import com.kahin.espressodemo.R
import com.kahin.espressodemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val NAME_PHONE = "name_phone"
        const val REQUEST_CODE_PICK = 100
    }

    private lateinit var binding: ActivityMainBinding

    private var mode: ActionMode? = null
    private var popupWindow: PopupWindow? = null
    private var flagPopupShowing = false

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
            btnShowPopUp.setOnClickListener(this@MainActivity)
            btnShowDialog.setOnClickListener(this@MainActivity)
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
            R.id.btn_show_pop_up -> showPopupWindow(context = this, view)
            R.id.tv_espresso,
            R.id.tv_americano,
            R.id.tv_latte,
            R.id.tv_cappuccino -> {
                view as TextView
                Toast.makeText(this, view.text, Toast.LENGTH_SHORT).show()
                popupWindow?.dismiss()
                flagPopupShowing = false
            }
            R.id.btn_show_dialog -> showDialog(this)
            else -> {}
        }
    }

    @SuppressLint("InflateParams")
    private fun showPopupWindow(context: Context, anchor: View) {
        if (popupWindow == null) {
            popupWindow = PopupWindow(context)
            popupWindow!!.apply {
                width = ConstraintLayout.LayoutParams.WRAP_CONTENT
                height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                contentView = layoutInflater.inflate(R.layout.layout_coffe, null)
                isOutsideTouchable = false
            }
            val view = popupWindow!!.contentView
            view.findViewById<TextView>(R.id.tv_espresso).setOnClickListener(this)
            view.findViewById<TextView>(R.id.tv_americano).setOnClickListener(this)
            view.findViewById<TextView>(R.id.tv_latte).setOnClickListener(this)
            view.findViewById<TextView>(R.id.tv_cappuccino).setOnClickListener(this)

        }

        if (!flagPopupShowing) {
            popupWindow!!.showAsDropDown(anchor, 0, -300)
            flagPopupShowing = true
        }
    }

    private fun showDialog(context: Context) {
        val dialog = AlertDialog.Builder(context)
        dialog.apply {
            setMessage(R.string.dialog_title)
            setPositiveButton(resources.getText(R.string.yes)) { dialog, _ ->
                Toast.makeText(context, resources.getText(R.string.yes), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            setNegativeButton(resources.getText(R.string.no)) { dialog, _ ->
                Toast.makeText(context, resources.getText(R.string.no), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}