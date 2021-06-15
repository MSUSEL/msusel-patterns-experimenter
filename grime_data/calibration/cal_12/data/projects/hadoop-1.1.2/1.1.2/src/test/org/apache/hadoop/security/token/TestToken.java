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

import java.io.*;
import java.util.Arrays;

import org.apache.hadoop.io.*;
import org.apache.hadoop.security.token.delegation.AbstractDelegationTokenIdentifier;

import junit.framework.TestCase;

/** Unit tests for Token */
public class TestToken extends TestCase {

  static boolean isEqual(Object a, Object b) {
    return a == null ? b == null : a.equals(b);
  }

  static boolean checkEqual(Token<TokenIdentifier> a, Token<TokenIdentifier> b) {
    return Arrays.equals(a.getIdentifier(), b.getIdentifier())
        && Arrays.equals(a.getPassword(), b.getPassword())
        && isEqual(a.getKind(), b.getKind())
        && isEqual(a.getService(), b.getService());
  }

  /**
   * Test token serialization
   */
  public void testTokenSerialization() throws IOException {
    // Get a token
    Token<TokenIdentifier> sourceToken = new Token<TokenIdentifier>();
    sourceToken.setService(new Text("service"));

    // Write it to an output buffer
    DataOutputBuffer out = new DataOutputBuffer();
    sourceToken.write(out);

    // Read the token back
    DataInputBuffer in = new DataInputBuffer();
    in.reset(out.getData(), out.getLength());
    Token<TokenIdentifier> destToken = new Token<TokenIdentifier>();
    destToken.readFields(in);
    assertTrue(checkEqual(sourceToken, destToken));
  }
  
  private static void checkUrlSafe(String str) throws Exception {
    int len = str.length();
    for(int i=0; i < len; ++i) {
      char ch = str.charAt(i);
      if (ch == '-') continue;
      if (ch == '_') continue;
      if (ch >= '0' && ch <= '9') continue;
      if (ch >= 'A' && ch <= 'Z') continue;
      if (ch >= 'a' && ch <= 'z') continue;
      fail("Encoded string " + str + 
           " has invalid character at position " + i);
    }
  }

  public static void testEncodeWritable() throws Exception {
    String[] values = new String[]{"", "a", "bb", "ccc", "dddd", "eeeee",
        "ffffff", "ggggggg", "hhhhhhhh", "iiiiiiiii",
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLM" +
             "NOPQRSTUVWXYZ01234567890!@#$%^&*()-=_+[]{}|;':,./<>?"};
    Token<AbstractDelegationTokenIdentifier> orig;
    Token<AbstractDelegationTokenIdentifier> copy = 
      new Token<AbstractDelegationTokenIdentifier>();
    // ensure that for each string the input and output values match
    for(int i=0; i< values.length; ++i) {
      String val = values[i];
      System.out.println("Input = " + val);
      orig = new Token<AbstractDelegationTokenIdentifier>(val.getBytes(),
          val.getBytes(), new Text(val), new Text(val));
      String encode = orig.encodeToUrlString();
      copy.decodeFromUrlString(encode);
      assertEquals(orig, copy);
      checkUrlSafe(encode);
    }
  }

}
