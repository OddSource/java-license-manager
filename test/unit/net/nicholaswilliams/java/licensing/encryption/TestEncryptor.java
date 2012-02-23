/*
 * TestEncryptor.java from LicenseManager modified Thursday, February 23, 2012 15:33:21 CST (-0600).
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

package net.nicholaswilliams.java.licensing.encryption;

import net.nicholaswilliams.java.licensing.exception.FailedToDecryptException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Test class for Encryptor.
 */
public class TestEncryptor
{
	@Before
	public void setUp()
	{

	}

	@After
	public void tearDown()
	{

	}

	@Test
	public void testConstructionForbidden()
			throws IllegalAccessException, InstantiationException, NoSuchMethodException
	{
		Constructor<Encryptor> constructor = Encryptor.class.getDeclaredConstructor();
		constructor.setAccessible(true);

		try
		{
			constructor.newInstance();
			fail("Expected exception java.lang.reflect.InvocationTargetException, but got no exception.");
		}
		catch(InvocationTargetException e)
		{
			Throwable cause = e.getCause();
			assertNotNull("Expected cause for InvocationTargetException, but got no cause.", cause);
			assertSame("Expected exception java.lang.RuntimeException, but got " + cause.getClass(), RuntimeException.class, cause.getClass());
			assertEquals("The message was incorrect.", "This class cannot be instantiated.", cause.getMessage());
		}
	}

	private byte[] getPadded(String toPad, int length) throws Exception
	{
		Method pad = Encryptor.class.getDeclaredMethod("pad", byte[].class, int.class);
		pad.setAccessible(true);
		return (byte[])pad.invoke(null, toPad.getBytes(), length);
	}

	@Test
	public void testPad1() throws Exception
	{
		String toPad = "12345678901234567890";
		int length = 10;

		byte[] padded = this.getPadded(toPad, length);
		String paddedString = new String(padded);
		paddedString = paddedString.substring(0, paddedString.length() - 1);

		assertNotNull("The padded string should not be null.", padded);
		assertEquals("The padded string has the wrong length.", toPad.length() + 1, padded.length);
		assertEquals("The padded string should actually just be identical.", toPad, paddedString);
		assertEquals("The last byte is wrong.", 1, (int)padded[padded.length - 1]);
	}

	private void testPad2(int length) throws Exception
	{
		String toPad = "g1nger";

		byte[] padded = this.getPadded(toPad, length);

		assertNotNull("The padded string should not be null.", padded);
		assertEquals("The padded string has the wrong length.", length + 1, padded.length);
		assertEquals("The last byte is wrong.", length - toPad.length() + 1, (int)padded[padded.length - 1]);
	}

	@Test
	public void testPad10() throws Exception
	{
		testPad2(10);
	}

	@Test
	public void testPad11() throws Exception
	{
		testPad2(11);
	}

	@Test
	public void testPad13() throws Exception
	{
		testPad2(13);
	}

	@Test
	public void testPad17() throws Exception
	{
		testPad2(17);
	}

	@Test
	public void testPad19() throws Exception
	{
		testPad2(19);
	}

	@Test
	public void testPad23() throws Exception
	{
		testPad2(23);
	}

	@Test
	public void testPad29() throws Exception
	{
		testPad2(29);
	}

	@Test
	@SuppressWarnings("PrimitiveArrayArgumentToVariableArgMethod")
	public void testUnPad1() throws Exception
	{
		byte[] tokens = {
				'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
				'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', (byte)1
		};
		String toUnPad = new String(tokens);

		Method pad = Encryptor.class.getDeclaredMethod("unPad", byte[].class);
		pad.setAccessible(true);

		byte[] unPadded = (byte[])pad.invoke(null, toUnPad.getBytes());
		String unPaddedString = new String(unPadded);

		assertNotNull("The unPadded string should not be null.", unPadded);
		assertEquals("The unPadded string is not correct.", "12345678901234567890", unPaddedString);
	}

	@Test
	@SuppressWarnings("PrimitiveArrayArgumentToVariableArgMethod")
	public void testUnPad2() throws Exception
	{
		byte[] tokens = { 'g', '1', 'n', 'g', 'e', 'r', (byte)1 };
		String toUnPad = new String(tokens);

		Method pad = Encryptor.class.getDeclaredMethod("unPad", byte[].class);
		pad.setAccessible(true);

		byte[] unPadded = (byte[])pad.invoke(null, toUnPad.getBytes());
		String unPaddedString = new String(unPadded);

		assertNotNull("The unPadded string should not be null.", unPadded);
		assertEquals("The unPadded string is not correct.", "g1nger", unPaddedString);
	}

