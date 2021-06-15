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
/*
 * Created on 29.09.2003
 *
 */
package net.sourceforge.ganttproject.document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * List of Documents MRU (<b>m</b>ost <b>r</b>ecently <b>u</b>sed)
 * 
 * @author Michael Haeusler (michael at akatose.de)
 */
public class DocumentsMRU {

    private int maxSize;

    private List documents;

    public DocumentsMRU(int maxSize) {
        if (maxSize <= 0)
            throw new IllegalArgumentException(
                    "maxSize must be larger than zero (" + maxSize + ")");
        this.maxSize = maxSize;
        documents = new ArrayList(maxSize);
    }

    /**
     * Adds a Document at the top of the list of Documents MRU. If the list is
     * already full, an old entry is removed, to make place for this new
     * document.
     * 
     * @param document
     *            the Document that should be added
     * @return wether the list has changed through the addition
     */
    public boolean add(Document document) {
        // if the document is invalid, we don't add it
        if (!document.isValidForMRU())
            return false;

        int i = documents.indexOf(document);
        // if the document is already on the top,
        // nothing needs to change.
        if (0 == i)
            return false;

        if (0 < i) {
            documents.remove(i);
        } else {
            while (documents.size() >= maxSize)
                documents.remove(maxSize - 1);
        }
        documents.add(0, document);

        return true;
    }

    /**
     * Appends a Document to the list of Documents MRU. If the list is already
     * full, the document will <b>not</b> be appended.
     * 
     * @param document
     *            the Document that should be added
     * @return wether the list has changed through the addition
     */
    public boolean append(Document document) {
        // if the document is invalid, we don't add it
        if (!document.isValidForMRU())
            return false;

        int i = documents.indexOf(document);
        // if the document is already at the bottom,
        // nothing needs to change.
        if (i == maxSize - 1)
            return false;

        if (i != -1) {
            documents.remove(i);
        }

        if (documents.size() < maxSize) {
            documents.add(document);
            return true;
        } else {
            return false;
        }
    }

    /**
     * clears the list of Documents MRU
     */
    public void clear() {
        documents.clear();
    }

    /**
     * returns an Iterator over the entries of the list of Documents MRU
     */
    public Iterator iterator() {
        return documents.iterator();
    }

}
