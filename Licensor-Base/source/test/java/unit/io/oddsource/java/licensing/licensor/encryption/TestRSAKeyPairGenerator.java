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
package io.oddsource.java.licensing.licensor.encryption;

import static org.junit.Assert.*;

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
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.oddsource.java.licensing.encryption.KeyFileUtilities;
import io.oddsource.java.licensing.encryption.PasswordProvider;
import io.oddsource.java.licensing.encryption.PublicKeyDataProvider;

/**
 * Test class for RSAKeyPairGenerator.
 */
public class TestRSAKeyPairGenerator
{
    private static KeyPair reusableKeyPair;

    private RSAKeyPairGenerator generator;

    public TestRSAKeyPairGenerator()
    {
        this.generator = new RSAKeyPairGenerator();
    }

    @BeforeClass
    public static void setUpClass()
    {
        TestRSAKeyPairGenerator.reusableKeyPair = new RSAKeyPairGenerator().generateKeyPair();
    }

    @AfterClass
    public static void tearDownClass()
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
    public void testGetKeyPair01()
    {
        KeyPair keyPair = this.generator.generateKeyPair();

        assertNotNull("The key pair should not be null.", keyPair);

        Assert.assertEquals("The algorithm is not correct.", KeyFileUtilities.keyAlgorithm,
                            keyPair.getPrivate().getAlgorithm()
        );
        assertEquals("The algorithm is not correct.", KeyFileUtilities.keyAlgorithm,
                     keyPair.getPublic().getAlgorithm()
        );
    }

    @Test
    public void testSaveKeyPairToFiles01() throws IOException
    {
        KeyPair keyPair = this.generator.generateKeyPair();

        File file1 = new File("testSaveKeyPairToFiles01.private");
        File file2 = new File("testSaveKeyPairToFiles01.public");

        if(file1.exists())
        {
            FileUtils.forceDelete(file1);
        }

        if(file2.exists())
        {
            FileUtils.forceDelete(file2);
        }

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

        PrivateKey privateKey = KeyFileUtilities.readEncryptedPrivateKey(file1, "testMyPassword01".toCharArray());
        PublicKey publicKey = KeyFileUtilities.readEncryptedPublicKey(file2, "testMyPassword01".toCharArray());

        assertNotNull("The private key should not be null.", privateKey);
        assertEquals("The private key is not correct.", keyPair.getPrivate(), privateKey);

        assertNotNull("The public key should not be null.", publicKey);
        assertEquals("The public key is not correct.", keyPair.getPublic(), publicKey);

        FileUtils.forceDelete(file1);
        FileUtils.forceDelete(file2);
    }

    @Test
    public void testSaveKeyPairToFiles02() throws IOException
    {
        File file1 = new File("testSaveKeyPairToFiles02.private");
        File file2 = new File("testSaveKeyPairToFiles02.public");

        if(file1.exists())
        {
            FileUtils.forceDelete(file1);
        }

        if(file2.exists())
        {
            FileUtils.forceDelete(file2);
        }

        assertFalse("File 1 should not exist.", file1.exists());
        assertFalse("File 2 should not exist.", file2.exists());

        this.generator.saveKeyPairToFiles(
            TestRSAKeyPairGenerator.reusableKeyPair,
            "testSaveKeyPairToFiles02.private",
            "testSaveKeyPairToFiles02.public",
            "testMyPassword02".toCharArray(),
            "testYourPassword02".toCharArray()
        );

        assertTrue("File 1 should exist.", file1.exists());
        assertTrue("File 2 should exist.", file2.exists());

        PrivateKey privateKey = KeyFileUtilities.readEncryptedPrivateKey(file1, "testMyPassword02".toCharArray());
        PublicKey publicKey = KeyFileUtilities.readEncryptedPublicKey(file2, "testYourPassword02".toCharArray());

        assertNotNull("The private key should not be null.", privateKey);
        assertEquals(
            "The private key is not correct.",
            TestRSAKeyPairGenerator.reusableKeyPair.getPrivate(),
            privateKey
        );

        assertNotNull("The public key should not be null.", publicKey);
        assertEquals("The public key is not correct.", TestRSAKeyPairGenerator.reusableKeyPair.getPublic(), publicKey);

        FileUtils.forceDelete(file1);
        FileUtils.forceDelete(file2);
    }

