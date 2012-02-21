/*
 * TestRSAKeyPairGenerator.java from LicenseManager modified Monday, February 20, 2012 22:53:01 CST (-0600).
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

import net.nicholaswilliams.java.licensing.licensor.PrivateKeyDataProvider;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for RSAKeyPairGenerator.
 */
public class TestRSAKeyPairGenerator
{
	private RSAKeyPairGenerator generator;

	public TestRSAKeyPairGenerator()
	{
		this.generator = new RSAKeyPairGenerator();
	}

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		
	}

	@AfterClass
	public static void tearDownClass() throws Exception
	{

	}

	@Before
	public void setUp()
	{

	}

	@After
	public void tearDown()
	{

	}

	@Test
	public void testPasswordsMatch01()
	{
		assertTrue("The passwords should match.", this.generator.passwordsMatch(
				new char[] {'s', 'a', 'm', 'p', 'l', 'e', 'K', 'e', 'y', '1', '9', '8', '4'},
				new char[] {'s', 'a', 'm', 'p', 'l', 'e', 'K', 'e', 'y', '1', '9', '8', '4'}
		));
	}

	@Test
	public void testPasswordsMatch02()
	{
		assertTrue("The passwords should match.", this.generator.passwordsMatch(
				new char[] {'y', 'o', 'u', 'r', 'K', 'e', 'y', '1', '9', '4', '8'},
				new char[] {'y', 'o', 'u', 'r', 'K', 'e', 'y', '1', '9', '4', '8'}
		));
	}

	@Test
	public void testPasswordsMatch03()
	{
		assertFalse("The passwords should not match.", this.generator.passwordsMatch(
				new char[] {'s', 'a', 'm', 'p', 'l', 'e', 'K', 'e', 'y', '1', '9', '8', '4'},
				new char[] {'S', 'a', 'm', 'p', 'l', 'e', 'K', 'e', 'y', '1', '9', '8', '4'}
		));
	}

	@Test
	public void testPasswordsMatch04()
	{
		assertFalse("The passwords should not match.", this.generator.passwordsMatch(
				new char[] {'y', 'o', 'u', 'r', 'K', 'e', 'y', '1', '9', '4', '8'},
				new char[] {'y', 'o', 'u', 'r', 'K', 'e', 'y', '1', '9', '4', '9'}
		));
	}

	@Test
	public void testPasswordsMatch05()
	{
		assertFalse("The passwords should not match.", this.generator.passwordsMatch(
				new char[] {'y', 'o', 'u', 'r', 'K', 'e', 'y', '1', '9', '4', '8'},
				new char[] {'y'}
		));
	}

	@Test
	public void testGetKeyPair01()
	{
		KeyPair keyPair = this.generator.generateKeyPair();

		assertNotNull("The key pair should not be null.", keyPair);

		assertEquals("The algorithm is not correct.", KeyFileUtilities.keyAlgorithm,
				keyPair.getPrivate().getAlgorithm());
		assertEquals("The algorithm is not correct.", KeyFileUtilities.keyAlgorithm,
				keyPair.getPublic().getAlgorithm());
	}

	@Test
	public void testSaveKeyPairToFiles01() throws IOException
	{
		KeyPair keyPair = this.generator.generateKeyPair();

		File file1 = new File("testSaveKeyPairToFiles01.private");
		File file2 = new File("testSaveKeyPairToFiles01.public");

		if(file1.exists())
			FileUtils.forceDelete(file1);

		if(file2.exists())
			FileUtils.forceDelete(file2);

		assertFalse("File 1 should not exist.", file1.exists());
		assertFalse("File 2 should not exist.", file2.exists());

		this.generator.saveKeyPairToFiles(
				keyPair,
				"testSaveKeyPairToFiles01.private",
				"testSaveKeyPairToFiles01.public",
				"testMyPassword01".toCharArray()
		);

		assertTrue("File 1 should exist.", file1.exists());
		assertTrue("File 2 should exist.", file2.exists());

		FileUtils.forceDelete(file1);
		FileUtils.forceDelete(file2);
	}

	@Test
	public void testByteArrayToIntArray01()
	{
		int[] array = this.generator.byteArrayToIntArray(new byte[] { 0x01, 0x06, 0x3F, 0x7A, 0x15, 0x41 });

		assertNotNull("The int array should not be null.", array);
		assertTrue("The int array should have length.", array.length > 0);
		assertArrayEquals("The array is not correct.", array, new int[] { 0x01, 0x06, 0x3F, 0x7A, 0x15, 0x41 });
	}

	@Test
	public void testByteArrayToIntArray02()
	{
		int[] array = this.generator.byteArrayToIntArray(new byte[] { 0x7A, 0x41, 0x02, 0x04, 0x06 });

		assertNotNull("The int array should not be null.", array);
		assertTrue("The int array should have length.", array.length > 0);
		assertArrayEquals("The array is not correct.", array, new int[] { 0x7A, 0x41, 0x02, 0x04, 0x06 });
	}

	@Test
	public void testCharArrayToIntArray01()
	{
		int[] array = this.generator.charArrayToIntArray(new char[] { 0x01, 0x06, 0x3F, 0x7A, 0x15, 0x41 });

		assertNotNull("The int array should not be null.", array);
		assertTrue("The int array should have length.", array.length > 0);
		assertArrayEquals("The array is not correct.", array, new int[] { 0x01, 0x06, 0x3F, 0x7A, 0x15, 0x41 });
	}

	@Test
	public void testCharArrayToIntArray02()
	{
		int[] array = this.generator.charArrayToIntArray(new char[] { 0x7A, 0x41, 0x02, 0x04, 0x06 });

		assertNotNull("The int array should not be null.", array);
		assertTrue("The int array should have length.", array.length > 0);
		assertArrayEquals("The array is not correct.", array, new int[] { 0x7A, 0x41, 0x02, 0x04, 0x06 });
	}

	@Test
	public void testArrayCodeToString01()
	{
		String code = this.generator.arrayToCodeString(new int[] { 0x01, 0x06, 0x3F, 0x7A, -128, 0x41 }, "byte");

		assertNotNull("The code should not be null.", code);
		assertTrue("The code should have length.", code.length() > 0);
		assertEquals("The code is not correct.",
					 "new byte[] {\n\t\t\t\t0x00000001, 0x00000006, 0x0000003F, 0x0000007A, 0xFFFFFF80, 0x00000041\n\t\t}", code);
	}

	@Test
	public void testArrayCodeToString02()
	{
		String code = this.generator.arrayToCodeString(new int[] { 0x7A, 0x41, 0x02, 0x04, 0x06 }, "char");

		assertNotNull("The code should not be null.", code);
		assertTrue("The code should have length.", code.length() > 0);
		assertEquals("The code is not correct.",
					 "new char[] {\n\t\t\t\t0x0000007A, 0x00000041, 0x00000002, 0x00000004, 0x00000006\n\t\t}", code);
	}

	@Test
	public void testArrayCodeToString03()
	{
		String code = this.generator.arrayToCodeString(new int[] {
				0x7A, 0x41, 0x02, 0x04, 0x06, 0x01, 0x06, 0x3F, 0x7A, 0x15, 0x41, 0x41, 0x15, 0x7A, 0x06, 0x3F,
				0x04, 0x41, 0x7A, 0x06
		}, "short");

		assertNotNull("The code should not be null.", code);
		assertTrue("The code should have length.", code.length() > 0);
		assertEquals("The code is not correct.",
					 "new short[] {\n\t\t\t\t" +
					 	 "0x0000007A, 0x00000041, 0x00000002, 0x00000004, 0x00000006, 0x00000001, 0x00000006, " +
						 "0x0000003F, \n\t\t\t\t0x0000007A, 0x00000015, 0x00000041, 0x00000041, 0x00000015, " +
						 "0x0000007A, 0x00000006, 0x0000003F, \n\t\t\t\t0x00000004, 0x00000041, 0x0000007A, " +
						 "0x00000006\n\t\t}",
					 code);
	}

	@SuppressWarnings("unchecked")
	private JavaFileManager compileClass(String fqcn, String code)
	{
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		JavaFileManager fileManager =
				new ClassFileManager<StandardJavaFileManager>(compiler.getStandardFileManager(null, null, null));

		List<JavaFileObject> javaFiles = new ArrayList<JavaFileObject>();
		javaFiles.add(new CharSequenceJavaFileObject(fqcn, code));

		ByteArrayOutputStream compilerOutput = new ByteArrayOutputStream();

		JavaCompiler.CompilationTask task = compiler.getTask(
				new OutputStreamWriter(compilerOutput), fileManager, null, null, null, javaFiles
		);

		if(!task.call())
			fail("Compiling of generated class did not succeed. Java code:\n" + code + "\nCompiler output:\n" + compilerOutput.toString());

		return fileManager;
	}

	@Test
	public void testGenerateJavaCode01()
			throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException,
				   InvocationTargetException
	{
		String code = this.generator.generateJavaCode(
				null,
				"TestGenerateJavaCode01",
				null,
				null,
				"public final String testMethod01(String concatenate)",
				"concatenate + \" some other cool text.\""
		);

		assertNotNull("The code should not be null.", code);
		assertTrue("The code should have length.", code.length() > 0);
		assertEquals("The code is not correct.",
					 "public final class TestGenerateJavaCode01\n" +
					 "{\n" +
					 "\tpublic final String testMethod01(String concatenate)\n" +
					 "\t{\n" +
					 "\t\treturn concatenate + \" some other cool text.\";\n" +
					 "\t}\n" +
					 "}\n",
					 code);

		JavaFileManager compiled = this.compileClass("TestGenerateJavaCode01", code);

		Class<?> classObject = compiled.getClassLoader(null).loadClass("TestGenerateJavaCode01");
		System.out.println("Successfully compiled " + classObject.toString() + ".");

		Object instance = classObject.newInstance();
		Method method = classObject.getMethod("testMethod01", String.class);

		String value = (String)method.invoke(instance, "Test text and");
		assertEquals("The returned value is not correct (1).", "Test text and some other cool text.", value);

		value = (String)method.invoke(instance, "More text plus");
		assertEquals("The returned value is not correct (2).", "More text plus some other cool text.", value);
	}

	@Test
	public void testGenerateJavaCode02()
			throws ClassNotFoundException, IllegalAccessException, InstantiationException, InterruptedException
	{
		String code = this.generator.generateJavaCode(
				"com.nicholaswilliams.java.mock",
				"TestGenerateJavaCode02",
				"TestRSAKeyPairGenerator.TestDynamicCompileInterface",
				new String[] { "net.nicholaswilliams.java.licensing.encryption.TestRSAKeyPairGenerator" },
				"public long getSystemTimeInSeconds()",
				"System.currentTimeMillis() / 1000L"
		);

		assertNotNull("The code should not be null.", code);
		assertTrue("The code should have length.", code.length() > 0);
		assertEquals("The code is not correct.",
					 "package com.nicholaswilliams.java.mock;\n" +
					 "\n" +
					 "import net.nicholaswilliams.java.licensing.encryption.TestRSAKeyPairGenerator;\n" +
					 "\n" +
					 "public final class TestGenerateJavaCode02 implements TestRSAKeyPairGenerator.TestDynamicCompileInterface\n" +
					 "{\n" +
					 "\t@Override\n" +
					 "\tpublic long getSystemTimeInSeconds()\n" +
					 "\t{\n" +
					 "\t\treturn System.currentTimeMillis() / 1000L;\n" +
					 "\t}\n" +
					 "}\n",
					 code);

		JavaFileManager compiled = this.compileClass("com.nicholaswilliams.java.mock.TestGenerateJavaCode02", code);

		Class<?> classObject = compiled.getClassLoader(null).loadClass("com.nicholaswilliams.java.mock.TestGenerateJavaCode02");
		System.out.println("Successfully compiled " + classObject.toString() + ".");

		TestDynamicCompileInterface instance = (TestDynamicCompileInterface)classObject.newInstance();

		long seconds = System.currentTimeMillis() / 1000L;
		assertTrue("The return value is not correct (1).", instance.getSystemTimeInSeconds() >= seconds);
		Thread.sleep(1000);
		assertTrue("The return value is not correct (2).", instance.getSystemTimeInSeconds() >= (seconds + 1));
	}

	public interface TestDynamicCompileInterface
	{
		public long getSystemTimeInSeconds();
	}

	@Test
	public void testSaveToProviders01() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		String pn = "com.nicholaswilliams.mock";

		RSAKeyPairGeneratorInterface.GeneratedClassDescriptor privateKPDescriptor =
				new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
		privateKPDescriptor.setPackageName(pn);
		privateKPDescriptor.setClassName("TestPrivateKeyProvider01");

		RSAKeyPairGeneratorInterface.GeneratedClassDescriptor publicKPDescriptor =
				new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
		publicKPDescriptor.setPackageName(pn);
		publicKPDescriptor.setClassName("TestPublicKeyProvider01");

		RSAKeyPairGeneratorInterface.GeneratedClassDescriptor passwordPDescriptor =
				new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
		passwordPDescriptor.setPackageName(pn);
		passwordPDescriptor.setClassName("TestKeyPasswordProvider01");

		KeyPair keyPair = this.generator.generateKeyPair();

		this.generator.saveKeyPairToProviders(
				keyPair, privateKPDescriptor, publicKPDescriptor, passwordPDescriptor, "testPassword01".toCharArray()
		);

		///////////////////////
		assertNotNull("The private key code should not be null.", privateKPDescriptor.getJavaFileContents());
		assertTrue("The private key code should have length.", privateKPDescriptor.getJavaFileContents().length() > 0);

		Class<?> privateKPClass = this.compileClass(
				pn + ".TestPrivateKeyProvider01", privateKPDescriptor.getJavaFileContents()
		).getClassLoader(null).loadClass(pn + ".TestPrivateKeyProvider01");

		PrivateKeyDataProvider privateKeyDataProvider = (PrivateKeyDataProvider)privateKPClass.newInstance();
		assertNotNull("The private key data provider should not be null.", privateKeyDataProvider);

		byte[] privateKeyData = privateKeyDataProvider.getEncryptedPrivateKeyData();
		assertNotNull("The private key data should not be null.", privateKeyData);
		assertTrue("The private key data should have length.", privateKeyData.length > 0);

		///////////////////////
		assertNotNull("The public key code should not be null.", publicKPDescriptor.getJavaFileContents());
		assertTrue("The public key code should have length.", publicKPDescriptor.getJavaFileContents().length() > 0);

		Class<?> publicKPClass = this.compileClass(
				pn + ".TestPublicKeyProvider01", publicKPDescriptor.getJavaFileContents()
		).getClassLoader(null).loadClass(pn + ".TestPublicKeyProvider01");

		PublicKeyDataProvider publicKeyDataProvider = (PublicKeyDataProvider)publicKPClass.newInstance();
		assertNotNull("The public key data provider should not be null.", publicKeyDataProvider);

		byte[] publicKeyData = publicKeyDataProvider.getEncryptedPublicKeyData();
		assertNotNull("The public key data should not be null.", publicKeyData);
		assertTrue("The public key data should have length.", publicKeyData.length > 0);

		///////////////////////
		assertNotNull("The key password code should not be null.", passwordPDescriptor.getJavaFileContents());
		assertTrue("The key password code should have length.", passwordPDescriptor.getJavaFileContents().length() > 0);

		Class<?> passwordPClass = this.compileClass(
				pn + ".TestKeyPasswordProvider01", passwordPDescriptor.getJavaFileContents()
		).getClassLoader(null).loadClass(pn + ".TestKeyPasswordProvider01");

		KeyPasswordProvider keyPasswordProvider = (KeyPasswordProvider)passwordPClass.newInstance();
		assertNotNull("The key password provider should not be null.", keyPasswordProvider);

		char[] password = keyPasswordProvider.getKeyPassword();
		assertNotNull("The key password should not be null.", password);
		assertTrue("The key password should have length.", password.length > 0);

		///////////////////////
		assertEquals("The password is not correct.", "testPassword01", new String(password));

		PrivateKey privateKey = KeyFileUtilities.readEncryptedPrivateKey(privateKeyData, password);
		assertNotNull("The private key should not be null.", privateKey);
		assertEquals("The private keys do not match.", keyPair.getPrivate(), privateKey);

		PublicKey publicKey = KeyFileUtilities.readEncryptedPublicKey(publicKeyData, password);
		assertNotNull("The public key should not be null.", publicKey);
		assertEquals("The public keys do not match.", keyPair.getPublic(), publicKey);
	}

	@Test
	public void testSaveToProviders02() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		String pn = "com.nicholaswilliams.another";

		RSAKeyPairGeneratorInterface.GeneratedClassDescriptor privateKPDescriptor =
				new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
		privateKPDescriptor.setPackageName(pn);
		privateKPDescriptor.setClassName("TestPrivateKeyProvider02");

		RSAKeyPairGeneratorInterface.GeneratedClassDescriptor publicKPDescriptor =
				new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
		publicKPDescriptor.setPackageName(pn);
		publicKPDescriptor.setClassName("TestPublicKeyProvider02");

		RSAKeyPairGeneratorInterface.GeneratedClassDescriptor passwordPDescriptor =
				new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
		passwordPDescriptor.setPackageName(pn);
		passwordPDescriptor.setClassName("TestKeyPasswordProvider02");

		KeyPair keyPair = this.generator.generateKeyPair();

		this.generator.saveKeyPairToProviders(
				keyPair, privateKPDescriptor, publicKPDescriptor, passwordPDescriptor, "anotherPassword02".toCharArray()
		);

		///////////////////////
		assertNotNull("The private key code should not be null.", privateKPDescriptor.getJavaFileContents());
		assertTrue("The private key code should have length.", privateKPDescriptor.getJavaFileContents().length() > 0);

		Class<?> privateKPClass = this.compileClass(
				pn + ".TestPrivateKeyProvider02", privateKPDescriptor.getJavaFileContents()
		).getClassLoader(null).loadClass(pn + ".TestPrivateKeyProvider02");

		PrivateKeyDataProvider privateKeyDataProvider = (PrivateKeyDataProvider)privateKPClass.newInstance();
		assertNotNull("The private key data provider should not be null.", privateKeyDataProvider);

		byte[] privateKeyData = privateKeyDataProvider.getEncryptedPrivateKeyData();
		assertNotNull("The private key data should not be null.", privateKeyData);
		assertTrue("The private key data should have length.", privateKeyData.length > 0);

		///////////////////////
		assertNotNull("The public key code should not be null.", publicKPDescriptor.getJavaFileContents());
		assertTrue("The public key code should have length.", publicKPDescriptor.getJavaFileContents().length() > 0);

		Class<?> publicKPClass = this.compileClass(
				pn + ".TestPublicKeyProvider02", publicKPDescriptor.getJavaFileContents()
		).getClassLoader(null).loadClass(pn + ".TestPublicKeyProvider02");

		PublicKeyDataProvider publicKeyDataProvider = (PublicKeyDataProvider)publicKPClass.newInstance();
		assertNotNull("The public key data provider should not be null.", publicKeyDataProvider);

		byte[] publicKeyData = publicKeyDataProvider.getEncryptedPublicKeyData();
		assertNotNull("The public key data should not be null.", publicKeyData);
		assertTrue("The public key data should have length.", publicKeyData.length > 0);

		///////////////////////
		assertNotNull("The key password code should not be null.", passwordPDescriptor.getJavaFileContents());
		assertTrue("The key password code should have length.", passwordPDescriptor.getJavaFileContents().length() > 0);

		Class<?> passwordPClass = this.compileClass(
				pn + ".TestKeyPasswordProvider02", passwordPDescriptor.getJavaFileContents()
		).getClassLoader(null).loadClass(pn + ".TestKeyPasswordProvider02");

		KeyPasswordProvider keyPasswordProvider = (KeyPasswordProvider)passwordPClass.newInstance();
		assertNotNull("The key password provider should not be null.", keyPasswordProvider);

		char[] password = keyPasswordProvider.getKeyPassword();
		assertNotNull("The key password should not be null.", password);
		assertTrue("The key password should have length.", password.length > 0);

		///////////////////////
		assertEquals("The password is not correct.", "anotherPassword02", new String(password));

		PrivateKey privateKey = KeyFileUtilities.readEncryptedPrivateKey(privateKeyData, password);
		assertNotNull("The private key should not be null.", privateKey);
		assertEquals("The private keys do not match.", keyPair.getPrivate(), privateKey);

		PublicKey publicKey = KeyFileUtilities.readEncryptedPublicKey(publicKeyData, password);
		assertNotNull("The public key should not be null.", publicKey);
		assertEquals("The public keys do not match.", keyPair.getPublic(), publicKey);
	}
}

