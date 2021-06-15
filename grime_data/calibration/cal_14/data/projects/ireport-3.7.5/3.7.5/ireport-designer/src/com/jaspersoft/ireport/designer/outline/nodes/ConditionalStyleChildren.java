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

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class ConditionalStyleChildren extends Index.KeysChildren {

    JasperDesign jd = null;
    private JRDesignConditionalStyle style = null;
    private Lookup doLkp = null;
    
    @SuppressWarnings("unchecked")
    public ConditionalStyleChildren(JasperDesign jd, JRDesignConditionalStyle style, Lookup doLkp) {
        super(new ArrayList());
        this.jd = jd;
        this.doLkp=doLkp;
        this.style=style;
    }

    

    /**
     * This method assumes that all the keys are JRDesignConditionalStyle.
     * 
     * @param key
     * @return
     */
    protected Node[] createNodes(Object key) {
        
        return null;
    }
    
    @Override
    protected void addNotify() {
        super.addNotify();
    }
    
    
    @SuppressWarnings("unchecked")
    public void recalculateKeys() {
        List l = (List)lock();
        l.clear();
        update();
    }

  
    public JRDesignConditionalStyle getStyle() {
        return style;
    }

    public void setStyle(JRDesignConditionalStyle style) {
        this.style = style;
    }

}
