package com.status102.bilibili.api.live.area

import com.google.gson.JsonObject
import com.status102.bilibili.api.live.area.entity.AreaList
import com.status102.bilibili.api.live.area.entity.RoomList
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface LiveAreaService {
	companion object {
		const val BASE_URL = "https://api.live.bilibili.com"
	}

	/**
	 * 获取直播间分区下tag标签(1 0 2为总表, 0 x 2为某区的表但两者回包结构不同)
	 * @param needEntrance 1
	 * @param parentId 0
	 * @param sourceId 2
	 */
	@GET("/room/v1/Area/getList")
	suspend fun getAreaList(
		@Query("need_entrance") needEntrance: Int,
		@Query("parent_id") parentId: Int,
		@Query("source_id") sourceId: Int
	): AreaList

	@GET("/room/v1/Area/getRoomList")
	suspend fun getRoomList(
		@Query("parent_area_id") parentAreaId: Int,
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int=20
	): RoomList
}