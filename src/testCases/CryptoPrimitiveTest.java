/**
 * 
 */
package testCases;

import java.math.BigInteger;
import java.util.Random;

import crypto.Gfunction;
import crypto.Hasher;
import timer.Timer;

/**
 * @author chenqian
 *
 */
public class CryptoPrimitiveTest {

	public static void testTime(){
		int times = 1000000;
		Timer timer = new Timer();
		System.out.println("begin test:");
		String str = "hello world";
		timer.reset();
		for (int i = 0; i < times; i ++) {
			str = Hasher.hashString(str);
		}
		timer.stop();
		System.out.println("Hash costs: " + timer.timeElapseinMs() / times + "ms");
		
		times = 1000;
		BigInteger p = BigInteger.probablePrime(1024, new Random());
		BigInteger phip = p.subtract(BigInteger.ONE), x = BigInteger.ONE;
		System.out.println("begin test:");
		timer.reset();
		for (int i = 0; i < times; i ++) {
			x = phip.modPow(phip, p);
		}
		timer.stop();
		System.out.println("Power costs: " + timer.timeElapseinMs() / times + "ms");
		
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testTime();
	}

}
