package com.apjake.spacexlaunchesbetterhr.presentation.launches.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.apjake.spacexlaunchesbetterhr.common.util.Resource
import com.apjake.spacexlaunchesbetterhr.domain.model.LaunchDetail
import com.apjake.spacexlaunchesbetterhr.domain.usecase.GetLaunchDetailUseCase
import com.apjake.spacexlaunchesbetterhr.presentation.util.UiEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LaunchDetailViewModel @AssistedInject constructor(
    private val getLaunchDetailUseCase: GetLaunchDetailUseCase,
    @Assisted("id") private val id: String,
    @Assisted("missionId") private val missionId: String
): ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
    private val _event = Channel<UiEvent>()
    val event = _event.receiveAsFlow()

    init {
        loadLaunchDetail()
    }

    fun loadLaunchDetail(){
        viewModelScope.launch {
            getLaunchDetailUseCase(id, missionId).collectLatest { result ->
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
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            detail = result.data
                        )
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface LaunchDetailAssistedFactory{
        fun create(
            @Assisted("id") id: String,
            @Assisted("missionId") missionId: String
        ): LaunchDetailViewModel
    }
    companion object{
        fun provideFactory(
            assistedFactory: LaunchDetailAssistedFactory,
            id: String,
            missionId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(id, missionId) as T
            }
        }
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val detail: LaunchDetail? = null
)