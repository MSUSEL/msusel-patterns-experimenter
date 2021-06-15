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
package org.archive.net.s3;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

/**
 * A protocol handler for an s3 scheme. Takes URLs of the form:
 * <code>s3://aws.access.key.id:aws.access.key.secret@BUCKET/PATH</code> (Same
 * as in hadoop).
 * 
 * @author stack
 */
public class Handler extends URLStreamHandler {
    protected URLConnection openConnection(URL u)
    throws IOException {
        // This looking for accessKey id and accessKey secret code is based
        // on code from hadoop S3.
        String accessKey = null;
        String secretAccessKey = null;
        String userInfo = u.getUserInfo();
        if (userInfo != null) {
            int index = userInfo.indexOf(':');
            if (index != -1) {
              accessKey = userInfo.substring(0, index);
              secretAccessKey = userInfo.substring(index + 1);
            } else {
              accessKey = userInfo;
            }
        }
        if (accessKey == null) {
          accessKey = System.getProperty("aws.access.key.id");
        }
        if (secretAccessKey == null) {
          secretAccessKey = System.getProperty("aws.access.key.secret");
        }
        if (accessKey == null && secretAccessKey == null) {
          throw new IllegalArgumentException("AWS " +
                "Access Key ID and Secret Access Key " +
                "must be specified as the username " +
                "or password (respectively) of a s3 URL, " +
                "or by setting the " +
                "aws.access.key.id or " +                
                "aws.access.key.secret properties (respectively).");
        } else if (accessKey == null) {
          throw new IllegalArgumentException("AWS " +
                "Access Key ID must be specified " +
                "as the username of a s3 URL, or by setting the " +
                "aws.access.key.id property.");
        } else if (secretAccessKey == null) {
          throw new IllegalArgumentException("AWS " +
                "Secret Access Key must be specified " +
                "as the password of a s3 URL, or by setting the " +
                "aws.access.key.secret property.");        
        }
        
        RestS3Service s3Service;
        try {
            s3Service = new RestS3Service(
                new AWSCredentials(accessKey, secretAccessKey));
        } catch (S3ServiceException e) {
            e.printStackTrace();
            throw new IOException(e.toString());
        }
        InputStream is = null;
        try {
            // This opens the stream to the bucket/key object.
            S3Object s3obj = s3Service.getObject(new S3Bucket(u.getHost()),
                u.getPath().substring(1) /* Skip starting '/' character */);
            is = s3obj.getDataInputStream();
        } catch (S3ServiceException e) {
            e.printStackTrace();
            throw new IOException(e.toString());
        }

        final InputStream inputStream = is;
        return new URLConnection(u) {
            private InputStream is = inputStream;
            @Override
            public InputStream getInputStream() throws IOException {
                return this.is;
            }
            @Override
            public void connect() throws IOException {
                // Nothing to do. When we give back this object, we're
                // connected.
            }
        };
    }

    /**
     * Main dumps rsync file to STDOUT.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args)
    throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java " +
                "org.archive.net.s3.Handler " +
                "s3://AWS_ACCESS_KEY_ID:AWS_ACCESS_KEY_SECRET@BUCKET/KEY");
            System.exit(1);
        }
        URL u = new URL(args[0]);
        URLConnection connect = u.openConnection();
        // Write download to stdout.
        final int bufferlength = 4096;
        byte [] buffer = new byte [bufferlength];
        InputStream is = connect.getInputStream();
        try {
            for (int count = -1;
                    (count = is.read(buffer, 0, bufferlength)) != -1;) {
                System.out.write(buffer, 0, count);
            }
            System.out.flush();
        } finally {
            is.close();
        }
    }
}