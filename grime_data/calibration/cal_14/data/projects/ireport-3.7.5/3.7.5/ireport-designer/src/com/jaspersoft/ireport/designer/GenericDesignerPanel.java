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
package com.jaspersoft.ireport.designer;

import javax.swing.Icon;
import javax.swing.JComponent;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: GenericDesignerPanel.java 0 2010-03-12 14:38:24 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public interface GenericDesignerPanel {

    public void setJasperDesign(JasperDesign jd);

    /**
     * If this design panel uses a Scene, this method is used to synchronize objects selection.
     * The method should return null otherwise.
     * 
     * @return
     */
    public AbstractReportObjectScene getScene();

    /**
     * In this design panel refers to a specific element container, this method is used
     * to get it. The method should return null otherwise.
     * @return
     */
    public JRDesignElement getElement();


    /**
     * The component of this designer.
     * @return
     */
    public JComponent getComponent();


    /**
     * Get label used in the tab button (i.e.  "My element {0}")
     * {0} is replaced with the element index
     * {1} is replaced with the element key
     * @return
     */
    public String getLabel();

    /**
     * The icon used in the tab button
     */
    public Icon getIcon();

    /**
     * Returns the elements dataset of the specified element.
     * If the element is not an element of the scene, returns null;
     * 
     * @param element
     * @return
     */
    public JRDesignDataset getElementDataset(JRDesignElement element);


}
