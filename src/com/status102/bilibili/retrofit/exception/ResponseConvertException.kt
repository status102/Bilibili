package com.status102.bilibili.retrofit.exception

import okhttp3.Response
import java.io.IOException

/**
 * 返回不是json时抛出，手动解析response，标准解析e.response.message
 */
class ResponseConvertException (val response: Response): IOException()