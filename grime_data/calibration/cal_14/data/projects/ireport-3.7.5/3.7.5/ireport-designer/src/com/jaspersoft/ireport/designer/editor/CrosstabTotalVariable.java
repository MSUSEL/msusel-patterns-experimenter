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
package com.jaspersoft.ireport.designer.editor;

import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;

/**
 *
 * @author gtoffoli
 */
public class CrosstabTotalVariable extends ExpObject {

    private JRCrosstabColumnGroup columnGroup = null;
    private JRCrosstabRowGroup rowGroup = null;
    private JRCrosstabMeasure measure = null;

    public CrosstabTotalVariable(
            JRCrosstabMeasure measure,
            JRCrosstabRowGroup rowGroup,
            JRCrosstabColumnGroup columnGroup)
    {
        this.measure = measure;
        this.rowGroup = rowGroup;
        this.columnGroup = columnGroup;
        
        setClassType( measure.getValueClassName() );
        setName( toString() );
        setType(TYPE_VARIABLE);
        
    }
    
    public JRCrosstabColumnGroup getColumnGroup() {
        return columnGroup;
    }

    public void setColumnGroup(JRCrosstabColumnGroup columnGroup) {
        this.columnGroup = columnGroup;
    }

    public JRCrosstabRowGroup getRowGroup() {
        return rowGroup;
    }

    public void setRowGroup(JRCrosstabRowGroup rowGroup) {
        this.rowGroup = rowGroup;
    }

    public JRCrosstabMeasure getMeasure() {
        return measure;
    }

    public void setMeasure(JRCrosstabMeasure measure) {
        this.measure = measure;
    }
    
    @Override
    public String toString()
    {
            String s =  measure.getName();
            if (columnGroup == null && rowGroup == null) return s;
            if (columnGroup == null)
            {
                return  s + " (total by " + rowGroup.getName() + ")";
            }
            else if (rowGroup == null)
            {
                return  s + " (total by " + columnGroup.getName() + ")";
            }
            else
            {
                return  s + " (total by " + rowGroup.getName() + " and " + columnGroup.getName() +")";
            }
    }
    
    @Override
    public String getExpression()
    {
        String s = "$V{" + measure.getName();
        if (rowGroup != null)
        {
            s += "_" + rowGroup.getName();
        }
        
        if (columnGroup != null)
        {
             s += "_" + columnGroup.getName();
        }
        
        return s + "_ALL}";
    }
}
