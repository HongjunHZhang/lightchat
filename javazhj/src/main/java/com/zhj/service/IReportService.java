package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.entity.ReportInfo;
import com.zhj.entity.UserCount;
import com.zhj.entity.ret.RetReportInfo;

import java.util.List;

/**
 * IReportService
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/12/22 11:56
 */
public interface IReportService extends IService<ReportInfo> {
    /**
     * 举报用户
     * @param reportInfo 举报实体
     * @return 举报状态
     */
    String reportUser( ReportInfo reportInfo);

    /**
     * 获取所有被举报用户列表
     * @return 用户信息列表
     */
    List<RetReportInfo> getReportUser();

    /**
     * 封禁用户
     * @param json 用户帐户
     * @return 封禁状态
     */
    String banUser(String json);

    /**
     * 查看被封禁用户列表
     * @return 被封禁用户列表
     */
    List<UserCount> getBanUser();

    /**
     * 解封用户
     * @param id 帐户id
     * @return 操作状态
     */
    String unBanUser(Integer id);
}
