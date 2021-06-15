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
package com.jaspersoft.ireport.designer.options;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;
import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

final public class IReportOptionsPanelController extends OptionsPanelController {

    private IReportPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;
    private List<OptionsPanelController> delegatedControllers = new ArrayList<OptionsPanelController>();
    private List<AdvancedOption> advancedOptions = new ArrayList<AdvancedOption>();

    public IReportOptionsPanelController()
    {
        Lookup lookup = Lookups.forPath("OptionsDialog/iReport"); // NOI18N
        Collection<? extends AdvancedOption> subTabs = lookup.lookupAll(AdvancedOption.class);
        Iterator<? extends AdvancedOption> it = subTabs.iterator();
        while (it.hasNext ()) {

            AdvancedOption option = it.next();
            advancedOptions.add(option);
            OptionsPanelController opc = option.create();
            delegatedControllers.add(opc);
            JComponent c = opc.getComponent(null);
            getPanel().addTab( option.getDisplayName() , c);
        }
    }

    public void update() {
        getPanel().load();
        for (OptionsPanelController opc : delegatedControllers)
        {
            opc.update();
        }

        changed = false;
    }

    public void applyChanges() {
        getPanel().store();
        for (OptionsPanelController opc : delegatedControllers)
        {
            opc.applyChanges();
        }
        changed = false;
    }

    public void cancel() {
        for (OptionsPanelController opc : delegatedControllers)
        {
            opc.cancel();
        }
    }

    public boolean isValid() {
        for (OptionsPanelController opc : delegatedControllers)
        {
            if (!opc.isValid()) return false;
        }
        return getPanel().valid();
    }

    public boolean isChanged() {
        return changed;
    }

    public HelpCtx getHelpCtx() {
        return null; // new HelpCtx("...ID") if you have a help set
    }

    public JComponent getComponent(Lookup masterLookup) {
        return getPanel();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
        for (OptionsPanelController opc : delegatedControllers)
        {
            opc.addPropertyChangeListener(l);
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
        for (OptionsPanelController opc : delegatedControllers)
        {
            opc.removePropertyChangeListener(l);
        }
    }

    private IReportPanel getPanel() {
        if (panel == null) {
            panel = new IReportPanel(this);
        }
        return panel;
    }

    public void changed() {
        if (!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }
}
