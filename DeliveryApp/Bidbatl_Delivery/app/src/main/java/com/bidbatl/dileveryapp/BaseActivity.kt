package com.bidbatl.dileveryapp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.bidbatl.dileveryapp.ui.login.LoginActivity
import com.preference.Preference
import dagger.android.support.DaggerAppCompatActivity
import java.util.*
import javax.inject.Inject

open class BaseActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var preference: Preference
    var dialog: Dialog? = null
    private val dialogs = Vector<Dialog>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
//        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

    }
    open fun showCircleDialog() {
        dialog = Dialog(this)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.loading_dialog)
        dialog!!.show()
        dialogs.add(dialog)
    }
    fun navigateToLogin() {
        preference.clear()
        val intent = Intent(BaseApplication.instance, LoginActivity::class.java);
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        BaseApplication.instance.startActivity(intent)
        finishAffinity()

    }
    fun showToastMessage(message:String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }
    open fun dismissDialog() {
        for (dialog in dialogs) if (dialog.isShowing) dialog.dismiss()
    }

    fun backPressed(){
        super.onBackPressed()
    }
}
