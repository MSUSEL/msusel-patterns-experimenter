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
/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. See accompanying LICENSE file.
 */
package org.apache.hadoop.security.authentication.util;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Signs strings and verifies signed strings using a SHA digest.
 */
public class Signer {
  private static final String SIGNATURE = "&s=";

  private byte[] secret;

  /**
   * Creates a Signer instance using the specified secret.
   *
   * @param secret secret to use for creating the digest.
   */
  public Signer(byte[] secret) {
    if (secret == null) {
      throw new IllegalArgumentException("secret cannot be NULL");
    }
    this.secret = secret.clone();
  }

  /**
   * Returns a signed string.
   * <p/>
   * The signature '&s=SIGNATURE' is appended at the end of the string.
   *
   * @param str string to sign.
   *
   * @return the signed string.
   */
  public String sign(String str) {
    if (str == null || str.length() == 0) {
      throw new IllegalArgumentException("NULL or empty string to sign");
    }
    String signature = computeSignature(str);
    return str + SIGNATURE + signature;
  }

  /**
   * Verifies a signed string and extracts the original string.
   *
   * @param signedStr the signed string to verify and extract.
   *
   * @return the extracted original string.
   *
   * @throws SignerException thrown if the given string is not a signed string or if the signature is invalid.
   */
  public String verifyAndExtract(String signedStr) throws SignerException {
    int index = signedStr.lastIndexOf(SIGNATURE);
    if (index == -1) {
      throw new SignerException("Invalid signed text: " + signedStr);
    }
    String originalSignature = signedStr.substring(index + SIGNATURE.length());
    String rawValue = signedStr.substring(0, index);
    String currentSignature = computeSignature(rawValue);
    if (!originalSignature.equals(currentSignature)) {
      throw new SignerException("Invalid signature");
    }
    return rawValue;
  }

  /**
   * Returns then signature of a string.
   *
   * @param str string to sign.
   *
   * @return the signature for the string.
   */
  protected String computeSignature(String str) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA");
      md.update(str.getBytes());
      md.update(secret);
      byte[] digest = md.digest();
      return new Base64(0).encodeToString(digest);
    } catch (NoSuchAlgorithmException ex) {
      throw new RuntimeException("It should not happen, " + ex.getMessage(), ex);
    }
  }

}
