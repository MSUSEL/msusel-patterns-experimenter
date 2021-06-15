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
 * Created on Mar 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.ganttproject.parser;

import net.sourceforge.ganttproject.CustomPropertyDefinition;
import net.sourceforge.ganttproject.CustomPropertyManager;
import net.sourceforge.ganttproject.task.CustomColumn;
import net.sourceforge.ganttproject.task.CustomColumnsStorage;

import org.xml.sax.Attributes;

public class TaskPropertiesTagHandler implements TagHandler, ParsingListener {
	private final CustomColumnsStorage myColumnStorage;

	public TaskPropertiesTagHandler(CustomColumnsStorage columnStorage) {
    	myColumnStorage = columnStorage;
    }

    public void startElement(String namespaceURI, String sName, String qName,
            Attributes attrs) {
        if (qName.equals("taskproperty"))
            loadTaskProperty(attrs);
    }

    public void endElement(String namespaceURI, String sName, String qName) {
    }

    private void loadTaskProperty(Attributes atts) {
        String name = atts.getValue("name");
        String id = atts.getValue("id");
        String type = atts.getValue("valuetype");

        if (atts.getValue("type").equals("custom")) {
            CustomColumn cc;
            String valueStr = atts.getValue("defaultvalue");
	            CustomPropertyDefinition stubDefinition = CustomPropertyManager.PropertyTypeEncoder.decodeTypeAndDefaultValue(type, valueStr); 
            cc = new CustomColumn(name, stubDefinition.getType(), stubDefinition.getDefaultValue());
            cc.setId(id);

          	myColumnStorage.addCustomColumn(cc);
        }
    }

    public void parsingStarted() {
    }

    public void parsingFinished() {
    }
}
