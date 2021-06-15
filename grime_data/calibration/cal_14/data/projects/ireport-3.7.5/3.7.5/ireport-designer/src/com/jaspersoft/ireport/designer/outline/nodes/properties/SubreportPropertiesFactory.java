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
package com.jaspersoft.ireport.designer.outline.nodes.properties;

import com.jaspersoft.ireport.designer.sheet.properties.SubreportUsingCacheProperty;
import com.jaspersoft.ireport.designer.sheet.properties.SubreportParametersProperty;
import com.jaspersoft.ireport.designer.sheet.properties.SubreportExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ParametersMapExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.properties.DataSourceExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ConnectionExpressionProperty;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.sheet.properties.RunToBottomProperty;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Sheet;

/**
 *
 * @author gtoffoli
 */
public class SubreportPropertiesFactory {

    
    
    /**
     * Get the static text properties...
     */
    public static Sheet.Set getSubreportPropertySet(JRDesignSubreport element, JasperDesign jd)
    {
        
        JRDesignDataset dataset = ModelUtils.getElementDataset(element, jd);
        Sheet.Set propertySet = Sheet.createPropertiesSet();
        propertySet.setName("SUBREPORT_ELEMENT_PROPERTIES");
        propertySet.setDisplayName("Subreport properties");
        propertySet.put(new SubreportExpressionProperty(element, dataset));
        propertySet.put(new SubreportExpressionClassNameProperty(element));
        propertySet.put(new SubreportUsingCacheProperty(element));
        propertySet.put(new RunToBottomProperty(element));
        propertySet.put(new ParametersMapExpressionProperty(element, dataset));
        propertySet.put(new ConnectionTypeProperty(element) );
        propertySet.put(new ConnectionExpressionProperty(element, dataset));
        propertySet.put(new DataSourceExpressionProperty(element, dataset));
        propertySet.put(new SubreportParametersProperty(element, dataset));
        propertySet.put(new SubreportReturnValuesProperty(element, dataset));
        
        //propertySet.put(new LeftProperty( element ));
        return propertySet;
    }
    
    /**
     * Convenient way to get all the properties of an element.
     * Properties positions could be reordered to have a better order.
     */
    public static List<Sheet.Set> getPropertySets(JRDesignElement element, JasperDesign jd)
    {
        List<Sheet.Set> sets = new ArrayList<Sheet.Set>();
        
        if (element instanceof  JRDesignSubreport)
        {
            sets.add( getSubreportPropertySet((JRDesignSubreport)element, jd ));
        }
        
        return sets;
    }
    
    
}
