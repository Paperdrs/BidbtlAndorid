package com.bidbatl.dileveryapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.bidbatl.dileveryapp.ui.live.HomeActivity
import com.bidbatl.dileveryapp.ui.login.MainActivity
import com.bidbatl.dileveryapp.util.Common

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //4second splash time
        Handler().postDelayed({
            if (preference.getBoolean(Common.PreferenceKey.isLoggedIn)){
                navigateToHome()
            }
            else{
                navigateToMain()
            }            //finish this activity
            finish()
        },3000)

    }
    fun navigateToHome() {
        val intent = Intent(BaseApplication.instance, HomeActivity::class.java);
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        BaseApplication.instance.startActivity(intent)

    }
    fun navigateToMain() {
        val intent = Intent(BaseApplication.instance, MainActivity::class.java);
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        BaseApplication.instance.startActivity(intent)

    }
}
