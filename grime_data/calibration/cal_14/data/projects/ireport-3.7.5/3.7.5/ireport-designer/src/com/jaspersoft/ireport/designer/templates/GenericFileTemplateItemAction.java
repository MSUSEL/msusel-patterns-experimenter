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

import javax.swing.ImageIcon;

/**
 *
 * @author gtoffoli
 */
public class GenericFileTemplateItemAction extends TemplateItemAction {

    private String targetName = null;
    private String templateName = null;


    public GenericFileTemplateItemAction(String displayName, String description, String targetName, String templateName, ImageIcon icon)
    {
        setDisplayName(displayName);
        setDescription(description);
        this.targetName = targetName;
        this.templateName = templateName;
        setIcon(icon);
        putProperty(PROP_SHOW_TEMPLATES, Boolean.FALSE);
        putProperty(PROP_SHOW_FINISH_BUTTON, Boolean.TRUE);
        putProperty(PROP_SHOW_LAUNCH_REPORT_WIZARD_BUTTON, Boolean.FALSE);
        putProperty(PROP_SHOW_OPEN_TEMPLATE_BUTTON, Boolean.FALSE);
    }

    @Override
    public void performAction(TemplatesFrame frame, int buttonId) {

        if (buttonId == BUTTON_FINISH)
        {
            frame.runTemplateWizard(getTargetName(), getTemplateName());
        }


    }

    /**
     * @return the targetName
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * @param targetName the targetName to set
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * @return the templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName the templateName to set
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

}
