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
package net.sourceforge.ganttproject.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileFilter;


import net.sourceforge.ganttproject.gui.options.OptionsPageBuilder;
import net.sourceforge.ganttproject.gui.options.SpringUtilities;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.gui.projectwizard.WizardPage;
import net.sourceforge.ganttproject.language.GanttLanguage;

public abstract class FileChooserPageBase implements WizardPage {
    private JPanel myComponent;
	private TextFieldAndFileChooserComponent myChooser;
	private OptionsPageBuilder myOptionsBuilder;
	private JPanel mySecondaryOptionsComponent;
	
	protected FileChooserPageBase() {
		myOptionsBuilder = new OptionsPageBuilder();
        mySecondaryOptionsComponent = new JPanel(new BorderLayout()); 
	}
    
    protected abstract String getFileChooserTitle();
    
	public Component getComponent() {
        myComponent = new JPanel(new BorderLayout());
        myChooser = new TextFieldAndFileChooserComponent(GanttLanguage.getInstance().getText("file")+":",
        		getFileChooserTitle()) {
            protected void onFileChosen(File file) {
            	FileChooserPageBase.this.onFileChosen(file);
            	super.onFileChosen(file);
            }
        };
        JComponent contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(myChooser.getComponent(), BorderLayout.NORTH);
        contentPanel.add(mySecondaryOptionsComponent, BorderLayout.CENTER);
//        SpringUtilities.makeCompactGrid(contentPanel, 2, 1,
//                0, 0, 5, 5);
        myComponent.add(contentPanel, BorderLayout.NORTH);
        return myComponent;
    }

    public void setActive(boolean b) {
    	GPOptionGroup[] optionGroups = getOptionGroups();
        if (b == false) {
        	for (int i=0; i<optionGroups.length; i++) {
        		optionGroups[i].commit();
        	}
        } else {
        	for (int i=0; i<optionGroups.length; i++) {
        		optionGroups[i].lock();
        	}
        	if (mySecondaryOptionsComponent!=null){
                mySecondaryOptionsComponent.removeAll();
            }        	
        	//mySecondaryOptionsComponent= new JPanel(new BorderLayout());
            mySecondaryOptionsComponent.add(myOptionsBuilder.buildPlanePage(optionGroups), BorderLayout.NORTH); 
            //myComponent.add(mySecondaryOptionsComponent, BorderLayout.CENTER);
            mySecondaryOptionsComponent.invalidate();
            myComponent.invalidate();
            myChooser.setFileFilter(createFileFilter());
        }
    }
    

	protected void setSelectedFile(File file) {
		myChooser.setFile(file);
	}

	protected File getSelectedFile() {
		return myChooser.getFile();
	}
	
	protected void setOptionsEnabled(boolean enabled) {
		if (mySecondaryOptionsComponent!=null) {
			setEnabledTree(mySecondaryOptionsComponent, enabled);
		}
	}
	
    private void setEnabledTree(JComponent root, boolean isEnabled) {
    	UIUtil.setEnabledTree(root, isEnabled);
    }
	
    
	protected abstract FileFilter createFileFilter();

	protected abstract GPOptionGroup[] getOptionGroups();

	protected abstract void onFileChosen(File file);

}
