package com.bidbatl.dileveryapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bidbatl.dileveryapp.model.Profile
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Resource
import com.bidbatl.dileveryapp.repositories.ProfileRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository):ViewModel() {
    fun test(){
        println("Profile")
    }
    fun getProfile():LiveData<Resource<Profile>>{
       return profileRepository.getUserProfile()
    }

}