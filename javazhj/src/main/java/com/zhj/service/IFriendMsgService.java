package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.entity.FriendMsg;
import com.zhj.entity.result.DataResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhj
 * @since 2022-09-28
 */
public interface IFriendMsgService extends IService<FriendMsg> {

    /**
     * 获取与好友间的信息
     * @param json 用户信息，包括分页大小等
     * @return 消息列表
     */
    DataResult getDefaultMsg(String json);

    /**
     * 好友间发送信息
     * @param file 文件
     * @param defaultInfo FriendMsg的json格式
     * @param viaGroup 是否通过群聊发起消息
     * @param bsId websocket发送消息信息
     * @return 发送消息结果
     * @throws IOException file Io异常
     */
    boolean sendMsg(MultipartFile file,String defaultInfo, String viaGroup,String bsId) throws IOException;

    /**
     * 在与他人视屏时拒绝其他人的视屏申请
     */
    void refuseCall();

    /**
     * test 方法
     */
     void modifyCount();

}
