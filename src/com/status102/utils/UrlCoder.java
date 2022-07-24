package com.status102.utils;

import java.io.UnsupportedEncodingException;
import java.net.*;

public class UrlCoder {
	// Url编码
	public static String encodeUtf_8(String str) throws UnsupportedEncodingException {
		return URLEncoder.encode(str, "utf-8");
	}

	// Url解码
	public static String decodeUtf_8(String str) throws UnsupportedEncodingException {
		return URLDecoder.decode(str, "utf-8");
	}

}