	@SuppressWarnings("PrimitiveArrayArgumentToVariableArgMethod")
	private void testUnPad3(int length) throws Exception
	{
		String toPad = "g1nger";

		byte[] padded = this.getPadded(toPad, length);

		Method pad = Encryptor.class.getDeclaredMethod("unPad", byte[].class);
		pad.setAccessible(true);

		byte[] unPadded = (byte[])pad.invoke(null, padded);
		String unPaddedString = new String(unPadded);

		assertNotNull("The unPadded string should not be null.", unPadded);
		assertEquals("The unPadded string is not correct.", toPad, unPaddedString);
	}

	@Test
	public void testUnPad10() throws Exception
	{
		testUnPad3(10);
	}

	@Test
	public void testUnPad11() throws Exception
	{
		testUnPad3(11);
	}

	@Test
	public void testUnPad13() throws Exception
	{
		testUnPad3(13);
	}

	@Test
	public void testUnPad17() throws Exception
	{
		testUnPad3(17);
	}

	@Test
	public void testUnPad19() throws Exception
	{
		testUnPad3(19);
	}

	@Test
	public void testUnPad23() throws Exception
	{
		testUnPad3(23);
	}

	@Test
	public void testUnPad29() throws Exception
	{
		testUnPad3(29);
	}

	// Test for difference/encryptability
	@Test
	public void testEncrypt1()
	{
		String toEncrypt = "g1nger";

		String encrypted = Encryptor.encrypt(toEncrypt);
		assertNotNull("The encrypted string should not be null.", encrypted);
		assertTrue("The encrypted string should have length greater than the original string.",
				encrypted.length() > toEncrypt.length());
	}

	// Test for difference/encryptability
	@Test
	public void testPasswordBasedEncrypt1()
	{
		String toEncrypt = "g1nger";

		String encrypted = Encryptor.encrypt(toEncrypt, "myCoolPassword".toCharArray());
		assertNotNull("The encrypted string should not be null.", encrypted);
		assertTrue("The encrypted string should have length greater than the original string.",
				encrypted.length() > toEncrypt.length());
	}

	// Test for repeatability
	@Test
	public void testEncrypt2()
	{
		String toEncrypt = "aoeugalogeual9o87gu3l2'r3ghsproguhSNoehusNOEhusntoehoUZTOHEUNT<>" +
				"H<(>*JGJCROEHunt.huaahsnoeuhs,cr.a,g.ulreo8gukchrseunhaonteihaorceugaroscuh" +
				".nsphilraeugil98g'l38rgph's23cnhps	.c,huiscraoeha/l-iuh.p-sihtaosntiha.,edo" +
				"guoeuar/oe-suihbtnaoseihtenoatnuaoehik/eolrkl=alhil.,]'ry3c012]cp5/239cpyhl" +
				".',-huilreoh/eho/ik0hceo/ikcrih.,0/igc34yyr2/34gy8[p90.8icgeurlxhkelocugxu0" +
				"egoi/crleohil/p',hy/1c'gy029348y[.p,90gcileo;hl/GLRCDHIRCH)($*:(?YC{>)(i8e9" +
				"8cloraihl'hy34/l'cg809[i8u09/10ch32clr5hps'th,.tsnuihaeokc8gao[e09ig8caoelc" +
				"huitnsoeabitnoeudh/lroeuceucog8xxgp,0ldynst43htnys5hhy543tsnh65crl234hyig8o" +
				"eu90[euo8icgrheoistahoeaehosnuthoelrcigoeu[09icglerpuhdtnoseudh,ocilc0oeu78" +
				"90[e8oi2349cg5hcl234h5l/r2c43/lyr.cpg,lrcioghersueusih";

		String encrypted1 = Encryptor.encrypt(toEncrypt);
		String encrypted2 = Encryptor.encrypt(toEncrypt);
		String encrypted3 = Encryptor.encrypt(toEncrypt);
		String encrypted4 = Encryptor.encrypt(toEncrypt);

		assertNotNull("The encrypted string should not be null.", encrypted1);
		assertNotNull("The encrypted string should not be null.", encrypted2);
		assertNotNull("The encrypted string should not be null.", encrypted3);
		assertNotNull("The encrypted string should not be null.", encrypted4);

		assertEquals("The encrypted string is not the same.", encrypted1, encrypted2);
		assertEquals("The encrypted string is not the same.", encrypted1, encrypted3);
		assertEquals("The encrypted string is not the same.", encrypted1, encrypted4);
		assertEquals("The encrypted string is not the same.", encrypted2, encrypted3);
		assertEquals("The encrypted string is not the same.", encrypted2, encrypted4);
		assertEquals("The encrypted string is not the same.", encrypted3, encrypted4);
	}

