package com.status102.bilibili

object BilibiliProperty {
	/**
	 * 默认 UA, 用于大多数访问
	 */
	@Suppress("SpellCheckingInspection")
	val defaultUserAgent = "Mozilla/5.0 BiliDroid/5.53.1 (bbcallen@gmail.com)"

	/**
	 * Android 平台的 appKey(该默认值为普通版客户端, 非概念版)
	 */
	val appKey = "1d8b6e7d45233436"

	/**
	 * 由反编译 so 文件得到的 appSecret, 与 appKey 必须匹配
	 */
	@Suppress("SpellCheckingInspection")
	val appSecret = "560c52ccd288fed045859ed18bffd973"

	/**
	 * 获取视频播放地址使用的 appKey, 与访问其他 RestFulAPI 所用的 appKey 是不一样的
	 */
	@Suppress("SpellCheckingInspection")
	var videoAppKey = "iVGUTjsxvpLeuDCf"

	/**
	 * 获取视频播放地址所用的 appSecret
	 */
	@Suppress("SpellCheckingInspection")
	var videoAppSecret = "aHRmhWMLkdeMuILqORnYZocwMBpMEOdt"

	/**
	 * 客户端平台
	 */
	val platform = "android"

	/**
	 * 客户端类型
	 * 此属性在旧版客户端不存在
	 */
	val channel = "html5_app_bili"

	/**
	 * 硬件 ID, 尚不明确生成算法
	 */
	@Suppress("SpellCheckingInspection")
	var hardwareId = "aBRoDWAVeRhsA3FDewMzS3lLMwM"

	/**
	 * 屏幕尺寸, 大屏手机(已经没有小屏手机了)统一为 xxhdpi
	 * 此参数在新版客户端已经较少使用
	 */
	var scale = "xxhdpi"

	/**
	 * 版本号
	 */
	val version = "5.53.1.5531000"

	/**
	 * 构建版本号
	 */
	val build = "5531000"

	/**
	 * 构建版本 ID, 可能是某种 Hash
	 */
	var buildVersionId = "XYC6D04C4B45D983565A9E9C156DDC017E2C9"

	object Method {
		const val GET = "GET"
		const val POST = "POST"
		const val PATCH = "PATCH"
		const val PUT = "PUT"
		const val DELETE = "DELETE"
		const val OPTION = "OPTION"
	}

	object Header {
		const val DISPLAY_ID = "Display-ID"
		@Suppress("SpellCheckingInspection")
		const val BUILD_VERSION_ID = "Buvid"
		const val DEVICE_ID = "Device-ID"
		const val USER_AGENT = "User-Agent"
		const val ACCEPT = "Accept"
		const val ACCEPT_LANGUAGE = "Accept-Language"
		const val ACCEPT_ENCODING = "Accept-Encoding"

		//强制公共参数添加位置
		const val FORCE_PARAM = "Retrofit-Force-Param"
		const val FORCE_PARAM_QUERY = "query"
		@Suppress("MemberVisibilityCanBePrivate")
		const val FORCE_PARAM_FORM_BODY = "formBody"
		const val FORCE_QUERY = "$FORCE_PARAM: $FORCE_PARAM_QUERY"
		const val FORCE_FORM_BODY = "$FORCE_PARAM: $FORCE_PARAM_FORM_BODY"
	}

	object Param {
		const val ACCESS_KEY = "access_key"
		@Suppress("SpellCheckingInspection")
		const val APP_KEY = "appkey"
		const val ACTION_KEY = "actionKey"
		const val BUILD = "build"
		@Suppress("SpellCheckingInspection")
		const val BUILD_VERSION_ID = "buvid"
		const val BILI_LOCAL_ID="bili_local_id"
		const val CHANNEL = "channel"
		@Suppress("ObjectPropertyName")
		const val _DEVICE = "_device"
		const val DEVICE = "device"
		const val DEVICE_ID = "device_id"
		const val DEVICE_NAME = "device_name"
		const val DEVICE_PLATFORM = "device_platform"
		const val LOCAL_ID = "local_id"
		@Suppress("ObjectPropertyName", "SpellCheckingInspection")
		const val _HARDWARE_ID = "_hwid"
		const val SOURCE = "src"
		const val TRACE_ID = "trace_id"
		const val USER_ID = "uid"
		const val VERSION = "version"
		@Suppress("SpellCheckingInspection")
		const val MOBILE_APP = "mobi_app"
		const val PLATFORM = "platform"
		const val TIMESTAMP = "ts"
		const val EXPIRE = "expire"
		const val MID = "mid"
		const val SIGN = "sign"
	}

	object Charsets {
		const val UTF_8 = "UTF-8"
	}
}