/*
 * TestAbstractPasswordPrompter.java from LicenseManager modified Thursday, January 24, 2013 15:08:25 CST (-0600).
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

package io.oddsource.java.licensing.licensor.interfaces.spi;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for AbstractPasswordPrompter.
 */
public class TestAbstractPasswordPrompter
{
    private AbstractPasswordPrompter prompter;

    @Before
    public void setUp()
    {
        this.prompter = EasyMock.createMockBuilder(AbstractPasswordPrompter.class).withConstructor().createStrictMock();
    }

    @After
    public void tearDown()
    {
        EasyMock.verify(this.prompter);
    }

    @Test
    public void testPasswordsMatch01()
    {
        EasyMock.replay(this.prompter);

        assertTrue("The passwords should match.", this.prompter.passwordsMatch(
                new char[] {'s', 'a', 'm', 'p', 'l', 'e', 'K', 'e', 'y', '1', '9', '8', '4'},
                new char[] {'s', 'a', 'm', 'p', 'l', 'e', 'K', 'e', 'y', '1', '9', '8', '4'}
        ));
    }

    @Test
    public void testPasswordsMatch02()
    {
        EasyMock.replay(this.prompter);

        assertTrue("The passwords should match.", this.prompter.passwordsMatch(
                new char[] {'y', 'o', 'u', 'r', 'K', 'e', 'y', '1', '9', '4', '8'},
                new char[] {'y', 'o', 'u', 'r', 'K', 'e', 'y', '1', '9', '4', '8'}
        ));
    }

    @Test
    public void testPasswordsMatch03()
    {
        EasyMock.replay(this.prompter);

        assertFalse("The passwords should not match.", this.prompter.passwordsMatch(
                new char[] {'s', 'a', 'm', 'p', 'l', 'e', 'K', 'e', 'y', '1', '9', '8', '4'},
                new char[] {'S', 'a', 'm', 'p', 'l', 'e', 'K', 'e', 'y', '1', '9', '8', '4'}
        ));
    }

    @Test
    public void testPasswordsMatch04()
    {
        EasyMock.replay(this.prompter);

        assertFalse("The passwords should not match.", this.prompter.passwordsMatch(
                new char[] { 'y', 'o', 'u', 'r', 'K', 'e', 'y', '1', '9', '4', '8' },
                new char[] { 'y', 'o', 'u', 'r', 'K', 'e', 'y', '1', '9', '4', '9' }
                                                                                   ));
    }

    @Test
    public void testPasswordsMatch05()
    {
        EasyMock.replay(this.prompter);

        assertFalse("The passwords should not match.", this.prompter.passwordsMatch(
                new char[] { 'y', 'o', 'u', 'r', 'K', 'e', 'y', '1', '9', '4', '8' },
                new char[] { 'y' }
                                                                                   ));
    }

    @Test
    public void testPromptForValidPassword01()
    {
        OutputDevice device = EasyMock.createStrictMock(OutputDevice.class);

        char[] password1 = "testPassword01".toCharArray();
        EasyMock.expect(this.prompter.readPassword("Enter pass phrase to encrypt both keys: ")).andReturn(password1);

        char[] password2 = "testPassword01".toCharArray();
        EasyMock.expect(this.prompter.readPassword("Verifying - Reenter pass phrase to encrypt both keys: ")).andReturn(
                password2);

        EasyMock.replay(this.prompter, device);

        char[] password = this.prompter.promptForValidPassword(6, 32,
                                                               "Enter pass phrase to encrypt both keys: ",
                                                               "Verifying - Reenter pass phrase to encrypt both keys: ",
                                                               "The password must be at least six characters and no more than 32 characters long.",
                                                               "ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.",
                                                               device);

        assertArrayEquals("The password is not correct.", "testPassword01".toCharArray(), password);
        assertNotSame("The arrays should be different objects (1).", password1, password);
        assertNotSame("The arrays should be different objects (2).", password2, password);
        assertFalse("The arrays should not equal each other (1).", new String(password1).equals(new String(password)));
        assertFalse("The arrays should not equal each other (2).", new String(password2).equals(new String(password)));

        EasyMock.verify(device);
    }

    @Test
    public void testPromptForValidPassword02()
    {
        OutputDevice device = EasyMock.createStrictMock(OutputDevice.class);

        char[] password1 = "test2".toCharArray();
        EasyMock.expect(this.prompter.readPassword("Enter pass phrase to encrypt the private key: ")).andReturn(
                password1);
        device.outputErrorMessage("The password must be at least seven characters and no more than 30 characters long.");
        EasyMock.expectLastCall();
        password1 = "123456789012345678901234567890123".toCharArray();
        EasyMock.expect(this.prompter.readPassword("Enter pass phrase to encrypt the private key: ")).andReturn(
                password1);
        device.outputErrorMessage("The password must be at least seven characters and no more than 30 characters long.");
        EasyMock.expectLastCall();
        password1 = "123456789012345678901234567890".toCharArray();
        EasyMock.expect(this.prompter.readPassword("Enter pass phrase to encrypt the private key: ")).andReturn(password1);

        char[] password2 = "testPassword01".toCharArray();
        EasyMock.expect(this.prompter.readPassword("Verifying - Reenter pass phrase to encrypt the private key: ")).andReturn(
                password2);
        device.outputErrorMessage("ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.");
        EasyMock.expectLastCall();

        password1 = "test02a".toCharArray();
        EasyMock.expect(this.prompter.readPassword("Enter pass phrase to encrypt the private key: ")).andReturn(password1);

        password2 = "test02a".toCharArray();
        EasyMock.expect(this.prompter.readPassword("Verifying - Reenter pass phrase to encrypt the private key: ")).andReturn(
                password2);

        EasyMock.replay(this.prompter, device);

        char[] password = this.prompter.promptForValidPassword(7, 30,
                                                               "Enter pass phrase to encrypt the private key: ",
                                                               "Verifying - Reenter pass phrase to encrypt the private key: ",
                                                               "The password must be at least seven characters and no more than 30 characters long.",
                                                               "ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.",
                                                               device);

        assertArrayEquals("The password is not correct.", "test02a".toCharArray(), password);
        assertNotSame("The arrays should be different objects (1).", password1, password);
        assertNotSame("The arrays should be different objects (2).", password2, password);
        assertFalse("The arrays should not equal each other (1).", new String(password1).equals(new String(password)));
        assertFalse("The arrays should not equal each other (2).", new String(password2).equals(new String(password)));

        EasyMock.verify(device);
    }

