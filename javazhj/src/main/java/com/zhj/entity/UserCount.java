package com.zhj.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 *
 * </p>
 *
 * @author zhj
 * @since 2021-11-17
 */
@ApiModel(value = "UserCount对象", description = "用户")
@TableName("user_count")
public class UserCount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("唯一标识符id")
    private Integer id;

    @TableField("user_count")
    @ApiModelProperty("用户账户")
    private String userCount;

    @ApiModelProperty("用户密码")
    @TableField(select = false)
    private String password;

    @TableField("space_id")
    @ApiModelProperty("空间ID")
    private String spaceId;

    @TableField("is_valid")
    @ApiModelProperty("帐户是否任合法使用，1为正常使用")
    private String isValid;

    @TableField("status")
    @ApiModelProperty("帐户状态，1为正常，详见userStatus字典")
    private String status;

    @TableField("root_level")
    @ApiModelProperty("用户帐户权限，1为普通用户，通常数字越大级别越高")
    private String rootLevel;

    @TableField("safe_level")
    @ApiModelProperty("用户安全等级，1为正常使用，其余详见userSafeLevel字典")
    private String safeLevel;

    @TableField("online")
    @ApiModelProperty("判断用户是否在线，1为在线")
    private String online;

    @TableField("create_time")
    @ApiModelProperty("用户创建时间")
    private String createTime;

    @TableField("nick_name")
    @ApiModelProperty("昵称")
    private String nickName;

    @TableField("photo")
    @ApiModelProperty("用户头像")
    private String photo;


    @TableField("sign")
    @ApiModelProperty("用户签名")
    private String sign;

    @TableField("age")
    @ApiModelProperty("用户年龄")
    private Integer age;

    @TableField("birthday")
    @ApiModelProperty("用户生日")
    private String birthday;

    @TableField("constellation")
    @ApiModelProperty("用户星座")
    private String constellation;

    @TableField("phone")
    @ApiModelProperty("用户手机号")
    private String phone;

    @TableField("email")
    @ApiModelProperty("用户邮箱")
    private String email;

    @TableField("open")
    @ApiModelProperty("是否允许别人查看，1为允许查看")
    private String open;

    @TableField("school")
    @ApiModelProperty("学校")
    private String school;

    @TableField("address")
    @ApiModelProperty("地址")
    private String address;

    @TableField("thumbs")
    @ApiModelProperty("点赞数量")
    private String thumbs;

    @TableField("sex")
    @ApiModelProperty("性别")
    private String sex;

    @TableField("simple_info")
    @ApiModelProperty("简介")
    private String simpleInfo;

    @TableField("habit")
    @ApiModelProperty("爱好")
    private String habit;

    @TableField("is_default_photo")
    @ApiModelProperty("爱好")
    private String isDefaultPhoto;

    @TableField("company")
    @ApiModelProperty("公司")
    private String company;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserName(String userCount) {
        this.userCount = userCount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getThumbs() {
        return thumbs;
    }

    public void setThumbs(String thumbs) {
        this.thumbs = thumbs;
    }


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSimpleInfo() {
        return simpleInfo;
    }

    public void setSimpleInfo(String simpleInfo) {
        this.simpleInfo = simpleInfo;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public String getHabit() {
        return habit;
    }

    public void setHabit(String habit) {
        this.habit = habit;
    }

    public String getIsDefaultPhoto() {
        return isDefaultPhoto;
    }

    public void setIsDefaultPhoto(String isDefaultPhoto) {
        this.isDefaultPhoto = isDefaultPhoto;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

}
