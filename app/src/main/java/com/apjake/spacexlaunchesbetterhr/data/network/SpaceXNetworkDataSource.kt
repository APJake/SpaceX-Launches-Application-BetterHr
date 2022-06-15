package com.apjake.spacexlaunchesbetterhr.data.network

import com.apjake.spacexlaunchesbetterhr.data.util.CustomException
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.example.rocketreserver.LaunchDetailQuery
import com.example.rocketreserver.LaunchListQuery
import javax.inject.Inject

class SpaceXNetworkDataSource @Inject constructor(
    private val client: ApolloClient
) {

    suspend fun getLaunchList(offset: Int, limit: Int): List<LaunchListQuery.Launch>{
        val response = client.query(LaunchListQuery(
            limit = Optional.Present(limit),
            offset = Optional.Present(offset)
        )).execute()
        if(response.hasErrors()){
            throw CustomException(response.errors?.get(0)?.message?: "Response has unknown error")
        }
        if(response.data?.launches == null){
            throw CustomException("No response found!!!")
        }
        return response.data?.launches.orEmpty().filterNotNull()
    }

    suspend fun getLaunchDetail(id: String, missionId: String): LaunchDetailQuery.Data {
        val response = client.query(LaunchDetailQuery(
            id = id,
            missionId = missionId
        )).execute()
        if(response.hasErrors()){
            throw CustomException(response.errors?.get(0)?.message?: "Response has unknown error")
        }
        if(response.data == null){
            throw CustomException("No response found!!!")
        }
        return response.data!!
    }

}