    @Test
    public void testPromptForValidPassword03()
    {
        OutputDevice device = EasyMock.createStrictMock(OutputDevice.class);

        char[] password1 = "another03".toCharArray();
        EasyMock.expect(this.prompter.readPassword("Enter pass phrase to encrypt the public key: ")).andReturn(password1);

        char[] password2 = "testPassword01".toCharArray();
        EasyMock.expect(this.prompter.readPassword("Verifying - Reenter pass phrase to encrypt the public key: ")).andReturn(
                password2);
        device.outputErrorMessage("ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.");
        EasyMock.expectLastCall();

        EasyMock.expect(this.prompter.readPassword("Enter pass phrase to encrypt the public key: ")).andReturn(password1);

        password2 = "another03".toCharArray();
        EasyMock.expect(this.prompter.readPassword("Verifying - Reenter pass phrase to encrypt the public key: ")).andReturn(
                password2);

        EasyMock.replay(this.prompter, device);

        char[] password = this.prompter.promptForValidPassword(6, 32,
                                                               "Enter pass phrase to encrypt the public key: ",
                                                               "Verifying - Reenter pass phrase to encrypt the public key: ",
                                                               "The password must be at least six characters and no more than 32 characters long.",
                                                               "ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.",
                                                               device);

        assertArrayEquals("The password is not correct.", "another03".toCharArray(), password);
        assertNotSame("The arrays should be different objects (1).", password1, password);
        assertNotSame("The arrays should be different objects (2).", password2, password);
        assertFalse("The arrays should not equal each other (1).", new String(password1).equals(new String(password)));
        assertFalse("The arrays should not equal each other (2).", new String(password2).equals(new String(password)));

        EasyMock.verify(device);
    }

    @Test
    public void testPromptForValidPassword04()
    {
        OutputDevice device = EasyMock.createStrictMock(OutputDevice.class);

        char[] password1 = "test".toCharArray();
        EasyMock.expect(this.prompter.readPassword("Enter pass phrase to encrypt both keys: ")).andReturn(password1);
        device.outputErrorMessage("The password must be at least eight characters and no more than 48 characters long.");
        EasyMock.expectLastCall();
        password1 = "finalTest04".toCharArray();
        EasyMock.expect(this.prompter.readPassword("Enter pass phrase to encrypt both keys: ")).andReturn(password1);

        char[] password2 = "finalTest04".toCharArray();
        EasyMock.expect(this.prompter.readPassword("Verifying - Reenter pass phrase to encrypt both keys: ")).andReturn(
                password2);

        EasyMock.replay(this.prompter, device);

        char[] password = this.prompter.promptForValidPassword(8, 48,
                                                               "Enter pass phrase to encrypt both keys: ",
                                                               "Verifying - Reenter pass phrase to encrypt both keys: ",
                                                               "The password must be at least eight characters and no more than 48 characters long.",
                                                               "ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.",
                                                               device);

        assertArrayEquals("The password is not correct.", "finalTest04".toCharArray(), password);
        assertNotSame("The arrays should be different objects (1).", password1, password);
        assertNotSame("The arrays should be different objects (2).", password2, password);
        assertFalse("The arrays should not equal each other (1).", new String(password1).equals(new String(password)));
        assertFalse("The arrays should not equal each other (2).", new String(password2).equals(new String(password)));

        EasyMock.verify(device);
    }

    @Test
    public void testPromptForValidPassword05()
    {
        OutputDevice device = EasyMock.createStrictMock(OutputDevice.class);

        EasyMock.expect(this.prompter.readPassword("Enter pass phrase to encrypt both keys: ")).
                andReturn("".toCharArray());

        EasyMock.expect(this.prompter.readPassword("Verifying - Reenter pass phrase to encrypt both keys: ")).
                andReturn("".toCharArray());

        EasyMock.replay(this.prompter, device);

        char[] password = this.prompter.promptForValidPassword(0, 32,
                                                               "Enter pass phrase to encrypt both keys: ",
                                                               "Verifying - Reenter pass phrase to encrypt both keys: ",
                                                               "The password must be at least zero characters and no more than 32 characters long.",
                                                               "ERROR: Passwords do not match. Please try again, or press Ctrl+C to cancel.",
                                                               device);

        assertArrayEquals("The password is not correct.", "".toCharArray(), password);

        EasyMock.verify(device);
    }
}