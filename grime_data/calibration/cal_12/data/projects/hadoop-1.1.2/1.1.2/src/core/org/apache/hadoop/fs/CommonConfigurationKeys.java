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
package org.apache.hadoop.fs;

/** 
 * This class contains constants for configuration keys used
 * in the common code.
 *
 */

public class CommonConfigurationKeys {
  
  /** See src/core/core-default.xml */
  public static final String  FS_DEFAULT_NAME_KEY = "fs.default.name";
  public static final String  FS_DEFAULT_NAME_DEFAULT = "file:///";

  /** See src/core/core-default.xml */
  public static final String  HADOOP_SECURITY_GROUP_MAPPING =
    "hadoop.security.group.mapping";
  /** See src/core/core-default.xml */
  public static final String  HADOOP_SECURITY_AUTHENTICATION =
    "hadoop.security.authentication";
  /** See src/core/core-default.xml */
  public static final String HADOOP_SECURITY_AUTHORIZATION =
    "hadoop.security.authorization";
  /** See src/core/core-default.xml */
  public static final String  HADOOP_SECURITY_SERVICE_USER_NAME_KEY = 
    "hadoop.security.service.user.name.key";
  /** See src/core/core-default.xml */
  public static final String HADOOP_SECURITY_TOKEN_SERVICE_USE_IP =
    "hadoop.security.token.service.use_ip";
  public static final boolean HADOOP_SECURITY_TOKEN_SERVICE_USE_IP_DEFAULT =
      true;
  public static final String HADOOP_SECURITY_USE_WEAK_HTTP_CRYPTO_KEY =
      "hadoop.security.use-weak-http-crypto";
  public static final boolean HADOOP_SECURITY_USE_WEAK_HTTP_CRYPTO_DEFAULT =
      false;
  
  public static final String IPC_SERVER_RPC_READ_THREADS_KEY =
                                        "ipc.server.read.threadpool.size";
  public static final int IPC_SERVER_RPC_READ_THREADS_DEFAULT = 1;

  public static final String  IO_NATIVE_LIB_AVAILABLE_KEY =
      "hadoop.native.lib";
  /** Default value for IO_NATIVE_LIB_AVAILABLE_KEY */
  public static final boolean IO_NATIVE_LIB_AVAILABLE_DEFAULT = true;

  /** Internal buffer size for Snappy compressor/decompressors */
  public static final String IO_COMPRESSION_CODEC_SNAPPY_BUFFERSIZE_KEY =
      "io.compression.codec.snappy.buffersize";

  /** Default value for IO_COMPRESSION_CODEC_SNAPPY_BUFFERSIZE_KEY */
  public static final int IO_COMPRESSION_CODEC_SNAPPY_BUFFERSIZE_DEFAULT =
      256 * 1024;

  /** See src/core/core-default.xml */
  public static final String HADOOP_RELAXED_VERSION_CHECK_KEY =
      "hadoop.relaxed.worker.version.check";
  public static final boolean HADOOP_RELAXED_VERSION_CHECK_DEFAULT = false;
}

