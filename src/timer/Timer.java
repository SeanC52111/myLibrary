/**
 * 
 */
package timer;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @author chenqian
 * 
 */
public class Timer {

	private ThreadMXBean	bean;
	private long			start, end, elapsed;
	private ArrayList<Long>	laps;
	private final double	USSCALE	= 1000.0;
	private final double	MSSCALE	= 1000000.0;
	private final double	SSCALE	= 1000000000.0;

	public Timer() {
		bean = ManagementFactory.getThreadMXBean();
		reset();
	}

	/**
	 * reset timer
	 */
	public void reset() {
		resume();
		end = -1;
		elapsed = 0;
	}
	
	/**
	 * Pause the timer
	 */
	public void pause() {
		stop();
		elapsed += end - start;
		resume();
	}
	
	/**
	 * 
	 */
	public void resume() {
		start = bean.getCurrentThreadCpuTime();
	}

	public void stop() {
		end = bean.getCurrentThreadCpuTime();
	}

	public long getTotal() {
		if (end == -1)
			stop();
		return (elapsed + end - start);
	}
	
	/**
	 * get time elapsed in ms.
	 * 
	 * */
	public double timeElapseinMs() {
		return getTotal() / MSSCALE;
	}

	/**
	 * get time elapsed in s.
	 * 
	 * */
	public double timeElapseinS() {
		return getTotal() / SSCALE;
	}

	/**
	 * get time elapsed in us.
	 * 
	 * @return
	 */
	public double timeElapseinUs() {
		return getTotal() / USSCALE;
	}

	/**
	 * get time elapsed in ns.
	 * 
	 * @return
	 */
	public double timeElapseinNs() {
		return getTotal() * 1.0;
	}

	/**
	 * Record a lap time.
	 */
	public double lap() {
		long lapEnd = bean.getCurrentThreadCpuTime();
		laps.add(lapEnd);
		return (lapEnd - start) / MSSCALE;
	}

	/**
	 * 
	 * Clear lap times
	 * */
	public void clearLaps() {
		laps.clear();
	}

	/**
	 * Get laps times.
	 * */
	public ArrayList<Double> getLapsTime() {
		ArrayList<Double> lapsTime = new ArrayList<Double>();
		for (Long end : laps) {
			lapsTime.add((end - start) / MSSCALE);
		}
		return lapsTime;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Timer timer = new Timer(), timer2 = new Timer();
		timer.reset();
		timer2.reset();
		BigInteger ans = new BigInteger("2");
		BigInteger mod = BigInteger.ONE.shiftLeft(127).subtract(ans);
		for (int i = 0; i < 1000; i++) {
			
			for (int j = 0; j < 1000; j++) {
				ans = ans.multiply(ans).mod(mod);
			}
		}
		timer2.pause();
		timer2.resume();
		timer.stop();
		timer2.stop();
		System.out.println("Time elapse: " + timer.timeElapseinMs() + "ms");
		System.out.println("Time elapse: " + timer2.timeElapseinMs() + "ms");
	}
}
