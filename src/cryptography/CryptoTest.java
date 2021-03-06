package cryptography;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import dataManagement.DataEntry;
import dataManagement.User;

public class CryptoTest {

	public CryptoTest() { 
		
		
	}
	public static void main(String[] args) { 
		Crypto test = new Crypto();
		int secLvl = 1;
		DataEntry testData = new DataEntry(null, null, null, null, secLvl, null);
		User u = new User(null, null, null, null, null, null, null);
		try{
		SaltGenerator twitch = new SaltGenerator();
		
			u.setPasswordSalt(twitch.generateSalt());
			u.setDefaultHighSecurity(secLvl);
		} catch (NoSuchAlgorithmException e1) {
		
			e1.printStackTrace();
		} 
 		try {
			testData.setEncryptionKey(test.randomDataKey(testData.isHighSecurity()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		System.out.println(testData.getEncryptionKey()+ "\n");
		
		//testData.addDataField("\n");
		//testData.addDataField("This is SPARTA.");
		//testData.addDataField("This too is a test.");
		//testData.addDataField("TESTING!");
		testData.addDataField("ASDFASDLKJFALSDKJF!#@!#!@##@LRKAJSFLIAJ[]);;;''");
		//testData.addDataField("You may have a mild case of... er.. severe brain damage.");
		
			test.encrypt(u, testData);
			String encrypted = testData.getFieldDataList().get(0);
			System.out.println(encrypted+"\n");
			/*test.decrypt(u, testData);
			System.out.println(testData.getFieldDataList());*/
		String decrypted = test.shareDecrypt(secLvl, u.getPasswordSalt(), testData.getEncryptionKey(), encrypted);
		System.out.println(decrypted+"\n");
		
	}
	
	
}
