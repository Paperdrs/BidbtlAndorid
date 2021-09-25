package com.bidbatl.dileveryapp.ui.live

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bidbatl.dileveryapp.BaseApplication
import com.bidbatl.dileveryapp.R
import com.bumptech.glide.Glide

class PaymentStatusActivity : AppCompatActivity() {

    lateinit var imageViewSuccess: ImageView
    lateinit var imageViewFailed: ImageView
    lateinit var successLayout:ConstraintLayout
    lateinit var failedLayout:ConstraintLayout
    lateinit var buttonContinueWithSuccess: Button
    lateinit var buttonContinueWithFailed: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_status)
        successLayout = findViewById(R.id.success)
        failedLayout = findViewById(R.id.failure)
        imageViewSuccess = findViewById(R.id.imageView_success)
        buttonContinueWithFailed = findViewById(R.id.button_continue)
        buttonContinueWithSuccess = findViewById(R.id.button_continue_success)
        buttonContinueWithSuccess.setOnClickListener {
            showHomeScreen()
        }
        buttonContinueWithFailed.setOnClickListener {
            showHomeScreen()
        }

        imageViewFailed = findViewById(R.id.imageView_error)
       if ( intent.getBooleanExtra("status",false)) {
           successLayout.visibility = View.VISIBLE
           Glide.with(this)
               .asGif()
               .load(R.drawable.success_done)
               .into(imageViewSuccess)
       }
        else{
           failedLayout.visibility = View.VISIBLE
           Glide.with(this)
               .asGif()
               .load(R.drawable.error_animation)
               .into(imageViewFailed)
       }

    }

    private fun showHomeScreen() {
        val intent = Intent(BaseApplication.instance, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        finishAffinity()
        BaseApplication.instance.startActivity(intent)
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        showHomeScreen()
    }

}
