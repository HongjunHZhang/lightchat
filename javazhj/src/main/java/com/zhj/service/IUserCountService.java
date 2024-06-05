package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.entity.FriendMsgTemp;
import com.zhj.entity.UserCount;
import com.zhj.entity.ret.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhj
 * @since 2021-11-17
 */
public interface IUserCountService extends IService<UserCount> {
     /**
      * 填充用户的默认信息
      * @param userCount 用户账户
      */
     void fillDefaultUser(UserCount userCount);

     /**
      * 登录接口
      * @param userCount 用户账户信息
      * @param httpResponse 响应流
      * @return 登录状态
      */
     UserCount login(UserCount userCount, HttpServletResponse httpResponse);

     /**
      * 游客登陆
      * @param httpResponse 响应流
      * @return 登录状态
      */
     UserCount touristLogin(HttpServletResponse httpResponse);

     /**
      * 注册帐号
      * @param userCountVerify 注册帐号信息
      * @return 注册状态
      */
     boolean register(UserCountVerify userCountVerify);

     /**
      * 绑定邮箱
      * @param json 邮箱帐号的json形式
      * @return 操作状态
      * @throws Exception 邮件发送异常
      */
     String emailVerifyCode(String json) throws Exception;

     /**
      * 帐号下线
      */
     void outLine();

     /**
      * 通过帐号获取帐号详情
      * @param userCount 用户帐号
      * @return 帐号详情
      */
     UserCount getUserInfoByUserCount(String userCount);

     /**
      * 加载历史信息
      * @return 历史信息记录
      */
     List<FriendMsgCurrent> loadHistoryMsg();

     /**
      * 获取新信息
      * @return 信息新列表
      */
     List<FriendMsgTemp> getNewMsg();

     /**
      * 获取主页信息记录
      * @return 信息记录包含群聊和好友信息
      */
     RetFriendAndGroup getFriendAndGroup();

     /**
      * 获取好友帐号详情
      * @param json 用户帐号json形式
      * @return
      */
     UserCount getFriendInfo(String json);

     /**
      * 修改用户信息
      * @param photo 用户头像
      * @param json 帐号信息的json形式
      * @return 修改状态
      */
     String updateUserInfo(MultipartFile photo, String json);

     /**
      * 修改密码
      * @param json 密码实体
      * @return 修改状态
      */
     String updatePassword(String json);

     /**
      * 重置所有用户密码
      * @return
      */
     int resetAllUserPassword(String password);

     /**
      * 新建好友分组
      * @param shipType 分组名
      * @return 操作状态
      */
     String submitNewShip(String shipType);

     /**
      * 移动好友分组
      * @param json 分组信息
      * @return 操作状态
      */
     String updateShipType(String json);

     /**
      * 获取最近联系的账户
      * @return 最近联系的账户
      */
     List<CurrentTalkCount> getCurrentTalkCount();

     /**
      * 搜索账户，通过输入内容
      * @return 最近联系的账户
      */
     List<CurrentTalkCount> getSearchCountList();

     /**
      * 转发消息
      * @param forwardPeople 转发人员信息
      * @param msgValue 消息内容
      * @return 返回值
      */
     boolean forwardMsg( String forwardPeople, String msgValue);

}