class ClassFileManager<M extends JavaFileManager> extends ForwardingJavaFileManager<M>
{
	private JavaClassObject javaClassObject;

	public ClassFileManager(M standardManager)
	{
		super(standardManager);
	}

	@Override
	public ClassLoader getClassLoader(Location location)
	{
		return new SecureClassLoader()
		{
			@Override
			protected Class<?> findClass(String name) throws ClassNotFoundException
			{
				byte[] b = ClassFileManager.this.javaClassObject.getBytes();
				return super.defineClass(name, ClassFileManager.this.javaClassObject.getBytes(), 0, b.length);
			}
		};
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind,
											   FileObject sibling)
			throws IOException
	{
		this.javaClassObject = new JavaClassObject(className, kind);
		return this.javaClassObject;
	}
}

class JavaClassObject extends SimpleJavaFileObject
{
	protected final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	public JavaClassObject(String name, Kind kind)
	{
		super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
	}

	public byte[] getBytes()
	{
		return outputStream.toByteArray();
	}

	@Override
	public OutputStream openOutputStream() throws IOException
	{
		return outputStream;
	}
}

class CharSequenceJavaFileObject extends SimpleJavaFileObject
{
	private CharSequence content;

	public CharSequenceJavaFileObject(String className, CharSequence content)
	{
		super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
		this.content = content;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors)
	{
		return this.content;
	}
}
