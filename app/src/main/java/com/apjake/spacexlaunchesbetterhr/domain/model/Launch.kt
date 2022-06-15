package com.apjake.spacexlaunchesbetterhr.domain.model

import com.example.rocketreserver.LaunchListQuery

data class Launch(
    val id: String,
    val launchDate: String,
    val missionName: String,
    val missionId: String,
    val images: List<String>,
)