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
package io.oddsource.java.licensing.licensor.interfaces.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import io.oddsource.java.licensing.License;
import io.oddsource.java.licensing.LicensingCharsets;
import io.oddsource.java.licensing.encryption.FilePrivateKeyDataProvider;
import io.oddsource.java.licensing.encryption.PasswordProvider;
import io.oddsource.java.licensing.encryption.PrivateKeyDataProvider;
import io.oddsource.java.licensing.exception.AlgorithmNotSupportedException;
import io.oddsource.java.licensing.exception.InappropriateKeyException;
import io.oddsource.java.licensing.exception.InappropriateKeySpecificationException;
import io.oddsource.java.licensing.exception.KeyNotFoundException;
import io.oddsource.java.licensing.exception.ObjectSerializationException;
import io.oddsource.java.licensing.licensor.LicenseCreator;
import io.oddsource.java.licensing.licensor.LicenseCreatorProperties;
import io.oddsource.java.licensing.licensor.interfaces.cli.spi.TextInterfaceDevice;

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
 * <code>io.oddsource.java.licensing.privateKeyFile=[The path to the file containing the encrypted private key; either
 * this or io.oddsource.java.licensing.privateKeyClass must be specified.]<br />
 * io.oddsource.java.licensing.privateKeyProvider=[The fully-qualified (canonical) name of the implementation of {@link
 * PrivateKeyDataProvider}; either this or io.oddsource.java.licensing.privateKeyFile must be specified.]<br />
 * io.oddsource.java.licensing.privateKeyPassword=[The password for decrypting the private key; either this or
 * io.oddsource.java.licensing.privateKeyPasswordClass must be specified.]<br />
 * io.oddsource.java.licensing.privateKeyPasswordProvider=[The fully-qualified (canonical) name of the implementation of
 * {@link PasswordProvider} that provides the private key password; either this or
 * io.oddsource.java.licensing.privateKeyPassword must be specified]</code><br />
 * <br />
 * <b>License Properties File Properties</b> (Specify only those that you wish to include in the license)<br />
 * <code>io.oddsource.java.licensing.productKey=[The product key/serial number (String) for this license]
 * io.oddsource.java.licensing.holder=[The holder (String) for this license]
 * io.oddsource.java.licensing.issuer=[The issuer (String) for this license]
 * io.oddsource.java.licensing.subject=[The subject (String) for this license]
 * io.oddsource.java.licensing.issueDate=[The date this license was issued (defaults to the current date/time), format:
 * YYYY-MM-DD hh:mm:ss in 24-hour time]
 * io.oddsource.java.licensing.goodAfterDate=[The date this license becomes active, format: YYYY-MM-DD hh:mm:ss in
 * 24-hour time]
 * io.oddsource.java.licensing.goodBeforeDate=[The date this license expires, format: format: YYYY-MM-DD hh:mm:ss in
 * 24-hour time]
 * io.oddsource.java.licensing.numberOfLicenses=[The seats/users/number of licenses this license is good for]
 * io.oddsource.java.licensing.features.MY_FEATURE=[Feature expiration date for "MY_FEATURE", format: YYYY-MM-DD
 * hh:mm:ss in 24-hour time or "none"]
 * io.oddsource.java.licensing.features.ANOTHER_FEATURE=[Feature expiration date for "ANOTHER_FEATURE", format:
 * YYYY-MM-DD hh:mm:ss in 24-hour time or "none"]</code><br />
 * <br />
 * You can specify any arbitrary number of features (and their expiration date) using multiple properties that start
 * with <code>io.oddsource.java.licensing.features</code>. The last part of the property key should be the
 * feature name. The value should either be "none" or the date the feature expires (YYYY-MM-DD hh:mm:ss in 24-hour
 * time).<br />
 * <br />
 * <b>NOTE:</b> The default behavior is to encrypt the resulting license with the password or password provider
 * specified in the config properties file. To override this and use a different password to encrypt the license,
 * add <code>io.oddsource.java.licensing.password=[encryption password]</code> to the license properties
 * file.<br />
 * <br />
 * <b>NOTE:</b> The default behavior is to print the resulting license, Base64-encoded, to standard out. Instead, you
 * can write the raw, binary license data to a specific file with the property
 * <code>io.oddsource.java.licensing.licenseFile=[file name]</code>.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("Duplicates")
public class ConsoleLicenseGenerator
{
    public static final String PROPERTY_PRIVATE_KEY_FILE = "io.oddsource.java.licensing.privateKeyFile";

    public static final String PROPERTY_PRIVATE_KEY_PROVIDER = "io.oddsource.java.licensing.privateKeyProvider";

