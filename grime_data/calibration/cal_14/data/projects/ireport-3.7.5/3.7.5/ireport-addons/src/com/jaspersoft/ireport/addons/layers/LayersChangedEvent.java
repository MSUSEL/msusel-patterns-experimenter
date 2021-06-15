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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.addons.layers;

import java.util.List;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: LayersChangedEvent.java 0 2010-02-27 11:11:18 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class LayersChangedEvent {

    /**
     * A new layer as been added to the list.
     * Check for what layer has been added with getChangedLayers
     */
    public static final int LAYERS_ADDED = 1;
    public static final int LAYERS_REMOVED = 2;

    /**
     * The list of layers currently active is changed
     * The new list of layers can be get with LayersSupport.getInstance().getLayers();
     */
    public static final int LAYERS_LIST_CHANGED = 3;

    private List<Layer> changedLayers = null;
    private int type = LAYERS_LIST_CHANGED;
    private JasperDesign jasperDesign = null;

    public LayersChangedEvent(int type, JasperDesign jd, List<Layer> changedLayers)
    {
        this.type = type;
        this.changedLayers = changedLayers;
        this.jasperDesign = jd;
    }

    public LayersChangedEvent(int type,  JasperDesign jd)
    {
        this(type, jd,  null);
    }


    /**
     * This is a shortcut for:
     * LayersSupport.getInstance().getLayers()
     */
    public List<Layer> getLayers()
    {
        return LayersSupport.getInstance().getLayers();
    }

    /**
     * Valid only for events of type LAYERS_ADDED and LAYERS_REMOVED
     * @return the changedLayer
     */
    public List<Layer> getChangedLayers() {
        return changedLayers;
    }

    /**
     * @param changedLayer the changedLayer to set
     */
    public void setChangedLayers(List<Layer> changedLayers) {
        this.changedLayers = changedLayers;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * The current active JasperDesign or null of no jasperdesign is currently active
     * @return the jasperDesign
     */
    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    /**
     * @param jasperDesign the jasperDesign to set
     */
    public void setJasperDesign(JasperDesign jasperDesign) {
        this.jasperDesign = jasperDesign;
    }

}