	// Test for repeatability
	@Test
	public void testPasswordBasedEncrypt2()
	{
		String toEncrypt = "aoeugalogeual9o87gu3l2'r3ghsproguhSNoehusNOEhusntoehoUZTOHEUNT<>" +
				"H<(>*JGJCROEHunt.huaahsnoeuhs,cr.a,g.ulreo8gukchrseunhaonteihaorceugaroscuh" +
				".nsphilraeugil98g'l38rgph's23cnhps	.c,huiscraoeha/l-iuh.p-sihtaosntiha.,edo" +
				"guoeuar/oe-suihbtnaoseihtenoatnuaoehik/eolrkl=alhil.,]'ry3c012]cp5/239cpyhl" +
				".',-huilreoh/eho/ik0hceo/ikcrih.,0/igc34yyr2/34gy8[p90.8icgeurlxhkelocugxu0" +
				"egoi/crleohil/p',hy/1c'gy029348y[.p,90gcileo;hl/GLRCDHIRCH)($*:(?YC{>)(i8e9" +
				"8cloraihl'hy34/l'cg809[i8u09/10ch32clr5hps'th,.tsnuihaeokc8gao[e09ig8caoelc" +
				"huitnsoeabitnoeudh/lroeuceucog8xxgp,0ldynst43htnys5hhy543tsnh65crl234hyig8o" +
				"eu90[euo8icgrheoistahoeaehosnuthoelrcigoeu[09icglerpuhdtnoseudh,ocilc0oeu78" +
				"90[e8oi2349cg5hcl234h5l/r2c43/lyr.cpg,lrcioghersueusih";

		String encrypted1 = Encryptor.encrypt(toEncrypt, "myOtherPassword2".toCharArray());
		String encrypted2 = Encryptor.encrypt(toEncrypt, "myOtherPassword2".toCharArray());
		String encrypted3 = Encryptor.encrypt(toEncrypt, "yourOtherPassword2".toCharArray());
		String encrypted4 = Encryptor.encrypt(toEncrypt, "yourOtherPassword2".toCharArray());

		assertNotNull("The encrypted string should not be null.", encrypted1);
		assertNotNull("The encrypted string should not be null.", encrypted2);
		assertNotNull("The encrypted string should not be null.", encrypted3);
		assertNotNull("The encrypted string should not be null.", encrypted4);

		assertEquals("The encrypted string is not the same.", encrypted1, encrypted2);
		assertEquals("The encrypted string is not the same.", encrypted3, encrypted4);
		assertFalse("The encrypted string is not the same.", encrypted1.equals(encrypted3));
		assertFalse("The encrypted string is not the same.", encrypted1.equals(encrypted4));
		assertFalse("The encrypted string is not the same.", encrypted2.equals(encrypted3));
		assertFalse("The encrypted string is not the same.", encrypted2.equals(encrypted4));
	}

	// Test for expectation
	@Test
	public void testEncrypt3()
	{
		String expected = "NSARPhey360gZ2xTOaswGn6lWCd01poo1AW8o0Oobudbf2MNe38lX21EpHEFGl-MKgZnPZKsSHrDFXjL1CvtWqQ2JN6RuMKW0iht1TacVKE";

		String toEncrypt = "testAPasswordThatIsLongerThanThePaddingLengthToPreventRandomness";

		String encrypted = Encryptor.encrypt(toEncrypt);
		assertNotNull("The encrypted string should not be null.", encrypted);
		assertEquals("The encrypted string is not the same.", expected, encrypted);
	}

	// Test for expectation
	@Test
	public void testPasswordBasedEncrypt3()
	{
		String expected = "YPEXhIzBx1VzkbqMwaS2QpMPQJCY9kALXLYY0HFWRCSpRobyCrAGLtwNPnEKDW_TKMSRzf7BAUHFueySb_dh2tISTe8zx-uZ_WG-c06x2Ns";

		String toEncrypt = "testAPasswordThatIsLongerThanThePaddingLengthToPreventRandomness";

		String encrypted = Encryptor.encrypt(toEncrypt, "lastEncryptionPassword3".toCharArray());
		assertNotNull("The encrypted string should not be null.", encrypted);
		assertEquals("The encrypted string is not the same.", expected, encrypted);
	}

