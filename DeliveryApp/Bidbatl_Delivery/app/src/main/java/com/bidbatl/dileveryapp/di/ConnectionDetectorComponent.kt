package com.bidbatl.dileveryapp.di

import com.bidbatl.dileveryapp.util.ConnectionDetector
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface ConnectionDetectorComponent {
    fun connectionDetector(): ConnectionDetector
}