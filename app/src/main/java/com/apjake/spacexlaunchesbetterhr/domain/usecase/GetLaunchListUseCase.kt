package com.apjake.spacexlaunchesbetterhr.domain.usecase

import com.apjake.spacexlaunchesbetterhr.common.util.AppConstants
import com.apjake.spacexlaunchesbetterhr.domain.repository.SpaceXRepository
import javax.inject.Inject

class GetLaunchListUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    operator fun invoke(offset: Int = 0) = repository.getLaunchList(
        offset = offset,
        limit = AppConstants.DEFAULT_LAUNCH_LIST_LIMIT
    )
}