    @Test
    public void testByteArrayToIntArray01()
    {
        int[] array = this.generator.byteArrayToIntArray(new byte[] {0x01, 0x06, 0x3F, 0x7A, 0x15, 0x41});

        assertNotNull("The int array should not be null.", array);
        assertTrue("The int array should have length.", array.length > 0);
        assertArrayEquals("The array is not correct.", array, new int[] {0x01, 0x06, 0x3F, 0x7A, 0x15, 0x41});
    }

    @Test
    public void testByteArrayToIntArray02()
    {
        int[] array = this.generator.byteArrayToIntArray(new byte[] {0x7A, 0x41, 0x02, 0x04, 0x06});

        assertNotNull("The int array should not be null.", array);
        assertTrue("The int array should have length.", array.length > 0);
        assertArrayEquals("The array is not correct.", array, new int[] {0x7A, 0x41, 0x02, 0x04, 0x06});
    }

    @Test
    public void testCharArrayToIntArray01()
    {
        int[] array = this.generator.charArrayToIntArray(new char[] {0x01, 0x06, 0x3F, 0x7A, 0x15, 0x41});

        assertNotNull("The int array should not be null.", array);
        assertTrue("The int array should have length.", array.length > 0);
        assertArrayEquals("The array is not correct.", array, new int[] {0x01, 0x06, 0x3F, 0x7A, 0x15, 0x41});
    }

    @Test
    public void testCharArrayToIntArray02()
    {
        int[] array = this.generator.charArrayToIntArray(new char[] {0x7A, 0x41, 0x02, 0x04, 0x06});

        assertNotNull("The int array should not be null.", array);
        assertTrue("The int array should have length.", array.length > 0);
        assertArrayEquals("The array is not correct.", array, new int[] {0x7A, 0x41, 0x02, 0x04, 0x06});
    }

    @Test
    public void testArrayCodeToString01()
    {
        String code = this.generator.arrayToCodeString(new int[] {0x01, 0x06, 0x3F, 0x7A, -128, 0x41}, "byte");

        assertNotNull("The code should not be null.", code);
        assertTrue("The code should have length.", code.length() > 0);
        assertEquals("The code is not correct.",
                     "new byte[] {\n                0x00000001, 0x00000006, 0x0000003F, 0x0000007A, 0xFFFFFF80, " +
                     "0x00000041\n        }",
                     code
        );
    }

    @Test
    public void testArrayCodeToString02()
    {
        String code = this.generator.arrayToCodeString(new int[] {0x7A, 0x41, 0x02, 0x04, 0x06}, "char");

        assertNotNull("The code should not be null.", code);
        assertTrue("The code should have length.", code.length() > 0);
        assertEquals("The code is not correct.",
                     "new char[] {\n                0x0000007A, 0x00000041, 0x00000002, 0x00000004, 0x00000006\n     " +
                     "   }",
                     code
        );
    }

    @Test
    public void testArrayCodeToString03()
    {
        String code = this.generator.arrayToCodeString(new int[] {
            0x7A, 0x41, 0x02, 0x04, 0x06, 0x01, 0x06, 0x3F, 0x7A, 0x15, 0x41, 0x41, 0x15, 0x7A, 0x06, 0x3F,
            0x04, 0x41, 0x7A, 0x06,
        }, "short");

        assertNotNull("The code should not be null.", code);
        assertTrue("The code should have length.", code.length() > 0);
        assertEquals(
            "The code is not correct.",
            "new short[] {\n                " +
            "0x0000007A, 0x00000041, 0x00000002, 0x00000004, 0x00000006, 0x00000001, 0x00000006, " +
            "0x0000003F, \n                0x0000007A, 0x00000015, 0x00000041, 0x00000041, 0x00000015, " +
            "0x0000007A, 0x00000006, 0x0000003F, \n                0x00000004, 0x00000041, 0x0000007A, " +
            "0x00000006\n        }",
            code
        );
    }

