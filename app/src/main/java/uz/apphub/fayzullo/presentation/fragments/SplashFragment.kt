package uz.apphub.fayzullo.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.databinding.FragmentSplashBinding
import uz.apphub.fayzullo.domain.model.SplashItemModel
import uz.apphub.fayzullo.presentation.adapter.ViewPagerAdapter
import uz.apphub.fayzullo.presentation.viewmodel.SplashViewModel
import uz.apphub.fayzullo.utils.AppPermissionsHelper

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash),
    ViewPagerAdapter.OnSplashButtonClickListener {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val viewPagerList = listOf(
        SplashItemModel("Biz bilan qurilmangizni xavfsizlikda saqlang.", R.drawable.ic_yulduz),
        SplashItemModel(
            "Qurilmangizni (o‘zingizni) kiber firibgarlardan asrang ",
            R.drawable.ic_yulduz
        ),
        SplashItemModel(
            "O’z shaxsiy hayotingizni kiberbuzg’unchilarga berib qo’ymang!",
            R.drawable.ic_yulduz
        ),
        SplashItemModel("O’z qurilmangni – birga asraymiz !", R.drawable.ic_yulduz)
    )

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSplashBinding.bind(view)

        lifecycleScope.launch {
            AppPermissionsHelper.getInstalledApps(requireContext())
            startViewPager()
        }
    }

    private fun startViewPager() {
        if (viewModel.isFirstLaunch()) {
            binding.viewPager2.visibility = View.VISIBLE
            val adapter = ViewPagerAdapter(viewPagerList)
            adapter.initListener(this)
            binding.viewPager2.adapter = adapter
        } else {
            binding.viewPager2.visibility = View.GONE
            openNewFragment()
        }
    }

    override fun onButtonClick(position: Int) {
        openNewFragment()
    }

    private fun openNewFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
    }
}
