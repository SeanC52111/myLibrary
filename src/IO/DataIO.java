package IO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;


public class DataIO {

	public static String defaultCharSet = "ISO-8859-1";
	
	/**
	 * Common prefix of two strings
	 * @param a
	 * @param b
	 * @return
	 */
	public static String commonPrefix(String a, String b) {
		int len = Math.min(a.length(), b.length());
		for (int i = 0; i < len; i ++) {
			if (a.charAt(i) != b.charAt(i)) return a.substring(0, i);
		}
		return a.substring(0, len);
	}
	
	/**
	 * The most naive implementation of common prefix of String arrays, from start to end (including).
	 * Other more efficient methods may be tire, etc.
	 * @param strs
	 * @param start
	 * @param end
	 * @return
	 */
	public static String commonPrefix(String[] strs, int start, int end) {
		String ans = strs[start];
		for (int i = start + 1; i <= end; i ++) {
			ans = commonPrefix(ans, strs[i]);
		}
		return ans;
	}
	
	public static byte[] padding(byte[] data, int bytesLength) {
		byte[] newData = null;
		if (data.length == bytesLength) {
			return data;
		} else if (data.length < bytesLength) {
			newData = new byte[bytesLength];
			for (int i = 0; i < bytesLength; i ++) {
				if (i < data.length) {
					newData[bytesLength - i - 1] = data[data.length - i - 1];
				}else{
					newData[bytesLength - i - 1] = 0;
				}
			}
		} else {
			throw new IllegalStateException("The data length is larger than " + bytesLength + " bytes");
		}
		return newData;
	}
	
	public static String toHexFromString(String txt) {
        return toHexFromBytes(txt.getBytes());
    }
	
    public static String toStringfromHex(String hex) {
        return new String(toBytesFromHex(hex));
    }

    public static byte[] toBytesFromHex(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }

    public static String toHexFromBytes(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }
    
    private final static String HEX = "0123456789abcdef";
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    }
    
	public static int[] readIntArrays(DataInputStream dis){
		int len;
		int[] a = null;
		try {
			len = dis.readInt();
			a = new int[len];
			for(int i = 0; i < len; i++){
				a[i] = dis.readInt();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}
	
	public static void writeIntArrays(DataOutputStream dos, int[] a){
		try {
			dos.writeInt(a.length);
			for(int v : a){
				dos.writeInt(v);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeIntArrays(DataOutputStream dos, Integer[] a){
		try {
			dos.writeInt(a.length);
			for(int v : a){
				dos.writeInt(v);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeBigInteger(DataOutputStream dos, BigInteger b){
		try {
			if (b == null) {
				dos.writeInt(0);
				return;
			}
			writeBytes(dos, b.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static BigInteger readBigInteger(DataInputStream dis){
		int len;
		byte[] data = null;
		try {
			len = dis.readInt();
			data = new byte[len];
			dis.read(data, 0, len);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BigInteger(data);
	}
	
	public static void writeString(DataOutputStream dos, String str, String charset){
		try {
			if(str == null || str.length() == 0){
				dos.writeInt(0);
				return;
			}
			writeBytes(dos, str.getBytes(charset));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	public static void writeString(DataOutputStream dos, String str){
		try {
			if(str == null || str.length() == 0){
				dos.writeInt(0);
				return;
			}
			writeBytes(dos, str.getBytes(defaultCharSet));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	public static String readString(DataInputStream dis){
		int len;
		byte[] data;
		try {
			data = readBytes(dis);
			return new String(data, defaultCharSet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean compareString(String str1, String str2, String charset){
		byte[] data1 = null;
		try {
			data1 = str1.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data2 = null;
		try {
			data2 = str2.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(data1.length != data2.length)return false;
		for(int i = 0; i < data1.length; i++){
			if(data1[i] != data2[i])return false;
		}
		return true;
	}
	
	public static boolean compareString(String str1, String str2){
		byte[] data1 = null;
		try {
			data1 = str1.getBytes(defaultCharSet);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data2 = null;
		try {
			data2 = str2.getBytes(defaultCharSet);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(data1.length != data2.length)return false;
		for(int i = 0; i < data1.length; i++){
			if(data1[i] != data2[i])return false;
		}
		return true;
	}
	
	public static void writeBytes(DataOutputStream dos, byte[] data) {
		try {
			if (data == null) {
				dos.writeInt(0);
				return;
			}
			dos.writeInt(data.length);
			dos.write(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static byte[] readBytes(DataInputStream dis) {
		byte[] data = null;
		try {
			data = new byte[dis.readInt()];
			dis.read(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public static void main(String[] args){
		System.out.println(toHexFromString("ab").length());
		Out.printBytes(toBytesFromHex(toHexFromString("ab")));
//		String str1 = "????pY?@????	o????0";
//		try {
//			if(compareString(str1, new String(str1.getBytes("ISO-8859-1"), "ISO-8859-1"), "ISO-8859-1")){
//				System.out.println("right!");
//			}else{
//				System.err.println("error!");
//			}
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
