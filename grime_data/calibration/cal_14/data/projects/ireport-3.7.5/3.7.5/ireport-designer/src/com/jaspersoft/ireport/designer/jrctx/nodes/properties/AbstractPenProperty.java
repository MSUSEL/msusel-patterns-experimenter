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
package com.jaspersoft.ireport.designer.jrctx.nodes.properties;

import com.jaspersoft.ireport.designer.sheet.editors.JRPenPropertyEditor;
import com.jaspersoft.ireport.designer.sheet.properties.AbstractProperty;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.chartthemes.simple.ColorProvider;
import net.sf.jasperreports.chartthemes.simple.PaintProvider;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPenContainer;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.util.JRPenUtil;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractPenProperty extends AbstractProperty implements JRPenContainer
{
    private JRPenPropertyEditor editor = null;
    
    @SuppressWarnings("unchecked")
    public AbstractPenProperty(Object object)
    {
       super(JRPen.class, object);

       setValue( "canEditAsText", Boolean.FALSE );
    }

    @Override
    public Object getPropertyValue() 
    {
        return getPen();
    }

    @Override
    public Object getOwnPropertyValue() 
    {
        return getOwnPen();
    }

    @Override
    public Object getDefaultValue() 
    {
        return getDefaultPen();
    }

    @Override
    public PropertyEditor getPropertyEditor() {
        
        if (editor == null)
        {
            editor = new JRPenPropertyEditor();
        }
        return editor;
    }

    @Override
    public void validate(Object value)
    {
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException 
    {
        setPropertyValue(getDefaultPen());
    }

    @Override
    public void setPropertyValue(Object value)
    {
        setPen((JRPen)value);
    }

    public JRPen getPen() 
    {
        JRPen pen = new JRBasePen(this);
        
        if (
            getPaintProvider() != null
            && getStroke() != null
            )
        {
            ColorProvider colorProvider = 
                getPaintProvider() instanceof ColorProvider
                ? (ColorProvider)getPaintProvider()
                : null;
            if (colorProvider != null)
            {
                pen.setLineColor(colorProvider.getColor());
            }
            BasicStroke basicStroke = 
                getStroke() instanceof BasicStroke
                ? (BasicStroke)getStroke()
                : null;
            if (basicStroke != null)
            {
                setToPen(basicStroke, pen);
            }
        }

        return pen;
    }

    public JRPen getOwnPen()
    {
        return getPen();
    }

    public JRPen getDefaultPen()
    {
        return null;
    }

    public void setPen(JRPen pen)
    {
        if (pen == null)
        {
            setPaintProvider(null);
            setStroke(null);
        }
        else
        {
            setPaintProvider(new ColorProvider(pen.getLineColor()));
            setStroke(JRPenUtil.getStroke(pen, BasicStroke.CAP_SQUARE));
        }
    }

    /**
     *
     */
    public void setToPen(BasicStroke stroke, JRPen pen)
    {
        if (stroke != null && pen != null)
        {
            float lineWidth = stroke.getLineWidth();
            float[] dashArray = stroke.getDashArray();

            pen.setLineWidth(lineWidth);
            
            int lineCap = stroke.getEndCap();
            switch (lineCap)
            {
                case BasicStroke.CAP_SQUARE :
                {
                    if (
                        dashArray != null && dashArray.length == 2 
                        && dashArray[0] == 0 && dashArray[1] == 2 * lineWidth
                        )
                    {
                        pen.setLineStyle(JRPen.LINE_STYLE_DOTTED);
                    }
                    else if (
                        dashArray != null && dashArray.length == 2 
                        && dashArray[0] == 4 * lineWidth && dashArray[1] == 4 * lineWidth
                        )
                    {
                        pen.setLineStyle(JRPen.LINE_STYLE_DASHED);
                    }
                    else
                    {
                        pen.setLineStyle(JRPen.LINE_STYLE_SOLID);
                    }
                    break;
                }
                case BasicStroke.CAP_BUTT :
                {
                    if (
                        dashArray != null && dashArray.length == 2 
                        && dashArray[0] == lineWidth && dashArray[1] == lineWidth
                        )
                    {
                        pen.setLineStyle(JRPen.LINE_STYLE_DOTTED);
                    }
                    else if (
                        dashArray != null && dashArray.length == 2 
                        && dashArray[0] == 5 * lineWidth && dashArray[1] == 3 * lineWidth
                        )
                    {
                        pen.setLineStyle(JRPen.LINE_STYLE_DASHED);
                    }
                    else
                    {
                        pen.setLineStyle(JRPen.LINE_STYLE_SOLID);
                    }
                    break;
                }
            }
        }
    }

    public Float getDefaultLineWidth() {
        return new Float(0);
    }

    public Color getDefaultLineColor() {
        return null;
    }

    public JRDefaultStyleProvider getDefaultStyleProvider() {
        return null;
    }

    public JRStyle getStyle() {
        return null;
    }

    public String getStyleNameReference() {
        return null;
    }

    public abstract PaintProvider getPaintProvider();

    public abstract void setPaintProvider(PaintProvider paintProvider);

    public abstract Stroke getStroke();

    public abstract void setStroke(Stroke stroke);

}
