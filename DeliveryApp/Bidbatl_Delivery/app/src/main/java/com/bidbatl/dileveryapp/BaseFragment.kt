package com.bidbatl.dileveryapp

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.preference.Preference
import dagger.android.support.DaggerFragment
import java.util.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
open class BaseFragment : DaggerFragment() {
    @Inject
    lateinit var preference: Preference
    var dialog: Dialog? = null
    private val dialogs = Vector<Dialog>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    open fun showCircleDialog() {
        dialog = Dialog(context!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.loading_dialog)
        dialog!!.show()
        dialogs.add(dialog)
    }

    fun showToastMessage(message:String){
        Toast.makeText(context,message, Toast.LENGTH_LONG).show()
    }
    open fun dismissDialog() {
        for (dialog in dialogs) if (dialog.isShowing) dialog.dismiss()
    }

}
