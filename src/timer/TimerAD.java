package timer;

import java.math.BigInteger;
import java.util.ArrayList;

public class TimerAD {

	private long			start, end, elapsed;
	private ArrayList<Long>	laps;
	private final double	USSCALE	= 1000.0;
	private final double	MSSCALE	= 1000000.0;
	private final double	SSCALE	= 1000000000.0;

	public TimerAD() {
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
		start = System.nanoTime();
	}

	public void stop() {
		end = System.nanoTime();
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
		long lapEnd = System.nanoTime();
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
		TimerAD timer = new TimerAD(), timer2 = new TimerAD();
		timer.reset();
		timer2.reset();
		BigInteger ans = new BigInteger("2");
		BigInteger mod = BigInteger.ONE.shiftLeft(127).subtract(ans);
		for (int i = 0; i < 10000; i++) {
			for (int j = 0; j < 1000; j++) {
				ans = ans.multiply(ans).mod(mod);
			}
		}
		timer2.pause();
//		timer2.resume();
		timer.stop();
		timer2.stop();
		System.out.println("Time elapse: " + timer.timeElapseinMs() + "ms");
		System.out.println("Time elapse: " + timer2.timeElapseinMs() + "ms");
	}

}
