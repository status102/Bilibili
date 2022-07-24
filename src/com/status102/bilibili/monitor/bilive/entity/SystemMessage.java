package com.status102.bilibili.monitor.bilive.entity;

public class SystemMessage extends BaseMessage {
	/**
	 * {"cmd":"sysmsg","msg":"一个莫得感情的服务器，欢迎投喂"}
	 *
	 *   cmd: 'sysmsg'
	 *   msg: string
	 */
	private String msg = null;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
