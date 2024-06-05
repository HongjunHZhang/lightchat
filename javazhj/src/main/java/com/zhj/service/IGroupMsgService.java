package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.entity.GroupMsg;
import com.zhj.entity.GroupMsgTemp;
import com.zhj.entity.ReportInfo;
import com.zhj.entity.ret.RetGroupMsg;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhj
 * @since 2022-10-09
 */
public interface IGroupMsgService extends IService<GroupMsgTemp> {
    /**
     * 获取群消息
     * @param json 群帐号以及分页等信息
     * @return 群消息（按分页情况）
     */
    RetGroupMsg getDefaultMsg(String json);

    /**
     * 获取群消息下一页记录
     * @param json 群账号以及分页等信息
     * @return  群消息（按分页情况）
     */
    RetGroupMsg getNextMsg(String json);

    /**
     * 群消息发送消息
     * @param file 文件
     * @param defaultInfo 消息详情描述
     * @param bsId websocket信息
     * @return 发送状态
     * @throws IOException file Io 异常信息
     */
    boolean saveMsg(MultipartFile file, String defaultInfo, String bsId) throws IOException;

    /**
     * 撤回群聊信息
     * @param groupMsg 撤回信息记录 id表示消息标识
     * @return
     */
    String withdrawGroupMsg( GroupMsg groupMsg);


}
