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
package com.jaspersoft.ireport.designer.compatibility;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import org.openide.util.Exceptions;
import org.openide.util.Mutex;


/**
 *
 * @author gtoffoli
 */
public class JRXmlWriterHelper {

    private static final Map<String, Class> writers = new HashMap();

    static {

        writers.put("3_7_4", JRXmlWriter_3_7_4.class);
        writers.put("3_7_3", JRXmlWriter_3_7_3.class);
        writers.put("3_7_1", JRXmlWriter_3_7_1.class);
        writers.put("3_6_2", JRXmlWriter_3_6_2.class);
        writers.put("3_6_1", JRXmlWriter_3_6_1.class);
        writers.put("3_6_0", JRXmlWriter_3_6_0.class);
        writers.put("3_5_2", JRXmlWriter_3_5_2.class);
        writers.put("3_5_1", JRXmlWriter_3_5_1.class);
        writers.put("3_5_0", JRXmlWriter_3_5_0.class);
        writers.put("3_1_4", JRXmlWriter_3_1_4.class);
        writers.put("3_1_3", JRXmlWriter_3_1_3.class);
        writers.put("3_1_2", JRXmlWriter_3_1_2.class);
        writers.put("3_1_0", JRXmlWriter_3_1_0.class);
        writers.put("3_0_1", JRXmlWriter_3_0_1.class);
        writers.put("3_0_0", JRXmlWriter_3_0_0.class);
        writers.put("2_0_5", JRXmlWriter_2_0_5.class);
        writers.put("2_0_4", JRXmlWriter_2_0_4.class);
        writers.put("2_0_3", JRXmlWriter_2_0_3.class);
        writers.put("2_0_2", JRXmlWriter_2_0_2.class);
    }

    private static VersionWarningDialog dialog = null;

    public static String writeReport(JRReport report, String encoding, String version) throws Exception
    {

        if (IReportManager.getPreferences().getBoolean("show_compatibility_warning", true))
        {
            setDialog(null); // force the instance of a new dialog...

           getDialog().setVersion(version);

           if (SwingUtilities.isEventDispatchThread())
           {
               getDialog().setVisible(true);
           }
           else
           {
               SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        getDialog().setVisible(true);
                    }
                });
           }
           
           int res = getDialog().getDialogResult();

           if (!getDialog().askAgain())
           {
               IReportManager.getPreferences().putBoolean("show_compatibility_warning", false);
           }

           if (res == VersionWarningDialog.DIALOG_RESULT_USE_LAST_VERSION)
           {
               version = "";
           }

        }
        if (writers.containsKey(version))
        {
            Class clazz = writers.get(version);
            return (String)clazz.getMethod("writeReport", new Class[]{JRReport.class, String.class}).invoke(null, new Object[]{report, encoding});
        }
        else if (version.length() == 0)
        {
            return JRXmlWriter.writeReport(report, encoding);
        }

        throw new Exception("XML writer for version " +version + " not found.");
    }

    /**
     * @return the dialog
     */
    public static VersionWarningDialog getDialog() {
        if (dialog == null)
        {
            // The operation Misc.getMainFrame() requires an AWT thread...
           Runnable run = new Runnable(){
                    public void run() {
                            setDialog(new VersionWarningDialog(Misc.getMainFrame(), true));
                        }
               };

               if (SwingUtilities.isEventDispatchThread())
               {
                   run.run();
               }
               else
               {
                try {
                    SwingUtilities.invokeAndWait(run);
                } catch (Exception ex) {
                }
               }
        }
        return dialog;
    }

    /**
     * @param dialog the dialog to set
     */
    public static void setDialog(VersionWarningDialog dlg) {
        dialog = dlg;
    }


}
