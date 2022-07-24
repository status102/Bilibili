package com.status102.bilibili.monitor.bilive.entity;

import com.google.gson.annotations.SerializedName;

public class RaffleMessage extends BaseMessage {
	/**
	 *  cmd: 'raffle'
	 * 	roomID: number
	 * 	id: number
	 * 	type: string
	 * 	title: string
	 * 	time: number
	 * 	max_time: number
	 * 	time_wait: number
	 */
	private long roomID = -1;

	private long id = -1;

	private String type = null;

	private String title = null;

	private long time = -1;

	@SerializedName(value = "max_time")
	private long maxTime = -1;

	@SerializedName(value = "time_wait")
	private long timeWait = -1;

	public long getRoomID() {
		return roomID;
	}

	public void setRoomID(long roomID) {
		this.roomID = roomID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}

	public long getTimeWait() {
		return timeWait;
	}

	public void setTimeWait(long timeWait) {
		this.timeWait = timeWait;
	}
}
