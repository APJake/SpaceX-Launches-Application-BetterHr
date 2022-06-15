package com.apjake.spacexlaunchesbetterhr.data.repository

import com.apjake.spacexlaunchesbetterhr.common.util.Resource
import com.apjake.spacexlaunchesbetterhr.data.mapper.LaunchDetailMapper
import com.apjake.spacexlaunchesbetterhr.data.network.SpaceXNetworkDataSource
import com.apjake.spacexlaunchesbetterhr.data.mapper.LaunchMapper
import com.apjake.spacexlaunchesbetterhr.data.util.CustomException
import com.apjake.spacexlaunchesbetterhr.domain.model.Launch
import com.apjake.spacexlaunchesbetterhr.domain.model.LaunchDetail
import com.apjake.spacexlaunchesbetterhr.domain.repository.SpaceXRepository
import com.apjake.yoteshingeek.data.features.movies.util.GetErrorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SpaceXRepositoryImpl @Inject constructor(
    private val networkDataSource: SpaceXNetworkDataSource
): SpaceXRepository {
    override fun getLaunchList(offset: Int, limit: Int): Flow<Resource<List<Launch>>> = flow {
        emit(Resource.Loading())
        try {
            val data = networkDataSource.getLaunchList(
                offset = offset,
                limit = limit
            )
            emit(Resource.Success(data.map { LaunchMapper.map(it) }))
        }catch (e: HttpException){
            emit(Resource.Error(GetErrorMessage.fromException(e)))
        }catch (e: IOException){
            emit(Resource.Error(GetErrorMessage.fromException(e)))
        }catch (e: CustomException){
            emit(Resource.Error(GetErrorMessage.fromException(e)))
        }
    }

    override fun getLaunchDetail(id: String, missionId: String): Flow<Resource<LaunchDetail>> = flow {
        emit(Resource.Loading())
        try {
            val data = networkDataSource.getLaunchDetail(id, missionId)
            emit(Resource.Success(LaunchDetailMapper.map(data)))
        }catch (e: HttpException){
            emit(Resource.Error(GetErrorMessage.fromException(e)))
        }catch (e: IOException){
            emit(Resource.Error(GetErrorMessage.fromException(e)))
        }catch (e: CustomException){
            emit(Resource.Error(GetErrorMessage.fromException(e)))
        }
    }
}