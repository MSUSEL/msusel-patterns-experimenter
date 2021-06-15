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
package org.apache.hadoop.mapreduce.security.token;

import java.util.Map;
import java.util.TreeMap;

import javax.crypto.SecretKey;

import org.apache.hadoop.security.token.SecretManager;
import org.apache.hadoop.security.token.Token;

/**
 * SecretManager for job token. It can be used to cache generated job tokens.
 */
public class JobTokenSecretManager extends SecretManager<JobTokenIdentifier> {
  private final SecretKey masterKey;
  private final Map<String, SecretKey> currentJobTokens;

  /**
   * Convert the byte[] to a secret key
   * @param key the byte[] to create the secret key from
   * @return the secret key
   */
  public static SecretKey createSecretKey(byte[] key) {
    return SecretManager.createSecretKey(key);
  }
  
  /**
   * Compute the HMAC hash of the message using the key
   * @param msg the message to hash
   * @param key the key to use
   * @return the computed hash
   */
  public static byte[] computeHash(byte[] msg, SecretKey key) {
    return createPassword(msg, key);
  }
  
  /**
   * Default constructor
   */
  public JobTokenSecretManager() {
    this.masterKey = generateSecret();
    this.currentJobTokens = new TreeMap<String, SecretKey>();
  }
  
  /**
   * Create a new password/secret for the given job token identifier.
   * @param identifier the job token identifier
   * @return token password/secret
   */
  @Override
  public byte[] createPassword(JobTokenIdentifier identifier) {
    byte[] result = createPassword(identifier.getBytes(), masterKey);
    return result;
  }

  /**
   * Add the job token of a job to cache
   * @param jobId the job that owns the token
   * @param token the job token
   */
  public void addTokenForJob(String jobId, Token<JobTokenIdentifier> token) {
    SecretKey tokenSecret = createSecretKey(token.getPassword());
    synchronized (currentJobTokens) {
      currentJobTokens.put(jobId, tokenSecret);
    }
  }

  /**
   * Remove the cached job token of a job from cache
   * @param jobId the job whose token is to be removed
   */
  public void removeTokenForJob(String jobId) {
    synchronized (currentJobTokens) {
      currentJobTokens.remove(jobId);
    }
  }
  
  /**
   * Look up the token password/secret for the given jobId.
   * @param jobId the jobId to look up
   * @return token password/secret as SecretKey
   * @throws InvalidToken
   */
  public SecretKey retrieveTokenSecret(String jobId) throws InvalidToken {
    SecretKey tokenSecret = null;
    synchronized (currentJobTokens) {
      tokenSecret = currentJobTokens.get(jobId);
    }
    if (tokenSecret == null) {
      throw new InvalidToken("Can't find job token for job " + jobId + " !!");
    }
    return tokenSecret;
  }
  
  /**
   * Look up the token password/secret for the given job token identifier.
   * @param identifier the job token identifier to look up
   * @return token password/secret as byte[]
   * @throws InvalidToken
   */
  @Override
  public byte[] retrievePassword(JobTokenIdentifier identifier)
      throws InvalidToken {
    return retrieveTokenSecret(identifier.getJobId().toString()).getEncoded();
  }
  
  /**
   * Create an empty job token identifier
   * @return a newly created empty job token identifier
   */
  @Override
  public JobTokenIdentifier createIdentifier() {
    return new JobTokenIdentifier();
  }
}
