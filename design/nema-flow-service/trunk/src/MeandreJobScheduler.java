import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.meandre.client.MeandreClient;

public class MeandreJobScheduler implements JobScheduler {

	/* #MeandreJobSchedulerConfig lnkMeandreJobSchedulerConfig */
	private MeandreServer headNode;

	private Lock queueLock = new ReentrantLock();

	private Queue<Job> jobQueue = new LinkedList<Job>();

	private Lock clusterLock = new ReentrantLock();

	private Cluster cluster = new RoundRobinCluster();

	private final ScheduledExecutorService executor = Executors
			.newSingleThreadScheduledExecutor();

	private final ScheduledFuture<?> runJobsFuture;

	public MeandreJobScheduler(MeandreJobSchedulerConfig config) {
		headNode = config.getHeadNode();
		for (MeandreServer server : config.getServers()) {
			cluster.addServer(server);
		}
		runJobsFuture = executor.scheduleAtFixedRate(new RunQueuedJobs(), 10,
				5, TimeUnit.SECONDS);
	}

	public void abortJob(Job job) {
		MeandreServer executingServer = findExecutingServer(job);
		executingServer.abortJob(job);
	}

	/**
	 * Find the server that is executing the given {@link Job}.
	 * 
	 * @param job
	 *            The {@link Job} to find the executing server instance for.
	 * @return The {@link MeandreServer} that is currently executing the job, or
	 *         <code>null</code> if no known server is executing the job.
	 */
	private MeandreServer findExecutingServer(Job job) {
		clusterLock.lock();
		try {
			for (MeandreServer server : cluster.serverList()) {
				if (job.getServerAddress().equals(server.getHost())) {
					return server;
				}
			}
		} finally {
			clusterLock.unlock();
		}
		return null;
	}

	public void scheduleJob(Job job) {
		queueLock.lock();
		try {
			jobQueue.offer(job);
		} finally {
			queueLock.unlock();
		}
	}

	private class RunQueuedJobs implements Runnable {
		public void run() {
			System.out.println("Running queued jobs.");
			runJobs();
		}
	}

	private void runJobs() {
		queueLock.lock();
		clusterLock.lock();
		try {
			if (jobQueue.size() < 1) {
				System.out.println("No queued jobs.");
				return;
			}
			boolean availableServers = true;
			while (!jobQueue.isEmpty() && availableServers == true) {
				MeandreServer server = cluster.nextAvailableServer();
				if (server == null) {
					System.out.println(jobQueue.size() + " jobs are queued but all servers are busy.");
                    availableServers = false;
					return;
				}
				Job job = jobQueue.poll();
				// update job
				// persist job
				server.executeJob(job);
			}
		} finally {
			clusterLock.unlock();
			queueLock.unlock();
		}
	}

	private class RoundRobinCluster implements Cluster {
		Node curr = null;

		int size = 0;

		public MeandreServer nextAvailableServer() {
			if (size == 0) {
				return null;
			}

			assert curr != null : "Current node should not be null";

			Node headNode = curr.getNext();
			Node iterNode = headNode;

			do {
				if (!iterNode.getServer().isBusy()) {
					curr = iterNode;
					return iterNode.getServer();
				}
				iterNode = iterNode.getNext();

			} while (!iterNode.equals(headNode));

			return null;
		}

		public List<MeandreServer> serverList() {
			if (size == 0) {
				return null;
			}

			assert curr != null : "Node pointer curr should not be null";

			List<MeandreServer> serverList = new ArrayList<MeandreServer>();

			Node headNode = curr.getNext();
			Node iterNode = headNode;

			do {
				serverList.add(iterNode.getServer());
				iterNode = iterNode.getNext();

			} while (!iterNode.equals(headNode));

			return serverList;
		}

		public void addServer(MeandreServer server) {
			Node node = new Node(server);
			if (size == 0) {
				node.setNext(node);
				node.setPrev(node);
				curr = node;
			} else {
				node.setNext(curr.getNext());
				curr.getNext().setPrev(node);
				node.setPrev(curr);
				curr.setNext(node);
			}
			size++;
		}

