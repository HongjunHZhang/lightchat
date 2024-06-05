package com.zhj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 *
 * </p>
 *
 * @author zhj
 * @since 2022-10-10
 */
@TableName("load_msg_record")
@ApiModel(value = "LoadMsgRecord对象", description = "加载消息记录")
public class LoadMsgRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("帐户")
    private String userCount;

    @ApiModelProperty("上一次读取新消息的时间")
    private String lastReadTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }
    public String getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(String lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    @Override
    public String toString() {
        return "LoadMsgRecord{" +
            "id=" + id +
            ", userCount=" + userCount +
            ", lastReadTime=" + lastReadTime +
        "}";
    }
}