	@Test
	public void testDecrypt1()
	{
		String toEncrypt = "foo";

		String encrypted = Encryptor.encrypt(toEncrypt);
		assertNotNull("The encrypted string should not be null.", encrypted);

		String decrypted = Encryptor.decrypt(encrypted);
		assertNotNull("The decrypted string should not be null.", decrypted);
		assertFalse("The decrypted string should not equal the encrypted string.", encrypted.equals(decrypted));
		assertEquals("The decrypted string should equal the original string.", toEncrypt, decrypted);
	}

	@Test
	public void testPasswordBasedDecrypt1()
	{
		String toEncrypt = "foo";

		String encrypted = Encryptor.encrypt(toEncrypt, "myDecryptPassword1".toCharArray());
		assertNotNull("The encrypted string should not be null.", encrypted);

		// right password
		String decrypted = Encryptor.decrypt(encrypted, "myDecryptPassword1".toCharArray());
		assertNotNull("The decrypted string should not be null.", decrypted);
		assertFalse("The decrypted string should not equal the encrypted string.", encrypted.equals(decrypted));
		assertEquals("The decrypted string should equal the original string.", toEncrypt, decrypted);

		// wrong password
		try {
			Encryptor.decrypt(encrypted, "MyDecryptPassword1".toCharArray());
			fail("Expected FailedToDecryptException, but no exception thrown.");
		} catch(FailedToDecryptException e) {
			// this is good
		} catch(Throwable t) {
			fail("Expected FailedToDecryptException, but got: " + t.toString());
		}
	}

	@Test
	public void testDecrypt2()
	{
		String toEncrypt = "g1nger";

		String encrypted = Encryptor.encrypt(toEncrypt);
		assertNotNull("The encrypted string should not be null.", encrypted);

		String decrypted = Encryptor.decrypt(encrypted);
		assertNotNull("The decrypted string should not be null.", decrypted);
		assertFalse("The decrypted string should not equal the encrypted string.", encrypted.equals(decrypted));
		assertEquals("The decrypted string should equal the original string.", toEncrypt, decrypted);
	}

	@Test
	public void testPasswordBasedDecrypt2()
	{
		String toEncrypt = "g1nger";

		String encrypted = Encryptor.encrypt(toEncrypt, "yourDecryptPassword2".toCharArray());
		assertNotNull("The encrypted string should not be null.", encrypted);

		// right password
		String decrypted = Encryptor.decrypt(encrypted, "yourDecryptPassword2".toCharArray());
		assertNotNull("The decrypted string should not be null.", decrypted);
		assertFalse("The decrypted string should not equal the encrypted string.", encrypted.equals(decrypted));
		assertEquals("The decrypted string should equal the original string.", toEncrypt, decrypted);

		// wrong password
		try {
			Encryptor.decrypt(encrypted, "YourDecryptPassword2".toCharArray());
			fail("Expected FailedToDecryptException, but no exception thrown.");
		} catch(FailedToDecryptException e) {
			// this is good
		} catch(Throwable t) {
			fail("Expected FailedToDecryptException, but got: " + t.toString());
		}
	}

	@Test
	public void testDecrypt3()
	{
		String toEncrypt = "testAPasswordThatIsLongerThanThePaddingLengthToPreventRandomness";

		String encrypted = Encryptor.encrypt(toEncrypt);
		assertNotNull("The encrypted string should not be null.", encrypted);

		String decrypted = Encryptor.decrypt(encrypted);
		assertNotNull("The decrypted string should not be null.", decrypted);
		assertFalse("The decrypted string should not equal the encrypted string.", encrypted.equals(decrypted));
		assertEquals("The decrypted string should equal the original string.", toEncrypt, decrypted);
	}

	@Test
	public void testPasswordBasedDecrypt3()
	{
		String toEncrypt = "testAPasswordThatIsLongerThanThePaddingLengthToPreventRandomness";

		String encrypted = Encryptor.encrypt(toEncrypt, "hisdecryptpassword3".toCharArray());
		assertNotNull("The encrypted string should not be null.", encrypted);

		// right password
		String decrypted = Encryptor.decrypt(encrypted, "hisdecryptpassword3".toCharArray());
		assertNotNull("The decrypted string should not be null.", decrypted);
		assertFalse("The decrypted string should not equal the encrypted string.", encrypted.equals(decrypted));
		assertEquals("The decrypted string should equal the original string.", toEncrypt, decrypted);

		// wrong password
		try {
			Encryptor.decrypt(encrypted, "hisDecryptPassword3".toCharArray());
			fail("Expected FailedToDecryptException, but no exception thrown.");
		} catch(FailedToDecryptException e) {
			// this is good
		} catch(Throwable t) {
			fail("Expected FailedToDecryptException, but got: " + t.toString());
		}
	}