		public void removeServer(MeandreServer server)
				throws IllegalStateException {
			if (size == 0) {
				return;
			}

			Node match = findNodeWithServer(server);

			if (match == null) {
				return;
			}

			if (server.getNumJobsRunning() != 0) {
				throw new IllegalStateException(
						"Server "
								+ server.getHost()
								+ ":"
								+ server.getPort()
								+ " cannot be removed from the scheduler because it is currently running a job.");
			}

			if (size == 1) {
				curr = null;
			} else {
				if (curr == match) {
					curr = curr.getNext();
				}
				match.getNext().setPrev(match.getPrev());
				match.getPrev().setNext(match.getNext());
			}
			size--;
		}

		private Node findNodeWithServer(MeandreServer server) {
			if (size == 0) {
				return null;
			}

			Node headNode = curr;
			Node iterNode = curr;

			do {
				if (iterNode.getServer().equals(server)) {
					return iterNode;
				}
				iterNode = iterNode.getNext();

			} while (!iterNode.equals(headNode));

			return null;
		}

		private class Node {
			MeandreServer server = null;
			Node next = null;
			Node prev = null;

			public Node(MeandreServer server) {
				this.server = server;
			}

			public void setNext(Node next) {
				this.next = next;
			}

			public Node getNext() {
				return next;
			}

			public void setPrev(Node prev) {
				this.prev = prev;
			}

			public Node getPrev() {
				return prev;
			}

			public MeandreServer getServer() {
				return server;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result
						+ ((server == null) ? 0 : server.hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				final Node other = (Node) obj;
				if (server == null) {
					if (other.server != null)
						return false;
				} else if (!server.equals(other.server))
					return false;
				return true;
			}



		}
	}

	private interface Cluster {
		public MeandreServer nextAvailableServer();

		public void addServer(MeandreServer server);

		public void removeServer(MeandreServer server)
				throws IllegalStateException;

		public List<MeandreServer> serverList();
	}

	public static void main(String[] args) {
		MeandreJobSchedulerConfig config = new MeandreJobSchedulerConfig() {
			public MeandreServer getHeadNode() {
				return new MeandreServer("localhost", 10000, 1, true);
			}

			public List<MeandreServer> getServers() {
				List<MeandreServer> servers = new ArrayList<MeandreServer>();
				servers.add(new MeandreServer("192.168.0.1", 10000, 1, false));
				servers.add(new MeandreServer("192.168.0.2", 10000, 1, false));
				servers.add(new MeandreServer("192.168.0.3", 10000, 1, false));
				servers.add(new MeandreServer("192.168.0.4", 10000, 1, false));
				return servers;
			}
		};
		MeandreJobScheduler jobScheduler = new MeandreJobScheduler(config);
		System.out.println("inserted 4 servers...");
		System.out.println(((RoundRobinCluster) jobScheduler.cluster).size);
		
		System.out.println(jobScheduler.cluster.serverList().size()==4);
		

		for (int i = 0; i < 4; i++) {
			jobScheduler.scheduleJob(new Job());
		}


		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < 8; i++) {
			System.out.println(jobScheduler.cluster.nextAvailableServer()
					.getHost());
		}
		System.out.println("removing server 1...");
		jobScheduler.cluster.removeServer(new MeandreServer("192.168.0.1",
				10000, 1, false));
		for (int i = 0; i < 6; i++) {
			System.out.println(jobScheduler.cluster.nextAvailableServer()
					.getHost());
		}
		System.out.println("removing server 2...");
		jobScheduler.cluster.removeServer(new MeandreServer("192.168.0.2",
				10000, 1, false));
		for (int i = 0; i < 4; i++) {
			System.out.println(jobScheduler.cluster.nextAvailableServer()
					.getHost());
		}
		System.out.println("removing server 3...");
		jobScheduler.cluster.removeServer(new MeandreServer("192.168.0.3",
				10000, 1, false));
		for (int i = 0; i < 2; i++) {
			System.out.println(jobScheduler.cluster.nextAvailableServer()
					.getHost());
		}
		System.out.println("removing server 4...");
		jobScheduler.cluster.removeServer(new MeandreServer("192.168.0.4",
				10000, 1, false));
		for (int i = 0; i < 1; i++) {
			System.out
					.println(jobScheduler.cluster.nextAvailableServer() == null);
		}

		System.out.println("finishing...");
	}
}
