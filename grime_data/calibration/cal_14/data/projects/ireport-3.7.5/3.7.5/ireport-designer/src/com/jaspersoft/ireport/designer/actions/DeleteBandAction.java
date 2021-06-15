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
package com.jaspersoft.ireport.designer.actions;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.outline.nodes.BandNode;
import com.jaspersoft.ireport.designer.undo.DeleteBandUndoableEdit;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

public final class DeleteBandAction extends NodeAction {

    private static DeleteBandAction instance = null;
    
    public static synchronized DeleteBandAction getInstance()
    {
        if (instance == null)
        {
            instance = new DeleteBandAction();
        }
        
        return instance;
    }
    
    private DeleteBandAction()
    {
        super();
    }
    
    
    public String getName() {
        return I18n.getString("DeleteBandAction.Name.CTL_DeleteBandAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    protected void performAction(org.openide.nodes.Node[] activatedNodes) {
        
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (activatedNodes[i] instanceof BandNode)
            {
                BandNode nbn = (BandNode)activatedNodes[i];
                JasperDesign jd = nbn.getJasperDesign();
                JRDesignBand band = nbn.getBand();
                
                if (jd != null && band !=null)
                {
                    
                    if (band.getOrigin().getBandType() == JROrigin.BACKGROUND) jd.setBackground(null);
                    else if (band.getOrigin().getBandType() == JROrigin.TITLE) jd.setTitle(null);
                    else if (band.getOrigin().getBandType() == JROrigin.PAGE_HEADER) jd.setPageHeader(null);
                    else if (band.getOrigin().getBandType() == JROrigin.COLUMN_HEADER) jd.setColumnHeader(null);
                    else if (band.getOrigin().getBandType() == JROrigin.DETAIL)
                    {
                        JRDesignSection section = (JRDesignSection)jd.getDetailSection();
                        section.removeBand(band);
                        //jasperDesign.setDetail(null);
                    }
                    else if (band.getOrigin().getBandType() == JROrigin.COLUMN_FOOTER) jd.setColumnFooter(null);
                    else if (band.getOrigin().getBandType() == JROrigin.PAGE_FOOTER) jd.setPageFooter(null);
                    else if (band.getOrigin().getBandType() == JROrigin.LAST_PAGE_FOOTER) jd.setLastPageFooter(null);
                    else if (band.getOrigin().getBandType() == JROrigin.SUMMARY) jd.setSummary(null);
                    else if (band.getOrigin().getBandType() == JROrigin.NO_DATA) jd.setNoData(null);
                    else if (band.getOrigin().getBandType() == JROrigin.GROUP_HEADER)
                    {
                        JRDesignGroup group = ((JRDesignGroup)jd.getGroupsMap().get(band.getOrigin().getGroupName()));
                        JRDesignSection section = (JRDesignSection)group.getGroupHeaderSection();
                        section.removeBand(band);
                        //  JRDesignGroup g = (JRDesignGroup)jd.getGroupsMap().get( band.getOrigin().getGroupName());
                        //  g.setGroupHeader(null);

                    }
                    else if (band.getOrigin().getBandType() == JROrigin.GROUP_FOOTER)
                    {
                        JRDesignGroup group = ((JRDesignGroup)jd.getGroupsMap().get(band.getOrigin().getGroupName()));
                        JRDesignSection section = (JRDesignSection)group.getGroupFooterSection();
                        section.removeBand(band);
                        //  JRDesignGroup g = (JRDesignGroup)jd.getGroupsMap().get( band.getOrigin().getGroupName());
                        //  g.setGroupFooter(null);
                    }
                    
                    DeleteBandUndoableEdit edit = new DeleteBandUndoableEdit(band,jd);
                    IReportManager.getInstance().addUndoableEdit(edit);
                }
            }
            
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) return false;
        for (int i=0; i<activatedNodes.length; ++i)
        {
            if (!(activatedNodes[i] instanceof BandNode)) return false;
        }
        return true;
    }
}