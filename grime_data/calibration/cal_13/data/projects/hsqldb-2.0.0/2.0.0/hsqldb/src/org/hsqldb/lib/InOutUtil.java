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

package org.hsqldb.lib;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Input / Output utility
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.8.0
 * @since 1.7.2
 */
public class InOutUtil {

    /**
     * Implementation only supports unix line-end format and is suitable for
     * processing HTTP and other network protocol communications. Reads and writes
     * a line of data. Returns the number of bytes read/written.
     */
    public static int readLine(InputStream in,
                               OutputStream out) throws IOException {

        int count = 0;

        for (;;) {
            int b = in.read();

            if (b == -1) {
                break;
            }

            count++;

            out.write(b);

            if (b == '\n') {
                break;
            }
        }

        return count;
    }

    /**
     * Retrieves the serialized form of the specified <CODE>Object</CODE>
     * as an array of bytes.
     *
     * @param s the Object to serialize
     * @return  a static byte array representing the passed Object
     */
    public static byte[] serialize(Serializable s) throws IOException {

        HsqlByteArrayOutputStream bo = new HsqlByteArrayOutputStream();
        ObjectOutputStream        os = new ObjectOutputStream(bo);

        os.writeObject(s);

        return bo.toByteArray();
    }

    /**
     * Deserializes the specified byte array to an
     * <CODE>Object</CODE> instance.
     *
     * @return the Object resulting from deserializing the specified array of bytes
     * @param ba the byte array to deserialize to an Object
     */
    public static Serializable deserialize(byte[] ba)
    throws IOException, ClassNotFoundException {

        HsqlByteArrayInputStream bi = new HsqlByteArrayInputStream(ba);
        ObjectInputStream        is = new ObjectInputStream(bi);

        return (Serializable) is.readObject();
    }
}
