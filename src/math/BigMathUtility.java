/**
 * 
 */
package math;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author chenqian
 *
 */
public class BigMathUtility {

	public static BigInteger lcm(BigInteger a, BigInteger b){
		return a.multiply(b).divide(a.gcd(b));
	}
	

	public static BigDecimal Square(BigDecimal a){
		return a.multiply(a);
	}
	
	public static BigDecimal getDistance2in2d(BigMathPoint a, BigMathPoint b){
		return Square(a.getX().subtract(b.getX())).add(Square(a.getY().subtract(b.getY())));
	}
	
	public static BigDecimal Det(BigMathPoint a, BigMathPoint b, BigMathPoint c){
		return c.getX().subtract(a.getX()).multiply(
				b.getY().subtract(a.getY())
			).subtract(
				b.getX().subtract(a.getX()).multiply(
					c.getY().subtract(a.getY())
				)
			);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BigInteger a = new BigInteger("5"), b = new BigInteger("3");
		System.out.println(lcm(a, b));
		a = new BigInteger("5"); b = new BigInteger("15");
		System.out.println(lcm(a, b));
	}

}
