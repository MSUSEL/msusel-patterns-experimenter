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
package net.sourceforge.ganttproject.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import net.sourceforge.ganttproject.GanttPreviousState;
import net.sourceforge.ganttproject.GanttPreviousStateTask;

class HistorySaver extends SaverBase {

    void save(List/*<GanttPreviousState*/ history, TransformerHandler handler) throws SAXException, ParserConfigurationException, IOException {
        AttributesImpl attrs = new AttributesImpl();
        startElement("previous", handler);
        for (int i=0; i<history.size(); i++) {
            final GanttPreviousState nextState = (GanttPreviousState) history.get(i);
            final List/*<GanttPreviousStateTask>*/ stateTasks = nextState.load();
            addAttribute("name", nextState.getName(), attrs);
            startElement("previous-tasks", attrs, handler);
            // ArrayList list =
            // ((GanttPreviousState)previous.get(i)).getTasks();
            for (int j=0; j<stateTasks.size(); j++) {
                GanttPreviousStateTask task = (GanttPreviousStateTask)stateTasks.get(j);
                addAttribute("id", String.valueOf(task.getId()), attrs);
                addAttribute("start", task.getStart().toXMLString(), attrs);
                addAttribute("duration", String.valueOf(task.getDuration()), attrs);
                addAttribute("meeting", String.valueOf(task.isMilestone()), attrs);
                addAttribute("super", String.valueOf(task.hasNested()), attrs);
                emptyElement("previous-task", attrs, handler);

            }
            endElement("previous-tasks", handler);
        }
        endElement("previous", handler);
        
    }

}
