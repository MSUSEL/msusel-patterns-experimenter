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

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;

public class ICC_Profile {
    protected byte[] data;
    protected int numComponents;
    private static HashMap<String, Integer> cstags = new HashMap<String, Integer>();

    protected ICC_Profile() {
    }

    public static ICC_Profile getInstance(byte[] data) {
        try {
            if (data.length < 128 || data[36] != 0x61 || data[37] != 0x63
                || data[38] != 0x73 || data[39] != 0x70)
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile"));
            ICC_Profile icc = new ICC_Profile();
            icc.data = data;
            Integer cs = cstags.get(new String(data, 16, 4, "US-ASCII"));
            icc.numComponents = cs == null ? 0 : cs.intValue();
            return icc;
        }
        catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
    }

    public static ICC_Profile getInstance(InputStream file) {
        try {
            byte[] head = new byte[128];
            int remain = head.length;
            int ptr = 0;
            while (remain > 0) {
                int n = file.read(head, ptr, remain);
                if (n < 0)
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile"));
                remain -= n;
                ptr += n;
            }
            if (head[36] != 0x61 || head[37] != 0x63
                || head[38] != 0x73 || head[39] != 0x70)
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile"));
            remain = (head[0] & 0xff) << 24 | (head[1] & 0xff) << 16
                      | (head[2] & 0xff) <<  8 | head[3] & 0xff;
            byte[] icc = new byte[remain];
            System.arraycopy(head, 0, icc, 0, head.length);
            remain -= head.length;
            ptr = head.length;
            while (remain > 0) {
                int n = file.read(icc, ptr, remain);
                if (n < 0)
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile"));
                remain -= n;
                ptr += n;
            }
            return getInstance(icc);
        }
        catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
    }

    public static ICC_Profile GetInstance(String fname) {
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(fname);
            ICC_Profile icc = getInstance(fs);
            return icc;
        }
        catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
        finally {
            try{fs.close();}catch(Exception x){}
        }
    }

    public byte[] getData() {
        return data;
    }

    public int getNumComponents() {
        return numComponents;
    }

    static {
        cstags.put("XYZ ", new Integer(3));
        cstags.put("Lab ", new Integer(3));
        cstags.put("Luv ", new Integer(3));
        cstags.put("YCbr", new Integer(3));
        cstags.put("Yxy ", new Integer(3));
        cstags.put("RGB ", new Integer(3));
        cstags.put("GRAY", new Integer(1));
        cstags.put("HSV ", new Integer(3));
        cstags.put("HLS ", new Integer(3));
        cstags.put("CMYK", new Integer(4));
        cstags.put("CMY ", new Integer(3));
        cstags.put("2CLR", new Integer(2));
        cstags.put("3CLR", new Integer(3));
        cstags.put("4CLR", new Integer(4));
        cstags.put("5CLR", new Integer(5));
        cstags.put("6CLR", new Integer(6));
        cstags.put("7CLR", new Integer(7));
        cstags.put("8CLR", new Integer(8));
        cstags.put("9CLR", new Integer(9));
        cstags.put("ACLR", new Integer(10));
        cstags.put("BCLR", new Integer(11));
        cstags.put("CCLR", new Integer(12));
        cstags.put("DCLR", new Integer(13));
        cstags.put("ECLR", new Integer(14));
        cstags.put("FCLR", new Integer(15));
    }
}
