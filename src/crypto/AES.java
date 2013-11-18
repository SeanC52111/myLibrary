/**
 * 
 */
package crypto;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import IO.DataIO;

/**
 * 
 * @author chenqian
 *
 */
public class AES {

	
	public static byte[] encrypt(byte[] key, byte[] mes) {
		 SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		 Cipher cipher;
		 byte[] encrypted = null;
			try {
				cipher = Cipher.getInstance("AES/ECB/NoPadding");
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
	 * 
	 * @param key
	 * @param mes
	 * @return
	 */
	public static BigInteger encryptBI(byte[] key, byte[] mes) {
		byte[] encrypted = encrypt(key, mes);
		return new BigInteger(DataIO.toHexFromBytes(encrypted), 16);
	}
	
	public static byte[] decrypt(byte[] key, byte[] encryped){
		 SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		 Cipher cipher;
		 byte[] decrypted = null;
			try {
				cipher = Cipher.getInstance("AES/ECB/NoPadding");
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
	
	public static byte[] decryptBI(byte[] key, BigInteger encrypted) {
		byte[] data = DataIO.padding(DataIO.toBytesFromHex(encrypted.toString(16)), 16);
		return decrypt(key, data);
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
		byte[] key = DataIO.toBytesFromHex("140b41b22a29beb4061bda66b6747e14");
		byte[] mes = DataIO.padding(DataIO.toBytesFromHex("12abcd"), 16);
		System.out.println(mes.length);
		System.out.println("message to encrypt:" + DataIO.toHexFromBytes(mes));
		BigInteger cipherBI = AES.encryptBI(key, mes);
		byte[] cipher = AES.encrypt(key, mes);
		byte[] mes2 = AES.decryptBI(key, cipherBI);
		System.out.println("message after encryptBI:" + DataIO.toHexFromBytes(mes2));
		mes2 = AES.decrypt(key, cipher);
		System.out.println("message after encrypt:" + DataIO.toHexFromBytes(mes2));
	}

}
