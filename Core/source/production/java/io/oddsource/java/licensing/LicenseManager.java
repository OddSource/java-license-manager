/*
 * Copyright © 2010-2019 OddSource Code (license@oddsource.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.oddsource.java.licensing;

import java.lang.reflect.AnnotatedElement;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Hashtable;

import io.oddsource.java.licensing.encryption.Encryptor;
import io.oddsource.java.licensing.encryption.KeyFileUtilities;
import io.oddsource.java.licensing.encryption.PasswordProvider;
import io.oddsource.java.licensing.encryption.PublicKeyDataProvider;
import io.oddsource.java.licensing.exception.AlgorithmNotSupportedException;
import io.oddsource.java.licensing.exception.CorruptSignatureException;
import io.oddsource.java.licensing.exception.ExpiredLicenseException;
import io.oddsource.java.licensing.exception.FailedToDecryptException;
import io.oddsource.java.licensing.exception.InappropriateKeyException;
import io.oddsource.java.licensing.exception.InappropriateKeySpecificationException;
import io.oddsource.java.licensing.exception.InsecureEnvironmentError;
import io.oddsource.java.licensing.exception.InvalidLicenseException;
import io.oddsource.java.licensing.exception.InvalidSignatureException;
import io.oddsource.java.licensing.exception.KeyNotFoundException;
import io.oddsource.java.licensing.exception.ObjectTypeNotExpectedException;

/**
 * This class manages licenses in the client application. All interaction with the license manager done from the client
 * application should go through here. Before getting the manager instance for the first time, relevant properties
 * should be set in {@link LicenseManagerProperties}. The values in this class will be used to instantiate the license
 * manager. After setting all the necessary properties there, one can retrieve an instance using
 * {@link #getInstance()}. Be sure to set all the properties first; once {@link #getInstance()} is called for the first
 * time, any changes to {@link LicenseManagerProperties} will be ignored.<br />
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
 * @see LicenseSecurityManager
 * @see InsecureEnvironmentError
 * @author Nick Williams
 * @version 1.0.2
 * @since 1.0.0
 */
public final class LicenseManager
{
    private static final int defaultCacheTimeInMillis = 10 * 1000;

    private static final int millisecondsPerMinute = 60 * 1000;

    private static LicenseManager instance;

    private final PublicKeyDataProvider publicKeyDataProvider;

    private final PasswordProvider publicKeyPasswordProvider;

    private final LicenseProvider licenseProvider;

    private final PasswordProvider licensePasswordProvider;

    private final LicenseValidator licenseValidator;

    private final int cacheTimeInMilliseconds;

    private final Hashtable<Object, LicenseCacheEntry> licenseCache = new Hashtable<>();

    private LicenseManager()
    {
        if(LicenseManagerProperties.getLicenseProvider() == null)
        {
            throw new IllegalArgumentException("Parameter licenseProvider must not be null.");
        }

        if(LicenseManagerProperties.getPublicKeyDataProvider() == null)
        {
            throw new IllegalArgumentException("Parameter publicKeyDataProvider must not be null.");
        }

        if(LicenseManagerProperties.getPublicKeyPasswordProvider() == null)
        {
            throw new IllegalArgumentException("Parameter publicKeyPasswordProvider must not be null.");
        }

        // install the security manager
        try
        {
            Class.forName("io.oddsource.java.licensing.LicenseSecurityManager");
        }
        catch(final ClassNotFoundException e)
        {
            throw new InsecureEnvironmentError("The class LicenseSecurityManager could not be initialized.", e);
        }

        final int cacheTimeInMinutes = LicenseManagerProperties.getCacheTimeInMinutes();

        this.publicKeyDataProvider = LicenseManagerProperties.getPublicKeyDataProvider();
        this.publicKeyPasswordProvider = LicenseManagerProperties.getPublicKeyPasswordProvider();
        this.licenseProvider = LicenseManagerProperties.getLicenseProvider();
        this.licensePasswordProvider = LicenseManagerProperties.getLicensePasswordProvider() == null ?
                                       LicenseManagerProperties.getPublicKeyPasswordProvider() :
                                       LicenseManagerProperties.getLicensePasswordProvider();
        this.licenseValidator = LicenseManagerProperties.getLicenseValidator();
        this.cacheTimeInMilliseconds = cacheTimeInMinutes < 1 ?
                                       LicenseManager.defaultCacheTimeInMillis :
                                       (cacheTimeInMinutes * LicenseManager.millisecondsPerMinute);
    }

