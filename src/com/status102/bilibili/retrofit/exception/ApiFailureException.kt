package com.status102.bilibili.retrofit.exception

import com.google.gson.JsonObject
import java.io.IOException

/**
 * 返回Json中code不为0情况，手动解析Json
 */
class ApiFailureException(val jsonObject: JsonObject) : IOException()