package com.zhj.service.impl;

import com.zhj.entity.LogRecord;
import com.zhj.mapper.LogRecordMapper;
import com.zhj.service.ILogRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhj
 * @since 2022-12-28
 */
@Service
public class LogRecordServiceImpl extends ServiceImpl<LogRecordMapper, LogRecord> implements ILogRecordService {

}
