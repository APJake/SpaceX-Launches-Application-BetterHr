package com.apjake.spacexlaunchesbetterhr.presentation.launches.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.apjake.spacexlaunchesbetterhr.R
import com.apjake.spacexlaunchesbetterhr.common.util.show
import com.apjake.spacexlaunchesbetterhr.common.util.showSnackBar
import com.apjake.spacexlaunchesbetterhr.databinding.FragmentLaunchDetailBinding
import com.apjake.spacexlaunchesbetterhr.domain.model.LaunchDetail
import com.apjake.spacexlaunchesbetterhr.presentation.util.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class LaunchDetailFragment : Fragment(){

    private val args: LaunchDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentLaunchDetailBinding

    @Inject
    lateinit var launchDetailViewModelAssistedFactory: LaunchDetailViewModel.LaunchDetailAssistedFactory
    private val viewModel: LaunchDetailViewModel by viewModels {
        LaunchDetailViewModel.provideFactory(
            launchDetailViewModelAssistedFactory,
            id = args.launchId,
            missionId = args.missionId
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()
    }

    private fun setUpObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collectLatest { state ->
                binding.pbLoading.isVisible = state.isLoading
                binding.llContainer.isVisible = !state.isLoading
                if(state.detail!=null)
                    setUpLaunchDetail(state.detail)
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

    private fun setUpLaunchDetail(detail: LaunchDetail) {
        binding.tvLaunchName.text = detail.missionName
        binding.tvLaunchDate.text = detail.date
        binding.tvDetail.text = detail.launchDetail
        binding.tvTwitterLink.text = getString(R.string.twitter_link, detail.twitterLink)
        binding.tvWikiLink.text = getString(R.string.wiki_link, detail.wikiLink)
        binding.ivLaunch.show(detail.image)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLaunchDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

}