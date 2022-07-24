package com.status102.bilibili.api.live.wallet.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/*{
	* 	"code": 0,
	* 	"msg": "兑换成功",
	* 	"message": "兑换成功",
	* 	"data": {
	* 		"gold": "10300",
	* 		"silver": "2178010",
	* 		"tid": "438245741191f21879253069554dafc32141",
	* 		"coin": 1
	*        }
	* }
	*
 */
//{"code":403,"msg":"每天最多能兑换 1 个","message":"每天最多能兑换 1 个","data":[]}
data class Silver2Coin(
	@SerializedName("code")
	var code: Int,// 0
	@SerializedName("msg")
	var msg: String,// "兑换成功"
	@SerializedName("message")
	var message: String,// "兑换成功"
	@SerializedName("data")
	var data: Data
) : Serializable {
	data class Data(
		@SerializedName("gold")
		var gold: String,// "10300"
		@SerializedName("silver")
		var silver: String,// "2178010"
		@SerializedName("tid")
		var tid: String,// "438245741191f21879253069554dafc32141"
		@SerializedName("coin")
		var coin: Int// 1
	) : Serializable
}