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
package org.archive.queue;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.SequenceInputStream;

import org.archive.util.ArchiveUtils;
import org.archive.util.Reporter;

/**
 * Command-line tool that displays serialized object streams in a
 * line-oriented format.
 *
 * @author gojomo
 */
public class QueueCat {
    public static void main(String[] args) 
    throws IOException, ClassNotFoundException {
        InputStream inStream;
        if (args.length == 0) {
            inStream = System.in;
        } else {
            inStream = new FileInputStream(args[0]);
        }

        // Need to handle the case where the stream lacks the usual
        // objectstream prefix
        byte[] serialStart = { (byte)0xac, (byte)0xed, (byte)0x00, (byte)0x05 };
        byte[] actualStart = new byte[4];
        byte[] pseudoStart;
        inStream.read(actualStart);
        if (ArchiveUtils.byteArrayEquals(serialStart,actualStart)) {
            pseudoStart = serialStart;
        } else {
            // Have to fake serialStart and original 4 bytes
            pseudoStart = new byte[8];
            System.arraycopy(serialStart,0,pseudoStart,0,4);
            System.arraycopy(actualStart,0,pseudoStart,4,4);
        }
        inStream = new SequenceInputStream(
            new ByteArrayInputStream(pseudoStart),
            inStream);

        ObjectInputStream oin = new ObjectInputStream(inStream);

        Object o;
        while(true) {
            try {
                o=oin.readObject();
            } catch (EOFException e) {
                return;
            }
            if(o instanceof Reporter) {
                System.out.println(((Reporter)o).singleLineReport());
            } else {
                // TODO: flatten multiple-line strings!
                System.out.println(o.toString());
            }
        }
    }
}
