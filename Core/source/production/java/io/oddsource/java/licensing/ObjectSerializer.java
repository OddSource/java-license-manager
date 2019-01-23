/*
 * ObjectSerializer.java from LicenseManager modified Thursday, May 17, 2012 21:31:40 CDT (-0500).
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

package io.oddsource.java.licensing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.io.output.ByteArrayOutputStream;

import io.oddsource.java.licensing.exception.ObjectDeserializationException;
import io.oddsource.java.licensing.exception.ObjectSerializationException;
import io.oddsource.java.licensing.exception.ObjectTypeNotExpectedException;

/**
 * This is a helper class for writing any object and reading simple objects (no
 * arrays, collections, or generic top-level objects) to and from byte arrays.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ObjectSerializer
{
    /**
     * Deserializes an object of the specified type from the provided byte stream.
     *
     * @param expectedType The type that is expected to be retrieved from {@code byteStream} (must implement {@link
     *     Serializable})
     * @param byteStream The byte stream to retrieve the object from (it must contain exactly one object, of the
     *     exact type passed to {@code expectedType})
     *
     * @return the requested unserialized object, presumably in the stream.
     *
     * @throws ObjectTypeNotExpectedException If the object found in the stream does not match the type {@code
     *     expectedType} or if a {@link ClassNotFoundException} or {@link NoClassDefFoundError} occurs
     * @throws ObjectDeserializationException If an I/O exception occurs while deserializing the object from the
     *     stream
     */
    public final <T extends Serializable> T readObject(final Class<T> expectedType, final byte[] byteStream)
        throws ObjectDeserializationException
    {
        final ByteArrayInputStream bytes = new ByteArrayInputStream(byteStream);
        try(final ObjectInputStream stream = new ObjectInputStream(bytes))
        {
            final Object allegedObject = stream.readObject();
            if(!expectedType.isInstance(allegedObject))
            {
                throw new ObjectTypeNotExpectedException(
                    expectedType.getName(),
                    allegedObject.getClass().getName()
                );
            }

            return expectedType.cast(allegedObject);
        }
        catch(final IOException e)
        {
            throw new ObjectDeserializationException(
                "An I/O error occurred while reading the object from the byte array.",
                e
            );
        }
        catch(final ClassNotFoundException | NoClassDefFoundError e)
        {
            throw new ObjectTypeNotExpectedException(
                expectedType.getName(),
                e.getMessage(),
                e
            );
        }
    }

    /**
     * Serializes the {@link Serializable} object passed and returns it as a byte array.
     *
     * @param object The object to serialize
     *
     * @return the byte stream with the object serialized in it.
     *
     * @throws ObjectSerializationException if an I/O exception occurs while serializing the object.
     */
    public final byte[] writeObject(final Serializable object) throws ObjectSerializationException
    {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try(final ObjectOutputStream stream = new ObjectOutputStream(bytes))
        {
            stream.writeObject(object);
        }
        catch(final IOException e)
        {
            throw new ObjectSerializationException(e);
        }

        return bytes.toByteArray();
    }
}
