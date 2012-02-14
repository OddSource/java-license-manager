/*
 * LicenseManager.java from LicenseManager modified Monday, February 13, 2012 23:39:59 CST (-0600).
 *
 * Copyright 2010-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.nicholaswilliams.java.licensing;

import net.nicholaswilliams.java.licensing.encryption.Encryptor;
import net.nicholaswilliams.java.licensing.encryption.KeyFileUtilities;
import net.nicholaswilliams.java.licensing.encryption.KeyPasswordProvider;
import net.nicholaswilliams.java.licensing.encryption.PublicKeyDataProvider;
import net.nicholaswilliams.java.licensing.exception.AlgorithmNotSupportedException;
import net.nicholaswilliams.java.licensing.exception.CorruptSignatureException;
import net.nicholaswilliams.java.licensing.exception.FailedToDecryptException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeyException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeySpecificationException;
import net.nicholaswilliams.java.licensing.exception.InsecureEnvironmentException;
import net.nicholaswilliams.java.licensing.exception.InvalidSignatureException;
import net.nicholaswilliams.java.licensing.exception.KeyNotFoundException;
import net.nicholaswilliams.java.licensing.exception.ObjectDeserializationException;
import net.nicholaswilliams.java.licensing.exception.ObjectTypeNotExpectedException;

import java.lang.reflect.AnnotatedElement;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * This class manages licenses in the client application. All interaction with the license manager done from the client
 * application should go through here. The license manager is first initialized by calling
 * {@link #createInstance(LicenseProvider, KeyPasswordProvider, PublicKeyDataProvider, LicenseValidator, int)}, which
 * also returns the singleton instance created. Then all future instances of the license manager are obtained by
 * calling {@link #getInstance()}.<br />
 * <br />
 * The license manager maintains a cache of license objects, which cannot be disabled entirely. When initializing the
 * license manager, a maximum cache object age is specified in minutes. If any value less than 1 minute is specified,
 * then the maximum cache object age is set to 10 seconds by default. The advantage of using a longer cache age is
 * increased client application performance, especially with multi-tenant SaaS applications with high load. The
 * disadvantage is decreased security, although that security concern is almost completely mitigated by the presence of
 * the {@link LicenseSecurityManager}.<br />
 * <br />
 * This security manager is one of the most integral pieces to the license manager. It prevents reflection attacks from
 * disabling or compromising the security features in this product. It is instantiated when {@code createInstance} is
 * called and cannot be disabled. For more information on how it works, see the JavaDoc for the
 * {@link LicenseSecurityManager}.
 * 
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 * @see LicenseSecurityManager
 * @see InsecureEnvironmentException
 */
public final class LicenseManager
{
	private static LicenseManager instance = null;

	private final KeyPasswordProvider passwordProvider;

	private final PublicKeyDataProvider publicKeyDataProvider;

	private final LicenseProvider licenseProvider;

	private final LicenseValidator licenseValidator;

	private final int cacheTimeInMilliseconds;

	private final Hashtable<Object, LicenseCacheEntry> licenseCache = new Hashtable<Object, LicenseCacheEntry>();

	private LicenseManager(LicenseProvider licenseProvider, KeyPasswordProvider passwordProvider,
						   PublicKeyDataProvider publicKeyDataProvider, LicenseValidator licenseValidator,
						   int cacheTimeInMinutes)
	{
		if(licenseProvider == null)
			throw new IllegalArgumentException("Parameter licenseProvider must not be null.");

		if(passwordProvider == null)
			throw new IllegalArgumentException("Parameter passwordProvider must not be null.");

		if(publicKeyDataProvider == null)
			throw new IllegalArgumentException("Parameter publicKeyDataProvider must not be null.");

		// install the security manager
		try
		{
			Class.forName("net.nicholaswilliams.java.licensing.LicenseSecurityManager");
		}
		catch(ClassNotFoundException e)
		{
			throw new InsecureEnvironmentException("The class net.nicholaswilliams.java.licensing.LicenseSecurityManager could not be initialized.", e);
		}

		this.licenseProvider = licenseProvider;
		this.passwordProvider = passwordProvider;
		this.publicKeyDataProvider = publicKeyDataProvider;
		this.licenseValidator = licenseValidator;
		this.cacheTimeInMilliseconds = cacheTimeInMinutes < 1 ? ( 10 * 1000 ) : ( cacheTimeInMinutes * 60 * 1000 );
	}

