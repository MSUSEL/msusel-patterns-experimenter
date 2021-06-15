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
package net.sourceforge.ganttproject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import net.sourceforge.ganttproject.document.DocumentManager;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.parser.ParserFactory;
import net.sourceforge.ganttproject.parser.PreviousStateTasksTagHandler;
import net.sourceforge.ganttproject.task.Task;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author nbohn
 * 
 */
public class GanttPreviousState {
    private final String myName;

    private DocumentManager myManager;

    private ParserFactory myFactory;

    private File myFile;

    private OutputStreamWriter os;

    private GanttTree2 myTree;

    private GanttProject myProject;

    private GanttLanguage lang = GanttLanguage.getInstance();

    private String s = "    "; // the marge

    // constructor for a new previous state
    public GanttPreviousState(String name, GanttProject project)
            throws IOException {
        myName = name;
        myTree = (GanttTree2) project.getTree();
        myProject = project;
        myFile = createTemporaryFile();
        myFile.deleteOnExit();
        os = new OutputStreamWriter(new FileOutputStream(myFile), "UTF-8");

        saveFile();
    }

    // constructor for a loaded previous state
    public GanttPreviousState(String name) throws IOException {
        myName = name;
        myFile = createTemporaryFile();
        myFile.deleteOnExit();
        os = new OutputStreamWriter(new FileOutputStream(myFile), "UTF-8");
    }

    public void saveFile() {
        try {
            AttributesImpl attrs = new AttributesImpl();
            StreamResult result = new StreamResult(os);
            SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory
                    .newInstance();
            TransformerHandler handler = factory.newTransformerHandler();
            Transformer serializer = handler.getTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");
            handler.setResult(result);
            handler.startDocument();

            handler.endDocument();
            writeTasks();
            os.close();
        } catch (Throwable e) {
        	if (!GPLogger.log(e)) {
        		e.printStackTrace(System.err);
        	}
        }
    }

    private void writeTasks() {
        Enumeration children = ((DefaultMutableTreeNode) myTree.getJTree()
                .getModel().getRoot()).children();
        write(s + "<previous-tasks name=\"" + myName + "\">\n");

        while (children.hasMoreElements()) {
            DefaultMutableTreeNode element = (DefaultMutableTreeNode) children
                    .nextElement();
            writeTask(os, /* lot.indexOf(element) */element);
        }
        write(s + "</previous-tasks>");
    }

    public void saveFilesFromLoaded(ArrayList tasks) {
        try {
            AttributesImpl attrs = new AttributesImpl();
            StreamResult result = new StreamResult(os);
            SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory
                    .newInstance();
            TransformerHandler handler = factory.newTransformerHandler();
            Transformer serializer = handler.getTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");
            handler.setResult(result);
            handler.startDocument();

            handler.endDocument();
            writeTasksFromLoaded(tasks);
            os.close();
        } catch (Throwable e) {
        	if (!GPLogger.log(e)) {
        		e.printStackTrace(System.err);
        	}
        }
    }

    public void writeTasksFromLoaded(ArrayList tasks) throws IOException {
        write(s + "<previous-tasks name=\"" + myName + "\">\n");
        for (int i = 0; i < tasks.size(); i++) {
            os.write(s + s + "<previous-task id=\""
                    + ((GanttPreviousStateTask) tasks.get(i)).getId() + "\"");
            os.write(" start=\""
                    + ((GanttPreviousStateTask) tasks.get(i)).getStart()
                            .toXMLString() + "\"");
            os.write(" duration=\""
                    + ((GanttPreviousStateTask) tasks.get(i)).getDuration()
                    + "\"");
            os.write(" meeting=\""
                    + ((GanttPreviousStateTask) tasks.get(i)).isMilestone()
                    + "\"");
            os.write(" super=\""
                    + ((GanttPreviousStateTask) tasks.get(i)).hasNested()
                    + "\"");
            os.write("/>\n");
        }
        write(s + "</previous-tasks>");
    }

    public void write(String s) {
        try {
            os.write(s);
        } catch (IOException e) {
        	if (!GPLogger.log(e)) {
        		e.printStackTrace(System.err);
        	}
        }

    }

    private File createTemporaryFile() throws IOException {
        String fileName = "_GanttProject_ps_" + (int) (10000. * Math.random());
        return File.createTempFile(fileName, ".gan");
    }

    public String getName() {
        return myName;
    }

    public File getFile() {
        return myFile;
    }

    public void remove() {
        myFile.delete();
    }

    /** Simple write information of tasks */
    public void writeTask(Writer fout, DefaultMutableTreeNode node) {
        ArrayList lot = new ArrayList();
        try {
            GanttTask task = (GanttTask) node.getUserObject();

            if (task.getTaskID() == -1)
                throw new RuntimeException(
                        "A task can not has a number equal to -1");

            int id = task.getTaskID();

            /*
             * if (id >= lot.size()) { return; }
             */

            boolean haschild = false;

            ArrayList child = myTree.getAllChildTask(node);
            if (child.size() != 0) {
                haschild = true;

            }

            // Writes data of task
            fout.write(s + s + "<previous-task id=\"" + task.getTaskID() + // lots.indexOf(task.toString())
                    // +
                    // //By
                    // CL
                    "\"");
            fout.write(" start=\"" + task.getStart().toXMLString() + "\"");
            fout.write(" duration=\"" + task.getLength() + "\"");
            fout.write(" meeting=\"" + task.isMilestone() + "\"");
            fout.write(" super=\"" + haschild + "\"");

            fout.write("/>\n");

            // Write the child of the task
            if (haschild) {
                for (int i = 0; i < child.size(); i++) {
                    Task task2 = (Task) ((DefaultMutableTreeNode) child.get(i))
                            .getUserObject();
                    int newid = -1; // lot.lastIndexOf(task2);

                    for (int j = 0; j < lot.size(); j++) {
                        String a = task2.toString();
                        if (a == null)
                            System.out.println("nul");
                        String b = lot.get(j).toString();

                        if (a.equals(b)) {
                            newid = j;
                        }
                    }
                    writeTask(fout, (DefaultMutableTreeNode) child.get(i));
                }

            }

            // end of task section

        } catch (Exception e) {
        	if (!GPLogger.log(e)) {
        		e.printStackTrace(System.err);
        	}
        }
    }

    /** Correct the charcters to be compatible with xml format */
    public String correct(String s) {
        String res;
        if (s != null) {
            res = s.replaceAll("&", "&#38;");
            res = res.replaceAll("<", "&#60;");
            res = res.replaceAll(">", "&#62;");
            res = res.replaceAll("/", "&#47;");
            res = res.replaceAll("\"", "&#34;");
        } else
            res = s;
        return res;
    }

    public String replaceAll(String notes, String s1, String s2) {
        return notes.replaceAll(s1, s2);
    }

    public ArrayList load() throws ParserConfigurationException, SAXException, IOException {
        ArrayList tasks = null;
        PreviousStateTasksTagHandler handler = new PreviousStateTasksTagHandler(
                null);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(myFile, handler);
        tasks = handler.getTasks();
        return tasks;
    }

}
