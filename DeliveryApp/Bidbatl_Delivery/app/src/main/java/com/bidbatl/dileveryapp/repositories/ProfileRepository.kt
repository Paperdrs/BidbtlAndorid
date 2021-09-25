package com.bidbatl.dileveryapp.repositories

import androidx.lifecycle.LiveData
import com.bidbatl.dileveryapp.api.ProfileAPI
import com.bidbatl.dileveryapp.model.Profile
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.NetworkBoundResource
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Resource
import com.bidbatl.dileveryapp.util.AppExecutors
import com.bidbatl.dileveryapp.util.Common
import com.preference.Preference
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val appExecutors: AppExecutors,private val profileAPI: ProfileAPI,val preference: Preference) {
    fun getUserProfile():LiveData<Resource<Profile>>{
        return object :NetworkBoundResource<Profile,Profile>(appExecutors){
            override fun createCall() = profileAPI.getUserProfile(preference.getString(Common.token))
        }.asLiveData()
    }
}