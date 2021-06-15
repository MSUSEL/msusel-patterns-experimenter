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
package org.apache.hadoop.mapred;

import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.net.NetworkTopology;

import junit.framework.TestCase;

public class TestGetSplitHosts extends TestCase {

  public void testGetSplitHosts() throws Exception {

    int numBlocks = 3;
    int block1Size = 100, block2Size = 150, block3Size = 75;
    int fileSize = block1Size + block2Size + block3Size;
    int replicationFactor = 3;
    NetworkTopology clusterMap = new NetworkTopology();
    
    BlockLocation[] bs = new BlockLocation[numBlocks];
    
    String [] block1Hosts = {"host1","host2","host3"};
    String [] block1Names = {"host1:100","host2:100","host3:100"};
    String [] block1Racks = {"/rack1/","/rack1/","/rack2/"};
    String [] block1Paths = new String[replicationFactor];
    
    for (int i = 0; i < replicationFactor; i++) { 
      block1Paths[i] = block1Racks[i]+block1Names[i];
    }
    
    bs[0] = new BlockLocation(block1Names,block1Hosts,
                              block1Paths,0,block1Size);


    String [] block2Hosts = {"host4","host5","host6"};
    String [] block2Names = {"host4:100","host5:100","host6:100"};
    String [] block2Racks = {"/rack2/","/rack3/","/rack3/"};
    String [] block2Paths = new String[replicationFactor];
    
    for (int i = 0; i < replicationFactor; i++) { 
      block2Paths[i] = block2Racks[i]+block2Names[i];
    }

    bs[1] = new BlockLocation(block2Names,block2Hosts,
                              block2Paths,block1Size,block2Size);

    String [] block3Hosts = {"host1","host7","host8"};
    String [] block3Names = {"host1:100","host7:100","host8:100"};
    String [] block3Racks = {"/rack1/","/rack4/","/rack4/"};
    String [] block3Paths = new String[replicationFactor];
    
    for (int i = 0; i < replicationFactor; i++) { 
      block3Paths[i] = block3Racks[i]+block3Names[i];
    }

    bs[2] = new BlockLocation(block3Names,block3Hosts,
                              block3Paths,block1Size+block2Size,
                              block3Size);

    
    SequenceFileInputFormat< String, String> sif = 
      new SequenceFileInputFormat<String,String>();
    String [] hosts = sif.getSplitHosts(bs, 0, fileSize, clusterMap);

    // Contributions By Racks are
    // Rack1   175       
    // Rack2   275       
    // Rack3   150       
    // So, Rack2 hosts, host4 and host 3 should be returned
    // even if their individual contribution is not the highest

    assertTrue (hosts.length == replicationFactor);
    assertTrue(hosts[0].equalsIgnoreCase("host4"));
    assertTrue(hosts[1].equalsIgnoreCase("host3"));
    assertTrue(hosts[2].equalsIgnoreCase("host1"));
    
    
    // Now Create the blocks without topology information
    bs[0] = new BlockLocation(block1Names,block1Hosts,0,block1Size);
    bs[1] = new BlockLocation(block2Names,block2Hosts,block1Size,block2Size);
    bs[2] = new BlockLocation(block3Names,block3Hosts,block1Size+block2Size,
                               block3Size);

    hosts = sif.getSplitHosts(bs, 0, fileSize, clusterMap);
    
    // host1 makes the highest contribution among all hosts
    // So, that should be returned before others
    
    assertTrue (hosts.length == replicationFactor);
    assertTrue(hosts[0].equalsIgnoreCase("host1"));
  }
}
