/*
 * ConsoleLicenseGenerator.java from LicenseManager modified Monday, May 21, 2012 20:07:08 CDT (-0500).
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

package net.nicholaswilliams.java.licensing.licensor.interfaces.text;

import net.nicholaswilliams.java.licensing.License;
import net.nicholaswilliams.java.licensing.encryption.PasswordProvider;
import net.nicholaswilliams.java.licensing.exception.AlgorithmNotSupportedException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeyException;
import net.nicholaswilliams.java.licensing.exception.InappropriateKeySpecificationException;
import net.nicholaswilliams.java.licensing.exception.KeyNotFoundException;
import net.nicholaswilliams.java.licensing.exception.ObjectSerializationException;
import net.nicholaswilliams.java.licensing.licensor.FilePrivateKeyDataProvider;
import net.nicholaswilliams.java.licensing.licensor.LicenseCreator;
import net.nicholaswilliams.java.licensing.licensor.LicenseCreatorProperties;
import net.nicholaswilliams.java.licensing.licensor.PrivateKeyDataProvider;
import net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction.CliOptionsBuilder;
import net.nicholaswilliams.java.licensing.licensor.interfaces.text.abstraction.TextInterfaceDevice;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A command-line tool for generating licenses. Usage is as follows:<br />
 * <br />
 * To view usage and help:<br />
 * <code>ConsoleLicenseGenerator -help</code><br />
 * <br />
 * To specify just config properties and be prompted for license properties:<br />
 * <code>ConsoleLicenseGenerator -config <file></code><br />
 * <br />
 * To specify just license properties and be prompted for config properties:<br />
 * <code>ConsoleLicenseGenerator -license <file></code><br />
 * <br />
 * To specify all options via properties files:<br />
 * <code>ConsoleLicenseGenerator -config <file> -license <file></code><br />
 * <br />
 * The ConsoleLicenseGenerator expects to be passed the path to two properties files, or one of them, or neither.
 * The "config" properties file contains information necessary to generate all licenses (key paths, passwords, etc.)
 * and generally will not need to change. The "license" properties file contains all of the information you need to
 * generate this particular license. See the Javadoc API documentation for information about the contents of these two
 * files.<br />
 * <br />
 * If you do not specify the "config" properties file, you will be prompted to provide the values that were expected in
 * that file. Likewise, if you do not specify the "license" properties file, you will be prompted to provide the values
 * that were expected in that file.<br />
 * <br />
 * <b>Required Config Properties File Properties</b><br />
 * <code>net.nicholaswilliams.java.licensing.privateKeyFile=[The path to the file containing the encrypted private key; either this or net.nicholaswilliams.java.licensing.privateKeyClass must be specified.]<br />
 * net.nicholaswilliams.java.licensing.privateKeyProvider=[The fully-qualified (canonical) name of the implementation of {@link net.nicholaswilliams.java.licensing.licensor.PrivateKeyDataProvider}; either this or net.nicholaswilliams.java.licensing.privateKeyFile must be specified.]<br />
 * net.nicholaswilliams.java.licensing.privateKeyPassword=[The password for decrypting the private key; either this or net.nicholaswilliams.java.licensing.privateKeyPasswordClass must be specified.]<br />
 * net.nicholaswilliams.java.licensing.privateKeyPasswordProvider=[The fully-qualified (canonical) name of the implementation of {@link net.nicholaswilliams.java.licensing.encryption.PasswordProvider} that provides the private key password; either this or net.nicholaswilliams.java.licensing.privateKeyPassword must be specified]</code><br />
 * <br />
 * <b>License Properties File Properties<b/> (Specify only those that you wish to include in the license)<br />
 * <code>net.nicholaswilliams.java.licensing.productKey=[The product key/serial number (String) for this license]
 * net.nicholaswilliams.java.licensing.holder=[The holder (String) for this license]
 * net.nicholaswilliams.java.licensing.issuer=[The issuer (String) for this license]
 * net.nicholaswilliams.java.licensing.subject=[The subject (String) for this license]
 * net.nicholaswilliams.java.licensing.issueDate=[The date this license was issued (defaults to the current date/time), format: YYYY-MM-DD hh:mm:ss in 24-hour time]
 * net.nicholaswilliams.java.licensing.goodAfterDate=[The date this license becomes active, format: YYYY-MM-DD hh:mm:ss in 24-hour time]
 * net.nicholaswilliams.java.licensing.goodBeforeDate=[The date this license expires, format: format: YYYY-MM-DD hh:mm:ss in 24-hour time]
 * net.nicholaswilliams.java.licensing.numberOfLicenses=[The seats/users/number of licenses this license is good for]
 * net.nicholaswilliams.java.licensing.features.MY_FEATURE=[Feature expiration date for "MY_FEATURE", format: YYYY-MM-DD hh:mm:ss in 24-hour time or "none"]
 * net.nicholaswilliams.java.licensing.features.ANOTHER_FEATURE=[Feature expiration date for "ANOTHER_FEATURE", format: YYYY-MM-DD hh:mm:ss in 24-hour time or "none"]</code><br />
 * <br />
 * You can specify any arbitrary number of features (and their expiration date) using multiple properties that start
 * with <code>net.nicholaswilliams.java.licensing.features</code>. The last part of the property key should be the
 * feature name. The value should either be "none" or the date the feature expires (YYYY-MM-DD hh:mm:ss in 24-hour
 * time).<br />
 * <br />
 * <b>NOTE:</b> The default behavior is to encrypt the resulting license with the password or password provider
 * specified in the config properties file. To override this and use a different password to encrypt the license,
 * add <code>net.nicholaswilliams.java.licensing.password=[encryption password]</code> to the license properties
 * file.<br />
 * <br />
 * <b>NOTE:</b> The default behavior is to print the resulting license, Base64-encoded, to standard out. Instead, you
 * can write the raw, binary license data to a specific file with the property
 * <code>net.nicholaswilliams.java.licensing.licenseFile=[file name]</code>.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConsoleLicenseGenerator
{
	public static final String PROPERTY_PRIVATE_KEY_FILE = "net.nicholaswilliams.java.licensing.privateKeyFile";

	public static final String PROPERTY_PRIVATE_KEY_PROVIDER = "net.nicholaswilliams.java.licensing.privateKeyProvider";

	public static final String PROPERTY_PRIVATE_KEY_PASSWORD = "net.nicholaswilliams.java.licensing.privateKeyPassword";

	public static final String PROPERTY_PRIVATE_KEY_PASSWORD_PROVIDER = "net.nicholaswilliams.java.licensing.privateKeyPasswordProvider";

	public static final String PROPERTY_LICENSE_PRODUCT_KEY = "net.nicholaswilliams.java.licensing.productKey";

	public static final String PROPERTY_LICENSE_HOLDER = "net.nicholaswilliams.java.licensing.holder";

	public static final String PROPERTY_LICENSE_ISSUER = "net.nicholaswilliams.java.licensing.issuer";

	public static final String PROPERTY_LICENSE_SUBJECT = "net.nicholaswilliams.java.licensing.subject";

	public static final String PROPERTY_LICENSE_ISSUE_DATE = "net.nicholaswilliams.java.licensing.issueDate";

	public static final String PROPERTY_LICENSE_GOOD_AFTER_DATE = "net.nicholaswilliams.java.licensing.goodAfterDate";

	public static final String PROPERTY_LICENSE_GOOD_BEFORE_DATE = "net.nicholaswilliams.java.licensing.goodBeforeDate";

	public static final String PROPERTY_LICENSE_NUM_LICENSES = "net.nicholaswilliams.java.licensing.numberOfLicenses";

	public static final String PROPERTY_LICENSE_FEATURE_PREFIX = "net.nicholaswilliams.java.licensing.features.";

	public static final String PROPERTY_LICENSE_FILE = "net.nicholaswilliams.java.licensing.licenseFile";

	public static final String PROPERTY_LICENSE_PASSWORD = "net.nicholaswilliams.java.licensing.password";

	private static final int CLI_WIDTH = 105;

	private static final String USAGE = " ConsoleLicenseGenerator -help\r\n" +
										" ConsoleLicenseGenerator\r\n" +
										" ConsoleLicenseGenerator -config <file>\r\n" +
										" ConsoleLicenseGenerator -license <file>\r\n" +
										" ConsoleLicenseGenerator -config <file> -license <file>\r\n" +
										" \r\n" +
										" The ConsoleLicenseGenerator expects to be passed the path to two " +
										"properties files, or one of them, or neither. The \"config\" properties " +
										"file contains information necessary to generate all licenses (key paths, " +
										"passwords, etc.) and generally will not need to change. The \"license\" " +
										"properties file contains all of the information you need to generate this " +
										"particular license. See the Javadoc API documentation for information about " +
										"the contents of these two files.\r\n" +
										" \r\n" +
										" If you do not specify the \"config\" properties file, you will be prompted " +
										"to provide the values that were expected in that file. Likewise, if you do " +
										"not specify the \"license\" properties file, you will be prompted to " +
										"provide the values that were expected in that file.";

	private static final Option HELP = CliOptionsBuilder.get().withDescription("Display this help message").
			hasArg(false).create("help");

	private static final Option CONFIG = CliOptionsBuilder.get().withArgName("file").
			withDescription("Specify the .properties file that configures this generator").hasArg(true).
			isRequired(false).create("config");

	private static final Option LICENSE = CliOptionsBuilder.get().withArgName("file").
			withDescription("Specify the .properties file that contains the data for this license").hasArg(true).
			isRequired(false).create("license");

	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private final TextInterfaceDevice device;

	private final CommandLineParser cliParser;

	protected CommandLine cli = null;

	protected ConsoleLicenseGenerator(TextInterfaceDevice textInterfaceDevice,
									  CommandLineParser cliParser)
	{
		this.device = textInterfaceDevice;
		this.cliParser = cliParser;
	}

	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		this.device.printOutLn();
	}

	protected void processCommandLineOptions(String[] arguments)
	{
		Options firstParseOptions = new Options();
		firstParseOptions.addOption(HELP);

		Options options = new Options();
		options.addOption(HELP).addOption(CONFIG).addOption(LICENSE);

		try
		{
			this.cli = this.cliParser.parse(firstParseOptions, arguments, true);

			if(this.cli.hasOption("help"))
			{
				PrintWriter writer = new PrintWriter(this.device.out());
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp(writer, CLI_WIDTH, USAGE, null, options, 1, 3, null, false);
				writer.flush();

				this.device.exit(0);
			}
			else
			{
				this.cli = this.cliParser.parse(options, arguments);
			}
		}
		catch(ParseException e)
		{
			this.device.printErrLn(e.getLocalizedMessage());

			PrintWriter writer = new PrintWriter(this.device.out());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(writer, CLI_WIDTH, USAGE, null, options, 1, 3, null, false);
			writer.flush();

			this.device.exit(1);
		}
	}

	protected Properties readPropertiesFile(String fileName) throws Exception
	{
		File file = new File(fileName);

		if(!file.exists())
			throw new FileNotFoundException("The file [" + fileName + "] does not exist.");
		if(!file.canRead())
			throw new IOException("The file [" + fileName + "] is not readable.");

		FileReader fileReader = null;
		try
		{
			fileReader = new FileReader(file);

			Properties properties = new Properties();
			properties.load(fileReader);

			return properties;
		}
		finally
		{
			if(fileReader != null)
			{
				try {
					fileReader.close();
				} catch(Throwable ignore) { }
			}
		}
	}

	protected String promptForString(String message)
	{
		String input = this.device.readLine(message);
		this.device.printOutLn();

		return input != null && input.trim().length() > 0 ? input.trim() : null;
	}

	protected char[] promptForPassword(String message)
	{
		char[] input = this.device.readPassword(message);
		this.device.printOutLn();

		return input != null && input.length > 0 ? input : null;
	}

	protected void initializeLicenseCreator() throws Exception
	{
		Properties properties = null;
		if(this.cli.hasOption("config"))
		{
			String value = this.cli.getOptionValue("config");
			if(value != null && value.trim().length() > 0)
				properties = this.readPropertiesFile(value);
		}

		LicenseCreatorProperties.setPrivateKeyDataProvider(this.getPrivateKeyDataProvider(properties));
		LicenseCreatorProperties.setPrivateKeyPasswordProvider(this.getPrivateKeyPasswordProvider(properties));
		LicenseCreator.getInstance();
	}

	private boolean promptToUsePrivateKeyProviderClass()
	{
		this.device.printOutLn("Would you like to...");
		this.device.printOutLn("    (1) Read the private key from a file?");
		this.device.printOutLn("    (2) Use a PrivateKeyDataProvider implementation from the classpath?");
		String input = this.device.readLine("Your selection (default 1)? ");
		this.device.printOutLn();

		return input != null && input.trim().equals("2");
	}

	private PrivateKeyDataProvider getPrivateKeyDataProvider(Properties properties) throws FileNotFoundException
	{
		PrivateKeyDataProvider provider;

		if(properties == null)
		{
			if(!this.promptToUsePrivateKeyProviderClass())
			{
				String fileName = this.promptForString("Please enter the name of the private key file to use: ");
				File file = null;
				while(file == null)
				{
					while(fileName == null)
					{
						fileName = this.promptForString("Invalid or non-existent file. Please enter the name of the " +
														"private key file to use: ");
					}

					file = new File(fileName);
					if(!file.exists() || !file.canRead())
					{
						fileName = null;
						file = null;
					}
				}

				provider = new FilePrivateKeyDataProvider(file);
			}
			else
			{
				String className = this.promptForString("Please enter the fully-qualified class name for the " +
														"PrivateKeyDataProvider implementation: ");
				while(className == null)
				{
					className = this.promptForString("Please enter the fully-qualified class name for the " +
													 "PrivateKeyDataProvider implementation: ");
				}

				provider = this.getObjectAsClass(className, PrivateKeyDataProvider.class);
			}
		}
		else
		{
			String fileName = properties.getProperty(ConsoleLicenseGenerator.PROPERTY_PRIVATE_KEY_FILE);
			if(fileName != null && fileName.trim().length() > 0)
			{
				File file = new File(fileName);
				if(!file.exists() || !file.canRead())
					throw new FileNotFoundException("The private key file [" + file.getAbsolutePath() +
											   "] does not exist or cannot be read.");

				provider = new FilePrivateKeyDataProvider(file);
			}
			else
			{
				String className = properties.getProperty(ConsoleLicenseGenerator.PROPERTY_PRIVATE_KEY_PROVIDER);
				if(className != null && className.trim().length() > 0)
				{
					provider = this.getObjectAsClass(className, PrivateKeyDataProvider.class);
				}
				else
				{
					throw new RuntimeException("Neither [" + ConsoleLicenseGenerator.PROPERTY_PRIVATE_KEY_FILE +
											   "] nor [" + ConsoleLicenseGenerator.PROPERTY_PRIVATE_KEY_PROVIDER +
											   "] properties specified.");
				}
			}
		}

		return provider;
	}

	private boolean promptToUsePasswordProviderClass()
	{
		this.device.printOutLn("Would you like to...");
		this.device.printOutLn("    (1) Type the private key password in manually?");
		this.device.printOutLn("    (2) Use a PasswordProvider implementation from the classpath?");
		String input = this.device.readLine("Your selection (default 1)? ");
		this.device.printOutLn();

		return input != null && input.trim().equals("2");
	}

	private final class PrivatePasswordProvider implements PasswordProvider
	{
		private final char[] password;

		PrivatePasswordProvider(char[] password)
		{
			this.password = password;
		}

		@Override
		public char[] getPassword()
		{
			return this.password;
		}
	}

	private PasswordProvider getPrivateKeyPasswordProvider(Properties properties)
	{
		PasswordProvider provider;

		if(properties == null)
		{
			if(!this.promptToUsePasswordProviderClass())
			{
				char[] password = this.promptForPassword("Please type the password for the private key: ");
				while(password == null)
					password = this.promptForPassword("Invalid password. Please type the password for the private key: ");

				provider = new PrivatePasswordProvider(password);
			}
			else
			{
				String className = this.promptForString("Please enter the fully-qualified class name for the " +
														"PasswordProvider implementation: ");
				while(className == null)
				{
					className = this.promptForString("Please enter the fully-qualified class name for the " +
													 "PasswordProvider implementation: ");
				}

				provider = this.getObjectAsClass(className, PasswordProvider.class);
			}
		}
		else
		{
			String password = properties.getProperty(ConsoleLicenseGenerator.PROPERTY_PRIVATE_KEY_PASSWORD);
			if(password != null && password.trim().length() > 0)
			{
				provider = new PrivatePasswordProvider(password.toCharArray());
			}
			else
			{
				String className = properties.getProperty(ConsoleLicenseGenerator.PROPERTY_PRIVATE_KEY_PASSWORD_PROVIDER);
				if(className != null && className.trim().length() > 0)
				{
					provider = this.getObjectAsClass(className, PasswordProvider.class);
				}
				else
				{
					throw new RuntimeException("Neither [" + ConsoleLicenseGenerator.PROPERTY_PRIVATE_KEY_PASSWORD +
											   "] nor [" + ConsoleLicenseGenerator.PROPERTY_PRIVATE_KEY_PASSWORD_PROVIDER +
											   "] properties specified.");
				}
			}
		}

		return provider;
	}

	private <T> T getObjectAsClass(String className, Class<T> castClass)
	{
		try
		{
			Class<?> objectClass = Class.forName(className);
			return objectClass.asSubclass(castClass).newInstance();
		}
		catch(ClassNotFoundException e)
		{
			throw new RuntimeException("The class [" + className + "] could not be located.");
		}
		catch(ClassCastException e)
		{
			throw new RuntimeException("The class [" + className + "] does not implement interface [" +
									   castClass.getCanonicalName() + "].");
		}
		catch(Exception e)
		{
			throw new RuntimeException("Unable to instantiate class [" + className + "].", e);
		}
	}

	protected void generateLicense() throws Exception
	{
		Properties properties = null;
		if(this.cli.hasOption("license"))
		{
			String value = this.cli.getOptionValue("license");
			if(value != null && value.trim().length() > 0)
				properties = this.readPropertiesFile(value);
		}

		License.Builder builder = new License.Builder();

		{
			String productKey = this.getLicenseProductKey(properties);
			if(productKey != null)
				builder.withProductKey(productKey);
		}

		{
			String holder = this.getLicenseHolder(properties);
			if(holder != null)
				builder.withHolder(holder);
		}

		{
			String issuer = this.getLicenseIssuer(properties);
			if(issuer != null)
				builder.withIssuer(issuer);
		}

		{
			String subject = this.getLicenseSubject(properties);
			if(subject != null)
				builder.withSubject(subject);
		}

		builder.withIssueDate(this.getLicenseIssueDate(properties));
		builder.withGoodAfterDate(this.getLicenseGoodAfterDate(properties));
		builder.withGoodBeforeDate(this.getLicenseGoodBeforeDate(properties));
		builder.withNumberOfLicenses(this.getLicenseNumberOfLicenses(properties));

		Map<String, Long> map = this.getLicenseFeatures(properties);
		for(String name : map.keySet())
		{
			Long expiration = map.get(name);

			if(expiration == null || expiration <= 0L)
				builder.withFeature(name);
			else
				builder.withFeature(name, expiration);
		}

		char[] password = this.getLicensePassword(properties);

		byte[] licenseData;

		License license = builder.build();
		if(password != null)
			licenseData = LicenseCreator.getInstance().signAndSerializeLicense(license, password);
		else
			licenseData = LicenseCreator.getInstance().signAndSerializeLicense(license);

		this.returnLicenseData(licenseData, properties);
	}

	private String getLicenseProductKey(Properties properties)
	{
		if(properties == null)
			return this.promptForString("Please enter a product key for this license (you can leave this blank): ");
		else
			return properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_PRODUCT_KEY);
	}

	private String getLicenseHolder(Properties properties)
	{
		if(properties == null)
			return this.promptForString("Please enter a holder for this license (you can leave this blank): ");
		else
			return properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_HOLDER);
	}

	private String getLicenseIssuer(Properties properties)
	{
		if(properties == null)
			return this.promptForString("Please enter an issuer for this license (you can leave this blank): ");
		else
			return properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_ISSUER);
	}

	private String getLicenseSubject(Properties properties)
	{
		if(properties == null)
			return this.promptForString("Please enter a subject for this license (you can leave this blank): ");
		else
			return properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_SUBJECT);
	}

	private long parseDate(String date)
	{
		if(date == null || date.trim().length() == 0)
			return 0L;
		
		try
		{
			return this.simpleDateFormat.parse(date).getTime();
		}
		catch(java.text.ParseException e)
		{
			return 0L;
		}
	}

	private long getLicenseIssueDate(Properties properties)
	{
		if(properties == null)
			return this.parseDate(this.promptForString("Please enter an issue date for this license " +
													   "(YYYY-MM-DD hh:mm:ss or blank): "));
		else
			return this.parseDate(properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_ISSUE_DATE));
	}

	private long getLicenseGoodAfterDate(Properties properties)
	{
		if(properties == null)
			return this.parseDate(this.promptForString("Please enter an activation/good-after date for this license " +
													   "(YYYY-MM-DD hh:mm:ss or blank): "));
		else
			return this.parseDate(properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_GOOD_AFTER_DATE));
	}

	private long getLicenseGoodBeforeDate(Properties properties)
	{
		if(properties == null)
			return this.parseDate(this.promptForString("Please enter an expiration date for this license " +
													   "(YYYY-MM-DD hh:mm:ss or blank): "));
		else
			return this.parseDate(properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_GOOD_BEFORE_DATE));
	}

	private int parseInt(String integer)
	{
		if(integer == null || integer.trim().length() == 0)
			return 0;

		try
		{
			return Integer.parseInt(integer);
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	private int getLicenseNumberOfLicenses(Properties properties)
	{
		if(properties == null)
			return this.parseInt(this.promptForString("Please enter a number of seats/licenses for this license " +
													  "(you can leave this blank): "));
		else
			return this.parseInt(properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_NUM_LICENSES));
	}

	public Map<String, Long> getLicenseFeatures(Properties properties)
	{
		Map<String, Long> map = new HashMap<String, Long>();

		if(properties == null)
		{
			String featureName = this.promptForString("Optionally enter the name/key of a feature you want to add to " +
													  "this license (you can leave this blank): ");

			while(featureName != null)
			{
				long expiration = this.parseDate(
						this.promptForString("Optionally enter an expiration date for feature [" + featureName +
											 "] (you can leave this blank): ")
				);

				map.put(featureName, expiration);

				featureName = this.promptForString("Enter another feature to add to this license (you can leave " +
												   "this blank): ");
			}
		}
		else
		{
			for(String key : properties.stringPropertyNames())
			{
				if(key.startsWith(ConsoleLicenseGenerator.PROPERTY_LICENSE_FEATURE_PREFIX))
				{
					String featureName = key.replace(ConsoleLicenseGenerator.PROPERTY_LICENSE_FEATURE_PREFIX, "");

					long expiration = this.parseDate(properties.getProperty(key));

					map.put(featureName, expiration);
				}
			}
		}

		return map;
	}

	private char[] getLicensePassword(Properties properties)
	{
		if(properties == null)
		{
			char[] password = this.promptForPassword("Please enter a password to encrypt the license with (if left " +
													 "blank, will use the private key password provider): ");
			return password == null || password.length == 0 ? null : password;
		}
		else
		{
			String password = properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_PASSWORD);
			return password == null || password.trim().length() == 0 ? null : password.toCharArray();
		}
	}

	private boolean promptToWriteLicenseToFile()
	{
		this.device.printOutLn("Would you like to...");
		this.device.printOutLn("    (1) Output the Base64-encoded license data to the screen?");
		this.device.printOutLn("    (2) Write the raw, binary license data to a file?");
		String input = this.device.readLine("Your selection (default 1)? ");
		this.device.printOutLn();

		return input != null && input.trim().equals("2");
	}

	private void returnLicenseData(byte[] licenseData, Properties properties) throws Exception
	{
		if(properties == null)
		{
			if(this.promptToWriteLicenseToFile())
			{
				String fileName = this.promptForString("Please enter the name of the file to save the license to: ");
				while(fileName == null)
				{
					fileName = this.promptForString("Invalid file name. Please enter the name of the file to save " +
													"the license to: ");
				}

				File file = new File(fileName);
				FileUtils.writeByteArrayToFile(file, licenseData);
			}
			else
			{
				this.device.printOutLn("License Data:");
				this.device.printOutLn(new String(Base64.encodeBase64(licenseData)));
			}
		}
		else
		{
			String fileName = properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_FILE);
			if(fileName != null && fileName.trim().length() > 0)
			{
				File file = new File(fileName);
				FileUtils.writeByteArrayToFile(file, licenseData);
			}
			else
			{
				this.device.printOut(Base64.encodeBase64(licenseData));
			}
		}
	}

	public void run(String[] arguments)
	{
		this.processCommandLineOptions(arguments);

		try
		{
			this.initializeLicenseCreator();

			this.generateLicense();
		}
		catch(KeyNotFoundException e)
		{
			this.device.printErrLn(e.getLocalizedMessage() + " Correct the error and try again.");
			this.device.exit(51);
			return;
		}
		catch(ObjectSerializationException e)
		{
			this.device.printErrLn(e.getLocalizedMessage() + " Correct the error and try again.");
			this.device.exit(52);
			return;
		}
		catch(AlgorithmNotSupportedException e)
		{
			this.device.printErrLn(e.getLocalizedMessage() + " Contact your system administrator for assistance.");
			this.device.exit(41);
			return;
		}
		catch(InappropriateKeyException e)
		{
			this.device.printErrLn(e.getLocalizedMessage() + " Contact your system administrator for assistance.");
			this.device.exit(42);
			return;
		}
		catch(InappropriateKeySpecificationException e)
		{
			this.device.printErrLn(e.getLocalizedMessage() + " Contact your system administrator for assistance.");
			this.device.exit(43);
			return;
		}
		catch(InterruptedException e)
		{
			// in theory, this error wouldn't actually get reached in this circumstance,
			// but we'll catch it just in case
			this.device.printErrLn("The system was interrupted while waiting for events to complete.");
			this.device.exit(44);
			return;
		}
		catch(IOException e)
		{
			this.device.printErrLn("An error occurred writing or reading files from the system. Analyze the error " +
					"below to determine what went wrong and fix it!");
			this.device.printErrLn(e.toString());
			e.printStackTrace();
			this.device.exit(21);
			return;
		}
		catch(Throwable t)
		{
			this.device.printErrLn(t.toString());
			t.printStackTrace();
			this.device.exit(-1);
			return;
		}

		this.device.exit(0);
	}

	public static void main(String[] arguments)
	{
		TextInterfaceDevice device = TextInterfaceDevice.CONSOLE;

		ConsoleUtilities.configureInterfaceDevice(device);

		new ConsoleLicenseGenerator(device, new GnuParser()).run(arguments);
	}
}
