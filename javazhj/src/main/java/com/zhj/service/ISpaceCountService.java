package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.entity.SpaceCount;
import com.zhj.entity.SpaceRemark;
import com.zhj.entity.SpaceValue;
import com.zhj.entity.UserCount;
import com.zhj.entity.ret.RetSpaceDetail;
import com.zhj.entity.ret.RetSpaceInfo;
import com.zhj.entity.ret.TalkBox;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhj
 * @since 2022-03-09
 */
public interface ISpaceCountService extends IService<SpaceCount> {

   /**
    * 发表说说
    * @param file 文件
    * @param msg 描述信息
    * @return 发表状态
    * @throws IOException file解析时可能涉及的异常
    */
   String talk(MultipartFile[] file, String msg) throws IOException;

   /**
    * 删除说说
    * @param talkId 说说id
    * @return 删除结果
    */
   String deleteTalk(Integer talkId);

   /**
    * 获取自己空间的前几条说说
    * @param json 用户帐户的json形式
    * @return 说说集合
    */
   RetSpaceInfo getDefaultInfo(String json);

   /**
    * 回到自己的空间
    * @param json 帐户信息
    * @return 空间信息
    */
   RetSpaceInfo goBackSpace(String json);

   /**
    * 获取空间信息
    * @param json 用户账号的json形式
    * @return 空间信息
    */
   RetSpaceDetail getSpaceInfo(String json);

   /**
    * 去到一个随机的空间
    * @return 空间信息
    */
   String getRandomSpace();

   /**
    * 去到好友空间
    * @param json 好友信息
    * @return 空间信息
    */
   RetSpaceInfo getFriendInfo(String json);

   /**
    * 通过用户账户获取空间信息
    * @param json 帐号的json形式
    * @return 空间信息
    */
   RetSpaceInfo getSpaceInfoByUser(String json);

   /**
    * 获取更新后信息在提交新说说之后
    * @return 第一条说说，会加入到空间说说第一条中
    */
   RetSpaceInfo getCurrentTalk();

   /**
    * 获取最近拜访人员名单
    * @return 最近拜访人员名单
    */
   List<UserCount> getCurrentVisitors();

   /**
    * 提交评论
    * @param spaceRemark 评论详情信息
    * @return 评论信息
    */
   TalkBox submitRemark(SpaceRemark spaceRemark);

   /**
    * 点赞
    * @param spaceValue 说说信息
    * @return 点赞后信息
    */
   TalkBox thumb(SpaceValue spaceValue);

   /**
    * 自动填充空间与用户状态绑定
    * @return 填充状态
    */
   String autoSpace();

   /**
    * 自动填充好友
    */
   void autoFillFriend();

   /**
    * 提交留言
    * @param json 留言的json形式
    * @return 操作状态
    */
   String submitPrivateTalk(String json);

   /**
    * 获取留言信息
    * @param json 分页等信息
    * @return 留言信息表
    */
   List<SpaceValue> getPrivateTalk(String json);

   /**
    * 获取空间照片墙
    * @param json 空间信息
    * @return 照片列表
    */
   List<SpaceValue> getSpacePhotoList(String json);

}
