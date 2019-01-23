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

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import io.oddsource.java.licensing.immutable.ImmutableLinkedHashSet;

/**
 * This is the license object, which is serialized and signed to a file for use by the application later. All the
 * object fields are immutable and final so as to prevent modification by reflection.<br />
 * <br />
 * This object is not created directly. For instructions on creating licenses, see the documentation for
 * {@link License.Builder}.
 *
 * @see License.Builder
 * @see License.Feature
 * @author Nick Williams
 * @version 1.7.0
 * @since 1.0.0
 */
public final class License implements Serializable, Cloneable
{
    private final static long serialVersionUID = -5844818190125277296L;

    private final static short licensePartsLength = 9;

    private final static short licensePartsProductKey = 0;

    private final static short licensePartsHolder = 1;

    private final static short licensePartsIssuer = 2;

    private final static short licensePartsSubject = 3;

    private final static short licensePartsIssueDate = 4;

    private final static short licensePartsGoodAfterDate = 5;

    private final static short licensePartsGoodBeforeDate = 6;

    private final static short licensePartsNumberOfLicenses = 7;

    private final static short licensePartsFeatures = 8;

    private final static String licenseStringRepresentationSeparator = "][";

    private final static String featureExpirationSeparator = new String(new char[]{0x1F});

    private final String productKey;

    private final String holder;

    private final String issuer;

    private final String subject;

    private final long issueDate;

    private final long goodAfterDate;

    private final long goodBeforeDate;

    private final int numberOfLicenses;

    private final ImmutableLinkedHashSet<Feature> features;

    /**
     * An internal constructor for creating a license from a builder.
     *
     * @param builder The builder to use to construct this license
     */
    private License(final License.Builder builder)
    {
        this.productKey = builder.productKey == null ? "" : builder.productKey;
        this.holder = builder.holder == null ? "" : builder.holder;
        this.issuer = builder.issuer == null ? "" : builder.issuer;
        this.subject = builder.subject == null ? "" : builder.subject;
        this.issueDate = builder.issueDate;
        this.goodAfterDate = builder.goodAfterDate;
        this.goodBeforeDate = builder.goodBeforeDate;
        this.numberOfLicenses = builder.numberOfLicenses;
        this.features = new ImmutableLinkedHashSet<>(builder.features);
    }

    /**
     * An internal constructor for creating a license from a tokenized string.
     *
     * @param parts The tokenized string to construct this license from
     */
    private License(final String[] parts)
    {
        if(parts == null || parts.length != License.licensePartsLength)
        {
            throw new IllegalArgumentException("There should be exactly nine parts to the serialized license.");
        }

        this.productKey = parts[License.licensePartsProductKey] == null ? "" : parts[License.licensePartsProductKey];
        this.holder = parts[License.licensePartsHolder] == null ? "" : parts[License.licensePartsHolder];
        this.issuer = parts[License.licensePartsIssuer] == null ? "" : parts[License.licensePartsIssuer];
        this.subject = parts[License.licensePartsSubject] == null ? "" : parts[License.licensePartsSubject];
        this.issueDate = Long.parseLong(parts[License.licensePartsIssueDate]);
        this.goodAfterDate = Long.parseLong(parts[License.licensePartsGoodAfterDate]);
        this.goodBeforeDate = Long.parseLong(parts[License.licensePartsGoodBeforeDate]);
        this.numberOfLicenses = Integer.parseInt(parts[License.licensePartsNumberOfLicenses]);

        final String featuresPart = parts[License.licensePartsFeatures];
        final Set<License.Feature> features = new LinkedHashSet<>();
        if(featuresPart != null && featuresPart.trim().length() > 0)
        {
            for(final String feature : featuresPart.split(", "))
            {
                features.add(License.Feature.fromString(feature));
            }
        }
        this.features = new ImmutableLinkedHashSet<>(features);
    }

