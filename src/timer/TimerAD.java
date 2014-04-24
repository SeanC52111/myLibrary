package timer;

import java.util.ArrayList;

public class TimerAD {

	private long			start, end;
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
		start = System.nanoTime();
		end = -1;
	}

	public void stop() {
		end = System.nanoTime();
	}

	/**
	 * get time elapsed in ms.
	 * 
	 * */
	public double timeElapseinMs() {
		if (end == -1)
			stop();
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
	 * get time elapsed in us.
	 * 
	 * @return
	 */
	public double timeElapseinUs() {
		return (end - start) / USSCALE;
	}

	/**
	 * get time elapsed in ns.
	 * 
	 * @return
	 */
	public double timeElapseinNs() {
		return (end - start) * 1.0;
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
		Timer timer = new Timer();
		timer.reset();
		for (int ans = 0, i = 0; i < 1000000; i++) {
			ans += i;
		}
		timer.stop();
		System.out.println("Time elapse: " + timer.timeElapseinMs() + "ms");
	}

}
