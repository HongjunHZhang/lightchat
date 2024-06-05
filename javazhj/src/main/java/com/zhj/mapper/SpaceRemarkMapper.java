package com.zhj.mapper;

import com.zhj.entity.SpaceRemark;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhj
 * @since 2022-03-09
 */
public interface SpaceRemarkMapper extends BaseMapper<SpaceRemark> {
    /**
     * 获取说说信息
     * @param talkId 说说id
     * @param lastId 上一层的id,0表示第一层（即发起的评论-不是回复），其他表示对某一条评论或回复进行的回复
     * @return 返回说说评论信息
     */
    List<SpaceRemark> getSendRemark(@RequestParam("talkId") Integer talkId,@RequestParam("lastId") Integer lastId);

    /**
     * 获取评论信息
     * @param id 评论id
     * @return 评论信息
     */
    List<SpaceRemark> getRemarkById(@RequestParam("id") Integer id);
    /**
     * 获取评论信息
     * @param id 评论id
     * @return 评论信息
     */
    List<SpaceRemark> getReplayOfRemark(@RequestParam("id") Integer id);
}
