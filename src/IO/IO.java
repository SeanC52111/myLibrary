package io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import sun.awt.image.ByteArrayImageSource;
import io.Out;


public class IO {

	public static String defaultCharSet = "ISO-8859-1";
	
	/**
	 * Concatenate two byte arrays.
	 * @param A
	 * @param B
	 * @return
	 */
    public static byte[] concat(final byte[] ... strs) {
    	int len = 0;
    	for (int i = 0; i < strs.length; i ++) {
    		if (strs[i] != null) len += strs[i].length;
    	}
    	final byte[] result = new byte[len];
    	for (int i = 0, p = 0; i < strs.length; i ++) {
    		if (strs[i] != null) {
    			System.arraycopy(strs[i], 0, result, p, strs[i].length);
    			p += strs[i].length;
    		}
    	}
        return result;
    }
	
	public static String getIndent (int level) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < level; i ++) {
			sb.append("  ");
		}
		return sb.toString();
	}
	
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
    
    public static int readInt(DataInputStream ds) {
    	try {
			return ds.readInt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return 0;
    }
    
    public static long readLong(DataInputStream ds) {
    	try {
			return ds.readLong();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return 0;
    }
    
	public static int[] readIntArrays(DataInputStream ds){
		int len;
		int[] a = null;
		try {
			len = ds.readInt();
			a = new int[len];
			for(int i = 0; i < len; i++){
				a[i] = ds.readInt();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}
	
	public static void writeInt(DataOutputStream ds, int a) {
		try {
			ds.writeInt(a);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeLong(DataOutputStream ds, long a) {
		try {
			ds.writeLong(a);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeIntArrays(DataOutputStream ds, int[] a){
		try {
			ds.writeInt(a.length);
			for(int v : a){
				ds.writeInt(v);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeIntArrays(DataOutputStream ds, Integer[] a){
		try {
			ds.writeInt(a.length);
			for(int v : a){
				ds.writeInt(v);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeBigInteger(DataOutputStream ds, BigInteger b){
		try {
			if (b == null) {
				ds.writeInt(0);
				return;
			}
			writeBytes(ds, b.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static BigInteger readBigInteger(DataInputStream ds){
		int len;
		byte[] data = null;
		try {
			len = ds.readInt();
			data = new byte[len];
			ds.read(data, 0, len);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BigInteger(data);
	}
	
	public static void writeString(DataOutputStream ds, String str, String charset){
		try {
			if(str == null || str.length() == 0){
				ds.writeInt(0);
				return;
			}
			writeBytes(ds, str.getBytes(charset));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	public static void writeString(DataOutputStream ds, String str){
		try {
			if(str == null || str.length() == 0){
				ds.writeInt(0);
				return;
			}
			writeBytes(ds, str.getBytes(defaultCharSet));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	public static String readString(DataInputStream ds){
		byte[] data;
		try {
			data = readBytes(ds);
			return new String(data, defaultCharSet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean compareBytes(byte[] data1, byte[] data2) {
		if(data1.length != data2.length)return false;
		for(int i = 0; i < data1.length; i++){
			if(data1[i] != data2[i])return false;
		}
		return true;
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
		return compareBytes(data1, data2);
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
		return compareBytes(data1, data2);
	}
	
	public static byte[] toBytes(RW rw) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		DataOutputStream ds = new DataOutputStream(new BufferedOutputStream(bs));
		rw.write(ds);
		return bs.toByteArray();
	}
	
	public static void loadBytes(RW rw, byte[] data) {
		DataInputStream ds = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(data)));
		rw.read(ds);
	}
	
	public static void writeBytes(DataOutputStream ds, byte[] data) {
		try {
			if (data == null) {
				ds.writeInt(0);
				return;
			}
			ds.writeInt(data.length);
			ds.write(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static byte[] readBytes(DataInputStream ds) {
		byte[] data = null;
		try {
			data = new byte[ds.readInt()];
			ds.read(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public static byte[] readALLBytes(DataInputStream ds) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = ds.read(buf)) != -1;) {
                bs.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
            }
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
        return bs.toByteArray();
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
		byte[]  str1 = "abc".getBytes();
		byte[]  str2 = "def".getBytes();
		System.out.println(toHexFromBytes(str1));
		System.out.println(toHexFromBytes(str2));
		System.out.println(toHexFromBytes(concat(str1, str2)));
	}
}
