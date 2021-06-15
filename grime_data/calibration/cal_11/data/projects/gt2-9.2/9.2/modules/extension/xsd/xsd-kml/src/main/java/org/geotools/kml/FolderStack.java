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
package org.geotools.kml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FolderStack implements Iterable<Folder> {

    private final List<Folder> stack;

    public FolderStack() {
        stack = new ArrayList<Folder>();
    }

    public void push(Folder folder) {
        stack.add(folder);
    }

    private boolean elementsExist() {
        return stack.size() > 0;
    }

    public Folder pop() {
        return elementsExist() ? stack.remove(lastElementIndex()) : null;
    }

    private int lastElementIndex() {
        return stack.size() - 1;
    }

    public Folder peek() {
        return elementsExist() ? stack.get(lastElementIndex()) : null;
    }

    @Override
    public Iterator<Folder> iterator() {
        return stack.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Folder folder : this) {
            String name = folder.getName();
            if (name != null) {
                String trimmedName = name.trim();
                if (trimmedName.length() > 0) {
                    sb.append(sb.length() > 0 ? " -> " + trimmedName : trimmedName);
                }
            }
        }
        return sb.toString();
    }

    public List<Folder> asList() {
        return new ArrayList<Folder>(stack);
    }

}