    /**
     * Returns the license manager instance. Before this method can be called the first time, all of the parameters must
     * bet set in {@link LicenseManagerProperties}. See the documentation for that class for more details.
     *
     * @return the license manager instance.
     *
     * @throws IllegalArgumentException if {@link LicenseManagerProperties#setLicenseProvider(LicenseProvider)
     *     licenseProvider}, {@link LicenseManagerProperties#setPublicKeyPasswordProvider(PasswordProvider)
     *     publicKeyPasswordProvider} or {@link LicenseManagerProperties#setPublicKeyDataProvider(PublicKeyDataProvider)
     *     publicKeyDataProvider} are null.
     * @throws InsecureEnvironmentError if the {@link LicenseSecurityManager} cannot be instantiated
     * @see LicenseSecurityManager for more information on the security features that protect the license manager
     */
    public static synchronized LicenseManager getInstance()
    {
        if(LicenseManager.instance == null)
        {
            LicenseManager.instance = new LicenseManager();
        }

        return LicenseManager.instance;
    }

    /**
     * This method calls {@link LicenseValidator#validateLicense(License)} on the validator that was provided, if one
     * was provided.
     *
     * @param license The license to validate
     *
     * @throws InvalidLicenseException when the license is invalid for any reason.
     * @throws ExpiredLicenseException when the license is expired.
     */
    public final void validateLicense(final License license) throws InvalidLicenseException
    {
        if(this.licenseValidator != null)
        {
            this.licenseValidator.validateLicense(license);
        }
    }

    /**
     * Checks whether the license assigned to the specified context is licensed to use the feature specified.<br />
     * <br />
     * Throws the same exceptions as {@link #getLicense(Object)} and for the same reasons.
     *
     * @param context The context (account, client, etc.) for which to check the feature(s) against its license
     * @param featureName The feature (or features) to check against the license
     *
     * @return {@code true} if the license exists and has this feature enabled, {@code false} otherwise.
     *
     * @throws InvalidLicenseException when the license is invalid for any reason.
     * @throws ExpiredLicenseException when the license is expired.
     */
    public final boolean hasLicenseForFeature(final Object context, final String featureName)
        throws InvalidLicenseException
    {
        final License license = this.getValidatedLicenseOrNullIfNonExistent(context);
        if(license == null)
        {
            return false;
        }

        return license.hasLicenseForFeature(featureName);
    }

    /**
     * Checks whether the license assigned to the specified context is licensed to use the feature specified.<br />
     * <br />
     * Throws the same exceptions as {@link #getLicense(Object)} and for the same reasons.
     *
     * @param context The context (account, client, etc.) for which to check the feature(s) against its license
     * @param feature The feature (or features) to check against the license
     *
     * @return {@code true} if the license exists and has this feature enabled, {@code false} otherwise.
     *
     * @throws InvalidLicenseException when the license is invalid for any reason.
     * @throws ExpiredLicenseException when the license is expired.
     */
    public final boolean hasLicenseForFeature(final Object context, final FeatureObject feature)
        throws InvalidLicenseException
    {
        final License license = this.getValidatedLicenseOrNullIfNonExistent(context);
        if(license == null)
        {
            return false;
        }

        return license.hasLicenseForFeature(feature);
    }

    /**
     * Checks whether the license assigned to the specified context is licensed to use any of the features specified.<br
     * />
     * <br />
     * Throws the same exceptions as {@link #getLicense(Object)} and for the same reasons.
     *
     * @param context The context (account, client, etc.) for which to check the feature(s) against its license
     * @param featureNames The feature (or features) to check against the license
     *
     * @return {@code true} if the license exists and has this feature enabled, {@code false} otherwise.
     *
     * @throws InvalidLicenseException when the license is invalid for any reason.
     * @throws ExpiredLicenseException when the license is expired.
     */
    public final boolean hasLicenseForAnyFeature(final Object context, final String... featureNames)
        throws InvalidLicenseException
    {
        final License license = this.getValidatedLicenseOrNullIfNonExistent(context);
        if(license == null)
        {
            return false;
        }

        return license.hasLicenseForAnyFeature(featureNames);
    }

    /**
     * Checks whether the license assigned to the specified context is licensed to use any of the features specified.<br
     * />
     * <br />
     * Throws the same exceptions as {@link #getLicense(Object)} and for the same reasons.
     *
     * @param context The context (account, client, etc.) for which to check the feature(s) against its license
     * @param features The feature (or features) to check against the license
     *
     * @return {@code true} if the license exists and has this feature enabled, {@code false} otherwise.
     *
     * @throws InvalidLicenseException when the license is invalid for any reason.
     * @throws ExpiredLicenseException when the license is expired.
     */
    public final boolean hasLicenseForAnyFeature(final Object context, final FeatureObject... features)
        throws InvalidLicenseException
    {
        final License license = this.getValidatedLicenseOrNullIfNonExistent(context);
        if(license == null)
        {
            return false;
        }

        return license.hasLicenseForAnyFeature(features);
    }

