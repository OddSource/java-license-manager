/*
 * TestAbstractTextInterfaceDevice.java from LicenseManager modified Thursday, January 24, 2013 15:14:11 CST (-0600).
 *
 * Copyright 2010-2013 the original author or authors.
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

package net.nicholaswilliams.java.licensing.licensor.interfaces.cli.spi;

import net.nicholaswilliams.java.licensing.licensor.interfaces.spi.OutputDevice;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * Test class for AbstractTextInterfaceDevice.
 */
public class TestAbstractTextInterfaceDevice
{
    private AbstractTextInterfaceDevice device;

    private InputStream inputStream;

    private PrintStream outputStream;

    private PrintStream errorStream;

    @Before
    public void setUp() throws FileNotFoundException
    {
        this.inputStream = EasyMock.createStrictMock(InputStream.class);
        this.outputStream = EasyMock.createStrictMock(PrintStream.class);
        this.errorStream = EasyMock.createStrictMock(PrintStream.class);

        this.device = EasyMock.createMockBuilder(AbstractTextInterfaceDevice.class).
                withConstructor(InputStream.class, PrintStream.class, PrintStream.class).
                withArgs(this.inputStream, this.outputStream, this.errorStream).
                createStrictMock();
    }

    @After
    public void tearDown()
    {
        EasyMock.verify(this.device, this.inputStream, this.outputStream, this.errorStream);
    }

    @Test
    public void testIn()
    {
        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        assertSame("The input stream is not correct (1).", this.inputStream, this.device.in);
        assertSame("The input stream is not correct (2).", this.inputStream, this.device.in());
    }

    @Test
    public void testOut()
    {
        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        assertSame("The output stream is not correct (1).", this.outputStream, this.device.out);
        assertSame("The output stream is not correct (2).", this.outputStream, this.device.out());
    }

    @Test
    public void testErr()
    {
        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        assertSame("The error output stream is not correct (1).", this.errorStream, this.device.err);
        assertSame("The error output stream is not correct (2).", this.errorStream, this.device.err());
    }

    @Test
    public void testExit()
    {
        this.device.exit(0);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.exit();
    }

    @Test
    public void testPrintOut01()
    {
        this.outputStream.print('c');
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printOut('c');
    }

    @Test
    public void testPrintOut02()
    {
        this.outputStream.print('z');
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printOut('z');
    }

    @Test
    public void testPrintOut03()
    {
        this.outputStream.print("hello");
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printOut("hello");
    }

    @Test
    public void testPrintOut04()
    {
        this.outputStream.print("world");
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printOut("world");
    }

    @Test
    public void testPrintOut05()
    {
        Object object = new Object();

        this.outputStream.print(object);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printOut(object);
    }

    @Test
    public void testPrintOutLn01()
    {
        this.outputStream.println('c');
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printOutLn('c');
    }

    @Test
    public void testPrintOutLn02()
    {
        this.outputStream.println('z');
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printOutLn('z');
    }

    @Test
    public void testPrintOutLn03()
    {
        this.outputStream.println("hello");
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printOutLn("hello");
    }

    @Test
    public void testPrintOutLn04()
    {
        this.outputStream.println("world");
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printOutLn("world");
    }

    @Test
    public void testOutputMessage01()
    {
        this.outputStream.println("hello");
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.outputMessage("hello");
    }

    @Test
    public void testOutputMessage02()
    {
        this.outputStream.println("world");
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.outputMessage("world");
    }

    @Test
    public void testPrintOutLn05()
    {
        Object object = new Object();

        this.outputStream.println(object);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printOutLn(object);
    }

    @Test
    public void testPrintOutLn06()
    {
        this.outputStream.println();
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printOutLn();
    }

    @Test
    public void testPrintErr01()
    {
        this.errorStream.print('c');
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printErr('c');
    }

    @Test
    public void testPrintErr02()
    {
        this.errorStream.print('z');
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printErr('z');
    }

    @Test
    public void testPrintErr03()
    {
        this.errorStream.print("hello");
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printErr("hello");
    }

    @Test
    public void testPrintErr04()
    {
        this.errorStream.print("world");
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printErr("world");
    }

    @Test
    public void testPrintErr05()
    {
        Object object = new Object();

        this.errorStream.print(object);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printErr(object);
    }

    @Test
    public void testPrintErrLn01()
    {
        this.errorStream.println('c');
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printErrLn('c');
    }

    @Test
    public void testPrintErrLn02()
    {
        this.errorStream.println('z');
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printErrLn('z');
    }

    @Test
    public void testPrintErrLn03()
    {
        this.errorStream.println("hello");
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printErrLn("hello");
    }

    @Test
    public void testPrintErrLn04()
    {
        this.errorStream.println("world");
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printErrLn("world");
    }

    @Test
    public void testOutputErrorMessage01()
    {
        this.errorStream.println("hello");
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.outputErrorMessage("hello");
    }

    @Test
    public void testOutputErrorMessage02()
    {
        this.errorStream.println("world");
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.outputErrorMessage("world");
    }

