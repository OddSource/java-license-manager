/*
 * TestDataSignatureManager.java from LicenseManager modified Thursday, January 24, 2013 15:33:51 CST (-0600).
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

package net.nicholaswilliams.java.licensing;

import net.nicholaswilliams.java.licensing.exception.InvalidSignatureException;
import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

/**
 * Test class for DataSignatureManager.
 */
public class TestDataSignatureManager
{
    private static final String privateKeyString = "rO0ABXNyABRqYXZhLnNlY3VyaXR5LktleVJlcL35T7OImqVDAgAETAAJYWxnb3Jpd" +
                                                   "GhtdAASTGphdmEvbGFuZy9TdHJpbmc7WwAHZW5jb2RlZHQAAltCTAAGZm9ybWF0cQ" +
                                                   "B+AAFMAAR0eXBldAAbTGphdmEvc2VjdXJpdHkvS2V5UmVwJFR5cGU7eHB0AANSU0F" +
                                                   "1cgACW0Ks8xf4BghU4AIAAHhwAAAEwDCCBLwCAQAwDQYJKoZIhvcNAQEBBQAEggSm" +
                                                   "MIIEogIBAAKCAQEAkJ69eXODVaSFdxLPg5MmP6XNJ5KJSZCeCwoYS+fRupBfBzBlU" +
                                                   "hmvRUD2DX33QqqaTuW2sRm4o0ho9yu1dnlihZQCSwzERaVe023AuyXk6MLzSgBk31" +
                                                   "Sp0d7J71nnMDouvA43Y8QecgmZmVM4l70niL4L9Jfnb/cChtOYJlh5sGCfhBqJfYm" +
                                                   "V1znDTi63MNup6T2lRMDLbW12Occ6wV5JPUQtHFJCrMNg8KDXZ6EyHLl693np4h71" +
                                                   "Lywcf6WUkGe8qxwNU4dtz7lyjO9VOml0CwqLnW20h72Pl+pL1QTjoAs87KT1R2KVV" +
                                                   "cyfPlLZoGHh3UAhOzrQNu1KJuSKT4vtOQIDAQABAoIBAETGqTcb/yZ4glxZXsSk8z" +
                                                   "irogAiyRVqKC750wouKeh+hiLGGjlMK7VUqm9KK4/R0GabCiH5KHzGj9yUNc0s9vS" +
                                                   "tqdWFMcYpWgpUlKar48vqAod73nzzJ3u+ZuxkJfUihl06qY7RkuUKubhYAn7vHuEB" +
                                                   "+G+O2iq+Hr593ErIIr8EzW376/bgU15L+Wt1dtpeF7xSrH+s9goH3KdAw1ABFPVFT" +
                                                   "q37Ee6ygmMVx3yUfteUFssHY6wCQ8bD/IOfjagQfVMLrl5qqGw2EZjB1yqahH+Id7" +
                                                   "Ktuc/zqcrjxhEVrcITqqxJA5BDsodkFvHHrHNb4w/R/qeKDUQOJQGh+Ts4tbECgYE" +
                                                   "Aw6ywHpDlRhBxF0WjOFZ5KjVBkaY5iCp1l6Ef1oTvyjkT1YGb9M/SJhQbd5mFNaUt" +
                                                   "4EChmIixkDEh7ixYLReIonjWVqTJZrllIVWXkDFxMdQgt2HTUyouCRrpsD5PdFhuV" +
                                                   "KPwfU09u+Bj46cyKX3j1OsPmoPk1JiWbA6Oh8OHGk0CgYEAvTSn8qJ8MX7Jdn3+xh" +
                                                   "v7EdLMrzPV2L6xmljFiBdeHj0Aaj03YHPsz2lewaMNeiVd3eThxjqJImLh+vFmvdO" +
                                                   "/edeEj/Qh5H/6ycnIi8Xbdc4Y4DlKnsIYBk6hd63XRuLd8kfWPKV9fqEotidj29T7" +
                                                   "6maxoIk3u6X/26x/OEOB/J0CgYA1Nt6CwYcIwenvmUmlRacX+nnFgX498MYgIXqGH" +
                                                   "YsU5Obm9qOSNX6CSo+ZvA+FIlHSneEUmYAopaQDoN2uDatj/BbWY2Q+YocFRMC+Py" +
                                                   "P4mCb5mEofYOY6Ja6N9rQnAPGoZRk/CmWSlZi0zcCPliQCwJZywBHYW1L1OhQ7Ccr" +
                                                   "RNQKBgGZ649DG82+3lZVJjzpso2O3AsiO0fAw8W+BT5Rz27WTIutoNttWTtjU4M8O" +
                                                   "6tjS7nGmbCd7QxXN60qJgDWnQFnVQubZu5XRP9wWIDqcs06uj8i7H1C4Hl0kL87r6" +
                                                   "ONrM/3rrP1yQXPaBHwM/8htJvmCIAms6PQJaHjqiInH/tr9AoGAJkhDzSkQEDRQ8y" +
                                                   "+Sb1YT4XPXZANQtu6wE8UlX3DMPbyCHoMiuS/UZX1nTabu588RPgP3Gm5ZmVamoKT" +
                                                   "spzZHKTwszdDcXq2pLlg0sP4TvRk0YPNScDOvnEP/Bvvhft2mj6zUnp6tZaYuhFBa" +
                                                   "YwV/CxpANbAPwA2CqJxhaZ0ELcV0AAZQS0NTIzh+cgAZamF2YS5zZWN1cml0eS5LZ" +
                                                   "XlSZXAkVHlwZQAAAAAAAAAAEgAAeHIADmphdmEubGFuZy5FbnVtAAAAAAAAAAASAA" +
                                                   "B4cHQAB1BSSVZBVEU=";

