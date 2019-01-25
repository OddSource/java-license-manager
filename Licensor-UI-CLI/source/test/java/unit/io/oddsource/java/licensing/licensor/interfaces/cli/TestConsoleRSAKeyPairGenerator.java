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

import static io.oddsource.java.licensing.licensor.encryption.RSAKeyPairGeneratorInterface.GeneratedClassDescriptor;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.io.FileUtils;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.oddsource.java.licensing.exception.AlgorithmNotSupportedException;
import io.oddsource.java.licensing.exception.InappropriateKeyException;
import io.oddsource.java.licensing.exception.InappropriateKeySpecificationException;
import io.oddsource.java.licensing.licensor.encryption.RSAKeyPairGeneratorInterface;
import io.oddsource.java.licensing.licensor.exception.RSA2048NotSupportedException;
import io.oddsource.java.licensing.licensor.interfaces.cli.spi.TextInterfaceDevice;

/**
 * Test class for ConsoleRSAKeyPairGenerator.
 */
public class TestConsoleRSAKeyPairGenerator
{
    private static final String LF = System.getProperty("line.separator");

    private ConsoleRSAKeyPairGenerator console;

    private RSAKeyPairGeneratorInterface generator;

    private TextInterfaceDevice device;

    @Before
    public void setUp()
    {
        this.generator = EasyMock.createMock(RSAKeyPairGeneratorInterface.class);
        this.device = EasyMock.createMock(TextInterfaceDevice.class);

        this.console = new ConsoleRSAKeyPairGenerator(this.generator, this.device, new DefaultParser())
        {
            @Override
            @SuppressWarnings("MethodDoesntCallSuperMethod")
            protected void finalize()
            {

            }
        };
    }

    @After
    public void tearDown()
    {
        EasyMock.verify(this.generator, this.device);
    }

    @Test
    public void testProcessCommandLineOptions01()
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream printer = new PrintStream(stream);

