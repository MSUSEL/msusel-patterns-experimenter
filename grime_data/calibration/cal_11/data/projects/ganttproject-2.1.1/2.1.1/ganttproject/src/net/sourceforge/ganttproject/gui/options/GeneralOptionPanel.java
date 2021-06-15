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
/***************************************************************************
 GeneralOptionPanel.java 
 ------------------------------------------
 begin                : 24 juin 2004
 copyright            : (C) 2004 by Thomas Alexandre
 email                : alexthomas(at)ganttproject.org
 ***************************************************************************/

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package net.sourceforge.ganttproject.gui.options;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;

import javax.swing.Box;
import javax.swing.JPanel;

import net.sourceforge.ganttproject.Mediator;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author athomas Abstract class for the Options panels
 */
public abstract class GeneralOptionPanel extends JPanel {

    // -- ATTRIBUTES --

    protected GanttLanguage language = GanttLanguage.getInstance();

    /** General vertical box. */
    protected Box vb = Box.createVerticalBox();

    /** Tell if the parameters of the panel have change. */
    protected boolean bHasChange = false;

    /** Ganttproject object. */
    private Frame appli;

    private String myTitle;

    private String myComment;

    // -- METHODS --

    public GeneralOptionPanel(String title, String comment) {
        this(title, comment, null);
    }

    public GeneralOptionPanel(String title, String comment, Frame parent) {
        super();
        appli = parent;
        setLayout(new BorderLayout());
        add(vb, BorderLayout.CENTER);
        myTitle = title;
        myComment = comment;

    }

    public Component getComponent() {
        return this;
    }

    /** This method check if the value has changed, and assk for commit changes. */
    public abstract boolean applyChanges(boolean askForApply);

    /** Initialize the component. */
    public abstract void initialize();

    /** This method ask for saving the changes. */
    public boolean askForApplyChanges() {
        return (UIFacade.Choice.YES==getUIFacade().showConfirmationDialog(language.getText("msg20"),
                language.getText("question")));
    }

    /**
     * @return
     */
    public String getTitle() {
        return myTitle;
    }

    /**
     * @return
     */
    public String getComment() {
        return myComment;
    }

    private UIFacade getUIFacade() {
        return Mediator.getGanttProjectSingleton().getUIFacade();
    }

    public void rollback() {
        initialize();
    }
}