    public static final String PROPERTY_PRIVATE_KEY_PASSWORD = "io.oddsource.java.licensing.privateKeyPassword";

    public static final String PROPERTY_PRIVATE_KEY_PASSWORD_PROVIDER =
        "io.oddsource.java.licensing.privateKeyPasswordProvider";

    public static final String PROPERTY_LICENSE_PRODUCT_KEY = "io.oddsource.java.licensing.productKey";

    public static final String PROPERTY_LICENSE_HOLDER = "io.oddsource.java.licensing.holder";

    public static final String PROPERTY_LICENSE_ISSUER = "io.oddsource.java.licensing.issuer";

    public static final String PROPERTY_LICENSE_SUBJECT = "io.oddsource.java.licensing.subject";

    public static final String PROPERTY_LICENSE_ISSUE_DATE = "io.oddsource.java.licensing.issueDate";

    public static final String PROPERTY_LICENSE_GOOD_AFTER_DATE = "io.oddsource.java.licensing.goodAfterDate";

    public static final String PROPERTY_LICENSE_GOOD_BEFORE_DATE = "io.oddsource.java.licensing.goodBeforeDate";

    public static final String PROPERTY_LICENSE_NUM_LICENSES = "io.oddsource.java.licensing.numberOfLicenses";

    public static final String PROPERTY_LICENSE_FEATURE_PREFIX = "io.oddsource.java.licensing.features.";

    public static final String PROPERTY_LICENSE_FILE = "io.oddsource.java.licensing.licenseFile";

    public static final String PROPERTY_LICENSE_PASSWORD = "io.oddsource.java.licensing.password";

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

    private static final Option HELP = Option.builder("help").desc("Display this help message").hasArg(false).build();

    private static final Option CONFIG = Option.builder("config").argName("file").hasArg(true).
        desc("Specify the .properties file that configures this generator").required(false).build();

    private static final Option LICENSE = Option.builder("license").argName("file").hasArg(true).
        desc("Specify the .properties file that contains the data for this license").required(false).build();

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final TextInterfaceDevice device;

    private final CommandLineParser cliParser;

    protected CommandLine cli = null;

    protected ConsoleLicenseGenerator(
        final TextInterfaceDevice textInterfaceDevice,
        final CommandLineParser cliParser
    )
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

    protected void processCommandLineOptions(final String[] arguments)
    {
        final Options firstParseOptions = new Options();
        firstParseOptions.addOption(HELP);

        final Options options = new Options();
        options.addOption(HELP).addOption(CONFIG).addOption(LICENSE);

        try
        {
            this.cli = this.cliParser.parse(firstParseOptions, arguments, true);

            if(this.cli.hasOption("help"))
            {
                final HelpFormatter formatter = new HelpFormatter();
                this.printHelp(formatter, options);

                this.device.exit(0);
            }
            else
            {
                this.cli = this.cliParser.parse(options, arguments);
            }
        }
        catch(final ParseException e)
        {
            this.device.printErrLn(e.getLocalizedMessage());

            final HelpFormatter formatter = new HelpFormatter();
            this.printHelp(formatter, options);

            this.device.exit(1);
        }
    }

    private void printHelp(final HelpFormatter formatter, final Options options)
    {
        final OutputStreamWriter streamWriter = new OutputStreamWriter(this.device.out(), LicensingCharsets.UTF_8);
        final PrintWriter printWriter = new PrintWriter(streamWriter);
        formatter.printHelp(printWriter, CLI_WIDTH, USAGE, null, options, 1, 3, null, false);
        printWriter.close();
        try
        {
            streamWriter.close();
        }
        catch(final IOException e)
        {
            e.printStackTrace(this.device.err());
        }
    }

    protected Properties readPropertiesFile(final String fileName) throws Exception
    {
        final File file = new File(fileName);

        if(!file.exists())
        {
            throw new FileNotFoundException("The file [" + fileName + "] does not exist.");
        }
        if(!file.canRead())
        {
            throw new IOException("The file [" + fileName + "] is not readable.");
        }

        try(
            FileInputStream stream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(stream, LicensingCharsets.UTF_8)
        )
        {
            final Properties properties = new Properties();
            properties.load(reader);

            return properties;
        }
    }

    protected String promptForString(final String message)
    {
        final String input = this.device.readLine(message);
        this.device.printOutLn();

        return input != null && input.trim().length() > 0 ? input.trim() : null;
    }

    protected char[] promptForPassword(final String message)
    {
        final char[] input = this.device.readPassword(message);
        this.device.printOutLn();

        return input != null && input.length > 0 ? input : null;
    }

