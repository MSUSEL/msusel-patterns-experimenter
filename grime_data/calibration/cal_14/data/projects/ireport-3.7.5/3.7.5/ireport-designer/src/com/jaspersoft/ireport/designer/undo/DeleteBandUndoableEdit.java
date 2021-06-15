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
package com.jaspersoft.ireport.designer.undo;

import com.jaspersoft.ireport.designer.ModelUtils;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class DeleteBandUndoableEdit extends AggregatedUndoableEdit {

    private JRDesignBand band = null;
    private JasperDesign jasperDesign = null;
    
    public DeleteBandUndoableEdit(JRDesignBand band, JasperDesign jasperDesign)
    {
        this.band = band;
        this.jasperDesign = jasperDesign;
    }
    
    @Override
    public void undo() throws CannotUndoException {
        
        super.undo();
        JROrigin origin = getBand().getOrigin();
        if (origin != null)
        {
            switch (origin.getBandType())
            {
                case JROrigin.BACKGROUND: 
                    jasperDesign.setBackground(band); break;
                case JROrigin.TITLE: 
                    jasperDesign.setTitle(band); break;
                case JROrigin.PAGE_HEADER: 
                    jasperDesign.setPageHeader(band); break;
                case JROrigin.COLUMN_HEADER: 
                    jasperDesign.setColumnHeader(band); break;
                case JROrigin.DETAIL: 
                {
                    ((JRDesignSection)jasperDesign.getDetailSection()).addBand(band);
                    break;
                }
                case JROrigin.COLUMN_FOOTER: 
                    jasperDesign.setColumnFooter(band); break;
                case JROrigin.PAGE_FOOTER: 
                    jasperDesign.setPageFooter(band); break;
                case JROrigin.LAST_PAGE_FOOTER: 
                    jasperDesign.setLastPageFooter(band); break;
                case JROrigin.SUMMARY: 
                    jasperDesign.setSummary(band); break;
                case JROrigin.NO_DATA: 
                    jasperDesign.setNoData(band); break;
                case JROrigin.GROUP_HEADER:
                {
                    JRDesignGroup group = (JRDesignGroup)jasperDesign.getGroupsMap().get(origin.getGroupName());
                    ((JRDesignSection)group.getGroupHeaderSection()).addBand(band);
                    break;
                }
                case JROrigin.GROUP_FOOTER:
                {
                    JRDesignGroup group = (JRDesignGroup)jasperDesign.getGroupsMap().get(origin.getGroupName());
                    ((JRDesignSection)group.getGroupFooterSection()).addBand(band);
                    break;
                }
            }
        }
        
    }

    @Override
    public void redo() throws CannotRedoException {
        
        super.redo();
        
        JROrigin origin = getBand().getOrigin();

        if (origin != null)
        {
            switch (origin.getBandType())
            {
                case JROrigin.BACKGROUND: 
                    jasperDesign.setBackground(null); break;
                case JROrigin.TITLE: 
                    jasperDesign.setTitle(null); break;
                case JROrigin.PAGE_HEADER: 
                    jasperDesign.setPageHeader(null); break;
                case JROrigin.COLUMN_HEADER: 
                    jasperDesign.setColumnHeader(null); break;
                case JROrigin.DETAIL:
                {
                    JRDesignSection section = (JRDesignSection)jasperDesign.getDetailSection();
                    section.removeBand(band);
                    break;
                    //jasperDesign.setDetail(null); break;
                }
                case JROrigin.COLUMN_FOOTER: 
                    jasperDesign.setColumnFooter(null); break;
                case JROrigin.PAGE_FOOTER: 
                    jasperDesign.setPageFooter(null); break;
                case JROrigin.LAST_PAGE_FOOTER: 
                    jasperDesign.setLastPageFooter(null); break;
                case JROrigin.SUMMARY: 
                    jasperDesign.setSummary(null); break;
                case JROrigin.NO_DATA: 
                    jasperDesign.setNoData(null); break;
                case JROrigin.GROUP_HEADER:
                {
                    JRDesignGroup group = ((JRDesignGroup)jasperDesign.getGroupsMap().get(origin.getGroupName()));
                    JRDesignSection section = (JRDesignSection)group.getGroupHeaderSection();
                    section.removeBand(band);
                    break;
                    //((JRDesignGroup)jasperDesign.getGroupsMap().get(origin.getGroupName())).setGroupHeader(null); break;
                }
                case JROrigin.GROUP_FOOTER:
                {
                    JRDesignGroup group = ((JRDesignGroup)jasperDesign.getGroupsMap().get(origin.getGroupName()));
                    JRDesignSection section = (JRDesignSection)group.getGroupFooterSection();
                    section.removeBand(band);
                    break;
                    //((JRDesignGroup)jasperDesign.getGroupsMap().get(origin.getGroupName())).setGroupFooter(null); break;
                }
                    
            }
        }
    }
    
    @Override
    public String getPresentationName() {
        return "Delete Band " + ModelUtils.nameOf(getBand().getOrigin());
    }

    public JRDesignBand getBand() {
        return band;
    }

    public void setBand(JRDesignBand band) {
        this.band = band;
    }

    public JasperDesign getJasperDesign() {
        return jasperDesign;
    }

    public void setJasperDesign(JasperDesign jasperDesign) {
        this.jasperDesign = jasperDesign;
    }
}
