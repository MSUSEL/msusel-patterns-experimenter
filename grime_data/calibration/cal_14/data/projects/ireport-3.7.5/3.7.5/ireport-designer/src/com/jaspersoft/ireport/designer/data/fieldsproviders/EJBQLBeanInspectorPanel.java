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
package com.jaspersoft.ireport.designer.data.fieldsproviders;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.data.fieldsproviders.ejbql.EJBQLFieldsReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import net.sf.jasperreports.engine.design.JRDesignField;

/**
 *
 * @author gtoffoli
 */
public class EJBQLBeanInspectorPanel extends BeanInspectorPanel {
    
    /** Creates a new instance of EJBQLBeanInspectorPanel */
    public EJBQLBeanInspectorPanel() {
        super();
        
    }
    
    /**
     * Ad hoc queryChanged method for EJBQL queries....
     */
    @SuppressWarnings("unchecked")
    public void queryChanged(String newQuery) {
    
        lastExecution++;
        int thisExecution = lastExecution;
        // Execute a thread to perform the query change...
        
        String error_msg = "";
        lastExecution++;
            
        int in = lastExecution;
            
        getReportQueryDialog().getJLabelStatusSQL().setText("Executing EJBQL query....");
        /////////////////////////////
            
        try {
        Thread.currentThread().setContextClassLoader( IReportManager.getInstance().getReportClassLoader());
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
            
        if (in < lastExecution) return; //Abort, new execution requested
        
        EJBQLFieldsReader ejbqlFR = new EJBQLFieldsReader(newQuery, getReportQueryDialog().getDataset().getParametersList());
            
            try {
                Vector fields = ejbqlFR.readFields();
                
                List columns = new ArrayList();
                for (int i=0; i<fields.size(); ++i)
                {
                    JRDesignField field = (JRDesignField)fields.elementAt(i);
                    columns.add( new Object[]{field, field.getValueClassName(), field.getDescription()} );
                }
                Vector v = null;
                if (ejbqlFR.getSingleClassName() != null)
                {
                    v = new Vector();
                    v.add( ejbqlFR.getSingleClassName() );
                }
                
                setBeanExplorerFromWorker(v,true,false);
                setColumnsFromWorker(columns);
                
            } catch (Exception ex)
            {
                ex.printStackTrace();
                setBeanExplorerFromWorker(null,true,false);
                setColumnErrorFromWork( "Error: " +  ex.getMessage() );
            }
        
        getReportQueryDialog().getJLabelStatusSQL().setText("Ready");
    }
    
}
