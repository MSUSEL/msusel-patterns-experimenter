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
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author gtoffoli
 */
class DatasetChildren extends Children.Keys {

    JasperDesign jd = null;
    JRDesignDataset dataset = null;
    Lookup doLkp = null;
    
    public DatasetChildren(JasperDesign jd, JRDesignDataset dataset, Lookup doLkp) {
        this.jd = jd;
        this.doLkp = doLkp;
        this.dataset = dataset;
    }
        
    protected Node[] createNodes(Object key) {
        
        if (key.equals("parameters"))
        {
            return new Node[]{new ParametersNode(jd, dataset, doLkp)};
        }
        else if (key.equals("fields"))
        {
            return new Node[]{new FieldsNode(jd, dataset, doLkp)};
        }
        else if (key.equals("variables"))
        {
            return new Node[]{new VariablesNode(jd, dataset, doLkp)};
        }
        else if (key.equals("groups"))
        {
            return new Node[]{new GroupsNode(jd, dataset, doLkp)};
        }
        
        AbstractNode node = new AbstractNode(LEAF, Lookups.singleton(key));
        node.setName(key+"");
        return new Node[]{node};
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void addNotify() {
        super.addNotify();
        ArrayList children = new ArrayList();
        children.add("parameters");
        children.add("fields");
        children.add("variables");
        children.add("groups");
        setKeys(children);
    }

}
