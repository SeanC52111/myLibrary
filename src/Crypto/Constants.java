/**
 * 
 */
package Crypto;

import java.math.BigInteger;

/**
 * @author chenqian
 *
 */
public class Constants {

	public final static BigInteger TWO = new BigInteger("2");
	public final static BigInteger PRIME_P = TWO.pow(107).subtract(BigInteger.ONE);
	public final static BigInteger PRIME_Q = TWO.pow(127).subtract(BigInteger.ONE);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
