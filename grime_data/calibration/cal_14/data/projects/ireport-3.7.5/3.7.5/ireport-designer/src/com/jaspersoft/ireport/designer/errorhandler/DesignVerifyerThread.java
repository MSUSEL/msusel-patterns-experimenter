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
package com.jaspersoft.ireport.designer.errorhandler;


import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.ModelChangeListener;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JRValidationFault;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 * This class is tied to the JrxmlVisualView.
 * All the times there is a change in the model, the document is verified.
 * The verification starts 500 milliseconds after the change, so it can
 * collect other changed before to proceed...
 * The verification should be very fast since we are operating directly
 * on the model...
 * 
 * @author gtoffoli
 */
public class DesignVerifyerThread implements Runnable, ModelChangeListener {
    
    private boolean reportChanged = true;
    private Thread thisThread = null;
    private boolean stop = false;
    private JrxmlVisualView jrxmlVisualView = null;
    
    
    /** Creates a new instance of DesignVerifyerThread */
    public DesignVerifyerThread(JrxmlVisualView view) {
    
        this.jrxmlVisualView = view;
        // Listen for changes in the document...
        view.addModelChangeListener(this);
        thisThread = new Thread( this );
    }
    
    public void start()
    {
        thisThread.start();
    }
    
    public void stop()
    {
        setStop(true);
    }

    public void run() {
        
        while (!isStop())
        {
            try {
                Thread.sleep(2000);
            } catch (Exception ex)
            {
            }
        
            if (isReportChanged())    
            {
                setReportChanged(false);
                verifyDesign();
            }
        }
    }

    public boolean isReportChanged() {
        return reportChanged;
    }

    public void setReportChanged(boolean reportChanged) {
        this.reportChanged = reportChanged;
    }

    public void modelChanged(JrxmlVisualView view) {
        if (this.getJrxmlVisualView() == view)
        {
            setReportChanged(true);
        }
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
    
    
    public void verifyDesign()
    {
       // Remove all the WARNINGS...
        
        
       for (int i=0; i<getJrxmlVisualView().getReportProblems().size(); ++i)
       {
           ProblemItem pii = getJrxmlVisualView().getReportProblems().get(i);
           if (pii.getProblemType() == ProblemItem.WARNING ||
               pii.getProblemType() == ProblemItem.INFORMATION)
           {
               getJrxmlVisualView().getReportProblems().remove(i);
               i--;
           }
       }
       //getJReportFrame().getReportProblems().clear();
       
       try {
            //SourceTraceDigester digester = IReportCompiler.createDigester();
            //ReportWriter rw = new ReportWriter(getJReportFrame().getReport());
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //rw.writeToOutputStream(baos);
            //JasperDesign jd = IReportCompiler.loadJasperDesign( new ByteArrayInputStream( baos.toByteArray() ), digester);
            
            if (getJrxmlVisualView().getModel() != null &&
                getJrxmlVisualView().getModel().getJasperDesign() != null)
            {
                JasperDesign design = getJrxmlVisualView().getModel().getJasperDesign();
            
                Collection ls = JasperCompileManager.verifyDesign(design);
                Iterator iterator = ls.iterator();
                while (iterator.hasNext())
                {
                    JRValidationFault fault = (JRValidationFault)iterator.next();
                    String s = fault.getMessage();
                    //SourceLocation sl = digester.getLocation( fault.getSource() );
                    getJrxmlVisualView().getReportProblems().add( new ProblemItem(ProblemItem.WARNING, fault));
                }
            }
       } catch (Exception ex)
        {
            ex.printStackTrace();
            getJrxmlVisualView().getReportProblems().add(new ProblemItem(ProblemItem.WARNING, ex.getMessage(), null, null) );
        }
       
       Runnable runner = new Runnable(){
                public void run()
                {
                    //MainFrame.getMainInstance().getLogPane().getProblemsPanel().update();
                    ErrorHandlerTopComponent.getDefault().refreshErrors();
                }
        };

        SwingUtilities.invokeLater(runner);
        
    }

    public JrxmlVisualView getJrxmlVisualView() {
        return jrxmlVisualView;
    }

    public void setJrxmlVisualView(JrxmlVisualView jrxmlVisualView) {
        this.jrxmlVisualView = jrxmlVisualView;
    }
}
