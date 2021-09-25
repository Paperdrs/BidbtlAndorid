package com.bidbatl.dileveryapp.ui.profile

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bidbatl.dileveryapp.BaseActivity
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.ActivityMyProfileBinding
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.model.Profile
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Status
import com.bumptech.glide.Glide
import javax.inject.Inject

class MyProfileActivity : BaseActivity() {
    lateinit var profileViewModel: ProfileViewModel

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    lateinit var toolbar: Toolbar
    lateinit var profileBindig: ActivityMyProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileBindig = DataBindingUtil.setContentView(this, R.layout.activity_my_profile)
        initVM()
        setToolBar()
    }

    private fun initVM() {
        profileViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(ProfileViewModel::class.java)
        showCircleDialog()
        profileViewModel.getProfile().observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                dismissDialog()
                if (it.data?.code == 200){
                    setProfileData(it.data)
                }
                else{

                }

            }
            if (it.status == Status.ERROR) {
                dismissDialog()

            }

        })
    }

    private fun setProfileData(profileData: Profile) {
        profileBindig.textViewName.setText(profileData.data.name)
        profileBindig.editTextEmployeeCode.setText(profileData.data.employee_code)
        profileBindig.textViewDob.setText(profileData.data.dob)
        if (profileData.data.address != null){
            profileBindig.textViewAddress.setText(profileData.data.address.address)

        }
        profileBindig.textViewDlNo.setText(profileData.data.dl_number)
        profileBindig.textViewIdProof.setText(profileData.data.id_proof.id_number)
        Glide.with(this).load(profileData.data.photo).placeholder(R.drawable.user_photo).into(profileBindig.imageViewProfile)


    }

    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar_normal)
        val backAction = toolbar.findViewById<ImageView>(R.id.imageView_back)
        val title = toolbar.findViewById<TextView>(R.id.textView_title)
        title.text = getString(R.string.profile_title)
        backAction.setOnClickListener {
            backPressed()
        }
        setSupportActionBar(toolbar)
    }
}
