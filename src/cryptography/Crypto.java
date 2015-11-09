package cryptography;

import dataManagement.*;
import sun.misc.*;
import java.io.IOException;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;

public class Crypto {
	
	public Crypto () {
		
	}
	
	public String randomDataKey(int security) {// USE THE DATAENTRY SECURITY KEY HERE!
		String output;
		SecureRandom r = new SecureRandom();

		if( security == 1) { //AES key gen
			byte[] secureDataKey = new byte[16]; //AES data key
			r.nextBytes(secureDataKey); 
			String secureOutput = new String(secureDataKey);  
			output = secureOutput;
		}
		else {  //3DES key gen
			byte[] dataKey = new byte [24]; //3DES data key
			r.nextBytes(dataKey); // 
			String normalOutput = new String(dataKey);
			output = normalOutput;
		}
		return output;
	}
	
	// We're using AES & 3DES encryption. They're symmetric (same key for encrypt/decrypt).
	public Key keyGen (User user, DataEntry data) {  
		//generates the key the algorithm uses from the one stored in the user.
		//DataEntries created by the User will be given the key that the user has. 
		 //USER KEY GOES INTO THE SECRET KEY SPEC!
		Key key;
		if( data.isHighSecurity() == 1) { //AES key gen
		 key = new SecretKeySpec(data.getEncryptionKey().getBytes(), "AES");
		}
		else{ //3DES key gen
			key = new SecretKeySpec(data.getEncryptionKey().getBytes(), "DESede");
		}
		return key;
	}
	
	public byte[] ivGen(User user, int security) { //Encryption was insecure, needed salt.
		String iv = user.getPasswordSalt();
		if(security == 1) { //AES
			char[] salt16 = new char[16];
			iv.getChars(0, 16, salt16, 0);
			return new String(salt16).getBytes();
		}
		
		else { //3DES
			char[] salt8 = new char[8];
			iv.getChars(0, 8, salt8, 0);
			return new String(salt8).getBytes();		
		 }
	}
	
	public DataEntry encrypt(User user, DataEntry data) { //TODO figure out where the high security is being retrieved from.
				
		try {
			Cipher c;
			IvParameterSpec iv = new IvParameterSpec(ivGen(user,data.isHighSecurity()));
			List<String> dataList = new ArrayList<String>();
			Key key = keyGen(user,data); // makes a key from the user's data key.
			
			if(data.isHighSecurity() == 1) { //AES encryption
				 c = Cipher.getInstance("AES/CBC/PKCS5Padding");
				c.init(Cipher.ENCRYPT_MODE, key, iv); //make a cipher.
			}
			
			else { //3DES encryption
				 c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
				c.init(Cipher.ENCRYPT_MODE, key, iv);
				
			}
			for(String entry : data.getFieldDataList()){	//NOTE: If there is a null entry in this list, you WILL get a NullPointerException
				byte[] encrypted = c.doFinal(entry.getBytes()); //loops through the list, encrypting as it goes
				BASE64Encoder k = new BASE64Encoder();
				String encryptedData = k.encode(encrypted);
				dataList.add(encryptedData);//adds the encrypted data to the temp list
			}
			data.setDataFields(dataList); //sets temp list into the dataEntry.
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public DataEntry decrypt(User user, DataEntry data) { //Return type is temporary
		try {
			Key key = keyGen(user,data);
			Cipher c;
			IvParameterSpec iv = new IvParameterSpec(ivGen(user,data.isHighSecurity()));
			List<String> dataList = new ArrayList<String>();
			
			if(data.isHighSecurity()==1) { //AES encryption decrypt
				c = Cipher.getInstance("AES/CBC/PKCS5Padding");
				c.init(Cipher.DECRYPT_MODE, key, iv);
			}
			
			else { //3DES encryption decrypt
				 c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
				c.init(Cipher.DECRYPT_MODE, key,iv);
			}
			for(String entry : data.getFieldDataList()){	//NOTE: If there is a null entry in this list, you WILL get a NullPointerException
				BASE64Decoder k = new BASE64Decoder();
				byte[] decryptedBytes = k.decodeBuffer(entry);
				byte[] decryptedData = c.doFinal(decryptedBytes);
				String output = new String(decryptedData);
				dataList.add(output);
			}
			data.setDataFields(dataList);
			
		} catch (Exception e ) {
			e.printStackTrace();
		}
		return data;
	}
}
