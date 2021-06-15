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
package com.jaspersoft.ireport.designer.palette.actions;

import com.jaspersoft.ireport.designer.IReportManager;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class CreateTextFieldAction extends CreateReportElementsAction
{

    static private java.util.List acceptedTextfieldClasses = null;
    static {
        acceptedTextfieldClasses = new java.util.ArrayList();

        acceptedTextfieldClasses.add("java.lang.Boolean");
        acceptedTextfieldClasses.add("java.lang.Byte");
        acceptedTextfieldClasses.add("java.util.Date");
        acceptedTextfieldClasses.add("java.sql.Timestamp");
        acceptedTextfieldClasses.add("java.sql.Time");
        acceptedTextfieldClasses.add("java.lang.Double");
        acceptedTextfieldClasses.add("java.lang.Float");
        acceptedTextfieldClasses.add("java.lang.Integer");
        acceptedTextfieldClasses.add("java.lang.Long");
        acceptedTextfieldClasses.add("java.lang.Short");
        acceptedTextfieldClasses.add("java.math.BigDecimal");
        acceptedTextfieldClasses.add("java.lang.Number");
        acceptedTextfieldClasses.add("java.lang.String");
    }
    
    public JRDesignElement createReportElement(JasperDesign jd)
    {
        JRDesignTextField element = new JRDesignTextField( jd );
        JRDesignExpression exp = new JRDesignExpression();
        exp.setValueClassName("java.lang.String");
        exp.setText("$F{field}");
        element.setExpression(exp);
        element.setWidth(100);
        element.setHeight(20);
        return element;
    }
    
    /** Setter for property classExpression.
     * If newClass is not in 
     * java.lang.Boolean
     * java.lang.Byte
     * java.util.Date
     * java.sql.Timestamp
     * java.sql.Time
     * java.lang.Double
     * java.lang.Float
     * java.lang.Integer
     * java.lang.Long
     * java.lang.Short
     * java.math.BigDecimal
     * java.lang.Number
     * java.lang.String
     * the method trys to find something of similar using getSuperClass
     * it is used.
     *
     * In no solution is find, the type is set to java.lang.String an optionally is possible
     * change the expression with something like ""+<exp>. This should be very rare...
     * @param classExpression New value of property classExpression.
     * 
     */
    public static void setMatchingClassExpression(JRDesignExpression expression, java.lang.String newClassName, boolean adjustExpression) {
        
        if (!acceptedTextfieldClasses.contains(newClassName))
        {
            try {
                Class newClass = IReportManager.getInstance().getReportClassLoader().loadClass( newClassName );
                setMatchingClassExpression(expression, newClass, adjustExpression);
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            expression.setValueClassName(newClassName);
        }
    }
    
    private static void setMatchingClassExpression(JRDesignExpression expression, Class newClass, boolean adjustExpression) {
        
        // try with the superclasss..
        Class superClass = newClass.getSuperclass();

        if (superClass == null)
        {
            expression.setValueClassName("java.lang.String");
            if (adjustExpression)
            {
                expression.setText("\"\"+" + expression.getText());
            }
        }
        else
        {
            setMatchingClassExpression(expression, superClass.getName(), adjustExpression);
        }
    }

    @Override
    public JRDesignElement[] createReportElements(JasperDesign jd) {
        return new JRDesignElement[]{createReportElement(jd)};
    }

}
