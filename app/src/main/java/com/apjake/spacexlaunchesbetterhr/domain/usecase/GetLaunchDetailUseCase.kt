package com.apjake.spacexlaunchesbetterhr.domain.usecase

import com.apjake.spacexlaunchesbetterhr.domain.repository.SpaceXRepository
import javax.inject.Inject

class GetLaunchDetailUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    operator fun invoke(id: String, missionId: String) = repository.getLaunchDetail(
        id, missionId
    )
}