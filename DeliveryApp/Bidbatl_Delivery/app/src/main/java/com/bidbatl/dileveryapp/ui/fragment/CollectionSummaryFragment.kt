package com.bidbatl.dileveryapp.ui.fragment


import android.graphics.Color
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
import com.bidbatl.dileveryapp.databinding.FragmentCollectionSummaryBinding
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.model.data.OperationData
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Status
import com.bidbatl.dileveryapp.ui.adapter.CollectionSummaryAdapter
import com.bidbatl.dileveryapp.ui.callback.FilterInterface
import com.bidbatl.dileveryapp.ui.operation.OperationViewModel
import com.bidbatl.dileveryapp.util.Common
import kotlinx.android.synthetic.main.fragment_collection_summary.view.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class CollectionSummaryFragment : BaseFragment() {
    lateinit var operationViewModel: OperationViewModel
    lateinit var inteface: FilterInterface
    var summaryData = ArrayList<OperationData>()

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    var adapter = CollectionSummaryAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCollectionSummaryBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_collection_summary,
            container,
            false
        )
        operationViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(OperationViewModel::class.java)
        buttonSelection(binding)
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
            getOrderSummary(null)

        }
        getOrderSummary(null)

        binding.buttonFilter.setOnClickListener {
            inteface.showCustomerList()
        }

        binding.recyclerviewCollecSummary.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recyclerviewCollecSummary.adapter = adapter

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

    private fun getOrderSummary(type:String?) {
        showCircleDialog()
        operationViewModel.getOrder().observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                dismissDialog()
                if (it.data?.code == 200) {
                    if (!it.data.data.isNullOrEmpty()) {
                        val customerId = preference.getString(Common.PreferenceKey.customerId)
                        if (customerId.isNullOrEmpty()) {
                            summaryData = it.data.data as ArrayList<OperationData>
                            adapter.setItems(it.data.data)
                        } else {
                            val filteredOrder = it.data.data.filter { it1 -> it1.user_id == customerId && it1.payment_status == type  }
                            if (filteredOrder.isNullOrEmpty()) {
                                adapter.setItems(listOf())
                            } else {
                                summaryData = filteredOrder as ArrayList<OperationData>
                                adapter.setItems(filteredOrder)
                            }
                        }
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

    private fun performSearch(binding: FragmentCollectionSummaryBinding) {
        if (binding.editTextSearch.text.isNullOrEmpty()) {
            getOrderSummary(null)
            return
        }
        showCircleDialog()
        operationViewModel.searchOrder(binding.editTextSearch.text.toString())
            .observe(viewLifecycleOwner, Observer {
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

    private fun buttonSelection(binding: FragmentCollectionSummaryBinding) {
        binding.linearLayout4.button_cash.setOnClickListener {
            binding.linearLayout4.button_cash.setBackgroundResource(R.drawable.selected_bg)
            binding.linearLayout4.button_cheque.setBackgroundResource(R.drawable.unselected_bg)
            binding.linearLayout4.button_cheque.setTextColor(Color.BLACK)
            binding.linearLayout4.button_cash.setTextColor(Color.WHITE)
            val filteredOrder = summaryData.filter { it1 -> it1.payment_type == null || it1.payment_type == "cash"  }
            adapter.setItems(filteredOrder)

        }
        binding.linearLayout4.button_cheque.setOnClickListener {
            binding.linearLayout4.button_cash.setBackgroundResource(R.drawable.unselected_bg)
            binding.linearLayout4.button_cheque.setBackgroundResource(R.drawable.selected_bg)
            binding.linearLayout4.button_cheque.setTextColor(Color.WHITE)
            binding.linearLayout4.button_cash.setTextColor(Color.BLACK)
            val filteredOrder = summaryData.filter { it1 -> it1.payment_type == "cheque"  }
            adapter.setItems(filteredOrder)
        }

    }

    companion object {

        // TODO: Rename and change types and number of parameters
        fun newInstance() =
            CollectionSummaryFragment()
    }


}
