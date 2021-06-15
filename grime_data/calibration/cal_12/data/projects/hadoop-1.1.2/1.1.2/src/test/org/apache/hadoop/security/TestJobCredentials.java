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

package org.apache.hadoop.security;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collection;

import static org.mockito.Mockito.mock;

import javax.crypto.KeyGenerator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.security.Credentials;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.security.token.TokenIdentifier;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestJobCredentials {
  private static final String DEFAULT_HMAC_ALGORITHM = "HmacSHA1";
  private static final File tmpDir =
    new File(System.getProperty("test.build.data", "/tmp"), "mapred");  
    
  @Before
  public void setUp() {
    tmpDir.mkdir();
  }
  
  @SuppressWarnings("unchecked")
  @Test 
  public void testReadWriteStorage() 
  throws IOException, NoSuchAlgorithmException{
    // create tokenStorage Object
    Credentials ts = new Credentials();
    
    Token<? extends TokenIdentifier> token1 = new Token();
    Token<? extends TokenIdentifier> token2 = new Token();
    Text service1 = new Text("service1");
    Text service2 = new Text("service2");
    Collection<Text> services = new ArrayList<Text>();
    
    services.add(service1);
    services.add(service2);
    
    token1.setService(service1);
    token2.setService(service2);
    ts.addToken(new Text("sometoken1"), token1);
    ts.addToken(new Text("sometoken2"), token2);
    
    // create keys and put it in
    final KeyGenerator kg = KeyGenerator.getInstance(DEFAULT_HMAC_ALGORITHM);
    String alias = "alias";
    Map<Text, byte[]> m = new HashMap<Text, byte[]>(10);
    for(int i=0; i<10; i++) {
      Key key = kg.generateKey();
      m.put(new Text(alias+i), key.getEncoded());
      ts.addSecretKey(new Text(alias+i), key.getEncoded());
    }
   
    // create file to store
    File tmpFileName = new File(tmpDir, "tokenStorageTest");
    DataOutputStream dos = 
      new DataOutputStream(new FileOutputStream(tmpFileName));
    ts.write(dos);
    dos.close();
    
    // open and read it back
    DataInputStream dis = 
      new DataInputStream(new FileInputStream(tmpFileName));    
    ts = new Credentials();
    ts.readFields(dis);
    dis.close();
    
    // get the tokens and compare the services
    Collection<Token<? extends TokenIdentifier>> list = ts.getAllTokens();
    assertEquals("getAllTokens should return collection of size 2", 
        list.size(), 2);
    boolean foundFirst = false;
    boolean foundSecond = false;
    for (Token<? extends TokenIdentifier> token : list) {
      if (token.getService().equals(service1)) {
        foundFirst = true;
      }
      if (token.getService().equals(service2)) {
        foundSecond = true;
      }
    }
    assertTrue("Tokens for services service1 and service2 must be present", 
        foundFirst && foundSecond);
    // compare secret keys
    int mapLen = m.size();
    assertEquals("wrong number of keys in the Storage", 
        mapLen, ts.numberOfSecretKeys());
    for(Text a : m.keySet()) {
      byte [] kTS = ts.getSecretKey(a);
      byte [] kLocal = m.get(a);
      assertTrue("keys don't match for " + a, 
          WritableComparator.compareBytes(kTS, 0, kTS.length, kLocal,
              0, kLocal.length)==0);
    }
  }
 }
