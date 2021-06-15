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

import com.jaspersoft.ireport.designer.sheet.properties.OnErrorTypeProperty;
import com.jaspersoft.ireport.designer.sheet.properties.LazyProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ImageUsingCacheProperty;
import com.jaspersoft.ireport.designer.sheet.properties.VerticalAlignmentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.HorizontalAlignmentProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ScaleImageProperty;
import com.jaspersoft.ireport.designer.sheet.properties.FillProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ImageExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.properties.RadiusProperty;
import com.jaspersoft.ireport.designer.sheet.properties.DirectionProperty;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.sheet.JRPenProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ImageEvaluationTimeProperty;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignEllipse;
import net.sf.jasperreports.engine.design.JRDesignGraphicElement;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Sheet;

/**
 *
 * @author gtoffoli
 */
public class GraphicElementPropertiesFactory {

    /**
     * Get the GraphicElement properties...
     */
    public static List<Sheet.Set> getGraphicPropertySets(JRDesignGraphicElement element, JasperDesign jd)
    {
        JRDesignDataset dataset = ModelUtils.getElementDataset(element, jd);
        
        List<Sheet.Set> list = new ArrayList<Sheet.Set>();
        Sheet.Set propertySet = Sheet.createPropertiesSet();
        propertySet.setName("GRAPHIC_ELEMENT_PROPERTIES");
        propertySet.setDisplayName("Graphic properties");
        //propertySet.put(new PenProperty( element ));
        propertySet.put(new JRPenProperty(element.getLinePen(), element));
        propertySet.put(new FillProperty( element ));
        
        list.add(propertySet);
        
        if (element instanceof JRDesignImage)
        {
            Sheet.Set imagePropertySet = Sheet.createPropertiesSet();
            imagePropertySet.setName("IMAGE_ELEMENT_PROPERTIES");
            imagePropertySet.setDisplayName("Image properties");
            imagePropertySet.put(new ImageExpressionProperty((JRDesignImage)element, dataset));
            imagePropertySet.put(new ImageExpressionClassNameProperty((JRDesignImage)element) );
            imagePropertySet.put(new ScaleImageProperty( (JRDesignImage)element ));
            imagePropertySet.put(new HorizontalAlignmentProperty( (JRDesignImage)element ));
            imagePropertySet.put(new VerticalAlignmentProperty( (JRDesignImage)element ));
            imagePropertySet.put(new ImageUsingCacheProperty( (JRDesignImage)element ));
            imagePropertySet.put(new LazyProperty( (JRDesignImage)element ));
            imagePropertySet.put(new OnErrorTypeProperty( (JRDesignImage)element ));
            imagePropertySet.put(new ImageEvaluationTimeProperty((JRDesignImage)element, dataset));//, dataset));
            imagePropertySet.put(new EvaluationGroupProperty((JRDesignImage)element, dataset));
            list.add(imagePropertySet);
        }
        else if (element instanceof JRDesignLine)
        {
            Sheet.Set linePropertySet = Sheet.createPropertiesSet();
            linePropertySet.setName("LINE_ELEMENT_PROPERTIES");
            linePropertySet.setDisplayName("Line properties");
            linePropertySet.put(new DirectionProperty( (JRDesignLine)element ));
            list.add(linePropertySet);
        }
        else if (element instanceof JRDesignRectangle)
        {
            Sheet.Set rectanglePropertySet = Sheet.createPropertiesSet();
            rectanglePropertySet.setName("RECTANGLE_ELEMENT_PROPERTIES");
            rectanglePropertySet.setDisplayName("Rectangle properties");
            rectanglePropertySet.put(new RadiusProperty( (JRDesignRectangle)element ));
            list.add(rectanglePropertySet);
        }
        else if (element instanceof JRDesignEllipse)
        {
            // Nothing to do...
        }
        
        return list;
    }
    
    
}
