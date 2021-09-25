package com.bidbatl.dileveryapp.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bidbatl.dileveryapp.BaseFragment
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.FragmentGoodsReturnedBinding
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Status
import com.bidbatl.dileveryapp.ui.adapter.GoodsReturnAdapterAdapter
import com.bidbatl.dileveryapp.ui.callback.FilterInterface
import com.bidbatl.dileveryapp.ui.operation.OperationViewModel
import com.bidbatl.dileveryapp.util.Common
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class GoodsReturnedFragment : BaseFragment() {
val adapter = GoodsReturnAdapterAdapter()
    lateinit var operationViewModel: OperationViewModel
    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    lateinit var inteface: FilterInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentGoodsReturnedBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_goods_returned,container,false)
        binding.recyclerviewGoodsReturn.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        binding.recyclerviewGoodsReturn.adapter = adapter
        val customerId = preference.getString(Common.PreferenceKey.customerId)
        if (customerId.isNullOrEmpty()){
            binding.filterLayout.visibility = View.GONE
            binding.buttonFilter.background = resources.getDrawable(R.drawable.border)
        }
        else{
            binding.filterLayout.visibility = View.VISIBLE
            binding.buttonFilter.background = resources.getDrawable(R.drawable.border_map)
            binding.filterBy.text = "Filter By: " + preference.getString(Common.PreferenceKey.customerName)
        }

        binding.clearFilter.setOnClickListener {
            preference.remove(Common.PreferenceKey.customerId)
            binding.filterLayout.visibility = View.GONE
            binding.buttonFilter.background = resources.getDrawable(R.drawable.border)
            getGoodsReturnItems()

        }
        getGoodsReturnItems()
        binding.buttonFilter.setOnClickListener {
            inteface.showCustomerList()
        }
        binding.editTextSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                preference.setString(Common.PreferenceKey.customerId,"")
                performSearch(binding)
                return@OnEditorActionListener true
            }
            false
        })
        binding.editTextSearch.text.clear()
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is FilterInterface) inteface = (activity as FilterInterface?)!!
    }
    private fun getGoodsReturnItems(){
        showCircleDialog()
        operationViewModel = ViewModelProvider(this,viewModelProviderFactory).get(OperationViewModel::class.java)
        operationViewModel.getOrder().observe(viewLifecycleOwner, Observer { resource ->
            if (resource.status == Status.SUCCESS){
                dismissDialog()
                if (resource.data?.code == 200){
                    if (!resource.data.data.isNullOrEmpty()){
                        var customerId = preference.getString(Common.PreferenceKey.customerId)
                        if (customerId.isNullOrEmpty()) {
                            adapter.setItems(resource.data.data)
                        } else {
                            var filteredOrder = resource.data.data.filter { it.user_id == customerId }
                            if (filteredOrder.isNullOrEmpty()) {
                                adapter.setItems(listOf())
                            } else {
                                adapter.setItems(filteredOrder)
                            }
                        }
                    }
                    else{
                        adapter.setItems(listOf())
                    }

                }
                else{
                    adapter.setItems(listOf())
                }

            }
            if (resource.status == Status.ERROR){
                dismissDialog()

            }
        })
    }
    private fun performSearch(binding: FragmentGoodsReturnedBinding) {
        if (binding.editTextSearch.text.isNullOrEmpty()){
            getGoodsReturnItems()
            return
        }
        showCircleDialog()
        operationViewModel.searchOrder(binding.editTextSearch.text.toString()).observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                dismissDialog()
                if (it.data?.code == 200) {
                    if (!it.data.data.isNullOrEmpty()) {
                        adapter.setItems(it.data.data)
                    } else {
                        adapter.setItems(listOf())
                    }
                } else {
                    adapter.setItems(listOf())
                }

            }
            if (it.status == Status.ERROR) {
                dismissDialog()

            }
        })
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        fun newInstance() =
            GoodsReturnedFragment()
    }
}
