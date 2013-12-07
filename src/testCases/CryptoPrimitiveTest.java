/**
 * 
 */
package testCases;

import java.math.BigInteger;
import java.util.Random;

import IO.DataIO;
import crypto.AES;
import crypto.Gfunction;
import crypto.Hasher;
import crypto.RSA;
import timer.Timer;

/**
 * @author chenqian
 *
 */
public class CryptoPrimitiveTest {

	public static void testTime(){
		int times = 1000000;
		Timer timer = new Timer();
		System.out.println("=================== Hasher SHA-1 ===================");
		String str = "hello world";
		timer.reset();
		for (int i = 0; i < times; i ++) {
			str = Hasher.hashString(str);
		}
		timer.stop();
		System.out.println("Time Costs: " + timer.timeElapseinMs() / times + "ms");
		System.out.println("=====================================================");
		
		times = 1000;
		BigInteger p = BigInteger.probablePrime(1024, new Random());
		BigInteger phip = p.subtract(BigInteger.ONE), x = BigInteger.ONE;
		System.out.println("====================== g^2^1024 =======================");
		timer.reset();
		for (int i = 0; i < times; i ++) {
			x = phip.modPow(phip, p);
		}
		timer.stop();
		System.out.println("Time Costs: " + timer.timeElapseinMs() / times + "ms");
		System.out.println("=====================================================");
		
		RSA rsa = new RSA(); // personal implementation
		System.out.println("============ personel RSA encryption ================");
		timer.reset();
		BigInteger mes = BigInteger.probablePrime(1020, new Random()), cipher = null;
//		System.out.println(mes);
		for (int i = 0; i < times; i ++) {
			cipher = rsa.encrypt(mes);
		}
		timer.stop();
		System.out.println("Time Costs: " + timer.timeElapseinMs() / times + "ms");
		System.out.println("=====================================================");
		
		System.out.println("============ personel RSA decryption ================");
		timer.reset();
		for (int i = 0; i < times; i ++) {
			mes = rsa.decrypt(cipher);
		}
//		System.out.println(mes);
		timer.stop();
		System.out.println("Time Costs: " + timer.timeElapseinMs() / times + "ms");
		System.out.println("=====================================================");
		
		System.out.println("============ personel AES encryption ================");
		timer.reset();
		byte[] sk = AES.getSampleKey();
		byte[] byteMes = DataIO.padding("sdasfdasdf".getBytes(), 16);
		for (int i = 0; i < times; i ++) {
			byteMes = AES.encrypt(sk, byteMes);
		}
//		System.out.println(mes);
		timer.stop();
		System.out.println("Time Costs: " + timer.timeElapseinMs() / times + "ms");
		System.out.println("=====================================================");
		
		System.out.println("=============== prime generation ================");
		times = 100;
		timer.reset();
		for (int i = 0; i < times; i ++) {
			mes = BigInteger.probablePrime(1024, new Random());
		}
//		System.out.println(mes);
		timer.stop();
		System.out.println("Time Costs: " + timer.timeElapseinMs() / times + "ms");
		System.out.println("=====================================================");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testTime();
	}

}
