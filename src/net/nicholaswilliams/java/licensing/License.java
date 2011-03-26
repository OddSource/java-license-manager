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
import java.util.Arrays;
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

	private final ImmutableLinkedHashSet<String> features;

	public License(License.Builder builder)
	{
		this.holder = builder.holder;
		this.issuer = builder.issuer;
		this.subject = builder.subject;
		this.issueDate = builder.issueDate;
		this.goodAfterDate = builder.goodAfterDate;
		this.goodBeforeDate = builder.goodBeforeDate;
		this.numberOfLicenses = builder.numberOfLicenses;
		this.features = new ImmutableLinkedHashSet<String>(builder.features);
	}

	protected byte[] serialize()
	{
		return this.toString().getBytes();
	}

	protected static License deserialize(byte[] data)
	{
		String string = new String(data);
		String[] parts = string.substring(1, string.length() - 1).split("\\]\\[");

		return new License(
				new License.Builder().
						withHolder( new X500Principal(parts[0]) ).
						withIssuer( new X500Principal(parts[1]) ).
						withSubject( parts[2] ).
						withIssueDate( Long.parseLong(parts[3]) ).
						withGoodAfterDate( Long.parseLong(parts[4]) ).
						withGoodBeforeDate( Long.parseLong(parts[5]) ).
						withNumberOfLicenses( Integer.parseInt(parts[6]) ).
						withFeatures( Arrays.asList(parts[7].split("\\, ")) )
		);
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

	public final ImmutableLinkedHashSet<String> getFeatures()
	{
		return this.features.clone();
	}

	public final boolean hasLicenseForAllFeatures(String... features)
	{
		for(String feature : features)
		{
			if(!this.features.contains(feature))
				return false;
		}
		return true;
	}

	public final boolean hasLicenseForAnyFeature(String... features)
	{
		for(String feature : features)
		{
			if(this.features.contains(feature))
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

		return ((license.holder == null && this.holder == null) || (license.holder != null && license.holder.equals(this.holder))) &&
			   ((license.issuer == null && this.issuer == null) || (license.issuer != null && license.issuer.equals(this.issuer))) &&
			   ((license.subject == null && this.subject == null) || (license.subject != null && license.subject.equals(this.subject))) &&
			   license.issueDate == this.issueDate &&
			   license.goodAfterDate == this.goodAfterDate &&
			   license.goodBeforeDate == this.goodBeforeDate &&
			   license.numberOfLicenses == this.numberOfLicenses &&
			   license.hasLicenseForAllFeatures( this.features.toArray(new String[this.features.size()]) );
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

		for(String feature : this.features)
			builder.withFeature(feature);

		return new License(builder);
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

		private Set<String> features = new LinkedHashSet<String>();

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

		public Builder withFeature(String feature)
		{
			this.features.add(feature);
			return this;
		}

		public Builder withFeatures(Collection<String> features)
		{
			this.features.addAll(features);
			return this;
		}
	}
}
