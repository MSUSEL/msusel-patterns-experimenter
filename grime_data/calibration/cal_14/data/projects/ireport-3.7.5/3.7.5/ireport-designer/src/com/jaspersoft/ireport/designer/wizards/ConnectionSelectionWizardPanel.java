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
package com.jaspersoft.ireport.designer.wizards;

import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.data.WizardFieldsProvider;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Component;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.engine.design.JRDesignField;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class ConnectionSelectionWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel {

    private WizardDescriptor wizard;
   
    public ConnectionSelectionWizardPanel(WizardDescriptor wizard)
    {
        this.wizard = wizard;
    }
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private ConnectionSelectionVisualPanel component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    public Component getComponent() {
        if (component == null) {
            component = new ConnectionSelectionVisualPanel(this);
        }
        return component;
    }

    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
    // If you have context help:
    // return new HelpCtx(SampleWizardPanel1.class);
    }

    public boolean isValid() {
        
        IReportConnection con = component.getConnection();
        if (con instanceof WizardFieldsProvider &&
            component.isUseQuery() &&
            component.getQuery().trim().length() == 0)
        {
            getWizard().putProperty("WizardPanel_errorMessage", I18n.getString("Wizards.Property.Invalidquery"));
            return false;
        }
        
        getWizard().putProperty("WizardPanel_errorMessage", null);
        return true;
    }

    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0
        public final void addChangeListener(ChangeListener l) {
            synchronized (listeners) {
                listeners.add(l);
        }
    }
    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }
    protected final void fireChangeEvent() {

        Iterator<ChangeListener> it;
        synchronized (listeners) {
            it = new HashSet<ChangeListener>(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (it.hasNext()) {
            it.next().stateChanged(ev);
        }
    }

    // You can use a settings object to keep track of state. Normally the
    // settings object will be the WizardDescriptor, so you can use
    // WizardDescriptor.getProperty & putProperty to store information entered
    // by the user.
    public void readSettings(Object settings) {
        
    }

    public void storeSettings(Object settings) {
    }

    public WizardDescriptor getWizard() {
        return wizard;
    }

    public void setWizard(WizardDescriptor wizard) {
        this.wizard = wizard;
    }

    public void prepareValidation() {
    }

    public void validate() throws WizardValidationException {
        
        List<JRDesignField> discoveredFields = null;
        getWizard().putProperty("discoveredFields", discoveredFields);
        getWizard().putProperty("discoveredFieldsNeedRefresh", "true");
        getWizard().putProperty("query", null);
        getWizard().putProperty("queryLanguage", null);
            
        try {
            IReportConnection con = component.getConnection();
            if (con instanceof WizardFieldsProvider)
            {
                discoveredFields = ((WizardFieldsProvider)con).readFields(component.getQuery());
                getWizard().putProperty("query", component.getQuery());
                getWizard().putProperty("queryLanguage", ((WizardFieldsProvider)con).getQueryLanguage());
            }
            else
            {
                discoveredFields = null;
            }
            
            getWizard().putProperty("discoveredFields", discoveredFields);
            
        } catch (Exception ex)
        {
            Misc.showErrorMessage(ex.getMessage(),"Query error", ex);
            throw new WizardValidationException(component, ex.getMessage(),ex.getLocalizedMessage() );
        }
    }
}

