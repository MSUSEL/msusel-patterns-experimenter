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
package org.geotools.kml.bindings;

import javax.xml.namespace.QName;

import org.geotools.kml.Folder;
import org.geotools.kml.FolderStack;
import org.geotools.kml.v22.KML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.Binding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

public class NameBinding extends AbstractComplexBinding {

    private final FolderStack folderStack;

    private final static String FOLDER = KML.Folder.getLocalPart();

    public NameBinding(FolderStack folderStack) {
        this.folderStack = folderStack;
    }

    @Override
    public QName getTarget() {
        return KML.name;
    }

    @Override
    public int getExecutionMode() {
        return Binding.OVERRIDE;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class getType() {
        return String.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Node parent = node.getParent();
        if (parent != null) {
            String parentElementName = parent.getComponent().getName();
            if (FOLDER.equals(parentElementName)) {
                Folder folder = folderStack.peek();
                if (folder != null) {
                    folder.setName(value.toString());
                }
            }
        }
        return super.parse(instance, node, value);
    }
}