    /**
     * Serialized this license into a byte array that can be shipped electronically.
     *
     * @return a serialized form of this license.
     */
    public byte[] serialize()
    {
        return this.toString().getBytes(LicensingCharsets.UTF_8);
    }

    /**
     * Deserializes a serialized license into an actual License object.
     *
     * @param data The serialized data to create the license from
     *
     * @return the unserialized license.
     */
    protected static License deserialize(final byte[] data)
    {
        final String string = new String(data, LicensingCharsets.UTF_8);
        final String[] parts = string.substring(1, string.length() - 1).split("]\\[", -1);

        return new License(parts);
    }

    /**
     * Returns the product key for this license. The productKey, {@link #getIssuer() issuer},
     * {@link #getHolder() holder}, and {@link #getSubject() subject} are symbolically named; they are interchangeable
     * and can be used to hold any number of pieces of information. For example, one might use the holder to store a
     * hardware ID, or the subject to store a product name and version combination.
     *
     * @return the product key.
     */
    public final String getProductKey()
    {
        return this.productKey;
    }

    /**
     * Returns the issuer of this license. The {@link #getProductKey() productKey}, issuer,
     * {@link #getHolder() holder}, and {@link #getSubject() subject} are symbolically named; they are interchangeable
     * and can be used to hold any number of pieces of information. For example, one might use the holder to store a
     * hardware ID, or the subject to store a product name and version combination.
     *
     * @return the license issuer.
     */
    public final String getIssuer()
    {
        return this.issuer;
    }

    /**
     * Returns the holder of this license. The {@link #getProductKey() productKey}, {@link #getIssuer() issuer},
     * holder, and {@link #getSubject() subject} are symbolically named; they are interchangeable and can
     * be used to hold any number of pieces of information. For example, one might use the holder to store a hardware
     * ID, or the subject to store a product name and version combination.
     *
     * @return the license holder.
     */
    public final String getHolder()
    {
        return this.holder;
    }

    /**
     * Returns the subject for this license. The {@link #getProductKey() productKey}, {@link #getIssuer() issuer},
     * {@link #getHolder() holder}, and subject are symbolically named; they are interchangeable and can
     * be used to hold any number of pieces of information. For example, one might use the holder to store a hardware
     * ID, or the subject to store a product name and version combination.
     *
     * @return the license subject.
     */
    public final String getSubject()
    {
        return this.subject;
    }

    /**
     * Returns the millisecond timestamp for the issue date of this license.
     *
     * @return the date this license was issued.
     */
    public final long getIssueDate()
    {
        return this.issueDate;
    }

    /**
     * Returns the millisecond timestamp for the date after which this license is valid (usually equal or close to the
     * issue date, but this is not required).
     *
     * @return the date after which this license is valid.
     */
    public final long getGoodAfterDate()
    {
        return this.goodAfterDate;
    }

    /**
     * Returns the millisecond timestamp for the date before which this license is valid (i.e., the expiration date).
     *
     * @return the date before which this license is valid (and after which it has expired).
     */
    public final long getGoodBeforeDate()
    {
        return this.goodBeforeDate;
    }

    /**
     * Returns the number of licenses/seats/users that this license is valid for.
     *
     * @return the number of licenses this represents.
     */
    public final int getNumberOfLicenses()
    {
        return this.numberOfLicenses;
    }

    /**
     * Returns an immutable (unchangeable) list of all of the features contained within this license. For more
     * information on features see {@link License.Feature} and {@link License.Builder}.
     *
     * @return a list of all of the licensed features.
     *
     * @see License.Feature
     */
    public final ImmutableLinkedHashSet<License.Feature> getFeatures()
    {
        return this.features.clone();
    }

    /**
     * Checks if the feature specified is licensed. If the feature is licensed and has an expiration, ensures that the
     * feature is not expired based on the current date before returning {@code true}.
     *
     * @param featureName The feature to check
     *
     * @return {@code true} if this feature is licensed and valid, {@code false} otherwise.
     */
    public final boolean hasLicenseForFeature(final String featureName)
    {
        return this.hasLicenseForFeature(System.currentTimeMillis(), featureName);
    }