    private JavaFileManager compileClass(String fqcn, String code)
    {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileManager fileManager = new MockClassFileManager<>(compiler.getStandardFileManager(null, null, null));

        List<JavaFileObject> javaFiles = new ArrayList<>();
        javaFiles.add(new MockCharSequenceJavaFileObject(fqcn, code));

        ByteArrayOutputStream compilerOutput = new ByteArrayOutputStream();

        JavaCompiler.CompilationTask task = compiler.getTask(
            new OutputStreamWriter(compilerOutput), fileManager, null, null, null, javaFiles
        );

        if(!task.call())
        {
            fail("Compiling of generated class did not succeed. Java code:\n" + code + "\nCompiler output:\n" +
                 compilerOutput.toString());
        }

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
        assertEquals(
            "The code is not correct.",
            "public final class TestGenerateJavaCode01\n" +
            "{\n" +
            "    public final String testMethod01(String concatenate)\n" +
            "    {\n" +
            "        return concatenate + \" some other cool text.\";\n" +
            "    }\n" +
            "}\n",
            code
        );

        JavaFileManager compiled = this.compileClass("TestGenerateJavaCode01", code);

        Class<?> classObject = compiled.getClassLoader(null).loadClass("TestGenerateJavaCode01");
        System.out.println("Successfully compiled " + classObject.toString() + ".");

        Object instance = classObject.newInstance();
        Method method = classObject.getMethod("testMethod01", String.class);

        String value = (String) method.invoke(instance, "Test text and");
        assertEquals("The returned value is not correct (1).", "Test text and some other cool text.", value);

        value = (String) method.invoke(instance, "More text plus");
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
            new String[] {"io.oddsource.java.licensing.licensor.encryption.TestRSAKeyPairGenerator"},
            "public long getSystemTimeInSeconds()",
            "System.currentTimeMillis() / 1000L"
        );

        assertNotNull("The code should not be null.", code);
        assertTrue("The code should have length.", code.length() > 0);
        assertEquals(
            "The code is not correct.",
            "package com.nicholaswilliams.java.mock;\n" +
            "\n" +
            "import io.oddsource.java.licensing.licensor.encryption.TestRSAKeyPairGenerator;\n" +
            "\n" +
            "public final class TestGenerateJavaCode02 implements TestRSAKeyPairGenerator" +
            ".TestDynamicCompileInterface\n" +
            "{\n" +
            "    @Override\n" +
            "    public long getSystemTimeInSeconds()\n" +
            "    {\n" +
            "        return System.currentTimeMillis() / 1000L;\n" +
            "    }\n" +
            "}\n",
            code
        );

        JavaFileManager compiled = this.compileClass("com.nicholaswilliams.java.mock.TestGenerateJavaCode02", code);

        Class<?> classObject = compiled.getClassLoader(null).loadClass(
            "com.nicholaswilliams.java.mock.TestGenerateJavaCode02");
        System.out.println("Successfully compiled " + classObject.toString() + ".");

        TestDynamicCompileInterface instance = (TestDynamicCompileInterface) classObject.newInstance();

