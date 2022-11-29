package com.broad.job.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.broad.job.entity.SysJobLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 调度任务日志信息 数据层
 *
 * @author XingGao
 */
@Mapper
public interface SysJobLogMapper extends BaseMapper<SysJobLog> {
    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合 list
     */
    List<SysJobLog> selectJobLogList(SysJobLog jobLog);

    /**
     * 查询所有调度任务日志
     *
     * @return 调度任务日志列表 list
     */
    List<SysJobLog> selectJobLogAll();

    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息 sys job log
     */
    SysJobLog selectJobLogById(Long jobLogId);

    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     * @return 结果 int
     */
    int insertJobLog(SysJobLog jobLog);

    /**
     * 批量删除调度日志信息
     *
     * @param logIds 需要删除的数据ID
     * @return 结果 int
     */
    int deleteJobLogByIds(Long[] logIds);

    /**
     * 删除任务日志
     *
     * @param jobId 调度日志ID
     * @return 结果 int
     */
    int deleteJobLogById(Long jobId);

    /**
     * 清空任务日志
     */
    void cleanJobLog();
}
