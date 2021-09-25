package com.bidbatl.dileveryapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bidbatl.dileveryapp.BaseApplication
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.CollectionSummaryRowBinding
import com.bidbatl.dileveryapp.model.data.OperationData
import com.bidbatl.dileveryapp.util.Common
import com.bidbatl.dileveryapp.util.Constants
import com.preference.provider.PreferenceProvider.context

class CollectionSummaryAdapter() : RecyclerView.Adapter<CollectionSummaryViewHolder>() {
    var collectionSummaryList :List<OperationData> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionSummaryViewHolder {
        val collectionSummaryRowBinding : CollectionSummaryRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.collection_summary_row,parent,false)
       return  CollectionSummaryViewHolder(collectionSummaryRowBinding)
    }

    override fun getItemCount(): Int {
        return collectionSummaryList.size
    }
    fun setItems(items: List<OperationData>){
        this.collectionSummaryList = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CollectionSummaryViewHolder, position: Int) {
        holder.bindCollectionSummary(this.collectionSummaryList[position])
        holder.view.buttonInvoice.setOnClickListener {
            openURLInBrowser(this.collectionSummaryList[position].invoice_url,Constants.PageTitle.INVOICE.value,this.collectionSummaryList[position].number)
        }
    }
    private  fun openURLInBrowser(url:String,title:String,orderNumber:String){
        if (url.isEmpty()){
            Toast.makeText(context,"Invalid document", Toast.LENGTH_LONG).show()
//            showToastMessage("Invalid document")
            return
        }

        Common.downloadFile(url,title,BaseApplication.instance,orderNumber)
//        val intent = Intent(BaseApplication.instance, WebViewActivity::class.java)
//        intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK
//        intent.putExtra(Common.PreferenceKey.pageUrl,url)
//        intent.putExtra(Common.PreferenceKey.pageTitle,title)
//        startActivity(BaseApplication.instance,intent,null)
//        val openURL = Intent(Intent.ACTION_VIEW)
//        openURL.data = Uri.parse(url)
//       startActivity(BaseApplication.instance,openURL,null)

    }

}
class CollectionSummaryViewHolder (val view: CollectionSummaryRowBinding) : RecyclerView.ViewHolder(view.root) {
    fun bindCollectionSummary(bindingData:OperationData){
        view.collectionSummary = bindingData
    }
}