    /**
     * Checks if the feature specified is licensed. If the feature is licensed and has an expiration, ensures that the
     * feature is not expired based on the provided date before returning {@code true}.
     *
     * @param currentDate The date (millisecond timestamp) to check the feature against
     * @param featureName The feature to check
     *
     * @return {@code true} if this feature is licensed and valid, {@code false} otherwise.
     */
    public final boolean hasLicenseForFeature(final long currentDate, final String featureName)
    {
        final License.Feature feature = new License.Feature(featureName);
        final License.Feature contained = this.features.get(feature);
        return contained != null && (contained.getGoodBeforeDate() < 0 || contained.getGoodBeforeDate() >= currentDate);
    }

    /**
     * Checks if the feature specified is licensed. If the feature is licensed and has an expiration, ensures that the
     * feature is not expired based on the current date before returning {@code true}.
     *
     * @param feature The feature to check
     *
     * @return {@code true} if this feature is licensed and valid, {@code false} otherwise.
     */
    public final boolean hasLicenseForFeature(final FeatureObject feature)
    {
        return this.hasLicenseForFeature(System.currentTimeMillis(), feature.getName());
    }

    /**
     * Checks if the feature specified is licensed. If the feature is licensed and has an expiration, ensures that the
     * feature is not expired based on the provided date before returning {@code true}.
     *
     * @param currentDate The date (millisecond timestamp) to check the feature against
     * @param feature The feature to check
     *
     * @return {@code true} if this feature is licensed and valid, {@code false} otherwise.
     */
    public final boolean hasLicenseForFeature(final long currentDate, final FeatureObject feature)
    {
        return this.hasLicenseForFeature(currentDate, feature.getName());
    }

    /**
     * Checks if all of the features specified are licensed in this license. If any one feature is licensed, this
     * method returns {@code true}. Any features that contain expiration dates are checked against the current date.
     * Features are only valid if they have no expiration date or they are not expired.
     *
     * @param featureNames The features to check
     *
     * @return {@code true} if any one feature listed is licensed and valid, {@code false} otherwise.
     */
    public final boolean hasLicenseForAnyFeature(final String... featureNames)
    {
        return this.hasLicenseForAnyFeature(System.currentTimeMillis(), featureNames);
    }

