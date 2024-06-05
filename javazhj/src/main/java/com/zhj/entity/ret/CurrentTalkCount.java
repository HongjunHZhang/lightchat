package com.zhj.entity.ret;

/**
 * CurrentTalkCount
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2023/3/1 21:57
 */
public class CurrentTalkCount {
    private String count;
    private String countType;
    private String photo;
    private String nickName;

    public CurrentTalkCount(String count, String countType, String photo, String nickName) {
        this.count = count;
        this.countType = countType;
        this.photo = photo;
        this.nickName = nickName;
    }

    public CurrentTalkCount() {
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCountType() {
        return countType;
    }

    public void setCountType(String countType) {
        this.countType = countType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
