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
import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.palette.PaletteItemAction;
import com.jaspersoft.ireport.designer.tools.AggregationFunctionDialog;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Point;
import java.awt.dnd.DropTargetDropEvent;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.convert.StaticTextConverter;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.netbeans.api.visual.widget.Scene;
import org.openide.util.Exceptions;

/**
 *
 * @author gtoffoli
 */
@SuppressWarnings("unchecked")
public class CreateTextFieldFromFieldAction extends CreateTextFieldAction {

    @Override
    public JRDesignElement createReportElement(JasperDesign jasperDesign)
    {
        JRDesignTextField element = (JRDesignTextField)super.createReportElement( jasperDesign );

        JRDesignField field = (JRDesignField)getPaletteItem().getData();

        ((JRDesignExpression)element.getExpression()).setText("$F{"+ field.getName() + "}");
        setMatchingClassExpression(
            ((JRDesignExpression)element.getExpression()), 
            field.getValueClassName(), 
            true
            );

        return element;
    }

    public boolean isNumeric(String type)
    {
        return type!=null && (type.equals("java.lang.Byte") ||
            type.equals("java.lang.Short") ||
            type.equals("java.lang.Integer") ||
            type.equals("java.lang.Long") ||
            type.equals("java.lang.Float") ||
            type.equals("java.lang.Double") ||
            type.equals("java.lang.Number") ||
            type.equals("java.math.BigDecimal"));
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {

        JRDesignTextField element = (JRDesignTextField)createReportElement(getJasperDesign());

        if (element == null) return;
        // Find location...
        dropFieldElementAt(getScene(), getJasperDesign(), element, dtde.getLocation());
    }

    public void dropFieldElementAt(Scene theScene, JasperDesign jasperDesign, JRDesignTextField element, Point location)
    {

        JRDesignField newField = (JRDesignField)getPaletteItem().getData();

        JRDesignStaticText label = null; // If label != null, add the label too...
        Point labelLocation = null;

        if (theScene instanceof ReportObjectScene)
        {
            Point p = theScene.convertViewToScene( location );

            // find the band...
            JRDesignBand b = ModelUtils.getBandAt(jasperDesign, p);
            int yLocation = ModelUtils.getBandLocation(b, jasperDesign);
            Point pLocationInBand = new Point(p.x - jasperDesign.getLeftMargin(),
                                              p.y - yLocation);
            if (b != null)
            {

                // if the band is not a detail, propose to aggregate the value...
                if (b.getOrigin().getBandType() == JROrigin.GROUP_FOOTER ||
                    b.getOrigin().getBandType() == JROrigin.GROUP_HEADER ||
                    b.getOrigin().getBandType() == JROrigin.COLUMN_FOOTER ||
                    b.getOrigin().getBandType() == JROrigin.COLUMN_HEADER ||
                    b.getOrigin().getBandType() == JROrigin.PAGE_FOOTER ||
                    b.getOrigin().getBandType() == JROrigin.PAGE_HEADER ||
                    b.getOrigin().getBandType() == JROrigin.TITLE ||
                    b.getOrigin().getBandType() == JROrigin.SUMMARY)
                {
                    AggregationFunctionDialog dialog = new AggregationFunctionDialog(Misc.getMainFrame(), true);
                    dialog.setFunctionSet( ( isNumeric( newField.getValueClassName())) ? AggregationFunctionDialog.NUMERIC_SET : AggregationFunctionDialog.STRING_SET );

                    dialog.setDefaultSelection(AggregationFunctionDialog.DEFAULT_AS_VALUE);
                    dialog.setVisible(true);

                    Byte aggFunc = dialog.getSelectedFunction();
                    if (aggFunc != null)
                    {
                        // create the variable...
                        JRDesignVariable var = new JRDesignVariable();
                        var.setName(newField.getName());
                        var.setCalculation(aggFunc);
                        var.setExpression(Misc.createExpression( newField.getValueClassName() , "$F{" + newField.getName() + "}"));

                        if (aggFunc.equals(JRVariable.CALCULATION_COUNT) ||
                            aggFunc.equals(JRVariable.CALCULATION_DISTINCT_COUNT))
                        {
                            var.setValueClassName("java.lang.Integer");
                            element.setPattern(null);
                        }
                        else
                        {
                            var.setValueClassName(newField.getValueClassName());
                        }

                        if (b.getOrigin().getBandType() == JROrigin.SUMMARY ||
                            b.getOrigin().getBandType() == JROrigin.TITLE)
                        {
                            var.setResetType(JRVariable.RESET_TYPE_REPORT);
                        }
                        else if (b.getOrigin().getBandType() == JROrigin.GROUP_FOOTER ||
                            b.getOrigin().getBandType() == JROrigin.GROUP_HEADER)
                        {
                            var.setResetType(JRVariable.RESET_TYPE_GROUP);
                            var.setResetGroup( (JRGroup) jasperDesign.getGroupsMap().get( b.getOrigin().getGroupName() ) );
                        }
                        else if (b.getOrigin().getBandType() == JROrigin.COLUMN_FOOTER ||
                                 b.getOrigin().getBandType() == JROrigin.COLUMN_HEADER)
                        {
                            var.setResetType(JRVariable.RESET_TYPE_COLUMN);
                        }
                        else if (b.getOrigin().getBandType() == JROrigin.PAGE_HEADER ||
                                 b.getOrigin().getBandType() == JROrigin.PAGE_FOOTER)
                        {
                            var.setResetType(JRVariable.RESET_TYPE_PAGE);
                        }

                        try {
                            int i=1;
                            // Find the first available var...
                            while (jasperDesign.getVariablesMap().containsKey(var.getName()+"_"+i))
                            {
                                ++i;
                            }
                            var.setName(var.getName()+"_"+i);
                            jasperDesign.addVariable(var);
                        } catch (JRException ex) {
                            Exceptions.printStackTrace(ex);
                        }

                        JRDesignExpression exp = new JRDesignExpression();
                        exp.setText("$V{" + var.getName() + "}");
                        element.setExpression(exp);
                        CreateTextFieldFromFieldAction.setMatchingClassExpression(
                        ((JRDesignExpression)element.getExpression()),
                        var.getValueClassName(),
                        true);

                        if (b.getOrigin().getBandType() == JROrigin.GROUP_HEADER)
                        {
                            element.setEvaluationTime(JRExpression.EVALUATION_TIME_GROUP);
                            element.setEvaluationGroup( (JRGroup) jasperDesign.getGroupsMap().get( b.getOrigin().getGroupName() ) );
                        }
                        else if (b.getOrigin().getBandType() == JROrigin.COLUMN_HEADER)
                        {
                            element.setEvaluationTime(JRExpression.EVALUATION_TIME_COLUMN);
                        }
                        else if (b.getOrigin().getBandType() == JROrigin.PAGE_HEADER)
                        {
                            element.setEvaluationTime(JRExpression.EVALUATION_TIME_PAGE);
                        }
                        else if (b.getOrigin().getBandType() == JROrigin.TITLE)
                        {
                            element.setEvaluationTime(JRExpression.EVALUATION_TIME_REPORT);
                        }
                    }
                }
                else if (b.getOrigin().getBandType() == JROrigin.DETAIL)
                {
                    // Check if the column band exists and it is not zero height...
                    if (getJasperDesign().getColumnHeader() != null &&
                        getJasperDesign().getColumnHeader().getHeight() >= 20 &&
                        IReportManager.getPreferences().getBoolean("createLabelForField", true))
                    {
                        // Add a label for this textfield...
                        label = new JRDesignStaticText( getJasperDesign() );
                        if (newField.getDescription() != null &&
                            newField.getDescription().trim().length() > 0)
                        {
                            label.setText(newField.getDescription());
                        }
                        else
                        {
                            label.setText(newField.getName());
                        }
                        label.setWidth(100);
                        label.setHeight(20);
                        
                        int y = ModelUtils.getBandLocation(getJasperDesign().getColumnHeader(), getJasperDesign());
                        
                        labelLocation = theScene.convertSceneToView(new Point(0,y+2));
                        labelLocation.x = location.x;
                    }

                }
            }

        }

        super.dropElementsAt(theScene, jasperDesign, new JRDesignElement[]{element}, location);
        
        if (label != null && labelLocation != null)
        {
            super.dropElementsAt(theScene, jasperDesign, new JRDesignElement[]{label}, labelLocation);
        }
    }

    @Override
    public void adjustElement(JRDesignElement[] elements, int index, Scene theScene, JasperDesign jasperDesign, Object parent, Point dropLocation) {

        if (elements[index] instanceof JRDesignStaticText)
        {
            elements[index].setY(0);
        }

        super.adjustElement(elements, index, theScene, jasperDesign, parent, dropLocation);
    }



}
