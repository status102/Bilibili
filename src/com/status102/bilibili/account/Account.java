package com.status102.bilibili.account;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Account {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountID, account.accountID) &&
                        Objects.equals(password, account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, expiresTime, serverKey, accountStatus, checkBannedTimes, bannedTime, lastCheckBannedTime, checkBannedInterval, uid, username, accountID, password, accessToken, refreshToken, cookieList, device_ID, buvid, display_ID, setting, limit);
    }

    private String tag = null;

    @SerializedName(value = "expires_time", alternate = {"endTime"})
    private String expiresTime = null;

    @SerializedName(value = "server_key", alternate = {"serverKey"})
    private String serverKey = null;

    @SerializedName(value = "account_status", alternate = {"accountType"})
    private int accountStatus = 0;// -1 延期(不应开放) 0 正常 1 无密码 3 验证码 2 小黑屋 4 出黑屋后等待

    @SerializedName(value = "check_banned_times", alternate = {"blackHouseCheckTimes"})
    private int checkBannedTimes = 10;// 连续访问被拒绝次数达到该值判定进入小黑屋

    @SerializedName(value = "banned_time", alternate = {"enterBlackHouseTime"})
    private Long bannedTime = null;// 进小黑屋时间

    @SerializedName(value = "last_check_banned_time", alternate = {"lastCheckBlackHouseTime"})
    private Long lastCheckBannedTime = null;// 上次检查时间 出黑屋后转为出黑屋时间

    @SerializedName(value = "check_banned_interval", alternate = {"checkBlackHouseInterval"})
    private int checkBannedInterval = 8 * 60 * 60;// 检测小黑屋间隔

    private int uid = -1;

    @SerializedName(value = "username", alternate = {"userName"})
    private String username = null;

    private String accountID = null;// 登录凭证

    @SerializedName(value = "password", alternate = {"passWord"})
    private String password = null;

    @SerializedName(value = "access_token", alternate = {"accessToken"})
    private String accessToken = null;

    @SerializedName(value = "refresh_token", alternate = {"refreshToken"})
    private String refreshToken = null;

    @SerializedName(value = "cookie_list")
    private List<String> cookieList = new ArrayList<>();

    @SerializedName(value = "device_ID", alternate = {"Device_ID", "Device-ID"})
    private String device_ID = null;

    @SerializedName(value = "buvid", alternate = {"Buvid"})
    private String buvid = null;

    @SerializedName(value = "display_ID", alternate = {"Display_ID", "Display-ID"})
    private String display_ID = null;

    private Setting setting = new Setting();
    private Limit limit = new Limit();

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(String expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getCheckBannedTimes() {
        return checkBannedTimes;
    }

    public void setCheckBannedTimes(int checkBannedTimes) {
        this.checkBannedTimes = checkBannedTimes;
    }

    public Long getBannedTime() {
        return bannedTime;
    }

    public void setBannedTime(Long bannedTime) {
        this.bannedTime = bannedTime;
    }

    public Long getLastCheckBannedTime() {
        return lastCheckBannedTime;
    }

    public void setLastCheckBannedTime(Long lastCheckBannedTime) {
        this.lastCheckBannedTime = lastCheckBannedTime;
    }

    public int getCheckBannedInterval() {
        return checkBannedInterval;
    }

    public void setCheckBannedInterval(int checkBannedInterval) {
        this.checkBannedInterval = checkBannedInterval;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public List<String> getCookieList() {
        return cookieList;
    }

    public void setCookieList(List<String> cookieList) {
        this.cookieList = cookieList;
    }

    public String getDevice_ID() {
        return device_ID;
    }

    public void setDevice_ID(String device_ID) {
        this.device_ID = device_ID;
    }

    public String getBuvid() {
        return buvid;
    }

    public void setBuvid(String buvid) {
        this.buvid = buvid;
    }

    public String getDisplay_ID() {
        return display_ID;
    }

    public void setDisplay_ID(String display_ID) {
        this.display_ID = display_ID;
    }

    public static class Setting {

        @SerializedName(value = "daily_sign", alternate = {"dailySign"})
        private boolean dailySign = false;

        @SerializedName(value = "exchange_silver_to_coin")
        private boolean exchangeSilverToCoin = false;

        @SerializedName(value = "group_sign", alternate = {"groupSign"})
        private boolean groupSign = false;

        @SerializedName(value = "main_task", alternate = {"mainTask"})
        private boolean mainTask = false;

        @SerializedName(value = "twice_watch", alternate = {"twiceWatch"})
        private boolean twiceWatch = false;

        @SerializedName(value = "judgement")
        private boolean judgement = false;

        @SerializedName(value = "free_silver", alternate = {"freeSilver"})
        private boolean freeSilver = false;

        @SerializedName(value = "raffle_lottery", alternate = {"activityLottery", "activity_lottery"})
        private boolean raffleLottery = false;

        @SerializedName(value = "raffle_ignore", alternate = {"lotteryIgnore", "lottery_ignore"})
        private int raffleIgnore = 0;

        @SerializedName(value = "pk_lottery")
        private boolean pkLottery = true;

        @SerializedName(value = "pk_ignore")
        private int pkIgnore = 0;

        @SerializedName(value = "box_lottery", alternate = {"boxLottery"})
        private boolean boxLottery = true;

        @SerializedName(value = "guard_lottery", alternate = {"shipLottery", "ship_lottery"})
        private boolean guardLottery = false;

        @SerializedName(value = "guard_ignore", alternate = {"shipIgnore", "ship_ignore"})
        private int guardIgnore = 0;

        @SerializedName(value = "storm_lottery", alternate = {"stormLottery"})
        private boolean stormLottery = false;

        @SerializedName(value = "storm_ignore", alternate = {"stormIgnore"})
        private int stormIgnore = 0;

        @SerializedName(value = "storm_interval", alternate = {"stormInterval"})
        private int stormInterval = 80;

        @SerializedName(value = "storm_random_interval", alternate = {"stormRandomInterval"})
        private int stormRandomInterval = 60;

        @SerializedName(value = "storm_try_times", alternate = {"stormTryTimes"})
        private int stormTryTimes = 15;

        @SerializedName(value = "storm_limit", alternate = {"stormLimit"})
        private int stormLimit = -1;

        @SerializedName(value = "feed_medal", alternate = {"feedMedal"})
        private boolean feedMedal = false;

        @SerializedName(value = "send_expires_gift", alternate = {"outdate"})
        private boolean sendExpiresGift = false;

        @SerializedName(value = "send_expires_gift_room", alternate = {"outdateRoom"})
        private int sendExpiresGiftRoom = 0;

        @SerializedName(value = "dynamic_lottery", alternate = {"dynamicLottery"})
        private boolean dynamicLottery = false;

        @SerializedName(value = "dynamic_lottery_at_UID", alternate = {"dynamicLotteryAtUID"})
        private List<Integer> dynamicLotteryAtUID = new ArrayList<Integer>();

        @SerializedName(value = "dynamic_lottery_ignore_UID", alternate = {"dynamicLotteryIgnoreUID"})
        private List<Integer> dynamicLotteryIgnoreUID = new ArrayList<Integer>();

        @SerializedName(value = "dynamic_lottery_keyword_used", alternate = {"dynamicLotteryKeywordUsed"})
        private boolean dynamicLotteryKeywordUsed = false;

        @SerializedName(value = "dynamic_lottery_keyword", alternate = {"dynamicLotteryKeyword"})
        private List<String> dynamicLotteryKeyword = new ArrayList<String>();

        @SerializedName(value = "dynamic_lottery_ignore_keyword_used", alternate = {"dynamicLotteryIgnoreKeywordUsed"})
        private boolean dynamicLotteryIgnoreKeywordUsed = false;

        @SerializedName(value = "dynamic_lottery_ignore_keyword", alternate = {"dynamicLotteryIgnoreKeyword"})
        private List<String> dynamicLotteryIgnoreKeyword = new ArrayList<String>();

        @SerializedName(value = "dynamic_lottery_thank_word", alternate = {"dynamicLotteryThankWord"})
        private List<String> dynamicLotteryThankWord = new ArrayList<String>();

        @SerializedName(value = "un_subscribe", alternate = {"unSubscribe"})
        private boolean unSubscribe = false;

        @SerializedName(value = "delete_dynamic", alternate = {"deleteSpace"})
        private boolean deleteDynamic = false;

        @SerializedName(value = "live_assistant_room", alternate = {"liveAssistant"})
        private List<Integer> liveAssistantRoom = new ArrayList<>();

        @SerializedName(value = "send_after_sign", alternate = {"sendAfterSign"})
        private boolean sendAfterSign = false;

        @SerializedName(value = "send_after_judgement", alternate = {"sendAfterJudgement"})
        private boolean sendAfterJudgement = false;

        public boolean isDailySign() {
            return dailySign;
        }

        public void setDailySign(boolean dailySign) {
            this.dailySign = dailySign;
        }

        public boolean isExchangeSilverToCoin() {
            return exchangeSilverToCoin;
        }

        public void setExchangeSilverToCoin(boolean exchangeSilverToCoin) {
            this.exchangeSilverToCoin = exchangeSilverToCoin;
        }

        public boolean isGroupSign() {
            return groupSign;
        }

        public void setGroupSign(boolean groupSign) {
            this.groupSign = groupSign;
        }

        public boolean isMainTask() {
            return mainTask;
        }

        public void setMainTask(boolean mainTask) {
            this.mainTask = mainTask;
        }

        public boolean isTwiceWatch() {
            return twiceWatch;
        }

        public void setTwiceWatch(boolean twiceWatch) {
            this.twiceWatch = twiceWatch;
        }

        public boolean isJudgement() {
            return judgement;
        }

        public void setJudgement(boolean judgement) {
            this.judgement = judgement;
        }

        public boolean isFreeSilver() {
            return freeSilver;
        }

        public void setFreeSilver(boolean freeSilver) {
            this.freeSilver = freeSilver;
        }

        public boolean isRaffleLottery() {
            return raffleLottery;
        }

        public void setRaffleLottery(boolean raffleLottery) {
            this.raffleLottery = raffleLottery;
        }

        public int getRaffleIgnore() {
            return raffleIgnore;
        }

        public void setRaffleIgnore(int raffleIgnore) {
            this.raffleIgnore = raffleIgnore;
        }

        public boolean isPkLottery() {
            return pkLottery;
        }

        public void setPkLottery(boolean pkLottery) {
            this.pkLottery = pkLottery;
        }

        public int getPkIgnore() {
            return pkIgnore;
        }

        public void setPkIgnore(int pkIgnore) {
            this.pkIgnore = pkIgnore;
        }

        public boolean isBoxLottery() {
            return boxLottery;
        }

        public void setBoxLottery(boolean boxLottery) {
            this.boxLottery = boxLottery;
        }

        public boolean isGuardLottery() {
            return guardLottery;
        }

        public void setGuardLottery(boolean guardLottery) {
            this.guardLottery = guardLottery;
        }

        public int getGuardIgnore() {
            return guardIgnore;
        }

        public void setGuardIgnore(int guardIgnore) {
            this.guardIgnore = guardIgnore;
        }

        public boolean isStormLottery() {
            return stormLottery;
        }

        public void setStormLottery(boolean stormLottery) {
            this.stormLottery = stormLottery;
        }

        public int getStormIgnore() {
            return stormIgnore;
        }

        public void setStormIgnore(int stormIgnore) {
            this.stormIgnore = stormIgnore;
        }

        public int getStormInterval() {
            return stormInterval;
        }

        public void setStormInterval(int stormInterval) {
            this.stormInterval = stormInterval;
        }

        public int getStormRandomInterval() {
            return stormRandomInterval;
        }

        public void setStormRandomInterval(int stormRandomInterval) {
            this.stormRandomInterval = stormRandomInterval;
        }

        public int getStormTryTimes() {
            return stormTryTimes;
        }

        public void setStormTryTimes(int stormTryTimes) {
            this.stormTryTimes = stormTryTimes;
        }

        public int getStormLimit() {
            return stormLimit;
        }

        public void setStormLimit(int stormLimit) {
            this.stormLimit = stormLimit;
        }

        public boolean isFeedMedal() {
            return feedMedal;
        }

        public void setFeedMedal(boolean feedMedal) {
            this.feedMedal = feedMedal;
        }

        public boolean isSendExpiresGift() {
            return sendExpiresGift;
        }

        public void setSendExpiresGift(boolean sendExpiresGift) {
            this.sendExpiresGift = sendExpiresGift;
        }

        public int getSendExpiresGiftRoom() {
            return sendExpiresGiftRoom;
        }

        public void setSendExpiresGiftRoom(int sendExpiresGiftRoom) {
            this.sendExpiresGiftRoom = sendExpiresGiftRoom;
        }

        public boolean isDynamicLottery() {
            return dynamicLottery;
        }

        public void setDynamicLottery(boolean dynamicLottery) {
            this.dynamicLottery = dynamicLottery;
        }

        public List<Integer> getDynamicLotteryAtUID() {
            return dynamicLotteryAtUID;
        }

        public void setDynamicLotteryAtUID(List<Integer> dynamicLotteryAtUID) {
            this.dynamicLotteryAtUID = dynamicLotteryAtUID;
        }

        public List<Integer> getDynamicLotteryIgnoreUID() {
            return dynamicLotteryIgnoreUID;
        }

        public void setDynamicLotteryIgnoreUID(List<Integer> dynamicLotteryIgnoreUID) {
            this.dynamicLotteryIgnoreUID = dynamicLotteryIgnoreUID;
        }

        public boolean isDynamicLotteryKeywordUsed() {
            return dynamicLotteryKeywordUsed;
        }

        public void setDynamicLotteryKeywordUsed(boolean dynamicLotteryKeywordUsed) {
            this.dynamicLotteryKeywordUsed = dynamicLotteryKeywordUsed;
        }

        public List<String> getDynamicLotteryKeyword() {
            return dynamicLotteryKeyword;
        }

        public void setDynamicLotteryKeyword(List<String> dynamicLotteryKeyword) {
            this.dynamicLotteryKeyword = dynamicLotteryKeyword;
        }

        public boolean isDynamicLotteryIgnoreKeywordUsed() {
            return dynamicLotteryIgnoreKeywordUsed;
        }

        public void setDynamicLotteryIgnoreKeywordUsed(boolean dynamicLotteryIgnoreKeywordUsed) {
            this.dynamicLotteryIgnoreKeywordUsed = dynamicLotteryIgnoreKeywordUsed;
        }

        public List<String> getDynamicLotteryIgnoreKeyword() {
            return dynamicLotteryIgnoreKeyword;
        }

        public void setDynamicLotteryIgnoreKeyword(List<String> dynamicLotteryIgnoreKeyword) {
            this.dynamicLotteryIgnoreKeyword = dynamicLotteryIgnoreKeyword;
        }

        public List<String> getDynamicLotteryThankWord() {
            return dynamicLotteryThankWord;
        }

        public void setDynamicLotteryThankWord(List<String> dynamicLotteryThankWord) {
            this.dynamicLotteryThankWord = dynamicLotteryThankWord;
        }

        public boolean isUnSubscribe() {
            return unSubscribe;
        }

        public void setUnSubscribe(boolean unSubscribe) {
            this.unSubscribe = unSubscribe;
        }

        public boolean isDeleteDynamic() {
            return deleteDynamic;
        }

        public void setDeleteDynamic(boolean deleteDynamic) {
            this.deleteDynamic = deleteDynamic;
        }

        public List<Integer> getLiveAssistantRoom() {
            return liveAssistantRoom;
        }

        public void setLiveAssistantRoom(List<Integer> liveAssistantRoom) {
            this.liveAssistantRoom = liveAssistantRoom;
        }

        public boolean isSendAfterSign() {
            return sendAfterSign;
        }

        public void setSendAfterSign(boolean sendAfterSign) {
            this.sendAfterSign = sendAfterSign;
        }

        public boolean isSendAfterJudgement() {
            return sendAfterJudgement;
        }

        public void setSendAfterJudgement(boolean sendAfterJudgement) {
            this.sendAfterJudgement = sendAfterJudgement;
        }
    }

    public static class Limit {

        @SerializedName(value = "op")
        private boolean op = false;

        @SerializedName(value = "daily_sign", alternate = {"dailySign"})
        private boolean dailySign = true;

        @SerializedName(value = "group_sign", alternate = {"groupSign"})
        private boolean groupSign = true;

        @SerializedName(value = "main_task", alternate = {"mainTask"})
        private boolean mainTask = true;

        @SerializedName(value = "watch_exp", alternate = {"watchExp"})
        private boolean watchExp = true;

        @SerializedName(value = "twice_watch", alternate = {"twiceWatch"})
        private boolean twiceWatch = true;

        @SerializedName(value = "judgement")
        private boolean judgement = true;

        @SerializedName(value = "free_silver", alternate = {"freeSilver"})
        private boolean freeSilver = true;

        @SerializedName(value = "raffle_lottery", alternate = {"activityLottery", "activity_lottery"})
        private boolean raffleLottery = true;

        @SerializedName(value = "pk_lottery")
        private boolean pkLottery = true;

        @SerializedName(value = "box_lottery", alternate = {"boxLottery"})
        private boolean boxLottery = true;

        @SerializedName(value = "guard_lottery", alternate = {"shipLottery", "ship_lottery"})
        private boolean guardLottery = true;

        @SerializedName(value = "storm_lottery", alternate = {"stormLottery"})
        private boolean stormLottery = true;

        @SerializedName(value = "feed_medal", alternate = {"feedMedal"})
        private boolean feedMedal = true;

        @SerializedName(value = "send_expires_gift", alternate = {"outdate"})
        private boolean sendExpiresGift = true;

        @SerializedName(value = "dynamic_lottery", alternate = {"dynamicLottery"})
        private boolean dynamicLottery = true;

        public boolean isOp() {
            return op;
        }

        public void setOp(boolean op) {
            this.op = op;
        }

        public boolean isDailySign() {
            return dailySign;
        }

        public void setDailySign(boolean dailySign) {
            this.dailySign = dailySign;
        }

        public boolean isGroupSign() {
            return groupSign;
        }

        public void setGroupSign(boolean groupSign) {
            this.groupSign = groupSign;
        }

        public boolean isMainTask() {
            return mainTask;
        }

        public void setMainTask(boolean mainTask) {
            this.mainTask = mainTask;
        }

        public boolean isWatchExp() {
            return watchExp;
        }

        public void setWatchExp(boolean watchExp) {
            this.watchExp = watchExp;
        }

        public boolean isTwiceWatch() {
            return twiceWatch;
        }

        public void setTwiceWatch(boolean twiceWatch) {
            this.twiceWatch = twiceWatch;
        }

        public boolean isJudgement() {
            return judgement;
        }

        public void setJudgement(boolean judgement) {
            this.judgement = judgement;
        }

        public boolean isFreeSilver() {
            return freeSilver;
        }

        public void setFreeSilver(boolean freeSilver) {
            this.freeSilver = freeSilver;
        }

        public boolean isRaffleLottery() {
            return raffleLottery;
        }

        public void setRaffleLottery(boolean raffleLottery) {
            this.raffleLottery = raffleLottery;
        }

        public boolean isPkLottery() {
            return pkLottery;
        }

        public void setPkLottery(boolean pkLottery) {
            this.pkLottery = pkLottery;
        }

        public boolean isBoxLottery() {
            return boxLottery;
        }

        public void setBoxLottery(boolean boxLottery) {
            this.boxLottery = boxLottery;
        }

        public boolean isGuardLottery() {
            return guardLottery;
        }

        public void setGuardLottery(boolean guardLottery) {
            this.guardLottery = guardLottery;
        }

        public boolean isStormLottery() {
            return stormLottery;
        }

        public void setStormLottery(boolean stormLottery) {
            this.stormLottery = stormLottery;
        }

        public boolean isFeedMedal() {
            return feedMedal;
        }

        public void setFeedMedal(boolean feedMedal) {
            this.feedMedal = feedMedal;
        }

        public boolean isSendExpiresGift() {
            return sendExpiresGift;
        }

        public void setSendExpiresGift(boolean sendExpiresGift) {
            this.sendExpiresGift = sendExpiresGift;
        }

        public boolean isDynamicLottery() {
            return dynamicLottery;
        }

        public void setDynamicLottery(boolean dynamicLottery) {
            this.dynamicLottery = dynamicLottery;
        }
    }
}
