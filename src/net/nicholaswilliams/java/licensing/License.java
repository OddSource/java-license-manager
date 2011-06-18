/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.nicholaswilliams.java.licensing;

import net.nicholaswilliams.java.licensing.immutable.ImmutableLinkedHashSet;

import javax.security.auth.x500.X500Principal;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is the license object, which is serialized and signed to a file for use
 * by the application later. All the object fields are immutable and final so
 * as to prevent modification by reflection.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public final class License implements Serializable, Cloneable
{
	private final static long serialVersionUID = -5844818190125277296L;

	private final X500Principal holder;

	private final X500Principal issuer;

	private final String subject;

	private final long issueDate;

	private final long goodAfterDate;

	private final long goodBeforeDate;

	private final int numberOfLicenses;

	private final ImmutableLinkedHashSet<License.Feature> features;

	public License(License.Builder builder)
	{
		this.holder = builder.holder;
		this.issuer = builder.issuer;
		this.subject = builder.subject;
		this.issueDate = builder.issueDate;
		this.goodAfterDate = builder.goodAfterDate;
		this.goodBeforeDate = builder.goodBeforeDate;
		this.numberOfLicenses = builder.numberOfLicenses;
		this.features = new ImmutableLinkedHashSet<License.Feature>(builder.features);
	}

	protected byte[] serialize()
	{
		return this.toString().getBytes();
	}

	protected static License deserialize(byte[] data)
	{
		String string = new String(data);
		String[] parts = string.substring(1, string.length() - 1).split("\\]\\[");

		License.Builder builder = new License.Builder().
				withHolder(new X500Principal(parts[0])).
				withIssuer(new X500Principal(parts[1])).
				withSubject(parts[2]).
				withIssueDate(Long.parseLong(parts[3])).
				withGoodAfterDate(Long.parseLong(parts[4])).
				withGoodBeforeDate(Long.parseLong(parts[5])).
				withNumberOfLicenses(Integer.parseInt(parts[6]));

		for(String feature : parts[7].split("\\, "))
			builder.withFeature(License.Feature.fromString(feature));

		return new License(builder);
	}
	
	public final X500Principal getIssuer()
	{
		return this.issuer;
	}
	
	public final X500Principal getHolder()
	{
		return this.holder;
	}
	
	public final String getSubject()
	{
		return this.subject;
	}
	
	public final long getIssueDate()
	{
		return this.issueDate;
	}
	
	public final long getGoodAfterDate()
	{
		return this.goodAfterDate;
	}
	
	public final long getGoodBeforeDate()
	{
		return this.goodBeforeDate;
	}

	public final int getNumberOfLicenses()
	{
		return this.numberOfLicenses;
	}

	public final ImmutableLinkedHashSet<License.Feature> getFeatures()
	{
		return this.features.clone();
	}

	public final boolean hasLicenseForAllFeatures(String... featureNames)
	{
		long date = Calendar.getInstance().getTimeInMillis();
		for(String featureName : featureNames)
		{
			License.Feature feature = new License.Feature(featureName);
			License.Feature contained = this.features.get(feature);
			if(contained == null || (contained.getGoodBeforeDate() >= 0 && contained.getGoodBeforeDate() < date))
				return false;
		}
		return true;
	}

	public final boolean hasLicenseForAnyFeature(String... featureNames)
	{
		long date = Calendar.getInstance().getTimeInMillis();
		for(String featureName : featureNames)
		{
			License.Feature feature = new License.Feature(featureName);
			License.Feature contained = this.features.get(feature);
			if(contained != null && (contained.getGoodBeforeDate() < 0 || contained.getGoodBeforeDate() >= date))
				return true;
		}
		return false;
	}

	@Override
	public final boolean equals(Object object)
	{
		if(object == null || !object.getClass().equals(License.class))
			return false;

		License license = (License)object;

		boolean equals = ((license.holder == null && this.holder == null) || (license.holder != null && license.holder.equals(this.holder))) &&
			   ((license.issuer == null && this.issuer == null) || (license.issuer != null && license.issuer.equals(this.issuer))) &&
			   ((license.subject == null && this.subject == null) || (license.subject != null && license.subject.equals(this.subject))) &&
			   license.issueDate == this.issueDate &&
			   license.goodAfterDate == this.goodAfterDate &&
			   license.goodBeforeDate == this.goodBeforeDate &&
			   license.numberOfLicenses == this.numberOfLicenses;
		if(!equals)
			return false;

		for(License.Feature feature : this.features)
		{
			if(!license.hasLicenseForAllFeatures(feature.getName()))
				return false;
		}

		return true;
	}

	@Override
	public final int hashCode()
	{
		int result = this.numberOfLicenses;

		if(this.holder != null)
			result = 31 * result + this.holder.hashCode();
		else
			result = 31 * result;

		if(this.issuer != null)
			result = 31 * result + this.issuer.hashCode();
		else
			result = 31 * result;

		if(this.subject != null)
			result = 31 * result + this.subject.hashCode();
		else
			result = 31 * result;

		result = 31 * result + new Long(this.issueDate).hashCode();
		result = 31 * result + new Long(this.goodAfterDate).hashCode();
		result = 31 * result + new Long(this.goodBeforeDate).hashCode();

		return result;
	}

	@Override
	public final String toString()
	{
		return new StringBuilder().append('[').
				append(this.holder).append("][").
				append(this.issuer).append("][").
				append(this.subject).append("][").
				append(this.issueDate).append("][").
				append(this.goodAfterDate).append("][").
				append(this.goodBeforeDate).append("][").
				append(this.numberOfLicenses).append(']').
				append(this.features).toString();
	}

	@Override
	@SuppressWarnings("CloneDoesntCallSuperClone")
	public final License clone()
	{
		License.Builder builder = new License.Builder().
				withHolder(new X500Principal(this.holder.getEncoded())).
				withIssuer(new X500Principal(this.issuer.getEncoded())).
				withSubject(this.subject).
				withIssueDate(this.issueDate).
				withGoodAfterDate(this.goodAfterDate).
				withGoodBeforeDate(this.goodBeforeDate).
				withNumberOfLicenses(this.numberOfLicenses);

		for(License.Feature feature : this.features)
			builder.withFeature(feature);

		return new License(builder);
	}

	public static final class Feature
	{
		private final String name;

		private final long goodBeforeDate;

		private Feature(String name)
		{
			this(name, -1);
		}

		private Feature(String name, long goodBeforeDate)
		{
			this.name = name;
			this.goodBeforeDate = goodBeforeDate;
		}

		public final String getName()
		{
			return name;
		}

		public final long getGoodBeforeDate()
		{
			return goodBeforeDate;
		}

		@Override
		public final int hashCode()
		{
			return this.name.hashCode();
		}

		@Override
		public final boolean equals(Object object)
		{
			if(object == null || object.getClass() != License.Feature.class)
				return false;
			License.Feature feature = (License.Feature)object;
			return feature.name.equals(this.name);
		}

		@Override
		public final String toString()
		{
			return this.name + (char)0x1F + Long.toString(this.goodBeforeDate);
		}

		private static License.Feature fromString(String input)
		{
			String[] parts = input.split("" + (char)0x1F);
			if(parts == null || parts.length != 2)
				throw new IllegalArgumentException("The input argument did not contain exactly two parts.");

			return new License.Feature(parts[0], Long.parseLong(parts[1]));
		}
	}

	public static final class Builder
	{
		private X500Principal holder;

		private X500Principal issuer;

		private String subject;

		private long issueDate = Calendar.getInstance().getTimeInMillis();

		private long goodAfterDate;

		private long goodBeforeDate;

		private int numberOfLicenses = Integer.MAX_VALUE;

		private Set<License.Feature> features = new LinkedHashSet<License.Feature>();

		public Builder withHolder(X500Principal holder)
		{
			this.holder = holder;
			return this;
		}

		public Builder withIssuer(X500Principal issuer)
		{
			this.issuer = issuer;
			return this;
		}

		public Builder withSubject(String subject)
		{
			this.subject = subject;
			return this;
		}

		public Builder withIssueDate(long issueDate)
		{
			this.issueDate = issueDate;
			return this;
		}

		public Builder withGoodAfterDate(long goodAfterDate)
		{
			this.goodAfterDate = goodAfterDate;
			return this;
		}

		public Builder withGoodBeforeDate(long goodBeforeDate)
		{
			this.goodBeforeDate = goodBeforeDate;
			return this;
		}

		public Builder withNumberOfLicenses(int numberOfLicenses)
		{
			this.numberOfLicenses = numberOfLicenses;
			return this;
		}

		public Builder withFeature(String featureName)
		{
			this.features.add(new License.Feature(featureName));
			return this;
		}

		public Builder withFeature(String featureName, long goodBeforeDate)
		{
			this.features.add(new License.Feature(featureName, goodBeforeDate));
			return this;
		}

		public Builder withFeature(License.Feature feature)
		{
			this.features.add(feature);
			return this;
		}

		@SuppressWarnings("unused")
		public Builder withFeatures(Collection<License.Feature> features)
		{
			this.features.addAll(features);
			return this;
		}
	}
}
