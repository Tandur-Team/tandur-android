package com.tandurteam.tandur.dashboard.myplantlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.adapter.SectionPagerAdapter
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.FragmentMyPlantBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPlantFragment : Fragment() {

    private val viewModel: MyPlantViewModel by viewModel()
    private var _binding: FragmentMyPlantBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPlantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set view pager adapter and tab layout
        val sectionPagerAdapter = SectionPagerAdapter(this)
        with(binding) {
            viewPager.adapter = sectionPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }

        // on create plant clicked
        binding.btnTanamBaru.setOnClickListener {
            val action = MyPlantFragmentDirections.navigateToChooseFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }
    }

    fun navigateToMyPlantDetail(plantName: String, plantId: String) {
        val action = MyPlantFragmentDirections.navigateToMyDetailPlantFragment(plantName, plantId)
        Navigation.findNavController(binding.root).navigate(action)
    }

    override fun onResume() {
        super.onResume()

        // show bottom nav
        (requireActivity() as DashboardActivity).setBottomNavVisibility(true)
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }
}