    /**
     * Checks whether the license assigned to the specified context is licensed to use all of the features specified.<br
     * />
     * <br />
     * Throws the same exceptions as {@link #getLicense(Object)} and for the same reasons.
     *
     * @param context The context (account, client, etc.) for which to check the feature(s) against its license
     * @param featureNames The feature (or features) to check against the license
     *
     * @return {@code true} if the license exists and has this feature enabled, {@code false} otherwise.
     *
     * @throws InvalidLicenseException when the license is invalid for any reason.
     * @throws ExpiredLicenseException when the license is expired.
     */
    public final boolean hasLicenseForAllFeatures(final Object context, final String... featureNames)
        throws InvalidLicenseException
    {
        final License license = this.getValidatedLicenseOrNullIfNonExistent(context);
        if(license == null)
        {
            return false;
        }

        return license.hasLicenseForAllFeatures(featureNames);
    }

    /**
     * Checks whether the license assigned to the specified context is licensed to use all of the features specified.<br
     * />
     * <br />
     * Throws the same exceptions as {@link #getLicense(Object)} and for the same reasons.
     *
     * @param context The context (account, client, etc.) for which to check the feature(s) against its license
     * @param features The feature (or features) to check against the license
     *
     * @return {@code true} if the license exists and has this feature enabled, {@code false} otherwise.
     *
     * @throws InvalidLicenseException when the license is invalid for any reason.
     * @throws ExpiredLicenseException when the license is expired.
     */
    public final boolean hasLicenseForAllFeatures(final Object context, final FeatureObject... features)
        throws InvalidLicenseException
    {
        final License license = this.getValidatedLicenseOrNullIfNonExistent(context);
        if(license == null)
        {
            return false;
        }

        return license.hasLicenseForAllFeatures(features);
    }

