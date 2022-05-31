package com.tandurteam.tandur.dashboard.myplantlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.adapter.MyPlantListAdapter
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.databinding.FragmentListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : Fragment() {

    private val viewModel: MyPlantViewModel by viewModel()
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MyPlantListAdapter
    private var index: Int = 0
    private var query = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get index from adapter's bundle
        index = arguments?.getInt(ARG_SECTION_NUMBER, 0) ?: 0
        query = arguments?.getString(ARG_QUERY) ?: ""

        // init adapter
        adapter = MyPlantListAdapter()
        adapter.onItemClick = { myPlant ->
            if (myPlant.plantName != null && myPlant.id != null) {
                (parentFragment as MyPlantFragment).navigateToMyPlantDetail(
                    myPlant.plantName,
                    myPlant.id
                )
            }
        }

        index.let { isHarvested ->
            Log.d(TAG, "onViewCreated: $index")

            // on swipe refresh
            binding.swipeRefresh.setOnRefreshListener { observeLiveData(isHarvested) }

            // observe live data
            observeLiveData(isHarvested)
        }
    }

    private fun observeLiveData(isHarvested: Int = 0) {
        viewModel.getAllMyPlant(isHarvested, query).observe(viewLifecycleOwner) {
            it?.let { myPlantList ->
                when (myPlantList) {
                    is ApiResponse.Loading -> {
                        binding.swipeRefresh.isRefreshing = true
                        binding.tvError.visibility = View.GONE
                    }

                    is ApiResponse.Success -> {
                        binding.swipeRefresh.isRefreshing = false
                        binding.tvError.visibility = View.GONE

                        // set adapter of rvListMovie
                        adapter.setData(myPlantList.data.data)
                        binding.rvTanamanku.apply {
                            setHasFixedSize(true)
                            adapter = this@ListFragment.adapter
                        }
                    }

                    is ApiResponse.Error -> {
                        binding.swipeRefresh.isRefreshing = false

                        // set visibility
                        binding.tvError.visibility = View.VISIBLE
                        binding.rvTanamanku.visibility = View.GONE

                        // set message
                        binding.tvError.text =
                            requireContext().getString(R.string.error_connecting_to_api)
                    }

                    is ApiResponse.Empty -> {
                        binding.swipeRefresh.isRefreshing = false

                        // set visibility
                        binding.tvError.visibility = View.VISIBLE
                        binding.rvTanamanku.visibility = View.GONE

                        // set message
                        binding.tvError.text = requireContext().getString(R.string.empty_my_plant)
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = ListFragment::class.java.simpleName
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_QUERY = "query"
    }
}