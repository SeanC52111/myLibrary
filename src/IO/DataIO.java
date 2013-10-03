package IO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;


public class DataIO {

	public static String defaultCharSet = "ISO-8859-1";
	
	
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
		byte[] data = b.toByteArray();
		try {
			dos.writeInt(data.length);
			dos.write(data);
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
			}
			dos.writeInt(str.length());
			dos.write(str.getBytes(charset));
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
			dos.writeInt(str.length());
			dos.write(str.getBytes(defaultCharSet));
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
			len = dis.readInt();
			if(len == 0)return null;
			data = new byte[len];
			dis.read(data);
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
