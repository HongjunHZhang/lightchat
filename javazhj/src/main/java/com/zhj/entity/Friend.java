package com.zhj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 789
 */
@ApiModel(value = "好友表", description = "好友列表")
@TableName("friend")
public class Friend {
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("唯一标识符id")
    private Integer id;

    @TableField("user_id")
    @ApiModelProperty("用户账户")
    private String userId;

    @TableField("friend_id")
    @ApiModelProperty("好友账户")
    private String friendId;

    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private String createTime;

    @TableField("ship")
    @ApiModelProperty("亲密关系")
    private String ship;

    @TableField("ship_type")
    @ApiModelProperty("亲密关系")
    private String shipType;

    @TableField("last_read_time")
    @ApiModelProperty("上一次阅读到的时间")
    private String lastReadTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getShip() {
        return ship;
    }

    public void setShip(String ship) {
        this.ship = ship;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public String getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(String lastReadTime) {
        this.lastReadTime = lastReadTime;
    }
}
