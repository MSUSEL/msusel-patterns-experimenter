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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.ganttproject.GPLogger;
import net.sourceforge.ganttproject.GanttCalendar;
import net.sourceforge.ganttproject.GanttPreviousState;
import net.sourceforge.ganttproject.GanttPreviousStateTask;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author nbohn
 * 
 */
public class PreviousStateTasksTagHandler extends DefaultHandler implements
        TagHandler {
    private String myName = "";

    private GanttPreviousState previousState;

    private String s = "    "; // the marge

    private final List myPreviousStates;

    private ArrayList tasks = new ArrayList();

    public PreviousStateTasksTagHandler(List previousStates) {
        myPreviousStates = previousStates;
    }

    public void startElement(String namespaceURI, String sName, String qName,
            Attributes attrs) {
        if (qName.equals("previous-tasks")) {
            setName(attrs.getValue("name"));
            tasks = new ArrayList();
            if (myPreviousStates != null) {
                try {
                    previousState = new GanttPreviousState(myName);
                    myPreviousStates.add(previousState);
                } catch (IOException e) {
                	if (!GPLogger.log(e)) {
                		e.printStackTrace(System.err);
                	}
                }
            }
        } else if ((qName.equals("previous-task"))) {// && (myPreviousStates
            // != null)) {
            writePreviousTask(attrs);
        }
    }

    public void endElement(String namespaceURI, String sName, String qName) {
        if ((qName.equals("previous-tasks")) && (myPreviousStates != null)) {
            previousState.saveFilesFromLoaded(tasks);
        }
    }

    private void setName(String name) {
        myName = name;
    }

    private void writePreviousTask(Attributes attrs) {

        String id = attrs.getValue("id");

        String meetingAsString = attrs.getValue("meeting");

        boolean meeting = false;

        if (meetingAsString.equals("true"))
            meeting = true;

        String start = attrs.getValue("start");

        String duration = attrs.getValue("duration");

        String nestedAsString = attrs.getValue("super");

        boolean nested = false;

        if (nestedAsString.equals("true"))
            nested = true;

        GanttPreviousStateTask task = new GanttPreviousStateTask(
                new Integer(id).intValue(), GanttCalendar.parseXMLDate(start),
                new Integer(duration).intValue(), meeting, nested);
        tasks.add(task);
    }

    public String getName() {
        return myName;
    }

    public ArrayList getTasks() {
        return tasks;
    }
}
