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
package com.jaspersoft.ireport.addons.layers;

import java.beans.PropertyChangeSupport;

/**
 *
 * @version $Id: Layer.java 0 2010-02-25 15:31:45 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class Layer {

    public static final String PROPERTY_NAME = "PROPERTY_NAME";
    public static final String PROPERTY_VISIBLE = "PROPERTY_VISIBLE";
    public static final String PROPERTY_PRINT_WHEN_EXPRESSION = "PROPERTY_PRINT_WHEN_EXPRESSION";

    private PropertyChangeSupport eventSupport = null;

    private int id = 0;
    private String name = "Layer";
    private boolean visible = true;
    private String printWhenExpression = null;

    private boolean backgroundLayer = false;

    public Layer()
    {
        eventSupport = new PropertyChangeSupport(this);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        getEventSupport().firePropertyChange(PROPERTY_NAME, oldName, this.name );
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        
        boolean oldVisible = this.visible;
        this.visible = visible;
        getEventSupport().firePropertyChange(PROPERTY_VISIBLE, oldVisible, this.visible );
    }

    /**
     * @return the backgroundLayer
     */
    public boolean isBackgroundLayer() {
        return backgroundLayer;
    }

    /**
     * @param backgroundLayer the backgroundLayer to set
     */
    public void setBackgroundLayer(boolean backgroundLayer) {
        this.backgroundLayer = backgroundLayer;
    }

    /**
     * @return the eventSupport
     */
    public PropertyChangeSupport getEventSupport() {
        return eventSupport;
    }

    /**
     * @return the printWhenExpression
     */
    public String getPrintWhenExpression() {
        return printWhenExpression;
    }

    /**
     * @param printWhenExpression the printWhenExpression to set
     */
    public void setPrintWhenExpression(String newPrintWhenExpression) {
        String oldPrintWhenExpression = this.printWhenExpression;
        this.printWhenExpression = newPrintWhenExpression;
        getEventSupport().firePropertyChange(PROPERTY_PRINT_WHEN_EXPRESSION, oldPrintWhenExpression, this.printWhenExpression );
    }


}
