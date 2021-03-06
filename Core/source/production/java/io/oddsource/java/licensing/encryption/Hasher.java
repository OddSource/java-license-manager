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
package io.oddsource.java.licensing.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

import io.oddsource.java.licensing.LicensingCharsets;
import io.oddsource.java.licensing.exception.AlgorithmNotSupportedException;

/**
 * Used for creating hash keys of things that won't need to be unencrypted.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Hasher
{
    /**
     * The algorithm we use to hash strings.
     */
    private static final String algorithm = "SHA-512";

    /**
     * The salt that we use to hash strings.
     */
    private static final String salt = "j4KgU305PZp't.\"%ordAY7q*?z9%8]amNL(0Wx5eG49b1sRj(^;8Kg2w0EoM";

    /**
     * This class cannot be instantiated.
     */
    private Hasher()
    {
        throw new RuntimeException("This class cannot be instantiated.");
    }

    /**
     * Calculate the SHA-512 message digest hash of the
     * provided string and return it with its binary
     * data Base64 encoded.
     *
     * @param string The string to hash
     *
     * @return the hashed string Base64 encoded.
     */
    public static String hash(final String string)
    {
        try
        {
            return new String(
                Base64.encodeBase64(
                    MessageDigest.getInstance(Hasher.algorithm).digest(
                        (string + Hasher.salt).getBytes(LicensingCharsets.UTF_8)
                    )
                ),
                LicensingCharsets.UTF_8
            );
        }
        catch(final NoSuchAlgorithmException e)
        {
            throw new AlgorithmNotSupportedException(Hasher.algorithm, e);
        }
    }
}
