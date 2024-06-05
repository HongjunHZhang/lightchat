package com.zhj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhj.entity.*;
import com.zhj.entity.dic.DefaultSrc;
import com.zhj.entity.dic.SpaceValueTypeDic;
import com.zhj.entity.ret.*;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.exception.CustomException;
import com.zhj.exception.ExceptionCodeEnum;
import com.zhj.mapper.*;
import com.zhj.service.ISpaceCountService;
import com.zhj.util.FileUtil;
import com.zhj.util.TimeUtil;
import com.zhj.util.cache.CacheRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhj
 * @since 2022-03-09
 */
@Slf4j
@Service
public class SpaceCountServiceImpl extends ServiceImpl<SpaceCountMapper, SpaceCount> implements ISpaceCountService {
    @Resource
    SpaceCountMapper spaceCountMapper;
    @Resource
    SpaceRemarkMapper spaceRemarkMapper;
    @Resource
    SpaceTalkMapper spaceTalkMapper;
    @Resource
    SpaceValueMapper spaceValueMapper;
    @Resource
    UserCountMapper userCountMapper;
    @Resource
    FriendMapper friendMapper;
    @Resource
    VisitorsMapper visitorsMapper;
    @Resource
    AdduserMapper adduserMapper;

    public static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    @Transactional(rollbackFor = Exception.class ,propagation = Propagation.REQUIRED)
    @Override
    public String talk(MultipartFile[] file, String msg) throws IOException {
        UserContext.checkIsTourist();
        String userCount = UserContext.getUserCount();
        List<String> simpleTimeList = new ArrayList<>();
            if (file != null) {
                for (MultipartFile multipartFile : file) {
                    String simpleTime = TimeUtil.getSimpleTime();
                    String fileName = UserContext.getUserCount() + "-" + simpleTime+".jpg";
                    String path = DefaultSrc.getRootUserFile()+"/"+userCount+"/spaceCount";
                    String actualName = FileUtil.saveFileAndCompress(multipartFile,fileName,path);
                    simpleTimeList.add(actualName);
                }
            }

        SpaceCount spaceCount = spaceCountMapper.selectOne(new QueryWrapper<SpaceCount>().eq("user_count", userCount));
        SpaceTalk spaceTalk = new SpaceTalk();
        fillSpaceTalk(spaceTalk,msg,spaceCount.getId(),userCount,simpleTimeList);
        return "发送动态成功";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String deleteTalk(Integer talkId) {
        SpaceTalk spaceTalk = spaceTalkMapper.selectOne(new QueryWrapper<SpaceTalk>().lambda().eq(SpaceTalk::getId, talkId));
        if (spaceTalk == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.NO_MARCH_RECORD);
        }

        if (!UserContext.getUserCount().equals(spaceTalk.getUserCount())){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_IDENTIFY_NOT_MARCH);
        }