    @Test
    public void testPrintErrLn05()
    {
        Object object = new Object();

        this.errorStream.println(object);
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printErrLn(object);
    }

    @Test
    public void testPrintErrLn06()
    {
        this.errorStream.println();
        EasyMock.expectLastCall();

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        this.device.printErrLn();
    }

    @Test
    public void testPromptForValidPasswordSimple01()
    {
        this.device = EasyMock.createMockBuilder(AbstractTextInterfaceDevice.class).
                withConstructor(InputStream.class, PrintStream.class, PrintStream.class).
                withArgs(this.inputStream, this.outputStream, this.errorStream).
                addMockedMethod("promptForValidPassword", int.class, int.class, String.class, String.class,
                                String.class, String.class, OutputDevice.class).
                createStrictMock();

        char[] password = "testPassword01".toCharArray();

        EasyMock.expect(
                this.device.promptForValidPassword(6, 32,
                                                   "Enter pass phrase to encrypt coolness: ",
                                                   "Verifying - Reenter pass phrase to encrypt coolness: ",
                                                   "The password must be at least six characters and no more than 32 characters long.",
                                                   "ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.",
                                                   this.device)
        ).andReturn(password);

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        char[] returned = this.device.promptForValidPassword(6, 32, "coolness");

        assertNotNull("The returned value should not be null.", returned);
        assertSame("The returned value is not correct.", password, returned);
        assertArrayEquals("The returned value should match.", "testPassword01".toCharArray(), returned);
    }

    @Test
    public void testPromptForValidPasswordSimple02()
    {
        this.device = EasyMock.createMockBuilder(AbstractTextInterfaceDevice.class).
                withConstructor(InputStream.class, PrintStream.class, PrintStream.class).
                withArgs(this.inputStream, this.outputStream, this.errorStream).
                addMockedMethod("promptForValidPassword", int.class, int.class, String.class, String.class,
                                String.class, String.class, OutputDevice.class).
                createStrictMock();

        char[] password = "anotherPassword02".toCharArray();

        EasyMock.expect(
                this.device.promptForValidPassword(10, 11,
                                                   "Enter pass phrase to encrypt flying monkeys: ",
                                                   "Verifying - Reenter pass phrase to encrypt flying monkeys: ",
                                                   "The password must be at least ten characters and no more than 11 characters long.",
                                                   "ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.",
                                                   this.device)
        ).andReturn(password);

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        char[] returned = this.device.promptForValidPassword(10, 11, "flying monkeys");

        assertNotNull("The returned value should not be null.", returned);
        assertSame("The returned value is not correct.", password, returned);
        assertArrayEquals("The returned value should match.", "anotherPassword02".toCharArray(), returned);
    }

    @Test
    public void testPromptForValidPasswordSimple03()
    {
        this.device = EasyMock.createMockBuilder(AbstractTextInterfaceDevice.class).
                withConstructor(InputStream.class, PrintStream.class, PrintStream.class).
                withArgs(this.inputStream, this.outputStream, this.errorStream).
                addMockedMethod("promptForValidPassword", int.class, int.class, String.class, String.class,
                                String.class, String.class, OutputDevice.class).
                createStrictMock();

        char[] password = "closingPassword03".toCharArray();

        EasyMock.expect(
                this.device.promptForValidPassword(-1, 0,
                                                   "Enter pass phrase to encrypt warmness: ",
                                                   "Verifying - Reenter pass phrase to encrypt warmness: ",
                                                   "The password must be at least -1 characters and no more than zero characters long.",
                                                   "ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.",
                                                   this.device)
        ).andReturn(password);

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        char[] returned = this.device.promptForValidPassword(-1, 0, "warmness");

        assertNotNull("The returned value should not be null.", returned);
        assertSame("The returned value is not correct.", password, returned);
        assertArrayEquals("The returned value should match.", "closingPassword03".toCharArray(), returned);
    }

    @Test
    public void testPromptForValidPasswordSimple04()
    {
        this.device = EasyMock.createMockBuilder(AbstractTextInterfaceDevice.class).
                withConstructor(InputStream.class, PrintStream.class, PrintStream.class).
                withArgs(this.inputStream, this.outputStream, this.errorStream).
                addMockedMethod("promptForValidPassword", int.class, int.class, String.class, String.class,
                                String.class, String.class, OutputDevice.class).
                createStrictMock();

        char[] password = "finalPassword04".toCharArray();

        EasyMock.expect(
                this.device.promptForValidPassword(5, 10,
                                                   "Enter pass phrase to encrypt the movie: ",
                                                   "Verifying - Reenter pass phrase to encrypt the movie: ",
                                                   "The password must be at least five characters and no more than ten characters long.",
                                                   "ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.",
                                                   this.device)
        ).andReturn(password);

        EasyMock.replay(this.device, this.inputStream, this.outputStream, this.errorStream);

        char[] returned = this.device.promptForValidPassword(5, 10, "the movie");

        assertNotNull("The returned value should not be null.", returned);
        assertSame("The returned value is not correct.", password, returned);
        assertArrayEquals("The returned value should match.", "finalPassword04".toCharArray(), returned);
    }
}