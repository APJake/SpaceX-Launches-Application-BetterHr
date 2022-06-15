package com.apjake.spacexlaunchesbetterhr.domain.repository

import com.apjake.spacexlaunchesbetterhr.common.util.Resource
import com.apjake.spacexlaunchesbetterhr.domain.model.Launch
import com.apjake.spacexlaunchesbetterhr.domain.model.LaunchDetail
import kotlinx.coroutines.flow.Flow

interface SpaceXRepository {
    fun getLaunchList(offset: Int, limit: Int): Flow<Resource<List<Launch>>>
    fun getLaunchDetail(id: String, missionId: String): Flow<Resource<LaunchDetail>>
}