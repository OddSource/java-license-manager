/*
 * SignedLicense.java from LicenseManager modified Tuesday, February 21, 2012 10:59:35 CST (-0600).
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

import java.io.Serializable;
import java.util.Arrays;

/**
 * This class contains the encrypted license content and the signature for the
 * encrypted license content.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public final class SignedLicense implements Serializable
{
    private final static long serialVersionUID = -8465360339059185020L;

    private final byte[] licenseContent;

    private final byte[] signatureContent;

    public SignedLicense(byte[] licenseContent, byte[] signatureContent)
    {
        this.licenseContent = Arrays.copyOf(licenseContent, licenseContent.length);
        this.signatureContent = Arrays.copyOf(signatureContent, signatureContent.length);
    }

    /**
     * Get the content of the actual license object. This is encrypted and
     * corresponds to {@link License}. For security reasons, only a copy of
     * the content is returned.
     *
     * @return the encrypted license content.
     */
    public final byte[] getLicenseContent()
    {
        return Arrays.copyOf(this.licenseContent, this.licenseContent.length);
    }

    /**
     * Get the signature for the license content. For security reasons, only a
     * copy of the signature is returned.
     *
     * @return the license signature.
     */
    public final byte[] getSignatureContent()
    {
        return Arrays.copyOf(this.signatureContent, this.signatureContent.length);
    }

    /**
     * Erase the contents of this object. This is a security feature to write
     * zeroes to the license and signature data so that it doesn't hang around
     * in memory where it might be reverse engineered.
     */
    protected final void erase()
    {
        Arrays.fill(this.licenseContent, (byte)0);
        Arrays.fill(this.signatureContent, (byte)0);
    }
}
