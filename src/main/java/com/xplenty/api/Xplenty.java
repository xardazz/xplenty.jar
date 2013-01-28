/**
 * 
 */
package com.xplenty.api;

/**
 * A bunch of convenience structures
 * @author Yuriy Kovalek
 *
 */
public class Xplenty {
	public enum JobStatus {		
		Idle("idle"),
		Stopped("stopped"),
		Completed("completed"),
		Pending("pending"),
		Failed("failed"),
		Running("running"),
		PendingStoppage("pending_stoppage"),
		Stopping("stopping");
		
		private final String status;
		
		JobStatus(String status) {
			this.status = status;
		}

	}

	public static enum ClusterStatus {
		Pending("pending"),
		Error("error"),
		Creating("creating"),
		Available("available"),
		Scaling("scaling"),
		PendingTerminate("pending_terminate"),
		Terminating("terminating"),
		Terminated("terminated");
		
		private final String status;
		
		ClusterStatus(String status) {
			this.status = status;
		}
	}

	/**
	 * Endpoints and short descriptions for REST resources
	 */
	public static enum Resource {
		ClusterPlans("cluster_plans", "List cluster plans"),
		Clusters("clusters", "List clusters"), 
		Cluster("clusters/%s", "Get cluster information"),
		CreateCluster("clusters", "Create cluster"),
		TerminateCluster("clusters/%s", "Terminate cluster"),
		Jobs("jobs", "List jobs"),
		Job("jobs/%s", "Get job info"),
		RunJob("jobs", "Run job"),
		StopJob("jobs/%s", "Stop job");
		
		public final String value;
		public final String name;
		
		Resource(String val, String name) {
			this.value = val;
			this.name = name;
		}
		
		public String format(String... values) {
			return String.format(value, values);
		}
	}
	
	public static enum Version {
		V1(1);
		
		private final int value;
		
		Version(int ver) {
			this.value = ver;
		}
		
		public String format() {
			return "version=" + Integer.toString(value);
		}
	}
	
	public static enum Protocol {
		Http("http"),
		Https("https");
		
		public final String value;
		
		Protocol(String value) {
			this.value = value;
		}
	}
}