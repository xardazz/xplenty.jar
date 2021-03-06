## Xplenty Java Wrapper

[ ![Codeship Status for xplenty/xplenty.jar](https://codeship.com/projects/02b4fd80-c70e-0131-e4a4-02421a2eebea/status?branch=master)](https://codeship.com/projects/22116)

The Xplenty jar is a Java artifact that provides a simple wrapper for the [Xplenty REST Api](https://github.com/xplenty/xplenty-api-doc). 
To use it, create an XplentyAPI object and call its methods to access the Xplenty API.
This page describes the available XplentyAPI methods.

### Create an XplentyAPI Object

Pass your account ID and API key to the XplentyAPI constructor.
```java
String account_id = "MyAccountID";
String api_key = "V4eyfgNqYcSasXGhzNxS";
XplentyAPI xplentyAPI = new XplentyAPI(account_id , api_key);
```

If you want to supply custom values for the version, protocol, host, timeout, logging or client implementation that the XplentyAPI object will use,
you can use XplentyAPI builder methods to customize these properties.

```java
String account_id = "MyAccountID";
String api_key = "V4eyfgNqYcSasXGhzNxS";
Xplenty.Version version = Xplenty.Version.V1;
String host = 'myHost';
Http.Protocol proto = Http.Protocol.Https;
HttpClientBuilder builder = new HttpClientBuilder().withAccount(account_id).withApiKey(api_key).
                withHost(host).withLogHttpCommunication(true).withClientImpl(Http.HttpClientImpl.SyncNetty);
XplentyAPI xplentyAPI = new XplentyAPI(builder); 
```
### List the Cluster Plans

A cluster plan is a definition of a cluster type, which includes the number of nodes in the cluster and its pricing. Cluster plan details can be viewed in the Xplenty web application.
After you've determined which cluster plan is appropriate for your needs, use this method to retrieve the cluster plan ID. The cluster plan ID can then be used when creating a new cluster.

```java
for(ClusterPlan plan : xplentyAPI.listClusterPlans())
{
	long clusterPlanId = plan.getId();
}
```
### Create a Cluster

This method creates a new cluster. A cluster is a group of machines ("nodes") allocated to your account. The number of nodes in the cluster is determined by the "plan_id" value that you supply to the call. While the cluster is active, only your account's users can run jobs on the cluster.
You will need to provide an active cluster when starting a new job. Save the cluster ID value returned in the response "id" field. You will use the value to refer to this cluster in subsequent API calls.
```java
long plan_id = 1;
String name = "New Cluster #199999";
String description = "New Cluster's Description";
Cluster cluster = xplentyAPI.createCluster(plan_id, name, description);
long cluster_id = cluster.getId();
```
### List All Clusters

This method returns the list of clusters that were created by users in your account.
You can use this information to monitor and display your clusters and their statuses.
```java
for(Cluster cluster: xplentyAPI.listClusters())
{
	String name = cluster.getName();
	long id = cluster.getId();
}
```
XplentyAPI.listClusters() is shorthand for XplentyAPI.listClusters(new Properties()), which returns all clusters with no filtering.
You can also pass property parameters which filter the clusters according to their status, and determine the order in which they'll be sorted.
Only clusters which have the status passed in the PARAMETER_STATUS property will be returned, sorted according to the field passed in PARAMETER_SORT,
in ascending or descending order according to the PARAMETER_DIRECTION property.

```java
Properties params = new Properties();
params.put(ListClusters.PARAMETER_STATUS, ClusterStatus.available);
//params.put(ListClusters.PARAMETER_STATUS, "all");
params.put(ListClusters.PARAMETER_SORT, Sort.updated);
params.put(ListClusters.PARAMETER_DIRECTION, SortDirection.ascending);

for(Cluster cluster: xplentyAPI.listClusters(params))
{
	String name = cluster.getName();
	long id = cluster.getId();
}
```
### Get Cluster Information

This method returns the details of the cluster with the given ID.
```java
long id = 85;
Cluster cluster = xplentyAPI.clusterInformation(id);
String cluster_name = cluster.getName();
```
### Terminate a Cluster

This method deactivates the given cluster, releasing its resources and terminating its runtime period. Use this method when all of the cluster's jobs are completed and it's no longer needed. The method returns the given cluster's details, including a status of "pending_terminate".
```java
long id = 85;
Cluster cluster = xplentyAPI.terminateCluster(id);
ClusterStatus status = cluster.getStatus();
```
### Run a Job

This method creates a new job and triggers it to run. The job performs the series of data processing tasks that are defined in the job's package. Unless the job encounters an error or is terminated by the user, it will run until it completes its tasks on all of the input data. Save the job ID value returned in the response "id" field. You will use the value to refer to this job in subsequent API calls.
```java
long cluster_id = 83;
long package_id = 782;
Map<String, String> variables = new Map<String, String>();
variables.put("OUTPUTPATH", "test/job_vars.csv");
variables.put("Date", "09-10-2012");

Job job = xplentyAPI.runJob(cluster_id, package_id, variables);

long id = job.getId();
```
### List All Jobs

This method returns information for all the jobs that have been created under your account.
```java
for(Job job : xplentyAPI.listJobs())
{
	long id = job.getId();
	JobStatus status = job.getStatus();
	Double progress = job.getProgress();
}
```   
XplentyAPI.listJobs() is shorthand for XplentyAPI.listJobs(new Properties()), which returns all jobs with no filtering.
You can also pass property parameters which filter the jobs according to their status, and determine the order in which they'll be sorted.
Only jobs which have the status passed in the PARAMETER_STATUS property will be returned, sorted according to the field passed in PARAMETER_SORT,
in ascending or descending order according to the PARAMETER_DIRECTION property.

	Properties params = new Properties();
	params.put(ListJobs.PARAMETER_STATUS, JobStatus.available);
	//params.put(ListJobs.PARAMETER_STATUS, "all");
	params.put(ListJobs.PARAMETER_SORT, Sort.updated);
	params.put(ListJobs.PARAMETER_DIRECTION, SortDirection.ascending);
```java	
for(Job job : xplentyAPI.listJobs(params))
{
	long id = job.getId();
	JobStatus status = job.getStatus();
	Double progress = job.getProgress();
}
```
### Get Job Information

This method retrieves information for a job, according to the given job ID.
```java
long job_id = 235;
Job job = xplentyAPI.jobInformation(job_id);
JobStatus status = job.getStatus();
```
### Terminate a Job

This method terminates an active job. Usually it's unnecessary to request to terminate a job, because normally the job will end when its tasks are completed. You may want to actively terminate a job if you need its cluster resources for a more urgent job, or if the job is taking too long to complete.
```java
long job_id = 235;
Job job = xplentyAPI.stopJob(job_id);
JobStatus status = job.getStatus();
```
### Watch Cluster

You can watch a cluster that you or others have created. If you'r watching a cluster, you'll receive notifications (email messages or web notifications) on important updates.

This method adds the calling user as a watcher of the specifeid cluster.
```java
long clusterId = 17;
ClusterWatchingLogEntry res = xplentyAPI.addClusterWatchers(clusterId);
```
### Stop Watching Cluster

This method removes the calling user from the watcher list of the specified cluster.
```java
long clusterId = 17;
Boolean res = xplentyAPI.removeClusterWatchers(clusterId);
```
### Get Cluster Watchers

This call retrieves the list of users watching the specified cluster.
```java
long clusterId = 17;
List<Watcher> res = xplentyAPI.listClusterWatchers(clusterId);
```
### Watch Job

You can watch a job that you or others have executed. If you'r watching a job, you'll receive notifications (email messages or web notifications) on important updates.

This method adds the calling user as a watcher of the specifeid job.
```java
long jobId = 5;
JobWatchingLogEntry res = xplentyAPI.addJobWatchers(jobId);

```
### Stop Watching Job

This method removes the calling user from the watcher list of the specified job.
```java
long jobId = 5;
Boolean res = xplentyAPI.removeJobWatchers(jobId);
```
### Get Job Watchers

This call retrieves the list of users watching the specified job.
```java
long jobId = 5;
List<Watcher> res = xplentyAPI.listJobWatchers(jobId);
```
## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

## License
Released under the [MIT license](http://www.opensource.org/licenses/mit-license.php).