    private static final String publicKeyString = "rO0ABXNyABRqYXZhLnNlY3VyaXR5LktleVJlcL35T7OImqVDAgAETAAJYWxnb3JpdG" +
                                                  "htdAASTGphdmEvbGFuZy9TdHJpbmc7WwAHZW5jb2RlZHQAAltCTAAGZm9ybWF0cQB+" +
                                                  "AAFMAAR0eXBldAAbTGphdmEvc2VjdXJpdHkvS2V5UmVwJFR5cGU7eHB0AANSU0F1cg" +
                                                  "ACW0Ks8xf4BghU4AIAAHhwAAABJjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoC" +
                                                  "ggEBAJCevXlzg1WkhXcSz4OTJj+lzSeSiUmQngsKGEvn0bqQXwcwZVIZr0VA9g1990" +
                                                  "Kqmk7ltrEZuKNIaPcrtXZ5YoWUAksMxEWlXtNtwLsl5OjC80oAZN9UqdHeye9Z5zA6" +
                                                  "LrwON2PEHnIJmZlTOJe9J4i+C/SX52/3AobTmCZYebBgn4QaiX2Jldc5w04utzDbqe" +
                                                  "k9pUTAy21tdjnHOsFeST1ELRxSQqzDYPCg12ehMhy5evd56eIe9S8sHH+llJBnvKsc" +
                                                  "DVOHbc+5cozvVTppdAsKi51ttIe9j5fqS9UE46ALPOyk9UdilVXMnz5S2aBh4d1AIT" +
                                                  "s60DbtSibkik+L7TkCAwEAAXQABVguNTA5fnIAGWphdmEuc2VjdXJpdHkuS2V5UmVw" +
                                                  "JFR5cGUAAAAAAAAAABIAAHhyAA5qYXZhLmxhbmcuRW51bQAAAAAAAAAAEgAAeHB0AA" +
                                                  "ZQVUJMSUM=";

    private static final PrivateKey privateKey;

    private static final PublicKey publicKey;

    static
    {
        ObjectSerializer serializer = new ObjectSerializer();

        privateKey = serializer.readObject(PrivateKey.class, Base64.decodeBase64(privateKeyString));
        publicKey = serializer.readObject(PublicKey.class, Base64.decodeBase64(publicKeyString));
    }

    private DataSignatureManager manager;

    public TestDataSignatureManager()
    {
        this.manager = new DataSignatureManager();
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
    public void testSignature01()
    {
        byte[] data = new byte[] { (byte)2, (byte)3, (byte)5, (byte)7, (byte)11, (byte)13, (byte)17 };

        byte[] signature = this.manager.signData(
                TestDataSignatureManager.privateKey, data
        );

        this.manager.verifySignature(
                TestDataSignatureManager.publicKey, data, signature
        );
    }

    @Test(expected=InvalidSignatureException.class)
    public void testSignature02()
    {
        byte[] data = new byte[] { (byte)2, (byte)3, (byte)5, (byte)7, (byte)11, (byte)13, (byte)17 };

        byte[] signature = this.manager.signData(
                TestDataSignatureManager.privateKey, data
        );

        data[4] = 76;

        this.manager.verifySignature(
                TestDataSignatureManager.publicKey, data, signature
        );
    }

    @Test
    public void testSignature03()
    {
        byte[] data = Arrays.copyOf(new byte[] { (byte)19, (byte)23, (byte)29, (byte)31, (byte)37, (byte)41, (byte)43 }, 629383);

        byte[] signature = this.manager.signData(
                TestDataSignatureManager.privateKey, data
        );

        this.manager.verifySignature(
                TestDataSignatureManager.publicKey, data, signature
        );
    }

    @Test(expected=InvalidSignatureException.class)
    public void testSignature04()
    {
        byte[] data = Arrays.copyOf(new byte[] { (byte)19, (byte)23, (byte)29, (byte)31, (byte)37, (byte)41, (byte)43 }, 629383);

        byte[] signature = this.manager.signData(
                TestDataSignatureManager.privateKey, data
        );

        data[6983] = 76;

        this.manager.verifySignature(
                TestDataSignatureManager.publicKey, data, signature
        );
    }
}