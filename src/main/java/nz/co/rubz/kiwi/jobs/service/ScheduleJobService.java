package nz.co.rubz.kiwi.jobs.service;

import java.util.List;

import nz.co.rubz.kiwi.bean.Schedule;

/**
 * 定时任务service
 *
 */
public interface ScheduleJobService {

    /**
     * 初始化定时任务
     */
    public void initScheduleJob();

    /**
     * 新增
     * 
     * @param schedule
     * @return
     */
    public String insert(Schedule  schedule );

    /**
     * 直接修改 只能修改运行的时间，参数、同异步等无法修改
     * 
     * @param schedule 
     */
    public void update(Schedule  schedule );

    /**
     * 删除重新创建方式
     * 
     * @param schedule 
     */
    public void delUpdate(Schedule  schedule );

    /**
     * 删除
     * 
     * @param scheduleJobId
     */
    public void delete(String scheduleJobId);

    /**
     * 运行一次任务
     *
     * @param scheduleJobId the schedule job id
     * @return
     */
    public void runOnce(String scheduleJobId);

    /**
     * 暂停任务
     *
     * @param scheduleJobId the schedule job id
     * @return
     */
    public void pauseJob(String scheduleJobId);

    /**
     * 恢复任务
     *
     * @param scheduleJobId the schedule job id
     * @return
     */
    public void resumeJob(String scheduleJobId);


    /**
     * 查询任务列表
     * 
     * @param schedule 
     * @return
     */
    public List<Schedule> queryList();

    /**
     * 获取运行中的任务列表
     *
     * @return
     */
    public List<Schedule> queryExecutingJobList();

}
