/**
 * 
 */
package com.xplenty.api.request;

import com.xplenty.api.Xplenty;
import com.xplenty.api.model.Job;

/**
 * @author Yuriy Kovalek
 *
 */
public class JobInfo extends AbstractInfoRequest<Job> {

    public JobInfo(long entityId) {
        super(entityId);
    }

    @Override
	public String getName() {
		return Xplenty.Resource.Job.name;
	}

	@Override
	public String getEndpoint() {
		return Xplenty.Resource.Job.format(Long.toString(entityId));
	}

}
