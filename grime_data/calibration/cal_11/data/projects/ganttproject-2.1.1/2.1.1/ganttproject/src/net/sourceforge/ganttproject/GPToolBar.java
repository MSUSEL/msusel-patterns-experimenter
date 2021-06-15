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
/*
 * Created on 29.09.2005
 */
package net.sourceforge.ganttproject;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import net.sourceforge.ganttproject.gui.TestGanttRolloverButton;
import net.sourceforge.ganttproject.language.GanttLanguage;

public class GPToolBar extends JToolBar {
    public static final Object SEPARATOR_OBJECT = new Object() {
        private final String myString = GanttLanguage.getInstance().getText("separator");
        public String toString() {
            return myString;
        }
    };
    
    private final GanttOptions options;
    private List myButtons;
    
    public GPToolBar(String title, int toolBarPosition, GanttOptions options) {
        super(title, toolBarPosition);
        setBorderPainted(true);
        setRollover(true);
        setFloatable(true);        
        this.options = options;
    }
    
    void populate(List/*<JButton>*/ buttons) {
        removeAll();
        myButtons = new ArrayList(buttons.size());
        for (int i = 0; i < buttons.size(); i++) {
            Object nextButton = buttons.get(i);
            if (GPToolBar.SEPARATOR_OBJECT.equals(nextButton)) {
                int size = Integer.parseInt(options.getIconSize());
                // toolBar.addSeparator(new Dimension(size, size));
                ImageIcon icon;
                if (getOrientation() == JToolBar.HORIZONTAL) {
                    icon =  new ImageIcon(getClass().getResource(
                    "/icons/sepV_16.png"));
                }
                else {
                    icon = new ImageIcon(getClass().getResource(
                    "/icons/sepH_16.png"));
                }
                add(new JLabel(icon));
            } else {
                add((AbstractButton)nextButton);
                if (nextButton instanceof TestGanttRolloverButton) {
                    myButtons.add(nextButton);
                }
            }
        }
        invalidate();
    }

    void updateButtonsLook() {
        for (int i=0; i<myButtons.size(); i++) {
            TestGanttRolloverButton nextButton = (TestGanttRolloverButton) myButtons.get(i);
            nextButton.setIconHidden(options.getButtonShow() == GanttOptions.TEXT);
            nextButton.setTextHidden(options.getButtonShow() == GanttOptions.ICONS);
        }
        invalidate();
    }
    

}
