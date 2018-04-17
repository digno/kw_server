package nz.co.rubz.kiwi.bean;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

import nz.co.rubz.kiwi.model.BaseEntity;

@Entity(noClassnameStored = true)
@Deprecated
public class Schedule extends BaseEntity implements Cloneable{

	private static final long serialVersionUID = 1L;

	@Property("noti_id")
	private String notiId;
	
	/** 任务名称 */
	@Property("job_name")
	private String jobName;

	/** 任务别名 */
	@Property("alias_name")
	private String aliasName;

	/** 任务分组 */
	@Property("job_group")
	private String jobGroup;

	/** 触发器 */
	@Property("job_trigger")
	private String jobTrigger;

	/** 任务状态 */
	@Property("job_status")
	private String jobStatus;

	/** 任务运行时间表达式 */
	@Property("corn_expression")
	private String cronExpression;

	/** 是否异步 */
	@Property("is_sync")
	private String isSync;

	/** 任务描述 */

	private String description;

	/** 创建时间 */
	@Property("action_time")
	private Date actionTime;

	/** 修改时间 */
	@Property("update_time")
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date updateTime;

	
	public String getNotiId() {
		return notiId;
	}

	public void setNotiId(String notiId) {
		this.notiId = notiId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobTrigger() {
		return jobTrigger;
	}

	public void setJobTrigger(String jobTrigger) {
		this.jobTrigger = jobTrigger;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getIsSync() {
		return isSync;
	}

	public void setIsSync(String isSync) {
		this.isSync = isSync;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	
	public Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
