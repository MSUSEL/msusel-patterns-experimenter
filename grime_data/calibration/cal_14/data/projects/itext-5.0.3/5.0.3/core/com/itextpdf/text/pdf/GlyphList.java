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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.itextpdf.text.pdf.fonts.FontsResourceAnchor;

public class GlyphList {
    private static HashMap<Integer, String> unicode2names = new HashMap<Integer, String>();
    private static HashMap<String, int[]> names2unicode = new HashMap<String, int[]>();

    static {
        InputStream is = null;
        try {
            is = BaseFont.getResourceStream(BaseFont.RESOURCE_PATH + "glyphlist.txt", new FontsResourceAnchor().getClass().getClassLoader());
            if (is == null) {
                String msg = "glyphlist.txt not found as resource. (It must exist as resource in the package com.itextpdf.text.pdf.fonts)";
                throw new Exception(msg);
            }
            byte buf[] = new byte[1024];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while (true) {
                int size = is.read(buf);
                if (size < 0)
                    break;
                out.write(buf, 0, size);
            }
            is.close();
            is = null;
            String s = PdfEncodings.convertToString(out.toByteArray(), null);
            StringTokenizer tk = new StringTokenizer(s, "\r\n");
            while (tk.hasMoreTokens()) {
                String line = tk.nextToken();
                if (line.startsWith("#"))
                    continue;
                StringTokenizer t2 = new StringTokenizer(line, " ;\r\n\t\f");
                String name = null;
                String hex = null;
                if (!t2.hasMoreTokens())
                    continue;
                name = t2.nextToken();
                if (!t2.hasMoreTokens())
                    continue;
                hex = t2.nextToken();
                Integer num = Integer.valueOf(hex, 16);
                unicode2names.put(num, name);
                names2unicode.put(name, new int[]{num.intValue()});
            }
        }
        catch (Exception e) {
            System.err.println("glyphlist.txt loading error: " + e.getMessage());
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (Exception e) {
                    // empty on purpose
                }
            }
        }
    }

    public static int[] nameToUnicode(String name) {
        return names2unicode.get(name);
    }

    public static String unicodeToName(int num) {
        return unicode2names.get(new Integer(num));
    }
}