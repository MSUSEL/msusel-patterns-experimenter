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
package com.jaspersoft.ireport.components.table.nodes;

import com.jaspersoft.ireport.components.table.TableCell;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class TableChildren extends Index.KeysChildren {

    private JRDesignComponentElement component = null;
    private JasperDesign jd = null;
    private Lookup doLkp = null;

    
    @SuppressWarnings("unchecked")
    public TableChildren(JasperDesign jd, JRDesignComponentElement component, Lookup doLkp) {
        super(new ArrayList());
        this.jd = jd;
        this.component = component;
        this.doLkp = doLkp;
    }


    @Override
    protected void addNotify() {
        super.addNotify();
        recalculateKeys();
    }

    @SuppressWarnings("unchecked")
    public void recalculateKeys() {
        
        List l = (List)lock();
        l.clear();
        if (component.getComponent() instanceof StandardTable)
        {
            StandardTable table = (StandardTable)component.getComponent();
            l.add(new TableSectionNode(jd, component, TableCell.TABLE_HEADER, null, doLkp));
            l.add(new TableSectionNode(jd, component, TableCell.COLUMN_HEADER, null, doLkp));

            JRDesignDataset dataset = jd.getMainDesignDataset();
            if (table.getDatasetRun() != null && table.getDatasetRun().getDatasetName() != null)
            {
                dataset = (JRDesignDataset) jd.getDatasetMap().get(table.getDatasetRun().getDatasetName());
            }
            JRGroup[] groups = dataset.getGroups();
            for (JRGroup group : groups)
            {
                l.add(new TableSectionNode(jd, component, TableCell.GROUP_HEADER, (JRDesignGroup) group, doLkp));
            }
            l.add(new TableSectionNode(jd, component, TableCell.DETAIL, null, doLkp));
            for (JRGroup group : groups)
            {
                l.add(new TableSectionNode(jd, component, TableCell.GROUP_FOOTER, (JRDesignGroup)group, doLkp));
            }
            l.add(new TableSectionNode(jd, component, TableCell.COLUMN_FOOTER, null, doLkp));
            l.add(new TableSectionNode(jd, component, TableCell.TABLE_FOOTER, null, doLkp));


        }
        update();
    }

    protected Node[] createNodes(Object key)
    {
        Node node = null;
        if (key instanceof Node)
        {
            node = (Node)key;
        }

        if (node != null)
            return new Node[]{node};


        return new Node[]{};
    }


}
