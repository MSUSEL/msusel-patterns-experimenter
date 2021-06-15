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
package com.jaspersoft.ireport.designer.palette.actions.tools;

import com.jaspersoft.ireport.designer.palette.actions.*;
import com.jaspersoft.ireport.designer.utils.Misc;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.Exceptions;

/**
 *
 * @author gtoffoli
 */
public class CreatePercentageTextfieldAction extends CreateTextFieldAction {

    @Override
    public JRDesignElement createReportElement(JasperDesign jd)
    {
        JRDesignTextField element = (JRDesignTextField)super.createReportElement( jd );

        FieldPercentageDialog dialog = new FieldPercentageDialog(Misc.getMainFrame());
        dialog.setJasperDesign(jd);

        dialog.setVisible(true);
        if (dialog.getDialogResult() == JOptionPane.OK_OPTION)
        {
            JRField f = dialog.getSelectedField();
            Byte resetType = dialog.getSelectedResetType();
            JRGroup group = null;
            if (resetType == JRVariable.RESET_TYPE_GROUP)
            {
                group = dialog.getSelectedGroup();
            }

            // Let's create the variable...

            JRDesignVariable variable = new JRDesignVariable();
            for (int i = 0; ; ++i)
            {
                String vname = f.getName() + "_SUM";
                if (i > 0) vname += "_" + i;

                if (jd.getVariablesMap().containsKey(vname))
                {
                    continue;
                }

                variable.setName(vname);
                break;
            }

            variable.setExpression( Misc.createExpression( f.getValueClassName(), "$F{" + f.getName() + "}" ));
            variable.setValueClassName( f.getValueClassName() );
            variable.setCalculation( JRVariable.CALCULATION_SUM);
            variable.setResetType(resetType);
            if (resetType == JRVariable.RESET_TYPE_GROUP)
            {
                variable.setResetGroup(group);
            }
            try {
                jd.addVariable(variable);
            } catch (JRException ex) {
                ex.printStackTrace();
            }

            ((JRDesignExpression)element.getExpression()).setText("new Double( $F{" + f.getName() + "}.doubleValue() / $V{"+ variable.getName() + "}.doubleValue() )");
            ((JRDesignExpression)element.getExpression()).setValueClassName("java.lang.Double");

            element.setPattern( "#,##0.00%" );
            setMatchingClassExpression(
            ((JRDesignExpression)element.getExpression()),
            ((JRDesignExpression)element.getExpression()).getValueClassName(),true);

            element.setEvaluationTime(JRExpression.EVALUATION_TIME_AUTO);

            return element;
        }

        return null;
    }
}
