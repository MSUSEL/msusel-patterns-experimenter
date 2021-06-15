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

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class IRExpressionCollector extends JRExpressionCollector {

    JasperDesign jasperDesign = null;
    
    public List extraExpressions = new ArrayList();
    
    public IRExpressionCollector(JasperDesign jd)
    {
        super(null, jd);
        jasperDesign = jd;
    }

    @Override
    public List getExpressions() {
        List expressions = super.getExpressions();
        expressions.addAll(extraExpressions);
        return expressions;
    }
    
    
    
    
    @Override
    public void collect(JRChart element)
    {
            JRDesignChart chart = (JRDesignChart)element;
            super.collect(element);
            chart.getDataset().collectExpressions(this);
            chart.getPlot().collectExpressions(this);
            JRDatasetRun datasetRun = chart.getDataset().getDatasetRun();
            
            
            if (datasetRun != null &&
                datasetRun.getDatasetName() != null)
            {
                    JRExpressionCollector collector = getCollector( (JRDataset)jasperDesign.getDatasetMap().get(datasetRun.getDatasetName()));
                    extraExpressions.addAll(collector.getExpressions());
            }
            System.out.flush();
            
    }
    
}
