package nz.co.rubz.kiwi.thread;

import java.util.LinkedList;
import java.util.List;

public class MyThreadPool {

	// 默认的工作线程数
	private static int workerNum = 3;

	// 已经完成的任务数
	private static volatile int finish_tasks = 0;

	// 线程池内的工作线程数量
	private WorkerThread[] workThreads = null;

	// 待执行的任务队列
	private List<Runnable> taskQueue = new LinkedList<Runnable>();

	private static MyThreadPool threadPool = null;

	// 单例方法关键点，把构造函数private掉
	// 无法通过new MyThreadPool的形式初始化
	private MyThreadPool() {
		this(workerNum);
	}

	private MyThreadPool(int num) {
		workerNum = num;
		workThreads = new WorkerThread[workerNum]; // 初始化worker线程数组
		for (int i = 0; i < workerNum; i++) {
			workThreads[i] = new WorkerThread();
			workThreads[i].start(); // 启动线程池中的线程
		}
	}

	// 使用单例方法初始化线程池
	public static MyThreadPool newThreadPool(int num) {
		if (num <= 0) {
			num = workerNum;
		}
		workerNum = num;
		if (threadPool == null) {
			threadPool = new MyThreadPool(workerNum);
		}
		return threadPool;
	}

	// 每次执行就是将可执行的任务放到 任务队列中。由于LinkedList 线程不安全，所以需要sync
	public void execute(Runnable task) {
		synchronized (taskQueue) { 
			taskQueue.add(task);
			taskQueue.notifyAll();  // 唤醒所有等待的线程
		}
	}

	public void execute(Runnable[] tasks) {
		synchronized (taskQueue) {
			for (Runnable task : tasks) {
				taskQueue.add(task);
			}
			taskQueue.notifyAll();
		}
	}

	public void execute(List<Runnable> tasks) {
		synchronized (taskQueue) {
			for (Runnable task : tasks) {
				taskQueue.add(task);
			}
			taskQueue.notifyAll();
		}
	}

	public boolean shutdown() {
		while (!taskQueue.isEmpty()) { // 如果任务队列中还有任务尚未完成，则等待。直到所有任务完成后停止
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < workerNum; i++) {
			workThreads[i].stopWorker(); // 停止一个线程的常用方法，将线程是否正在运行的标记置为false
			workThreads[i] = null;
		}
		threadPool = null; //释放ThreadPool对象
		taskQueue.clear(); //线程池被销毁，任务队列应当被清除
		return true;
	}

	// 覆盖toString方法，返回线程池信息：工作线程个数和已完成任务个数
	@Override
	public String toString() {
		return "WorkThread number:" + workerNum + "  finished task number:" + finish_tasks + "  wait task number:"
				+ getWaitTasknumber();
	}

	private int getWaitTasknumber() {
		return taskQueue.size();
	}

	public int getFinishedTask() {
		return finish_tasks;
	}

	public int getWorkerNum() {
		return workerNum;
	}

	

	/**
	 * 内部类，工作线程
	 */
	private class WorkerThread extends Thread {
		//线程运行标记
		private boolean isRunning = true;
		Runnable r = null;
		@Override
		public void run() {
			while (isRunning) { // 线程一直运行
				synchronized (taskQueue) { // 获取taskQueue的同步锁。wait 必须在同步块中才生效
					while (isRunning && taskQueue.isEmpty()) { // 判断线程是否正在运行，任务队列中是否还有任务
						try {
							taskQueue.wait(20); // 正在运行，但任务队列为空，等待。20毫秒后继续竞争 taskQueue的锁
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (!taskQueue.isEmpty()) { //获取到了任务队列 锁。且任务队列不为空
						r = taskQueue.remove(0); // 删除链表中的元素，并返回被删除的元素。
					}
				}
				if (r != null) {
					r.run(); // 任务执行
				}
				finish_tasks++; // run方法执行完毕，标记着完成一个任务
				r = null; // 释放Runnable资源。
			}

		}

		public void stopWorker() {
			isRunning = false; // 置线程状态为关闭
		}

	}
}
