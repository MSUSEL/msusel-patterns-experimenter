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
package com.jaspersoft.ireport.designer.undo;

import com.jaspersoft.ireport.designer.ModelUtils;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class BandChangeUndoableEdit  extends AggregatedUndoableEdit {

    private JRDesignBand oldBand = null;
    private JRDesignBand newBand = null;
    private JRDesignElement element = null;
    private JasperDesign jasperDesign = null;

    public BandChangeUndoableEdit(JasperDesign jasperDesign,
                                  JRDesignBand oldBand,
                                  JRDesignBand newBand,
                                  JRDesignElement element)
    {
        this.oldBand = oldBand;
        this.newBand = newBand;
        this.element = element;
        this.jasperDesign = jasperDesign;
    }

    @Override
    public void undo() throws CannotUndoException {

        super.undo();

        if (oldBand != null && newBand != oldBand)
        {
            int y1 = ModelUtils.getBandLocation(newBand, getJasperDesign());
            int y0 = ModelUtils.getBandLocation(oldBand, getJasperDesign());

            int deltaBand = y0 - y1;
            // Update element band...
            newBand.getChildren().remove(getElement());
            //oldBand.removeElement(dew.getElement());

            // Update the element coordinates...
            getElement().setElementGroup(oldBand);
            getElement().setY(getElement().getY() - deltaBand);
            oldBand.getChildren().add(getElement());
            newBand.getEventSupport().firePropertyChange(JRDesignBand.PROPERTY_CHILDREN, null, null);
            oldBand.getEventSupport().firePropertyChange(JRDesignBand.PROPERTY_CHILDREN, null, null);
        }

    }

    @Override
    public void redo() throws CannotRedoException {

        super.redo();

        if (newBand != null && newBand != oldBand)
        {
            int y1 = ModelUtils.getBandLocation(newBand, getJasperDesign());
            int y0 = ModelUtils.getBandLocation(oldBand, getJasperDesign());

            int deltaBand = y0 - y1;
            // Update element band...
            oldBand.getChildren().remove(getElement());
            //oldBand.removeElement(dew.getElement());

            // Update the element coordinates...
            getElement().setElementGroup(newBand);
            getElement().setY(getElement().getY() + deltaBand);
            newBand.getChildren().add(getElement());
            newBand.getEventSupport().firePropertyChange(JRDesignBand.PROPERTY_CHILDREN, null, null);
            oldBand.getEventSupport().firePropertyChange(JRDesignBand.PROPERTY_CHILDREN, null, null);
        }

    }

    @Override
    public String getPresentationName() {
        return "Element band change";
    }



    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    public void setJasperDesign(JasperDesign jasperDesign) {
        this.jasperDesign = jasperDesign;
    }

    /**
     * @return the oldBand
     */
    public JRDesignBand getOldBand() {
        return oldBand;
    }

    /**
     * @param oldBand the oldBand to set
     */
    public void setOldBand(JRDesignBand oldBand) {
        this.oldBand = oldBand;
    }

    /**
     * @return the newBand
     */
    public JRDesignBand getNewBand() {
        return newBand;
    }

    /**
     * @param newBand the newBand to set
     */
    public void setNewBand(JRDesignBand newBand) {
        this.newBand = newBand;
    }

    /**
     * @return the element
     */
    public JRDesignElement getElement() {
        return element;
    }

    /**
     * @param element the element to set
     */
    public void setElement(JRDesignElement element) {
        this.element = element;
    }
}
