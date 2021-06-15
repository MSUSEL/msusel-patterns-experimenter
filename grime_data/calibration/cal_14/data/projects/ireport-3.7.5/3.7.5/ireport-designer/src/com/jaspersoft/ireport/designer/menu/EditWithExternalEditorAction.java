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
package com.jaspersoft.ireport.designer.menu;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.io.IOException;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

public final class EditWithExternalEditorAction extends NodeAction {

    @Override
    protected void performAction(Node[] nodes) {
        
        JasperDesign jd = IReportManager.getInstance().getActiveReport();
        if (jd != null)
        {

            if (IReportManager.getInstance().getActiveVisualView().getEditorSupport().isModified())
            {
                int res = javax.swing.JOptionPane.showConfirmDialog(Misc.getMainFrame(),
                        I18n.getString("messages.fileModifiedLaunchingEditor"),"",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                if (res == javax.swing.JOptionPane.OK_OPTION) {
                    try {
                        IReportManager.getInstance().getActiveVisualView().getEditorSupport().saveDocument();
                    } catch (IOException ex) {
                        return;
                    }
                }
                else
                    return;
            }
            Runtime rt = Runtime.getRuntime();
            String editor = null;
            String fileName = null;
            try {
                editor = IReportManager.getPreferences().get("ExternalEditor", null);
                if (editor == null)
                {
                    javax.swing.JOptionPane.showMessageDialog(Misc.getMainFrame(),
                    I18n.getString("messages.noExternalEditorDefined"),"",javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                fileName = FileUtil.toFile(IReportManager.getInstance().getActiveVisualView().getEditorSupport().getDataObject().getPrimaryFile()).getPath();

            // WRONG ON UNIX: rt.exec(editor+ " \"" +jrf.getReport().getFilename()+"\"");
            // String tokenizer wasn't parsing the command and arguments
            // properly, so pass them in as separate elements.
                rt.exec(new String[] { editor, fileName });

            } catch (Exception ex) {

                javax.swing.JOptionPane.showMessageDialog(Misc.getMainFrame(),
                        I18n.getString("messages.errorExecutingEditor", new Object[]{editor,fileName}) ,"",javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public String getName() {
        return I18n.getString("action.editWithExternalEditor");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected boolean enable(Node[] nodes) {
        return IReportManager.getInstance().getActiveReport() != null;
    }
}