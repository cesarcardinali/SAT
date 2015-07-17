package Supportive;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class Encryptation {

	/*public static void main(String[] args) {
		
		try {
			byte[] keyBytes = "1234567890azertyuiopqsdf".getBytes("ASCII");
			String pass = "testefdsafdsaf";
			
			DESedeKeySpec keySpec = new DESedeKeySpec(keyBytes);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
			SecretKey key = factory.generateSecret(keySpec);
			byte[] text = pass.getBytes("ASCII");

			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encrypted = cipher.doFinal(text);
			
			
			File f = new File("encrypt.txt");
			BufferedOutputStream out;
			try {
				out = new BufferedOutputStream(new FileOutputStream(f));
				out.write(encrypted);
				out.close();
				System.out.println("File saved ; )" + encrypted.length);
			} catch(Exception e){
				e.printStackTrace();
			}
			
			
			f = new File("encrypt.txt");
			byte[] toDecrypt = new byte[8];
			BufferedInputStream in;
			try {
				in = new BufferedInputStream(new FileInputStream(f));
				in.read(toDecrypt);
				in.close();
				System.out.println("File saved ; )");
			} catch(Exception e){
				e.printStackTrace();
			}
			

			cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decrypted = cipher.doFinal(toDecrypt);
			
			
			
			
			//System.out.println(new String(decrypted, "ASCII"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}*/
	
	
	public static byte[] encrypt(String pass) throws InvalidKeyException, UnsupportedEncodingException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		byte[] keyBytes = "1234567890azertyuiopqsdf".getBytes("ASCII");
		
		DESedeKeySpec keySpec = new DESedeKeySpec(keyBytes);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
		SecretKey key = factory.generateSecret(keySpec);
		byte[] text = pass.getBytes("ASCII");

		Cipher cipher = Cipher.getInstance("DESede");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encrypted = cipher.doFinal(text);
		
		return encrypted;
	}
	
	public static String decrypt(byte[] toDecrypt) throws Exception{
		byte[] keyBytes = "1234567890azertyuiopqsdf".getBytes("ASCII");
		DESedeKeySpec keySpec = new DESedeKeySpec(keyBytes);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
		SecretKey key = factory.generateSecret(keySpec);
		
		Cipher cipher = Cipher.getInstance("DESede");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decrypted = cipher.doFinal(toDecrypt);
		
		return new String(decrypted, "ASCII");
	}
}
