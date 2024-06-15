package uz.apphub.fayzullo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.apphub.chopar2.data.local.AppSharedPref
import javax.inject.Inject

/**Created By Begzod Shokirov on 11/06/2024 **/

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPref: AppSharedPref
) : ViewModel() {

    fun isFirstLaunch(): Boolean = sharedPref.isFirstLaunch()
}