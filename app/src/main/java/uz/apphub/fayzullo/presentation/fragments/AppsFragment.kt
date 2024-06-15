package uz.apphub.fayzullo.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.databinding.FragmentAppsBinding
import uz.apphub.fayzullo.domain.model.MyApps
import uz.apphub.fayzullo.presentation.adapter.AppsAdapter
import uz.apphub.fayzullo.utils.AppClickListener
import uz.apphub.fayzullo.utils.AppPermissionsHelper

@AndroidEntryPoint
class AppsFragment : Fragment(R.layout.fragment_apps),AppClickListener {

    private var _binding: FragmentAppsBinding? = null
    private val binding get() = _binding!!

    private var appList: List<MyApps> = arrayListOf()
    private var isPlayMarket : Int = 0
    private var adapter: AppsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAppsBinding.bind(view)

        appList = AppPermissionsHelper.getInstalledApps(requireContext())
        isPlayMarket = requireArguments().getInt("isPlayMarket")
        initAdapter()
        tabClick()
        initSortList("SMS")
    }

    private fun tabClick() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tabItemClicked(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun tabItemClicked(position: Int) {
        when (position) {
            0 -> {
                initSortList("SMS")
            }

            1 -> {
                initSortList("LOCATION")
            }

            2 -> {
                initSortList("CAMERA")
            }

            3 -> {
                initSortList("PHONE")
            }

            4 -> {
                initSortList("STORAGE")
            }

            5 -> {
                initSortList("INTERNET")
            }

            6 -> {
                initSortList("MICROPHONE")
            }

            7 -> {
                initSortList("CONTACTS")
            }
        }
    }

    private fun initSortList(text: String) {
        val myApps = mutableListOf<MyApps>()
        appList.forEach { app ->
            if (app.permissionModels.any {
                    it.name.contains(text, ignoreCase = true) && it.isGranted
                }) {
                myApps.add(app)
            }
        }
        initList(myApps.toList())
    }





    private fun initAdapter() {
        adapter = AppsAdapter()
        adapter?.initListener(this)
        binding.rvApps.adapter = adapter
    }

    private fun initList(myApps: List<MyApps>) {
        val filteredList = when (this.isPlayMarket) {
            0 -> myApps // Installed apps
            1 -> myApps.filter { it.isPlayMarket } // Play Market apps
            2 -> myApps.filter { !it.isPlayMarket } // Other sources
            else -> myApps
        }
        adapter?.initList(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(app: MyApps) {
        findNavController().navigate(
            R.id.action_appsFragment_to_permissionsFragment,
            bundleOf(
                "appName" to app.appName
            )
        )
    }
}

