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
package org.apache.hadoop.tools.rumen;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The purpose of this class is to generate new random seeds from a master
 * seed. This is needed to make the Random().next*() calls in rumen and mumak
 * deterministic so that mumak simulations become deterministically replayable.
 *
 * In these tools we need many independent streams of random numbers, some of
 * which are created dynamically. We seed these streams with the sub-seeds 
 * returned by RandomSeedGenerator.
 * 
 * For a slightly more complicated approach to generating multiple streams of 
 * random numbers with better theoretical guarantees, see
 * P. L'Ecuyer, R. Simard, E. J. Chen, and W. D. Kelton, 
 * ``An Objected-Oriented Random-Number Package with Many Long Streams and 
 * Substreams'', Operations Research, 50, 6 (2002), 1073--1075
 * http://www.iro.umontreal.ca/~lecuyer/papers.html
 * http://www.iro.umontreal.ca/~lecuyer/myftp/streams00/
 */
public class RandomSeedGenerator {
  private static Log LOG = LogFactory.getLog(RandomSeedGenerator.class);
  
  /** MD5 algorithm instance, one for each thread. */
  private static final ThreadLocal<MessageDigest> md5Holder =
      new ThreadLocal<MessageDigest>() {
        @Override protected MessageDigest initialValue() {
          MessageDigest md5 = null; 
          try {
            md5 = MessageDigest.getInstance("MD5");
          } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException("Can't create MD5 digests", nsae);
          }
          return md5;
        }
      };
      
  /**
   * Generates a new random seed.
   *
   * @param streamId a string identifying the stream of random numbers
   * @param masterSeed higher level master random seed
   * @return the random seed. Different (streamId, masterSeed) pairs result in
   *         (vastly) different random seeds.
   */   
  public static long getSeed(String streamId, long masterSeed) {
    MessageDigest md5 = md5Holder.get();
    md5.reset();
    //'/' : make sure that we don't get the same str from ('11',0) and ('1',10)
    // We could have fed the bytes of masterSeed one by one to md5.update()
    // instead
    String str = streamId + '/' + masterSeed;
    byte[] digest = md5.digest(str.getBytes());
    // Create a long from the first 8 bytes of the digest
    // This is fine as MD5 has the avalanche property.
    // Paranoids could have XOR folded the other 8 bytes in too. 
    long seed = 0;
    for (int i=0; i<8; i++) {
      seed = (seed<<8) + ((int)digest[i]+128);
    }
    return seed;
  }
}
