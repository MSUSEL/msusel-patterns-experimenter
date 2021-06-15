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
package com.itextpdf.text.pdf;

import java.io.IOException;
import java.util.HashMap;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.error_messages.MessageLocalization;
/** Enumerates all the fonts inside a True Type Collection.
 *
 * @author  Paulo Soares
 */
class EnumerateTTC extends TrueTypeFont{

    protected String[] names;

    EnumerateTTC(String ttcFile) throws DocumentException, IOException {
        fileName = ttcFile;
        rf = new RandomAccessFileOrArray(ttcFile);
        findNames();
    }

    EnumerateTTC(byte ttcArray[]) throws DocumentException, IOException {
        fileName = "Byte array TTC";
        rf = new RandomAccessFileOrArray(ttcArray);
        findNames();
    }

    void findNames() throws DocumentException, IOException {
        tables = new HashMap<String, int[]>();

        try {
            String mainTag = readStandardString(4);
            if (!mainTag.equals("ttcf"))
                throw new DocumentException(MessageLocalization.getComposedMessage("1.is.not.a.valid.ttc.file", fileName));
            rf.skipBytes(4);
            int dirCount = rf.readInt();
            names = new String[dirCount];
            int dirPos = rf.getFilePointer();
            for (int dirIdx = 0; dirIdx < dirCount; ++dirIdx) {
                tables.clear();
                rf.seek(dirPos);
                rf.skipBytes(dirIdx * 4);
                directoryOffset = rf.readInt();
                rf.seek(directoryOffset);
                if (rf.readInt() != 0x00010000)
                    throw new DocumentException(MessageLocalization.getComposedMessage("1.is.not.a.valid.ttf.file", fileName));
                int num_tables = rf.readUnsignedShort();
                rf.skipBytes(6);
                for (int k = 0; k < num_tables; ++k) {
                    String tag = readStandardString(4);
                    rf.skipBytes(4);
                    int table_location[] = new int[2];
                    table_location[0] = rf.readInt();
                    table_location[1] = rf.readInt();
                    tables.put(tag, table_location);
                }
                names[dirIdx] = getBaseFont();
            }
        }
        finally {
            if (rf != null)
                rf.close();
        }
    }

    String[] getNames() {
        return names;
    }

}
