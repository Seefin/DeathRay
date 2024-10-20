/**
 * MIT License
 *
 * Copyright (c) 2024 Connor Iain Te Ahu Findlay &lt;code@findlays.io&gt;
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * CHANGE LOG
 * ==========
 * 2024, Connor F: Created Class
 */
package deathray.crypto;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.util.HashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import deathray.crypto.ciphers.CrystalsKhyberCipher;
import sun.misc.Unsafe;

/**
 * Map DeathRay Crypto to JCA/JCE classes.
 * <p>
 * The {@link DeathRayCryptoProvider} maps the DeathRay implementations of 
 * cryptographic primatives to their corresponding classes in the JCA/JCE API as
 * appropriate.
 * 
 * @author Connor F
 * @see Provider
 */
@SuppressWarnings("deprecation")
public class DeathRayCryptoProvider extends Provider {
	private static final long serialVersionUID = 3225688089213529980L;

	private static final String NAME = "DR";
	private static final double VERSION = 1.0;
	private static final String INFO = "DeathRay Crypto Service Provider";
	
	/**
	 * Create a new Provider
	 * <p>
	 * We hide this from public access, and use the defined constants to create
	 * the class. 
	 * 
	 * @param name        The name of this provider, used to specify it in the JCE
	 * @param versionStr  The version of the provider
	 * @param info        Short information string
	 */
	@SuppressWarnings({ "removal" })
	private DeathRayCryptoProvider(String name, double versionStr, String info) {
		super(name, versionStr, info);
		/* In a context where the Security Manager is awake and enforcing restrictions,
         * this will allow us to install the custom implementations, provided the
         * access rules allow it.
         */
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
        	registerCiphers();
        	return null;
        });

	}
	
	private void registerCiphers() {
		put("Cipher.ML-KEM", CrystalsKhyberCipher.class.getName());
		put("Alg.Alias.Cipher.KHYBER", "ML-KEM");
	}

	/**
	 * Create a new {@link DeathRayCryptoProvider}
	 * <p>
	 * Registers the DeathRay cryptographic services with the JVM, and allows users
	 * to call them.
	 * <p>
	 * This class also patches the JVM (unless stopped by a {@link SecurityManager}) to
	 * allow this provider (and only this provider) to provide restricted services.
	 * Restricted services are those that were previously under export control, and which
	 * are still prohibited in some versions of the JVM.
	 * <p>
	 * To disable JVM patching, set system property "DeathRay.noPatchJvm" to true before
	 * instantiating this class.
	 */
	public DeathRayCryptoProvider() {
		this(NAME,VERSION,INFO);
		if (!Boolean.getBoolean("DeathRay.noPatchJvm")) {
			Logger.getLogger("deathray.provider").log(Level.FINEST, "Patching JVM to allow non-signed DeathRayProvider class to run");
			patchJVM();
		}
	}

	/**
	 * Allow this class to register restricted services.
	 * <p>
	 * For historical reasons, mostly relating to the ITAR and AECA legislation in
	 * the US, providers are not allowed to implement certain services, unless
	 * they are signed by an Oracle code signing certificate. Some JVMs (such as
	 * OpenJDK) no longer require this. I've kept this code here, as I usually add
	 * it to all Crypto Code I write, in case I run it on an older, or propritary
	 * JVM at some point.  
	 */
	private void patchJVM() {
		try {
		final Class<?> ourClass = this.getClass();
		Class<?> jceSecurityClass = Class.forName("javax.crypto.JceSecurity");
		Field field = jceSecurityClass.getDeclaredField("verificationResults");
		field.setAccessible(true);

		Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
		unsafeField.setAccessible(true);
		Unsafe unsafe = (Unsafe) unsafeField.get(null);

		// This is not the provider you are looking for, you don't need to see its identification, move along
		unsafe.putObject(unsafe.staticFieldBase(field), unsafe.staticFieldOffset(field), new HashMap<Object, Boolean>() {
		    private static final long serialVersionUID = -7298727080210769710L;

			@Override
		    public Boolean get(Object key) {              
		        return ourClass.isInstance(key) ? Boolean.TRUE : Boolean.FALSE;
		    }

		    @Override
		    public Boolean computeIfAbsent(Object key, Function<? super Object, ? extends Boolean> mappingFunction) {
		        return super.computeIfAbsent(key, object -> Boolean.TRUE);
		    }
		});
		} catch (ClassNotFoundException | NoSuchFieldException | SecurityException |
				IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalStateException("Can't patch JVM signing out", e);
		}
	}

}