    protected void initializeLicenseCreator() throws Exception
    {
        Properties properties = null;
        if(this.cli.hasOption("config"))
        {
            final String value = this.cli.getOptionValue("config");
            if(value != null && value.trim().length() > 0)
            {
                properties = this.readPropertiesFile(value);
            }
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
        final String input = this.device.readLine("Your selection (default 1)? ");
        this.device.printOutLn();

        return input != null && input.trim().equals("2");
    }

    private PrivateKeyDataProvider getPrivateKeyDataProvider(final Properties properties) throws FileNotFoundException
    {
        final PrivateKeyDataProvider provider;

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
            final String fileName = properties.getProperty(ConsoleLicenseGenerator.PROPERTY_PRIVATE_KEY_FILE);
            if(fileName != null && fileName.trim().length() > 0)
            {
                final File file = new File(fileName);
                if(!file.exists() || !file.canRead())
                {
                    throw new FileNotFoundException("The private key file [" + file.getAbsolutePath() +
                                                    "] does not exist or cannot be read.");
                }

                provider = new FilePrivateKeyDataProvider(file);
            }
            else
            {
                final String className = properties.getProperty(ConsoleLicenseGenerator.PROPERTY_PRIVATE_KEY_PROVIDER);
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
        final String input = this.device.readLine("Your selection (default 1)? ");
        this.device.printOutLn();

        return input != null && input.trim().equals("2");
    }

    private static final class PrivatePasswordProvider implements PasswordProvider
    {
        private final char[] password;

        PrivatePasswordProvider(final char[] password)
        {
            this.password = password;
        }

        @Override
        public char[] getPassword()
        {
            return this.password;
        }
    }

    private PasswordProvider getPrivateKeyPasswordProvider(final Properties properties)
    {
        final PasswordProvider provider;

        if(properties == null)
        {
            if(!this.promptToUsePasswordProviderClass())
            {
                char[] password = this.promptForPassword("Please type the password for the private key: ");
                while(password == null)
                {
                    password =
                        this.promptForPassword("Invalid password. Please type the password for the private key: ");
                }

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
            final String password = properties.getProperty(ConsoleLicenseGenerator.PROPERTY_PRIVATE_KEY_PASSWORD);
            if(password != null && password.trim().length() > 0)
            {
                provider = new PrivatePasswordProvider(password.toCharArray());
            }
            else
            {
                final String className =
                    properties.getProperty(ConsoleLicenseGenerator.PROPERTY_PRIVATE_KEY_PASSWORD_PROVIDER);
                if(className != null && className.trim().length() > 0)
                {
                    provider = this.getObjectAsClass(className, PasswordProvider.class);
                }
                else
                {
                    throw new RuntimeException("Neither [" + ConsoleLicenseGenerator.PROPERTY_PRIVATE_KEY_PASSWORD +
                                               "] nor [" +
                                               ConsoleLicenseGenerator.PROPERTY_PRIVATE_KEY_PASSWORD_PROVIDER +
                                               "] properties specified.");
                }
            }
        }

        return provider;
    }

    private <T> T getObjectAsClass(final String className, final Class<T> castClass)
    {
        try
        {
            final Class<?> objectClass = Class.forName(className);
            return objectClass.asSubclass(castClass).newInstance();
        }
        catch(final ClassNotFoundException e)
        {
            throw new RuntimeException("The class [" + className + "] could not be located.");
        }
        catch(final ClassCastException e)
        {
            throw new RuntimeException("The class [" + className + "] does not implement interface [" +
                                       castClass.getCanonicalName() + "].");
        }
        catch(final Exception e)
        {
            throw new RuntimeException("Unable to instantiate class [" + className + "].", e);
        }
    }

    protected void generateLicense() throws Exception
    {
        Properties properties = null;
        if(this.cli.hasOption("license"))
        {
            final String value = this.cli.getOptionValue("license");
            if(value != null && value.trim().length() > 0)
            {
                properties = this.readPropertiesFile(value);
            }
        }

        final License.Builder builder = new License.Builder();

        {
            final String productKey = this.getLicenseProductKey(properties);
            if(productKey != null)
            {
                builder.withProductKey(productKey);
            }
        }

        {
            final String holder = this.getLicenseHolder(properties);
            if(holder != null)
            {
                builder.withHolder(holder);
            }
        }

        {
            final String issuer = this.getLicenseIssuer(properties);
            if(issuer != null)
            {
                builder.withIssuer(issuer);
            }
        }

        {
            final String subject = this.getLicenseSubject(properties);
            if(subject != null)
            {
                builder.withSubject(subject);
            }
        }

        builder.withIssueDate(this.getLicenseIssueDate(properties));
        builder.withGoodAfterDate(this.getLicenseGoodAfterDate(properties));
        builder.withGoodBeforeDate(this.getLicenseGoodBeforeDate(properties));
        builder.withNumberOfLicenses(this.getLicenseNumberOfLicenses(properties));

        final Map<String, Long> map = this.getLicenseFeatures(properties);
        for(final Map.Entry<String, Long> entry : map.entrySet())
        {
            final String name = entry.getKey();
            final Long expiration = map.get(name);

            if(expiration == null || expiration <= 0L)
            {
                builder.addFeature(name);
            }
            else
            {
                builder.addFeature(name, expiration);
            }
        }

        final char[] password = this.getLicensePassword(properties);

        final byte[] licenseData;

        final License license = builder.build();
        if(password != null)
        {
            licenseData = LicenseCreator.getInstance().signAndSerializeLicense(license, password);
        }
        else
        {
            licenseData = LicenseCreator.getInstance().signAndSerializeLicense(license);
        }

        this.returnLicenseData(licenseData, properties);
    }

    private String getLicenseProductKey(final Properties properties)
    {
        if(properties == null)
        {
            return this.promptForString("Please enter a product key for this license (you can leave this blank): ");
        }
        else
        {
            return properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_PRODUCT_KEY);
        }
    }

    private String getLicenseHolder(final Properties properties)
    {
        if(properties == null)
        {
            return this.promptForString("Please enter a holder for this license (you can leave this blank): ");
        }
        else
        {
            return properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_HOLDER);
        }
    }

    private String getLicenseIssuer(final Properties properties)
    {
        if(properties == null)
        {
            return this.promptForString("Please enter an issuer for this license (you can leave this blank): ");
        }
        else
        {
            return properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_ISSUER);
        }
    }

