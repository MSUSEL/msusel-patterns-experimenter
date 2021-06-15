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
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class CreatePageNumberTextfieldAction extends CreateTextFieldAction {

    @Override
    public JRDesignElement createReportElement(JasperDesign jd)
    {
        JRDesignTextField element = (JRDesignTextField)super.createReportElement( jd );

        ((JRDesignExpression)element.getExpression()).setText("$V{PAGE_NUMBER}");
        ((JRDesignExpression)element.getExpression()).setValueClassName("java.lang.Integer");

        setMatchingClassExpression(
            ((JRDesignExpression)element.getExpression()),
            "java.lang.Integer",
            true
            );

        return element;
    }

    /*
    public static PaletteItem createPaletteItem()
    {
        Properties props = new Properties();
        props.setProperty(PaletteItem.ACTION ,CreatePageNumberTextfieldAction.class.getName());
        props.setProperty(PaletteItem.PROP_ID , "PageNumber");
        props.setProperty(PaletteItem.PROP_NAME , "Page #");
        props.setProperty(PaletteItem.PROP_COMMENT , "Creates a textfield to display the page number");
        props.setProperty(PaletteItem.PROP_ICON16,"com/jaspersoft/ireport/designer/resources/textfield-16.png");
        props.setProperty(PaletteItem.PROP_ICON32,"com/jaspersoft/ireport/designer/resources/textfield-32.png");

        try {
            FileObject selectedPaletteFolder = Misc.createFolders("ireport/palette/tools");

            // We create the card if it does not exist:

            FileObject toolFile = selectedPaletteFolder.getFileObject(
                "PageNumber","irpitem");
            if (toolFile==null) {
                toolFile = selectedPaletteFolder.createData(
                     "PageNumber","irpitem");
            }
            FileLock lock = toolFile.lock();
            OutputStream out = toolFile.getOutputStream(lock);
    // Write the icon that the user selected
    // to the card file:
            props.store(out, "Tool PageNumber");
            out.close();
            lock.releaseLock();
        } catch (IOException ex) {
                ex.printStackTrace();
        }

        return null;
    }
     */
}
