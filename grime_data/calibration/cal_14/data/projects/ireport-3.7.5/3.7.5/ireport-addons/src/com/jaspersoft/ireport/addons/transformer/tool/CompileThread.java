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
package com.jaspersoft.ireport.addons.transformer.tool;

import com.jaspersoft.ireport.addons.transformer.BarcodeTransformer;
import com.jaspersoft.ireport.addons.transformer.Transformer;
import com.jaspersoft.ireport.designer.IRURLClassLoader;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ReportClassLoader;
import com.jaspersoft.ireport.designer.compatibility.JRXmlWriterHelper;
import com.jaspersoft.ireport.designer.utils.Misc;
import javax.swing.table.*;
import java.io.*;
import java.net.URL;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
/**
 *
 * @author  Administrator
 */
public class CompileThread implements Runnable {
    
    private TransformationFrame massiveCompilerFrame = null;
    private boolean stop = false;
    private Thread thread = null;
    ReportClassLoader reportClassLoader = null;
    private Transformer transformer = null;
    
    
    private boolean compileSelectedOnly = false;
    
    public CompileThread(TransformationFrame mcf)
    {
        this.massiveCompilerFrame = mcf;
        thread = new Thread(this);
    }
    
    public void stop()
    {
        stop = true;
    }
    
    public void start()
    {
        thread.start();
    }
    
    public void run() {
        if (massiveCompilerFrame == null)
        {
            return;
        }
        

        final DefaultTableModel dtm = (DefaultTableModel)massiveCompilerFrame.getFileTable().getModel();

        for (int i=0; i<dtm.getRowCount(); ++i)
        {
            final int index = i;
            final FileEntry fe = (FileEntry)dtm.getValueAt(i, 0);
            if ( isCompileSelectedOnly() &&  !massiveCompilerFrame.getFileTable().isRowSelected(i))
            {
                continue;
            }
            
            // Start to transform this report...
            String srcFileName = "";
               
            try
            {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            try  {
                                fe.setStatus( FileEntry.STATUS_TRANSFORMING );
                                //DefaultTableModel dtm = (DefaultTableModel)massiveCompilerFrame.getFileTable().getModel();
                                dtm.setValueAt( fe, index, 0);
                            
                                dtm.setValueAt( fe.getFile().getCanonicalPath(), index, 1);
                                dtm.setValueAt( FileEntry.decodeStatus( fe.getStatus()), index, 2);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
               } catch (Exception ex) {}
                
                srcFileName = fe.getFile().getCanonicalPath();
                
                try {
                        IRURLClassLoader cl = new IRURLClassLoader(new URL[]{ fe.getFile().toURI().toURL() } , IReportManager.getReportClassLoader());
                        Thread.currentThread().setContextClassLoader( cl );
                } catch (Exception ex) {

                    ex.printStackTrace();
                }
                

                JasperDesign jasperDesign = getTransformer().transform(srcFileName);


                if (jasperDesign != null)
                {
                    writeReport(jasperDesign, new File(srcFileName));
                }
                fe.setStatus( FileEntry.STATUS_TRANSFORMED );

            } catch (Exception ex)
            {
                fe.setStatus( FileEntry.STATUS_ERROR_TRANSFORMING );
                StringWriter sw = new StringWriter();
                ex.printStackTrace( new PrintWriter( sw ));
                fe.setMessage(  sw.getBuffer().toString() );
            }

            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        try {
                            dtm.setValueAt(fe, index, 0);
                            dtm.setValueAt(fe.getFile().getCanonicalPath(), index, 1);
                            dtm.setValueAt(FileEntry.decodeStatus(fe.getStatus()), index, 2);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            } catch (Exception ex) {

            }
        }


        try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {

                            massiveCompilerFrame.getFileTable().updateUI();
                            massiveCompilerFrame.finishedCompiling();

                    }
                });
            } catch (Exception ex) {}
        
    }
    
    /** Getter for property compileSelectedOnly.
     * @return Value of property compileSelectedOnly.
     *
     */
    public boolean isCompileSelectedOnly() {
        return compileSelectedOnly;
    }    
    
    /** Setter for property compileSelectedOnly.
     * @param compileSelectedOnly New value of property compileSelectedOnly.
     *
     */
    public void setCompileSelectedOnly(boolean compileSelectedOnly) {
        this.compileSelectedOnly = compileSelectedOnly;
    }

    /**
     * @return the transformer
     */
    public Transformer getTransformer() {
        return transformer;
    }

    /**
     * @param transformer the transformer to set
     */
    public void setTransformer(Transformer transformer) {
        this.transformer = transformer;
    }

    public static void writeReport(JasperDesign jd, File outputFile) throws java.lang.Exception
    {
        final String compatibility = IReportManager.getPreferences().get("compatibility", "");

        String content = "";
        if (compatibility.length() == 0)
        {
            content = JRXmlWriter.writeReport(jd, "UTF-8"); // IReportManager.getInstance().getProperty("jrxmlEncoding", System.getProperty("file.encoding") ));
        }
        else
        {
            content = JRXmlWriterHelper.writeReport(jd, "UTF-8", compatibility);
        }

        Writer out = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
        out.write(content);
        out.close();
    }

}
