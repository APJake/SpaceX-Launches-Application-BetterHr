package com.apjake.spacexlaunchesbetterhr.data.mapper

import com.apjake.spacexlaunchesbetterhr.common.base.UniMapper
import com.apjake.spacexlaunchesbetterhr.domain.model.Launch
import com.example.rocketreserver.LaunchListQuery

object LaunchMapper: UniMapper<LaunchListQuery.Launch, Launch> {
    override fun map(data: LaunchListQuery.Launch): Launch {
        return Launch(
            id = data.id.orEmpty(),
            launchDate = data.launch_date_local.toString(),
            missionName = data.mission_name.orEmpty(),
            missionId = if(data.mission_id.isNullOrEmpty()) "" else data.mission_id.first().orEmpty(),
            images = data.links?.flickr_images.orEmpty().filterNotNull()
        )
    }
}