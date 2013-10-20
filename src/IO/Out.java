package IO;

public class Out {

	public static void printBytes(byte[] bytes){
		System.out.println("Length: " + bytes.length);
		for (int i = 0; i < bytes.length; i ++){
			System.out.print(bytes[i]);
			if(i == bytes.length - 1) System.out.println("");
			else System.out.print(", ");
		}
	}
	
}
