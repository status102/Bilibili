package com.status102.bilibili.api.live.area.entity

import java.io.Serializable

/*
{
	"code": 0,
	"msg": "success",
	"message": "success",
	"data": [{
		"id": 2,
		"name": "网游",
		"list": [{
			"id": "0",
			"parent_id": "2",
			"old_area_id": "0",
			"name": "全部网游",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 0,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/live/3aa12550186a2f8c4dd9352f6c3fcc82054e594d.png",
			"parent_name": "网游",
			"area_type": 1
		}, {
			"id": "102",
			"parent_id": "2",
			"old_area_id": "3",
			"name": "最终幻想14",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 1,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/vc/32a5b7da3e79feb394f538c9d95a858fea97b113.png",
			"parent_name": "网游",
			"area_type": 0
		}, {
			"id": "107",
			"parent_id": "2",
			"old_area_id": "1",
			"name": "其他游戏",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 0,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/vc/336fdcc2afc0d8ed0b98809802fafb4e6d41db71.png",
			"parent_name": "网游",
			"area_type": 0
		}]
	}, {
		"id": 3,
		"name": "手游",
		"list": [{
			"id": "0",
			"parent_id": "3",
			"old_area_id": "0",
			"name": "全部手游",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 0,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/live/77d7c5e206dde7621027542e514de2560256e858.png",
			"parent_name": "手游",
			"area_type": 1
		},{
			"id": "255",
			"parent_id": "3",
			"old_area_id": "12",
			"name": "明日方舟",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 0,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/vc/acdee9991422269f0b5b132a2d02ac2f3406372d.png",
			"parent_name": "手游",
			"area_type": 0
		}, {
			"id": "98",
			"parent_id": "3",
			"old_area_id": "12",
			"name": "其他手游",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 0,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/vc/e31422cdef5b54f8bfbab3fb53c718ba7964229e.png",
			"parent_name": "手游",
			"area_type": 0
		}]
	}, {
		"id": 6,
		"name": "单机",
		"list": [{
			"id": "0",
			"parent_id": "6",
			"old_area_id": "0",
			"name": "全部单机",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 0,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/live/827033eb0ac50db3d9f849abe8e39a5d3b1ecd53.png",
			"parent_name": "单机",
			"area_type": 1
		}, {
			"id": "216",
			"parent_id": "6",
			"old_area_id": "1",
			"name": "我的世界",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 0,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/vc/a5a2abb10cd6e06e41c9447ffb10fbd7bdf6b2d6.png",
			"parent_name": "单机",
			"area_type": 0
		}, {
			"id": "235",
			"parent_id": "6",
			"old_area_id": "1",
			"name": "其他单机",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 0,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/vc/5baf148ef7da6c4b3943282a6e89daa794984067.png",
			"parent_name": "单机",
			"area_type": 0
		}]
	}, {
		"id": 1,
		"name": "娱乐",
		"list": [{
			"id": "0",
			"parent_id": "1",
			"old_area_id": "0",
			"name": "全部娱乐",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 0,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/live/8fd5339dac84ec34e72f707f4c3b665d0aa41905.png",
			"parent_name": "娱乐",
			"area_type": 1
		}, {
			"id": "34",
			"parent_id": "1",
			"old_area_id": "7",
			"name": "音乐台",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 0,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/vc/8537694f4fe68ab0798dd5d493d3ca5deb908088.png",
			"cate_id": "3",
			"parent_name": "娱乐",
			"area_type": 0
		}]
	}, {
		"id": 5,
		"name": "电台",
		"list": [{
			"id": "0",
			"parent_id": "5",
			"old_area_id": "0",
			"name": "全部电台",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 0,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/live/a7adae1f7571a97f51d60f685823acc610d00a7e.png",
			"parent_name": "电台",
			"area_type": 1
		}, {
			"id": "192",
			"parent_id": "5",
			"old_area_id": "6",
			"name": "聊天电台",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 0,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/vc/a95b25ebec1e009870c2e3dd823e8cae50fa5223.png",
			"parent_name": "电台",
			"area_type": 0
		}]
	}, {
		"id": 4,
		"name": "绘画",
		"list": [{
			"id": "0",
			"parent_id": "4",
			"old_area_id": "0",
			"name": "全部绘画",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 0,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/live/fc6b69a3ae9c68b00d0c73a02685c9213c8fb356.png",
			"parent_name": "绘画",
			"area_type": 1
		}, {
			"id": "96",
			"parent_id": "4",
			"old_area_id": "9",
			"name": "其他绘画",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 0,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/vc/66198263edc675afd7862c7798b8bb9e7b5a3464.png",
			"parent_name": "绘画",
			"area_type": 0
		}]
	}, {
		"id": 8,
		"name": "战疫",
		"list": [{
			"id": "0",
			"parent_id": "8",
			"old_area_id": "0",
			"name": "全部战疫",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 0,
			"lock_status": "0",
			"pic": "https://i0.hdslb.com/bfs/live/c9208d317226f399998bf4e641a2711d393717d7.png",
			"parent_name": "战疫",
			"area_type": 1
		}, {
			"id": "315",
			"parent_id": "8",
			"old_area_id": "6",
			"name": "共抗疫情",
			"act_id": "0",
			"pk_status": "0",
			"hot_status": 1,
			"lock_status": "1",
			"pic": "https://i0.hdslb.com/bfs/vc/c9208d317226f399998bf4e641a2711d393717d7.png",
			"parent_name": "战疫",
			"area_type": 0
		}]
	}]
}
*/
data class AreaList(
	var code: Int,// 0
	var msg: String?,// success
	var message: String?,// success
	var data: List<Data>?
) : Serializable {
	data class Data(
		var id: Int ,
		var name: String,
		var list: kotlin.collections.List<List>
	) : Serializable {
		data class List(
			var id: String?,
			var parent_id: String?,
			var old_area_id: String?,
			var name: String?,
			var act_id: String?,
			var pk_status: String?,
			var hot_status: Int?,
			var lock_status: String?,
			var pic: String?,
			var parent_name: String?,
			var area_type: Int?
		)
	}
}