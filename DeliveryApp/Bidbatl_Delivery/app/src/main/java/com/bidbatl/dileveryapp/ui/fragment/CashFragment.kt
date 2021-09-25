package com.bidbatl.dileveryapp.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bidbatl.dileveryapp.BaseApplication
import com.bidbatl.dileveryapp.BaseFragment
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.FragmentCashBinding
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Status
import com.bidbatl.dileveryapp.ui.live.AcceptPaymentActivity
import com.bidbatl.dileveryapp.ui.live.LiveViewModel
import com.bidbatl.dileveryapp.ui.live.PaymentStatusActivity
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CashFragment : BaseFragment() {
    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    lateinit var liveViewModel: LiveViewModel
    lateinit var bindingCashFragment: FragmentCashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingCashFragment = DataBindingUtil.inflate(inflater,R.layout.fragment_cash,container,false)
       liveViewModel = ViewModelProvider(this,viewModelProviderFactory).get(LiveViewModel::class.java)
        calculateCash()

        bindingCashFragment.buttonProceed.setOnClickListener {
            if (BaseApplication.instance.kLiveDeliveryData != null) {
                val diff =
                if (BaseApplication.instance.kLiveDeliveryData!!.data!!.total_amount.toFloat().toInt() == bindingCashFragment.textViewGrandTotal.text.toString()
                        .toInt()
                ) {
                    acceptCashPayment()

                }
                else{
                    showToastMessage("Total amount dose'nt match to your cash amount")
                }
            }
        }
        return bindingCashFragment.root
    }
    private fun calculateCash(){
        bindingCashFragment.textViewNoN1.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                grandTotal()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    bindingCashFragment.textViewAmount2000.text = "0"
                }
                else{
                    bindingCashFragment.textViewAmount2000.text = (2000* s.toString().toInt()).toString()
                }
            }
        })
        bindingCashFragment.textViewNoN2.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                grandTotal()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    bindingCashFragment.textViewAmount500.text = "0"
                }
                else{
                    bindingCashFragment.textViewAmount500.text = (500* s.toString().toInt()).toString()
                }
            }
        })
        bindingCashFragment.textViewNoN3.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                grandTotal()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    bindingCashFragment.textViewAmount200.text = "0"
                }
                else{
                    bindingCashFragment.textViewAmount200.text = (200* s.toString().toInt()).toString()
                }
            }
        })
        bindingCashFragment.textViewNoN4.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                grandTotal()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    bindingCashFragment.textViewAmount100.text = "0"
                }
                else{
                    bindingCashFragment.textViewAmount100.text = (100* s.toString().toInt()).toString()
                }
            }
        })
        bindingCashFragment.textViewNoN5.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                grandTotal()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    bindingCashFragment.textViewAmount50.text = "0"
                }
                else{
                    bindingCashFragment.textViewAmount50.text = (50* s.toString().toInt()).toString()
                }
            }
        })
        bindingCashFragment.textViewNoN6.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                grandTotal()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    bindingCashFragment.textViewAmount20.text = "0"
                }
                else{
                    bindingCashFragment.textViewAmount20.text = (20* s.toString().toInt()).toString()
                }
            }
        })
        bindingCashFragment.textViewNoN7.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                grandTotal()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    bindingCashFragment.textViewAmount10.text = "0"
                }
                else{
                    bindingCashFragment.textViewAmount10.text = (10* s.toString().toInt()).toString()
                }
            }
        })
        bindingCashFragment.textViewNoN8.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                grandTotal()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    bindingCashFragment.textViewAmountCoins.text = "0"
                }
                else{
                    bindingCashFragment.textViewAmountCoins.text = (1* s.toString().toInt()).toString()
                }
            }
        })


    }
    fun grandTotal(){
        bindingCashFragment.textViewGrandTotal.text = (
                bindingCashFragment.textViewAmount2000.text.toString().toInt()+
                        bindingCashFragment.textViewAmount500.text.toString().toInt()+
                        bindingCashFragment.textViewAmount200.text.toString().toInt()+
                        bindingCashFragment.textViewAmount100.text.toString().toInt()+
                        bindingCashFragment.textViewAmount50.text.toString().toInt()+
                        bindingCashFragment.textViewAmount20.text.toString().toInt()+
                        bindingCashFragment.textViewAmount10.text.toString().toInt()+
                        bindingCashFragment.textViewAmountCoins.text.toString().toInt()
                ).toString()
    }
    private fun acceptCashPayment(){
        //if (bindingCashFragment.textViewGrandTotal.text == BaseApplication.instance.kLiveDeliveryData?.data?.total_amount){
            var params = HashMap<String,Any>()
            var noteParams = HashMap<String,Any>()
            params["order_id"] = BaseApplication.instance.kLiveDeliveryData?.data!!.id
            params["type"] = "cash"
           params["coins"] = bindingCashFragment.textViewAmountCoins.text.toString()

        noteParams["2000"] = formatNote(bindingCashFragment.textViewNoN1.text.toString())
        noteParams["500"] = formatNote(bindingCashFragment.textViewNoN2.text.toString())
        noteParams["200"] = formatNote(bindingCashFragment.textViewNoN3.text.toString())
        noteParams["100"] = formatNote(bindingCashFragment.textViewNoN4.text.toString())
        noteParams["50"] = formatNote( bindingCashFragment.textViewNoN5.text.toString())
        noteParams["20"] = formatNote(bindingCashFragment.textViewNoN6.text.toString())
        noteParams["10"] = formatNote(bindingCashFragment.textViewNoN7.text.toString())
            //params["coins"] = bindingCashFragment.textViewNoN8.text
            params["notes"] = noteParams
        showCircleDialog()
            liveViewModel.acceptPayment(params).observe(viewLifecycleOwner, Observer {

                if (it.status == Status.SUCCESS){
                    if (it.data?.code == 200) {
                        updateDeliveryStatus()
                        showToastMessage("Payment Accepted Successfully")
                        showStatusScreen(true)
                    }
                    else{
                        showStatusScreen(false)
                    }
                    dismissDialog()
                }
                if (it.status == Status.ERROR){
                    showStatusScreen(false)
                   dismissDialog()
                }
            })
//        }
//        else{
//
//        }

    }
    private fun updateDeliveryStatus(){
        val params = java.util.HashMap<String, Any>()
        params["status"] = "delivered"
        params["order_id"] = BaseApplication.instance.kLiveDeliveryData?.data!!.id
        liveViewModel.updateDeliveryStatus(params).observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                dismissDialog()
                if (it.data?.code == 200) {
                }
            }
            else if (it.status == Status.ERROR) {
                dismissDialog()
                showToastMessage(it.message!!)
            }
        })
    }

    private fun showStatusScreen(status: Boolean) {
        val intent = Intent(BaseApplication.instance, PaymentStatusActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("status",status)
        BaseApplication.instance.startActivity(intent)
        (activity as AcceptPaymentActivity).finish()
    }

    private fun formatNote(text:String):String{
        return if (text.isNullOrEmpty()) "0" else text
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        fun newInstance() = CashFragment()
    }
}
