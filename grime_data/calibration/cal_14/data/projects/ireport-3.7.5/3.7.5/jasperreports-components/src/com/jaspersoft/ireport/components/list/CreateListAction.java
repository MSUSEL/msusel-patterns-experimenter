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
package com.jaspersoft.ireport.components.list;

import com.jaspersoft.ireport.designer.palette.actions.*;
import net.sf.jasperreports.components.list.DesignListContents;
import net.sf.jasperreports.components.list.StandardListComponent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.Exceptions;

/**
 *
 * @author gtoffoli
 */
public class CreateListAction extends CreateReportElementAction {

    @Override
    public JRDesignElement createReportElement(JasperDesign jd) {




        JRDesignComponentElement component = new JRDesignComponentElement();
        StandardListComponent componentImpl = new StandardListComponent();
        DesignListContents contents = new DesignListContents();
        contents.setHeight(50);
        contents.setWidth(0);
        componentImpl.setContents(contents);

        JRDesignDataset newDataset = new JRDesignDataset(false);
        String name = "dataset";
        for (int i = 1;; i++) {
            if (!jd.getDatasetMap().containsKey(name + i)) {
                newDataset.setName(name + i);
                break;
            }
        }
        try {
            jd.addDataset(newDataset);
        } catch (JRException ex) {
            //Exceptions.printStackTrace(ex);
        }
        JRDesignDatasetRun datasetRun = new JRDesignDatasetRun();

        datasetRun.setDatasetName(newDataset.getName());

        JRDesignExpression exp = new JRDesignExpression();
        exp.setValueClassName("net.sf.jasperreports.engine.JRDataSource");//NOI18N
        exp.setText("new net.sf.jasperreports.engine.JREmptyDataSource(1)");//NOI18N

        datasetRun.setDataSourceExpression(exp);
        
        
        componentImpl.setDatasetRun(datasetRun);
        component.setComponent(componentImpl);
        component.setComponentKey(new ComponentKey(
                                    "http://jasperreports.sourceforge.net/jasperreports/components",
                                    "jr", "list"));

        component.setWidth(400);
        component.setHeight(50);

        return component;
    }



    
}