	@Test
	public void testDecrypt4()
	{
		String toEncrypt = "aoeugalogeual9o87gu3l2'r3ghsproguhSNoehusNOEhusntoehoUZTOHEUNT<>" +
				"H<(>*JGJCROEHunt.huaahsnoeuhs,cr.a,g.ulreo8gukchrseunhaonteihaorceugaroscuh" +
				".nsphilraeugil98g'l38rgph's23cnhps	.c,huiscraoeha/l-iuh.p-sihtaosntiha.,edo" +
				"guoeuar/oe-suihbtnaoseihtenoatnuaoehik/eolrkl=alhil.,]'ry3c012]cp5/239cpyhl" +
				".',-huilreoh/eho/ik0hceo/ikcrih.,0/igc34yyr2/34gy8[p90.8icgeurlxhkelocugxu0" +
				"egoi/crleohil/p',hy/1c'gy029348y[.p,90gcileo;hl/GLRCDHIRCH)($*:(?YC{>)(i8e9" +
				"8cloraihl'hy34/l'cg809[i8u09/10ch32clr5hps'th,.tsnuihaeokc8gao[e09ig8caoelc" +
				"huitnsoeabitnoeudh/lroeuceucog8xxgp,0ldynst43htnys5hhy543tsnh65crl234hyig8o" +
				"eu90[euo8icgrheoistahoeaehosnuthoelrcigoeu[09icglerpuhdtnoseudh,ocilc0oeu78" +
				"90[e8oi2349cg5hcl234h5l/r2c43/lyr.cpg,lrcioghersueusih";

		String encrypted = Encryptor.encrypt(toEncrypt);
		assertNotNull("The encrypted string should not be null.", encrypted);

		String decrypted = Encryptor.decrypt(encrypted);
		assertNotNull("The decrypted string should not be null.", decrypted);
		assertFalse("The decrypted string should not equal the encrypted string.", encrypted.equals(decrypted));
		assertEquals("The decrypted string should equal the original string.", toEncrypt, decrypted);
	}

	@Test
	public void testPasswordBasedDecrypt4()
	{
		String toEncrypt = "aoeugalogeual9o87gu3l2'r3ghsproguhSNoehusNOEhusntoehoUZTOHEUNT<>" +
				"H<(>*JGJCROEHunt.huaahsnoeuhs,cr.a,g.ulreo8gukchrseunhaonteihaorceugaroscuh" +
				".nsphilraeugil98g'l38rgph's23cnhps	.c,huiscraoeha/l-iuh.p-sihtaosntiha.,edo" +
				"guoeuar/oe-suihbtnaoseihtenoatnuaoehik/eolrkl=alhil.,]'ry3c012]cp5/239cpyhl" +
				".',-huilreoh/eho/ik0hceo/ikcrih.,0/igc34yyr2/34gy8[p90.8icgeurlxhkelocugxu0" +
				"egoi/crleohil/p',hy/1c'gy029348y[.p,90gcileo;hl/GLRCDHIRCH)($*:(?YC{>)(i8e9" +
				"8cloraihl'hy34/l'cg809[i8u09/10ch32clr5hps'th,.tsnuihaeokc8gao[e09ig8caoelc" +
				"huitnsoeabitnoeudh/lroeuceucog8xxgp,0ldynst43htnys5hhy543tsnh65crl234hyig8o" +
				"eu90[euo8icgrheoistahoeaehosnuthoelrcigoeu[09icglerpuhdtnoseudh,ocilc0oeu78" +
				"90[e8oi2349cg5hcl234h5l/r2c43/lyr.cpg,lrcioghersueusih";

		String encrypted = Encryptor.encrypt(toEncrypt, "herdecryptpassword3".toCharArray());
		assertNotNull("The encrypted string should not be null.", encrypted);

		// right password
		String decrypted = Encryptor.decrypt(encrypted, "herdecryptpassword3".toCharArray());
		assertNotNull("The decrypted string should not be null.", decrypted);
		assertFalse("The decrypted string should not equal the encrypted string.", encrypted.equals(decrypted));
		assertEquals("The decrypted string should equal the original string.", toEncrypt, decrypted);

		// wrong password
		try {
			Encryptor.decrypt(encrypted, "herDecryptPassword3".toCharArray());
			fail("Expected FailedToDecryptException, but no exception thrown.");
		} catch(FailedToDecryptException e) {
			// this is good
		} catch(Throwable t) {
			fail("Expected FailedToDecryptException, but got: " + t.toString());
		}
	}
}
