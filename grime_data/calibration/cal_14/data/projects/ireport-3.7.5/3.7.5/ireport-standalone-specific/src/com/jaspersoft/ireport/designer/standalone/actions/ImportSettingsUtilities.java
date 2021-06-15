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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.designer.standalone.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.prefs.Preferences;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;

/**
 *
 * @version $Id: ImportSettingsUtilities.java 0 2009-11-20 10:48:09 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ImportSettingsUtilities {

    /**
     * Return the list of iReport installations (included this one)
     * If an error occurs, or the netbeans.user property is not set, the method returns null.
     * 
     * @return
     */
    public static String[] getAvailableVersions()
    {
        String dir = System.getProperty("netbeans.user"); //NOI18N
        if (dir != null && dir.length() > 0)
        {
            File f = new File(dir);
            if (f.exists() && f.getParentFile() != null && f.getParentFile().exists())
            {
                String[] versions = f.getParentFile().list(new FilenameFilter() {

                    public boolean accept(File dir, String name) {
                        File f = new File(dir, name);
                        String currentVersion = getCurrentVersion();
                        return (!name.contains(currentVersion)) && isValidConfigurationDirectory(f);
                    }
                });
                Arrays.sort(versions);
                //FIXMEGT For each value, check it is a file, and it contains the config\Preferences\com directory...
                return versions;
            }
        }

        return null;
    }
    
    public static String getCurrentVersion()
    {
        String s = NbBundle.getBundle("org.netbeans.core.startup.Bundle").getString("currentVersion"); //NOI18N
        if (s == null) return "";
        s = s.trim();
        if (s.length()>0 && s.lastIndexOf(" ") >= 0)
        {
            s = s.substring(s.lastIndexOf(" ")+1);
        }
        return s;
    }

    public static void importSettings(File dir) throws Exception
    {
        
        File iReportSettingsFile = new File(dir, "config/Preferences/com/jaspersoft/ireport.properties");

        // remove UUID...
        Preferences prefs = NbPreferences.forModule(IReportManager.class);
        if (iReportSettingsFile.exists())
        {
            Properties props = new java.util.Properties();
            props.load(new FileInputStream(iReportSettingsFile));

            Enumeration enumer = props.keys();
            while (enumer.hasMoreElements())
            {
                String key = (String)enumer.nextElement();
                if (key.equals("UUID")) continue;

                prefs.put(key, props.getProperty(key));
            }
        }

        File jasperPluginSettingsFile = new File(dir, "config/Preferences/com/jaspersoft/ireport/jasperserver.properties");

        // remove UUID...
        prefs = NbPreferences.forModule(JasperServerManager.class);
        if (jasperPluginSettingsFile.exists())
        {
            Properties props = new java.util.Properties();
            props.load(new FileInputStream(jasperPluginSettingsFile));

            Enumeration enumer = props.keys();
            while (enumer.hasMoreElements())
            {
                String key = (String)enumer.nextElement();
                prefs.put(key, props.getProperty(key));
            }
        }
    }

    public static final boolean isValidConfigurationDirectory(File f)
    {
        try {
        if (f == null || !f.exists() || !f.isDirectory()) return false;
        // Look for the /config/preferences/ directory...
        File f2 = new File(f, "config/preferences");
        return f2.exists();
        } catch (Exception ex)
        {

        }
        return false;
    }
}
