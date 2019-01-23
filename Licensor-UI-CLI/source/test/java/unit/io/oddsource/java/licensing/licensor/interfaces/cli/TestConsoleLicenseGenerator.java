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

import io.oddsource.java.licensing.License;
import io.oddsource.java.licensing.MockLicenseHelper;
import io.oddsource.java.licensing.ObjectSerializer;
import io.oddsource.java.licensing.SignedLicense;
import io.oddsource.java.licensing.encryption.Encryptor;
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
import io.oddsource.java.licensing.mock.MockEmbeddedPrivateKeyDataProvider;
import io.oddsource.java.licensing.mock.MockFilePrivateKeyDataProvider;
import io.oddsource.java.licensing.mock.MockPasswordProvider;
import io.oddsource.java.mock.MockPermissiveSecurityManager;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Test class for ConsoleLicenseGenerator.
 */
public class TestConsoleLicenseGenerator
{
    private static final String LF = System.getProperty("line.separator");

    private ConsoleLicenseGenerator console;

    private TextInterfaceDevice device;

    @Before
    public void setUp()
    {
        this.device = EasyMock.createMock(TextInterfaceDevice.class);

        this.console = new ConsoleLicenseGenerator(this.device, new DefaultParser()) {
            @Override
            protected void finalize()
            {

            }
        };
    }

    @After
    public void tearDown()
    {
        EasyMock.verify(this.device);
    }

    @Test
    public void testProcessCommandLineOptions01() throws ParseException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream printer = new PrintStream(stream);

