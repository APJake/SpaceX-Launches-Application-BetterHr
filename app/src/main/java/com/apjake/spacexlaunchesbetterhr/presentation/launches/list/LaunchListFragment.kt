package com.apjake.spacexlaunchesbetterhr.presentation.launches.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.apjake.spacexlaunchesbetterhr.common.util.showSnackBar
import com.apjake.spacexlaunchesbetterhr.databinding.FragmentLaunchListBinding
import com.apjake.spacexlaunchesbetterhr.presentation.util.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LaunchListFragment : Fragment(){

    private lateinit var binding: FragmentLaunchListBinding
    private val viewModel: LaunchListViewModel by viewModels()
    private lateinit var adapter: LaunchListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        adapter = LaunchListAdapter(onEndReach = {
            viewModel.loadMore()
        }) { launch ->
            val action = LaunchListFragmentDirections.actionLaunchListFragmentToLaunchDetailFragment(
                launchId = launch.id,
                missionId = launch.missionId
            )
            findNavController().navigate(action)
        }
        binding.rcyLaunches.adapter = adapter
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.rcyLaunches.layoutManager = gridLayoutManager

        setUpObservers()
    }

    private fun setUpObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collectLatest { state ->
                binding.pbLoading.isVisible = state.isLoading
                adapter.enableOnEndReach(state.hasMore)
                adapter.submitList(state.launches)
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.event.collectLatest { event ->
                when(event){
                    is UiEvent.ShowErrorSnackBar ->{
                        binding.root.showSnackBar(event.message)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLaunchListBinding.inflate(inflater, container, false)
        return binding.root
    }

}