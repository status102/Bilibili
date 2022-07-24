package com.status102.bilibili.api.live.room.entity.app

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/*
{
	"code": 0,
	"message": "0",
	"ttl": 1,
	"data": {
		"room_info": {
			"uid": 99748932,
			"room_id": 3990262,
			"short_id": 0,
			"title": "荣耀20S新品发布会：看直播，赢新机！",
			"cover": "http://i0.hdslb.com/bfs/live/room_cover/cacbc329bf0594caf7e9191ef103d788f626a643.jpg",
			"tags": "荣耀手机,荣耀20系列,荣耀20S,很吓人的技术,李现",
			"background": "1",
			"description": "\u003cp\u003e耀耀切克闹，直播唠嗑来一套。\u003c/p\u003e\n\u003cp\u003e每逢新品必直播，没有新品偶尔播。\u003c/p\u003e\n\u003cp\u003e（大多数情况下，直播都会抽奖）\u003c/p\u003e\n\u003cp\u003e              ----一个努力不掉粉的官号UP主\u003c/p\u003e",
			"online": 480179,
			"live_status": 1,
			"live_start_time": 1567593987,
			"live_screen_type": 0,
			"lock_status": 0,
			"lock_time": 0,
			"hidden_status": 0,
			"hidden_time": 0,
			"area_id": 123,
			"area_name": "户外",
			"parent_area_id": 1,
			"parent_area_name": "娱乐",
			"keyframe": "http://i0.hdslb.com/bfs/live/3990262.jpg?09042010",
			"special_type": 0,
			"up_session": "l:one:live:record:3990262:1567593987",
			"pk_status": 0,
			"pendants": {
				"frame": {
					"name": "",
					"position": 0,
					"value": "",
					"desc": ""
				},
				"badge": null
			},
			"on_voice_join": 0
		},
		"anchor_info": {
			"base_info": {
				"uname": "荣耀手机",
				"face": "http://i2.hdslb.com/bfs/face/68ecb50a503e10dfeb49fed51b4738b33d0c8e44.jpg",
				"gender": "保密",
				"official_info": {
					"role": 1,
					"title": "荣耀手机官方账号",
					"desc": ""
				}
			},
			"live_info": {
				"level": 22,
				"level_color": 10512625
			},
			"relation_info": {
				"attention": 446286
			}
		},
		"tab_info": [{
			"type": "interaction",
			"desc": "互动",
			"url": "",
			"status": 1,
			"order": 100,
			"default": 1,
			"default_sub_tab": "",
			"sub_tab": []
		}, {
			"type": "up-tab",
			"desc": "主播",
			"url": "",
			"status": 1,
			"order": 200,
			"default": 0,
			"default_sub_tab": "video",
			"sub_tab": [{
				"type": "video",
				"desc": "视频",
				"url": "",
				"status": 1,
				"order": 201,
				"documents": "",
				"rank_name": "",
				"default_sub_tab": "",
				"grand_tab": []
			}, {
				"type": "dynamic",
				"desc": "动态",
				"url": "",
				"status": 1,
				"order": 202,
				"documents": "",
				"rank_name": "",
				"default_sub_tab": "",
				"grand_tab": []
			}]
		}, {
			"type": "contribution-rank",
			"desc": "贡献榜",
			"url": "",
			"status": 1,
			"order": 300,
			"default": 0,
			"default_sub_tab": "gold-rank",
			"sub_tab": [{
				"type": "gold-rank",
				"desc": "金瓜子榜",
				"url": "",
				"status": 1,
				"order": 302,
				"documents": "按最近七日的金瓜子贡献排序，最多展示前20名用户",
				"rank_name": "",
				"default_sub_tab": "",
				"grand_tab": []
			}, {
				"type": "gift-rank",
				"desc": "礼物榜",
				"url": "",
				"status": 1,
				"order": 303,
				"documents": "",
				"rank_name": "",
				"default_sub_tab": "today-rank",
				"grand_tab": [{
					"type": "today-rank",
					"desc": "今日榜",
					"url": "",
					"status": 1,
					"order": 1,
					"documents": "按今日的送礼积分进行排序，最多展示前50名用户",
					"rank_name": ""
				}, {
					"type": "seven-rank",
					"desc": "七日榜",
					"url": "",
					"status": 1,
					"order": 2,
					"documents": "按最近7日的送礼积分排序，最多展示前50名用户",
					"rank_name": ""
				}]
			}, {
				"type": "fans-rank",
				"desc": "粉丝榜",
				"url": "",
				"status": 1,
				"order": 304,
				"documents": "按粉丝勋章的等级排序，最多展示前50名用户",
				"rank_name": "",
				"default_sub_tab": "",
				"grand_tab": []
			}]
		}, {
			"type": "guard",
			"desc": "大航海",
			"url": "",
			"status": 1,
			"order": 400,
			"default": 0,
			"default_sub_tab": "",
			"sub_tab": []
		}, {
			"type": "more-live",
			"desc": "更多直播",
			"url": "",
			"status": 1,
			"order": 500,
			"default": 0,
			"default_sub_tab": "relative-recommend",
			"sub_tab": [{
				"type": "relative-recommend",
				"desc": "相关推荐",
				"url": "",
				"status": 1,
				"order": 501,
				"documents": "",
				"rank_name": "",
				"default_sub_tab": "",
				"grand_tab": []
			}, {
				"type": "view-history-sub",
				"desc": "观看历史",
				"url": "",
				"status": 1,
				"order": 502,
				"documents": "",
				"rank_name": "",
				"default_sub_tab": "",
				"grand_tab": []
			}]
		}, {
			"type": "love-club",
			"desc": "友爱社",
			"url": "",
			"status": 0,
			"order": 600,
			"default": 0,
			"default_sub_tab": "",
			"sub_tab": []
		}],
		"pk_info": null,
		"guard_info": {
			"count": 1,
			"achievement_level": 1
		},
		"guard_buy_info": {
			"count": 0,
			"duration": 86400,
			"list": []
		},
		"rankdb_info": {
			"rank_desc": "小时总榜",
			"color": "#FB7299",
			"h5_url": "https://live.bilibili.com/p/html/live-app-rankcurrent/index.html?is_live_half_webview=1\u0026hybrid_half_ui=1,5,85p,70p,FFE293,0,30,100,10;2,2,320,100p,FFE293,0,30,100,0;4,2,320,100p,FFE293,0,30,100,0;6,5,65p,60p,FFE293,0,30,100,10;5,5,55p,60p,FFE293,0,30,100,10;3,5,85p,70p,FFE293,0,30,100,10;7,5,65p,60p,FFE293,0,30,100,10;\u0026anchor_uid=99748932\u0026rank_type=master_realtime_hour_room\u0026area_hour=1\u0026area_v2_id=123\u0026area_v2_parent_id=1",
			"web_url": "https://live.bilibili.com/blackboard/room-current-rank.html?rank_type=master_realtime_hour_room\u0026area_hour=1\u0026area_v2_id=123\u0026area_v2_parent_id=1",
			"timestamp": 1567599202
		},
		"round_video_info": null,
		"anchor_reward": {
			"wish_open": false
		},
		"activity_banner_info": {
			"top": [{
				"id": 371,
				"expire_hour": 24,
				"activity_title": "mamo",
				"title": "",
				"cover": "http://i0.hdslb.com/bfs/live/6b6fca15e2bcc16998cefa90021b0656c9bbfd48.png",
				"jump_url": "https://www.bilibili.com/blackboard/live/activity-mamorumiyanoasialivetour2019-h5.html",
				"rank": "",
				"color": "",
				"is_close": 1,
				"type": 0,
				"rank_name": "",
				"gift_img": "",
				"week_gift_color": "",
				"week_text_color": "",
				"week_rank_color": "",
				"is_clock_in_over": 0,
				"gift_progress": []
			}, {
				"id": 199,
				"expire_hour": 24,
				"activity_title": "周星",
				"title": "排名",
				"cover": "http://i0.hdslb.com/bfs/live/c81f9ec0e09bc6d5dffa4f8f583971540f36541f.png",
				"jump_url": "https://live.bilibili.com/p/html/live-app-weekstar/index.html?is_live_half_webview=1\u0026hybrid_biz=live-app-weekStar\u0026hybrid_rotate_d=1\u0026hybrid_half_ui=1,3,100p,70p,300e51,0,30,100;2,2,375,100p,300e51,0,30,100;3,3,100p,70p,300e51,0,30,100;4,2,375,100p,300e51,0,30,100;5,3,100p,70p,300e51,0,30,100;6,3,100p,70p,300e51,0,30,100;7,3,100p,70p,300e51,0,30,100\u0026room_id=3990262\u0026tab=task",
				"rank": "597",
				"color": "",
				"is_close": 1,
				"type": 1,
				"rank_name": "么么哒",
				"gift_img": "https://s1.hdslb.com/bfs/vc/342180be748870201dadd6f4b88dbf77493c79bd.png",
				"week_gift_color": "#ffffff",
				"week_text_color": "#ffffff",
				"week_rank_color": "#ffffff",
				"is_clock_in_over": 1,
				"gift_progress": [{
					"pic": "http://i0.hdslb.com/bfs/live/5668f46bf2a0c57f943b925bdf4f2102a8464067.png",
					"current": 2,
					"num": 50
				}, {
					"pic": "http://i0.hdslb.com/bfs/live/084ff1eaa7832c4c1040772a5fec4911930bd107.png",
					"current": 0,
					"num": 1
				}, {
					"pic": "http://i0.hdslb.com/bfs/live/209e914b602b1accb6083c8a4f38297d8eff46dc.png",
					"current": 6,
					"num": 30
				}]
			}],
			"bottom": [],
			"inputBanner": [],
			"superBanner": null,
			"lol_activity": {
				"vote_cover": "http://i0.hdslb.com/bfs/live/6030cb2847f4d197caacb12fbe12f2656b999bcf.png",
				"guess_cover": "http://i0.hdslb.com/bfs/live/61d1c4bcce470080a5408d6c03b7b48e0a0fa8d7.png",
				"status": 0
			},
			"gift_banner": {
				"img": [],
				"interval": 0
			},
			"revenue_banner": {
				"fliping_interval": 8,
				"list": []
			}
		},
		"activity_score_info": null,
		"skin_info": {
			"id": 0,
			"skin_config": "",
			"start_time": 0,
			"end_time": 0,
			"current_time": 0
		},
		"activity_lol_match_info": {
			"match_id": 0,
			"status": 0,
			"round": 0,
			"team_info": [],
			"commentatorInfo": [],
			"vote_config": {
				"vote_nums": [1, 10, 2000],
				"price": 1000,
				"status": 0
			},
			"guess_info": [],
			"timestamp": 1567599203
		},
		"battle_info": null,
		"switch_info": {
			"close_guard": false,
			"close_gift": false,
			"close_online": false
		},
		"studio_info": null,
		"voice_join": {
			"voice_join_open": 0,
			"voice_join_status": {
				"room_status": 0,
				"status": 0,
				"uid": 0,
				"user_name": "",
				"head_pic": "",
				"guard": 0,
				"room_id": 0,
				"start_at": 0,
				"current_time": 1567599204
			},
			"vocie_join_columns": {
				"icon_close": "https://i0.hdslb.com/bfs/live/a176d879dffe8de1586a5eb54c2a08a0c7d31392.png",
				"icon_open": "https://i0.hdslb.com/bfs/live/70f0844c9a12d29db1e586485954290144534be9.png",
				"icon_wait": "https://i0.hdslb.com/bfs/live/1049bb88f1e7afd839cc1de80e13228ccd5807e8.png",
				"icon_starting": "https://i0.hdslb.com/bfs/live/948433d1647a0704f8216f017c406224f9fff518.gif"
			}
		}
	}
}
 */
