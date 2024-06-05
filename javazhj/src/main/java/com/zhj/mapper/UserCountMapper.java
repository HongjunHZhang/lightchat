package com.zhj.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhj.entity.UserCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhj.entity.ret.UserType;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhj
 * @since 2021-11-17
 */
public interface UserCountMapper extends BaseMapper<UserCount> {
   /**
    * 获取登录信息，检验是否存在该账号
    * @param userCount 用户账户
    * @return 账号信息
    */
   UserCount getLoginInfo(UserCount userCount);

   /**
    * 获取帐号好友列表
    * @param userCount 用户账户
    * @return 帐号好友列表信息
    */
   List<UserType> getFriend(@RequestParam("userCount") String userCount);

   /**
    * 获取帐号已加入的群信息
    * @param userCount 用户账户
    * @return 群列表，结果包含在群中的权限等级例（群主，管理员）
    */
   List<UserType> getGroup(@RequestParam("userCount") String userCount);

   /**
    * 标记帐号上线
    * @param userCount 用户账户
    * @return 操作行数
    */
   int online(@Param("userCount") String userCount);

   /**
    * 标记用户下线
    * @param userCount 用户账户
    * @return 操作行数
    */
   int outline(@Param("userCount") String userCount);

   /**
    * 获取所有在线用户
    * @return 用户列表
    */
   List<String> getOnlineUser();

   /**
    * 获取userCountList中在线的人员集合
    * @param userCountList 用户列表
    * @return 在线人员集合
    */
   int onlineList(@Param("userCountList") List<String> userCountList);

   /**
    * 获取userCountList中离线的人员集合
    * @param userCountList 用户列表
    * @return 离线人员集合
    */
   int outlineList(@Param("userCountList") List<String> userCountList);

   /**
    * 通过id更新空间信息
    * @param id 用户id
    * @param spaceId 空间id
    * @return 操作行数
    */
   int updateSpaceById(@Param("id") Integer id,@Param("space_id") Integer spaceId);

   /**
    * 修改用户帐号密码
    * @param userCount 用户账户
    * @param password 新密码
    * @return 操作行数
    */
   int updatePasswordByUserCount(@Param("userCount") String userCount,@Param("password") String password);

   /**
    *
    * @param password
    * @return
    */
   int resetAllPassword(@Param("password") String password);

   /**
    * 修改用户帐号关联邮箱
    * @param userCount 用户账号
    * @param email 新邮箱
    * @return 操作行数
    */
   int updateEmailByUserCount(@Param("userCount") String userCount,@Param("email") String email);

}
