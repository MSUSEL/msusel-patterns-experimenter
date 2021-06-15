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
package com.jaspersoft.ireport.designer.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

/**
 *
 * @version $Id: DataScriptlet.java 0 2010-05-28 17:22:14 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class DataScriptlet extends JRDefaultScriptlet {

    DefaultTableModel model = null;

    Map fields = null;
    List<String> fieldNames = null;

    @Override
    public void afterReportInit() throws JRScriptletException {
    }




    @Override
    public void afterDetailEval() throws JRScriptletException {
        
        
        if (model == null)
        {
            model = (DefaultTableModel)getParameterValue("ireport.data.tabelmodel");
        }

        if (fieldNames == null)
        {

            model.setRowCount(0);
            model.setColumnCount(0);
            fieldNames = new ArrayList<String>();
            Iterator fieldsIter = this.fieldsMap.keySet().iterator();

            while (fieldsIter.hasNext())
            {
                final String fName = fieldsIter.next()+"";
                fieldNames.add(fName);
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            model.addColumn(fName);
                        }
                    });
                } catch (Exception ex) {
                }
                
            }
        }

        

        final Object[] row = new Object[fieldNames.size()];

        int i=0;
        for (String fName : fieldNames)
        {
            // Add the value to the table...
            row[i] = getFieldValue(fName);
            i++;
        }
        try {

            

            
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    try {
                        final int record_count = (Integer)getVariableValue("REPORT_COUNT");
                        if (model.getRowCount() < record_count) model.addRow(row);
                    } catch (Exception ex) {}
                }
            });
        } catch (Exception ex) {
        }


        super.afterDetailEval();

    }


}
