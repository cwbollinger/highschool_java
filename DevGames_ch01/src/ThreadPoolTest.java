import java.util.Scanner;

public class ThreadPoolTest {

	public static void main(String[] args) {
		int numTasks;
		int numThreads;

		if (args.length == 2) {
			numTasks = Integer.parseInt(args[0]);
			numThreads = Integer.parseInt(args[1]);
		} else {
			Scanner scanner = new Scanner(System.in);

			System.out.println("Tests the ThreadPool task.");
			System.out.println("Usage: java ThreadPoolTest numTasks numThreads");

			System.out.println("Enter numTasks: # of task to run:");
			numTasks = scanner.nextInt();

			System.out.println("Enter numThreads: # of threads in pool:");
			numThreads = scanner.nextInt();
		}

		// create the thread pool
		ThreadPool threadPool = new ThreadPool(numThreads);

		// run example tasks
		for (int i = 0; i < numTasks; i++) {
			threadPool.runTask(createTask(i));
		}

		// close the pool and wait for all tasks to finish.
		threadPool.join();
	}

	/**
	 * Creates a simple Runnable that prints an ID, waits 500 milliseconds, then
	 * prints the ID again.
	 */
	private static Runnable createTask(final int taskID) {
		return new Runnable() {
			public void run() {
				System.out.println("Task " + taskID + ": start");

				// simulate a long-running task
				try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
				}

				System.out.println("Task " + taskID + ": end");
			}
		};
	}

}
