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
package com.jaspersoft.ireport.designer.templates;

import com.jaspersoft.ireport.locale.I18n;

/**
 *
 * @author gtoffoli
 */
public class ReportTemplateItemAction extends TemplateItemAction {

    public ReportTemplateItemAction()
    {
        setDisplayName(I18n.getString("ReportTemplateItemAction.displayName"));
        setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/template/new_report.png")));
        setDescription(I18n.getString("ReportTemplateItemAction.description"));
        putProperty(PROP_SHOW_TEMPLATES, Boolean.TRUE);
        putProperty(PROP_SHOW_FINISH_BUTTON, Boolean.FALSE);
        putProperty(PROP_SHOW_LAUNCH_REPORT_WIZARD_BUTTON, Boolean.TRUE);
        putProperty(PROP_SHOW_OPEN_TEMPLATE_BUTTON, Boolean.TRUE);
    }


    @Override
    public void performAction(TemplatesFrame frame, int buttonIndex) {

        if (frame.getSelectedTemplateDescriptor() == null) return;
        frame.runTemplateWizard(frame.getSelectedTemplateDescriptor(), buttonIndex == BUTTON_LAUNCH_REPORT_WIZARD);
    }

}