	/**
	 * The first time this is called, it creates and returns a license manager with the given providers and cache
	 * time. All subsequent calls are equivalent to calling {@link #getInstance()} (i.e., the parameters are ignored
	 * and the previously-created instance is returned).
	 *
	 * @param licenseProvider The provider of the persisted license(s)
	 * @param passwordProvider The provider of the password for decrypting the license key
	 * @param publicKeyDataProvider The provider of the data for the public key companion to the private key used to sign the license object
	 * @param licenseValidator The validator implementation that validates all licenses; if null, licenses are assumed to always be valid
	 * @param cacheTimeInMinutes The length of time in minutes to cache license information (for performance reasons, anything less than 1 minute results in a 10-second cache life; the cache cannot be disabled completely)
	 * @return the created instance of the license manager.
	 * @throws IllegalArgumentException if {@code licenseProvider}, {@code passwordProvider} or {@code publicKeyDataProvider} are null
	 * @throws InsecureEnvironmentException if the {@link LicenseSecurityManager} cannot be instantiated
	 * @see LicenseSecurityManager for more information on the security features that protect the license manager
	 */
	public static synchronized LicenseManager createInstance(LicenseProvider licenseProvider,
															 KeyPasswordProvider passwordProvider,
															 PublicKeyDataProvider publicKeyDataProvider,
															 LicenseValidator licenseValidator,
															 int cacheTimeInMinutes)
	{
		if(LicenseManager.instance == null)
		{
			LicenseManager.instance = new LicenseManager(
					licenseProvider,
					passwordProvider,
					publicKeyDataProvider,
					licenseValidator,
					cacheTimeInMinutes
			);
		}

		return LicenseManager.instance;
	}

	/**
	 * Returns the license manager instance previously created by
	 * {@link #createInstance(LicenseProvider, KeyPasswordProvider, PublicKeyDataProvider, LicenseValidator, int)}. If this method is
	 * called before {@code createInstance()}, a {@link RuntimeException} is thrown.
	 *
	 * @return the license manager instance.
	 * @throws RuntimeException if no instance has yet be created by {@code createInstance()}.
	 */
	public static synchronized LicenseManager getInstance()
	{
		if(LicenseManager.instance == null)
			throw new RuntimeException("The LicenseManager instance has not been created yet. Please create it with createInstance().");

		return LicenseManager.instance;
	}

	/**
	 * This method calls {@link LicenseValidator#validateLicense(License)} on the validator that was provided, if one
	 * was provided.
	 *
	 * @param license The license to validate
	 * @throws net.nicholaswilliams.java.licensing.exception.InvalidLicenseException when the license is invalid for any reason
	 */
	public final void validateLicense(License license)
	{
		if(this.licenseValidator != null)
			this.licenseValidator.validateLicense(license);
	}

	/**
	 * Checks whether the license assigned to the specified context is licensed to use all of the features specified.<br />
	 * <br />
	 * Throws the same exceptions as {@link #getLicense(Object)} and for the same reasons.
	 *
	 * @param context The context (account, client, etc.) for which to check the feature(s) against its license
	 * @param feature The feature (or features) to check against the license
	 * @return {@code true} if the license exists and has this feature enabled, {@code false} otherwise.
	 */
	public final boolean hasLicenseForAllFeatures(Object context, String... feature)
	{
		License license = this.getLicense(context);
		if(license == null)
			return false;

		this.validateLicense(license);

		return license.hasLicenseForAllFeatures(feature);
	}

	/**
	 * Checks whether the license assigned to the specified context is licensed to use any of the features specified.<br />
	 * <br />
	 * Throws the same exceptions as {@link #getLicense(Object)} and for the same reasons.
	 *
	 * @param context The context (account, client, etc.) for which to check the feature(s) against its license
	 * @param feature The feature (or features) to check against the license
	 * @return {@code true} if the license exists and has this feature enabled, {@code false} otherwise.
	 */
	public final boolean hasLicenseForAnyFeature(Object context, String... feature)
	{
		License license = this.getLicense(context);
		if(license == null)
			return false;

		this.validateLicense(license);

		return license.hasLicenseForAnyFeature(feature);
	}

	/**
	 * Checks whether the license assigned to the specified context is licensed to use the feature(s) in the annotation value.<br />
	 * <br />
	 * Throws the same exceptions as {@link #getLicense(Object)} and for the same reasons.
	 *
	 * @param context The context (account, client, etc.) for which to check the feature(s) against its license
	 * @param annotation The annotation object whose value(s) is(are) the feature(s) to check against the license
	 * @return {@code true} if the license exists and has this feature(s) enabled, {@code false} otherwise.
	 */
	public final boolean hasLicenseForFeatures(Object context, FeatureRestriction annotation)
	{
		License license = this.getLicense(context);
		if(license == null)
			return false;

		this.validateLicense(license);

		return annotation.operand() == FeatureRestrictionOperand.AND ?
				license.hasLicenseForAllFeatures(annotation.value()) :
				license.hasLicenseForAnyFeature(annotation.value());
	}

