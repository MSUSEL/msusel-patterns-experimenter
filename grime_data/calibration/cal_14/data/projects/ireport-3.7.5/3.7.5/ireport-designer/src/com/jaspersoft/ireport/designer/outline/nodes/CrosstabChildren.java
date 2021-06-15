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

import com.jaspersoft.ireport.designer.ModelUtils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRCrosstabOrigin;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author gtoffoli
 */
public class CrosstabChildren  extends Index.KeysChildren implements PropertyChangeListener {

    Lookup doLkp = null;
    
    
    private JRDesignCrosstab crosstab = null;
    private JasperDesign jd = null;
    
    @SuppressWarnings("unchecked")
    public CrosstabChildren(JasperDesign jd, JRDesignCrosstab crosstab, Lookup doLkp) {
        super(new ArrayList());
        this.doLkp = doLkp;
        this.jd=jd;
        this.crosstab = crosstab;
        this.crosstab.getEventSupport().addPropertyChangeListener(this);
    }
    
    @SuppressWarnings("unchecked")
    public void recalculateKeys() {
        
        List l = (List)lock();
        l.clear();
        
        //AbstractNode parameters = new AbstractNode(doLkp);
        //parameters.setName("Parameters");
        l.add(JRDesignCrosstab.PROPERTY_PARAMETERS);
        
        //AbstractNode rows = new AbstractNode(Children.LEAF,doLkp);
        //rows.setName("Row groups");
        l.add(JRDesignCrosstab.PROPERTY_ROW_GROUPS);
        
        //AbstractNode columns = new AbstractNode(Children.LEAF,doLkp);
        //columns.setName("Column groups");
        l.add(JRDesignCrosstab.PROPERTY_COLUMN_GROUPS);
        
        //AbstractNode measures = new AbstractNode(Children.LEAF, doLkp);
        //measures.setName("Measures");
        l.add(JRDesignCrosstab.PROPERTY_MEASURES);
        
        if (crosstab.getHeaderCell() == null)
        {
            // Add a null cell here
            NullCell cell = new NullCell(new JRCrosstabOrigin( crosstab, JRCrosstabOrigin.TYPE_HEADER_CELL ));
            l.add( cell );
        }
        else
        {
            l.add(crosstab.getHeaderCell());
        }
        
        // Add all the columns...
        JRCrosstabColumnGroup[] columns = crosstab.getColumnGroups();
        for (int i=0; i < columns.length; ++i)
        {
            if ( columns[i].getHeader() == null)
            {
                NullCell cell = new NullCell(new JRCrosstabOrigin( crosstab, JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER, null, columns[i].getName() ));
                l.add( cell );
            }
            else
            {
                l.add( columns[i].getHeader() );
            }
            
            if ( columns[i].getHeader() == null)
            {
                NullCell cell = new NullCell(new JRCrosstabOrigin( crosstab, JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER, null, columns[i].getName() ));
                l.add( cell );
            }
            else
            {
                l.add( columns[i].getTotalHeader());
            }
            
        }
        // Add all the rows...
        JRCrosstabRowGroup[] rows = crosstab.getRowGroups();
        for (int i=0; i < rows.length; ++i)
        {
            if ( rows[i].getHeader() == null)
            {
                NullCell cell = new NullCell(new JRCrosstabOrigin( crosstab, JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER, rows[i].getName(), null));
                l.add( cell );
            }
            else
            {
                l.add( rows[i].getHeader() );
            }
            
            if ( rows[i].getHeader() == null)
            {
                NullCell cell = new NullCell(new JRCrosstabOrigin( crosstab, JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER, rows[i].getName(), null ));
                l.add( cell );
            }
            else
            {
                l.add( rows[i].getTotalHeader());
            }
        }
        
        // We should use the crosstab.getCells() to have the cells ordered..
        // but the matrix could be not updated...
        
//        JRCrosstabCell[][] cells = crosstab.getCells();
//        for (int i = cells.length-1; i>=0; --i)
//        {
//            for (int j = cells[i].length-1; j>=0; --j)
//            {
//                if (cells[i][j].getContents() == null)
//                {
//                    String rowGrp = (i < rows.length) ? rows[i].getName() : null;
//                    String colGrp = (j < columns.length) ? columns[j].getName() : null;
//                    
//                    NullCell cell = new NullCell(new JRCrosstabOrigin( crosstab, JRCrosstabOrigin.TYPE_DATA_CELL, rowGrp, colGrp ));
//                    l.add( cell );
//                }
//                else
//                {
//                    l.add( cells[i][j].getContents() );
//                }
//            }
//        }
        
        List cells = crosstab.getCellsList();
        for (int i = 0; i<cells.size(); ++i)
        {
            JRCrosstabCell theCell = (JRCrosstabCell) cells.get(i);
            if (theCell.getContents() == null)
            {
                   NullCell cell = new NullCell(
                        new JRCrosstabOrigin( crosstab, JRCrosstabOrigin.TYPE_DATA_CELL, 
                                theCell.getRowTotalGroup(),
                                theCell.getColumnTotalGroup()));
                l.add( cell );
            }
            else
            {
                l.add( theCell.getContents() );
            }
        }
        
        update();
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        recalculateKeys();
    }
    
    @Override
    protected Node[] createNodes(Object key) {
        
        AbstractNode a = new AbstractNode(Children.LEAF,doLkp);
        a.setName(key+"");
        
        if (key instanceof String && key != null && key.equals( JRDesignCrosstab.PROPERTY_PARAMETERS))
        {
            return new Node[]{new CrosstabParametersNode(jd, crosstab, doLkp)};
        }
        else if (key instanceof String && key != null && key.equals( JRDesignCrosstab.PROPERTY_ROW_GROUPS))
        {
            return new Node[]{new CrosstabGroupsNode(jd, crosstab, doLkp, CrosstabGroupNode.ROW_GROUP)};
        }
        else if (key instanceof String && key != null && key.equals( JRDesignCrosstab.PROPERTY_COLUMN_GROUPS))
        {
            return new Node[]{new CrosstabGroupsNode(jd, crosstab, doLkp, CrosstabGroupNode.COLUMN_GROUP)};
        }
        else if (key instanceof String && key != null && key.equals( JRDesignCrosstab.PROPERTY_MEASURES))
        {
            return new Node[]{new CrosstabMeasuresNode(jd, crosstab, doLkp)};
        }
        if (key instanceof JRDesignCellContents)
        {
            CellNode node = new CellNode(jd,crosstab,(JRDesignCellContents)key, doLkp);
            return new Node[]{node};
        }
        else if (key instanceof NullCell)
        {
            NullCellNode node = new NullCellNode(jd, (NullCell)key, crosstab, doLkp);
            return new Node[]{node};
        }
        return new Node[]{a};
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
    }
    

}
