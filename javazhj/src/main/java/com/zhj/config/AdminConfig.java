package com.zhj.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhj.entity.AdminInfo;
import com.zhj.mapper.AdminInfoMapper;
import com.zhj.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Optional;
import java.util.TimeZone;

/**
 * @author 789
 */
@Component
@Slf4j
public class AdminConfig {
	public static boolean isValid = true;
	public static String reason;
	@Resource
	AdminInfoMapper adminInfoMapper;

	@PostConstruct
	public void init() {
		AdminInfo adminInfo = adminInfoMapper.selectOne(new QueryWrapper<AdminInfo>().eq("user_name", "zhj").eq("password", "5201314"));
        if (adminInfo == null){
			log.error("账号密码错误，请不要使用盗版");
			isValid = false;
			reason = "账号密码错误，请不要使用盗版";
		}
        else {
			String simpleTime = TimeUtil.getSimpleTime();
			String s = Optional.ofNullable(adminInfo.getValidTime()).orElse("0000");
			if (simpleTime.compareTo(s)>0){
				log.error("系统过期请不要使用盗版");
				isValid = false;
				reason = "系统过期请不要使用盗版";
			}
		}

	}

	@PreDestroy
	public void destroy() {
		System.out.println("系统运行结束");
	}

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
	}

}
