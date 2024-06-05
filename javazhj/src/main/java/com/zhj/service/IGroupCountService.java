package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.entity.GroupCount;
import com.zhj.entity.GroupMsg;
import com.zhj.entity.Journal;
import com.zhj.entity.ret.ActiveLb;
import com.zhj.entity.ret.RetGroup;
import com.zhj.entity.ret.RetJournal;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhj
 * @since 2022-02-21
 */
public interface IGroupCountService extends IService<GroupCount> {

    /**
     * 更新用户在群聊中的权限等级
     * @param json 用户信息
     * @return 更新状态
     */
    String updateLevel(String json);

    /**
     * 删除用户
     * @param json 删除账户信息等
     * @return 删除状态
     */
    String deleteUser(String json);

    /**
     * 获取群聊信息
     * @param json 群帐号的json字符串形式
     * @return 群聊信息
     */
    GroupCount getGroupInfo(String json);

    /**
     * 获取群所有图片集合
     * @param groupCount 群帐号
     * @return 群图片集合
     */
    List<List<RetGroup>> getGroupPhoto(String groupCount);

    /**
     * 获取群文件
     * @param groupCount 群帐号
     * @return 群文件
     */
    List<GroupMsg> getGroupFile(String groupCount);

    /**
     * 更新日程表
     * @param journal 日程表实体
     * @return 更新状态
     */
    String updateJournal(Journal journal);

    /**
     * 获取日程表
     * @param json 群账号以及页数的json形式
     * @return 日程表信息
     */
    RetJournal getJournal(String json);

    /**
     * 获取当前日程表
     * @param json 群账号的json形式
     * @return 当前群日志信息
     */
    RetJournal getCurrentJournal(String json);

    /**
     * 通过日期获取日志
     * @param json 日期的json形式
     * @return 日志信息
     */
    RetJournal getJournalByDay(String json);

    /**
     * 获取群成员活跃排名情况
     * @param json 群账号的json形式
     * @return 活跃排名信息
     */
    ActiveLb getGroupActive(String json);

}
