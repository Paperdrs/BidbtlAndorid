package com.bidbatl.dileveryapp.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.bidbatl.dileveryapp.BaseApplication
import com.bidbatl.dileveryapp.BaseFragment
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.FragmentDeliverySummaryBinding
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.model.data.OperationData
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Status
import com.bidbatl.dileveryapp.ui.adapter.DeliverySummaryAdapter
import com.bidbatl.dileveryapp.ui.callback.DeliverySummaryItemClicked
import com.bidbatl.dileveryapp.ui.operation.OperationViewModel
import com.bidbatl.dileveryapp.ui.operation.SummaryDetailActivity
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class DeliverySummaryFragment : BaseFragment(),DeliverySummaryItemClicked {
    lateinit var adapter : DeliverySummaryAdapter
    lateinit var operationViewModel: OperationViewModel


    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bindeing: FragmentDeliverySummaryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_delivery_summary, container, false)
        bindeing.recyclerviewDelSummary.layoutManager =
            LinearLayoutManager(context, VERTICAL, false)
       adapter = DeliverySummaryAdapter(this)
        bindeing.recyclerviewDelSummary.adapter = adapter
        getDeliverySummary()
        return bindeing.root
    }



    private fun getDeliverySummary() {
        showCircleDialog()
        operationViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(OperationViewModel::class.java)
        operationViewModel.getOrder().observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                dismissDialog()
                if (it.data?.code == 200) {
                    if (it.data.data.isNullOrEmpty()) {

                    } else {
                        adapter.setItems(it.data.data)
                    }
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
            DeliverySummaryFragment()
    }

    override fun didClickedSummaryItem(operationModel: OperationData) {
        println("clicked")
        val intent = Intent(BaseApplication.instance, SummaryDetailActivity::class.java);
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("summaryData",operationModel)
        BaseApplication.instance.startActivity(intent)
        operationViewModel.setSelectedOrder(operationModel)

    }


}
