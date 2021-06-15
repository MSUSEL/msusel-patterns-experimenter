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
package org.apache.hadoop.security.token;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * The server-side secret manager for each token type.
 * @param <T> The type of the token identifier
 */
public abstract class SecretManager<T extends TokenIdentifier> {
  /**
   * The token was invalid and the message explains why.
   */
  @SuppressWarnings("serial")
  public static class InvalidToken extends IOException {
    public InvalidToken(String msg) { 
      super(msg);
    }
  }
  
  /**
   * Create the password for the given identifier.
   * identifier may be modified inside this method.
   * @param identifier the identifier to use
   * @return the new password
   */
  protected abstract byte[] createPassword(T identifier);
  
  /**
   * Retrieve the password for the given token identifier. Should check the date
   * or registry to make sure the token hasn't expired or been revoked. Returns 
   * the relevant password.
   * @param identifier the identifier to validate
   * @return the password to use
   * @throws InvalidToken the token was invalid
   */
  public abstract byte[] retrievePassword(T identifier) throws InvalidToken;
  
  /**
   * Create an empty token identifier.
   * @return the newly created empty token identifier
   */
  public abstract T createIdentifier();
  
  /**
   * The name of the hashing algorithm.
   */
  private static final String DEFAULT_HMAC_ALGORITHM = "HmacSHA1";

  /**
   * The length of the random keys to use.
   */
  private static final int KEY_LENGTH = 64;

  /**
   * A thread local store for the Macs.
   */
  private static final ThreadLocal<Mac> threadLocalMac =
    new ThreadLocal<Mac>(){
    @Override
    protected Mac initialValue() {
      try {
        return Mac.getInstance(DEFAULT_HMAC_ALGORITHM);
      } catch (NoSuchAlgorithmException nsa) {
        throw new IllegalArgumentException("Can't find " + DEFAULT_HMAC_ALGORITHM +
                                           " algorithm.");
      }
    }
  };

  /**
   * Key generator to use.
   */
  private final KeyGenerator keyGen;
  {
    try {
      keyGen = KeyGenerator.getInstance(DEFAULT_HMAC_ALGORITHM);
      keyGen.init(KEY_LENGTH);
    } catch (NoSuchAlgorithmException nsa) {
      throw new IllegalArgumentException("Can't find " + DEFAULT_HMAC_ALGORITHM +
      " algorithm.");
    }
  }

  /**
   * Generate a new random secret key.
   * @return the new key
   */
  protected SecretKey generateSecret() {
    SecretKey key;
    synchronized (keyGen) {
      key = keyGen.generateKey();
    }
    return key;
  }

  /**
   * Compute HMAC of the identifier using the secret key and return the 
   * output as password
   * @param identifier the bytes of the identifier
   * @param key the secret key
   * @return the bytes of the generated password
   */
  protected static byte[] createPassword(byte[] identifier, 
                                         SecretKey key) {
    Mac mac = threadLocalMac.get();
    try {
      mac.init(key);
    } catch (InvalidKeyException ike) {
      throw new IllegalArgumentException("Invalid key to HMAC computation", 
                                         ike);
    }
    return mac.doFinal(identifier);
  }
  
  /**
   * Convert the byte[] to a secret key
   * @param key the byte[] to create a secret key from
   * @return the secret key
   */
  protected static SecretKey createSecretKey(byte[] key) {
    return new SecretKeySpec(key, DEFAULT_HMAC_ALGORITHM);
  }
}
