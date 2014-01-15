/**
 * 
 */
package crypto;

import io.IO;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author chenqian
 *
 */
public class AES {

	/**
	 * AES encrypt
	 * @param key
	 * @param mes
	 * @return
	 */
	public static byte[] encrypt(byte[] key, byte[] mes) {
		 SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		 Cipher cipher;
		 byte[] encrypted = null;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encrypted = cipher.doFinal(mes);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (encrypted);
	}
	
	/***
	 * AES encrypt, the result is a BigInterger
	 * @param key
	 * @param mes
	 * @return
	 */
	public static BigInteger encryptBI(byte[] key, byte[] mes) {
		byte[] encrypted = encrypt(key, mes);
		return new BigInteger(IO.toHexFromBytes(encrypted), 16);
	}
	
	/**
	 * AES decrypt.
	 * @param key
	 * @param encryped
	 * @return
	 */
	public static byte[] decrypt(byte[] key, byte[] encryped){
		 SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		 Cipher cipher;
		 byte[] decrypted = null;
			try {
				cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, skeySpec);
				decrypted = cipher.doFinal(encryped);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return (decrypted);
	}
	
	/**
	 * Decrypt the message in BigInteger.
	 * @param key
	 * @param encrypted
	 * @return
	 */
	public static byte[] decryptBI(byte[] key, BigInteger encrypted) {
		byte[] data = IO.padding(IO.toBytesFromHex(encrypted.toString(16)), 16);
		return decrypt(key, data);
	}
	
	/**
	 * get sampled key
	 * @return
	 */
	public static byte[] getSampleKey() {
		return IO.toBytesFromHex("140b41b22a29beb4061bda66b6747e14");
	}
	
	/**
	 * 
	 */
	public AES() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		byte[] key = IO.toBytesFromHex("140b41b22a29beb4061bda66b6747e14");
//		byte[] mes = DataIO.padding(DataIO.toBytesFromHex("12abcd"), 16);
		byte[] mes = IO.toBytesFromHex("12abcd");
		System.out.println(mes.length);
		System.out.println("message to encrypt:" + IO.toHexFromBytes(mes));
		BigInteger cipherBI = AES.encryptBI(key, mes);
		byte[] cipher = AES.encrypt(key, mes);
		byte[] mes2 = AES.decryptBI(key, cipherBI);
		System.out.println("message after encryptBI:" + IO.toHexFromBytes(mes2));
		mes2 = AES.decrypt(key, cipher);
		System.out.println("message after encrypt:" + IO.toHexFromBytes(mes2));
	}

}
