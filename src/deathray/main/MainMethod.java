package deathray.main;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import deathray.crypto.DeathRayCryptoProvider;

public class MainMethod {

	public MainMethod() {}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
		Logger.getLogger("deathray.provider").setLevel(Level.FINEST);
		Security.addProvider(new DeathRayCryptoProvider());
		
		Cipher c = Cipher.getInstance("ML-KEM", "DR");
		System.out.println(c.getAlgorithm());
	}

}
