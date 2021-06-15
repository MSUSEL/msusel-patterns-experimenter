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
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class ReportChildren extends Children.Keys {

    JasperDesign jd = null;
    Lookup doLkp = null;
    
    public ReportChildren(JasperDesign jd, Lookup doLkp) {
        this.jd = jd;
        this.doLkp = doLkp;
    }
    
    protected Node[] createNodes(Object key) {

        if (key.equals("styles"))
        {
            return new Node[]{new StylesNode(jd,doLkp)};
        }
        else if (key.equals("parameters"))
        {
            return new Node[]{new ParametersNode(jd,doLkp)};
        }
        else if (key.equals("fields"))
        {
            return new Node[]{new FieldsNode(jd, jd.getMainDesignDataset(),doLkp)};
        }
        else if (key.equals("variables"))
        {
            return new Node[]{new VariablesNode(jd, jd.getMainDesignDataset(),doLkp)};
        }
        else if (key.equals("scriptlets"))
        {
            return new Node[]{new ScriptletsNode(jd,doLkp)};
        }
        else if (key instanceof JRDesignDataset)
        {
            return new Node[]{new DatasetNode(jd, (JRDesignDataset)key,doLkp)};
        }
        else if (key instanceof JRDesignBand)
        {
            return new Node[]{new BandNode(jd, (JRDesignBand)key,doLkp)};
        }
        else if (key instanceof NullBand)
        {
            return new Node[]{new NullBandNode(jd, (NullBand)key,doLkp)};
        }
        
        AbstractNode node = new IRAbstractNode(LEAF, new ProxyLookup(doLkp, Lookups.singleton(key)));
        node.setName(key+"");
        return new Node[]{node};
    }
    
    @Override
    protected void addNotify() {
        super.addNotify();
        updateChildren();
    }
    
    @SuppressWarnings("unchecked")
    public void updateChildren()
    {
        ArrayList children = new ArrayList();
        children.add("styles");
        children.add("parameters");
        children.add("fields");
        children.add("variables");
        children.add("scriptlets");
        children.addAll( jd.getDatasetsList() );
        //children.addAll( ModelUtils.getBands(jd) );
        children.add( ( jd.getTitle() != null) ? jd.getTitle() : new NullBand(new JROrigin(jd.getName(), JROrigin.TITLE )) );
        children.add( ( jd.getPageHeader() != null) ? jd.getPageHeader() : new NullBand(new JROrigin(jd.getName(), JROrigin.PAGE_HEADER )) );
        children.add( ( jd.getColumnHeader() != null) ? jd.getColumnHeader() : new NullBand(new JROrigin(jd.getName(), JROrigin.COLUMN_HEADER )) );
        // Group headers...
        JRGroup[] groups = jd.getGroups();
        for (int i=0 ;i<groups.length; ++i)
        {
            if (groups[i].getGroupHeaderSection() == null ||
                groups[i].getGroupHeaderSection().getBands().length == 0)
            {
                children.add( new NullBand(new JROrigin(jd.getName(),groups[i].getName(), JROrigin.GROUP_HEADER )));
            }
            else
            {
                JRSection section = groups[i].getGroupHeaderSection();
                JRBand[] bands = section.getBands();
                for (int k=0; k<bands.length; ++k)
                {
                    children.add(bands[k]);
                }
            }
        }
        if (jd.getDetailSection() == null ||
            jd.getDetailSection().getBands().length == 0)
        {
            children.add(new NullBand(new JROrigin(jd.getName(),JROrigin.DETAIL )));
        }
        else
        {
            JRSection section = jd.getDetailSection();
            JRBand[] bands = section.getBands();
            for (int k=0; k<bands.length; ++k)
            {
                children.add(bands[k]);
            }
        }
        //children.add( ( jd.getDetail() != null) ? jd.getDetail() : new NullBand(new JROrigin(jd.getName(), JROrigin.DETAIL )) );

        // Group footers...
        for (int i=groups.length-1; i>=0; --i)
        {
            if (groups[i].getGroupFooterSection() == null ||
                groups[i].getGroupFooterSection().getBands().length == 0)
            {
                children.add( new NullBand(new JROrigin(jd.getName(),groups[i].getName(), JROrigin.GROUP_FOOTER )));
            }
            else
            {
                JRSection section = groups[i].getGroupFooterSection();
                JRBand[] bands = section.getBands();
                for (int k=0; k<bands.length; ++k)
                {
                    children.add(bands[k]);
                }
            }
        }
        children.add( ( jd.getColumnFooter() != null) ? jd.getColumnFooter() : new NullBand(new JROrigin(jd.getName(), JROrigin.COLUMN_FOOTER )) );
        children.add( ( jd.getPageFooter() != null) ? jd.getPageFooter() : new NullBand(new JROrigin(jd.getName(), JROrigin.PAGE_FOOTER )) );
        children.add( ( jd.getLastPageFooter() != null) ? jd.getLastPageFooter() : new NullBand(new JROrigin(jd.getName(), JROrigin.LAST_PAGE_FOOTER )) );
        children.add( ( jd.getSummary() != null) ? jd.getSummary() : new NullBand(new JROrigin(jd.getName(), JROrigin.SUMMARY )) );
        children.add( ( jd.getNoData() != null) ? jd.getNoData() : new NullBand(new JROrigin(jd.getName(), JROrigin.NO_DATA )) );
        children.add( ( jd.getBackground() != null) ? jd.getBackground() : new NullBand(new JROrigin(jd.getName(), JROrigin.BACKGROUND )) );
        
        setKeys(children);
    }
}
