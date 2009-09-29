public interface JobScheduler {
	
	public void scheduleJob(Job job);
	public void abortJob(Job job);
}
