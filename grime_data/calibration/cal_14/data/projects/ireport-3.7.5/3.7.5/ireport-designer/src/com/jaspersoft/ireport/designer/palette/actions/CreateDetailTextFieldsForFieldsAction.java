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
import com.jaspersoft.ireport.designer.ModelUtils;
import java.awt.Point;
import java.awt.dnd.DropTargetDropEvent;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @version $Id: CreateDetailTextFieldsForFields.java 0 2010-09-07 09:05:54 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class CreateDetailTextFieldsForFieldsAction extends CreateTextFieldAction {

    public JRDesignElement[] createElements(JasperDesign jd, boolean labels)
    {

        List<JRDesignField> fields = (List<JRDesignField>)getPaletteItem().getData();
        List<JRDesignElement> elements = new ArrayList<JRDesignElement>();

        int w = jd.getPageWidth() - jd.getLeftMargin() - jd.getRightMargin();
        w /= fields.size();

        for (JRDesignField field : fields)
        {
            JRDesignElement element = null;
            if (!labels)
            {
                element = new JRDesignTextField( jd );
                JRDesignExpression exp = new JRDesignExpression();

                exp.setText("$F{"+ field.getName() + "}");
                exp.setValueClassName(field.getValueClassName());

                ((JRDesignTextField)element).setExpression(exp);

                setMatchingClassExpression(
                    ((JRDesignExpression)((JRDesignTextField)element).getExpression()),
                    field.getValueClassName(),
                true
                );

                ((JRDesignTextField)element).setExpression(exp);
            }
            else
            {
                element = new JRDesignStaticText( jd );
                if (field.getDescription() != null &&
                    field.getDescription().trim().length() > 0)
                {
                    ((JRDesignStaticText)element).setText(field.getDescription());
                }
                else
                {
                    ((JRDesignStaticText)element).setText(field.getName());
                }
            }
            element.setWidth(w);
            element.setHeight(20);
            elements.add(element);
        }

        return elements.toArray(new JRDesignElement[elements.size()]);
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {

        JRDesignElement[] elements = createElements(getJasperDesign(), false);

        if (elements == null || elements.length == 0) return;
        // Find location...
        dropElementsAt(getScene(), getJasperDesign(), elements, dtde.getLocation());

        if (getJasperDesign().getColumnHeader() != null &&
                        getJasperDesign().getColumnHeader().getHeight() >= 20 &&
                        IReportManager.getPreferences().getBoolean("createLabelForField", true))
        {
            int y = ModelUtils.getBandLocation(getJasperDesign().getColumnHeader(), getJasperDesign());

            Point labelLocation = getScene().convertSceneToView(new Point(getJasperDesign().getLeftMargin()+1,y+1));
            dropElementsAt(getScene(), getJasperDesign(), createElements(getJasperDesign(), true), labelLocation);
        }
    }

    @Override
    public void adjustElement(JRDesignElement[] elements, int index, Scene theScene, JasperDesign jasperDesign, Object parent, Point dropLocation) {
        if (index == 0 && elements.length == 1) return;
        
        int newx = (index == 0) ? 0 : elements[index-1].getX() + elements[index-1].getWidth();
        elements[index].setX(newx);
        elements[index].setY(0);
        
        super.adjustElement(elements, index, theScene, jasperDesign, parent, dropLocation);
    }




}
