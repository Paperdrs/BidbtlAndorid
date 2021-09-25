package com.bidbatl.dileveryapp.ui.live

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.bidbatl.dileveryapp.BaseActivity
import com.bidbatl.dileveryapp.BaseApplication
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.ActivityAcceptPaymentBinding
import com.bidbatl.dileveryapp.ui.fragment.CashFragment
import com.bidbatl.dileveryapp.ui.fragment.ChecqueFragment

class AcceptPaymentActivity : BaseActivity() {
    lateinit var toolbar : Toolbar
    lateinit var acceptPaymentBinding:ActivityAcceptPaymentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        acceptPaymentBinding = DataBindingUtil.setContentView(this,R.layout.activity_accept_payment)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.view_container, CashFragment.newInstance())
                .commit()
        }
        acceptPaymentBinding.acceptPaymentLayout.orderDetail = BaseApplication.instance.kLiveDeliveryData
        acceptPaymentBinding.acceptPaymentLayout.buttonCash.setOnClickListener {
            acceptPaymentBinding.acceptPaymentLayout.buttonCash.setBackgroundResource(R.drawable.selected_bg)
            acceptPaymentBinding.acceptPaymentLayout.buttonCheque.setBackgroundResource(R.drawable.unselected_bg)
            acceptPaymentBinding.acceptPaymentLayout.buttonCheque.setTextColor(Color.BLACK)
            acceptPaymentBinding.acceptPaymentLayout.buttonCash.setTextColor(Color.WHITE)
            supportFragmentManager.beginTransaction()
                .replace(R.id.view_container, CashFragment.newInstance())
                .commit()
        }
        acceptPaymentBinding.acceptPaymentLayout.buttonCheque.setOnClickListener {
            acceptPaymentBinding.acceptPaymentLayout.buttonCash.setBackgroundResource(R.drawable.unselected_bg)
            acceptPaymentBinding.acceptPaymentLayout.buttonCheque.setBackgroundResource(R.drawable.selected_bg)
            acceptPaymentBinding.acceptPaymentLayout.buttonCheque.setTextColor(Color.WHITE)
            acceptPaymentBinding.acceptPaymentLayout.buttonCash.setTextColor(Color.BLACK)
            supportFragmentManager.beginTransaction()
                .replace(R.id.view_container, ChecqueFragment.newInstance())
                .commit()
        }
        setToolBar()

    }
    private fun setToolBar(){
        toolbar = findViewById(R.id.toolbar)
        val menuImage = toolbar.findViewById<ImageView>(R.id.imageView_menu)
        menuImage.setImageDrawable(getDrawable(R.drawable.back))
        menuImage.visibility = View.VISIBLE
        menuImage.setOnClickListener {
            backPressed()
        }
        setSupportActionBar(toolbar)
    }
    public fun setFrag(view:View){

    }

}
