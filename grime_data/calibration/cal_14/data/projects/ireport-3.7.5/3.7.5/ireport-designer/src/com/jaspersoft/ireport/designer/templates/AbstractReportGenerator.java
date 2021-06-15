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
package com.jaspersoft.ireport.designer.templates;

import com.jaspersoft.ireport.designer.utils.Misc;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;

/**
 *
 * @author gtoffoli
 */
public abstract class AbstractReportGenerator implements ReportGenerator {

    abstract public FileObject generateReport(WizardDescriptor descriptor);
    
    /**
     * Find in band a JRDesignStaticText element having exp as text.
     * @param band
     * @param exp
     * @return the first matching element or null.
     */
    public static  JRDesignStaticText findStaticTextElement(JRElementGroup parent, String exp)
    {
        JRElement[] elements = parent.getElements();
        for (int i=0; i<elements.length; ++i)
        {
            JRElement ele = elements[i];
            if (ele instanceof JRDesignStaticText)
            {
                JRDesignStaticText st = (JRDesignStaticText)ele;
                if (st.getText() != null &&
                    st.getText().equalsIgnoreCase(exp))
                {
                    return st;
                }
            }
            else if (ele instanceof JRElementGroup)
            {
                JRDesignStaticText ele2 = findStaticTextElement((JRElementGroup)ele, exp);
                if (ele2 != null) return ele2;
            }
        }
        return null;
    }
    
    /**
     * Find in band a JRDesignTextField element having exp as expression value.
     * @param band
     * @param exp
     * @return the first matching element or null.
     */
    public static JRDesignTextField findTextFieldElement(JRElementGroup band, String exp)
    {
        JRElement[] elements = band.getElements();
        for (int i=0; i<elements.length; ++i)
        {
            JRElement ele = elements[i];
            if (ele instanceof JRDesignTextField)
            {
                String s = Misc.getExpressionText(((JRDesignTextField)ele).getExpression());
                if (s.startsWith("\""))
                {
                    s = s.substring(1);
                }
                if (s.endsWith("\""))
                {
                    s = s.substring(0, s.length()-1);
                }
                if (s.equalsIgnoreCase(exp)) return (JRDesignTextField) ele;
            }
            else if (ele instanceof JRElementGroup)
            {
                JRDesignTextField ele2 = findTextFieldElement((JRElementGroup)ele, exp);
                if (ele2 != null) return ele2;
            }
        }
        return null;
    }
    

}