//{"code":0,"message":"0","ttl":1,"data":{"room_info":{"uid":55411725,"room_id":2715784,"short_id":0,"title":"信仰鹿头，继续连败","cover":"","tags":"","background":"http://static.hdslb.com/live-static/images/bg/4.jpg","description":"","online":11,"live_status":0,"live_start_time":0,"live_screen_type":0,"lock_status":0,"lock_time":0,"hidden_status":0,"hidden_time":0,"area_id":163,"area_name":"第五人格","parent_area_id":3,"parent_area_name":"手游","keyframe":"http://i0.hdslb.com/bfs/live/2715784.jpg?07081820","special_type":0,"up_session":"","pk_status":0,"pendants":{"frame":{"name":"","position":0,"value":"","desc":""},"badge":null},"on_voice_join":0,"tv_screen_on":1,"room_type":{}},"anchor_info":{"base_info":{"uname":"雾中灯火","face":"http://i2.hdslb.com/bfs/face/a9fd7e530ceca83f07247a7e9d444d991c57c8d4.jpg","gender":"女","official_info":{"role":-1,"title":"","desc":""}},"live_info":{"level":16,"level_color":5805790},"relation_info":{"attention":555}},"tab_info":[{"type":"interaction","desc":"互动","url":"","status":1,"order":100,"default":1,"default_sub_tab":"","sub_tab":[]},{"type":"up-tab","desc":"主播","url":"","status":1,"order":200,"default":0,"default_sub_tab":"video","sub_tab":[{"type":"video","desc":"视频","url":"","status":1,"order":201,"documents":"","rank_name":"","default_sub_tab":"","grand_tab":[]},{"type":"dynamic","desc":"动态","url":"","status":1,"order":202,"documents":"","rank_name":"","default_sub_tab":"","grand_tab":[]}]},{"type":"contribution-rank","desc":"贡献榜","url":"","status":1,"order":300,"default":0,"default_sub_tab":"gold-rank","sub_tab":[{"type":"gold-rank","desc":"金瓜子榜","url":"","status":1,"order":302,"documents":"按最近七日的金瓜子贡献排序，最多展示前20名用户","rank_name":"","default_sub_tab":"","grand_tab":[]},{"type":"gift-rank","desc":"礼物榜","url":"","status":1,"order":303,"documents":"","rank_name":"","default_sub_tab":"seven-rank","grand_tab":[{"type":"today-rank","desc":"今日榜","url":"","status":1,"order":1,"documents":"按今日的送礼积分进行排序，最多展示前50名用户","rank_name":""},{"type":"seven-rank","desc":"七日榜","url":"","status":1,"order":2,"documents":"按最近7日的送礼积分排序，最多展示前50名用户","rank_name":""}]},{"type":"fans-rank","desc":"粉丝榜","url":"","status":1,"order":304,"documents":"按粉丝勋章的等级排序，最多展示前50名用户","rank_name":"","default_sub_tab":"","grand_tab":[]}]},{"type":"guard","desc":"大航海","url":"","status":1,"order":400,"default":0,"default_sub_tab":"","sub_tab":[]},{"type":"more-live","desc":"更多直播","url":"","status":1,"order":500,"default":0,"default_sub_tab":"relative-recommend","sub_tab":[{"type":"relative-recommend","desc":"相关推荐","url":"","status":1,"order":501,"documents":"","rank_name":"","default_sub_tab":"","grand_tab":[]},{"type":"view-history-sub","desc":"观看历史","url":"","status":1,"order":502,"documents":"","rank_name":"","default_sub_tab":"","grand_tab":[]}]},{"type":"love-club","desc":"友爱社","url":"","status":0,"order":600,"default":0,"default_sub_tab":"","sub_tab":[]}],"pk_info":null,"guard_info":{"count":0,"achievement_level":1},"guard_buy_info":{"count":0,"duration":86400,"list":[]},"rankdb_info":{"rank_desc":"小时总榜","color":"#FB7299","h5_url":"https://live.bilibili.com/p/html/live-app-rankcurrent/index.html?is_live_half_webview=1&hybrid_half_ui=1,5,85p,70p,FFE293,0,30,100,10;2,2,320,100p,FFE293,0,30,100,0;4,2,320,100p,FFE293,0,30,100,0;6,5,65p,60p,FFE293,0,30,100,10;5,5,55p,60p,FFE293,0,30,100,10;3,5,85p,70p,FFE293,0,30,100,10;7,5,65p,60p,FFE293,0,30,100,10;&anchor_uid=55411725&rank_type=master_realtime_hour_room&area_hour=1&area_v2_id=163&area_v2_parent_id=3","web_url":"https://live.bilibili.com/blackboard/room-current-rank.html?rank_type=master_realtime_hour_room&area_hour=1&area_v2_id=163&area_v2_parent_id=3","timestamp":1584023786},"round_video_info":null,"anchor_reward":{"wish_open":false},"activity_banner_info":{"top":[{"id":649,"expire_hour":24,"activity_title":"你的直播间长猫了","title":"","cover":"https://i0.hdslb.com/bfs/live/666ddd7fc1232c1249d5afca28606140b44dca7f.jpg","jump_url":"https://live.bilibili.com/blackboard/cat-2020-room.html?is_live_half_webview=1&room_id=2715784&width=376&height=600&hybrid_rotate_d=1&is_cling_player=1&hybrid_half_ui=1,3,100p,70p,f7efcb,0,30,100;2,2,375,100p,f7efcb,0,30,100;3,3,100p,70p,f7efcb,0,30,100;4,2,375,100p,f7efcb,0,30,100;5,3,100p,70p,f7efcb,0,30,100;6,3,100p,70p,f7efcb,0,30,100;7,3,100p,70p,f7efcb,0,30,100","rank":"","color":"","is_close":1,"type":0,"rank_name":"","gift_img":"","week_gift_color":"","week_text_color":"","week_rank_color":"","is_clock_in_over":0,"gift_progress":[]},{"id":199,"expire_hour":24,"activity_title":"周星","title":"排名","cover":"http://i0.hdslb.com/bfs/live/c81f9ec0e09bc6d5dffa4f8f583971540f36541f.png","jump_url":"https://live.bilibili.com/p/html/live-app-weekstar/index.html?is_live_half_webview=1&hybrid_biz=live-app-weekStar&hybrid_rotate_d=1&hybrid_half_ui=1,3,100p,70p,300e51,0,30,100;2,2,375,100p,300e51,0,30,100;3,3,100p,70p,300e51,0,30,100;4,2,375,100p,300e51,0,30,100;5,3,100p,70p,300e51,0,30,100;6,3,100p,70p,300e51,0,30,100;7,3,100p,70p,300e51,0,30,100&room_id=2715784&tab=task","rank":"","color":"","is_close":1,"type":1,"rank_name":"","gift_img":"","week_gift_color":"","week_text_color":"#ffffff","week_rank_color":"#ffffff","is_clock_in_over":1,"gift_progress":[{"pic":"http://i0.hdslb.com/bfs/live/5668f46bf2a0c57f943b925bdf4f2102a8464067.png","current":0,"num":50},{"pic":"http://i0.hdslb.com/bfs/live/084ff1eaa7832c4c1040772a5fec4911930bd107.png","current":0,"num":1},{"pic":"http://i0.hdslb.com/bfs/live/3e15a3150127db6183675f09cedc57db882da152.png","current":0,"num":30}]}],"bottom":[{"id":651,"expire_hour":24,"activity_title":"直播间长猫啦","title":"","cover":"https://i0.hdslb.com/bfs/live/09a870d6763863a6ce77ed80053c2fe723e1f20d.png","jump_url":"https://live.bilibili.com/blackboard/cat-2020-h5.html?is_live_full_webview=1&from=package#/","rank":"","color":"","is_close":1,"type":0,"rank_name":"","gift_img":"","week_gift_color":"","week_text_color":"","week_rank_color":"","is_clock_in_over":0,"gift_progress":null}],"inputBanner":[],"superBanner":null,"lol_activity":{"vote_cover":"https://i0.hdslb.com/bfs/activity-plat/static/20190930/4ae8d4def1bbff9483154866490975c2/oWyasOpox.png","guess_cover":"http://i0.hdslb.com/bfs/live/61d1c4bcce470080a5408d6c03b7b48e0a0fa8d7.png","status":0,"vote_h5_url":"https://live.bilibili.com/p/html/live-app-wishhelp/index.html?is_live_half_webview=1&hybrid_biz=live-app-wishhelp&hybrid_rotate_d=1&hybrid_half_ui=1,3,100p,360,0c1333,0,30,100;2,2,375,100p,0c1333,0,30,100;3,3,100p,360,0c1333,0,30,100;4,2,375,100p,0c1333,0,30,100;5,3,100p,360,0c1333,0,30,100;6,2,100p,360,0c1333,0,30,100;7,3,100p,360,0c1333,0,30,100;8,3,100p,360,0c1333,0,30,100;","vote_use_h5":true},"gift_banner":{"img":[],"interval":0},"revenue_banner":{"fliping_interval":8,"list":[{"id":457,"title":"每日历险","cover":"","background":"https://i0.hdslb.com/bfs/live/3e4a0885e7aa8b0497b5b8642bfc280318aff2f1.png","jump_url":"https://live.bilibili.com/p/html/live-app-daily/index.html?is_live_half_webview=1&hybrid_rotate_d=1&hybrid_half_ui=1,3,100p,70p,7b6fd8,0,30,100;2,2,375,100p,7b6fd8,0,30,100;3,3,100p,70p,7b6fd8,0,30,100;4,2,375,100p,7b6fd8,0,30,100;5,3,100p,70p,7b6fd8,0,30,100;6,3,100p,70p,7b6fd8,0,30,100;7,3,100p,70p,7b6fd8,0,30,100&room_id=2715784#/","title_color":"#FFFFFf","closeable":1,"banner_type":0,"weight":100},{"id":378,"title":"未上榜","cover":"","background":"https://i0.hdslb.com/bfs/activity-plat/static/20190904/b5e210ef68e55c042f407870de28894b/6mLB2jCQV.png","jump_url":"https://live.bilibili.com/p/html/live-app-rankcurrent/index.html?is_live_half_webview=1&hybrid_half_ui=1,5,85p,70p,FFE293,0,30,100,10;2,2,320,100p,FFE293,0,30,100,0;4,2,320,100p,FFE293,0,30,100,0;6,5,65p,60p,FFE293,0,30,100,10;5,5,55p,60p,FFE293,0,30,100,10;3,5,85p,70p,FFE293,0,30,100,10;7,5,65p,60p,FFE293,0,30,100,10;&anchor_uid=55411725&is_new_rank_container=1&area_v2_id=163&area_v2_parent_id=3&rank_type=master_realtime_hour_room&area_hour=1","title_color":"#8B5817","closeable":1,"banner_type":4,"weight":18}]}},"activity_score_info":null,"skin_info":{"id":0,"skin_config":"","start_time":0,"end_time":0,"current_time":0},"activity_lol_match_info":{"match_id":0,"status":0,"round":0,"team_info":[],"commentatorInfo":[],"vote_config":{"vote_nums":[1,10,2000],"price":1000,"status":0},"guess_info":[],"timestamp":1584023786},"battle_info":null,"switch_info":{"close_guard":false,"close_gift":false,"close_online":false},"studio_info":null,"voice_join":{"voice_join_open":0,"voice_join_status":{"room_status":0,"status":0,"uid":0,"user_name":"","head_pic":"","guard":0,"room_id":2715784,"start_at":0,"current_time":1584023786},"vocie_join_columns":{"icon_close":"https://i0.hdslb.com/bfs/live/a176d879dffe8de1586a5eb54c2a08a0c7d31392.png","icon_open":"https://i0.hdslb.com/bfs/live/70f0844c9a12d29db1e586485954290144534be9.png","icon_wait":"https://i0.hdslb.com/bfs/live/1049bb88f1e7afd839cc1de80e13228ccd5807e8.png","icon_starting":"https://i0.hdslb.com/bfs/live/948433d1647a0704f8216f017c406224f9fff518.gif"}},"super_chat":{"status":0,"jump_url":"https://live.bilibili.com/p/html/live-app-superchat2/index.html?is_live_half_webview=1&hybrid_half_ui=1,3,100p,70p,ffffff,0,30,100;2,2,375,100p,ffffff,0,30,100;3,3,100p,70p,ffffff,0,30,100;4,2,375,100p,ffffff,0,30,100;5,3,100p,60p,ffffff,0,30,100;6,3,100p,60p,ffffff,0,30,100;7,3,100p,60p,ffffff,0,30,100&is_cling_player=1","icon":"https://i0.hdslb.com/bfs/live/0a9ebd72c76e9cbede9547386dd453475d4af6fe.png","ranked_mark":0,"message_list":[]},"room_config_info":{"dm_text":"发个弹幕呗~"},"gift_memory_info":{"list":[]}}}
/**
 * 进房初始化获取到的信息，data下没做完，能用就行
 */
