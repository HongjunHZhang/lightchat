package com.zhj.entity.login;

import com.zhj.entity.UserCount;

/**
 * @author 789
 */
public class TokenBody {
    private String name;
    private String createTime;
    private String endTime;
    private String isValid;
    private String rootLevel;
    private String safeLevel;

    public TokenBody(String name, String createTime, String endTime, String isValid, String rootLevel, String safeLevel) {
        this.name = name;
        this.createTime = createTime;
        this.endTime = endTime;
        this.isValid = isValid;
        this.rootLevel = rootLevel;
        this.safeLevel = safeLevel;
    }

    public TokenBody() {
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getRootLevel() {
        return rootLevel;
    }

    public void setRootLevel(String rootLevel) {
        this.rootLevel = rootLevel;
    }

    public String getSafeLevel() {
        return safeLevel;
    }

    public void setSafeLevel(String safeLevel) {
        this.safeLevel = safeLevel;
    }



    public static TokenBody userCountToTokenBody(UserCount userCount){
        return new TokenBody(userCount.getUserCount(),String.valueOf(System.currentTimeMillis()),String.valueOf(System.currentTimeMillis()+1000 * 60 * 20),userCount.getIsValid(),userCount.getRootLevel(),userCount.getSafeLevel());
    }

    @Override
    public String toString() {
        return "TokenBody{" +
                "name='" + name + '\'' +
                ", createTime='" + createTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", isValid='" + isValid + '\'' +
                ", rootLevel='" + rootLevel + '\'' +
                ", safeLevel='" + safeLevel + '\'' +
                '}';
    }
}
