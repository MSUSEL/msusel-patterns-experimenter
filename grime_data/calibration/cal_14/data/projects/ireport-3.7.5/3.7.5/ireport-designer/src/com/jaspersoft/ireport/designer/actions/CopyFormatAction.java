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
import com.jaspersoft.ireport.designer.outline.nodes.ElementNode;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;
import com.jaspersoft.ireport.designer.ModelUtils;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.Node.PropertySet;

public final class CopyFormatAction extends NodeAction {

        
   public static final String[] propertyNames = new String[] {
        JRBaseStyle.PROPERTY_BACKCOLOR,
        JRBaseStyle.PROPERTY_BLANK_WHEN_NULL,
        JRBaseStyle.PROPERTY_BOLD,
        JRBaseStyle.PROPERTY_FILL,
        JRBaseStyle.PROPERTY_FONT_NAME,
        JRBaseStyle.PROPERTY_FONT_SIZE,
        JRBaseStyle.PROPERTY_FORECOLOR,
        JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT,
        JRBaseStyle.PROPERTY_ITALIC,
        JRBaseStyle.PROPERTY_LINE_SPACING,
        JRBaseStyle.PROPERTY_MARKUP,
        JRBaseStyle.PROPERTY_MODE,
        JRBaseStyle.PROPERTY_PATTERN,
        JRBaseStyle.PROPERTY_PDF_EMBEDDED,
        JRBaseStyle.PROPERTY_PDF_ENCODING,
        JRBaseStyle.PROPERTY_PDF_FONT_NAME,
        JRBaseStyle.PROPERTY_RADIUS,
        JRBaseStyle.PROPERTY_ROTATION,
        JRBaseStyle.PROPERTY_SCALE_IMAGE,
        JRBaseStyle.PROPERTY_STRIKE_THROUGH,
        JRBaseStyle.PROPERTY_UNDERLINE,
        JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT,
        JRDesignElement.PROPERTY_PARENT_STYLE
    };


    public static final Map formattingValues = new HashMap();

    public String getName() {
        return I18n.getString("CopyFormatAction.name");
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

        Node node = activatedNodes[0];
        formattingValues.clear();

        for (int i=0; i<propertyNames.length; ++i)
        {
            Object val = getPropertyValue(node, propertyNames[i]);
            if (val != null)
            {
                formattingValues.put(propertyNames[i], val);
            }
        }

    }

    protected Object getPropertyValue(Node node, String prop)
    {
        PropertySet[] sets = node.getPropertySets();
        Property p = ModelUtils.findProperty(sets, prop);
        try {
            return p.getValue();
        } catch (Exception  ex) {
            return null;
        }
    }

    protected boolean enable(org.openide.nodes.Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length != 1) return false;
        if (!(activatedNodes[0] instanceof ElementNode)) return false;
        return true;
    }
}