        spaceTalkMapper.delete(new QueryWrapper<SpaceTalk>().lambda().eq(SpaceTalk::getId, talkId));
        int num = spaceRemarkMapper.delete(new QueryWrapper<SpaceRemark>().lambda().eq(SpaceRemark::getTalkId, talkId));
        log.info("id为" + talkId + "的说说成功删除，一共删除" + num + "条评论");
        List<SpaceValue> photoList = spaceValueMapper.selectList(new QueryWrapper<SpaceValue>().lambda().select(SpaceValue::getValueInfo)
        .eq(SpaceValue::getAssociatedId, talkId).eq(SpaceValue::getType, SpaceValueTypeDic.PHOTO_PATH.getCode()));
        spaceValueMapper.delete(new QueryWrapper<SpaceValue>().lambda().eq(SpaceValue::getAssociatedId, talkId).in(true,SpaceValue::getType,SpaceValueTypeDic.TALK_INFO));
        List<String> photoPathList = photoList.stream().map(SpaceValue::getValueInfo).collect(Collectors.toList());
        if (photoPathList.size() > 0){
            FileUtil.deleteFile(photoPathList);
        }
        return "删除成功";
    }

    public List<TalkBox> getTalkBox(QueryWrapper<SpaceTalk> queryWrapper, PageMine page, String userCount){
        List<TalkBox> talkBoxList = new ArrayList<>();
        String simpleTime = TimeUtil.getZeroSimpleTime();
        List<SpaceTalk> spaceTalkList;
        if (page == null) {
            spaceTalkList = spaceTalkMapper.selectList(queryWrapper);
        }else {
            PageHelper.startPage(page.getPageNum(),page.getPageSize());
            List<SpaceTalk> spaceTalkPage = spaceTalkMapper.selectList(queryWrapper);
            PageInfo<SpaceTalk> pageInfo = new PageInfo<>(spaceTalkPage);
            SpaceCountServiceImpl.threadLocal.set(pageInfo.getPages());
            spaceTalkList = pageInfo.getList();
        }
        for (SpaceTalk spaceTalk : spaceTalkList) {
            UserCount userCountSelect = CacheRoom.getUserCountOfCount(spaceTalk.getUserCount());
            List<String> photoInfo = spaceValueMapper.getPhotoInfo(spaceTalk.getId());
            TalkBox talkBox = new TalkBox();
            talkBox.setId(spaceTalk.getId());
            talkBox.setMsgInfo(spaceTalk.getMsg());
            talkBox.setCreateTime(TimeUtil.getDayTime(spaceTalk.getCreateTime(),simpleTime));
            talkBox.setSendNickName(userCountSelect.getNickName());
            talkBox.setSendId(userCountSelect.getUserCount());
            talkBox.setSendPhoto(userCountSelect.getPhoto());
            talkBox.setPhotoSrc(photoInfo);
            talkBox.setSpaceCount(spaceTalk.getId());
            List<Map<String,String>> thumbList = spaceValueMapper.getThumbInfo(spaceTalk.getId());
            talkBox.setThumbNum(thumbList.size());
            List<Thumb> retThumb = new ArrayList<>();
            thumbList.forEach(t ->{
                Thumb thumb = new Thumb();
                thumb.setNickName(t.get("V"));
                thumb.setId(t.get("C"));
                if (t.get("C").equals(userCount)){
                    talkBox.setHasThumb(true);
                }
                retThumb.add(thumb);
            });
            talkBox.setThumb(retThumb);
            List<SpaceRemark> spaceRemarkList = spaceRemarkMapper.getSendRemark(spaceTalk.getId(),null);
            if (spaceRemarkList.size() == 0){
                talkBox.setRemark(new ArrayList<>());
                talkBoxList.add(talkBox);
                continue;
            }
            List<String> userCountCollect = spaceRemarkList.stream().map(SpaceRemark::getSendId).distinct().collect(Collectors.toList());
            Map<String, List<SpaceRemark>> remarkCollect = spaceRemarkList.stream().collect(Collectors.groupingBy(SpaceRemark::getLastId));
            //获取第一层的评论
            String firstDeepRemark = "0";
            List<SpaceRemark> sendRemark = remarkCollect.getOrDefault(firstDeepRemark,new ArrayList<>());
            Map<String, UserCount> userCountCacheMap = CacheRoom.getUserCountCache();
            Map<String, UserCount> userCountOfCountListMap = CacheRoom.getUserCountOfCountList(userCountCollect);
            List<RetRemark> remarkList = new ArrayList<>();
            for (SpaceRemark spaceRemark : sendRemark) {
                RetRemark retRemark = new RetRemark();
                retRemark.setSendId(spaceRemark.getSendId());
                retRemark.setId(spaceRemark.getId());
                retRemark.setMsg(spaceRemark.getMsg());
                retRemark.setSendNickName(spaceRemark.getSendNickName());
                retRemark.setCreateTime(TimeUtil.getDayTime(spaceRemark.getCreateTime(),simpleTime));
                retRemark.setSendPhoto(spaceRemark.getSendPhoto());
                List<SpaceRemark> remarkById = spaceRemarkMapper.getReplayOfRemark(spaceRemark.getId());
                remarkById.forEach(t ->{
                            t.setCreateTime(TimeUtil.getDayTime(t.getCreateTime(),simpleTime));
                            UserCount userCountSend = userCountCacheMap.getOrDefault(t.getSendId(),userCountOfCountListMap.getOrDefault(t.getSendId(),new UserCount()));
                            t.setSendNickName(userCountSend.getNickName());
                            t.setSendPhoto(userCountSend.getPhoto());
                            UserCount userCountReceive = CacheRoom.getUserCountOfCount(t.getReceiveId());
                            t.setReceiveNickName(userCountReceive.getNickName());
                            t.setReceivePhoto(userCountReceive.getPhoto());
                        }
                        );
                retRemark.setReplay(remarkById);
                remarkList.add(retRemark);
            }
            talkBox.setRemark(remarkList);
            talkBoxList.add(talkBox);

        }
        return talkBoxList;

    }

    @Override
    public RetSpaceInfo getDefaultInfo(String json) {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String userCount = UserContext.getUserCount();
        String spaceUserCount = map.get("userCount");
        RetSpaceInfo retSpaceInfo = new RetSpaceInfo();
        if (!UserContext.getUserCount().equals(spaceUserCount)){
            Visitors visitors = new Visitors();
            visitors.setVisitorsId(userCount);
            visitors.setBevisitorsId(spaceUserCount);
            visitors.setCreateTime(TimeUtil.getSimpleTime());
            visitorsMapper.insert(visitors);
        }
        SpaceCount spaceCount = spaceCountMapper.selectOne(new QueryWrapper<SpaceCount>().eq("user_count", spaceUserCount));
        retSpaceInfo.setTalkBox(getTalkBox(new QueryWrapper<SpaceTalk>().eq("space_count",spaceCount.getId())
                .orderByDesc("create_time"), new PageMine(Integer.parseInt(map.get("pageNum")), Integer.parseInt(map.get("pageSize"))),spaceUserCount));
        retSpaceInfo.setMaxPageSize(threadLocal.get());
        threadLocal.remove();
        return retSpaceInfo;
    }

    @Override
    public RetSpaceInfo goBackSpace(String json) {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String userCount = UserContext.getUserCount();
        RetSpaceInfo retSpaceInfo = new RetSpaceInfo();
        UserCount nickName = CacheRoom.getUserCountOfCount(userCount);
        SpaceCount spaceCount = spaceCountMapper.selectOne(new QueryWrapper<SpaceCount>().eq("user_count", userCount));
        retSpaceInfo.setTalkBox(getTalkBox(new QueryWrapper<SpaceTalk>().eq("space_count",spaceCount.getId())
                .orderByDesc("create_time"), new PageMine(Integer.parseInt(map.get("pageNum")), Integer.parseInt(map.get("pageSize"))),userCount));
        retSpaceInfo.setMaxPageSize(threadLocal.get());
        threadLocal.remove();
        return retSpaceInfo;
    }

    @Override
    public RetSpaceDetail getSpaceInfo(String json) {
        RetSpaceDetail ret = new RetSpaceDetail();
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String userCount = map.getOrDefault("userCount",UserContext.getUserCount());
        ret.setSpaceCount(spaceCountMapper.selectOne(new QueryWrapper<SpaceCount>().eq("user_count", userCount)));
        ret.setIsMaster(userCount.equals(UserContext.getUserCount()) ? 1 : 0);
        ret.setIsFriend(userCount.equals(UserContext.getUserCount()) ? 1 : adduserMapper.getFriend(userCount, UserContext.getUserCount()).size() > 0 ? 1 : 0);
        ret.setUserCount(CacheRoom.getUserCountOfCount(userCount));
        return ret;
    }

    @Override
    public String getRandomSpace() {
        String userCount = UserContext.getUserCount();
        List<SpaceCount> friendSpaceCount = friendMapper.getFriendSpaceCountInfo(userCount);
        if (friendSpaceCount.size() == 0){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_HAS_NOT_FRIEND);
        }
            int n = friendSpaceCount.size();
            int m = new Random().nextInt(n);
        System.out.println(n + "-" +m);
        SpaceCount spaceCount = friendSpaceCount.get(m);
        if (spaceCount == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_SPACE_NOT_EXIST);
        }
        return spaceCount.getUserCount();
    }

    @Override
    public RetSpaceInfo getFriendInfo(String json) {
        String userCount = UserContext.getUserCount();
        Map<String, Integer> map = JSONObject.parseObject(json, new TypeReference<Map<String, Integer>>() {});
        RetSpaceInfo retSpaceInfo = new RetSpaceInfo();
        List<String> friendSpaceCount = friendMapper.getFriendSpaceCount(userCount);
        if (friendSpaceCount.size() == 0){
            return retSpaceInfo;
        }
        retSpaceInfo.setTalkBox(getTalkBox(new QueryWrapper<SpaceTalk>().in("space_count", friendSpaceCount)
                .orderByDesc("create_time"),new PageMine(map.get("pageNum"), map.get("pageSize")),userCount));
        retSpaceInfo.setMaxPageSize(threadLocal.get());
        threadLocal.remove();
        return retSpaceInfo;
    }

    @Override
    public RetSpaceInfo getSpaceInfoByUser(String json) {
        String userCount = UserContext.getUserCount();
        Map<String, Integer> map = JSONObject.parseObject(json, new TypeReference<Map<String, Integer>>() {});
        RetSpaceInfo retSpaceInfo = new RetSpaceInfo();
        List<SpaceCount> spaceCounts = spaceCountMapper.selectList(new QueryWrapper<SpaceCount>().eq("user_count", map.get("userCount")));
        retSpaceInfo.setTalkBox(getTalkBox(new QueryWrapper<SpaceTalk>().eq("space_count", spaceCounts.get(0).getId())
                .orderByDesc("create_time"),new PageMine(map.get("pageNum"), map.get("pageSize")),userCount));
        retSpaceInfo.setMaxPageSize(threadLocal.get());
        threadLocal.remove();
        return retSpaceInfo;
    }

    @Override
    public RetSpaceInfo getCurrentTalk() {
        String userCount = UserContext.getUserCount();
        SpaceCount spaceCount = spaceCountMapper.selectOne(new QueryWrapper<SpaceCount>().eq("user_count", userCount));
        RetSpaceInfo retSpaceInfo = new RetSpaceInfo();
        retSpaceInfo.setTalkBox(getTalkBox(new QueryWrapper<SpaceTalk>().in("space_count", spaceCount.getId())
                .orderByDesc("create_time"),new PageMine(1, 1),userCount));
        return retSpaceInfo;
    }

    @Override
    public List<UserCount> getCurrentVisitors() {
        String userCount = UserContext.getUserCount();
        return visitorsMapper.getCurrentVisitors(userCount);
    }

    @Override
    public TalkBox submitRemark(SpaceRemark spaceRemark) {
        UserContext.checkIsTourist();
        spaceRemark.setCreateTime(TimeUtil.getSimpleTime());
        spaceRemark.setSendId(UserContext.getUserCount());
        spaceRemarkMapper.insert(spaceRemark);
        List<TalkBox> talkBox = getTalkBox(new QueryWrapper<SpaceTalk>().eq("id",spaceRemark.getChangeId()),null,UserContext.getUserCount());
        return talkBox.get(0);
    }

    @Override
    public TalkBox thumb(SpaceValue spaceValue) {
        String userCount = UserContext.getUserCount();
        fillSpaceValue(spaceValue,userCount);
        List<TalkBox> talkBox = getTalkBox(new QueryWrapper<SpaceTalk>().eq("id",spaceValue.getAssociatedId()),null,UserContext.getUserCount());
        return talkBox.get(0);
    }

    @Override
    public String autoSpace() {
        List<UserCount> userCounts = userCountMapper.selectList(new QueryWrapper<>());
        SpaceCount spaceCount = new SpaceCount();
        spaceCount.setAnimal("0");
        spaceCount.setScore(0);
        for (UserCount userCount : userCounts) {
            if (spaceCountMapper.selectOne(new QueryWrapper<SpaceCount>().eq("user_count",userCount.getUserCount())) == null){
                spaceCount.setCreateTime(TimeUtil.getSimpleTime());
                spaceCount.setUserCount(userCount.getUserCount());
                spaceCount.setId(null);
                spaceCountMapper.insert(spaceCount);
            }

        }
        return "成功";
    }

    @Override
    public void autoFillFriend() {
        List<Friend> friendOtherMine = friendMapper.getFriendOtherMine();
        for (Friend friend : friendOtherMine) {
            String userId = friend.getUserId();
            String friendId = friend.getFriendId();
            friend.setId(null);
            friend.setUserId(friendId);
            friend.setFriendId(userId);
            friendMapper.insert(friend);
        }
    }

    @Override
    public String submitPrivateTalk(String json) {
        String userCount = UserContext.getUserCount();
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        SpaceValue spaceValue = new SpaceValue();
        spaceValue.setValueInfo( map.get("msg"));
        spaceValue.setAssociatedId(Integer.parseInt(map.get("friendSpaceId")));
        spaceValue.setSendId(userCount);
        spaceValue.setType("2");
        spaceValue.setCreateTime(TimeUtil.getSimpleTime());
        spaceValueMapper.insert(spaceValue);
        return "留言成功";
    }

    @Override
    public List<SpaceValue> getPrivateTalk(String json) {
        String userCount = UserContext.getUserCount();
        Map<String, Integer> map = JSONObject.parseObject(json, new TypeReference<Map<String, Integer>>() {});
        Integer spaceCount = spaceCountMapper.selectOne(new QueryWrapper<SpaceCount>().eq("user_count",userCount)).getId();
        PageHelper.startPage(map.get("pageNum"),map.get("pageSize"));
        List<SpaceValue> privateTalk = spaceValueMapper.getPrivateTalk(spaceCount);
        String simpleTime = TimeUtil.getZeroSimpleTime();
        privateTalk.forEach(t->t.setCreateTime(TimeUtil.getDayTime(t.getCreateTime(),simpleTime)));
        PageInfo<SpaceValue> spaceCountPageInfo = new PageInfo<>(privateTalk);
        return spaceCountPageInfo.getList();
    }

    @Override
    public List<SpaceValue> getSpacePhotoList(String json) {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        Integer spaceCount = spaceCountMapper.selectOne(new QueryWrapper<SpaceCount>().lambda().eq(SpaceCount::getUserCount,map.get("userCount"))).getId();
        List<SpaceTalk> spaceCountSelect = spaceTalkMapper.selectList(new QueryWrapper<SpaceTalk>().lambda().eq(SpaceTalk::getSpaceCount, spaceCount));
        List<Integer> collect = spaceCountSelect.stream().map(SpaceTalk::getId).collect(Collectors.toList());
        return spaceValueMapper.selectList(new QueryWrapper<SpaceValue>().lambda().eq(SpaceValue::getType, SpaceValueTypeDic.PHOTO_PATH.getCode()).in(SpaceValue::getAssociatedId, collect));
    }

    public void fillSpaceTalk(SpaceTalk spaceTalk, String msg, Integer spaceCount, String userCount, List<String> photoList){
        spaceTalk.setCreateTime(TimeUtil.getSimpleTime());
        spaceTalk.setMsg(msg);
        spaceTalk.setSpaceCount(spaceCount);
        spaceTalk.setUserCount(userCount);
        spaceTalkMapper.insert(spaceTalk);
        if (photoList.size() > 0) {
            SpaceValue spaceValue = new SpaceValue();
            String simpleTime = TimeUtil.getSimpleTime();
            spaceValue.setCreateTime(simpleTime);
            spaceValue.setAssociatedId(spaceTalk.getId());
            spaceValue.setSendId(spaceTalk.getUserCount());
            spaceValue.setType("0");
            String src = DefaultSrc.getUserPhotoSuffix()+"/"+userCount+"/spaceCount/";
            photoList.forEach(t -> {
                spaceValue.setId(null);
                spaceValue.setValueInfo(src + t);
                spaceValueMapper.insert(spaceValue);
            });
        }

    }
    void fillPhoto(Integer spaceCount,String src){
        SpaceValue spaceValue = new SpaceValue();
        spaceValue.setAssociatedId(spaceCount);
        spaceValue.setValueInfo(src);
        spaceValue.setCreateTime(TimeUtil.getSimpleTime());
        spaceValue.setType("1");
        spaceValueMapper.insert(spaceValue);

    }
    void fillSpaceValue(SpaceValue spaceValue,String userCount){
        SpaceValue spaceValueSelect = spaceValueMapper.selectOne(new QueryWrapper<SpaceValue>().lambda().eq(SpaceValue::getAssociatedId, spaceValue.getAssociatedId()).eq(SpaceValue::getSendId, userCount)
                .eq(SpaceValue::getType,SpaceValueTypeDic.THUMB_INFO.getCode()));
        if (spaceValueSelect != null){
            spaceValueMapper.delete(new QueryWrapper<SpaceValue>().lambda().eq(SpaceValue::getId,spaceValueSelect.getId()));
        }else {
            spaceValue.setCreateTime(TimeUtil.getSimpleTime());
            spaceValue.setType("1");
            spaceValue.setValueInfo("0");
            spaceValue.setSendId(userCount);
            spaceValueMapper.insert(spaceValue);
        }


    }



}
