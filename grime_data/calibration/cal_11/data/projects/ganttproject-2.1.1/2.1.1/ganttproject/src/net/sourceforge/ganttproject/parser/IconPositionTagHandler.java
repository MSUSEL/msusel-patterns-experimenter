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
/**
 * 
 */
package net.sourceforge.ganttproject.parser;

import org.xml.sax.Attributes;

/**
 * @author nbohn
 */
public class IconPositionTagHandler implements TagHandler {
    private int[] myList;

    private String myPositions = "";

    private int[] myDeletedList;

    private String myDeletedPositions = "";

    public IconPositionTagHandler() {
    }

    public void startElement(String namespaceURI, String sName, String qName,
            Attributes attrs) throws FileFormatException {
        if (qName.equals("positions")) {
            loadIcon(attrs);
            loadDeletedIcon(attrs);
        }
    }

    public void endElement(String namespaceURI, String sName, String qName) {
    }

    private void loadIcon(Attributes atts) {
        myPositions = atts.getValue("icons-list");
        if (!myPositions.equals("")) {
            String[] positions = myPositions.split(",");
            myList = new int[positions.length];
            for (int i = 0; i < positions.length; i++)
                myList[i] = (new Integer(positions[i])).intValue();
        }
    }

    private void loadDeletedIcon(Attributes atts) {
        myDeletedPositions = atts.getValue("deletedIcons-list");
        if ((myDeletedPositions != null) && (!myDeletedPositions.equals(""))) {
            String[] positions = myDeletedPositions.split(",");
            myDeletedList = new int[positions.length];
            for (int i = 0; i < positions.length; i++)
                myDeletedList[i] = (new Integer(positions[i])).intValue();
        }
    }

    public int[] getList() {
        return myList;
    }

    public String getPositions() {
        return myPositions;
    }

    public int[] getDeletedList() {
        return myDeletedList;
    }

    public String getDeletedPositions() {
        return myDeletedPositions;
    }
}
