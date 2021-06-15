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
package com.jaspersoft.ireport.designer.styles;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.palette.PaletteItemAction;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.dnd.DropTargetDropEvent;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRTemplateReference;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class DragTemplateReferenceAction extends PaletteItemAction {

    @Override
    public void drop(DropTargetDropEvent dtde) {

        JasperDesign jd = getJasperDesign();
        Object toActivate = null;

        JRTemplateReference reference = (JRTemplateReference)getPaletteItem().getData();
        if (reference != null)
        {
                // Copy the reference...
                JRDesignReportTemplate reportTemplate = new JRDesignReportTemplate();
                reportTemplate.setSourceExpression(Misc.createExpression("java.lang.String", "\""+ Misc.string_replace("\\\\","\\",reference.getLocation()) +"\""));
                jd.addTemplate(reportTemplate);
                IReportManager.getInstance().notifyReportChange();
                toActivate = reportTemplate;
        }

        final Object obj = toActivate;

        if (obj != null)
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    IReportManager.getInstance().setSelectedObject(obj);
                    IReportManager.getInstance().getActiveVisualView().requestActive();
                    IReportManager.getInstance().getActiveVisualView().requestAttention(true);
                }
            });
        }
    }

}
