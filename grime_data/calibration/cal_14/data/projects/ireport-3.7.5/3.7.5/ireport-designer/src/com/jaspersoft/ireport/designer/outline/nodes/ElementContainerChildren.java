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
package com.jaspersoft.ireport.designer.outline.nodes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRVisitable;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class ElementContainerChildren  extends Index.KeysChildren implements PropertyChangeListener {

    private ElementNodeVisitor visitor = null;
    private JRDesignElementGroup container = null;
    
    private boolean init=false;
    private synchronized void setInit(boolean init)
    {
        this.init = init;
    }
    private synchronized boolean isInit()
    {
        return this.init;
    }
    
    @SuppressWarnings("unchecked")
    protected ElementContainerChildren(JasperDesign jasperDesign, Lookup doLkp) {
        super(new ArrayList());
        this.visitor = new ElementNodeVisitor(jasperDesign, doLkp);
    }
    
    
    @SuppressWarnings("unchecked")
    public ElementContainerChildren(JasperDesign jasperDesign, JRDesignElementGroup container, Lookup doLkp) {
        super(new ArrayList());
        this.visitor = new ElementNodeVisitor(jasperDesign, doLkp);
        this.container = container;
        this.container.getEventSupport().addPropertyChangeListener(this);
    }

    protected Node[] createNodes(Object key) 
    {
        IRIndexedNode node = visitor.getNode((JRVisitable)key);
        
        if (node != null)
            return new Node[]{node};
        
        return new Node[]{};
    }
    
    @Override
    protected void addNotify() {
        super.addNotify();
        recalculateKeys();
    }
    
    
    @SuppressWarnings("unchecked")
    public void recalculateKeys() {
        
        if (container == null) return;
        
        List l = (List)lock();
        l.clear();
        l.addAll(container.getChildren());
        boolean b = isInit();
        setInit(true);
        update();
        setInit(b);
    }
    
    @SuppressWarnings("unchecked")
    public void reorder() { 
            Mutex.Action action = new Mutex.Action(){ 
                public Object run(){ 
                    Index.Support.showIndexedCustomizer(ElementContainerChildren.this.getIndex()); 
                    return null; 
                } 
            }; 
            MUTEX.writeAccess(action); 
        }

    public void propertyChange(PropertyChangeEvent evt) {
        if (isInit()) return;
        
        if (evt.getPropertyName() == null) return;
        if (evt.getPropertyName().equals( JRDesignBand.PROPERTY_CHILDREN))
        {
            recalculateKeys();
        }
    }
}
