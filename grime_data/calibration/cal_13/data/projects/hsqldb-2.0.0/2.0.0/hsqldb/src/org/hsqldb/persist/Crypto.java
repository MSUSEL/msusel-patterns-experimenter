/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.hsqldb.persist;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.StringConverter;

public class Crypto {

    SecretKeySpec key;
    Cipher        outCipher;
    Cipher        inCipher;

    public Crypto(String keyString, String cipherName, String provider) {

        try {
            byte[] encodedKey =
                StringConverter.hexStringToByteArray(keyString);

            key       = new SecretKeySpec(encodedKey, cipherName);
            outCipher = provider == null ? Cipher.getInstance(cipherName)
                                         : Cipher.getInstance(cipherName,
                                         provider);

            outCipher.init(Cipher.ENCRYPT_MODE, key);

            inCipher = provider == null ? Cipher.getInstance(cipherName)
                                        : Cipher.getInstance(cipherName,
                                        provider);

            inCipher.init(Cipher.DECRYPT_MODE, key);

            return;
        } catch (NoSuchPaddingException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        } catch (NoSuchAlgorithmException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        } catch (InvalidKeyException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        } catch (NoSuchProviderException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        } catch (IOException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        }
    }

    public synchronized InputStream getInputStream(InputStream in) {

        if (inCipher == null) {
            return in;
        }

        try {
            inCipher.init(Cipher.DECRYPT_MODE, key);

            return new CipherInputStream(in, inCipher);
        } catch (java.security.InvalidKeyException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        }
    }

    public synchronized OutputStream getOutputStream(OutputStream out) {

        if (outCipher == null) {
            return out;
        }

        try {
            outCipher.init(Cipher.ENCRYPT_MODE, key);

            return new CipherOutputStream(out, outCipher);
        } catch (java.security.InvalidKeyException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        }
    }

    public synchronized int decode(byte[] source, int sourceOffset,
                                   int length, byte[] dest, int destOffset) {

        if (inCipher == null) {
            return length;
        }

        try {
            inCipher.init(Cipher.DECRYPT_MODE, key);

            return inCipher.doFinal(source, sourceOffset, length, dest,
                                    destOffset);
        } catch (java.security.InvalidKeyException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        } catch (BadPaddingException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        } catch (IllegalBlockSizeException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        } catch (ShortBufferException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        }
    }

    public synchronized int encode(byte[] source, int sourceOffset,
                                   int length, byte[] dest, int destOffset) {

        if (outCipher == null) {
            return length;
        }

        try {
            outCipher.init(Cipher.ENCRYPT_MODE, key);

            return outCipher.doFinal(source, sourceOffset, length, dest,
                                     destOffset);
        } catch (java.security.InvalidKeyException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        } catch (BadPaddingException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        } catch (IllegalBlockSizeException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        } catch (ShortBufferException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        }
    }

    public static byte[] getNewKey(String cipherName, String provider) {

        try {
            KeyGenerator generator = provider == null
                                     ? KeyGenerator.getInstance(cipherName)
                                     : KeyGenerator.getInstance(cipherName,
                                         provider);
            SecretKey key = generator.generateKey();
            byte[]    raw = key.getEncoded();

            return raw;
        } catch (java.security.NoSuchAlgorithmException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        } catch (NoSuchProviderException e) {
            throw Error.error(ErrorCode.X_S0531, e);
        }
    }

    public synchronized int getEncodedSize(int size) {

        try {
            return outCipher.getOutputSize(size);
        } catch (IllegalStateException ex) {
            try {
                outCipher.init(Cipher.ENCRYPT_MODE, key);

                return outCipher.getOutputSize(size);
            } catch (java.security.InvalidKeyException e) {
                throw Error.error(ErrorCode.X_S0531, e);
            }
        }
    }
}
