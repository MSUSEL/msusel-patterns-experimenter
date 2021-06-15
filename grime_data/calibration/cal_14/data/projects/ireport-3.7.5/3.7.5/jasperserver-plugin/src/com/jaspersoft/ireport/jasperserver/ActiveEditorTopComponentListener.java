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
package com.jaspersoft.ireport.jasperserver;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.openide.loaders.DataObject;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * This class listens for new documents opened. If the document is a JrxmlDocument
 * and there is a DomainsService view tied to it, listen for document changes.
 *
 * @version $Id: ReportOpenedListener.java 0 2009-10-05 18:17:54 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ActiveEditorTopComponentListener {

    List<String> openedFiles = new ArrayList<String>();
    private TopComponent activeEditorTopComponent = null;


    static private ActiveEditorTopComponentListener mainInstance = null;


    private ActiveEditorTopComponentListener()
    {
        WindowManager.getDefault().getRegistry().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {

                if (evt.getPropertyName().equals(TopComponent.Registry.PROP_ACTIVATED))
                {
                    TopComponent tc = (TopComponent)evt.getNewValue();
                    if (tc != null && tc.getLookup().lookup(DataObject.class) != null)
                    {
                        System.out.println("Active top component: " + tc.getDisplayName() + " " + tc.getLookup().lookup(DataObject.class).getPrimaryFile());
                        System.out.flush();
                        setActiveEditorTopComponent(tc);
                    }

                }
                else if (evt.getPropertyName().equals(TopComponent.Registry.PROP_TC_CLOSED))
                {
                    TopComponent tc = (TopComponent)evt.getNewValue();
                    if (tc == activeEditorTopComponent)
                    {
                        System.out.println("No active editor top component");
                        System.out.flush();
                        setActiveEditorTopComponent(null);
                    }
                }

            }
        });
    }

    public static void startListening() {

        getDefaultInstance();
    }

    public static ActiveEditorTopComponentListener getDefaultInstance() {

        if (mainInstance == null)
        {
            mainInstance = new ActiveEditorTopComponentListener();
        }
        return mainInstance;
    }

    /**
     * @return the activeEditorTopComponent
     */
    public TopComponent getActiveEditorTopComponent() {
        return activeEditorTopComponent;
    }

    /**
     * @param activeEditorTopComponent the activeEditorTopComponent to set
     */
    public void setActiveEditorTopComponent(TopComponent activeEditorTopComponent) {
        this.activeEditorTopComponent = activeEditorTopComponent;
    }

}
