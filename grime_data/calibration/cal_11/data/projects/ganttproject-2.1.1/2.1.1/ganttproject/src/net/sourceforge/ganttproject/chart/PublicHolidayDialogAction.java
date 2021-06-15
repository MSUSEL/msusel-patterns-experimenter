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
/**
 * 
 */
package net.sourceforge.ganttproject.chart;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sourceforge.ganttproject.GanttCalendar;
import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.action.CancelAction;
import net.sourceforge.ganttproject.action.OkAction;
import net.sourceforge.ganttproject.gui.GanttDialogPublicHoliday;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author nbohn
 */
public class PublicHolidayDialogAction extends AbstractAction {

    private IGanttProject myProject;

    private UIFacade myUIFacade;

    static GanttLanguage language = GanttLanguage.getInstance();

    public PublicHolidayDialogAction(IGanttProject project, UIFacade uiFacade) {
        super(GanttProject.correctLabel(language.getText("editPublicHolidays")));
        myProject = project;
        myUIFacade = uiFacade;
        this.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource(
                "/icons/holidays_16.gif")));
    }

    public void actionPerformed(ActionEvent arg0) {

        // myUIFacade.showDialog(createDialogComponent(), new Action[]{okAction,
        // cancelAction});
        final GanttDialogPublicHoliday dialog = new GanttDialogPublicHoliday(
                myProject, myUIFacade);
        Component dialogContent = dialog.getContentPane();
        myUIFacade.showDialog(dialogContent, new Action[] {
                new OkAction() {
                    public void actionPerformed(ActionEvent e) {
                        updateHolidays(dialog.getHolidays());
                    }
            
                }, 
                new CancelAction() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }
        });
    }
    
    private void updateHolidays(List holidays) {
        myProject.getActiveCalendar().getPublicHolidays().clear();
        for (int i = 0; i < holidays.size(); i++) {
            myProject.getActiveCalendar().setPublicHoliDayType(
                    ((GanttCalendar)holidays.get(i))
                            .getTime());
        }
        
    }
}
