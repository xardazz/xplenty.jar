/**
 * 
 */
package com.xplenty.api.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xplenty.api.Xplenty.JobStatus;
import com.xplenty.api.exceptions.XplentyAPIException;

/**
 * Data model for Xplenty job
 * 
 * @author Yuriy Kovalek
 *
 */
@XmlRootElement
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Job extends XplentyObject<Job> {
	
	public Job() {
		super(Job.class);
	}
	protected Long id;
	protected JobStatus status;
	protected Map<String, String> variables;	//for backwords compatibility
	@JsonProperty("dynamic_variables")
	protected Map<String, String> dynamicVariables;
	@JsonProperty("owner_id")
	protected Long ownerId;
	protected Double progress;
	@JsonProperty("outputs_count")
	protected Integer outputsCount;
	protected List<JobOutput> outputs;
	@JsonProperty("started_at")
	protected Date startedAt;
	@JsonProperty("created_at")
	protected Date createdAt;
    @JsonProperty("completed_at")
    protected Date completedAt;
    @JsonProperty("failed_at")
    protected Date failedAt;
	@JsonProperty("updated_at")
	protected Date updatedAt;
	@JsonProperty("cluster_id")
	protected Long clusterId;
	@JsonProperty("package_id")
	protected Long packageId;
	protected String errors;
	protected String url;
	@JsonProperty("runtime_in_seconds")
	protected Long runtimeInSeconds;




	/**
	 * Shorthand method for {@code waitForStatus(null, JobStatus...)} Will wait forever until the required status is received.
	 * @param statuses see {@link #waitForStatus(Long, JobStatus...)}
	 */
	public void waitForStatus(JobStatus... statuses) {
		waitForStatus(null, statuses);
	}
	
	/**
	 * Blocks execution until required status is received from the Xplenty server, or until timeout occurs.
	 * @param timeout time in seconds before terminating the wait, {@code null} to wait forever
	 * @param statuses list of statuses to wait for, see {@link JobStatus} for the list of supported statuses
	 */
	public void waitForStatus(Long timeout, JobStatus... statuses) {
		if (getParentApiInstance() == null)
			throw new XplentyAPIException("The parent API instance is not set");
		long start = System.currentTimeMillis();
		statusWait:
		while (true) {
			try {
				Thread.sleep(XplentyObject.StatusRefreshInterval);
			} catch (InterruptedException e) {
				throw new XplentyAPIException("Error sleeping", e);
			}
			Job c = getParentApiInstance().jobInformation(id);
			for (JobStatus status: statuses) {
				if (c.getStatus() == status)
					break statusWait;
			}
			if (System.currentTimeMillis() - timeout*1000 > start)
				throw new XplentyAPIException("Timeout occurred while waiting for required job status");
		}
	}
	
	public Job withId(long id) {
		this.id = id;
		return this;
	}
	
	public Job withPackage(long packageId) {
		this.packageId = packageId;
		return this;
	}
	
	public Job onCluster(long clusterId) {
		this.clusterId = clusterId;
		return this;
	}
	
	public Job withVariables(Map<String, String> vars) {
		this.variables = vars;
		return this;
	}
	
	public Job withDynamicVariables(Map<String, String> dynVars) {
		this.dynamicVariables = dynVars;
		return this;
	}
	
	public Long getId() {
		return id;
	}
	public JobStatus getStatus() {
		return status;
	}
	public Map<String, String> getVariables() {
		return variables;
	}
	
	public Long getOwnerId() {
		return ownerId;
	}
	public Double getProgress() {
		return progress;
	}
	public Integer getOutputsCount() {
		return outputsCount;
	}
	public List<JobOutput> getOutputs() {
		return outputs;
	}
	public Date getStartedAt() {
		return startedAt;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
    public Date getFailedAt() {
        return failedAt;
    }
    public Date getCompletedAt() {
        return completedAt;
    }
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public Long getClusterId() {
		return clusterId;
	}
	public Long getPackageId() {
		return packageId;
	}
	public String getErrors() {
		return errors;
	}
	public String getUrl() {
		return url;
	}
	public Long getRuntimeInSeconds() {
		return runtimeInSeconds;
	}
	@SuppressWarnings("unused")
	private void setId(long id) {
		this.id = id;
	}
	@SuppressWarnings("unused")
	private void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}
	@SuppressWarnings("unused")
	private void setStatus(JobStatus status) {
		this.status = status;
	}
	@SuppressWarnings("unused")
	private void setVariables(Map<String, String> variables) {
		this.variables = variables;
	}
	@SuppressWarnings("unused")
	private void setProgress(double progress) {
		this.progress = progress;
	}
	@SuppressWarnings("unused")
	private void setOutputsCount(int outputsCount) {
		this.outputsCount = outputsCount;
	}
	@SuppressWarnings("unused")
	private void setOutputs(List<JobOutput> outputs) {
		this.outputs = outputs;
	}
	@SuppressWarnings("unused")
	private void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}
	@SuppressWarnings("unused")
	private void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
    @SuppressWarnings("unused")
    private void setFailedAt(Date failedAt) {
        this.failedAt = failedAt;
    }
    @SuppressWarnings("unused")
    private void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }
	@SuppressWarnings("unused")
	private void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	@SuppressWarnings("unused")
	private void setClusterId(long clusterId) {
		this.clusterId = clusterId;
	}
	@SuppressWarnings("unused")
	private void setPackageId(long packageId) {
		this.packageId = packageId;
	}
	@SuppressWarnings("unused")
	private void setErrors(String errors) {
		this.errors = errors;
	}
	@SuppressWarnings("unused")
	private void setUrl(String url) {
		this.url = url;
	}
	@SuppressWarnings("unused")
	private void setRuntimeInSeconds(long runtimeInSeconds) {
		this.runtimeInSeconds = runtimeInSeconds;
	}
}