    private String getLicenseSubject(final Properties properties)
    {
        if(properties == null)
        {
            return this.promptForString("Please enter a subject for this license (you can leave this blank): ");
        }
        else
        {
            return properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_SUBJECT);
        }
    }

    private long parseDate(final String date)
    {
        if(date == null || date.trim().length() == 0)
        {
            return 0L;
        }

        try
        {
            return this.simpleDateFormat.parse(date).getTime();
        }
        catch(final java.text.ParseException e)
        {
            return 0L;
        }
    }

    private long getLicenseIssueDate(final Properties properties)
    {
        if(properties == null)
        {
            return this.parseDate(this.promptForString("Please enter an issue date for this license " +
                                                       "(YYYY-MM-DD hh:mm:ss or blank): "));
        }
        else
        {
            return this.parseDate(properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_ISSUE_DATE));
        }
    }

    private long getLicenseGoodAfterDate(final Properties properties)
    {
        if(properties == null)
        {
            return this.parseDate(this.promptForString("Please enter an activation/good-after date for this license " +
                                                       "(YYYY-MM-DD hh:mm:ss or blank): "));
        }
        else
        {
            return this.parseDate(properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_GOOD_AFTER_DATE));
        }
    }

    private long getLicenseGoodBeforeDate(final Properties properties)
    {
        if(properties == null)
        {
            return this.parseDate(this.promptForString("Please enter an expiration date for this license " +
                                                       "(YYYY-MM-DD hh:mm:ss or blank): "));
        }
        else
        {
            return this.parseDate(properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_GOOD_BEFORE_DATE));
        }
    }

    private int parseInt(final String integer)
    {
        if(integer == null || integer.trim().length() == 0)
        {
            return 0;
        }

        try
        {
            return Integer.parseInt(integer);
        }
        catch(final Exception e)
        {
            return 0;
        }
    }

    private int getLicenseNumberOfLicenses(final Properties properties)
    {
        if(properties == null)
        {
            return this.parseInt(this.promptForString("Please enter a number of seats/licenses for this license " +
                                                      "(you can leave this blank): "));
        }
        else
        {
            return this.parseInt(properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_NUM_LICENSES));
        }
    }

    public Map<String, Long> getLicenseFeatures(final Properties properties)
    {
        final Map<String, Long> map = new HashMap<>();

        if(properties == null)
        {
            String featureName = this.promptForString("Optionally enter the name/key of a feature you want to add to " +
                                                      "this license (you can leave this blank): ");

            while(featureName != null)
            {
                final long expiration = this.parseDate(
                    this.promptForString("Optionally enter an expiration date for feature [" + featureName +
                                         "] (YYYY-MM-DD hh:mm:ss or blank): ")
                );

                map.put(featureName, expiration);

                featureName = this.promptForString("Enter another feature to add to this license (you can leave " +
                                                   "this blank): ");
            }
        }
        else
        {
            for(final String key : properties.stringPropertyNames())
            {
                if(key.startsWith(ConsoleLicenseGenerator.PROPERTY_LICENSE_FEATURE_PREFIX))
                {
                    final String featureName = key.replace(ConsoleLicenseGenerator.PROPERTY_LICENSE_FEATURE_PREFIX, "");

                    final long expiration = this.parseDate(properties.getProperty(key));

                    map.put(featureName, expiration);
                }
            }
        }

        return map;
    }

    private char[] getLicensePassword(final Properties properties)
    {
        if(properties == null)
        {
            final char[] password = this.device.promptForValidPassword(
                0,
                32,
                "the license with (if left blank, will use " +
                "the private key password provider)"
            );
            this.device.printOutLn();
            return password == null || password.length == 0 ? null : password;
        }
        else
        {
            final String password = properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_PASSWORD);
            return password == null || password.trim().length() == 0 ? null : password.toCharArray();
        }
    }

    private boolean promptToWriteLicenseToFile()
    {
        this.device.printOutLn("Would you like to...");
        this.device.printOutLn("    (1) Output the Base64-encoded license data to the screen?");
        this.device.printOutLn("    (2) Write the raw, binary license data to a file?");
        final String input = this.device.readLine("Your selection (default 1)? ");
        this.device.printOutLn();

        return input != null && input.trim().equals("2");
    }

    private void returnLicenseData(final byte[] licenseData, final Properties properties) throws Exception
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

                final File file = new File(fileName);
                FileUtils.writeByteArrayToFile(file, licenseData);
            }
            else
            {
                this.device.printOutLn("License Data:");
                this.device.printOutLn(new String(Base64.encodeBase64(licenseData), LicensingCharsets.UTF_8));
            }
        }
        else
        {
            final String fileName = properties.getProperty(ConsoleLicenseGenerator.PROPERTY_LICENSE_FILE);
            if(fileName != null && fileName.trim().length() > 0)
            {
                final File file = new File(fileName);
                FileUtils.writeByteArrayToFile(file, licenseData);
            }
            else
            {
                this.device.printOut(new String(Base64.encodeBase64(licenseData), LicensingCharsets.UTF_8));
            }
        }
    }

    public void run(final String[] arguments)
    {
        this.processCommandLineOptions(arguments);

        try
        {
            this.initializeLicenseCreator();

            this.generateLicense();
        }
        catch(final KeyNotFoundException e)
        {
            this.device.printErrLn(e.getLocalizedMessage() + " Correct the error and try again.");
            this.device.exit(51);
            return;
        }
        catch(final ObjectSerializationException e)
        {
            this.device.printErrLn(e.getLocalizedMessage() + " Correct the error and try again.");
            this.device.exit(52);
            return;
        }
        catch(final AlgorithmNotSupportedException e)
        {
            this.device.printErrLn(e.getLocalizedMessage() + " Contact your system administrator for assistance.");
            this.device.exit(41);
            return;
        }
        catch(final InappropriateKeyException e)
        {
            this.device.printErrLn(e.getLocalizedMessage() + " Contact your system administrator for assistance.");
            this.device.exit(42);
            return;
        }
        catch(final InappropriateKeySpecificationException e)
        {
            this.device.printErrLn(e.getLocalizedMessage() + " Contact your system administrator for assistance.");
            this.device.exit(43);
            return;
        }
        catch(final InterruptedException e)
        {
            // in theory, this error wouldn't actually get reached in this circumstance,
            // but we'll catch it just in case
            this.device.printErrLn("The system was interrupted while waiting for events to complete.");
            this.device.exit(44);
            return;
        }
        catch(final IOException e)
        {
            this.device.printErrLn("An error occurred writing or reading files from the system. Analyze the error " +
                                   "below to determine what went wrong and fix it!");
            this.device.printErrLn(e.toString());
            e.printStackTrace();
            this.device.exit(21);
            return;
        }
        catch(final Throwable t)
        {
            this.device.printErrLn(t.toString());
            t.printStackTrace();
            this.device.exit(-1);
            return;
        }

        this.device.exit(0);
    }

    public static void main(final String... arguments)
    {
        final TextInterfaceDevice device = TextInterfaceDevice.CONSOLE;

        ConsoleUtilities.configureInterfaceDevice(device);

        new ConsoleLicenseGenerator(device, new DefaultParser()).run(arguments);
    }
}
