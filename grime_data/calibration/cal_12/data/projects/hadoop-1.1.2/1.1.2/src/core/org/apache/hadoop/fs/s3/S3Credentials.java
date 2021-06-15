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
package org.apache.hadoop.fs.s3;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;

/**
 * <p>
 * Extracts AWS credentials from the filesystem URI or configuration.
 * </p>
 */
public class S3Credentials {
  
  private String accessKey;
  private String secretAccessKey; 

  /**
   * @throws IllegalArgumentException if credentials for S3 cannot be
   * determined.
   */
  public void initialize(URI uri, Configuration conf) {
    if (uri.getHost() == null) {
      throw new IllegalArgumentException("Invalid hostname in URI " + uri);
    }
    
    String userInfo = uri.getUserInfo();
    if (userInfo != null) {
      int index = userInfo.indexOf(':');
      if (index != -1) {
        accessKey = userInfo.substring(0, index);
        secretAccessKey = userInfo.substring(index + 1);
      } else {
        accessKey = userInfo;
      }
    }
    
    String scheme = uri.getScheme();
    String accessKeyProperty = String.format("fs.%s.awsAccessKeyId", scheme);
    String secretAccessKeyProperty =
      String.format("fs.%s.awsSecretAccessKey", scheme);
    if (accessKey == null) {
      accessKey = conf.get(accessKeyProperty);
    }
    if (secretAccessKey == null) {
      secretAccessKey = conf.get(secretAccessKeyProperty);
    }
    if (accessKey == null && secretAccessKey == null) {
      throw new IllegalArgumentException("AWS " +
                                         "Access Key ID and Secret Access " +
                                         "Key must be specified as the " +
                                         "username or password " +
                                         "(respectively) of a " + scheme +
                                         " URL, or by setting the " +
                                         accessKeyProperty + " or " +
                                         secretAccessKeyProperty +
                                         " properties (respectively).");
    } else if (accessKey == null) {
      throw new IllegalArgumentException("AWS " +
                                         "Access Key ID must be specified " +
                                         "as the username of a " + scheme +
                                         " URL, or by setting the " +
                                         accessKeyProperty + " property.");
    } else if (secretAccessKey == null) {
      throw new IllegalArgumentException("AWS " +
                                         "Secret Access Key must be " +
                                         "specified as the password of a " +
                                         scheme + " URL, or by setting the " +
                                         secretAccessKeyProperty +
                                         " property.");       
    }

  }
  
  public String getAccessKey() {
    return accessKey;
  }
  
  public String getSecretAccessKey() {
    return secretAccessKey;
  }
}
