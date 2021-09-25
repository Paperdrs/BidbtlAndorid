package com.bidbatl.dileveryapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.DeliveryRowLayoutBinding
import com.bidbatl.dileveryapp.model.data.OperationData
import com.bidbatl.dileveryapp.ui.callback.DeliverySummaryItemClicked
import com.preference.provider.PreferenceProvider.context

class DeliverySummaryAdapter(val listener:DeliverySummaryItemClicked) : RecyclerView.Adapter<ViewHolder>() {
    lateinit var  binding : DeliveryRowLayoutBinding
    var deliverySummaryList :List<OperationData> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding  = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.delivery_row_layout,parent,false)
       return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return deliverySummaryList.size
    }
    fun setItems(items:List<OperationData>){
        deliverySummaryList = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindDeliverySummaryItems(deliverySummaryList[position])
        binding.index  = (position+1).toString()

        binding.rowLayout.setOnClickListener {
            listener.didClickedSummaryItem(deliverySummaryList[position])
        }

    }

}
class ViewHolder (val view: DeliveryRowLayoutBinding) : RecyclerView.ViewHolder(view.root) {
   fun bindDeliverySummaryItems(item:OperationData){
       view.deliverySummary = item
        if (item.payment_status == "accepted" || item.status == "delivered"){//cheque_in_hand
           view.textView7.background = context?.let { getDrawable(it,R.drawable.circle_green) }
           view.textView9.background = context?.let { getDrawable(it,R.drawable.circle_green) }
           view.textView11.background = context?.let { getDrawable(it,R.drawable.circle_green) }

       }
       else{
            if (item.is_unloading_start == "1" && item.is_unloading_end == "0"){
                view.textView7.background = context?.let { getDrawable(it,R.drawable.circle_green) }
                view.textView9.background = context?.let { getDrawable(it,R.drawable.circle_yellow) }
                view.textView11.background = context?.let { getDrawable(it,R.drawable.circle_gray) }

           }
           else  if (item.is_unloading_start == "1" && item.is_unloading_end == "1"){
                view.textView7.background = context?.let { getDrawable(it,R.drawable.circle_green) }
                view.textView9.background = context?.let { getDrawable(it,R.drawable.circle_green) }
                view.textView11.background = context?.let { getDrawable(it,R.drawable.circle_yellow) }

           }
           else{
                view.textView7.background = context?.let { getDrawable(it,R.drawable.circle_yellow) }
                view.textView9.background = context?.let { getDrawable(it,R.drawable.circle_gray) }
                view.textView11.background = context?.let { getDrawable(it,R.drawable.circle_gray) }

           }

       }

   }
}