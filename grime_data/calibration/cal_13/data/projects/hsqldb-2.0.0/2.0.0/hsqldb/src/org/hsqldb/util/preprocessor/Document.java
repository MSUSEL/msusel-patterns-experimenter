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

package org.hsqldb.util.preprocessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Vector;

/* $Id: Document.java 610 2008-12-22 15:54:18Z unsaved $ */

/**
 * Simple line-oriented text document ADT.
 *
 * @author boucherb@users
 * @version 1.8.1
 * @since 1.8.1
 */
class Document {
    Vector lines = new Vector();

    Document() {}

    Document(Document source) {
        this.appendDocument(source);
    }

    Document addSouceLine(String line) {
        if (line == null) {
            throw new IllegalArgumentException("line: null");
        }

        this.lines.addElement(line);

        return this;
    }

    Document appendDocument(Document doc) {
        if (doc != null) {
            int    count = doc.size();
            Vector src   = doc.lines;
            Vector dst   = this.lines;

            for (int i = 0; i < count; i++) {
                dst.addElement(src.elementAt(i));
            }
        }

        return this;
    }

    Document clear() {
        this.lines.removeAllElements();

        return this;
    }

    boolean contains(String pattern) {
        Vector lines = this.lines;
        int    size  = lines.size();

        for (int i = 0; i < size; i++) {
            if (((String)lines.elementAt(i)).indexOf(pattern) >= 0) {
                return true;
            }
        }

        return false;
    }

    Document deleteSourceLine(int index) {
        this.lines.removeElementAt(index);

        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof Document) {
            Document other = (Document) o;

            Vector v1 = this.lines;
            Vector v2 = other.lines;

            if (v1.size() != v2.size()) {
                return false;
            }

            for (int i = v1.size() - 1; i >= 0; i--) {
                if (v1.elementAt(i).equals(v2.elementAt(i))) {
                    continue;
                } else {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    String getSourceLine(int index) {
        return (String) this.lines.elementAt(index);
    }

    Document insertSourceLine(int index, String line) {
        if (line == null) {
            throw new IllegalArgumentException("line: null");
        }

        this.lines.insertElementAt(line, index);

        return this;
    }

    Document replaceWith(Document source) {
        return this.clear().appendDocument(source);
    }

    Document setSourceLine(int index, String line) {
        if (line == null) {
            throw new IllegalArgumentException("null");
        }

        this.lines.setElementAt(line, index);

        return this;
    }

    int size() {
        return this.lines.size();
    }

// ------------------------ I/O convenience methods ----------------------------

    Document load(Object source, String encoding) throws IOException,
            UnsupportedEncodingException{
        BufferedReader reader = null;
        boolean        close  = false;

        if (source instanceof InputStream) {
            InputStream       is  = (InputStream) source;
            InputStreamReader isr = isEncoding(encoding)
                    ? new InputStreamReader(is, encoding)
                    : new InputStreamReader(is);

            reader = new BufferedReader(isr);
        } else if (source instanceof File) {
            InputStream       is  = new FileInputStream((File) source);
            InputStreamReader isr = isEncoding(encoding)
                    ? new InputStreamReader(is, encoding)
                    : new InputStreamReader(is);

            close  = true;
            reader = new BufferedReader(isr);
        } else if (source instanceof String) {
            InputStream       is  = new FileInputStream((String) source);
            InputStreamReader isr = isEncoding(encoding)
                    ? new InputStreamReader(is, encoding)
                    : new InputStreamReader(is);

            close  = true;
            reader = new BufferedReader(isr);
        } else if (source instanceof BufferedReader) {
            reader = (BufferedReader) source;
        } else if (source instanceof Reader) {
            reader = new BufferedReader((Reader) source);
        } else {
            throw new IOException("unhandled load source: " + source); // NOI18N
        }

        clear();

        String line;
        Vector lines = this.lines;

        try {
            while(null != (line = reader.readLine())) {
                lines.addElement(line);
            }
        } finally {
            if (close) {
                try {
                    reader.close();
                } catch (IOException ex) {}
            }
        }

        return this;
    }

    Document save(Object target, String encoding) throws IOException {
        BufferedWriter writer = null;
        boolean        close  = false;

        if (target instanceof OutputStream) {
            OutputStream       os  = (OutputStream) target;
            OutputStreamWriter osr = isEncoding(encoding)
                    ? new OutputStreamWriter(os, encoding)
                    : new OutputStreamWriter(os);

            writer = new BufferedWriter(osr);
        } else if (target instanceof File) {
            OutputStream       os  = new FileOutputStream((File) target);
            OutputStreamWriter osr = isEncoding(encoding)
                    ? new OutputStreamWriter(os, encoding)
                    : new OutputStreamWriter(os);

            close  = true;
            writer = new BufferedWriter(osr);
        } else if (target instanceof String) {
            OutputStream       os  = new FileOutputStream((String) target);
            OutputStreamWriter osr = isEncoding(encoding)
                    ? new OutputStreamWriter(os, encoding)
                    : new OutputStreamWriter(os);

            close  = true;
            writer = new BufferedWriter(osr);
        } else if (target instanceof BufferedWriter) {
            writer = (BufferedWriter) target;
        } else if (target instanceof Writer) {
            writer = new BufferedWriter(writer);
        } else {
            throw new IOException("unhandled save target: " + target); // NOI18N
        }

        Vector lines = this.lines;
        int    count = lines.size();

        try {
            for (int i = 0; i < count; i++) {
                writer.write((String)lines.elementAt(i));
                writer.newLine();
            }

            writer.flush();
        } finally {
            if (close) {
                try {
                    writer.close();
                } catch (IOException ex) {}
            }
        }

        return this;
    }

    static boolean isEncoding(String enc) {
        return enc != null && enc.trim().length() > 0;
    }
}
