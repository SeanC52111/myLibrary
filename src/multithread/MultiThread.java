/**
 * 
 */
package multithread;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author chenqian
 *
 */
public class MultiThread {

	boolean 		verbose			= false;
	int				mod				= 1;
	final int[] 	lock 			= new int[1];
	int 			threadNum 		= 2;
	boolean[] 		threadStatus 	= null;
	Task[] 			tasks 			= null;
	
	public void run() {
		lock[0] = 0;
		threadStatus = new boolean[threadNum];
		final int totalNum = tasks.length;
		for(int id = 0; id < threadNum; id ++) {
			threadStatus[id] = false;
			final int tid  = id;
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					int curid, threadId = tid;
					while(true){
						synchronized (lock) {
							curid = lock[0];
							lock[0]++;	
						}
						if(curid >= totalNum) break;
						tasks[curid].run();
						if (verbose) {
							if ((curid + 1) % mod == 0) {
								System.out.println("fin task " + curid);
							}
						}
					}
					threadStatus[threadId] = true;
				}
			}).start();
		}
		while(true){
			boolean found = false;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i = 0; i < threadNum; i++) {
				if(threadStatus[i] == false) {
					found = true;
				}
			}
			if(!found) break;
		}
	}
	
	public void setThreadNum(int num) {
		this.threadNum = num;
	}
	
	/**
	 * 
	 */
	public MultiThread() {
		// TODO Auto-generated constructor stub
	}
	
	public MultiThread(int threadNum) {
		// TODO Auto-generated constructor stub
		this.threadNum = threadNum;
	}

	public MultiThread(Task[] tasks, int threadNum) {
		// TODO Auto-generated constructor stub
		this.tasks = tasks;
		this.threadNum = threadNum;
	}
	
	public MultiThread(Task[] tasks, int threadNum, boolean verbose, int mod) {
		this.tasks 		= tasks;
		this.threadNum 	= threadNum;
		this.verbose	= verbose;
		this.mod		= mod;
	}
	
	
	
	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public int getMod() {
		return mod;
	}

	public void setMod(int mod) {
		this.mod = mod;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