    /**
     * Checks if all of the features specified are licensed in this license. If any one feature is licensed, this
     * method returns {@code true}. Any features that contain expiration dates are checked against the current date.
     * Features are only valid if they have no expiration date or they are not expired.
     *
     * @param currentDate The date (millisecond timestamp) to check features against
     * @param featureNames The features to check
     *
     * @return {@code true} if any one feature listed is licensed and valid, {@code false} otherwise.
     */
    public final boolean hasLicenseForAnyFeature(final long currentDate, final String... featureNames)
    {
        for(final String featureName : featureNames)
        {
            if(this.hasLicenseForFeature(currentDate, featureName))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if all of the features specified are licensed in this license. If any one feature is licensed, this
     * method returns {@code true}. Any features that contain expiration dates are checked against the current date.
     * Features are only valid if they have no expiration date or they are not expired.
     *
     * @param features The features to check
     *
     * @return {@code true} if any one feature listed is licensed and valid, {@code false} otherwise.
     */
    public final boolean hasLicenseForAnyFeature(final FeatureObject... features)
    {
        return this.hasLicenseForAnyFeature(System.currentTimeMillis(), features);
    }

    /**
     * Checks if all of the features specified are licensed in this license. If any one feature is licensed, this
     * method returns {@code true}. Any features that contain expiration dates are checked against the current date.
     * Features are only valid if they have no expiration date or they are not expired.
     *
     * @param currentDate The date (millisecond timestamp) to check features against
     * @param features The features to check
     *
     * @return {@code true} if any one feature listed is licensed and valid, {@code false} otherwise.
     */
    public final boolean hasLicenseForAnyFeature(final long currentDate, final FeatureObject... features)
    {
        for(final FeatureObject feature : features)
        {
            if(this.hasLicenseForFeature(currentDate, feature))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if all of the features specified are licensed in this license. If any one feature isn't licensed, this
     * method returns {@code false}. Any features that contain expiration dates are checked against the current date.
     * If any one feature is expired, this method returns {@code false}.
     *
     * @param featureNames The features to check
     *
     * @return {@code true} if every feature listed is licensed and valid, {@code false} otherwise.
     */
    public final boolean hasLicenseForAllFeatures(final String... featureNames)
    {
        return this.hasLicenseForAllFeatures(System.currentTimeMillis(), featureNames);
    }

    /**
     * Checks if all of the features specified are licensed in this license. If any one feature isn't licensed, this
     * method returns {@code false}. Any features that contain expiration dates are checked against the provided date.
     * If any one feature is expired, this method returns {@code false}.
     *
     * @param currentDate The date (millisecond timestamp) to check features against
     * @param featureNames The features to check
     *
     * @return {@code true} if every feature listed is licensed and valid, {@code false} otherwise.
     */
    public final boolean hasLicenseForAllFeatures(final long currentDate, final String... featureNames)
    {
        for(final String featureName : featureNames)
        {
            if(!this.hasLicenseForFeature(currentDate, featureName))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if all of the features specified are licensed in this license. If any one feature isn't licensed, this
     * method returns {@code false}. Any features that contain expiration dates are checked against the current date.
     * If any one feature is expired, this method returns {@code false}.
     *
     * @param features The features to check
     *
     * @return {@code true} if every feature listed is licensed and valid, {@code false} otherwise.
     */
    public final boolean hasLicenseForAllFeatures(final FeatureObject... features)
    {
        return this.hasLicenseForAllFeatures(System.currentTimeMillis(), features);
    }

    /**
     * Checks if all of the features specified are licensed in this license. If any one feature isn't licensed, this
     * method returns {@code false}. Any features that contain expiration dates are checked against the provided date.
     * If any one feature is expired, this method returns {@code false}.
     *
     * @param currentDate The date (millisecond timestamp) to check features against
     * @param features The features to check
     *
     * @return {@code true} if every feature listed is licensed and valid, {@code false} otherwise.
     */
    public final boolean hasLicenseForAllFeatures(final long currentDate, final FeatureObject... features)
    {
        for(final FeatureObject feature : features)
        {
            if(!this.hasLicenseForFeature(currentDate, feature))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the provided license object is identical to this one in content, features and expiration dates,
     * deeply.
     *
     * @param object The license to check for equality against
     *
     * @return {@code true} if the licenses are identical, {@code false} otherwise.
     */
    @Override
    public final boolean equals(final Object object)
    {
        if(object == null || !object.getClass().equals(License.class))
        {
            return false;
        }

        final License license = (License) object;

        final boolean equals = (
                                   (license.productKey == null && this.productKey == null) ||
                                   (license.productKey != null && license.productKey.equals(this.productKey))
                               ) &&
                               (
                                   (license.holder == null && this.holder == null) ||
                                   (license.holder != null && license.holder.equals(this.holder))
                               ) &&
                               (
                                   (license.issuer == null && this.issuer == null) ||
                                   (license.issuer != null && license.issuer.equals(this.issuer))
                               ) &&
                               (
                                   (license.subject == null && this.subject == null) ||
                                   (license.subject != null && license.subject.equals(this.subject))
                               ) &&
                               license.issueDate == this.issueDate &&
                               license.goodAfterDate == this.goodAfterDate &&
                               license.goodBeforeDate == this.goodBeforeDate &&
                               license.numberOfLicenses == this.numberOfLicenses;
        if(!equals)
        {
            return false;
        }

        for(final License.Feature feature : this.features)
        {
            final License.Feature contained = license.features.get(feature);
            if(contained == null || contained.getGoodBeforeDate() != feature.getGoodBeforeDate())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Calculates a hash code for this license.
     *
     * @return a hash code for this license.
     */
    @Override
    public final int hashCode()
    {
        int result = this.numberOfLicenses;

        if(this.productKey != null && this.productKey.length() > 0)
        {
            result = 31 * result + this.productKey.hashCode();
        }
        else
        {
            result = 31 * result;
        }

        if(this.holder != null && this.holder.length() > 0)
        {
            result = 31 * result + this.holder.hashCode();
        }
        else
        {
            result = 31 * result;
        }

        if(this.issuer != null && this.issuer.length() > 0)
        {
            result = 31 * result + this.issuer.hashCode();
        }
        else
        {
            result = 31 * result;
        }

        if(this.subject != null && this.subject.length() > 0)
        {
            result = 31 * result + this.subject.hashCode();
        }
        else
        {
            result = 31 * result;
        }

        result = 31 * result + Long.valueOf(this.issueDate).hashCode();
        result = 31 * result + Long.valueOf(this.goodAfterDate).hashCode();
        result = 31 * result + Long.valueOf(this.goodBeforeDate).hashCode();
        result = 31 * result + this.features.hashCode();

        return result;
    }

    /**
     * Generates a string representation of this license.
     *
     * @return the string representation of this license.
     */
    @Override
    public final String toString()
    {
        //noinspection StringBufferReplaceableByString
        return new StringBuilder().append('[').
            append(this.productKey == null ? "" : this.productKey).append(License.licenseStringRepresentationSeparator).
            append(this.holder == null ? "" : this.holder).append(License.licenseStringRepresentationSeparator).
            append(this.issuer == null ? "" : this.issuer).append(License.licenseStringRepresentationSeparator).
            append(this.subject == null ? "" : this.subject).append(License.licenseStringRepresentationSeparator).
            append(this.issueDate).append(License.licenseStringRepresentationSeparator).
            append(this.goodAfterDate).append(License.licenseStringRepresentationSeparator).
            append(this.goodBeforeDate).append(License.licenseStringRepresentationSeparator).
            append(this.numberOfLicenses).append(']').
            append(this.features).toString();
    }

    /**
     * Performs a deep clone of this license and its content and features.
     *
     * @return a deep clone of this license.
     */
    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public final License clone()
    {
        final License.Builder builder = new License.Builder().
            withProductKey(this.productKey).
            withHolder(this.holder).
            withIssuer(this.issuer).
            withSubject(this.subject).
            withIssueDate(this.issueDate).
            withGoodAfterDate(this.goodAfterDate).
            withGoodBeforeDate(this.goodBeforeDate).
            withNumberOfLicenses(this.numberOfLicenses);

        for(final License.Feature feature : this.features)
        {
            builder.addFeature(feature.clone());
        }

        return builder.build();
    }

    /**
     * A class that represents a licensed feature. Products may have more than one "section" or "module" that is
     * licensed, and the list of features in the {@link License} object allows the user to specify any number of
     * features and, optionally, separate expiration dates for each.<br />
     * <br />
     * This object is not created directly. For instructions on using features, see the documentation for
     * {@link License.Builder}.
     *
     * @see License
     * @see License.Builder
     * @author Nick Williams
     * @version 1.2.0
     * @since 1.0.0
     */
    public static final class Feature implements Cloneable, Serializable, FeatureObject
    {
        private static final long serialVersionUID = 1L;

        private final String name;

        private final long goodBeforeDate;

        /**
         * Creates a license feature with the specified name and no expiration date.
         *
         * @param name The feature name
         */
        private Feature(final String name)
        {
            this(name, -1);
        }

        /**
         * Creates a license feature with the specified name and expiration date.
         *
         * @param name The feature name
         * @param goodBeforeDate The feature expiration date
         */
        private Feature(final String name, final long goodBeforeDate)
        {
            this.name = name;
            this.goodBeforeDate = goodBeforeDate;
        }

        /**
         * Deserializes a string representation of a feature into a feature.
         *
         * @param input The string representation of a feature, generated with {@link #toString()}.
         *
         * @return the unserialized feature.
         */
        private static License.Feature fromString(final String input)
        {
            if(input == null)
            {
                throw new IllegalArgumentException("The input argument cannot be null.");
            }

            final String[] parts = input.split(License.featureExpirationSeparator);
            if(parts.length != 2)
            {
                throw new IllegalArgumentException("The input argument did not contain exactly two parts.");
            }

            return new License.Feature(parts[0], Long.parseLong(parts[1]));
        }

        /**
         * Returns the feature name or subject.
         *
         * @return the feature name.
         */
        @Override
        public final String getName()
        {
            return name;
        }

        /**
         * Returns the feature expiration date.
         *
         * @return the expiration date.
         */
        public final long getGoodBeforeDate()
        {
            return goodBeforeDate;
        }

        /**
         * Indicates whether these features are the same feature. Important note: Two features <b>can</b> be the same
         * feature (equal) and have different expiration dates.
         *
         * @param object The feature to check for equality against
         *
         * @return {@code true} if the features are the same, {@code false} otherwise.
         */
        @Override
        public final boolean equals(final Object object)
        {
            if(object == null || object.getClass() != License.Feature.class)
            {
                return false;
            }
            final License.Feature feature = (License.Feature) object;
            return feature.name.equals(this.name);
        }

        /**
         * Generates a hash code for this feature.
         *
         * @return a hash code for this feature.
         */
        @Override
        public final int hashCode()
        {
            return this.name.hashCode();
        }

        /**
         * Generates a string representation of this feature (name and expiration date separated by 0x1F).
         *
         * @return the string representation of this feature.
         */
        @Override
        public final String toString()
        {
            return this.name + License.featureExpirationSeparator + this.goodBeforeDate;
        }

        /**
         * Returns a clone of this feature.
         *
         * @return the feature clone.
         */
        @Override
        @SuppressWarnings("CloneDoesntCallSuperClone")
        public License.Feature clone()
        {
            return new License.Feature(this.name, this.goodBeforeDate);
        }
    }

    /**
     * This class is responsible for all license creation. Each method in this class returns the builder instance to
     * make chaining possible. To create a license, simply create an instance of a builder, set any values you wish
     * to be set on the license (or choose not to set values, as appropriate), then call {@link #build()}.
     */
    public static final class Builder
    {
        private final Set<License.Feature> features = new LinkedHashSet<>();

        private String productKey;

        private String holder;

        private String issuer;

        private String subject;

        private long issueDate = System.currentTimeMillis();

        private long goodAfterDate;

        private long goodBeforeDate;

        private int numberOfLicenses = Integer.MAX_VALUE;

        /**
         * Creates a new builder for generating a new license.
         */
        public Builder()
        {

        }

        /**
         * Sets the product key for this license. The productKey, {@link #withIssuer(String) issuer},
         * {@link #withHolder(String) holder}, and {@link #withSubject(String) subject} are symbolically named; they are
         * interchangeable
         * and can be used to hold any number of pieces of information. For example, one might use the holder to store
         * a
         * hardware ID, or the subject to store a product name and version combination.
         *
         * @param productKey The product key
         *
         * @return the builder instance.
         */
        public Builder withProductKey(final String productKey)
        {
            this.productKey = productKey;
            return this;
        }

        /**
         * Sets the issuer for this license. The {@link #withProductKey(String) productKey}, issuer,
         * {@link #withHolder(String) holder}, and {@link #withSubject(String) subject} are symbolically named; they are
         * interchangeable
         * and can be used to hold any number of pieces of information. For example, one might use the holder to store
         * a
         * hardware ID, or the subject to store a product name and version combination.
         *
         * @param issuer The license issuer
         *
         * @return the builder instance.
         */
        public Builder withIssuer(final String issuer)
        {
            this.issuer = issuer;
            return this;
        }

        /**
         * Sets the holder for this license. The {@link #withProductKey(String) productKey}, {@link #withIssuer(String)
         * issuer},
         * holder, and {@link #withSubject(String) subject} are symbolically named; they are interchangeable
         * and can be used to hold any number of pieces of information. For example, one might use the holder to store
         * a
         * hardware ID, or the subject to store a product name and version combination.
         *
         * @param holder The license holder
         *
         * @return the builder instance.
         */
        public Builder withHolder(final String holder)
        {
            this.holder = holder;
            return this;
        }

        /**
         * Sets the subject for this license. The {@link #withProductKey(String) productKey}, {@link #withIssuer(String)
         * issuer}, {@link #withHolder(String) holder}, and subject are symbolically named; they are interchangeable
         * and can be used to hold any number of pieces of information. For example, one might use the holder to store
         * a
         * hardware ID, or the subject to store a product name and version combination.
         *
         * @param subject The license subject
         *
         * @return the builder instance.
         */
        public Builder withSubject(final String subject)
        {
            this.subject = subject;
            return this;
        }

        /**
         * Sets the issue date (millisecond timestamp) for this license.
         *
         * @param issueDate The date this license was issued
         *
         * @return the builder instance.
         */
        public Builder withIssueDate(final long issueDate)
        {
            this.issueDate = issueDate;
            return this;
        }

        /**
         * Sets the valid date (millisecond timestamp) for this license (usually the equal or close to the issue date,
         * but that is not required).
         *
         * @param goodAfterDate The date after which this license is valid
         *
         * @return the builder instance.
         */
        public Builder withGoodAfterDate(final long goodAfterDate)
        {
            this.goodAfterDate = goodAfterDate;
            return this;
        }

        /**
         * Sets the expiration date (millisecond timestamp) for this license.
         *
         * @param goodBeforeDate The date after which this license is no longer valid
         *
         * @return the builder instance.
         */
        public Builder withGoodBeforeDate(final long goodBeforeDate)
        {
            this.goodBeforeDate = goodBeforeDate;
            return this;
        }

        /**
         * Sets the number of licenses/seats/users this license is good for.
         *
         * @param numberOfLicenses The number of licenses this license is good for
         *
         * @return the builder instance.
         */
        public Builder withNumberOfLicenses(final int numberOfLicenses)
        {
            this.numberOfLicenses = numberOfLicenses;
            return this;
        }

        /**
         * Adds a feature to this license with no expiration date.
         *
         * @param featureName The feature to add to this license
         *
         * @return the builder instance.
         */
        public Builder addFeature(final String featureName)
        {
            this.features.add(new License.Feature(featureName));
            return this;
        }

        /**
         * Adds a feature to this license with the specified expiration date (millisecond timestamp).
         *
         * @param featureName The feature to add to this license
         * @param goodBeforeDate The expiration date for this license
         *
         * @return the builder instance.
         */
        public Builder addFeature(final String featureName, final long goodBeforeDate)
        {
            this.features.add(new License.Feature(featureName, goodBeforeDate));
            return this;
        }

        /**
         * Adds the existing feature object to this license.
         *
         * @param feature The feature object to add to this license
         *
         * @return the builder instance.
         */
        @SuppressWarnings("UnusedReturnValue")
        public Builder addFeature(final License.Feature feature)
        {
            this.features.add(feature);
            return this;
        }

        /**
         * Builds the license based on the criteria in this builder, then returns it.
         *
         * @return the completed license with all of the content specified by this builder.
         */
        public License build()
        {
            return new License(this);
        }
    }
}
