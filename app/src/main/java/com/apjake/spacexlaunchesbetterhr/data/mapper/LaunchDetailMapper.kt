package com.apjake.spacexlaunchesbetterhr.data.mapper

import com.apjake.spacexlaunchesbetterhr.common.base.UniMapper
import com.apjake.spacexlaunchesbetterhr.domain.model.LaunchDetail
import com.example.rocketreserver.LaunchDetailQuery

object LaunchDetailMapper: UniMapper<LaunchDetailQuery.Data, LaunchDetail?> {
    override fun map(data: LaunchDetailQuery.Data): LaunchDetail? {
        if(data.launch == null && data.mission == null ) return null
        val images = data.launch?.links?.flickr_images.orEmpty()
        return LaunchDetail(
            launchDetail = data.launch?.details.orEmpty(),
            missionName = data.mission?.name.orEmpty(),
            twitterLink = data.mission?.twitter.orEmpty(),
            wikiLink = data.mission?.wikipedia.orEmpty(),
            image = if(images.isNotEmpty()) images.first().orEmpty() else "",
            date = data.launch?.launch_date_utc.toString()
        )
    }
}