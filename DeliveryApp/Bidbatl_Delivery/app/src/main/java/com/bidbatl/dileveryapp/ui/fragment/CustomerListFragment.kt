package com.bidbatl.dileveryapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bidbatl.dileveryapp.BaseFragment
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.FragmentCustomerListBinding
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.model.Customer
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Status
import com.bidbatl.dileveryapp.ui.adapter.CustomerAdapter
import com.bidbatl.dileveryapp.ui.operation.OperationActivity
import com.bidbatl.dileveryapp.ui.operation.OperationViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class CustomerListFragment : BaseFragment() {
    lateinit var adapter: CustomerAdapter
    lateinit var binding: FragmentCustomerListBinding
    var customer: ArrayList<Customer> = arrayListOf()
    private lateinit var operationViewModel: OperationViewModel

    @Inject
    lateinit var viewProvider: ViewModelProviderFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_customer_list, container, false)
        operationViewModel =
            ViewModelProvider(this, viewProvider).get(OperationViewModel::class.java)
        operationViewModel.getCustomerList().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    print(it.data.toString())
                    if (it.data?.data.isNullOrEmpty() != null) {
                        customer = it.data?.data as ArrayList<Customer>
                        adapter.setItems(customer)
                    } else {
                        adapter.setItems(listOf())
                    }
                    dismissDialog()
                }
                Status.LOADING -> {
                    showCircleDialog()
                }
                Status.ERROR -> {
                    dismissDialog()
                    adapter.setItems(listOf())
                }
            }
        })
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = CustomerAdapter(activity as OperationActivity)
        initRecyclerView(binding)

    }

    private fun initRecyclerView(binding: FragmentCustomerListBinding) {
        binding.recyclerViewCustomer.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recyclerViewCustomer.adapter = adapter


        adapter.setItems(customer)

    }

    companion object {
        // TODO: Rename and change types and number of parameters
        fun newInstance() =
            CustomerListFragment()
    }
}

