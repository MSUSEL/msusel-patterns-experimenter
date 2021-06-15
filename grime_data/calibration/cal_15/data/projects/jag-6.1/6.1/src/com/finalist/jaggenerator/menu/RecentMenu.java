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
/* RecentMenu.java
 * Created on Apr 12, 2006
 *
 * Last modified: $Date: 2006/05/21 07:57:49 $
 * @version $Revision: 1.2 $ 
 * @author afeltes
 */
package com.finalist.jaggenerator.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.finalist.jaggenerator.JagGenerator;

public class RecentMenu extends JMenu implements ActionListener {

    /**
     * 
     */
    static Log log = LogFactory.getLog(RecentMenu.class);

    private static final long serialVersionUID = 1L;

    private static final String FS = System.getProperty("file.separator");

    /* In case there is no $HOME, it will write to $TEMP */
    private static String CONFIG_DIR = System.getProperty("user.home") + FS
            + ".jag";

    private static String CONFIG_FILE = CONFIG_DIR + FS + "recent.properties";

    private static final int MAX_RECENT = 10;

    private static final String RECENT_FILE_SELECTED = "_recent_file_selected";

    private JagGenerator mainApp = null;

    public RecentMenu() {
        initialize();
    }

    private void initialize() {
        checkDir(CONFIG_DIR);
        loadRecentList();
    }

    private void loadRecentList() {
        int i = 1;
        boolean mod = false;
        removeAll();
        TreeMap recent = getRecentFiles();
        Iterator iter = recent.keySet().iterator();

        while (iter.hasNext()) {
            String array_element = (String) recent.get(iter.next());
            // Object obj = iter.next();
            // String array_element = "";
            File f = new File(array_element);
            if (f.canRead()) {
                JMenuItem jmi = new JMenuItem("" + i + " " + f.getName());
                jmi.setToolTipText(array_element);
                jmi.setMnemonic(i + '0');
                jmi.setActionCommand(RECENT_FILE_SELECTED);
                jmi.setName(array_element);
                jmi.addActionListener(this);
                add(jmi);
                i++;
            } else {
                mod = true;
                recent.remove(array_element);
            }
            if (mod)
                saveRecentFileList(recent);
        }

    }

    private TreeMap getRecentFiles() {
        TreeMap ret = null;
        Properties prop = new Properties();
        try {
            if (new File(CONFIG_FILE).exists())
                prop.load(new FileInputStream(CONFIG_FILE));
            else
                createPropertiesFile();
            ret = new TreeMap(Collections.reverseOrder());
            ret.putAll(prop);
            if (!ret.isEmpty())
                JagGenerator.setFileChooserStartDir(
                        JagGenerator.FILECHOOSER_APPFILE_OPEN, new File(
                                (String) ret.get(ret.firstKey())).getParentFile());
        } catch (FileNotFoundException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        }
        return ret;
    }

    /**
     * Check if directory exists and if it writeable, or create it if it does
     * not
     */
    public static void checkDir(String name) {

        java.io.File dir = new java.io.File(name);
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                log.error("Application must be able to read from: "
                        + dir.getAbsolutePath());
                createTmpProperties();
            }
        } else {
            if (!dir.mkdirs()) {
                log.error("Application must be able to write to: "
                        + dir.getAbsolutePath());
                createTmpProperties();
            }
        }
    }

    /**
     * 
     */
    private static void createTmpProperties() {
        CONFIG_DIR = System.getProperty("java.io.tmpdir");
        CONFIG_FILE = CONFIG_DIR + FS + "recent.properties";
        Properties prop = new Properties();
        try {
            prop.store(new FileOutputStream(CONFIG_FILE), "");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            log.error("", e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("", e);
        }
    }

    private static void createPropertiesFile() {
        Properties prop = new Properties();
        try {
            prop.store(new FileOutputStream(CONFIG_FILE), "");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            log.error("", e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("", e);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            JMenuItem jmi = (JMenuItem) e.getSource();
            if (jmi.getActionCommand().equals(RECENT_FILE_SELECTED)) {
                getMainApp().loadApplicationFile(new File(jmi.getName()));
            }
        }
    }

    public JagGenerator getMainApp() {
        return mainApp;
    }

    public void setMainApp(JagGenerator mainApp) {
        this.mainApp = mainApp;
    }

    public void addToRecentList(String fullFilePath) {
        Date d = new Date();
        TreeMap recent = getRecentFiles();
        while (recent.size() >= MAX_RECENT) {
            recent.remove(recent.lastKey());
        }
        if (recent.containsValue(fullFilePath)) {
            removeFromTreeMap(recent, fullFilePath);
        }
        recent.put(new Long(d.getTime()).toString(), fullFilePath);
        saveRecentFileList(recent);
        loadRecentList();
    }

    private void removeFromTreeMap(TreeMap recent, String fullFilePath) {
        Iterator iter = recent.keySet().iterator();
        ArrayList al = new ArrayList();
        while (iter.hasNext()) {
            Object key = iter.next();
            if (recent.get(key).equals(fullFilePath))
                al.add(key);
        }
        iter = al.iterator();
        while (iter.hasNext())
            recent.remove(iter.next());
    }

    private void saveRecentFileList(TreeMap recent) {
        Properties props = new Properties();
        props.putAll(recent);
        try {
            props.store(new FileOutputStream(CONFIG_FILE),
                    "JAG recent project list");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            log.error("", e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("", e);
        }
    }

    public void removeFromRecentList(String absolutePath) {
        TreeMap recent = getRecentFiles();
        recent.remove(absolutePath);
        saveRecentFileList(recent);
    }

}
