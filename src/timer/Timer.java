/**
 * 
 */
package timer;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.LADD;

/**
 * @author chenqian
 *
 */
public class Timer {

	private ThreadMXBean bean;
	private long start, end;
	private ArrayList<Long> laps;
	private final double MSSCALE = 1000000.0;
	private final double SSCALE = 1000000000.0;
	
	public Timer() {
		bean = ManagementFactory.getThreadMXBean();
		reset();
	}
	
	/**
	 * reset timer 
	 */
	public void reset() {
		start = bean.getCurrentThreadCpuTime();
		end = -1;
	}
	
	public void stop() {
		end = bean.getCurrentThreadCpuTime();
	}
	
	/**
	 * get time elapsed in ms.
	 * 
	 * */
	public double timeElapseinMs() {
		if (end == -1) stop();
		return (end - start) / MSSCALE;
	}
	
	
	/**
	 * get time elapsed in s.
	 * 
	 * */
	public double timeElapseinS() {
		return (end - start) / SSCALE;
	}
	
	/**
	 * Record a lap time.
	 */
	public double lap(){
		long lapEnd = bean.getCurrentThreadCpuTime();
		laps.add(lapEnd);
		return (lapEnd - start) / MSSCALE;
	}  
	
	/**
	 * 
	 * Clear lap times
	 * */
	public void clearLaps(){
		laps.clear();
	}
	
	/**
	 * Get laps times.
	 * */
	public ArrayList<Double> getLapsTime() {
		ArrayList<Double> lapsTime = new ArrayList<Double>();
		for(Long end : laps) {
			lapsTime.add((end - start) / MSSCALE);
		}
		return lapsTime;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Timer timer = new Timer(); 
		timer.reset();
		for (int ans = 0, i = 0; i < 1000000; i ++) {
			ans += i;
		}
		timer.stop();
		System.out.println("Time elapse: " + timer.timeElapseinMs() + "ms");
	}
}
