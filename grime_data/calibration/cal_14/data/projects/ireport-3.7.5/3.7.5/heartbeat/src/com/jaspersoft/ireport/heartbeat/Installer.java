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
package com.jaspersoft.ireport.heartbeat;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.net.URLConnection;
import java.util.UUID;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import org.openide.modules.ModuleInstall;
import org.openide.windows.WindowManager;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall implements Runnable {

    public static final String VERSION = "3.7.5";//"3.7.4";//"3.7.3";//"3.7.2";//"3.7.1";//"3.7.0";//"3.6.2";//"3.6.2-RC1";//"3.6.1";//"3.6.0";//"3.6.0";//"3.5.3";//"3.5.2";//"3.5.1";//"3.5.0";//"3.4.0";
    
    @Override
    public void restored() {

        Thread t = new Thread(this);
        t.start();
        
        
    }
    
    public void run()
    {
            if (IReportManager.getInstance().isNoNetwork()) return;
            Preferences props = IReportManager.getPreferences();
            
            try {
            /*
            if (props.getProperty("update.useProxy", "false").equals("true"))
            {
                System.getProperties().put( "proxySet", "true" );
                
                String urlProxy = props.getProperty("update.proxyUrl", "");
                String port = "8080";
                String server = urlProxy;
                if (urlProxy.indexOf(":") > 0)
                {
                    port = urlProxy.substring(urlProxy.indexOf(":") + 1);
                    server = urlProxy.substring(0, urlProxy.indexOf(":"));
                }
                
                System.getProperties().put( "proxyHost", server );
                System.getProperties().put( "proxyPort", port );

                MainFrame.getMainInstance().logOnConsole("Using proxy: " + urlProxy);
                
            }
            */
            String uuid = IReportManager.getPreferences().get("UUID",null);
            int newInstallation = 0;
            if (uuid == null || uuid.length() == 0)
            {
                newInstallation = 1;
                uuid = UUID.randomUUID().toString();
                IReportManager.getPreferences().put("UUID",uuid);
            }

            System.out.println("Invoking URL " + "http://ireport.sf.net/lastversion.php?version=" + VERSION + "&nb=1&uuid=" + uuid + "&new="+newInstallation);
            System.out.flush();
            java.net.URL url = new java.net.URL("http://ireport.sf.net/lastversion.php?version=" + VERSION + "&nb=1&uuid=" + uuid + "&new="+newInstallation);
            
            byte[] webBuffer = new byte[400];
            URLConnection uConn = url.openConnection();
            
            
            java.io.InputStream is = uConn.getInputStream();
            int readed = is.read(webBuffer);
            final String version = new String(webBuffer,0,readed);

            if (version.compareTo(VERSION) > 0)
            {
                WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

                    public void run() {

                        JOptionPane.showMessageDialog(Misc.getMainFrame(), I18n.getString("new.version.available", version), I18n.getString("new.version.available.title"), JOptionPane.INFORMATION_MESSAGE);
                    }
                });

            }
            
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

}
