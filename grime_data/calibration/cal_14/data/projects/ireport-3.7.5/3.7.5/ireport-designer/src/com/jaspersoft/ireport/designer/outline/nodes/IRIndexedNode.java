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

import javax.swing.JPanel;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class IRIndexedNode extends IRAbstractNode {

    /** Index implementation */
    private Index indexImpl;

   
    /** Allows subclasses to provide their own children and
    * index handling.
    * @param children Lookup...
    * @param children the children implementation
    * @param indexImpl the index implementation
    */
    protected IRIndexedNode(Children children, Index indexImpl, Lookup lookup) {
        super(children, lookup);
        this.indexImpl = indexImpl;
    }

    /*
    * @return false to signal that the customizer should not be used.
    *  Subclasses can override this method to enable customize action
    *  and use customizer provided by this class.
    */
    @Override
    public boolean hasCustomizer() {
        return indexImpl != null;
    }

    /* Returns the customizer component.
    * @return the component
    */
    @Override
    @SuppressWarnings("deprecation")
    public java.awt.Component getCustomizer() {
        
        java.awt.Container c = new JPanel();
        if (indexImpl != null)
        {
            org.openide.nodes.IndexedCustomizer customizer = new org.openide.nodes.IndexedCustomizer();
            customizer.setObject(indexImpl);
        }
        return c;
    }

    /** Get a cookie.
    * @param clazz representation class
    * @return the index implementation or children if these match the cookie class,
    * else using the superclass cookie lookup
    */
    @Override
    public <T extends Node.Cookie> T getCookie(Class<T> clazz) {
        if (indexImpl != null && clazz.isInstance(indexImpl)) {
            // ok, Index implementor is enough
            return clazz.cast(indexImpl);
        }

        Children ch = getChildren();

        if (clazz.isInstance(ch)) {
            // ok, children are enough
            return clazz.cast(ch);
        }

        return super.getCookie(clazz);
    }
    
}
