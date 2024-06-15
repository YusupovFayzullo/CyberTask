package uz.apphub.fayzullo.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.databinding.FragmentPermissionsBinding
import uz.apphub.fayzullo.domain.model.MyApps
import uz.apphub.fayzullo.domain.model.PermissionModel
import uz.apphub.fayzullo.presentation.adapter.PermissionAdapter
import uz.apphub.fayzullo.utils.AppPermissionsHelper

@AndroidEntryPoint
class PermissionsFragment : Fragment(R.layout.fragment_permissions) {

    private var _binding: FragmentPermissionsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPermissionsBinding.bind(view)
        initViews()
    }

    private fun initViews() {
        val appName = requireArguments().getString("appName")
        val apps: List<MyApps> = AppPermissionsHelper.getInstalledApps(requireContext())
        var permissionModels = listOf<PermissionModel>()
        apps.forEach {
            if (it.appName == appName) {
                permissionModels = it.permissionModels
            }
        }

        binding.tvAppName.text = appName
        val adapter = PermissionAdapter(permissionModels)
        binding.rvPermissions.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

