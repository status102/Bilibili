package com.status102.bilibili.api.main.judgement

import com.google.gson.JsonObject
import com.status102.bilibili.api.main.judgement.entity.*
import retrofit2.http.*

/**
 * 风纪投票并不需要accessKey和sigh
 */
interface JudgementService {
	companion object {
		const val BASE_URL = "https://api.bilibili.com"
	}

	/**
	 * 获取自身信息
	 */
	@GET("/x/credit/jury/jury")
	suspend fun getMyInfo(
		@Query("jsonp") jsonp: String = "jsonp",
		@Query("_") ignore: Long = System.currentTimeMillis()
	): MyInfo

	/**
	 * 获取新案件
	 */
	@POST("/x/credit/jury/caseObtain")
	@FormUrlEncoded
	suspend fun getNewCase(@Field("csrf") csrf: String, @Field("jsonp") jsonp: String = "jsonp"): NewCase

	/**
	 * 获取案件信息（注意：有些案件没有投票数，建议都参考众议观点）
	 */
	@GET("/x/credit/jury/juryCase")
	suspend fun getCaseInfo(
		@Query("cid") cid: Long,
		@Query("csrf") csrf: String,
		@Query("jsonp") jsonp: String = "jsonp"
	): CaseInfo

	/**
	 * 获取众议观点
	 * @param ps 单次获取观点数，上限10
	 */
	@GET("/x/credit/jury/vote/opinion")
	suspend fun getOpinion(
		@Query("cid") cid: Long,
		@Query("pn") pn: Int,
		@Query("ps") ps: Int = 3,
		@Query("jsonp") jsonp: String = "jsonp",
		@Query("_") ignore: Long = System.currentTimeMillis()
	): Opinion
	//cid=1017251&otype=1&pn=1&ps=8&_=1587783271451

	/**
	 * 执行投票
	 * @param cid 案件数
	 * @param vote 封禁1，否2，弃权3，删除4
	 * @param attr 是否公开，是1否0
	 */
	@POST("/x/credit/jury/vote")
	@FormUrlEncoded
	suspend fun vote(
		@Field("csrf") csrf: String,
		@Field("cid") cid: Long,
		@Field("vote") vote: Int,

		@Field("content") content: String = "",
		@Field("likes") likes: String = "",
		@Field("hates") hates: String = "",
		@Field("attr") attr: Int = 0,
		@Field("jsonp") jsonp: String = "jsonp"
	): Vote

}