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
package io.oddsource.java.licensing;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for DeserializingLicenseProvider.
 */
public class TestDeserializingLicenseProvider
{
    private DeserializingLicenseProvider provider;

    @Before
    public void setUp()
    {
        this.provider = EasyMock.createMockBuilder(DeserializingLicenseProvider.class).
            addMockedMethod("getLicenseData").createStrictMock();
    }

    @After
    public void tearDown()
    {
        EasyMock.verify(this.provider);
    }

    @Test
    public void testGetLicense01()
    {
        EasyMock.expect(this.provider.getLicenseData("nullCustomer01")).andReturn(null);
        EasyMock.replay(this.provider);

        SignedLicense retrieved = this.provider.getLicense("nullCustomer01");

        assertNull("The retrieved license should be null.", retrieved);
    }

    @Test
    public void testGetLicense02()
    {
        byte[] licenseContent = new byte[] {0x1F};
        byte[] signatureContent = new byte[] {0x2F};
        SignedLicense license = new SignedLicense(licenseContent, signatureContent);
        byte[] serialized = new ObjectSerializer().writeObject(license);

        EasyMock.expect(this.provider.getLicenseData("testCustomer02")).andReturn(serialized);
        EasyMock.replay(this.provider);

        SignedLicense retrieved = this.provider.getLicense("testCustomer02");

        assertNotNull("The retrieved license should not be null.", retrieved);
        assertArrayEquals("The license is not correct.", licenseContent, retrieved.getLicenseContent());
        assertArrayEquals("The signature is not correct.", signatureContent, retrieved.getSignatureContent());
    }

    @Test
    public void testGetLicense03()
    {
        byte[] licenseContent = new byte[] {0x3F};
        byte[] signatureContent = new byte[] {0x4F};
        SignedLicense license = new SignedLicense(licenseContent, signatureContent);
        byte[] serialized = new ObjectSerializer().writeObject(license);

        EasyMock.expect(this.provider.getLicenseData("anotherCustomer03")).andReturn(serialized);
        EasyMock.replay(this.provider);

        SignedLicense retrieved = this.provider.getLicense("anotherCustomer03");

        assertNotNull("The retrieved license should not be null.", retrieved);
        assertArrayEquals("The license is not correct.", licenseContent, retrieved.getLicenseContent());
        assertArrayEquals("The signature is not correct.", signatureContent, retrieved.getSignatureContent());
    }
}
