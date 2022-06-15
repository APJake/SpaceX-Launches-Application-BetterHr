package com.apjake.spacexlaunchesbetterhr.presentation.util

sealed class UiEvent {
    data class ShowErrorSnackBar(val message: String): UiEvent()
}