        EasyMock.expect(this.device.out()).andReturn(printer);
        this.device.exit(0);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device);

        this.console.processCommandLineOptions(new String[] { "-help" });

        String output = stream.toString();

        assertNotNull("There should be output.", output);
        assertTrue("The output should have length.", output.length() > 0);
        assertEquals(
            "The output is not correct.",
            "usage:  ConsoleLicenseGenerator -help" + LF +
            " ConsoleLicenseGenerator" + LF +
            " ConsoleLicenseGenerator -config <file>" + LF +
            " ConsoleLicenseGenerator -license <file>" + LF +
            " ConsoleLicenseGenerator -config <file> -license <file>" + LF +
            LF +
            " The ConsoleLicenseGenerator expects to be passed the path to two properties files, or one of them, or" + LF +
            "        neither. The \"config\" properties file contains information necessary to generate all licenses" + LF +
            "        (key paths, passwords, etc.) and generally will not need to change. The \"license\" properties file" + LF +
            "        contains all of the information you need to generate this particular license. See the Javadoc API" + LF +
            "        documentation for information about the contents of these two files." + LF +
            LF +
            " If you do not specify the \"config\" properties file, you will be prompted to provide the values that were" + LF +
            "        expected in that file. Likewise, if you do not specify the \"license\" properties file, you will be" + LF +
            "        prompted to provide the values that were expected in that file." + LF +
            " -config <file>    Specify the .properties file that configures this generator" + LF +
            " -help             Display this help message" + LF +
            " -license <file>   Specify the .properties file that contains the data for this license" + LF,
            output
        );
    }

    @Test
    public void testProcessCommandLineOptions02() throws ParseException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream printer = new PrintStream(stream);

        this.device.printErrLn("Unrecognized option: -badOption");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(printer);
        this.device.exit(1);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device);

        this.console.processCommandLineOptions(new String[] { "-badOption" });

        String output = stream.toString();

        assertNotNull("There should be output.", output);
        assertTrue("The output should have length.", output.length() > 0);
        assertEquals(
            "The output is not correct.",
            "usage:  ConsoleLicenseGenerator -help" + LF +
            " ConsoleLicenseGenerator" + LF +
            " ConsoleLicenseGenerator -config <file>" + LF +
            " ConsoleLicenseGenerator -license <file>" + LF +
            " ConsoleLicenseGenerator -config <file> -license <file>" + LF +
            LF +
            " The ConsoleLicenseGenerator expects to be passed the path to two properties files, or one of them, or" + LF +
            "        neither. The \"config\" properties file contains information necessary to generate all licenses" + LF +
            "        (key paths, passwords, etc.) and generally will not need to change. The \"license\" properties file" + LF +
            "        contains all of the information you need to generate this particular license. See the Javadoc API" + LF +
            "        documentation for information about the contents of these two files." + LF +
            LF +
            " If you do not specify the \"config\" properties file, you will be prompted to provide the values that were" + LF +
            "        expected in that file. Likewise, if you do not specify the \"license\" properties file, you will be" + LF +
            "        prompted to provide the values that were expected in that file." + LF +
            " -config <file>    Specify the .properties file that configures this generator" + LF +
            " -help             Display this help message" + LF +
            " -license <file>   Specify the .properties file that contains the data for this license" + LF,
            output
        );
    }

    @Test
    public void testProcessCommandLineOptions03() throws ParseException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream printer = new PrintStream(stream);

        this.device.printErrLn("Missing argument for option: config");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(printer);
        this.device.exit(1);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device);

        this.console.processCommandLineOptions(new String[] { "-config" });

        String output = stream.toString();

        assertNotNull("There should be output.", output);
        assertTrue("The output should have length.", output.length() > 0);
        assertEquals(
            "The output is not correct.",
            "usage:  ConsoleLicenseGenerator -help" + LF +
            " ConsoleLicenseGenerator" + LF +
            " ConsoleLicenseGenerator -config <file>" + LF +
            " ConsoleLicenseGenerator -license <file>" + LF +
            " ConsoleLicenseGenerator -config <file> -license <file>" + LF +
            LF +
            " The ConsoleLicenseGenerator expects to be passed the path to two properties files, or one of them, or" + LF +
            "        neither. The \"config\" properties file contains information necessary to generate all licenses" + LF +
            "        (key paths, passwords, etc.) and generally will not need to change. The \"license\" properties file" + LF +
            "        contains all of the information you need to generate this particular license. See the Javadoc API" + LF +
            "        documentation for information about the contents of these two files." + LF +
            LF +
            " If you do not specify the \"config\" properties file, you will be prompted to provide the values that were" + LF +
            "        expected in that file. Likewise, if you do not specify the \"license\" properties file, you will be" + LF +
            "        prompted to provide the values that were expected in that file." + LF +
            " -config <file>    Specify the .properties file that configures this generator" + LF +
            " -help             Display this help message" + LF +
            " -license <file>   Specify the .properties file that contains the data for this license" + LF,
            output
        );
    }

    @Test
    public void testProcessCommandLineOptions04() throws ParseException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream printer = new PrintStream(stream);

        this.device.printErrLn("Missing argument for option: license");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(printer);
        this.device.exit(1);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device);

        this.console.processCommandLineOptions(new String[] { "-license" });

        String output = stream.toString();

        assertNotNull("There should be output.", output);
        assertTrue("The output should have length.", output.length() > 0);
        assertEquals(
            "The output is not correct.",
            "usage:  ConsoleLicenseGenerator -help" + LF +
            " ConsoleLicenseGenerator" + LF +
            " ConsoleLicenseGenerator -config <file>" + LF +
            " ConsoleLicenseGenerator -license <file>" + LF +
            " ConsoleLicenseGenerator -config <file> -license <file>" + LF +
            LF +
            " The ConsoleLicenseGenerator expects to be passed the path to two properties files, or one of them, or" + LF +
            "        neither. The \"config\" properties file contains information necessary to generate all licenses" + LF +
            "        (key paths, passwords, etc.) and generally will not need to change. The \"license\" properties file" + LF +
            "        contains all of the information you need to generate this particular license. See the Javadoc API" + LF +
            "        documentation for information about the contents of these two files." + LF +
            LF +
            " If you do not specify the \"config\" properties file, you will be prompted to provide the values that were" + LF +
            "        expected in that file. Likewise, if you do not specify the \"license\" properties file, you will be" + LF +
            "        prompted to provide the values that were expected in that file." + LF +
            " -config <file>    Specify the .properties file that configures this generator" + LF +
            " -help             Display this help message" + LF +
            " -license <file>   Specify the .properties file that contains the data for this license" + LF,
            output
        );
    }

    @Test
    public void testProcessCommandLineOptions05() throws ParseException
    {
        EasyMock.replay(this.device);

        this.console.processCommandLineOptions(new String[] { "-config", "config.properties" });

        assertNotNull("There should be a cli value.", this.console.cli);
    }

    @Test
    public void testProcessCommandLineOptions06() throws ParseException
    {
        EasyMock.replay(this.device);

        this.console.processCommandLineOptions(new String[] { "-license", "license.properties" });

        assertNotNull("There should be a cli value.", this.console.cli);
    }

    @Test
    public void testProcessCommandLineOptions07() throws ParseException
    {
        EasyMock.replay(this.device);

        this.console.processCommandLineOptions(
                new String[] { "-config", "config.properties", "-license", "license.properties" }
        );

        assertNotNull("There should be a cli value.", this.console.cli);
    }

    private void resetLicenseCreator()
    {
        try
        {
            Field field = LicenseCreator.class.getDeclaredField("instance");
            field.setAccessible(true);
            field.set(null, null);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private PrivateKeyDataProvider getPrivateKeyDataProvider()
    {
        try
        {
            Field field = LicenseCreatorProperties.class.getDeclaredField("privateKeyDataProvider");
            field.setAccessible(true);
            return (PrivateKeyDataProvider)field.get(null);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private PasswordProvider getPasswordProvider()
    {
        try
        {
            Field field = LicenseCreatorProperties.class.getDeclaredField("privateKeyPasswordProvider");
            field.setAccessible(true);
            return (PasswordProvider)field.get(null);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testInitializeLicenseCreator01() throws Exception
    {
        this.resetLicenseCreator();

        String fileName = "testInitializeLicenseCreator01.properties";
        File file = new File(fileName);
        if(file.exists())
            FileUtils.forceDelete(file);

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("config")).andReturn(true);
        EasyMock.expect(this.console.cli.getOptionValue("config")).andReturn(fileName);

        EasyMock.replay(this.console.cli, this.device);

        try
        {
            this.console.initializeLicenseCreator();
            fail("Expected exception FileNotFoundException.");
        }
        catch(FileNotFoundException ignore) { }
        finally
        {
            this.resetLicenseCreator();

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    @Ignore("canRead()/canWrite() do not work on Win; setReadable()/setWritable() do not work on some Macs.")
    public void testInitializeLicenseCreator02() throws Exception
    {
        this.resetLicenseCreator();

        String fileName = "testInitializeLicenseCreator02.properties";
        File file = new File(fileName);
        file = file.getCanonicalFile();
        if(file.exists())
            FileUtils.forceDelete(file);

        FileUtils.writeStringToFile(file, "test", "UTF-8");

        assertTrue("Setting the file to not readable should have returned true.", file.setReadable(false, false));
        assertTrue("The file should be writable.", file.canWrite());
        assertFalse("The file should not be readable.", file.canRead());

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("config")).andReturn(true);
        EasyMock.expect(this.console.cli.getOptionValue("config")).andReturn(fileName);

        EasyMock.replay(this.console.cli, this.device);

        try
        {
            this.console.initializeLicenseCreator();
            fail("Expected exception IOException.");
        }
        catch(IOException ignore) { }
        finally
        {
            this.resetLicenseCreator();

            FileUtils.forceDelete(file);

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    public void testInitializeLicenseCreator03() throws Exception
    {
        this.resetLicenseCreator();

        String fileName = "testInitializeLicenseCreator03.properties";
        File file = new File(fileName);
        if(file.exists())
            FileUtils.forceDelete(file);

        FileUtils.writeStringToFile(
            file,
            "io.oddsource.java.licensing.privateKeyFile=testInitializeLicenseCreator03.key\r\n" +
            "io.oddsource.java.licensing.privateKeyPassword=testPassword03",
            "UTF-8"
        );

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("config")).andReturn(true);
        EasyMock.expect(this.console.cli.getOptionValue("config")).andReturn(fileName);

        EasyMock.replay(this.console.cli, this.device);

        try
        {
            this.console.initializeLicenseCreator();
            fail("Expected exception FileNotFoundException.");
        }
        catch(FileNotFoundException e) { }
        finally
        {
            this.resetLicenseCreator();

            FileUtils.forceDelete(file);

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    public void testInitializeLicenseCreator04() throws Exception
    {
        this.resetLicenseCreator();

        String fileName = "testInitializeLicenseCreator04.properties";
        File file = new File(fileName);
        if(file.exists())
            FileUtils.forceDelete(file);

        File keyFile = new File("testInitializeLicenseCreator04.key");
        FileUtils.writeStringToFile(keyFile, "aKey", "UTF-8");

        FileUtils.writeStringToFile(
            file,
            "io.oddsource.java.licensing.privateKeyFile=testInitializeLicenseCreator04.key\r\n" +
            "io.oddsource.java.licensing.privateKeyPassword=testPassword04",
            "UTF-8"
        );

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("config")).andReturn(true);
        EasyMock.expect(this.console.cli.getOptionValue("config")).andReturn(fileName);

        EasyMock.replay(this.console.cli, this.device);

        try
        {
            this.console.initializeLicenseCreator();

            PrivateKeyDataProvider key = this.getPrivateKeyDataProvider();
            assertNotNull("The key provider should not be null.", key);
            assertSame("The key provider is not correct.", FilePrivateKeyDataProvider.class, key.getClass());
            assertEquals("The file is not correct.", keyFile.getAbsolutePath(),
                         ((FilePrivateKeyDataProvider)key).getPrivateKeyFile().getAbsolutePath());

            PasswordProvider password = this.getPasswordProvider();
            assertNotNull("The password provider should not be null.", password);
            assertArrayEquals("The password is not correct.", "testPassword04".toCharArray(), password.getPassword());
        }
        finally
        {
            LicenseCreatorProperties.setPrivateKeyDataProvider(null);
            LicenseCreatorProperties.setPrivateKeyPasswordProvider(null);

            this.resetLicenseCreator();

            FileUtils.forceDelete(file);
            FileUtils.forceDelete(keyFile);

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    public void testInitializeLicenseCreator05() throws Exception
    {
        this.resetLicenseCreator();

        String fileName = "testInitializeLicenseCreator05.properties";
        File file = new File(fileName);
        if(file.exists())
            FileUtils.forceDelete(file);

        File keyFile = new File("testInitializeLicenseCreator05.key");
        FileUtils.writeStringToFile(keyFile, "aKey", "UTF-8");

        FileUtils.writeStringToFile(
            file,
            "io.oddsource.java.licensing.privateKeyFile=testInitializeLicenseCreator05.key",
            "UTF-8"
        );

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("config")).andReturn(true);
        EasyMock.expect(this.console.cli.getOptionValue("config")).andReturn(fileName);

        EasyMock.replay(this.console.cli, this.device);

        try
        {
            this.console.initializeLicenseCreator();
            fail("Expected exception RuntimeException.");
        }
        catch(RuntimeException ignore) { }
        finally
        {
            LicenseCreatorProperties.setPrivateKeyDataProvider(null);
            LicenseCreatorProperties.setPrivateKeyPasswordProvider(null);

            this.resetLicenseCreator();

            FileUtils.forceDelete(file);
            FileUtils.forceDelete(keyFile);

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    public void testInitializeLicenseCreator06() throws Exception
    {
        this.resetLicenseCreator();

        String fileName = "testInitializeLicenseCreator06.properties";
        File file = new File(fileName);
        if(file.exists())
            FileUtils.forceDelete(file);

        File keyFile = new File("testInitializeLicenseCreator06.key");
        FileUtils.writeStringToFile(keyFile, "aKey", "UTF-8");

        FileUtils.writeStringToFile(
            file,
            "io.oddsource.java.licensing.privateKeyPassword=testPassword06",
            "UTF-8"
        );

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("config")).andReturn(true);
        EasyMock.expect(this.console.cli.getOptionValue("config")).andReturn(fileName);

        EasyMock.replay(this.console.cli, this.device);

        try
        {
            this.console.initializeLicenseCreator();
            fail("Expected exception RuntimeException.");
        }
        catch(RuntimeException ignore) { }
        finally
        {
            LicenseCreatorProperties.setPrivateKeyDataProvider(null);
            LicenseCreatorProperties.setPrivateKeyPasswordProvider(null);

            this.resetLicenseCreator();

            FileUtils.forceDelete(file);
            FileUtils.forceDelete(keyFile);

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    public void testInitializeLicenseCreator07() throws Exception
    {
        this.resetLicenseCreator();

        String fileName = "testInitializeLicenseCreator07.properties";
        File file = new File(fileName);
        if(file.exists())
            FileUtils.forceDelete(file);

        FileUtils.writeStringToFile(
            file,
            "io.oddsource.java.licensing.privateKeyProvider=io.oddsource.java.licensing.mock.MockFilePrivateKeyDataProvider\r\n" +
            "io.oddsource.java.licensing.privateKeyPasswordProvider=io.oddsource.java.licensing.mock.MockPasswordProvider",
            "UTF-8"
        );

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("config")).andReturn(true);
        EasyMock.expect(this.console.cli.getOptionValue("config")).andReturn(fileName);

        EasyMock.replay(this.console.cli, this.device);

        try
        {
            this.console.initializeLicenseCreator();

            PrivateKeyDataProvider key = this.getPrivateKeyDataProvider();
            assertNotNull("The key provider should not be null.", key);
            assertSame("The key provider is not correct.", MockFilePrivateKeyDataProvider.class, key.getClass());

            PasswordProvider password = this.getPasswordProvider();
            assertNotNull("The password provider should not be null.", password);
            assertSame("The password provider is not correct.", MockPasswordProvider.class, password.getClass());
        }
        finally
        {
            LicenseCreatorProperties.setPrivateKeyDataProvider(null);
            LicenseCreatorProperties.setPrivateKeyPasswordProvider(null);

            this.resetLicenseCreator();

            FileUtils.forceDelete(file);

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    public void testInitializeLicenseCreator08() throws Exception
    {
        this.resetLicenseCreator();

        File keyFile = new File("testInitializeLicenseCreator08.key");
        FileUtils.writeStringToFile(keyFile, "aKey", "UTF-8");

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("config")).andReturn(true);
        EasyMock.expect(this.console.cli.getOptionValue("config")).andReturn("  ");

        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Read the private key from a file?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Use a PrivateKeyDataProvider implementation from the classpath?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter the name of the private key file to use: ")).andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Invalid or non-existent file. Please enter the name of the private " +
                                             "key file to use: ")).andReturn("  ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Invalid or non-existent file. Please enter the name of the private " +
                                             "key file to use: ")).andReturn("testInitializeLicenseCreator08-bad.key");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Invalid or non-existent file. Please enter the name of the private " +
                                             "key file to use: ")).andReturn("testInitializeLicenseCreator08.key");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Type the private key password in manually?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Use a PasswordProvider implementation from the classpath?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("2");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter the fully-qualified class name for the " +
                                             "PasswordProvider implementation: ")).andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter the fully-qualified class name for the " +
                                             "PasswordProvider implementation: ")).andReturn("  ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter the fully-qualified class name for the " +
                                             "PasswordProvider implementation: ")).
                andReturn("io.oddsource.java.licensing.mock.MockPasswordProvider");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.replay(this.console.cli, this.device);

        try
        {
            this.console.initializeLicenseCreator();

            PrivateKeyDataProvider key = this.getPrivateKeyDataProvider();
            assertNotNull("The key provider should not be null.", key);
            assertSame("The key provider is not correct.", FilePrivateKeyDataProvider.class, key.getClass());
            assertEquals("The file is not correct.", keyFile.getAbsolutePath(),
                         ((FilePrivateKeyDataProvider)key).getPrivateKeyFile().getAbsolutePath());

            PasswordProvider password = this.getPasswordProvider();
            assertNotNull("The password provider should not be null.", password);
            assertSame("The password provider is not correct.", MockPasswordProvider.class, password.getClass());
        }
        finally
        {
            this.resetLicenseCreator();

            FileUtils.forceDelete(keyFile);

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    public void testInitializeLicenseCreator09() throws Exception
    {
        this.resetLicenseCreator();

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("config")).andReturn(false);

        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Read the private key from a file?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Use a PrivateKeyDataProvider implementation from the classpath?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("2");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter the fully-qualified class name for the " +
                                             "PrivateKeyDataProvider implementation: ")).andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter the fully-qualified class name for the " +
                                             "PrivateKeyDataProvider implementation: ")).andReturn("  ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter the fully-qualified class name for the " +
                                             "PrivateKeyDataProvider implementation: ")).
                andReturn("io.oddsource.java.licensing.mock.MockEmbeddedPrivateKeyDataProvider");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Type the private key password in manually?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Use a PasswordProvider implementation from the classpath?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readPassword("Please type the password for the private key: ")).andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readPassword("Invalid password. Please type the password for the private key: ")).
                andReturn("testPassword09".toCharArray());
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.replay(this.console.cli, this.device);

        try
        {
            this.console.initializeLicenseCreator();

            PrivateKeyDataProvider key = this.getPrivateKeyDataProvider();
            assertNotNull("The key provider should not be null.", key);
            assertSame("The key provider is not correct.", MockEmbeddedPrivateKeyDataProvider.class, key.getClass());

            PasswordProvider password = this.getPasswordProvider();
            assertNotNull("The password provider should not be null.", password);
            assertArrayEquals("The password is not correct.", "testPassword09".toCharArray(), password.getPassword());
        }
        finally
        {
            this.resetLicenseCreator();

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    public void testGenerateLicense01() throws Exception
    {
        this.resetLicenseCreator();

        String fileName = "testGenerateLicense01.properties";
        File file = new File(fileName);
        if(file.exists())
            FileUtils.forceDelete(file);

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("license")).andReturn(true);
        EasyMock.expect(this.console.cli.getOptionValue("license")).andReturn(fileName);

        EasyMock.replay(this.console.cli, this.device);

        try
        {
            this.console.generateLicense();
            fail("Expected exception FileNotFoundException.");
        }
        catch(FileNotFoundException ignore) { }
        finally
        {
            this.resetLicenseCreator();

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    @Ignore("canRead()/canWrite() do not work on Win; setReadable()/setWritable() do not work on some Macs.")
    public void testGenerateLicense02() throws Exception
    {
        this.resetLicenseCreator();

        String fileName = "testGenerateLicense02.properties";
        File file = new File(fileName);
        file = file.getCanonicalFile();
        if(file.exists())
            FileUtils.forceDelete(file);

        FileUtils.writeStringToFile(file, "test", "UTF-8");

        assertTrue("Setting the file to not readable should have returned true.", file.setReadable(false, false));
        assertTrue("The file should be writable.", file.canWrite());
        assertFalse("The file should not be readable.", file.canRead());

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("license")).andReturn(true);
        EasyMock.expect(this.console.cli.getOptionValue("license")).andReturn(fileName);

        EasyMock.replay(this.console.cli, this.device);

        try
        {
            this.console.generateLicense();
            fail("Expected exception IOException.");
        }
        catch(IOException ignore) { }
        finally
        {
            this.resetLicenseCreator();

            FileUtils.forceDelete(file);

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    public void testGenerateLicense03() throws Exception
    {
        this.resetLicenseCreator();

        MockPasswordProvider passwordProvider = new MockPasswordProvider();

        String fileName = "testGenerateLicense03.properties";
        File file = new File(fileName);
        if(file.exists())
            FileUtils.forceDelete(file);

        FileUtils.writeStringToFile(file, "", "UTF-8");

        Capture<String> capture = EasyMock.newCapture();

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("license")).andReturn(true);
        EasyMock.expect(this.console.cli.getOptionValue("license")).andReturn(fileName);

        this.device.printOut(EasyMock.capture(capture));
        EasyMock.expectLastCall();

        EasyMock.replay(this.console.cli, this.device);

        LicenseCreatorProperties.setPrivateKeyDataProvider(new MockEmbeddedPrivateKeyDataProvider());
        LicenseCreatorProperties.setPrivateKeyPasswordProvider(passwordProvider);

        try
        {
            this.console.generateLicense();

            assertNotNull("The encoded license data should not be null.", capture.getValue());

            byte[] data = Base64.decodeBase64(capture.getValue());

            assertNotNull("The license data should not be null.", data);
            assertTrue("The license data should not be empty.", data.length > 0);

            SignedLicense signed = (new ObjectSerializer()).readObject(SignedLicense.class, data);

            assertNotNull("The signed license should not be null.", signed);

            License license = MockLicenseHelper.deserialize(Encryptor.decryptRaw(
                    signed.getLicenseContent(), passwordProvider.getPassword()
            ));

            assertNotNull("The license is not correct.", license);

            assertEquals("The product key is not correct.", "", license.getProductKey());
            assertEquals("The holder is not correct.", "", license.getHolder());
            assertEquals("The issuer is not correct.", "", license.getIssuer());
            assertEquals("The subject is not correct.", "", license.getSubject());
            assertEquals("The issue date is not correct.", 0L, license.getIssueDate());
            assertEquals("The good after date is not correct.", 0L, license.getGoodAfterDate());
            assertEquals("The good before date is not correct.", 0L, license.getGoodBeforeDate());
            assertEquals("The number of licenses is not correct.", 0, license.getNumberOfLicenses());
            assertEquals("The number of features is not correct.", 0, license.getFeatures().size());
        }
        finally
        {
            this.resetLicenseCreator();

            FileUtils.forceDelete(file);

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    public void testGenerateLicense04() throws Exception
    {
        this.resetLicenseCreator();

        MockPasswordProvider passwordProvider = new MockPasswordProvider();

        String fileName = "testGenerateLicense04.properties";
        File file = new File(fileName);
        if(file.exists())
            FileUtils.forceDelete(file);

        FileUtils.writeStringToFile(
            file,
            "io.oddsource.java.licensing.password=somePassword04\r\n" +
            "io.oddsource.java.licensing.issueDate=abcdefg\r\n" +
            "io.oddsource.java.licensing.numberOfLicenses=gfedcba",
            "UTF-8"
        );

        Capture<String> capture = EasyMock.newCapture();

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("license")).andReturn(true);
        EasyMock.expect(this.console.cli.getOptionValue("license")).andReturn(fileName);

        this.device.printOut(EasyMock.capture(capture));
        EasyMock.expectLastCall();

        EasyMock.replay(this.console.cli, this.device);

        LicenseCreatorProperties.setPrivateKeyDataProvider(new MockEmbeddedPrivateKeyDataProvider());
        LicenseCreatorProperties.setPrivateKeyPasswordProvider(passwordProvider);

        try
        {
            this.console.generateLicense();

            assertNotNull("The encoded license data should not be null.", capture.getValue());

            byte[] data = Base64.decodeBase64(capture.getValue());

            assertNotNull("The license data should not be null.", data);
            assertTrue("The license data should not be empty.", data.length > 0);

            SignedLicense signed = (new ObjectSerializer()).readObject(SignedLicense.class, data);

            assertNotNull("The signed license should not be null.", signed);

            License license = MockLicenseHelper.deserialize(Encryptor.decryptRaw(
                    signed.getLicenseContent(), "somePassword04".toCharArray()
            ));

            assertNotNull("The license is not correct.", license);

            assertEquals("The product key is not correct.", "", license.getProductKey());
            assertEquals("The holder is not correct.", "", license.getHolder());
            assertEquals("The issuer is not correct.", "", license.getIssuer());
            assertEquals("The subject is not correct.", "", license.getSubject());
            assertEquals("The issue date is not correct.", 0L, license.getIssueDate());
            assertEquals("The good after date is not correct.", 0L, license.getGoodAfterDate());
            assertEquals("The good before date is not correct.", 0L, license.getGoodBeforeDate());
            assertEquals("The number of licenses is not correct.", 0, license.getNumberOfLicenses());
            assertEquals("The number of features is not correct.", 0, license.getFeatures().size());
        }
        finally
        {
            this.resetLicenseCreator();

            FileUtils.forceDelete(file);

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGenerateLicense05() throws Exception
    {
        this.resetLicenseCreator();

        MockPasswordProvider passwordProvider = new MockPasswordProvider();

        String fileName = "testGenerateLicense05.properties";
        File file = new File(fileName);
        if(file.exists())
            FileUtils.forceDelete(file);

        FileUtils.writeStringToFile(file, "io.oddsource.java.licensing.password=anotherPassword05\r\n" +
                                          "io.oddsource.java.licensing.productKey=6575-TH0T-SNL5-7XGG-1099-1040\r\n" +
                                          "io.oddsource.java.licensing.holder=myHolder01\r\n" +
                                          "io.oddsource.java.licensing.issuer=yourIssuer02\r\n" +
                                          "io.oddsource.java.licensing.subject=aSubject03\r\n" +
                                          "io.oddsource.java.licensing.issueDate=2012-05-01 22:21:20\r\n" +
                                          "io.oddsource.java.licensing.goodAfterDate=2012-06-01 00:00:00\r\n" +
                                          "io.oddsource.java.licensing.goodBeforeDate=2012-06-30 23:59:59\r\n" +
                                          "io.oddsource.java.licensing.numberOfLicenses=83\r\n" +
                                          "io.oddsource.java.licensing.features.MY_FEATURE_01=\r\n" +
                                          "io.oddsource.java.licensing.features.ANOTHER_FEATURE_02=2012-06-15 23:59:59\r\n");

        Capture<String> capture = EasyMock.newCapture();

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("license")).andReturn(true);
        EasyMock.expect(this.console.cli.getOptionValue("license")).andReturn(fileName);

        this.device.printOut(EasyMock.capture(capture));
        EasyMock.expectLastCall();

        EasyMock.replay(this.console.cli, this.device);

        LicenseCreatorProperties.setPrivateKeyDataProvider(new MockEmbeddedPrivateKeyDataProvider());
        LicenseCreatorProperties.setPrivateKeyPasswordProvider(passwordProvider);

        try
        {
            this.console.generateLicense();

            assertNotNull("The encoded license data should not be null.", capture.getValue());

            byte[] data = Base64.decodeBase64(capture.getValue());

            assertNotNull("The license data should not be null.", data);
            assertTrue("The license data should not be empty.", data.length > 0);

            SignedLicense signed = (new ObjectSerializer()).readObject(SignedLicense.class, data);

            assertNotNull("The signed license should not be null.", signed);

            License license = MockLicenseHelper.deserialize(Encryptor.decryptRaw(
                    signed.getLicenseContent(), "anotherPassword05".toCharArray()
            ));

            assertNotNull("The license is not correct.", license);

            assertEquals("The product key is not correct.", "6575-TH0T-SNL5-7XGG-1099-1040", license.getProductKey());
            assertEquals("The holder is not correct.", "myHolder01", license.getHolder());
            assertEquals("The issuer is not correct.", "yourIssuer02", license.getIssuer());
            assertEquals("The subject is not correct.", "aSubject03", license.getSubject());
            assertEquals("The issue date is not correct.", new Date(112, 4, 1, 22, 21, 20).getTime(), license.getIssueDate());
            assertEquals("The good after date is not correct.", new Date(112, 5, 1, 0, 0, 0).getTime(), license.getGoodAfterDate());
            assertEquals("The good before date is not correct.", new Date(112, 5, 30, 23, 59, 59).getTime(), license.getGoodBeforeDate());
            assertEquals("The number of licenses is not correct.", 83, license.getNumberOfLicenses());
            assertEquals("The number of features is not correct.", 2, license.getFeatures().size());

            HashMap<String, License.Feature> map = new HashMap<String, License.Feature>();
            for(License.Feature feature : license.getFeatures())
                map.put(feature.getName(), feature);

            assertNotNull("Feature 1 should not be null.", map.get("MY_FEATURE_01"));
            assertEquals("Feature 1 is not correct.", -1L, map.get("MY_FEATURE_01").getGoodBeforeDate());

            assertNotNull("Feature 2 should not be null.", map.get("ANOTHER_FEATURE_02"));
            assertEquals("Feature 2 is not correct.", new Date(112, 5, 15, 23, 59, 59).getTime(),
                         map.get("ANOTHER_FEATURE_02").getGoodBeforeDate());
        }
        finally
        {
            this.resetLicenseCreator();

            FileUtils.forceDelete(file);

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGenerateLicense06() throws Exception
    {
        this.resetLicenseCreator();

        MockPasswordProvider passwordProvider = new MockPasswordProvider();

        String fileName = "testGenerateLicense06.properties";
        File file = new File(fileName);
        if(file.exists())
            FileUtils.forceDelete(file);

        String licenseFileName = "testGenerateLicense06.license";
        File licenseFile = new File(licenseFileName);
        if(licenseFile.exists())
            FileUtils.forceDelete(licenseFile);

        FileUtils.writeStringToFile(file, "io.oddsource.java.licensing.password=finalPassword06\r\n" +
                                          "io.oddsource.java.licensing.productKey=5565-1039-AF89-GGX7-TN31-14AL\r\n" +
                                          "io.oddsource.java.licensing.holder=someHolder01\r\n" +
                                          "io.oddsource.java.licensing.issuer=coolIssuer02\r\n" +
                                          "io.oddsource.java.licensing.subject=lameSubject03\r\n" +
                                          "io.oddsource.java.licensing.issueDate=2011-07-15 15:17:19\r\n" +
                                          "io.oddsource.java.licensing.goodAfterDate=2011-09-01 00:00:00\r\n" +
                                          "io.oddsource.java.licensing.goodBeforeDate=2011-12-31 23:59:59\r\n" +
                                          "io.oddsource.java.licensing.numberOfLicenses=21\r\n" +
                                          "io.oddsource.java.licensing.features.FINAL_FEATURE_03=\r\n" +
                                          "io.oddsource.java.licensing.licenseFile=" + licenseFileName + "\r\n");

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("license")).andReturn(true);
        EasyMock.expect(this.console.cli.getOptionValue("license")).andReturn(fileName);

        EasyMock.replay(this.console.cli, this.device);

        LicenseCreatorProperties.setPrivateKeyDataProvider(new MockEmbeddedPrivateKeyDataProvider());
        LicenseCreatorProperties.setPrivateKeyPasswordProvider(passwordProvider);

        try
        {
            this.console.generateLicense();

            assertTrue("The license file should exist.", licenseFile.exists());

            byte[] data = FileUtils.readFileToByteArray(licenseFile);

            assertNotNull("The license data should not be null.", data);
            assertTrue("The license data should not be empty.", data.length > 0);

            SignedLicense signed = (new ObjectSerializer()).readObject(SignedLicense.class, data);

            assertNotNull("The signed license should not be null.", signed);

            License license = MockLicenseHelper.deserialize(Encryptor.decryptRaw(
                    signed.getLicenseContent(), "finalPassword06".toCharArray()
            ));

            assertNotNull("The license is not correct.", license);

            assertEquals("The product key is not correct.", "5565-1039-AF89-GGX7-TN31-14AL", license.getProductKey());
            assertEquals("The holder is not correct.", "someHolder01", license.getHolder());
            assertEquals("The issuer is not correct.", "coolIssuer02", license.getIssuer());
            assertEquals("The subject is not correct.", "lameSubject03", license.getSubject());
            assertEquals("The issue date is not correct.", new Date(111, 6, 15, 15, 17, 19).getTime(), license.getIssueDate());
            assertEquals("The good after date is not correct.", new Date(111, 8, 1, 0, 0, 0).getTime(), license.getGoodAfterDate());
            assertEquals("The good before date is not correct.", new Date(111, 11, 31, 23, 59, 59).getTime(), license.getGoodBeforeDate());
            assertEquals("The number of licenses is not correct.", 21, license.getNumberOfLicenses());
            assertEquals("The number of features is not correct.", 1, license.getFeatures().size());

            HashMap<String, License.Feature> map = new HashMap<String, License.Feature>();
            for(License.Feature feature : license.getFeatures())
                map.put(feature.getName(), feature);

            assertNotNull("Feature 1 should not be null.", map.get("FINAL_FEATURE_03"));
            assertEquals("Feature 1 is not correct.", -1L, map.get("FINAL_FEATURE_03").getGoodBeforeDate());
        }
        finally
        {
            this.resetLicenseCreator();

            FileUtils.forceDelete(file);
            FileUtils.forceDelete(licenseFile);

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    public void testGenerateLicense07() throws Exception
    {
        this.resetLicenseCreator();

        MockPasswordProvider passwordProvider = new MockPasswordProvider();

        Capture<String> capture = EasyMock.newCapture();

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("license")).andReturn(true);
        EasyMock.expect(this.console.cli.getOptionValue("license")).andReturn("  ");

        EasyMock.expect(this.device.readLine("Please enter a product key for this license (you can leave this " +
                                             "blank): ")).
                andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter a holder for this license (you can leave this blank): ")).
                andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an issuer for this license (you can leave this blank): ")).
                andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter a subject for this license (you can leave this blank): ")).
                andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an issue date for this license (YYYY-MM-DD hh:mm:ss " +
                                             "or blank): ")).
                andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an activation/good-after date for this license " +
                                             "(YYYY-MM-DD hh:mm:ss or blank): ")).
                andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an expiration date for this license " +
                                             "(YYYY-MM-DD hh:mm:ss or blank): ")).
                andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter a number of seats/licenses for this license " +
                                             "(you can leave this blank): ")).
                andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Optionally enter the name/key of a feature you want to add to this " +
                                             "license (you can leave this blank): ")).
                andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.promptForValidPassword(0, 32, "the license with (if left blank, will use the " +
                                                                  "private key password provider)")).
                andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Output the Base64-encoded license data to the screen?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Write the raw, binary license data to a file?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        this.device.printOutLn("License Data:");
        EasyMock.expectLastCall();
        this.device.printOutLn(EasyMock.capture(capture));
        EasyMock.expectLastCall();

        EasyMock.replay(this.console.cli, this.device);

        LicenseCreatorProperties.setPrivateKeyDataProvider(new MockEmbeddedPrivateKeyDataProvider());
        LicenseCreatorProperties.setPrivateKeyPasswordProvider(passwordProvider);

        try
        {
            this.console.generateLicense();

            assertNotNull("The encoded license data should not be null.", capture.getValue());

            byte[] data = Base64.decodeBase64(capture.getValue());

            assertNotNull("The license data should not be null.", data);
            assertTrue("The license data should not be empty.", data.length > 0);

            SignedLicense signed = (new ObjectSerializer()).readObject(SignedLicense.class, data);

            assertNotNull("The signed license should not be null.", signed);

            License license = MockLicenseHelper.deserialize(Encryptor.decryptRaw(
                    signed.getLicenseContent(), passwordProvider.getPassword()
            ));

            assertNotNull("The license is not correct.", license);

            assertEquals("The product key is not correct.", "", license.getProductKey());
            assertEquals("The holder is not correct.", "", license.getHolder());
            assertEquals("The issuer is not correct.", "", license.getIssuer());
            assertEquals("The subject is not correct.", "", license.getSubject());
            assertEquals("The issue date is not correct.", 0L, license.getIssueDate());
            assertEquals("The good after date is not correct.", 0L, license.getGoodAfterDate());
            assertEquals("The good before date is not correct.", 0L, license.getGoodBeforeDate());
            assertEquals("The number of licenses is not correct.", 0, license.getNumberOfLicenses());
            assertEquals("The number of features is not correct.", 0, license.getFeatures().size());
        }
        finally
        {
            this.resetLicenseCreator();

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    public void testGenerateLicense08() throws Exception
    {
        this.resetLicenseCreator();

        MockPasswordProvider passwordProvider = new MockPasswordProvider();

        Capture<String> capture = EasyMock.newCapture();

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("license")).andReturn(false);

        EasyMock.expect(this.device.readLine("Please enter a product key for this license (you can leave this " +
                                             "blank): ")).
                andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter a holder for this license (you can leave this blank): ")).
                andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an issuer for this license (you can leave this blank): ")).
                andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter a subject for this license (you can leave this blank): ")).
                andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an issue date for this license (YYYY-MM-DD hh:mm:ss " +
                                             "or blank): ")).
                andReturn("abcdefg");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an activation/good-after date for this license " +
                                             "(YYYY-MM-DD hh:mm:ss or blank): ")).
                andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an expiration date for this license " +
                                             "(YYYY-MM-DD hh:mm:ss or blank): ")).
                andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter a number of seats/licenses for this license " +
                                             "(you can leave this blank): ")).
                andReturn("gfedcba");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Optionally enter the name/key of a feature you want to add to this " +
                                             "license (you can leave this blank): ")).
                andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.promptForValidPassword(0, 32, "the license with (if left blank, will use the " +
                                                                  "private key password provider)")).
                andReturn("somePassword04".toCharArray());
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Output the Base64-encoded license data to the screen?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Write the raw, binary license data to a file?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("1");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        this.device.printOutLn("License Data:");
        EasyMock.expectLastCall();
        this.device.printOutLn(EasyMock.capture(capture));
        EasyMock.expectLastCall();

        EasyMock.replay(this.console.cli, this.device);

        LicenseCreatorProperties.setPrivateKeyDataProvider(new MockEmbeddedPrivateKeyDataProvider());
        LicenseCreatorProperties.setPrivateKeyPasswordProvider(passwordProvider);

        try
        {
            this.console.generateLicense();

            assertNotNull("The encoded license data should not be null.", capture.getValue());

            byte[] data = Base64.decodeBase64(capture.getValue());

            assertNotNull("The license data should not be null.", data);
            assertTrue("The license data should not be empty.", data.length > 0);

            SignedLicense signed = (new ObjectSerializer()).readObject(SignedLicense.class, data);

            assertNotNull("The signed license should not be null.", signed);

            License license = MockLicenseHelper.deserialize(Encryptor.decryptRaw(
                    signed.getLicenseContent(), "somePassword04".toCharArray()
            ));

            assertNotNull("The license is not correct.", license);

            assertEquals("The product key is not correct.", "", license.getProductKey());
            assertEquals("The holder is not correct.", "", license.getHolder());
            assertEquals("The issuer is not correct.", "", license.getIssuer());
            assertEquals("The subject is not correct.", "", license.getSubject());
            assertEquals("The issue date is not correct.", 0L, license.getIssueDate());
            assertEquals("The good after date is not correct.", 0L, license.getGoodAfterDate());
            assertEquals("The good before date is not correct.", 0L, license.getGoodBeforeDate());
            assertEquals("The number of licenses is not correct.", 0, license.getNumberOfLicenses());
            assertEquals("The number of features is not correct.", 0, license.getFeatures().size());
        }
        finally
        {
            this.resetLicenseCreator();

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGenerateLicense09() throws Exception
    {
        this.resetLicenseCreator();

        MockPasswordProvider passwordProvider = new MockPasswordProvider();

        Capture<String> capture = EasyMock.newCapture();

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("license")).andReturn(false);

        EasyMock.expect(this.device.readLine("Please enter a product key for this license (you can leave this " +
                                             "blank): ")).
                andReturn("6575-TH0T-SNL5-7XGG-1099-1040");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter a holder for this license (you can leave this blank): ")).
                andReturn("myHolder01");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an issuer for this license (you can leave this blank): ")).
                andReturn("yourIssuer02");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter a subject for this license (you can leave this blank): ")).
                andReturn("aSubject03");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an issue date for this license (YYYY-MM-DD hh:mm:ss " +
                                             "or blank): ")).
                andReturn("2012-05-01 22:21:20");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an activation/good-after date for this license " +
                                             "(YYYY-MM-DD hh:mm:ss or blank): ")).
                andReturn("2012-06-01 00:00:00");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an expiration date for this license " +
                                             "(YYYY-MM-DD hh:mm:ss or blank): ")).
                andReturn("2012-06-30 23:59:59");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter a number of seats/licenses for this license " +
                                             "(you can leave this blank): ")).
                andReturn("83");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Optionally enter the name/key of a feature you want to add to this " +
                                             "license (you can leave this blank): ")).
                andReturn("MY_FEATURE_01");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Optionally enter an expiration date for feature [MY_FEATURE_01] " +
                                             "(YYYY-MM-DD hh:mm:ss or blank): ")).
                andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Enter another feature to add to this license (you can leave " +
                                             "this blank): ")).
                andReturn("ANOTHER_FEATURE_02");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Optionally enter an expiration date for feature [ANOTHER_FEATURE_02] " +
                                             "(YYYY-MM-DD hh:mm:ss or blank): ")).
                andReturn("2012-06-15 23:59:59");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Enter another feature to add to this license (you can leave " +
                                             "this blank): ")).
                andReturn("    ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.promptForValidPassword(0, 32, "the license with (if left blank, will use the " +
                                                                  "private key password provider)")).
                andReturn("anotherPassword05".toCharArray());
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Output the Base64-encoded license data to the screen?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Write the raw, binary license data to a file?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        this.device.printOutLn("License Data:");
        EasyMock.expectLastCall();
        this.device.printOutLn(EasyMock.capture(capture));
        EasyMock.expectLastCall();

        EasyMock.replay(this.console.cli, this.device);

        LicenseCreatorProperties.setPrivateKeyDataProvider(new MockEmbeddedPrivateKeyDataProvider());
        LicenseCreatorProperties.setPrivateKeyPasswordProvider(passwordProvider);

        try
        {
            this.console.generateLicense();

            assertNotNull("The encoded license data should not be null.", capture.getValue());

            byte[] data = Base64.decodeBase64(capture.getValue());

            assertNotNull("The license data should not be null.", data);
            assertTrue("The license data should not be empty.", data.length > 0);

            SignedLicense signed = (new ObjectSerializer()).readObject(SignedLicense.class, data);

            assertNotNull("The signed license should not be null.", signed);

            License license = MockLicenseHelper.deserialize(Encryptor.decryptRaw(
                    signed.getLicenseContent(), "anotherPassword05".toCharArray()
            ));

            assertNotNull("The license is not correct.", license);

            assertEquals("The product key is not correct.", "6575-TH0T-SNL5-7XGG-1099-1040", license.getProductKey());
            assertEquals("The holder is not correct.", "myHolder01", license.getHolder());
            assertEquals("The issuer is not correct.", "yourIssuer02", license.getIssuer());
            assertEquals("The subject is not correct.", "aSubject03", license.getSubject());
            assertEquals("The issue date is not correct.", new Date(112, 4, 1, 22, 21, 20).getTime(), license.getIssueDate());
            assertEquals("The good after date is not correct.", new Date(112, 5, 1, 0, 0, 0).getTime(), license.getGoodAfterDate());
            assertEquals("The good before date is not correct.", new Date(112, 5, 30, 23, 59, 59).getTime(), license.getGoodBeforeDate());
            assertEquals("The number of licenses is not correct.", 83, license.getNumberOfLicenses());
            assertEquals("The number of features is not correct.", 2, license.getFeatures().size());

            HashMap<String, License.Feature> map = new HashMap<String, License.Feature>();
            for(License.Feature feature : license.getFeatures())
                map.put(feature.getName(), feature);

            assertNotNull("Feature 1 should not be null.", map.get("MY_FEATURE_01"));
            assertEquals("Feature 1 is not correct.", -1L, map.get("MY_FEATURE_01").getGoodBeforeDate());

            assertNotNull("Feature 2 should not be null.", map.get("ANOTHER_FEATURE_02"));
            assertEquals("Feature 2 is not correct.", new Date(112, 5, 15, 23, 59, 59).getTime(),
                         map.get("ANOTHER_FEATURE_02").getGoodBeforeDate());
        }
        finally
        {
            this.resetLicenseCreator();

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGenerateLicense10() throws Exception
    {
        this.resetLicenseCreator();

        MockPasswordProvider passwordProvider = new MockPasswordProvider();

        String licenseFileName = "testGenerateLicense10.license";
        File licenseFile = new File(licenseFileName);
        if(licenseFile.exists())
            FileUtils.forceDelete(licenseFile);

        this.console.cli = EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createStrictMock();

        EasyMock.expect(this.console.cli.hasOption("license")).andReturn(false);

        EasyMock.expect(this.device.readLine("Please enter a product key for this license (you can leave this " +
                                             "blank): ")).
                andReturn("5565-1039-AF89-GGX7-TN31-14AL");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter a holder for this license (you can leave this blank): ")).
                andReturn("someHolder01");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an issuer for this license (you can leave this blank): ")).
                andReturn("coolIssuer02");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter a subject for this license (you can leave this blank): ")).
                andReturn("lameSubject03");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an issue date for this license (YYYY-MM-DD hh:mm:ss " +
                                             "or blank): ")).
                andReturn("2011-07-15 15:17:19");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an activation/good-after date for this license " +
                                             "(YYYY-MM-DD hh:mm:ss or blank): ")).
                andReturn("2011-09-01 00:00:00");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter an expiration date for this license " +
                                             "(YYYY-MM-DD hh:mm:ss or blank): ")).
                andReturn("2011-12-31 23:59:59");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter a number of seats/licenses for this license " +
                                             "(you can leave this blank): ")).
                andReturn("21");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Optionally enter the name/key of a feature you want to add to this " +
                                             "license (you can leave this blank): ")).
                andReturn("FINAL_FEATURE_03");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Optionally enter an expiration date for feature [FINAL_FEATURE_03] " +
                                             "(YYYY-MM-DD hh:mm:ss or blank): ")).
                andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Enter another feature to add to this license (you can leave " +
                                             "this blank): ")).
                andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.promptForValidPassword(0, 32, "the license with (if left blank, will use the " +
                                                                  "private key password provider)")).
                andReturn("finalPassword06".toCharArray());
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Output the Base64-encoded license data to the screen?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Write the raw, binary license data to a file?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("2");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter the name of the file to save the license to: ")).
                andReturn("");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Invalid file name. Please enter the name of the file to save the " +
                                             "license to: ")).
                andReturn("   ");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Invalid file name. Please enter the name of the file to save the " +
                                             "license to: ")).
                andReturn(licenseFileName);
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.replay(this.console.cli, this.device);

        LicenseCreatorProperties.setPrivateKeyDataProvider(new MockEmbeddedPrivateKeyDataProvider());
        LicenseCreatorProperties.setPrivateKeyPasswordProvider(passwordProvider);

        try
        {
            this.console.generateLicense();

            assertTrue("The license file should exist.", licenseFile.exists());

            byte[] data = FileUtils.readFileToByteArray(licenseFile);

            assertNotNull("The license data should not be null.", data);
            assertTrue("The license data should not be empty.", data.length > 0);

            SignedLicense signed = (new ObjectSerializer()).readObject(SignedLicense.class, data);

            assertNotNull("The signed license should not be null.", signed);

            License license = MockLicenseHelper.deserialize(Encryptor.decryptRaw(
                    signed.getLicenseContent(), "finalPassword06".toCharArray()
            ));

            assertNotNull("The license is not correct.", license);

            assertEquals("The product key is not correct.", "5565-1039-AF89-GGX7-TN31-14AL", license.getProductKey());
            assertEquals("The holder is not correct.", "someHolder01", license.getHolder());
            assertEquals("The issuer is not correct.", "coolIssuer02", license.getIssuer());
            assertEquals("The subject is not correct.", "lameSubject03", license.getSubject());
            assertEquals("The issue date is not correct.", new Date(111, 6, 15, 15, 17, 19).getTime(), license.getIssueDate());
            assertEquals("The good after date is not correct.", new Date(111, 8, 1, 0, 0, 0).getTime(), license.getGoodAfterDate());
            assertEquals("The good before date is not correct.", new Date(111, 11, 31, 23, 59, 59).getTime(), license.getGoodBeforeDate());
            assertEquals("The number of licenses is not correct.", 21, license.getNumberOfLicenses());
            assertEquals("The number of features is not correct.", 1, license.getFeatures().size());

            HashMap<String, License.Feature> map = new HashMap<String, License.Feature>();
            for(License.Feature feature : license.getFeatures())
                map.put(feature.getName(), feature);

            assertNotNull("Feature 1 should not be null.", map.get("FINAL_FEATURE_03"));
            assertEquals("Feature 1 is not correct.", -1L, map.get("FINAL_FEATURE_03").getGoodBeforeDate());
        }
        finally
        {
            this.resetLicenseCreator();

            FileUtils.forceDelete(licenseFile);

            EasyMock.verify(this.console.cli);
        }
    }

    @Test
    public void testRun01() throws Exception
    {
        this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
                withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
                withArgs(this.device, new DefaultParser()).
                addMockedMethod("processCommandLineOptions").
                addMockedMethod("initializeLicenseCreator").
                addMockedMethod("generateLicense").
                createStrictMock();

        String[] arguments = new String[] { "help" };

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.initializeLicenseCreator();
        EasyMock.expectLastCall().andThrow(new KeyNotFoundException("message01."));

        this.device.printErrLn("message01. Correct the error and try again.");
        EasyMock.expectLastCall();
        this.device.exit(51);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.console);

        try
        {
            this.console.run(arguments);
        }
        finally
        {
            EasyMock.verify(this.console);
        }
    }

    @Test
    public void testRun02() throws Exception
    {
        this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
                withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
                withArgs(this.device, new DefaultParser()).
                addMockedMethod("processCommandLineOptions").
                addMockedMethod("initializeLicenseCreator").
                addMockedMethod("generateLicense").
                createStrictMock();

        String[] arguments = new String[] { "help" };

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.initializeLicenseCreator();
        EasyMock.expectLastCall().andThrow(new ObjectSerializationException("message02."));

        this.device.printErrLn("message02. Correct the error and try again.");
        EasyMock.expectLastCall();
        this.device.exit(52);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.console);

        try
        {
            this.console.run(arguments);
        }
        finally
        {
            EasyMock.verify(this.console);
        }
    }

    @Test
    public void testRun03() throws Exception
    {
        this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
                withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
                withArgs(this.device, new DefaultParser()).
                addMockedMethod("processCommandLineOptions").
                addMockedMethod("initializeLicenseCreator").
                addMockedMethod("generateLicense").
                createStrictMock();

        String[] arguments = new String[] { "help" };

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.initializeLicenseCreator();
        EasyMock.expectLastCall().andThrow(new AlgorithmNotSupportedException("message03."));

        this.device.printErrLn("The algorithm \"message03.\" is not supported on this system. " +
                               "Contact your system administrator for assistance.");
        EasyMock.expectLastCall();
        this.device.exit(41);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.console);

        try
        {
            this.console.run(arguments);
        }
        finally
        {
            EasyMock.verify(this.console);
        }
    }

    @Test
    public void testRun04() throws Exception
    {
        this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
                withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
                withArgs(this.device, new DefaultParser()).
                addMockedMethod("processCommandLineOptions").
                addMockedMethod("initializeLicenseCreator").
                addMockedMethod("generateLicense").
                createStrictMock();

        String[] arguments = new String[] { "help" };

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.initializeLicenseCreator();
        EasyMock.expectLastCall().andThrow(new InappropriateKeyException("message04."));

        this.device.printErrLn("message04. Contact your system administrator for assistance.");
        EasyMock.expectLastCall();
        this.device.exit(42);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.console);

        try
        {
            this.console.run(arguments);
        }
        finally
        {
            EasyMock.verify(this.console);
        }
    }

    @Test
    public void testRun05() throws Exception
    {
        this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
                withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
                withArgs(this.device, new DefaultParser()).
                addMockedMethod("processCommandLineOptions").
                addMockedMethod("initializeLicenseCreator").
                addMockedMethod("generateLicense").
                createStrictMock();

        String[] arguments = new String[] { "help" };

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.initializeLicenseCreator();
        EasyMock.expectLastCall().andThrow(new InappropriateKeySpecificationException("message05."));

        this.device.printErrLn("message05. Contact your system administrator for assistance.");
        EasyMock.expectLastCall();
        this.device.exit(43);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.console);

        try
        {
            this.console.run(arguments);
        }
        finally
        {
            EasyMock.verify(this.console);
        }
    }

    @Test
    public void testRun06() throws Exception
    {
        this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
                withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
                withArgs(this.device, new DefaultParser()).
                addMockedMethod("processCommandLineOptions").
                addMockedMethod("initializeLicenseCreator").
                addMockedMethod("generateLicense").
                createStrictMock();

        String[] arguments = new String[] { "help" };

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.initializeLicenseCreator();
        EasyMock.expectLastCall();
        this.console.generateLicense();
        EasyMock.expectLastCall().andThrow(new InterruptedException("message06."));

        this.device.printErrLn("The system was interrupted while waiting for events to complete.");
        EasyMock.expectLastCall();
        this.device.exit(44);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.console);

        try
        {
            this.console.run(arguments);
        }
        finally
        {
            EasyMock.verify(this.console);
        }
    }

    @Test
    public void testRun07() throws Exception
    {
        this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
                withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
                withArgs(this.device, new DefaultParser()).
                addMockedMethod("processCommandLineOptions").
                addMockedMethod("initializeLicenseCreator").
                addMockedMethod("generateLicense").
                createStrictMock();

        String[] arguments = new String[] { "help" };

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.initializeLicenseCreator();
        EasyMock.expectLastCall();
        this.console.generateLicense();
        EasyMock.expectLastCall().andThrow(new IOException("message07."));

        this.device.printErrLn("An error occurred writing or reading files from the system. Analyze the error " +
                    "below to determine what went wrong and fix it!");
        EasyMock.expectLastCall();
        this.device.printErrLn("java.io.IOException: message07.");
        EasyMock.expectLastCall();
        this.device.exit(21);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.console);

        try
        {
            this.console.run(arguments);
        }
        finally
        {
            EasyMock.verify(this.console);
        }
    }

    @Test
    public void testRun08() throws Exception
    {
        this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
                withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
                withArgs(this.device, new DefaultParser()).
                addMockedMethod("processCommandLineOptions").
                addMockedMethod("initializeLicenseCreator").
                addMockedMethod("generateLicense").
                createStrictMock();

        String[] arguments = new String[] { "testOption08" };

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.initializeLicenseCreator();
        EasyMock.expectLastCall();
        this.console.generateLicense();
        EasyMock.expectLastCall().andThrow(new RuntimeException("message08."));

        this.device.printErrLn("java.lang.RuntimeException: message08.");
        EasyMock.expectLastCall();
        this.device.exit(-1);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.console);

        try
        {
            this.console.run(arguments);
        }
        finally
        {
            EasyMock.verify(this.console);
        }
    }

    @Test
    public void testRun09() throws Exception
    {
        this.console = EasyMock.createMockBuilder(ConsoleLicenseGenerator.class).
                withConstructor(TextInterfaceDevice.class, CommandLineParser.class).
                withArgs(this.device, new DefaultParser()).
                addMockedMethod("processCommandLineOptions").
                addMockedMethod("initializeLicenseCreator").
                addMockedMethod("generateLicense").
                createStrictMock();

        String[] arguments = new String[] { "lastOption09" };

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.initializeLicenseCreator();
        EasyMock.expectLastCall();
        this.console.generateLicense();
        EasyMock.expectLastCall();

        this.device.exit(0);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.console);

        try
        {
            this.console.run(arguments);
        }
        finally
        {
            EasyMock.verify(this.console);
        }
    }

    private static class ThisExceptionMeansTestSucceededException extends SecurityException
    {
        private static final long serialVersionUID = 1L;
    }

    @Test(expected=ThisExceptionMeansTestSucceededException.class)
    public void testMain01()
    {
        SecurityManager securityManager = new MockPermissiveSecurityManager() {
            private boolean active = true;

            @Override
            public void checkExit(int status)
            {
                if(this.active)
                {
                    this.active = false;
                    assertEquals("The exit status is not correct.", 0, status);
                    throw new ThisExceptionMeansTestSucceededException();
                }
            }
        };

        EasyMock.replay(this.device);

        System.setSecurityManager(securityManager);

        ConsoleLicenseGenerator.main("-help");

        System.setSecurityManager(null);
    }

    @Test(expected=ThisExceptionMeansTestSucceededException.class)
    public void testMain02()
    {
        SecurityManager securityManager = new MockPermissiveSecurityManager() {
            private boolean active = true;

            @Override
            public void checkExit(int status)
            {
                if(this.active)
                {
                    this.active = false;
                    assertEquals("The exit status is not correct.", 1, status);
                    throw new ThisExceptionMeansTestSucceededException();
                }
            }
        };

        EasyMock.replay(this.device);

        System.setSecurityManager(securityManager);

        ConsoleLicenseGenerator.main("-badOption");

        System.setSecurityManager(null);
    }
}