        EasyMock.expect(this.device.out()).andReturn(printer);
        this.device.exit(0);
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device);

        this.console.processCommandLineOptions(new String[] {"-help"});

        String output = stream.toString();

        assertNotNull("There should be output.", output);
        assertTrue("The output should have length.", output.length() > 0);
        assertEquals(
            "The output is not correct.",
            "usage:  ConsoleRSAKeyPairGenerator -help" + LF +
            " ConsoleRSAKeyPairGenerator -interactive" + LF +
            " ConsoleRSAKeyPairGenerator -password <password> -private <file|class name> -public <file|class name>\n" +
            "        [-privatePassword <password>] [-classes -passwordClass <class name> -privatePasswordClass " +
            "<class\n" +
            "        name> [-privatePackage <package>] [-publicPackage <package>] [-passwordPackage <package>]" + LF +
            "        [-privatePasswordPackage <package>]]" + LF +
            " -classes                             Specify to generate compilable Java classes instead of key files" +
            LF +
            " -help                                Display this help message" + LF +
            " -interactive                         Specify to use interactive mode and ignore command-line options" +
            LF +
            " -password <password>                 The password to use to encrypt the public and private keys" + LF +
            "                                      (required unless in interactive mode)" + LF +
            " -passwordClass <class name>          The name of the password storage class to generate (optional," + LF +
            "                                      ignored unless generating classes)" + LF +
            " -passwordPackage <package>           The name of the package to use for the password storage class" + LF +
            "                                      (optional, ignored unless generating classes)" + LF +
            " -private <file|class name>           The name of the private key file or class to generate (required" +
            LF +
            "                                      unless in interactive mode)" + LF +
            " -privatePackage <package>            The name of the package to use for the private key class " +
            "(optional," +
            LF +
            "                                      ignored unless generating classes)" + LF +
            " -privatePassword <password>          A different password to use to encrypt the private key (optional)" +
            LF +
            " -privatePasswordClass <class name>   The name of the private key password storage class to generate" +
            LF +
            "                                      (optional, ignored unless generating classes)" + LF +
            " -privatePasswordPackage <package>    The name of the package to use for the private key password " +
            "storage" +
            LF +
            "                                      class (optional, ignored unless generating classes)" + LF +
            " -public <file|class name>            The name of the public key file or class to generate (required" +
            LF +
            "                                      unless in interactive mode)" + LF +
            " -publicPackage <package>             The name of the package to use for the public key class (optional," +
            LF +
            "                                      ignored unless generating classes)" + LF,
            output
        );
    }

    @Test
    public void testProcessCommandLineOptions02()
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream printer = new PrintStream(stream);

        this.device.printErrLn("Missing required options: private, public, password");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(printer);
        this.device.exit(1);
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device);

        this.console.processCommandLineOptions(new String[] {});

        String output = stream.toString();

        assertNotNull("There should be output.", output);
        assertTrue("The output should have length.", output.length() > 0);
        assertEquals(
            "The output is not correct.",
            "usage:  ConsoleRSAKeyPairGenerator -help" + LF +
            " ConsoleRSAKeyPairGenerator -interactive" + LF +
            " ConsoleRSAKeyPairGenerator -password <password> -private <file|class name> -public <file|class name>" +
            LF +
            "        [-privatePassword <password>] [-classes -passwordClass <class name> -privatePasswordClass <class" +
            LF +
            "        name> [-privatePackage <package>] [-publicPackage <package>] [-passwordPackage <package>]" + LF +
            "        [-privatePasswordPackage <package>]]" + LF +
            " -classes                             Specify to generate compilable Java classes instead of key files" +
            LF +
            " -help                                Display this help message" + LF +
            " -interactive                         Specify to use interactive mode and ignore command-line options" +
            LF +
            " -password <password>                 The password to use to encrypt the public and private keys" + LF +
            "                                      (required unless in interactive mode)" + LF +
            " -passwordClass <class name>          The name of the password storage class to generate (optional," + LF +
            "                                      ignored unless generating classes)" + LF +
            " -passwordPackage <package>           The name of the package to use for the password storage class" + LF +
            "                                      (optional, ignored unless generating classes)" + LF +
            " -private <file|class name>           The name of the private key file or class to generate (required" +
            LF +
            "                                      unless in interactive mode)" + LF +
            " -privatePackage <package>            The name of the package to use for the private key class " +
            "(optional," +
            LF +
            "                                      ignored unless generating classes)" + LF +
            " -privatePassword <password>          A different password to use to encrypt the private key (optional)" +
            LF +
            " -privatePasswordClass <class name>   The name of the private key password storage class to generate" +
            LF +
            "                                      (optional, ignored unless generating classes)" + LF +
            " -privatePasswordPackage <package>    The name of the package to use for the private key password " +
            "storage" +
            LF +
            "                                      class (optional, ignored unless generating classes)" + LF +
            " -public <file|class name>            The name of the public key file or class to generate (required" +
            LF +
            "                                      unless in interactive mode)" + LF +
            " -publicPackage <package>             The name of the package to use for the public key class (optional," +
            LF +
            "                                      ignored unless generating classes)" + LF,
            output
        );
    }

    @Test
    public void testProcessCommandLineOptions03()
    {
        EasyMock.replay(this.generator, this.device);

        this.console.processCommandLineOptions(new String[] {"-interactive"});

        assertNull("There should be no cli value.", this.console.getCli());
        assertTrue("The interactive flag should be true.", this.console.isInteractive());
    }

    @Test
    public void testProcessCommandLineOptions04()
    {
        EasyMock.replay(this.generator, this.device);

        this.console.processCommandLineOptions(
            new String[] {"-private", "private.key", "-public", "public.key", "-password", "myPassword01"});

        assertNotNull("There should be a cli value.", this.console.getCli());
        assertFalse("The interactive flag should be false.", this.console.isInteractive());
    }

    @Test
    public void testPromptToGenerateClasses01()
    {
        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Save the public and private keys to .key files?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Generate compilable Java code with embedded keys?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device);

        assertFalse("The return value should be false.", this.console.promptToGenerateClasses());
    }

    @Test
    public void testPromptToGenerateClasses02()
    {
        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Save the public and private keys to .key files?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Generate compilable Java code with embedded keys?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device);

        assertFalse("The return value should be false.", this.console.promptToGenerateClasses());
    }

    @Test
    public void testPromptToGenerateClasses03()
    {
        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Save the public and private keys to .key files?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Generate compilable Java code with embedded keys?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("1");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device);

        assertFalse("The return value should be false.", this.console.promptToGenerateClasses());
    }

    @Test
    public void testPromptToGenerateClasses04()
    {
        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Save the public and private keys to .key files?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Generate compilable Java code with embedded keys?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("2");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device);

        assertTrue("The return value should be true.", this.console.promptToGenerateClasses());
    }

    @Test
    public void testPromptToUseDifferentPasswords01()
    {
        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Use the same password to encrypt both keys?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Use a different password for each key?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device);

        assertFalse("The return value should be false.", this.console.promptToUseDifferentPasswords());
    }

    @Test
    public void testPromptToUseDifferentPasswords02()
    {
        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Use the same password to encrypt both keys?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Use a different password for each key?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device);

        assertFalse("The return value should be false.", this.console.promptToUseDifferentPasswords());
    }

    @Test
    public void testPromptToUseDifferentPasswords03()
    {
        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Use the same password to encrypt both keys?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Use a different password for each key?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("1");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device);

        assertFalse("The return value should be false.", this.console.promptToUseDifferentPasswords());
    }

    @Test
    public void testPromptToUseDifferentPasswords04()
    {
        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Use the same password to encrypt both keys?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Use a different password for each key?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("2");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device);

        assertTrue("The return value should be true.", this.console.promptToUseDifferentPasswords());
    }

    @Test
    public void testPromptForString01()
    {
        EasyMock.expect(this.device.readLine("Test message 01")).andReturn(null);
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device);

        assertNull("The returned value should be null.", this.console.promptForString("Test message 01"));
    }

    @Test
    public void testPromptForString02()
    {
        EasyMock.expect(this.device.readLine("Another message 02")).andReturn("");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device);

        assertNull("The returned value should be null.", this.console.promptForString("Another message 02"));
    }

    @Test
    public void testPromptForString03()
    {
        EasyMock.expect(this.device.readLine("Another message 03")).andReturn("     ");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device);

        assertNull("The returned value should be null.", this.console.promptForString("Another message 03"));
    }

    @Test
    public void testPromptForString04()
    {
        EasyMock.expect(this.device.readLine("Final message 04")).andReturn("Returned console value");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device);

        assertEquals("The returned value should be null.", "Returned console value",
                     this.console.promptForString("Final message 04")
        );
    }

    @Test
    public void testCheckAndPromptToOverwriteFile01()
    {
        EasyMock.replay(this.generator, this.device);

        assertTrue(
            "The value returned should be true.",
            this.console.checkAndPromptToOverwriteFile("testCheckAndPromptToOverwriteFile01")
        );
    }

    @Test
    public void testCheckAndPromptToOverwriteFile02() throws IOException
    {
        File file = new File("testCheckAndPromptToOverwriteFile02");
        FileUtils.writeStringToFile(file, "test string", "UTF-8");

        try
        {
            EasyMock.expect(this.device.readLine(
                "The file \"%s\" already exists. Overwrite it (YES/no)? ",
                file.getCanonicalPath()
            )).andReturn("y");

            EasyMock.replay(this.generator, this.device);

            assertTrue(
                "The value returned should be true.",
                this.console.checkAndPromptToOverwriteFile("testCheckAndPromptToOverwriteFile02")
            );
        }
        finally
        {
            FileUtils.forceDelete(file);
        }
    }

    @Test
    public void testCheckAndPromptToOverwriteFile03() throws IOException
    {
        File file = new File("testCheckAndPromptToOverwriteFile03");
        FileUtils.writeStringToFile(file, "test string", "UTF-8");

        try
        {
            EasyMock.expect(this.device.readLine(
                "The file \"%s\" already exists. Overwrite it (YES/no)? ",
                file.getCanonicalPath()
            )).andReturn("Y");

            EasyMock.replay(this.generator, this.device);

            assertTrue(
                "The value returned should be true.",
                this.console.checkAndPromptToOverwriteFile("testCheckAndPromptToOverwriteFile03")
            );
        }
        finally
        {
            FileUtils.forceDelete(file);
        }
    }

    @Test
    public void testCheckAndPromptToOverwriteFile04() throws IOException
    {
        File file = new File("testCheckAndPromptToOverwriteFile04");
        FileUtils.writeStringToFile(file, "test string", "UTF-8");

        try
        {
            EasyMock.expect(this.device.readLine(
                "The file \"%s\" already exists. Overwrite it (YES/no)? ",
                file.getCanonicalPath()
            )).andReturn("yes");

            EasyMock.replay(this.generator, this.device);

            assertTrue(
                "The value returned should be true.",
                this.console.checkAndPromptToOverwriteFile("testCheckAndPromptToOverwriteFile04")
            );
        }
        finally
        {
            FileUtils.forceDelete(file);
        }
    }

    @Test
    public void testCheckAndPromptToOverwriteFile05() throws IOException
    {
        File file = new File("testCheckAndPromptToOverwriteFile05");
        FileUtils.writeStringToFile(file, "test string", "UTF-8");

        try
        {
            EasyMock.expect(this.device.readLine(
                "The file \"%s\" already exists. Overwrite it (YES/no)? ",
                file.getCanonicalPath()
            )).andReturn("");

            EasyMock.replay(this.generator, this.device);

            assertTrue(
                "The value returned should be true.",
                this.console.checkAndPromptToOverwriteFile("testCheckAndPromptToOverwriteFile05")
            );
        }
        finally
        {
            FileUtils.forceDelete(file);
        }
    }

    @Test
    public void testCheckAndPromptToOverwriteFile06() throws IOException
    {
        File file = new File("testCheckAndPromptToOverwriteFile06");
        FileUtils.writeStringToFile(file, "test string", "UTF-8");

        try
        {
            EasyMock.expect(this.device.readLine(
                "The file \"%s\" already exists. Overwrite it (YES/no)? ",
                file.getCanonicalPath()
            )).andReturn("no");

            EasyMock.replay(this.generator, this.device);

            assertFalse(
                "The value returned should be true.",
                this.console.checkAndPromptToOverwriteFile("testCheckAndPromptToOverwriteFile06")
            );
        }
        finally
        {
            FileUtils.forceDelete(file);
        }
    }

    @Test
    public void testCheckAndPromptToOverwriteFile07() throws IOException
    {
        File file = new File("testCheckAndPromptToOverwriteFile07");
        file = file.getCanonicalFile();
        FileUtils.writeStringToFile(file, "test string", "UTF-8");

        assertTrue("Setting the file readable flag to false should have succeeded.", file.setReadable(false, false));
        assertTrue("The file should still be writable.", file.canWrite());
        assertFalse("The file should not be readable.", file.canRead());

        try
        {
            this.device.printErrLn(
                "The file " + file.getCanonicalPath() + " already exists and cannot be overwritten.");
            EasyMock.expectLastCall();

            EasyMock.replay(this.generator, this.device);

            assertFalse(
                "The value returned should be true.",
                this.console.checkAndPromptToOverwriteFile("testCheckAndPromptToOverwriteFile07")
            );
        }
        finally
        {
            FileUtils.forceDelete(file);
        }
    }

    @Test
    public void testCheckAndPromptToOverwriteFile08() throws IOException
    {
        File file = new File("testCheckAndPromptToOverwriteFile08");
        file = file.getCanonicalFile();
        FileUtils.writeStringToFile(file, "test string", "UTF-8");

        assertTrue("Setting the file readable flag to false should have succeeded.", file.setWritable(false, false));
        assertTrue("The file should still be readable.", file.canRead());
        assertFalse("The file should not be writable.", file.canWrite());

        try
        {
            this.device.printErrLn(
                "The file " + file.getCanonicalPath() + " already exists and cannot be overwritten.");
            EasyMock.expectLastCall();

            EasyMock.replay(this.generator, this.device);

            assertFalse(
                "The value returned should be true.",
                this.console.checkAndPromptToOverwriteFile("testCheckAndPromptToOverwriteFile08")
            );
        }
        finally
        {
            FileUtils.forceDelete(file);
        }
    }

    @Test
    public void testDoInteractive01() throws Exception
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        String privateFileName = "testDoInteractive01Private.key";
        String publicFileName = "testDoInteractive01Public.key";

        this.console.setCli(
            EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createMock()
        );

        PrivateKey privateKey = EasyMock.createMock(PrivateKey.class);
        PublicKey publicKey = EasyMock.createMock(PublicKey.class);
        KeyPair keyPair = new KeyPair(publicKey, privateKey);

        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Save the public and private keys to .key files?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Generate compilable Java code with embedded keys?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Use the same password to encrypt both keys?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Use a different password for each key?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn(" ");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.expect(this.device.promptForValidPassword(6, 32, "both keys")).
            andReturn("keyPassword01".toCharArray());
        this.device.printOutLn("Passwords match.");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.expect(this.device.readLine("Please enter the name of a file to store the public key in: ")).
            andReturn(publicFileName);
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter the name of a file to store the private key in: ")).
            andReturn(privateFileName);
        this.device.printOutLn();
        EasyMock.expectLastCall();

        this.device.printOut("Generating RSA key pair, 2048-bit long modulus");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        EasyMock.expect(this.generator.generateKeyPair()).andReturn(keyPair);

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOut("Key pair generated. Encrypting keys with 128-bit AES security");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        final Capture<char[]> capture1 = EasyMock.newCapture();

        this.generator.saveKeyPairToFiles(
            EasyMock.eq(keyPair), EasyMock.eq(privateFileName), EasyMock.eq(publicFileName),
            EasyMock.capture(capture1)
        );
        EasyMock.expectLastCall().andAnswer((IAnswer<Void>) () -> {
            assertNotNull("The captured key password should not be null.", capture1.getValue());
            assertArrayEquals("The captured key password is not correct.", "keyPassword01".toCharArray(),
                              capture1.getValue()
            );
            return null;
        });

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Private key written to " + privateFileName);
        EasyMock.expectLastCall();
        this.device.printOutLn("Public key written to " + publicFileName);

        EasyMock.replay(this.generator, this.device, this.console.getCli(), privateKey, publicKey);

        try
        {
            this.console.doInteractive();

            assertNotNull("The captured key password should still not be null.", capture1.getValue());
            assertArrayEquals("The captured key password should have been erased.",
                              new char[capture1.getValue().length], capture1.getValue()
            );
        }
        finally
        {
            EasyMock.verify(this.console.getCli(), privateKey, publicKey);
        }
    }

    @Test
    public void testDoInteractive02() throws Exception
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        String privateFileName = "testDoInteractive01Private.key";
        String publicFileName = "testDoInteractive01Public.key";

        this.console.setCli(
            EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createMock()
        );

        PrivateKey privateKey = EasyMock.createMock(PrivateKey.class);
        PublicKey publicKey = EasyMock.createMock(PublicKey.class);
        KeyPair keyPair = new KeyPair(publicKey, privateKey);

        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Save the public and private keys to .key files?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Generate compilable Java code with embedded keys?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("1");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Use the same password to encrypt both keys?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Use a different password for each key?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("2");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.expect(this.device.promptForValidPassword(6, 32, "the public key")).
            andReturn("publicPassword02".toCharArray());
        this.device.printOutLn("Passwords match.");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.expect(this.device.promptForValidPassword(6, 32, "the private key")).
            andReturn("privatePassword02".toCharArray());
        this.device.printOutLn("Passwords match.");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.expect(this.device.readLine("Please enter the name of a file to store the public key in: ")).
            andReturn(publicFileName);
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter the name of a file to store the private key in: ")).
            andReturn(privateFileName);
        this.device.printOutLn();
        EasyMock.expectLastCall();

        this.device.printOut("Generating RSA key pair, 2048-bit long modulus");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        EasyMock.expect(this.generator.generateKeyPair()).andReturn(keyPair);

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOut("Key pair generated. Encrypting keys with 128-bit AES security");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        final Capture<char[]> capture1 = EasyMock.newCapture();
        final Capture<char[]> capture2 = EasyMock.newCapture();

        this.generator.saveKeyPairToFiles(
            EasyMock.eq(keyPair), EasyMock.eq(privateFileName), EasyMock.eq(publicFileName),
            EasyMock.capture(capture1), EasyMock.capture(capture2)
        );
        EasyMock.expectLastCall().andAnswer((IAnswer<Void>) () -> {
            assertNotNull("The private key password should not be null.", capture1.getValue());
            assertArrayEquals("The private key password is not correct.", "privatePassword02".toCharArray(),
                              capture1.getValue()
            );
            assertNotNull("The public key password should not be null.", capture2.getValue());
            assertArrayEquals("The public key password is not correct.", "publicPassword02".toCharArray(),
                              capture2.getValue()
            );
            return null;
        });

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Private key written to " + privateFileName);
        EasyMock.expectLastCall();
        this.device.printOutLn("Public key written to " + publicFileName);

        EasyMock.replay(this.generator, this.device, this.console.getCli(), privateKey, publicKey);

        try
        {
            this.console.doInteractive();

            assertNotNull("The private key password should still not be null.", capture1.getValue());
            assertArrayEquals("The private key password should have been erased.",
                              new char[capture1.getValue().length], capture1.getValue()
            );
            assertNotNull("The public key password should still not be null.", capture2.getValue());
            assertArrayEquals("The public key password should have been erased.",
                              new char[capture2.getValue().length], capture2.getValue()
            );
        }
        finally
        {
            EasyMock.verify(this.console.getCli(), privateKey, publicKey);
        }
    }

    @Test
    public void testDoInteractive03() throws Exception
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        this.console.setCli(
            EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createMock()
        );

        PrivateKey privateKey = EasyMock.createMock(PrivateKey.class);
        PublicKey publicKey = EasyMock.createMock(PublicKey.class);
        KeyPair keyPair = new KeyPair(publicKey, privateKey);

        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Save the public and private keys to .key files?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Generate compilable Java code with embedded keys?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("2");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Use the same password to encrypt both keys?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Use a different password for each key?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("1");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.expect(this.device.promptForValidPassword(6, 32, "both keys")).
            andReturn("keyPassword03".toCharArray());
        this.device.printOutLn("Passwords match.");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.expect(this.device.readLine("Please enter the name of a Java class to embed the public key in: ")).
            andReturn("PublicKey01");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Enter an optional package name for the public key class: ")).
            andReturn("org.example.licensing.public");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter the name of a Java class to embed the private key in: ")).
            andReturn("PrivateKey01");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Enter an optional package name for the private key class: ")).
            andReturn("org.example.licensing.private");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.expect(this.device.readLine("If you wish to embed the key password in a Java class, " +
                                             "enter the class name now: ")).
            andReturn("KeyPassword01");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("You can optionally enter a package name for the key storage class: ")).
            andReturn("org.example.licensing.public");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        this.device.printOut("Generating RSA key pair, 2048-bit long modulus");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        EasyMock.expect(this.generator.generateKeyPair()).andReturn(keyPair);

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOut("Key pair generated. Encrypting keys with 128-bit AES security");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        final Capture<GeneratedClassDescriptor> descriptorA01 = EasyMock.newCapture();
        final Capture<GeneratedClassDescriptor> descriptorA02 = EasyMock.newCapture();
        final Capture<GeneratedClassDescriptor> descriptorB01 = EasyMock.newCapture();

        final Capture<char[]> passwordA01 = EasyMock.newCapture();
        final Capture<char[]> passwordB01 = EasyMock.newCapture();

        this.generator.saveKeyPairToProviders(
            EasyMock.eq(keyPair), EasyMock.capture(descriptorA01), EasyMock.capture(descriptorA02),
            EasyMock.capture(passwordA01)
        );
        EasyMock.expectLastCall().andAnswer((IAnswer<Void>) () -> {
            assertNotNull("The public key password should not be null.", passwordA01.getValue());
            assertArrayEquals("The public key password is not correct.", "keyPassword03".toCharArray(),
                              passwordA01.getValue()
            );
            descriptorA01.getValue().setJavaFileContents("privateKeyContents01");
            descriptorA02.getValue().setJavaFileContents("publicKeyContents01");
            return null;
        });

        this.generator.savePasswordToProvider(EasyMock.capture(passwordB01), EasyMock.capture(descriptorB01));
        EasyMock.expectLastCall().andAnswer((IAnswer<Void>) () -> {
            assertNotNull("The public key password should still not be null.", passwordB01.getValue());
            assertArrayEquals("The public key password is now not correct.", "keyPassword03".toCharArray(),
                              passwordB01.getValue()
            );
            descriptorB01.getValue().setJavaFileContents("publicPasswordContents01");
            return null;
        });

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Private key provider:");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("privateKeyContents01");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Public key provider:");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("publicKeyContents01");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Key password provider:");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("publicPasswordContents01");
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device, this.console.getCli(), privateKey, publicKey);

        try
        {
            this.console.doInteractive();

            assertNotNull("The public key password should again still not be null.", passwordA01.getValue());
            assertArrayEquals("The public key password should have been erased.",
                              new char[passwordA01.getValue().length], passwordA01.getValue()
            );

            assertEquals(
                "The private key package is not correct.",
                "org.example.licensing.private",
                descriptorA01.getValue().getPackageName()
            );
            assertEquals(
                "The public key package is not correct.",
                "org.example.licensing.public",
                descriptorA02.getValue().getPackageName()
            );
            assertEquals(
                "The public password package is not correct.",
                "org.example.licensing.public",
                descriptorB01.getValue().getPackageName()
            );

            assertEquals(
                "The private key class is not correct.",
                "PrivateKey01",
                descriptorA01.getValue().getClassName()
            );
            assertEquals(
                "The public key class is not correct.",
                "PublicKey01",
                descriptorA02.getValue().getClassName()
            );
            assertEquals(
                "The public password class is not correct.",
                "KeyPassword01",
                descriptorB01.getValue().getClassName()
            );
        }
        finally
        {
            EasyMock.verify(this.console.getCli(), privateKey, publicKey);
        }
    }

    @Test
    public void testDoInteractive04() throws Exception
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        this.console.setCli(
            EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createMock()
        );

        PrivateKey privateKey = EasyMock.createMock(PrivateKey.class);
        PublicKey publicKey = EasyMock.createMock(PublicKey.class);
        KeyPair keyPair = new KeyPair(publicKey, privateKey);

        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Save the public and private keys to .key files?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Generate compilable Java code with embedded keys?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("2");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        this.device.printOutLn("Would you like to...");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (1) Use the same password to encrypt both keys?");
        EasyMock.expectLastCall();
        this.device.printOutLn("    (2) Use a different password for each key?");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Your selection (default 1)? ")).andReturn("2");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.expect(this.device.promptForValidPassword(6, 32, "the public key")).
            andReturn("publicPassword04".toCharArray());
        this.device.printOutLn("Passwords match.");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.expect(this.device.promptForValidPassword(6, 32, "the private key")).
            andReturn("privatePassword04".toCharArray());
        this.device.printOutLn("Passwords match.");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.expect(this.device.readLine("Please enter the name of a Java class to embed the public key in: ")).
            andReturn("PublicKey02");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Enter an optional package name for the public key class: ")).
            andReturn("org.example.licensing.public");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Please enter the name of a Java class to embed the private key in: ")).
            andReturn("PrivateKey02");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("Enter an optional package name for the private key class: ")).
            andReturn("org.example.licensing.private");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.expect(this.device.readLine("If you wish to embed the public key password in a Java class, " +
                                             "enter the class name now: ")).
            andReturn("PublicPassword02");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("You can optionally enter a package name for the public key storage " +
                                             "class: ")).
            andReturn("org.example.licensing.public");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        EasyMock.expect(this.device.readLine("If you wish to embed the private key password in a Java class, " +
                                             "enter the class name now: ")).
            andReturn("PrivatePassword02");
        this.device.printOutLn();
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.readLine("You can optionally enter a package name for the private key storage " +
                                             "class: ")).
            andReturn("org.example.licensing.private");
        this.device.printOutLn();
        EasyMock.expectLastCall();

        this.device.printOut("Generating RSA key pair, 2048-bit long modulus");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        EasyMock.expect(this.generator.generateKeyPair()).andReturn(keyPair);

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOut("Key pair generated. Encrypting keys with 128-bit AES security");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        final Capture<GeneratedClassDescriptor> descriptorA01 = EasyMock.newCapture();
        final Capture<GeneratedClassDescriptor> descriptorA02 = EasyMock.newCapture();
        final Capture<GeneratedClassDescriptor> descriptorB01 = EasyMock.newCapture();
        final Capture<GeneratedClassDescriptor> descriptorB02 = EasyMock.newCapture();

        final Capture<char[]> passwordA01 = EasyMock.newCapture();
        final Capture<char[]> passwordA02 = EasyMock.newCapture();
        final Capture<char[]> passwordB01 = EasyMock.newCapture();
        final Capture<char[]> passwordB02 = EasyMock.newCapture();

        this.generator.saveKeyPairToProviders(
            EasyMock.eq(keyPair), EasyMock.capture(descriptorA01), EasyMock.capture(descriptorA02),
            EasyMock.capture(passwordA01), EasyMock.capture(passwordA02)
        );
        EasyMock.expectLastCall().andAnswer((IAnswer<Void>) () -> {
            assertNotNull("The private key password should not be null.", passwordA01.getValue());
            assertArrayEquals("The private key password is not correct.", "privatePassword04".toCharArray(),
                              passwordA01.getValue()
            );
            assertNotNull("The public key password should not be null.", passwordA02.getValue());
            assertArrayEquals("The public key password is not correct.", "publicPassword04".toCharArray(),
                              passwordA02.getValue()
            );
            descriptorA01.getValue().setJavaFileContents("privateKeyContents02");
            descriptorA02.getValue().setJavaFileContents("publicKeyContents02");
            return null;
        });

        this.generator.savePasswordToProvider(EasyMock.capture(passwordB01), EasyMock.capture(descriptorB01));
        EasyMock.expectLastCall().andAnswer((IAnswer<Void>) () -> {
            assertNotNull("The public key password should still not be null.", passwordB01.getValue());
            assertArrayEquals("The public key password is now not correct.", "publicPassword04".toCharArray(),
                              passwordB01.getValue()
            );
            descriptorB01.getValue().setJavaFileContents("publicPasswordContents02");
            return null;
        });

        this.generator.savePasswordToProvider(EasyMock.capture(passwordB02), EasyMock.capture(descriptorB02));
        EasyMock.expectLastCall().andAnswer((IAnswer<Void>) () -> {
            assertNotNull("The private key password should still not be null.", passwordB02.getValue());
            assertArrayEquals("The private key password is now not correct.", "privatePassword04".toCharArray(),
                              passwordB02.getValue()
            );
            descriptorB02.getValue().setJavaFileContents("privatePasswordContents02");
            return null;
        });

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Private key provider:");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("privateKeyContents02");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Public key provider:");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("publicKeyContents02");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Public key password provider:");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("publicPasswordContents02");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Private key password provider:");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("privatePasswordContents02");
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device, this.console.getCli(), privateKey, publicKey);

        try
        {
            this.console.doInteractive();

            assertNotNull("The public key password should again still not be null.", passwordA01.getValue());
            assertArrayEquals("The public key password should have been erased.",
                              new char[passwordA01.getValue().length], passwordA01.getValue()
            );
            assertNotNull("The private key password should again still not be null.", passwordA02.getValue());
            assertArrayEquals("The private key password should have been erased.",
                              new char[passwordA02.getValue().length], passwordA02.getValue()
            );

            assertEquals(
                "The private key package is not correct.",
                "org.example.licensing.private",
                descriptorA01.getValue().getPackageName()
            );
            assertEquals(
                "The public key package is not correct.",
                "org.example.licensing.public",
                descriptorA02.getValue().getPackageName()
            );
            assertEquals(
                "The public password package is not correct.",
                "org.example.licensing.public",
                descriptorB01.getValue().getPackageName()
            );
            assertEquals(
                "The private password package is not correct.",
                "org.example.licensing.private",
                descriptorB02.getValue().getPackageName()
            );

            assertEquals("The private key class is not correct.", "PrivateKey02",
                         descriptorA01.getValue().getClassName()
            );
            assertEquals(
                "The public key class is not correct.",
                "PublicKey02",
                descriptorA02.getValue().getClassName()
            );
            assertEquals("The public password class is not correct.", "PublicPassword02",
                         descriptorB01.getValue().getClassName()
            );
            assertEquals(
                "The private password class is not correct.",
                "PrivatePassword02",
                descriptorB02.getValue().getClassName()
            );
        }
        finally
        {
            EasyMock.verify(this.console.getCli(), privateKey, publicKey);
        }
    }

    @Test
    public void testDoCommandLine01() throws Exception
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        String privateFileName = "testDoCommandLine01Private.key";
        String publicFileName = "testDoCommandLine01Public.key";

        this.console.setCli(
            EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createMock()
        );

        PrivateKey privateKey = EasyMock.createMock(PrivateKey.class);
        PublicKey publicKey = EasyMock.createMock(PublicKey.class);
        KeyPair keyPair = new KeyPair(publicKey, privateKey);

        EasyMock.expect(this.console.getCli().hasOption("classes")).andReturn(false);
        EasyMock.expect(this.console.getCli().hasOption("privatePassword")).andReturn(false);
        EasyMock.expect(this.console.getCli().getOptionValue("password")).andReturn("keyPassword01");
        EasyMock.expect(this.console.getCli().getOptionValue("public")).andReturn(publicFileName);
        EasyMock.expect(this.console.getCli().getOptionValue("publicPackage")).andReturn(null);
        EasyMock.expect(this.console.getCli().getOptionValue("private")).andReturn(privateFileName);
        EasyMock.expect(this.console.getCli().getOptionValue("privatePackage")).andReturn(null);

        this.device.printOut("Generating RSA key pair, 2048-bit long modulus");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        EasyMock.expect(this.generator.generateKeyPair()).andReturn(keyPair);

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOut("Key pair generated. Encrypting keys with 128-bit AES security");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        final Capture<char[]> capture1 = EasyMock.newCapture();

        this.generator.saveKeyPairToFiles(
            EasyMock.eq(keyPair), EasyMock.eq(privateFileName), EasyMock.eq(publicFileName),
            EasyMock.capture(capture1)
        );
        EasyMock.expectLastCall().andAnswer((IAnswer<Void>) () -> {
            assertNotNull("The captured key password should not be null.", capture1.getValue());
            assertArrayEquals("The captured key password is not correct.", "keyPassword01".toCharArray(),
                              capture1.getValue()
            );
            return null;
        });

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Private key written to " + privateFileName);
        EasyMock.expectLastCall();
        this.device.printOutLn("Public key written to " + publicFileName);

        EasyMock.replay(this.generator, this.device, this.console.getCli(), privateKey, publicKey);

        try
        {
            this.console.doCommandLine();

            assertNotNull("The captured key password should still not be null.", capture1.getValue());
            assertArrayEquals("The captured key password should have been erased.",
                              new char[capture1.getValue().length], capture1.getValue()
            );
        }
        finally
        {
            EasyMock.verify(this.console.getCli(), privateKey, publicKey);
        }
    }

    @Test
    public void testDoCommandLine02() throws Exception
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        String privateFileName = "testDoCommandLine02Private.key";
        String publicFileName = "testDoCommandLine02Public.key";

        this.console.setCli(
            EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createMock()
        );

        PrivateKey privateKey = EasyMock.createMock(PrivateKey.class);
        PublicKey publicKey = EasyMock.createMock(PublicKey.class);
        KeyPair keyPair = new KeyPair(publicKey, privateKey);

        EasyMock.expect(this.console.getCli().hasOption("classes")).andReturn(false);
        EasyMock.expect(this.console.getCli().hasOption("privatePassword")).andReturn(true);
        EasyMock.expect(this.console.getCli().getOptionValue("password")).andReturn("publicPassword02");
        EasyMock.expect(this.console.getCli().getOptionValue("privatePassword")).andReturn("privatePassword02");
        EasyMock.expect(this.console.getCli().getOptionValue("public")).andReturn(publicFileName);
        EasyMock.expect(this.console.getCli().getOptionValue("publicPackage")).andReturn(null);
        EasyMock.expect(this.console.getCli().getOptionValue("private")).andReturn(privateFileName);
        EasyMock.expect(this.console.getCli().getOptionValue("privatePackage")).andReturn(null);

        this.device.printOut("Generating RSA key pair, 2048-bit long modulus");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        EasyMock.expect(this.generator.generateKeyPair()).andReturn(keyPair);

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOut("Key pair generated. Encrypting keys with 128-bit AES security");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        final Capture<char[]> capture1 = EasyMock.newCapture();
        final Capture<char[]> capture2 = EasyMock.newCapture();

        this.generator.saveKeyPairToFiles(
            EasyMock.eq(keyPair), EasyMock.eq(privateFileName), EasyMock.eq(publicFileName),
            EasyMock.capture(capture1), EasyMock.capture(capture2)
        );
        EasyMock.expectLastCall().andAnswer((IAnswer<Void>) () -> {
            assertNotNull("The private key password should not be null.", capture1.getValue());
            assertArrayEquals("The private key password is not correct.", "privatePassword02".toCharArray(),
                              capture1.getValue()
            );
            assertNotNull("The public key password should not be null.", capture2.getValue());
            assertArrayEquals("The public key password is not correct.", "publicPassword02".toCharArray(),
                              capture2.getValue()
            );
            return null;
        });

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Private key written to " + privateFileName);
        EasyMock.expectLastCall();
        this.device.printOutLn("Public key written to " + publicFileName);

        EasyMock.replay(this.generator, this.device, this.console.getCli(), privateKey, publicKey);

        try
        {
            this.console.doCommandLine();

            assertNotNull("The private key password should still not be null.", capture1.getValue());
            assertArrayEquals("The private key password should have been erased.",
                              new char[capture1.getValue().length], capture1.getValue()
            );
            assertNotNull("The public key password should still not be null.", capture2.getValue());
            assertArrayEquals("The public key password should have been erased.",
                              new char[capture2.getValue().length], capture2.getValue()
            );
        }
        finally
        {
            EasyMock.verify(this.console.getCli(), privateKey, publicKey);
        }
    }

    @Test
    public void testDoCommandLine03() throws Exception
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        this.console.setCli(
            EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createMock()
        );

        PrivateKey privateKey = EasyMock.createMock(PrivateKey.class);
        PublicKey publicKey = EasyMock.createMock(PublicKey.class);
        KeyPair keyPair = new KeyPair(publicKey, privateKey);

        EasyMock.expect(this.console.getCli().hasOption("classes")).andReturn(true);
        EasyMock.expect(this.console.getCli().hasOption("privatePassword")).andReturn(false);
        EasyMock.expect(this.console.getCli().getOptionValue("password")).andReturn("keyPassword03");
        EasyMock.expect(this.console.getCli().getOptionValue("public")).andReturn("PublicKey01");
        EasyMock.expect(this.console.getCli().getOptionValue("publicPackage")).
            andReturn("com.example.licensing.public");
        EasyMock.expect(this.console.getCli().getOptionValue("private")).andReturn("PrivateKey01");
        EasyMock.expect(this.console.getCli().getOptionValue("privatePackage")).
            andReturn("com.example.licensing.private");
        EasyMock.expect(this.console.getCli().getOptionValue("passwordClass")).andReturn("KeyPassword01");
        EasyMock.expect(this.console.getCli().getOptionValue("passwordPackage")).
            andReturn("com.example.licensing.public");

        this.device.printOut("Generating RSA key pair, 2048-bit long modulus");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        EasyMock.expect(this.generator.generateKeyPair()).andReturn(keyPair);

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOut("Key pair generated. Encrypting keys with 128-bit AES security");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        final Capture<GeneratedClassDescriptor> descriptorA01 = EasyMock.newCapture();
        final Capture<GeneratedClassDescriptor> descriptorA02 = EasyMock.newCapture();
        final Capture<GeneratedClassDescriptor> descriptorB01 = EasyMock.newCapture();

        final Capture<char[]> passwordA01 = EasyMock.newCapture();
        final Capture<char[]> passwordB01 = EasyMock.newCapture();

        this.generator.saveKeyPairToProviders(
            EasyMock.eq(keyPair), EasyMock.capture(descriptorA01), EasyMock.capture(descriptorA02),
            EasyMock.capture(passwordA01)
        );
        EasyMock.expectLastCall().andAnswer((IAnswer<Void>) () -> {
            assertNotNull("The public key password should not be null.", passwordA01.getValue());
            assertArrayEquals("The public key password is not correct.", "keyPassword03".toCharArray(),
                              passwordA01.getValue()
            );
            descriptorA01.getValue().setJavaFileContents("privateKeyContents01");
            descriptorA02.getValue().setJavaFileContents("publicKeyContents01");
            return null;
        });

        this.generator.savePasswordToProvider(EasyMock.capture(passwordB01), EasyMock.capture(descriptorB01));
        EasyMock.expectLastCall().andAnswer((IAnswer<Void>) () -> {
            assertNotNull("The public key password should still not be null.", passwordB01.getValue());
            assertArrayEquals("The public key password is now not correct.", "keyPassword03".toCharArray(),
                              passwordB01.getValue()
            );
            descriptorB01.getValue().setJavaFileContents("publicPasswordContents01");
            return null;
        });

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Private key provider:");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("privateKeyContents01");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Public key provider:");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("publicKeyContents01");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Key password provider:");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("publicPasswordContents01");
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device, this.console.getCli(), privateKey, publicKey);

        try
        {
            this.console.doCommandLine();

            assertNotNull("The public key password should again still not be null.", passwordA01.getValue());
            assertArrayEquals("The public key password should have been erased.",
                              new char[passwordA01.getValue().length], passwordA01.getValue()
            );

            assertEquals(
                "The private key package is not correct.",
                "com.example.licensing.private",
                descriptorA01.getValue().getPackageName()
            );
            assertEquals(
                "The public key package is not correct.",
                "com.example.licensing.public",
                descriptorA02.getValue().getPackageName()
            );
            assertEquals(
                "The public password package is not correct.",
                "com.example.licensing.public",
                descriptorB01.getValue().getPackageName()
            );

            assertEquals(
                "The private key class is not correct.",
                "PrivateKey01",
                descriptorA01.getValue().getClassName()
            );
            assertEquals(
                "The public key class is not correct.",
                "PublicKey01",
                descriptorA02.getValue().getClassName()
            );
            assertEquals(
                "The public password class is not correct.",
                "KeyPassword01",
                descriptorB01.getValue().getClassName()
            );
        }
        finally
        {
            EasyMock.verify(this.console.getCli(), privateKey, publicKey);
        }
    }

    @Test
    public void testDoCommandLine04() throws Exception
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        this.console.setCli(
            EasyMock.createMockBuilder(CommandLine.class).withConstructor().
                addMockedMethod("hasOption", String.class).
                addMockedMethod("getOptionValue", String.class).
                createMock()
        );

        PrivateKey privateKey = EasyMock.createMock(PrivateKey.class);
        PublicKey publicKey = EasyMock.createMock(PublicKey.class);
        KeyPair keyPair = new KeyPair(publicKey, privateKey);

        EasyMock.expect(this.console.getCli().hasOption("classes")).andReturn(true);
        EasyMock.expect(this.console.getCli().hasOption("privatePassword")).andReturn(true);
        EasyMock.expect(this.console.getCli().getOptionValue("password")).andReturn("publicPassword04");
        EasyMock.expect(this.console.getCli().getOptionValue("privatePassword")).andReturn("privatePassword04");
        EasyMock.expect(this.console.getCli().getOptionValue("public")).andReturn("PublicKey02");
        EasyMock.expect(this.console.getCli().getOptionValue("publicPackage")).
            andReturn("com.example.licensing.public");
        EasyMock.expect(this.console.getCli().getOptionValue("private")).andReturn("PrivateKey02");
        EasyMock.expect(this.console.getCli().getOptionValue("privatePackage")).
            andReturn("com.example.licensing.private");
        EasyMock.expect(this.console.getCli().getOptionValue("passwordClass")).andReturn("PublicPassword02");
        EasyMock.expect(this.console.getCli().getOptionValue("passwordPackage")).andReturn(
            "com.example.licensing.public");
        EasyMock.expect(this.console.getCli().getOptionValue("privatePasswordClass")).andReturn("PrivatePassword02");
        EasyMock.expect(this.console.getCli().getOptionValue("privatePasswordPackage")).
            andReturn("com.example.licensing.private");

        this.device.printOut("Generating RSA key pair, 2048-bit long modulus");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        EasyMock.expect(this.generator.generateKeyPair()).andReturn(keyPair);

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOut("Key pair generated. Encrypting keys with 128-bit AES security");
        EasyMock.expectLastCall();
        EasyMock.expect(this.device.out()).andReturn(new PrintStream(stream));

        final Capture<GeneratedClassDescriptor> descriptorA01 = EasyMock.newCapture();
        final Capture<GeneratedClassDescriptor> descriptorA02 = EasyMock.newCapture();
        final Capture<GeneratedClassDescriptor> descriptorB01 = EasyMock.newCapture();
        final Capture<GeneratedClassDescriptor> descriptorB02 = EasyMock.newCapture();

        final Capture<char[]> passwordA01 = EasyMock.newCapture();
        final Capture<char[]> passwordA02 = EasyMock.newCapture();
        final Capture<char[]> passwordB01 = EasyMock.newCapture();
        final Capture<char[]> passwordB02 = EasyMock.newCapture();

        this.generator.saveKeyPairToProviders(
            EasyMock.eq(keyPair), EasyMock.capture(descriptorA01), EasyMock.capture(descriptorA02),
            EasyMock.capture(passwordA01), EasyMock.capture(passwordA02)
        );
        EasyMock.expectLastCall().andAnswer((IAnswer<Void>) () -> {
            assertNotNull("The private key password should not be null.", passwordA01.getValue());
            assertArrayEquals("The private key password is not correct.", "privatePassword04".toCharArray(),
                              passwordA01.getValue()
            );
            assertNotNull("The public key password should not be null.", passwordA02.getValue());
            assertArrayEquals("The public key password is not correct.", "publicPassword04".toCharArray(),
                              passwordA02.getValue()
            );
            descriptorA01.getValue().setJavaFileContents("privateKeyContents02");
            descriptorA02.getValue().setJavaFileContents("publicKeyContents02");
            return null;
        });

        this.generator.savePasswordToProvider(EasyMock.capture(passwordB01), EasyMock.capture(descriptorB01));
        EasyMock.expectLastCall().andAnswer((IAnswer<Void>) () -> {
            assertNotNull("The public key password should still not be null.", passwordB01.getValue());
            assertArrayEquals("The public key password is now not correct.", "publicPassword04".toCharArray(),
                              passwordB01.getValue()
            );
            descriptorB01.getValue().setJavaFileContents("publicPasswordContents02");
            return null;
        });

        this.generator.savePasswordToProvider(EasyMock.capture(passwordB02), EasyMock.capture(descriptorB02));
        EasyMock.expectLastCall().andAnswer((IAnswer<Void>) () -> {
            assertNotNull("The private key password should still not be null.", passwordB02.getValue());
            assertArrayEquals("The private key password is now not correct.", "privatePassword04".toCharArray(),
                              passwordB02.getValue()
            );
            descriptorB02.getValue().setJavaFileContents("privatePasswordContents02");
            return null;
        });

        this.device.printOutLn("+++");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Private key provider:");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("privateKeyContents02");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Public key provider:");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("publicKeyContents02");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Public key password provider:");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("publicPasswordContents02");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("Private key password provider:");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.device.printOutLn("privatePasswordContents02");
        EasyMock.expectLastCall();

        EasyMock.replay(this.generator, this.device, this.console.getCli(), privateKey, publicKey);

        try
        {
            this.console.doCommandLine();

            assertNotNull("The public key password should again still not be null.", passwordA01.getValue());
            assertArrayEquals("The public key password should have been erased.",
                              new char[passwordA01.getValue().length], passwordA01.getValue()
            );
            assertNotNull("The private key password should again still not be null.", passwordA02.getValue());
            assertArrayEquals("The private key password should have been erased.",
                              new char[passwordA02.getValue().length], passwordA02.getValue()
            );

            assertEquals(
                "The private key package is not correct.",
                "com.example.licensing.private",
                descriptorA01.getValue().getPackageName()
            );
            assertEquals(
                "The public key package is not correct.",
                "com.example.licensing.public",
                descriptorA02.getValue().getPackageName()
            );
            assertEquals(
                "The public password package is not correct.",
                "com.example.licensing.public",
                descriptorB01.getValue().getPackageName()
            );
            assertEquals(
                "The private password package is not correct.",
                "com.example.licensing.private",
                descriptorB02.getValue().getPackageName()
            );

            assertEquals("The private key class is not correct.", "PrivateKey02",
                         descriptorA01.getValue().getClassName()
            );
            assertEquals(
                "The public key class is not correct.",
                "PublicKey02",
                descriptorA02.getValue().getClassName()
            );
            assertEquals("The public password class is not correct.", "PublicPassword02",
                         descriptorB01.getValue().getClassName()
            );
            assertEquals(
                "The private password class is not correct.",
                "PrivatePassword02",
                descriptorB02.getValue().getClassName()
            );
        }
        finally
        {
            EasyMock.verify(this.console.getCli(), privateKey, publicKey);
        }
    }

    @Test
    public void testRun01() throws Exception
    {
        this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
            withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
            withArgs(this.generator, this.device, new DefaultParser()).
            addMockedMethod("processCommandLineOptions", String[].class).
            addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

        String[] arguments = new String[] {"-interactive"};

        this.device.printOutLn("Using interactive mode...");
        EasyMock.expectLastCall();
        this.device.printOutLn();
        EasyMock.expectLastCall();
        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.doInteractive();
        EasyMock.expectLastCall();
        this.device.exit(0);
        EasyMock.expectLastCall();

        EasyMock.replay(this.console, this.generator, this.device);

        try
        {
            this.console.setInteractive(true);
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
        this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
            withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
            withArgs(this.generator, this.device, new DefaultParser()).
            addMockedMethod("processCommandLineOptions", String[].class).
            addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

        String[] arguments = new String[] {"-private"};

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.doCommandLine();
        EasyMock.expectLastCall();
        this.device.exit(0);
        EasyMock.expectLastCall();

        EasyMock.replay(this.console, this.generator, this.device);

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
    public void testRun03a() throws Exception
    {
        this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
            withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
            withArgs(this.generator, this.device, new DefaultParser()).
            addMockedMethod("processCommandLineOptions", String[].class).
            addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

        String[] arguments = new String[] {"-private"};

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.doCommandLine();
        EasyMock.expectLastCall().andThrow(new RSA2048NotSupportedException(
            "Message 03.",
            new NoSuchAlgorithmException()
        ));
        this.device.printErrLn("Message 03.");
        EasyMock.expectLastCall();
        this.device.exit(51);
        EasyMock.expectLastCall();
        this.device.exit(0);
        EasyMock.expectLastCall();

        EasyMock.replay(this.console, this.generator, this.device);

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
    public void testRun03b() throws Exception
    {
        this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
            withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
            withArgs(this.generator, this.device, new DefaultParser()).
            addMockedMethod("processCommandLineOptions", String[].class).
            addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

        String[] arguments = new String[] {"-private"};

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.doCommandLine();
        EasyMock.expectLastCall().andThrow(new RSA2048NotSupportedException("Message 03."));
        this.device.printErrLn("Message 03.");
        EasyMock.expectLastCall();
        this.device.exit(52);
        EasyMock.expectLastCall();
        this.device.exit(0);
        EasyMock.expectLastCall();

        EasyMock.replay(this.console, this.generator, this.device);

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
        this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
            withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
            withArgs(this.generator, this.device, new DefaultParser()).
            addMockedMethod("processCommandLineOptions", String[].class).
            addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

        String[] arguments = new String[] {"-private"};

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.doCommandLine();
        EasyMock.expectLastCall().andThrow(new AlgorithmNotSupportedException("Message 04."));
        this.device.printErrLn(
            "The algorithm \"Message 04.\" is not supported on this system. Contact your system administrator for " +
            "assistance.");
        EasyMock.expectLastCall();
        this.device.exit(41);
        EasyMock.expectLastCall();
        this.device.exit(0);
        EasyMock.expectLastCall();

        EasyMock.replay(this.console, this.generator, this.device);

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
        this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
            withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
            withArgs(this.generator, this.device, new DefaultParser()).
            addMockedMethod("processCommandLineOptions", String[].class).
            addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

        String[] arguments = new String[] {"-private"};

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.doCommandLine();
        EasyMock.expectLastCall().andThrow(new InappropriateKeyException("Message 05."));
        this.device.printErrLn("Message 05. Contact your system administrator for assistance.");
        EasyMock.expectLastCall();
        this.device.exit(42);
        EasyMock.expectLastCall();
        this.device.exit(0);
        EasyMock.expectLastCall();

        EasyMock.replay(this.console, this.generator, this.device);

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
        this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
            withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
            withArgs(this.generator, this.device, new DefaultParser()).
            addMockedMethod("processCommandLineOptions", String[].class).
            addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

        String[] arguments = new String[] {"-private"};

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.doCommandLine();
        EasyMock.expectLastCall().andThrow(new InappropriateKeySpecificationException("Message 06."));
        this.device.printErrLn("Message 06. Contact your system administrator for assistance.");
        EasyMock.expectLastCall();
        this.device.exit(43);
        EasyMock.expectLastCall();
        this.device.exit(0);
        EasyMock.expectLastCall();

        EasyMock.replay(this.console, this.generator, this.device);

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
        this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
            withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
            withArgs(this.generator, this.device, new DefaultParser()).
            addMockedMethod("processCommandLineOptions", String[].class).
            addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

        String[] arguments = new String[] {"-private"};

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.doCommandLine();
        EasyMock.expectLastCall().andThrow(new InterruptedException("Message 07."));
        this.device.printErrLn("The system was interrupted while waiting for events to complete.");
        EasyMock.expectLastCall();
        this.device.exit(44);
        EasyMock.expectLastCall();
        this.device.exit(0);
        EasyMock.expectLastCall();

        EasyMock.replay(this.console, this.generator, this.device);

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
        this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
            withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
            withArgs(this.generator, this.device, new DefaultParser()).
            addMockedMethod("processCommandLineOptions", String[].class).
            addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

        String[] arguments = new String[] {"-private"};

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.doCommandLine();
        EasyMock.expectLastCall().andThrow(new IOException("Message 08."));
        this.device.printErrLn(
            "An error occurred writing the key files to the file system. Analyze the error below to determine what " +
            "went wrong and fix it!");
        EasyMock.expectLastCall();
        this.device.printErrLn("java.io.IOException: Message 08.");
        EasyMock.expectLastCall();
        this.device.exit(21);
        EasyMock.expectLastCall();
        this.device.exit(0);
        EasyMock.expectLastCall();

        EasyMock.replay(this.console, this.generator, this.device);

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
        this.console = EasyMock.createMockBuilder(ConsoleRSAKeyPairGenerator.class).
            withConstructor(RSAKeyPairGeneratorInterface.class, TextInterfaceDevice.class, CommandLineParser.class).
            withArgs(this.generator, this.device, new DefaultParser()).
            addMockedMethod("processCommandLineOptions", String[].class).
            addMockedMethod("doInteractive").addMockedMethod("doCommandLine").createStrictMock();

        String[] arguments = new String[] {"-private"};

        this.console.processCommandLineOptions(arguments);
        EasyMock.expectLastCall();
        this.console.doCommandLine();
        EasyMock.expectLastCall().andThrow(new Exception("Message 09."));
        this.device.printErrLn("java.lang.Exception: Message 09.");
        EasyMock.expectLastCall();
        this.device.exit(-1);
        EasyMock.expectLastCall();
        this.device.exit(0);
        EasyMock.expectLastCall();

        EasyMock.replay(this.console, this.generator, this.device);

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

    @Test
    public void testMain01() throws Exception
    {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final PrintStream stream = new PrintStream(byteStream, true, "UTF-8");

        final Capture<String> errorCapture = EasyMock.newCapture();

        this.device.registerShutdownHook(EasyMock.anyObject(Thread.class));
        EasyMock.expectLastCall().once();
        this.device.out();
        EasyMock.expectLastCall().andReturn(stream);
        this.device.exit(0);
        EasyMock.expectLastCall().andThrow(new ThisExceptionMeansTestSucceededException());
        this.device.printErrLn(EasyMock.capture(errorCapture));
        EasyMock.expectLastCall().once();
        this.device.exit(EasyMock.anyInt());
        EasyMock.expectLastCall().anyTimes();

        EasyMock.replay(this.generator, this.device);

        TextInterfaceDevice existingConsole = TextInterfaceDevice.CONSOLE;

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);

        Field consoleField = TextInterfaceDevice.class.getField("CONSOLE");

        int existingModifiers = consoleField.getModifiers();
        modifiersField.setInt(consoleField, consoleField.getModifiers() & ~Modifier.FINAL);

        consoleField.set(null, this.device);

        try
        {
            ConsoleRSAKeyPairGenerator.main("-help");
        }
        finally
        {
            consoleField.set(null, existingConsole);
            modifiersField.setInt(consoleField, existingModifiers);
        }

        assertTrue(errorCapture.getValue().contains("ThisExceptionMeansTestSucceededException"));

        String output = byteStream.toString();
        assertTrue(output, output.toLowerCase().contains("usage"));
        assertTrue(output, output.contains("ConsoleRSAKeyPairGenerator -help"));
    }

    @Test
    public void testMain02() throws Exception
    {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final PrintStream stream = new PrintStream(byteStream, true, "UTF-8");

        final Capture<String> firstErrorCapture = EasyMock.newCapture();
        final Capture<String> secondErrorCapture = EasyMock.newCapture();

        this.device.registerShutdownHook(EasyMock.anyObject(Thread.class));
        EasyMock.expectLastCall().once();
        this.device.printErrLn(EasyMock.capture(firstErrorCapture));
        EasyMock.expectLastCall().once();
        this.device.out();
        EasyMock.expectLastCall().andReturn(stream);
        this.device.exit(1);
        EasyMock.expectLastCall().andThrow(new ThisExceptionMeansTestSucceededException());
        this.device.printErrLn(EasyMock.capture(secondErrorCapture));
        EasyMock.expectLastCall().once();
        this.device.exit(EasyMock.anyInt());
        EasyMock.expectLastCall().anyTimes();

        EasyMock.replay(this.generator, this.device);

        TextInterfaceDevice existingConsole = TextInterfaceDevice.CONSOLE;

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);

        Field consoleField = TextInterfaceDevice.class.getField("CONSOLE");

        int existingModifiers = consoleField.getModifiers();
        modifiersField.setInt(consoleField, consoleField.getModifiers() & ~Modifier.FINAL);

        consoleField.set(null, this.device);

        try
        {
            ConsoleRSAKeyPairGenerator.main("-private");
        }
        finally
        {
            consoleField.set(null, existingConsole);
            modifiersField.setInt(consoleField, existingModifiers);
        }

        assertTrue(firstErrorCapture.getValue().contains("private"));
        assertTrue(secondErrorCapture.getValue().contains("ThisExceptionMeansTestSucceededException"));

        String output = byteStream.toString();
        assertTrue(output, output.toLowerCase().contains("usage"));
        assertTrue(output, output.contains("ConsoleRSAKeyPairGenerator -help"));
    }
}