    /**
     * Checks whether the license assigned to the specified context is licensed to use the feature(s) in the annotation
     * value.<br />
     * <br />
     * Throws the same exceptions as {@link #getLicense(Object)} and for the same reasons.
     *
     * @param context The context (account, client, etc.) for which to check the feature(s) against its license
     * @param annotation The annotation object whose value(s) is(are) the feature(s) to check against the license
     *
     * @return {@code true} if the license exists and has this feature(s) enabled, {@code false} otherwise.
     *
     * @throws InvalidLicenseException when the license is invalid for any reason.
     * @throws ExpiredLicenseException when the license is expired.
     */
    public final boolean hasLicenseForFeatures(final Object context, final FeatureRestriction annotation)
        throws InvalidLicenseException
    {
        final License license = this.getValidatedLicenseOrNullIfNonExistent(context);
        if(license == null)
        {
            return false;
        }

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
     * @param target The target (a package reflection object, class reflection object or method reflection object)
     *     to check for the {@link FeatureRestriction} annotation and check its value against the license
     *
     * @return {@code false} if the license does not exist, {@code true} if the target is not annotated with {@link
     *     FeatureRestriction} and, if it is annotated, {@code true} if the feature(s) is(are) licensed or {@code false}
     *     if the feature(s) is(are) not licensed
     *
     * @throws InvalidLicenseException when the license is invalid for any reason.
     * @throws ExpiredLicenseException when the license is expired.
     */
    public final boolean hasLicenseForFeatures(final Object context, final AnnotatedElement target)
        throws InvalidLicenseException
    {
        final License license = this.getValidatedLicenseOrNullIfNonExistent(context);
        if(license == null)
        {
            return false;
        }

        final FeatureRestriction annotation = target.getAnnotation(FeatureRestriction.class);

        return annotation == null || (
            annotation.operand() == FeatureRestrictionOperand.AND ?
            license.hasLicenseForAllFeatures(annotation.value()) :
            license.hasLicenseForAnyFeature(annotation.value())
        );
    }

    private License getValidatedLicenseOrNullIfNonExistent(final Object context)
        throws InvalidLicenseException
    {
        final License license = this.getLicense(context);
        if(license == null)
        {
            return null;
        }

        this.validateLicense(license);

        return license;
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
     *
     * @return the requested license object, or null if none exists.
     *
     * @throws KeyNotFoundException if the public key data could not be found.
     * @throws AlgorithmNotSupportedException if the signature algorithm is not supported on this system.
     * @throws InappropriateKeySpecificationException if an inappropriate key specification is provided.
     * @throws InappropriateKeyException if there is a problem initializing the verification mechanism with the
     *     public key.
     * @throws CorruptSignatureException if the signature data has been corrupted (most likely tampered with).
     * @throws InvalidSignatureException if the signature is invalid (most likely tampered with).
     * @throws FailedToDecryptException if the license or signature could not be decrypted.
     * @throws ObjectTypeNotExpectedException if the license data was tampered with.
     */
    public final License getLicense(final Object context)
        throws KeyNotFoundException, CorruptSignatureException, InvalidSignatureException, FailedToDecryptException
    {
        if(context == null)
        {
            throw new IllegalArgumentException("License context cannot be null.");
        }

        final long time = System.currentTimeMillis();

        LicenseCacheEntry entry;

        synchronized(this.licenseCache)
        {
            entry = this.licenseCache.get(context);

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
                final SignedLicense signedLicense = this.licenseProvider.getLicense(context);
                if(signedLicense == null)
                {
                    return null;
                }

                final License license = this.decryptAndVerifyLicense(signedLicense);

                signedLicense.erase();

                final long expires = time + this.cacheTimeInMilliseconds;

                entry = new LicenseCacheEntry(license, expires);

                this.licenseCache.put(context, entry);
            }
        }

        return entry.license;
    }

    /**
     * Clears the cache of licenses, forcing all license data to be re-retrieved from the license data provider on the
     * next call to {@link #getLicense(Object)}.
     */
    public final void clearLicenseCache()
    {
        synchronized(this.licenseCache)
        {
            this.licenseCache.clear();
        }
    }

    /**
     * This method verifies the signed license object's signature. It throws an exception if the signature is invalid.
     * Normally you will not need to call this method; all of the other methods in this class call this method at some
     * point or another in one way or another (specifically by way of {@link #getLicense(Object)}). This is a
     * convenience method useful for verifying the signature of an individual license without going through all of the
     * retrieval and caching mechanisms normally used when calling {@link #getLicense(Object)}.
     *
     * @param signedLicense The signed license object to verify
     *
     * @throws AlgorithmNotSupportedException if the signature algorithm is not supported on this system.
     * @throws InappropriateKeyException if there is a problem initializing the verification mechanism with the
     *     public key.
     * @throws CorruptSignatureException if the signature data has been corrupted (most likely tampered with).
     * @throws InvalidSignatureException if the signature is invalid (most likely tampered with).
     */
    public final void verifyLicenseSignature(final SignedLicense signedLicense)
        throws AlgorithmNotSupportedException, InappropriateKeyException, CorruptSignatureException,
               InvalidSignatureException
    {
        final char[] password = this.publicKeyPasswordProvider.getPassword();
        final byte[] keyData = this.publicKeyDataProvider.getEncryptedPublicKeyData();

        final PublicKey key = KeyFileUtilities.readEncryptedPublicKey(keyData, password);

        Arrays.fill(password, '\u0000');
        Arrays.fill(keyData, (byte) 0);

        new DataSignatureManager().verifySignature(
            key, signedLicense.getLicenseContent(), signedLicense.getSignatureContent()
        );
    }

    /**
     * This method verifies the signed license object's signature, then decrypts the signed license and returns the
     * decrypted license. It throws an exception if the signature is invalid. Normally you will not need to call this
     * method; all of the other methods in this class call this method at some point or another in one way or another
     * (specifically by way of {@link #getLicense(Object)}). This is a convenience method useful for verifying the
     * signature of and interpreting an individual license without going through all of the retrieval and caching
     * mechanisms normally used when calling {@link #getLicense(Object)}.
     *
     * @param signedLicense The signed license object to verify
     *
     * @return the decrypted license object.
     *
     * @throws AlgorithmNotSupportedException if the signature algorithm is not supported on this system.
     * @throws InappropriateKeyException if there is a problem initializing the verification mechanism with the
     *     public key.
     * @throws CorruptSignatureException if the signature data has been corrupted (most likely tampered with).
     * @throws InvalidSignatureException if the signature is invalid (most likely tampered with).
     * @throws FailedToDecryptException if the license could not be decrypted.
     */
    public final License decryptAndVerifyLicense(final SignedLicense signedLicense)
    {
        this.verifyLicenseSignature(signedLicense);

        final char[] password = this.licensePasswordProvider.getPassword();
        final byte[] encrypted = signedLicense.getLicenseContent();

        final byte[] unencrypted = Encryptor.decryptRaw(encrypted, password);

        Arrays.fill(password, '\u0000');
        Arrays.fill(encrypted, (byte) 0);

        final License license = License.deserialize(unencrypted);

        Arrays.fill(unencrypted, (byte) 0);

        return license;
    }

    /**
     * An entry in the memory-based license cache.
     */
    private final static class LicenseCacheEntry
    {
        private final License license;

        private final long expires;

        public LicenseCacheEntry(final License license, final long expires)
        {
            this.license = license;
            this.expires = expires;
        }
    }
}
