package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.entity.AddShip;
import com.zhj.entity.AddUser;
import com.zhj.entity.ret.RetAddGroup;
import com.zhj.entity.ret.RetAddUser;
import com.zhj.entity.ret.RetAddUserMsg;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 789
 */
public interface AddUserService extends IService<AddUser> {
    /**
     * 获取默认的推荐添加用户名单
     * @return 用户名单
     */
    List<RetAddUser> getDefaultUser();

    /**
     * 通过帐号或昵称查找用户
     * @param json 帐号或昵称
     * @return 用户列表
     */
    List<RetAddUser> searchUser(String json);

    /**
     * 获取默认的推荐群聊名单
     * @return 推荐群聊名单
     */
    List<RetAddGroup> getDefaultGroup();

    /**
     * 通过帐号或昵称查找群聊
     * @param json 帐号或昵称
     * @return 群聊列表
     */
    List<RetAddGroup> searchGroup(String json);

    /**
     * 发起添加用户申请
     * @param json 用户帐户（json形式）
     * @return 是否发起成功
     */
    boolean adduser(String json);

    /**
     * 注册群聊
     * @param file 群头像图片
     * @param defaultInfo json形式的群消息
     */
    void registerGroup(MultipartFile file, String defaultInfo);

    /**
     * 获取用户有关的验证消息
     * @return 验证消息列表
     */
    List<RetAddUserMsg>  getNewUser();

    /**
     * 提交处理用户申请
     * @param retAddUserMsg 1为添加 2为拒绝 -1为忽略
     * @return 处理结果
     */
    String confirmAddUser(RetAddUserMsg retAddUserMsg);

    /**
     * 提交处理群聊申请
     * @param retAddUserMsg 1为添加 2为拒绝 -1为忽略
     * @return 处理结果
     */
    String confirmAddGroup(RetAddUserMsg retAddUserMsg);

    /**
     * 请求处理好友关系请求
     * @param addShip 处理参数
     * @return 处理结果
     */
    String addRelationShip(AddShip addShip);

    /**
     * 处理好友关系请求
     * @param retAddUserMsg 处理参数
     * @return 处理结果
     */
    String confirmRelationShip(RetAddUserMsg retAddUserMsg);

    /**
     * 删除用户
     * @param json 用户帐户的json形式
     * @return 删除返回结果
     */
    String deleteUser(String json);

    /**
     * 退出群聊
     * @param json groupMember成员的json形式
     * @return 退出结果
     */
    String exitGroup(String json);

    /**
     * 解散群聊
     * @param json groupMember成员的json形式
     * @return 解散结果
     */
    String dissolveGroup(String json);

    /**
     * 添加用户
     * @param userCount 请求用户帐户
     * @param friendId 被请求用户帐户
     * @param msg 验证消息
     * @param photo 头像
     * @return 添加结果
     */
    boolean addUser(String userCount,String friendId,String msg,String photo);

    /**
     * 申请进入群聊
     * @param userCount 请求用户帐户
     * @param groupCount 被请求群聊帐户
     * @param msg 验证消息
     * @return 申请结果
     */
    int addGroup(String userCount,String groupCount,String msg);
}
