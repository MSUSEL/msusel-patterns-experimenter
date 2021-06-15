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
package com.jaspersoft.ireport.designer.menu.preview;

import com.jaspersoft.ireport.designer.IReportManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

/**
 *
 * @author gtoffoli
 */
public abstract class AbstractPreviewAction extends CallableSystemAction implements PreferenceChangeListener, ItemListener {

    public abstract String getPreviewType();

    private JRadioButtonMenuItem item;
    private boolean updating = false;
    
    public AbstractPreviewAction()
    {
        item = new JRadioButtonMenuItem(getName());
        IReportManager.getPreferences().addPreferenceChangeListener(this);
        preferenceChange(null);
        item.addItemListener(this);
    }
    
    public void performAction()
    {

    }
            

    @Override
    public JMenuItem getMenuPresenter() {
        return item;
    }
     
    @Override
    protected void initialize() {
        super.initialize();
        
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
        
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    public void preferenceChange(PreferenceChangeEvent evt)
    {
        String fmt = IReportManager.getPreferences().get("output_format", "");
        if (getPreviewType().equals(fmt) != item.isSelected())
        {
            setUpdating(true);
            item.setSelected(!item.isSelected());
            setUpdating(false);
        }
    }
                      
    
    public void itemStateChanged(ItemEvent e)
    {
        if (isUpdating()) return;
        
        if (e.getStateChange() == ItemEvent.DESELECTED)
        {
            IReportManager.getPreferences().remove("output_format");
        }
        else
        {
            if (getPreviewType().length() > 0)
            {
                IReportManager.getPreferences().put("output_format", getPreviewType());
            }
            else
            {
                IReportManager.getPreferences().remove("output_format");
            }
        }
        performAction();
    }

    /**
     * @return the updating
     */
    public boolean isUpdating() {
        return updating;
    }

    /**
     * @param updating the updating to set
     */
    public void setUpdating(boolean updating) {
        this.updating = updating;
    }

}