data class IndexInfo(
	@SerializedName("code")
	var code: Int,// 0
	@SerializedName("message")
	var message: String,// "0"
	@SerializedName("ttl")
	var ttl: Int,// 1
	var data: Data
) : Serializable {
	data class Data(
		@SerializedName("room_info")
		var roomInfo: RoomInfo?,
		@SerializedName("anchor_info")
		var anchorInfo: AnchorInfo?
	) : Serializable {
		data class RoomInfo(
			@SerializedName("uid")
			var uid: Long?,// 99748932
			@SerializedName("room_id")
			var roomId: Long?,// 3990262
			@SerializedName("short_id")
			var shortId: Long?,// 不存在短号时为0
			@SerializedName("title")
			var title: String?,// 荣耀20S新品发布会：看直播，赢新机！
			@SerializedName("cover")
			var cover: String?,// http://i0.hdslb.com/bfs/live/room_cover/cacbc329bf0594caf7e9191ef103d788f626a643.jpg
			@SerializedName("tags")
			var tags: String?,// 荣耀手机,荣耀20系列,荣耀20S,很吓人的技术,李现
			@SerializedName("background")
			var background: String?,// "1"
			@SerializedName("description")
			var description: String?,// <p>耀耀切克闹，直播唠嗑来一套。</p><p>每逢新品必直播，没有新品偶尔播。</p><p>（大多数情况下，直播都会抽奖）</p><p>              ----一个努力不掉粉的官号UP主</p>
			@SerializedName("online")
			var online: Long?,// 480179
			@SerializedName("live_status")
			var liveStatus: Int?,// 1为开播
			@SerializedName("live_start_time")
			var liveStartTime: Long?,// 10位时间戳
			@SerializedName("live_screen_type")
			var liveScreenType: Int?,// 0
			@SerializedName("lock_status")
			var lockStatus: Int?,// 0
			@SerializedName("lock_time")
			var lockTime: Long?,// 0
			@SerializedName("hidden_status")
			var hiddenStatus: Int?,// 0
			@SerializedName("hidden_time")
			var hiddenTime: Long?,// 0
			@SerializedName("area_id")
			var areaId: Int?,// 123
			@SerializedName("area_name")
			var areaName: String?,// 户外
			@SerializedName("parent_area_id")
			var parentAreaId: Int?,// 1 主分区号
			@SerializedName("parent_area_name")
			var parentAreaName: String?,// 娱乐 主分区名
			@SerializedName("keyframe")
			var keyframe: String?,// http://i0.hdslb.com/bfs/live/3990262.jpg?09042010
			@SerializedName("special_type")
			var specialType: Int?,// 0
			@SerializedName("up_session")
			var upSession: String?,// l:one:live:record:3990262:1567593987
			@SerializedName("pk_status")
			var pkStatus: Int?,//0
			//@SerializedName("pendants")
			//var pendants:Pendant?,//
			@SerializedName("on_voice_join")
			var onVoiceJoin: Int?// 0
		) : Serializable

		data class AnchorInfo(
			@SerializedName("base_info")
			var baseInfo: BaseInfo?,
			@SerializedName("live_info")
			var liveInfo: LiveInfo?,
			@SerializedName("relation_info")
			var relationInfo: RelationInfo?
		) : Serializable {
			data class BaseInfo(
				@SerializedName("uname")
				var uname: String?,// 荣耀手机
				@SerializedName("face")
				var face: String?,// http://i2.hdslb.com/bfs/face/68ecb50a503e10dfeb49fed51b4738b33d0c8e44.jpg
				@SerializedName("gender")
				var gender: String?,// 保密
				@SerializedName("official_info")
				var officialInfo: OfficialInfo?
			) : Serializable {
				data class OfficialInfo(
					@SerializedName("role")
					var role: Int?,// 1
					@SerializedName("title")
					var title: String?,// 荣耀手机官方账号
					@SerializedName("desc")
					var desc: String?
				) : Serializable
			}

			data class LiveInfo(
				@SerializedName("level")
				var level: Int?,// 22
				@SerializedName("level_color")
				var levelColor: Long?// 10512625
			) : Serializable

			data class RelationInfo(
				@SerializedName("attention")
				var attention: Long?// 446286     看样子像是关注数
			) : Serializable
		}
	}
}