package com.apjake.spacexlaunchesbetterhr.presentation.launches.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apjake.spacexlaunchesbetterhr.common.util.AppConstants
import com.apjake.spacexlaunchesbetterhr.common.util.Resource
import com.apjake.spacexlaunchesbetterhr.domain.model.Launch
import com.apjake.spacexlaunchesbetterhr.domain.usecase.GetLaunchListUseCase
import com.apjake.spacexlaunchesbetterhr.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchListViewModel @Inject constructor(
    private val getLaunchListUseCase: GetLaunchListUseCase
): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
    private val _event = Channel<UiEvent>()
    val event = _event.receiveAsFlow()

    init {
        reloadLaunchList()
    }

    fun reloadLaunchList(){
        _state.value = UiState()
        loadMore()
    }

    fun loadMore(){
        if(_state.value.hasMore){
            viewModelScope.launch {
                getLaunchListUseCase(_state.value.nextOffset).collect{ result ->
                    when(result){
                        is Resource.Loading -> _state.value = _state.value.copy(
                            isLoading = true
                        )
                        is Resource.Error -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                            )
                            _event.send(UiEvent.ShowErrorSnackBar(result.message?: "Unknown error"))
                        }
                        is Resource.Success ->{
                            val launches = result.data.orEmpty()
                            if(launches.isEmpty()){
                                _state.value = _state.value.copy(
                                    isLoading = false,
                                    hasMore = false
                                )
                                _event.send(UiEvent.ShowErrorSnackBar("No more data"))
                            }else{
                                _state.value = _state.value.copy(
                                    isLoading = false,
                                    nextOffset = _state.value.nextOffset+AppConstants.DEFAULT_LAUNCH_LIST_LIMIT,
                                    launches = (_state.value.launches + launches).toMutableList()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

data class UiState(
    val isLoading: Boolean = false,
    val launches: MutableList<Launch> = mutableListOf(),
    val hasMore: Boolean = true,
    val nextOffset: Int = 0
)