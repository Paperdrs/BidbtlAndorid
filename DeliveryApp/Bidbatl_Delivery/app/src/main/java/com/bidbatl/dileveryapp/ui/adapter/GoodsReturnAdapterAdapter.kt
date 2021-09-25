package com.bidbatl.dileveryapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bidbatl.dileveryapp.BaseApplication
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.GoodsReturnRowBinding
import com.bidbatl.dileveryapp.model.data.OperationData
import com.bidbatl.dileveryapp.util.Common
import com.bidbatl.dileveryapp.util.Constants
import com.preference.provider.PreferenceProvider.context

class GoodsReturnAdapterAdapter() : RecyclerView.Adapter<GoodsReturnViewHolder>() {
    var goodsReturnList : List<OperationData> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsReturnViewHolder {

        val layoutGoodsReturnRowBinding : GoodsReturnRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.goods_return_row,parent,false)
        return GoodsReturnViewHolder(layoutGoodsReturnRowBinding)
    }

    override fun getItemCount(): Int {
        return goodsReturnList.size
    }
    fun setItems(items:List<OperationData>){
        goodsReturnList = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: GoodsReturnViewHolder, position: Int) {

        holder.bindGoodsReturnItems(goodsReturnList[position])
        holder.view.buttonInvoice.setOnClickListener {
            openURLInBrowser(this.goodsReturnList[position].invoice_url,Constants.PageTitle.INVOICE.value,goodsReturnList[position].number)
        }
    }
    private  fun openURLInBrowser(url:String,title:String,orderNumber:String){
        if (url.isNullOrEmpty()){
            Toast.makeText(context,"Invalid document",Toast.LENGTH_LONG).show()
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
//        startActivity(BaseApplication.instance,openURL,null)

    }

}
class GoodsReturnViewHolder (val view: GoodsReturnRowBinding) : RecyclerView.ViewHolder(view.root) {
    fun bindGoodsReturnItems(data:OperationData){
      view.goodsReturn = data
    }
    // Holds the TextView that will add each animal to
}