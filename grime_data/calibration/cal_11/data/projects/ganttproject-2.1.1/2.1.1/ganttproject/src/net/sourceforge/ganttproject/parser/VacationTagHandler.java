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

import net.sourceforge.ganttproject.GanttCalendar;
import net.sourceforge.ganttproject.calendar.GanttDaysOff;
import net.sourceforge.ganttproject.resource.HumanResource;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.resource.ResourceManager;

import org.xml.sax.Attributes;

/**
 * @author nbohn
 */
public class VacationTagHandler implements TagHandler, ParsingListener {
    private ResourceManager myResourceManager;

    public VacationTagHandler(ResourceManager resourceManager) {
        myResourceManager = (HumanResourceManager) resourceManager;
    }

    public void startElement(String namespaceURI, String sName, String qName,
            Attributes attrs) {

        if (qName.equals("vacation")) {
            loadResource(attrs);
        }

    }

    private void loadResource(Attributes atts) {
        try {
            // <vacation start="2005-04-14" end="2005-04-14" resourceid="0"/>
            // GanttCalendar.parseXMLDate(attrs.getValue(i)).getTime()

            String startAsString = atts.getValue("start");
            String endAsString = atts.getValue("end");
            String resourceIdAsString = atts.getValue("resourceid");
            HumanResource hr;
            hr = (HumanResource) myResourceManager.getById(Integer
                    .parseInt(resourceIdAsString));
            hr.addDaysOff(new GanttDaysOff(GanttCalendar
                    .parseXMLDate(startAsString), GanttCalendar
                    .parseXMLDate(endAsString)));
        } catch (NumberFormatException e) {
            System.out
                    .println("ERROR in parsing XML File year is not numeric: "
                            + e.toString());
            return;
        }
    }

    public void endElement(String namespaceURI, String sName, String qName) {
        // TODO Auto-generated method stub

    }

    public void parsingStarted() {
        // TODO Auto-generated method stub

    }

    public void parsingFinished() {
        // TODO Auto-generated method stub

    }

}
