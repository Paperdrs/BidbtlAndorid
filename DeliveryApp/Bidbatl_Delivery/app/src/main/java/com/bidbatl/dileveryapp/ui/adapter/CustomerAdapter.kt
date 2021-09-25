package com.bidbatl.dileveryapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.LayoutCustomerBinding
import com.bidbatl.dileveryapp.model.Customer
import com.bidbatl.dileveryapp.ui.callback.CustomerCallback
import com.bidbatl.dileveryapp.ui.operation.OperationActivity
import com.bidbatl.dileveryapp.util.Common
import com.preference.PowerPreference

class CustomerAdapter(var activity:OperationActivity) : RecyclerView.Adapter<CustomerViewHolder>() {
    var customerList :List<Customer> = listOf()
    lateinit var callback:CustomerCallback
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val collectionSummaryRowBinding : LayoutCustomerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.layout_customer,parent,false)

        if ((parent.context) is CustomerCallback) callback = ((parent.context) as CustomerCallback?)!!
        return  CustomerViewHolder(collectionSummaryRowBinding)
    }

    override fun getItemCount(): Int {
        return customerList.size
    }
    fun setItems(items: List<Customer>){
        this.customerList = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        holder.bindCustomer(this.customerList[position])
        holder.view.container.setOnClickListener {
            PowerPreference.getDefaultFile().setString(Common.PreferenceKey.customerId,this.customerList[position].id)
            PowerPreference.getDefaultFile().setString(Common.PreferenceKey.customerName,this.customerList[position].name)
        callback.filterByCustomerName(1,this.customerList[position])
          activity.onBackPressed()
        }
    }

}
class CustomerViewHolder (val view: LayoutCustomerBinding) : RecyclerView.ViewHolder(view.root) {
    fun bindCustomer(bindingData:Customer){
        view.customer = bindingData
    }
}