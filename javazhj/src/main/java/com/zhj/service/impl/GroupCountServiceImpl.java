package com.zhj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhj.entity.GroupCount;
import com.zhj.entity.GroupMember;
import com.zhj.entity.GroupMsg;
import com.zhj.entity.Journal;
import com.zhj.entity.ret.ActiveLb;
import com.zhj.entity.ret.RetActive;
import com.zhj.entity.ret.RetGroup;
import com.zhj.entity.ret.RetJournal;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.exception.CustomException;
import com.zhj.exception.ExceptionCodeEnum;
import com.zhj.mapper.GroupCountMapper;
import com.zhj.mapper.GroupMemberMapper;
import com.zhj.mapper.GroupMsgMapper;
import com.zhj.mapper.JournalMapper;
import com.zhj.service.IGroupCountService;
import com.zhj.util.TimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhj
 * @since 2022-02-21
 */
@Service
public class GroupCountServiceImpl extends ServiceImpl<GroupCountMapper, GroupCount> implements IGroupCountService {
    @Resource
    GroupMemberMapper groupMemberMapper;
    @Resource
    GroupCountMapper groupCountMapper;
    @Resource
    GroupMsgMapper groupMsgMapper;
    @Resource
    JournalMapper journalMapper;

    @Override
    public String updateLevel(String json) {
        String userCount = UserContext.getUserCount();
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String groupCount = AddUserServiceImpl.getGroupCount(map,userCount);
        GroupMember groupMember = groupMemberMapper.selectOne(new QueryWrapper<GroupMember>().eq("group_count", groupCount).eq("user_count", userCount)
                .eq("user_level", "0"));
        if (groupMember == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.GROUP_HAS_NOT_ROLE);
        }
        if (userCount.equals(map.get("aimCount"))){
            throw CustomException.createCustomException(ExceptionCodeEnum.GROUP_CHANGE_SELF_ROLE);
        }

