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
package org.apache.james.util;

import javax.mail.internet.MimeUtility;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;


/**
 * Simple Base64 string decoding function
 *
 * @version This is $Revision: 1.5.4.3 $
 */

public class Base64 {

    public static BufferedReader decode(String b64string) throws Exception {
        return new BufferedReader(
                   new InputStreamReader(
                       MimeUtility.decode(
                            new ByteArrayInputStream(
                                b64string.getBytes()), "base64")));
    }

    public static String decodeAsString(String b64string) throws Exception {
        if (b64string == null) {
            return b64string;
        }
        String returnString = decode(b64string).readLine();
        if (returnString == null) {
            return returnString;
        }
        return returnString.trim();
    }

    public static ByteArrayOutputStream encode(String plaintext)
            throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] in = plaintext.getBytes();
        ByteArrayOutputStream inStream = new ByteArrayOutputStream();
        inStream.write(in, 0, in.length);
        // pad
        if ((in.length % 3 ) == 1){
            inStream.write(0);
            inStream.write(0);
        } else if((in.length % 3 ) == 2){
            inStream.write(0);
        }
        inStream.writeTo( MimeUtility.encode(out, "base64")  );
        return out;
    }

    public static String encodeAsString(String plaintext) throws Exception {
        return  encode(plaintext).toString();
    }


}
