package com.bidbatl.dileveryapp.ui.operation

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.bidbatl.dileveryapp.BaseActivity
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.model.Customer
import com.bidbatl.dileveryapp.ui.callback.CustomerCallback
import com.bidbatl.dileveryapp.ui.callback.FilterInterface
import com.bidbatl.dileveryapp.ui.fragment.*
import com.bidbatl.dileveryapp.util.Common
import javax.inject.Inject

class OperationActivity : BaseActivity(),OperationFragment.OnActionClicked ,FilterInterface,CustomerCallback{
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var operationViewModel: OperationViewModel
    private lateinit var cardView:CardView
    private lateinit var liveButton :Button
    private lateinit var operationButton :Button
    lateinit var toolbar:Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operation)

        operationViewModel = ViewModelProvider(this,providerFactory).get(OperationViewModel::class.java)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.operation_frag_holder, OperationFragment.newInstance())
                .commit()
        }
        initCardView()
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
    private fun initCardView(){
        cardView = findViewById(R.id.button_layout)
        liveButton = cardView.findViewById(R.id.button_live)
        operationButton = cardView.findViewById(R.id.button_operation)
        liveButton.setOnClickListener {
            finish()
        }
            operationButton.setBackgroundResource(R.drawable.rounded_blue_button)
            liveButton.setBackgroundResource(R.drawable.rounded_gray_button)
            liveButton.setTextColor(Color.BLACK)
            operationButton.setTextColor(Color.WHITE)

    }
    private fun setDeliverySummaryFragment(){
        preference.setString(Common.PreferenceKey.customerId,"")
        supportFragmentManager.beginTransaction()
            .replace(R.id.operation_frag_holder, DeliverySummaryFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
    private fun setCollectionSummaryFragment(){
        preference.setString(Common.PreferenceKey.customerId,"")

        supportFragmentManager.beginTransaction()
            .replace(R.id.operation_frag_holder, CollectionSummaryFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
    private fun setGoodsReturnFragment(){
        preference.setString(Common.PreferenceKey.customerId,"")
        supportFragmentManager.beginTransaction()
            .replace(R.id.operation_frag_holder, GoodsReturnedFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
    private fun setCustomerFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.operation_frag_holder, CustomerListFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun actionSelected(actionType: ActionType) {
        when (actionType) {
             ActionType.Del_Summary -> setDeliverySummaryFragment()
            ActionType.Coll_Summary -> setCollectionSummaryFragment()
            ActionType.Goods_return -> setGoodsReturnFragment()
        }
    }

    override fun showCustomerList() {
        setCustomerFragment()


    }

    override fun filterByCustomerName(operationType: Int, customer: Customer) {
        showToastMessage(customer.name)

    }

}
