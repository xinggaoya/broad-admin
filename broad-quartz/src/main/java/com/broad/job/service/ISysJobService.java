package com.broad.job.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.broad.common.exception.job.TaskException;
import com.broad.job.entity.SysJob;
import com.broad.job.mapper.SysJobMapper;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 定时任务调度信息信息 服务层
 *
 * @author XingGao
 */
public interface ISysJobService extends IService<SysJob> {
    /**
     * 获取quartz调度器的计划任务
     *
     * @param job 调度信息
     * @return 调度任务集合 list
     */
    IPage<SysJob> selectJobList(SysJob job);

    /**
     * 通过调度任务ID查询调度信息
     *
     * @param jobId 调度任务ID
     * @return 调度任务对象信息 sys job
     */
    SysJob selectJobById(Long jobId);

    /**
     * 暂停任务
     *
     * @param job 调度信息
     * @return 结果 int
     * @throws SchedulerException the scheduler exception
     */
    int pauseJob(SysJob job) throws SchedulerException;

    /**
     * 恢复任务
     *
     * @param job 调度信息
     * @return 结果 int
     * @throws SchedulerException the scheduler exception
     */
    int resumeJob(SysJob job) throws SchedulerException;

    /**
     * 删除任务后，所对应的trigger也将被删除
     *
     * @param job 调度信息
     * @return 结果 int
     * @throws SchedulerException the scheduler exception
     */
    int deleteJob(SysJob job) throws SchedulerException;

    /**
     * 批量删除调度信息
     *
     * @param jobIds 需要删除的任务ID
     * @return 结果
     * @throws SchedulerException the scheduler exception
     */
    void deleteJobByIds(Long[] jobIds) throws SchedulerException;

    /**
     * 任务调度状态修改
     *
     * @param job 调度信息
     * @return 结果 int
     * @throws SchedulerException the scheduler exception
     */
    int changeStatus(SysJob job) throws SchedulerException;

    /**
     * 立即运行任务
     *
     * @param job 调度信息
     * @return 结果
     * @throws SchedulerException the scheduler exception
     */
    void run(SysJob job) throws SchedulerException;

    /**
     * 新增任务
     *
     * @param job 调度信息
     * @return 结果 int
     * @throws SchedulerException the scheduler exception
     * @throws TaskException      the task exception
     */
    int insertJob(SysJob job) throws SchedulerException, TaskException;

    /**
     * 更新任务
     *
     * @param job 调度信息
     * @return 结果 int
     * @throws SchedulerException the scheduler exception
     * @throws TaskException      the task exception
     */
    int updateJob(SysJob job) throws SchedulerException, TaskException;

    /**
     * 校验cron表达式是否有效
     *
     * @param cronExpression 表达式
     * @return 结果 boolean
     */
    boolean checkCronExpressionIsValid(String cronExpression);
}