        long seconds = System.currentTimeMillis() / 1000L;
        assertTrue("The return value is not correct (1).", instance.getSystemTimeInSeconds() >= seconds);
        Thread.sleep(1000);
        assertTrue("The return value is not correct (2).", instance.getSystemTimeInSeconds() >= (seconds + 1));
    }

    public static interface TestDynamicCompileInterface
    {
        public abstract long getSystemTimeInSeconds();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveKeysToProvidersFailed01()
    {
        String pn = "com.nicholaswilliams.mock";

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor privateKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        privateKeyProviderDescriptor.setPackageName(pn);
        privateKeyProviderDescriptor.setClassName("TestPrivateKeyProvider01");

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor publicKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        publicKeyProviderDescriptor.setPackageName(pn);
        publicKeyProviderDescriptor.setClassName("TestPublicKeyProvider01");

        this.generator.saveKeyPairToProviders(
            null,
            privateKeyProviderDescriptor,
            publicKeyProviderDescriptor,
            "test1".toCharArray(),
            "test2".toCharArray()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveKeysToProvidersFailed02()
    {
        String pn = "com.nicholaswilliams.mock";

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor publicKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        publicKeyProviderDescriptor.setPackageName(pn);
        publicKeyProviderDescriptor.setClassName("TestPublicKeyProvider01");

        this.generator.saveKeyPairToProviders(
            TestRSAKeyPairGenerator.reusableKeyPair,
            null,
            publicKeyProviderDescriptor,
            "test1".toCharArray(),
            "test2".toCharArray()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveKeysToProvidersFailed03()
    {
        String pn = "com.nicholaswilliams.mock";

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor privateKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        privateKeyProviderDescriptor.setPackageName(pn);
        privateKeyProviderDescriptor.setClassName("TestPrivateKeyProvider01");

        this.generator.saveKeyPairToProviders(
            TestRSAKeyPairGenerator.reusableKeyPair,
            privateKeyProviderDescriptor,
            null,
            "test1".toCharArray(),
            "test2".toCharArray()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveKeysToProvidersFailed04()
    {
        String pn = "com.nicholaswilliams.mock";

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor privateKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        privateKeyProviderDescriptor.setPackageName(pn);
        privateKeyProviderDescriptor.setClassName("TestPrivateKeyProvider01");

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor publicKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        publicKeyProviderDescriptor.setPackageName(pn);
        publicKeyProviderDescriptor.setClassName("TestPublicKeyProvider01");

        this.generator.saveKeyPairToProviders(
            TestRSAKeyPairGenerator.reusableKeyPair,
            privateKeyProviderDescriptor,
            publicKeyProviderDescriptor,
            null,
            "test2".toCharArray()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveKeysToProvidersFailed05()
    {
        String pn = "com.nicholaswilliams.mock";

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor privateKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        privateKeyProviderDescriptor.setPackageName(pn);
        privateKeyProviderDescriptor.setClassName("TestPrivateKeyProvider01");

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor publicKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        publicKeyProviderDescriptor.setPackageName(pn);
        publicKeyProviderDescriptor.setClassName("TestPublicKeyProvider01");

        this.generator.saveKeyPairToProviders(
            TestRSAKeyPairGenerator.reusableKeyPair,
            privateKeyProviderDescriptor,
            publicKeyProviderDescriptor,
            "".toCharArray(),
            "test2".toCharArray()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveKeysToProvidersFailed06()
    {
        String pn = "com.nicholaswilliams.mock";

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor privateKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        privateKeyProviderDescriptor.setPackageName(pn);
        privateKeyProviderDescriptor.setClassName("TestPrivateKeyProvider01");

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor publicKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        publicKeyProviderDescriptor.setPackageName(pn);
        publicKeyProviderDescriptor.setClassName("TestPublicKeyProvider01");

        this.generator.saveKeyPairToProviders(
            TestRSAKeyPairGenerator.reusableKeyPair,
            privateKeyProviderDescriptor,
            publicKeyProviderDescriptor,
            "test1".toCharArray(),
            null
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveKeysToProvidersFailed07()
    {
        String pn = "com.nicholaswilliams.mock";

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor privateKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        privateKeyProviderDescriptor.setPackageName(pn);
        privateKeyProviderDescriptor.setClassName("TestPrivateKeyProvider01");

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor publicKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        publicKeyProviderDescriptor.setPackageName(pn);
        publicKeyProviderDescriptor.setClassName("TestPublicKeyProvider01");

        this.generator.saveKeyPairToProviders(
            TestRSAKeyPairGenerator.reusableKeyPair,
            privateKeyProviderDescriptor,
            publicKeyProviderDescriptor,
            "test1".toCharArray(),
            "".toCharArray()
        );
    }

    @Test
    public void testSaveKeysToProviders01()
        throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        String pn = "com.nicholaswilliams.mock";

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor privateKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        privateKeyProviderDescriptor.setPackageName(pn);
        privateKeyProviderDescriptor.setClassName("TestPrivateKeyProvider01");

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor publicKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        publicKeyProviderDescriptor.setPackageName(pn);
        publicKeyProviderDescriptor.setClassName("TestPublicKeyProvider01");

        KeyPair keyPair = this.generator.generateKeyPair();

        this.generator.saveKeyPairToProviders(
            keyPair, privateKeyProviderDescriptor, publicKeyProviderDescriptor, "testPassword01".toCharArray()
        );

        ///////////////////////
        assertNotNull("The private key code should not be null.", privateKeyProviderDescriptor.getJavaFileContents());
        assertTrue(
            "The private key code should have length.",
            privateKeyProviderDescriptor.getJavaFileContents().length() > 0
        );

        Class<?> privateKeyProviderClass = this.compileClass(
            pn + ".TestPrivateKeyProvider01", privateKeyProviderDescriptor.getJavaFileContents()
        ).getClassLoader(null).loadClass(pn + ".TestPrivateKeyProvider01");

        PrivateKeyDataProvider privateKeyDataProvider = (PrivateKeyDataProvider) privateKeyProviderClass.newInstance();
        assertNotNull("The private key data provider should not be null.", privateKeyDataProvider);

        byte[] privateKeyData = privateKeyDataProvider.getEncryptedPrivateKeyData();
        assertNotNull("The private key data should not be null.", privateKeyData);
        assertTrue("The private key data should have length.", privateKeyData.length > 0);

        PrivateKey privateKey = KeyFileUtilities.readEncryptedPrivateKey(
            privateKeyData,
            "testPassword01".toCharArray()
        );
        assertNotNull("The private key should not be null.", privateKey);
        assertEquals("The private key is not correct.", keyPair.getPrivate(), privateKey);

        ///////////////////////
        assertNotNull("The public key code should not be null.", publicKeyProviderDescriptor.getJavaFileContents());
        assertTrue(
            "The public key code should have length.",
            publicKeyProviderDescriptor.getJavaFileContents().length() > 0
        );

        Class<?> publicKeyProviderClass = this.compileClass(
            pn + ".TestPublicKeyProvider01", publicKeyProviderDescriptor.getJavaFileContents()
        ).getClassLoader(null).loadClass(pn + ".TestPublicKeyProvider01");

        PublicKeyDataProvider publicKeyDataProvider = (PublicKeyDataProvider) publicKeyProviderClass.newInstance();
        assertNotNull("The public key data provider should not be null.", publicKeyDataProvider);

        byte[] publicKeyData = publicKeyDataProvider.getEncryptedPublicKeyData();
        assertNotNull("The public key data should not be null.", publicKeyData);
        assertTrue("The public key data should have length.", publicKeyData.length > 0);

        PublicKey publicKey = KeyFileUtilities.readEncryptedPublicKey(publicKeyData, "testPassword01".toCharArray());
        assertNotNull("The public key should not be null.", publicKey);
        assertEquals("The public key is not correct.", keyPair.getPublic(), publicKey);
    }

    @Test
    public void testSaveKeysToProviders02()
        throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        String pn = "com.nicholaswilliams.another";

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor privateKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        privateKeyProviderDescriptor.setPackageName(pn);
        privateKeyProviderDescriptor.setClassName("TestPrivateKeyProvider02");

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor publicKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        publicKeyProviderDescriptor.setPackageName(pn);
        publicKeyProviderDescriptor.setClassName("TestPublicKeyProvider02");

        this.generator.saveKeyPairToProviders(
            TestRSAKeyPairGenerator.reusableKeyPair,
            privateKeyProviderDescriptor,
            publicKeyProviderDescriptor,
            "anotherPassword02".toCharArray()
        );

        ///////////////////////
        assertNotNull("The private key code should not be null.", privateKeyProviderDescriptor.getJavaFileContents());
        assertTrue(
            "The private key code should have length.",
            privateKeyProviderDescriptor.getJavaFileContents().length() > 0
        );

        Class<?> privateKeyProviderClass = this.compileClass(
            pn + ".TestPrivateKeyProvider02", privateKeyProviderDescriptor.getJavaFileContents()
        ).getClassLoader(null).loadClass(pn + ".TestPrivateKeyProvider02");

        PrivateKeyDataProvider privateKeyDataProvider = (PrivateKeyDataProvider) privateKeyProviderClass.newInstance();
        assertNotNull("The private key data provider should not be null.", privateKeyDataProvider);

        byte[] privateKeyData = privateKeyDataProvider.getEncryptedPrivateKeyData();
        assertNotNull("The private key data should not be null.", privateKeyData);
        assertTrue("The private key data should have length.", privateKeyData.length > 0);

        PrivateKey privateKey = KeyFileUtilities.readEncryptedPrivateKey(
            privateKeyData,
            "anotherPassword02".toCharArray()
        );
        assertNotNull("The private key should not be null.", privateKey);
        assertEquals(
            "The private key is not correct.",
            TestRSAKeyPairGenerator.reusableKeyPair.getPrivate(),
            privateKey
        );

        ///////////////////////
        assertNotNull("The public key code should not be null.", publicKeyProviderDescriptor.getJavaFileContents());
        assertTrue(
            "The public key code should have length.",
            publicKeyProviderDescriptor.getJavaFileContents().length() > 0
        );

        Class<?> publicKeyProviderClass = this.compileClass(
            pn + ".TestPublicKeyProvider02", publicKeyProviderDescriptor.getJavaFileContents()
        ).getClassLoader(null).loadClass(pn + ".TestPublicKeyProvider02");

        PublicKeyDataProvider publicKeyDataProvider = (PublicKeyDataProvider) publicKeyProviderClass.newInstance();
        assertNotNull("The public key data provider should not be null.", publicKeyDataProvider);

        byte[] publicKeyData = publicKeyDataProvider.getEncryptedPublicKeyData();
        assertNotNull("The public key data should not be null.", publicKeyData);
        assertTrue("The public key data should have length.", publicKeyData.length > 0);

        PublicKey publicKey = KeyFileUtilities.readEncryptedPublicKey(publicKeyData, "anotherPassword02".toCharArray());
        assertNotNull("The public key should not be null.", publicKey);
        assertEquals("The public key is not correct.", TestRSAKeyPairGenerator.reusableKeyPair.getPublic(), publicKey);
    }

    @Test
    public void testSaveKeysToProviders03()
        throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        String pn = "com.nicholaswilliams.last";

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor privateKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        privateKeyProviderDescriptor.setPackageName(pn);
        privateKeyProviderDescriptor.setClassName("TestPrivateKeyProvider03");

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor publicKeyProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        publicKeyProviderDescriptor.setPackageName(pn);
        publicKeyProviderDescriptor.setClassName("TestPublicKeyProvider03");

        KeyPair keyPair = this.generator.generateKeyPair();

        this.generator.saveKeyPairToProviders(
            keyPair, privateKeyProviderDescriptor, publicKeyProviderDescriptor, "finalPasswordOne03".toCharArray(),
            "finalPasswordTwo03".toCharArray()
        );

        ///////////////////////
        assertNotNull("The private key code should not be null.", privateKeyProviderDescriptor.getJavaFileContents());
        assertTrue(
            "The private key code should have length.",
            privateKeyProviderDescriptor.getJavaFileContents().length() > 0
        );

        Class<?> privateKeyProviderClass = this.compileClass(
            pn + ".TestPrivateKeyProvider03", privateKeyProviderDescriptor.getJavaFileContents()
        ).getClassLoader(null).loadClass(pn + ".TestPrivateKeyProvider03");

        PrivateKeyDataProvider privateKeyDataProvider = (PrivateKeyDataProvider) privateKeyProviderClass.newInstance();
        assertNotNull("The private key data provider should not be null.", privateKeyDataProvider);

        byte[] privateKeyData = privateKeyDataProvider.getEncryptedPrivateKeyData();
        assertNotNull("The private key data should not be null.", privateKeyData);
        assertTrue("The private key data should have length.", privateKeyData.length > 0);

        PrivateKey privateKey = KeyFileUtilities.readEncryptedPrivateKey(
            privateKeyData,
            "finalPasswordOne03".toCharArray()
        );
        assertNotNull("The private key should not be null.", privateKey);
        assertEquals("The private key is not correct.", keyPair.getPrivate(), privateKey);

        ///////////////////////
        assertNotNull("The public key code should not be null.", publicKeyProviderDescriptor.getJavaFileContents());
        assertTrue(
            "The public key code should have length.",
            publicKeyProviderDescriptor.getJavaFileContents().length() > 0
        );

        Class<?> publicKeyProviderClass = this.compileClass(
            pn + ".TestPublicKeyProvider03", publicKeyProviderDescriptor.getJavaFileContents()
        ).getClassLoader(null).loadClass(pn + ".TestPublicKeyProvider03");

        PublicKeyDataProvider publicKeyDataProvider = (PublicKeyDataProvider) publicKeyProviderClass.newInstance();
        assertNotNull("The public key data provider should not be null.", publicKeyDataProvider);

        byte[] publicKeyData = publicKeyDataProvider.getEncryptedPublicKeyData();
        assertNotNull("The public key data should not be null.", publicKeyData);
        assertTrue("The public key data should have length.", publicKeyData.length > 0);

        PublicKey publicKey = KeyFileUtilities.readEncryptedPublicKey(
            publicKeyData,
            "finalPasswordTwo03".toCharArray()
        );
        assertNotNull("The public key should not be null.", publicKey);
        assertEquals("The public key is not correct.", keyPair.getPublic(), publicKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSavePasswordToProviderFailed01()
    {
        String pn = "com.nicholaswilliams.mock";

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor passwordProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        passwordProviderDescriptor.setPackageName(pn);
        passwordProviderDescriptor.setClassName("TestKeyPasswordProvider01");

        this.generator.savePasswordToProvider(null, passwordProviderDescriptor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSavePasswordToProviderFailed02()
    {
        String pn = "com.nicholaswilliams.mock";

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor passwordProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        passwordProviderDescriptor.setPackageName(pn);
        passwordProviderDescriptor.setClassName("TestKeyPasswordProvider01");

        this.generator.savePasswordToProvider("".toCharArray(), passwordProviderDescriptor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSavePasswordToProviderFailed03()
    {
        this.generator.savePasswordToProvider("test1".toCharArray(), null);
    }

    @Test
    public void testSavePasswordToProvider01()
        throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        String pn = "com.nicholaswilliams.mock";

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor passwordProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        passwordProviderDescriptor.setPackageName(pn);
        passwordProviderDescriptor.setClassName("TestKeyPasswordProvider01");

        this.generator.savePasswordToProvider("testPassword01".toCharArray(), passwordProviderDescriptor);

        assertNotNull("The key password code should not be null.", passwordProviderDescriptor.getJavaFileContents());
        assertTrue(
            "The key password code should have length.",
            passwordProviderDescriptor.getJavaFileContents().length() > 0
        );

        Class<?> passwordProviderClass = this.compileClass(
            pn + ".TestKeyPasswordProvider01", passwordProviderDescriptor.getJavaFileContents()
        ).getClassLoader(null).loadClass(pn + ".TestKeyPasswordProvider01");

        PasswordProvider passwordProvider = (PasswordProvider) passwordProviderClass.newInstance();
        assertNotNull("The key password provider should not be null.", passwordProvider);

        char[] password = passwordProvider.getPassword();
        assertNotNull("The key password should not be null.", password);
        assertTrue("The key password should have length.", password.length > 0);

        assertEquals("The password is not correct.", "testPassword01", new String(password));
    }

    @Test
    public void testSavePasswordToProvider02()
        throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        String pn = "com.nicholaswilliams.another";

        RSAKeyPairGeneratorInterface.GeneratedClassDescriptor passwordProviderDescriptor =
            new RSAKeyPairGeneratorInterface.GeneratedClassDescriptor();
        passwordProviderDescriptor.setPackageName(pn);
        passwordProviderDescriptor.setClassName("TestKeyPasswordProvider02");

        this.generator.savePasswordToProvider("anotherPassword02".toCharArray(), passwordProviderDescriptor);

        assertNotNull("The key password code should not be null.", passwordProviderDescriptor.getJavaFileContents());
        assertTrue(
            "The key password code should have length.",
            passwordProviderDescriptor.getJavaFileContents().length() > 0
        );

        Class<?> passwordProviderClass = this.compileClass(
            pn + ".TestKeyPasswordProvider02", passwordProviderDescriptor.getJavaFileContents()
        ).getClassLoader(null).loadClass(pn + ".TestKeyPasswordProvider02");

        PasswordProvider passwordProvider = (PasswordProvider) passwordProviderClass.newInstance();
        assertNotNull("The key password provider should not be null.", passwordProvider);

        char[] password = passwordProvider.getPassword();
        assertNotNull("The key password should not be null.", password);
        assertTrue("The key password should have length.", password.length > 0);

        assertEquals("The password is not correct.", "anotherPassword02", new String(password));
    }
}

class MockClassFileManager<M extends JavaFileManager> extends ForwardingJavaFileManager<M>
{
    private MockJavaClassObject javaClassObject;

    public MockClassFileManager(M standardManager)
    {
        super(standardManager);
    }

    @Override
    public ClassLoader getClassLoader(Location location)
    {
        return new SecureClassLoader()
        {
            @Override
            protected Class<?> findClass(String name)
            {
                byte[] b = MockClassFileManager.this.javaClassObject.getBytes();
                return super.defineClass(name, MockClassFileManager.this.javaClassObject.getBytes(), 0, b.length);
            }
        };
    }

    @Override
    public JavaFileObject getJavaFileForOutput(
        Location location, String className, JavaFileObject.Kind kind,
        FileObject sibling
    )
    {
        this.javaClassObject = new MockJavaClassObject(className, kind);
        return this.javaClassObject;
    }
}

class MockJavaClassObject extends SimpleJavaFileObject
{
    protected final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public MockJavaClassObject(String name, Kind kind)
    {
        super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
    }

    public byte[] getBytes()
    {
        return outputStream.toByteArray();
    }

    @Override
    public OutputStream openOutputStream()
    {
        return outputStream;
    }
}

class MockCharSequenceJavaFileObject extends SimpleJavaFileObject
{
    private CharSequence content;

    public MockCharSequenceJavaFileObject(String className, CharSequence content)
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