        GroupMember groupMemberSelect = groupMemberMapper.selectOne(new QueryWrapper<GroupMember>().eq("group_count", groupCount).eq("user_count", map.get("aimCount")));
        if (groupMemberSelect == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_NOT_EXIST);
        }

        groupMemberSelect.setUserLevel("1".equals(groupMemberSelect.getUserLevel())?"2":"1");
        groupMemberMapper.update(groupMemberSelect,new QueryWrapper<GroupMember>().eq("id",groupMemberSelect.getId()));
        return "操作成功";
    }

    @Override
    public String deleteUser(String json) {
        String userCount = UserContext.getUserCount();
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String groupCount = AddUserServiceImpl.getGroupCount(map,userCount);
        GroupMember userCountLevel = groupMemberMapper.selectOne(new QueryWrapper<GroupMember>().eq("group_count", groupCount).eq("user_count", userCount));
        GroupMember aimLevel = groupMemberMapper.selectOne(new QueryWrapper<GroupMember>().eq("group_count", groupCount).eq("user_count", map.get("aimCount")));
        if (userCountLevel == null || aimLevel == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_NOT_EXIST);
        }
        if (userCountLevel.getUserLevel().compareTo(aimLevel.getUserLevel())>=0){
            throw CustomException.createCustomException(ExceptionCodeEnum.GROUP_HAS_NOT_ROLE);
        }else {
            groupMemberMapper.delete(new QueryWrapper<GroupMember>().eq("group_count", groupCount).eq("user_count", map.get("aimCount")));
            return "操作成功";
        }
    }


    @Override
    public GroupCount getGroupInfo(String json) {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String groupCount = map.get("groupCount");
        GroupCount groupCountRet = groupCountMapper.selectOne(new QueryWrapper<GroupCount>().eq("group_count", groupCount));
        if (groupCountRet == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.GROUP_NOT_EXIST);
        }
        return groupCountRet;
    }

    @Override
    public List<List<RetGroup>> getGroupPhoto(String groupCount) {
        List<RetGroup> groupMsgList = groupMsgMapper.getGroupPhoto(groupCount);
        Map<String, List<RetGroup>> collectRet = groupMsgList.stream().collect(Collectors.groupingBy(RetGroup::getSendId));
        List<List<RetGroup>> retData = new ArrayList<>(collectRet.values());
        retData = retData.stream().sorted((t1, t2)->t2.get(0).getCreateTime().compareTo(t1.get(0).getCreateTime())).collect(Collectors.toList());
        String simpleTime = TimeUtil.getZeroSimpleTime();
        retData.forEach(t ->t.get(0).setCreateTime(TimeUtil.getDayTime(t.get(0).getCreateTime(),simpleTime)));
        return retData;
    }

    @Override
    public List<GroupMsg> getGroupFile(String groupCount) {
        List<String> fileType = new ArrayList<>();
        fileType.add("3");
        fileType.add("4");
        fileType.add("5");
        return groupMsgMapper.selectList(new QueryWrapper<GroupMsg>().eq("group_id",groupCount).in("type",fileType).orderByDesc("create_time"));
    }

    @Override
    public String updateJournal(Journal journal) {
        String userCount = UserContext.getUserCount();
        GroupMember groupMember = groupMemberMapper.selectOne(new QueryWrapper<GroupMember>().eq("group_count", journal.getGroupId()).eq("user_count",userCount));
        if (groupMember == null || "2".equals(groupMember.getUserLevel())){
            throw CustomException.createCustomException(ExceptionCodeEnum.GROUP_HAS_NOT_ROLE);
        }
        String simpleDay = TimeUtil.getSimpleDay();
        if (!simpleDay.equals(journal.getCreateTime())&&"1".equals(groupMember.getUserLevel())){
            throw CustomException.createCustomException("只有群主才能修改历史日志，你只能修改今天的日志");
        }
        Journal journalSelect = journalMapper.selectOne(new QueryWrapper<Journal>().eq("create_time", journal.getCreateTime()));
        if (journalSelect != null){
            journalSelect.setMsg(journal.getMsg());
            journalMapper.update(journalSelect,new QueryWrapper<Journal>().eq("id",journalSelect.getId()));
        }else {
            journal.setCreateTime(TimeUtil.getSimpleDay());
            journalMapper.insert(journal);
        }
        return "修改群日志成功";
    }

    @Override
    public RetJournal getJournal(String json) {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        RetJournal retJournal = new RetJournal();
        String simpleDay = TimeUtil.getSimpleDay();
        if ("1".equals(map.get("pageNum"))){
            Journal journal = journalMapper.selectOne(new QueryWrapper<Journal>().eq("group_id", map.get("groupCount")).eq("create_time",simpleDay));
            if (journal == null){
                Journal journalRet = new Journal();
                journalRet.setCreateTime(simpleDay);
                retJournal.setJournal(journalRet);
                retJournal.setPageNum(1);
                retJournal.setFormatTime(TimeUtil.getFormatDay(simpleDay));
            }else {
                retJournal.setJournal(journal);
                retJournal.setPageNum(1);
                retJournal.setFormatTime(TimeUtil.getFormatDay(journal.getCreateTime()));
            }
        }else {
            int pageNum = Integer.parseInt(map.get("pageNum"));
            int tempNum = pageNum;
            Journal journalSelect = journalMapper.selectOne(new QueryWrapper<Journal>().eq("group_id", map.get("groupCount")).eq("create_time", simpleDay));
            int pageSize = Integer.parseInt(map.get("pageSize"));
            tempNum = journalSelect == null?tempNum-1:tempNum;
            PageHelper.startPage(tempNum,pageSize);
            List<Journal> journal = journalMapper.selectList(new QueryWrapper<Journal>().eq("group_id", map.get("groupCount")).orderByDesc("create_time"));
            PageInfo<Journal> journalPageInfo = new PageInfo<>(journal);
            int pageNumMax = journalPageInfo.getPages();
            if (journalSelect == null){
                pageNumMax = pageNumMax + 1;
            }
            retJournal.setJournal(journal.get(0));
            retJournal.setPageNum(Math.min(pageNum, pageNumMax));
            retJournal.setFormatTime(TimeUtil.getFormatDay(journal.get(0).getCreateTime()));
        }
        return retJournal;
    }

    @Override
    public RetJournal getCurrentJournal(String json) {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        PageHelper.startPage(1,1);
        Journal journal = journalMapper.selectOne(new QueryWrapper<Journal>().eq("group_id", map.get("groupCount")).orderByDesc("create_time"));
        RetJournal retJournal = new RetJournal();
        String simpleDay = TimeUtil.getSimpleDay();
        if (journal != null) {
            retJournal.setJournal(journal);
            retJournal.setFormatTime(TimeUtil.getFormatDay(journal.getCreateTime()));
            retJournal.setPageNum(simpleDay.equals(journal.getCreateTime())?1:2);
        }else {
            journal = new Journal();
            journal.setMsg("");
            journal.setCreateTime(TimeUtil.getSimpleDay());
            retJournal.setJournal(journal);
            retJournal.setFormatTime(TimeUtil.getFormatDay(journal.getCreateTime()));
            retJournal.setPageNum(1);
        }
        return retJournal;
    }

    @Override
    public RetJournal getJournalByDay(String json) {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        Journal journal = journalMapper.selectOne(new QueryWrapper<Journal>().eq("create_time", map.get("createTime")));
        RetJournal retJournal = new RetJournal();
        String simpleDay = TimeUtil.getSimpleDay();
        String formatDay = TimeUtil.getFormatDay(simpleDay);
        if (journal != null){
            retJournal.setJournal(journal);
            retJournal.setPageNum(simpleDay.equals(journal.getCreateTime())?1:2);
            retJournal.setFormatTime(TimeUtil.getFormatDay(journal.getCreateTime()));
        }else {
            Journal journalRet = new Journal();
            journalRet.setCreateTime(simpleDay);
            journalRet.setMsg("");
            retJournal.setJournal(journalRet);
            retJournal.setPageNum(1);
            retJournal.setFormatTime(formatDay);
        }
        return retJournal;
    }

    @Override
    public ActiveLb getGroupActive(String json) {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String zeroSimpleTime = TimeUtil.getZeroSimpleTime();
        List<RetGroup> groupMsg = groupMsgMapper.getGroupMsgByDay(map.get("groupCount"),zeroSimpleTime);
        double size = groupMsg.size()*1.000;
        Map<String, List<RetGroup>> collect = groupMsg.stream().collect(Collectors.groupingBy(RetGroup::getSendId));
        List<List<RetGroup>> listRet = new ArrayList<>(collect.values());
        List<List<RetGroup>> collectRet = listRet.stream().sorted((t1, t2) -> t2.size() - t1.size()).collect(Collectors.toList());
        List<RetActive> threeList = new ArrayList<>();
        List<RetActive> otherList = new ArrayList<>();
        double sum = 0;
        int sizeList = collectRet.size();
        for (int i = 0; i < sizeList ; i++) {
            RetActive retActive = new RetActive();
            retActive.setRetGroup(collectRet.get(i).get(0));
            if (i != sizeList-1){
                retActive.setPercentage(new BigDecimal(100*collectRet.get(i).size()/size).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue());
            }else {
                retActive.setPercentage(0.0);
            }
            sum += retActive.getPercentage();

            if (i < 3){
                threeList.add(retActive);
            }else {
                otherList.add(retActive);
            }
        }
        double lastPercentage = new BigDecimal(100 - sum).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (otherList.size() > 0){
            otherList.get(otherList.size()-1).setPercentage(lastPercentage);
        }else if (threeList.size() > 0){
            threeList.get(threeList.size()-1).setPercentage(lastPercentage);
        }
        ActiveLb activeLb = new ActiveLb();
        activeLb.setThreeList(threeList);
        activeLb.setOtherList(otherList);
        return activeLb;
    }
}
