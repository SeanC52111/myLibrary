package crypto;

/**
 * @author chenqian
 * 
 * Instance of SHA-1 (default), returns 160 bits.
 * Instance of MD%, returns 128 bits.
 * 
 * */

import java.security.MessageDigest;
import IO.DataIO;



public class Hasher {

	private static MessageDigest HASHER = null;

	private static byte[] lock = new byte[0];

	static {
		init();
	}


	public static void main(String[] args) {
		
		int times = 10000;
		long start = System.currentTimeMillis();
		while(times -- > 0){
			String testStr = "13010177899260974698268335948222812229032559169368248982715056251741919|14180373046968447110771776474120209297778825660630606807910598702665019|5887452964664100737530293136278805430831466489207678051240397325075669|10376367661798113032690241680154132210665619298112842979765174675441004|13010177899260974698268335948222812229032559169368248982715056251741919|14180373046968447110771776474120209297778825660630606807910598702665019|5887452964664100737530293136278805430831466489207678051240397325075669|10376367661798113032690241680154132210665619298112842979765174675441004|";
			String result = hashString(testStr);
			System.out.println(result);
		}
		System.out.println(Hasher.getInstanceName());
		System.out.println(System.currentTimeMillis() - start);
	}

	/**
	 * @return the name of an instance
	 * */
	public static String getInstanceName(){
		return HASHER.getAlgorithm();
	}
	
	
	/**
	 * @param instance
	 * 				set the instance of Hasher, e.g., SHA-1, MD5
	 * */
	public static void setInstance(String instance){
		try {
			HASHER = MessageDigest.getInstance(instance);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private static void init() {
		try {
			HASHER = MessageDigest.getInstance("SHA-1");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * 
	 * @param str
	 * 				str in plain string
	 * @return HEX string
	 */
	public static String hashString(String str) {
		try {
			//System.err.println(str);
			byte[] data = DataIO.toBytesFromHex(DataIO.toHexFromString(str));
			synchronized (lock) {
				HASHER.update(data);
				data = HASHER.digest();
			}
			return DataIO.toHexFromBytes(data);
			//return EncodeConverter.byteToHexString(data);
		} catch (Exception e) {
			System.err.println("hashString fatal error. str:" + str);
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
	}
	

	/**
	 * 
	 * @param str
	 * 				str in plain string
	 * @return HEX string
	 */
	public static String hashStringFromHex(String hex_str) {
		try {
			//System.err.println(str);
			byte[] data = DataIO.toBytesFromHex(hex_str);
			synchronized (lock) {
				HASHER.update(data);
				data = HASHER.digest();
			}
			return DataIO.toHexFromBytes(data);
			//return EncodeConverter.byteToHexString(data);
		} catch (Exception e) {
			System.err.println("hashString fatal error. str:" + hex_str);
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
	}
	

	/**
	 * hash them together with | separated
	 * @param components String[]
	 * @return String
	 * */
	public static String computeGeneralHashValue(String[] components){
		StringBuffer str = new StringBuffer();
		if(components != null){
			for(int i = 0; i < components.length ; i++){
				if(i != 0)str.append("|");
				str.append(components[i]);
			}
		}
		return hashString(str.toString());
	}
}
