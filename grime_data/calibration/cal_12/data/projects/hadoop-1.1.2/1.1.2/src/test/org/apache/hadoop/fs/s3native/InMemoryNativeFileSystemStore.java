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
package org.apache.hadoop.fs.s3native;

import static org.apache.hadoop.fs.s3native.NativeS3FileSystem.PATH_DELIMITER;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;

/**
 * <p>
 * A stub implementation of {@link NativeFileSystemStore} for testing
 * {@link NativeS3FileSystem} without actually connecting to S3.
 * </p>
 */
class InMemoryNativeFileSystemStore implements NativeFileSystemStore {
  
  private Configuration conf;
  
  private SortedMap<String, FileMetadata> metadataMap =
    new TreeMap<String, FileMetadata>();
  private SortedMap<String, byte[]> dataMap = new TreeMap<String, byte[]>();

  public void initialize(URI uri, Configuration conf) throws IOException {
    this.conf = conf;
  }

  public void storeEmptyFile(String key) throws IOException {
    metadataMap.put(key, new FileMetadata(key, 0, System.currentTimeMillis()));
    dataMap.put(key, new byte[0]);
  }

  public void storeFile(String key, File file, byte[] md5Hash)
    throws IOException {
    
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] buf = new byte[8192];
    int numRead;
    BufferedInputStream in = null;
    try {
      in = new BufferedInputStream(new FileInputStream(file));
      while ((numRead = in.read(buf)) >= 0) {
        out.write(buf, 0, numRead);
      }
    } finally {
      if (in != null) {
        in.close();
      }
    }
    metadataMap.put(key,
        new FileMetadata(key, file.length(), System.currentTimeMillis()));
    dataMap.put(key, out.toByteArray());
  }

  public InputStream retrieve(String key) throws IOException {
    return retrieve(key, 0);
  }
  
  public InputStream retrieve(String key, long byteRangeStart)
    throws IOException {
    
    byte[] data = dataMap.get(key);
    File file = createTempFile();
    BufferedOutputStream out = null;
    try {
      out = new BufferedOutputStream(new FileOutputStream(file));
      out.write(data, (int) byteRangeStart,
          data.length - (int) byteRangeStart);
    } finally {
      if (out != null) {
        out.close();
      }
    }
    return new FileInputStream(file);
  }
  
  private File createTempFile() throws IOException {
    File dir = new File(conf.get("fs.s3.buffer.dir"));
    if (!dir.exists() && !dir.mkdirs()) {
      throw new IOException("Cannot create S3 buffer directory: " + dir);
    }
    File result = File.createTempFile("test-", ".tmp", dir);
    result.deleteOnExit();
    return result;
  }

  public FileMetadata retrieveMetadata(String key) throws IOException {
    return metadataMap.get(key);
  }

  public PartialListing list(String prefix, int maxListingLength)
      throws IOException {
    return list(prefix, maxListingLength, null, false);
  }

  public PartialListing list(String prefix, int maxListingLength,
      String priorLastKey, boolean recursive) throws IOException {

    return list(prefix, recursive ? null : PATH_DELIMITER, maxListingLength, priorLastKey);
  }

  private PartialListing list(String prefix, String delimiter,
      int maxListingLength, String priorLastKey) throws IOException {

    if (prefix.length() > 0 && !prefix.endsWith(PATH_DELIMITER)) {
      prefix += PATH_DELIMITER;
    }
    
    List<FileMetadata> metadata = new ArrayList<FileMetadata>();
    SortedSet<String> commonPrefixes = new TreeSet<String>();
    for (String key : dataMap.keySet()) {
      if (key.startsWith(prefix)) {
        if (delimiter == null) {
          metadata.add(retrieveMetadata(key));
        } else {
          int delimIndex = key.indexOf(delimiter, prefix.length());
          if (delimIndex == -1) {
            metadata.add(retrieveMetadata(key));
          } else {
            String commonPrefix = key.substring(0, delimIndex);
            commonPrefixes.add(commonPrefix);
          }
        }
      }
      if (metadata.size() + commonPrefixes.size() == maxListingLength) {
        new PartialListing(key, metadata.toArray(new FileMetadata[0]),
            commonPrefixes.toArray(new String[0]));
      }
    }
    return new PartialListing(null, metadata.toArray(new FileMetadata[0]),
        commonPrefixes.toArray(new String[0]));
  }

  public void delete(String key) throws IOException {
    metadataMap.remove(key);
    dataMap.remove(key);
  }

  public void copy(String srcKey, String dstKey) throws IOException {
    metadataMap.put(dstKey, metadataMap.get(srcKey));
    dataMap.put(dstKey, dataMap.get(srcKey));
  }
  
  public void purge(String prefix) throws IOException {
    Iterator<Entry<String, FileMetadata>> i =
      metadataMap.entrySet().iterator();
    while (i.hasNext()) {
      Entry<String, FileMetadata> entry = i.next();
      if (entry.getKey().startsWith(prefix)) {
        dataMap.remove(entry.getKey());
        i.remove();
      }
    }
  }

  public void dump() throws IOException {
    System.out.println(metadataMap.values());
    System.out.println(dataMap.keySet());
  }
}