	/**
	 * Checks whether the license assigned to the specified context is licensed to use the feature(s) in the
	 * {@link FeatureRestriction} annotation value, if the target is annotated with that annotation.<br />
	 * <br />
	 * Throws the same exceptions as {@link #getLicense(Object)} and for the same reasons.
	 *
	 * @param context The context (account, client, etc.) for which to check the feature(s) against its license
	 * @param target The target (a package reflection object, class reflection object or method reflection object) to check for the {@link FeatureRestriction} annotation and check its value against the license
	 * @return {@code false} if the license does not exist, {@code true} if the target is not annotated with {@link FeatureRestriction} and, if it is annotated, {@code true} if the feature(s) is(are) licensed or {@code false} if the feature(s) is(are) not licensed
	 */
	public final boolean hasLicenseForFeatures(Object context, AnnotatedElement target)
	{
		License license = this.getLicense(context);
		if(license == null)
			return false;

		this.validateLicense(license);
		
		FeatureRestriction annotation = target.getAnnotation(FeatureRestriction.class);

		return annotation == null || (
				annotation.operand() == FeatureRestrictionOperand.AND ?
					license.hasLicenseForAllFeatures(annotation.value()) :
					license.hasLicenseForAnyFeature(annotation.value())
		);
	}

	/**
	 * If the license has already been cached for the specified context (account, client, etc.) and the cache has not
	 * become stale (its age has not surpassed the cache time limitation configured for this manager), this returns the
	 * cached license. If it has not been cached or the cache is stale, this retrieves the license from the store,
	 * decrypts it, deserializes it, checks its signature and, if everything is kosher, caches and returns the
	 * license.<br />
	 * <br />
	 * This method takes precautions to ensure that the cache is not tampered with using reflection. However, it is not
	 * infallible. For extra security, one could configure this manager with a 10-second cache (zero cache time limit
	 * in minutes), but we highly recommend implementing caching in the license data provider: the signature checking
	 * process is time consuming (on the order of hundreds of milliseconds, and could happen multiple times per action)
	 * and the added overhead of retrieving the license from the store every time could bring an application to
	 * its knees.
	 *
	 * @param context The context (account, client, etc.) for which to retrieve the license object
	 * @return the requested license object, or null if none exists.
	 * @throws KeyNotFoundException if the public key data could not be found.
	 * @throws AlgorithmNotSupportedException if the encryption algorithm is not supported.
	 * @throws InappropriateKeySpecificationException if an inappropriate key specification is provided.
	 * @throws InappropriateKeyException if the key type and cipher type do not match.
	 * @throws CorruptSignatureException if the signature data has been corrupted (most likely tampered with).
	 * @throws InvalidSignatureException if the signature is invalid (most likely tampered with).
	 * @throws FailedToDecryptException if the license or signature could not be decrypted.
	 * @throws ObjectTypeNotExpectedException if the license data was tampered with.
	 * @throws ObjectDeserializationException if the license data was tampered with.
	 */
	public final License getLicense(Object context) throws KeyNotFoundException, AlgorithmNotSupportedException,
														   InappropriateKeySpecificationException,
														   InappropriateKeyException, CorruptSignatureException,
														   InvalidSignatureException, FailedToDecryptException,
														   ObjectDeserializationException
	{
		long time = System.currentTimeMillis();

		synchronized(this.licenseCache)
		{
			LicenseCacheEntry entry = this.licenseCache.get(context);
			
			if(entry != null && entry.license != null)
			{
				if(entry.expires <= time)
				{
					entry = null;
					this.licenseCache.remove(context);
				}
			}

			if(entry == null || entry.license == null)
			{
				SignedLicense signed = this.licenseProvider.getLicense(context);
				if(signed == null)
					return null;

				PublicKey key;
				{
					char[] password = this.passwordProvider.getKeyPassword();
					byte[] keyData = this.publicKeyDataProvider.getEncryptedPublicKeyData();

					key = KeyFileUtilities.readEncryptedPublicKey(keyData, password);

					Arrays.fill(password, '\u0000');
					Arrays.fill(keyData, (byte)0);
				}

				License license;
				{
					byte[] unencrypted;
					{
						byte[] signature = signed.getSignatureContent();
						byte[] encrypted = signed.getLicenseContent();
						signed.erase();

						new DataSignatureManager().verifySignature(key, encrypted, signature);

						unencrypted = Encryptor.decryptRaw(encrypted);
						
						Arrays.fill(signature, (byte)0);
						Arrays.fill(encrypted, (byte)0);
					}

					license = License.deserialize(unencrypted);
					
					Arrays.fill(unencrypted, (byte)0);
				}
				
				long expires = time + this.cacheTimeInMilliseconds;

				entry = new LicenseCacheEntry(license, expires);
				
				this.licenseCache.put(context, entry);
			}

			return entry.license;
		}
	}

	/**
	 * An entry in the memory-based license cache.
	 */
	private final static class LicenseCacheEntry
	{
		private final License license;

		private final long expires;

		public LicenseCacheEntry(License license, long expires)
		{
			this.license = license;
			this.expires = expires;
		}
	}
}
