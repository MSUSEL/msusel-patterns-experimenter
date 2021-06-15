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

import com.jaspersoft.ireport.designer.wizards.CustomChooserWizardPanel;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.TemplateWizard;

public final class EmptyReportWizardIterator implements WizardDescriptor.InstantiatingIterator {

    private int index;
    private WizardDescriptor wizard;
    private WizardDescriptor.Panel[] panels;

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            
            WizardDescriptor.Panel targetChooserPanel = null;
                boolean useCustomChooserPanel = false;
                
                if (wizard.getProperty("useCustomChooserPanel") != null &&
                    wizard.getProperty("useCustomChooserPanel").equals("true"))
                {
                    targetChooserPanel = new CustomChooserWizardPanel(wizard);
                }
                else
                {
                    System.out.println("Using regular panel...");
                System.out.flush();
                    targetChooserPanel = ((TemplateWizard)wizard).targetChooser();
                }
            
            panels = new WizardDescriptor.Panel[]{
                targetChooserPanel
            };
            String[] steps = createSteps();
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                if (steps[i] == null) {
                    // Default step name to component name of panel. Mainly
                    // useful for getting the name of the target chooser to
                    // appear in the list of steps.
                    steps[i] = c.getName();
                }
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    // Sets step number of a component
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                    // Sets steps names for a panel
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    // Turn on subtitle creation on each step
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                    // Show steps on the left side with the image on the background
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                    // Turn on numbering of all steps
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
                }
            }
        }
        return panels;
    }

    public Set instantiate() throws IOException {
        
        if (wizard.getProperty("filename") != null)
        {
           File f = new File( ""+wizard.getProperty("filename"));
           // Let's set the file folder...
           ((TemplateWizard)wizard).setTargetFolder(DataFolder.findFolder( FileUtil.toFileObject( f.getParentFile() )) );
           // Let's set the target folder...
           ((TemplateWizard)wizard).setTargetName(f.getName());
        }
            
            
        if (((TemplateWizard)wizard).getTargetFolder() != null)
        {
                String fname = ((TemplateWizard)wizard).getTargetName();
                String directory = ((TemplateWizard) wizard).getTargetFolder().getPrimaryFile().getPath();
                // We do some strong assumptions here:
                // 1. the directory exists
                // 2. we are not replacing another file if it was specified
                // 3. if specified, the file ends with .jrxml
                
                // Default name specified...
                // let's look for a new valid file name...
                if (fname == null)
                {
                    fname = "Report.jrxml";
                    File f = new File( directory,fname);
                    int i=1;
                    while (f.exists())
                    {
                        fname = "Report_" + i + ".jrxml";
                        f = new File( directory,fname);
                        i++;
                    }
                }
                
                ((TemplateWizard)wizard).setTargetName(fname);
            }
        
        DataFolder df = ((TemplateWizard)wizard).getTargetFolder();
        DataObject dTemplate = ((TemplateWizard)wizard).getTemplate();
        
        // Strip out the extension from the target name...
        String targetName = ((TemplateWizard)wizard).getTargetName();
        if (targetName.toLowerCase().endsWith(".jrxml"))
        {
            targetName = targetName.substring(0, targetName.length()-6);
        }
        
        DataObject dobj = dTemplate.createFromTemplate( df, targetName);
        
        OpenCookie cookie = dobj.getCookie( OpenCookie.class);
        cookie.open();
        return Collections.singleton (dobj.getPrimaryFile ());
    }

    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
    }

    public void uninitialize(WizardDescriptor wizard) {
        panels = null;
    }

    public WizardDescriptor.Panel current() {
        return getPanels()[index];
    }

    public String name() {
        return index + 1 + ". from " + getPanels().length;
    }

    public boolean hasNext() {
        return index < getPanels().length - 1;
    }

    public boolean hasPrevious() {
        return index > 0;
    }

    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    // If nothing unusual changes in the middle of the wizard, simply:
    public void addChangeListener(ChangeListener l) {
    }

    public void removeChangeListener(ChangeListener l) {
    }

    // If something changes dynamically (besides moving between panels), e.g.
    // the number of panels changes in response to user input, then uncomment
    // the following and call when needed: fireChangeEvent();
    /*
    private Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0
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
     */

    /*
    // You could safely ignore this method. Is is here to keep steps which were
    // there before this wizard was instantiated. It should be better handled
    // by NetBeans Wizard API itself rather than needed to be implemented by a
    // client code.
    private String[] createSteps() {
        String[] beforeSteps = null;
        Object prop = wizard.getProperty("WizardPanel_contentData");
        if (prop != null && prop instanceof String[]) {
            beforeSteps = (String[]) prop;
        }

        if (beforeSteps == null) {
            beforeSteps = new String[0];
        }

        String[] res = new String[(beforeSteps.length - 1) + panels.length];
        for (int i = 0; i < res.length; i++) {
            if (i < (beforeSteps.length - 1)) {
                res[i] = beforeSteps[i];
            } else {
                res[i] = panels[i - beforeSteps.length + 1].getComponent().getName();
            }
        }
        return res;
    }
    */
    
    private String[] createSteps() {
        
        String[] res = new String[panels.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = panels[i].getComponent().getName();
        }
        return res;
    }
}
