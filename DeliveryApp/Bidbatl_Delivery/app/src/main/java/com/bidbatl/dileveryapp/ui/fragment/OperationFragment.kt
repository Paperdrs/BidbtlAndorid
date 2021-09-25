package com.bidbatl.dileveryapp.ui.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.ui.operation.OperationViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
enum class ActionType{
    Del_Summary,
    Coll_Summary,
    Goods_return,
}
class OperationFragment : DaggerFragment() {
    lateinit var deliverySummaryButton :Button
    lateinit var collectionSummaryButton :Button
    lateinit var goodsReturnButton :Button
    private lateinit var listener : OnActionClicked
    private lateinit var operationViewModel: OperationViewModel
    @Inject
    lateinit var viewProvider:ViewModelProviderFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_operation, container, false)
        deliverySummaryButton = view.findViewById(R.id.button_del_summary)
        collectionSummaryButton = view.findViewById(R.id.button_coll_summary)
        goodsReturnButton = view.findViewById(R.id.button_goods_return)
        deliverySummaryButton.setOnClickListener {
          showDeliverySummary()
        }
        collectionSummaryButton.setOnClickListener {
            showCollectionSummary()
        }
        goodsReturnButton.setOnClickListener {
            goodsReturn()
        }
        initVM()

        return  view
    }
    private fun initVM(){
        operationViewModel = ViewModelProvider(this,viewProvider).get(OperationViewModel::class.java)
//        operationViewModel.getDeliverySummary("1").observe(viewLifecycleOwner, Observer {
//
//        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnActionClicked) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement OnDogSelected.")
        }
    }
    private fun showDeliverySummary() {
    listener.actionSelected(ActionType.Del_Summary)
    }

    private fun showCollectionSummary() {
        listener.actionSelected(ActionType.Coll_Summary)

    }
    private fun goodsReturn() {
        listener.actionSelected(ActionType.Goods_return)

    }

    companion object {

        // TODO: Rename and change types and number of parameters
        fun newInstance() =
            OperationFragment()
    }
    interface OnActionClicked{
        fun actionSelected(actionType: ActionType)

    }

}
