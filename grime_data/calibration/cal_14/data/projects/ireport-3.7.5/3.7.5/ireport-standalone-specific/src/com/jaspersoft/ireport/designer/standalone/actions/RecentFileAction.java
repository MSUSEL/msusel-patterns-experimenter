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
package com.jaspersoft.ireport.designer.standalone.actions;

import com.jaspersoft.ireport.designer.standalone.IReportStandaloneManager;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.beans.BeanInfo;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.actions.Presenter;

/**
 * Action that presents list of recently closed files/documents.
 *
 * @author Dafe Simonek
 */
public class RecentFileAction extends AbstractAction implements Presenter.Menu, PopupMenuListener {

    /** property of menu items where we store fileobject to open */
    private static final String FO_PROP = "RecentFileAction.Recent_FO"; // NOI18N

    /** number of maximum shown items in submenu */ 
    private static final int MAX_COUNT = 15;
            
    private JMenu menu;
    
    private static RecentFileAction theInstance = null;
    
    public static RecentFileAction getInstance()
    {
        return theInstance;
    }

    public RecentFileAction() {
        super(I18n.getString( IReportStandaloneManager.class,"LBL_RecentFileAction_Name")); // NOI18N
        RecentFiles.init();
        theInstance = this;
    }
    
    /********* Presenter.Menu impl **********/
    
    public JMenuItem getMenuPresenter() {
        if (menu == null) {
            menu = new JMenu(this);
            menu.setMnemonic(I18n.getString(IReportStandaloneManager.class,"MNE_RecentFileAction_Name").charAt(0)); // NOI18N
            //menu.getPopupMenu().addPopupMenuListener(this);

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    updateSubMenu();
                    menu.updateUI();
                }
            });
        }
        return menu;
    }
    
    /******* PopupMenuListener impl *******/
    
    public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
        updateSubMenu();
    }
    
    public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
        menu.removeAll();
    }
    
    public void popupMenuCanceled(PopupMenuEvent arg0) {
    }
    
    /** Fills submenu with recently closed files got from RecentFiles support */
    public void updateSubMenu () {
        List<String> files = RecentFiles.getRecentFiles();

        menu.removeAll();
        menu.setEnabled(files.size() != 0);
        int counter = 0;
        for (String hItem : files) {
            try {
                // obtain file object
                // note we need not check for null or validity, as it is ensured
                // by RecentFiles.getRecentFiles()
                FileObject fo = RecentFiles.convertURL2File(new URL(hItem));
                // allow only up to max items
                if (fo != null)
                {
                    if (++counter > MAX_COUNT) {
                        break;
                    }
                    // obtain icon for fileobject
                    Image icon = null;
                    try {

                        DataObject dObj = DataObject.find(fo);
                        icon = dObj.getNodeDelegate().getIcon(BeanInfo.ICON_COLOR_16x16);
                    } catch (DataObjectNotFoundException ex) {
                        // should not happen, log and skip to next
                        Logger.getLogger(RecentFiles.class.getName()).log(Level.INFO, ex.getMessage(), ex);
                        continue;
                    }
                    // create and configure menu item
                    JMenuItem jmi = null;
                    if (icon != null) {
                        jmi = new JMenuItem(fo.getNameExt(), new ImageIcon(icon));
                    } else {
                        jmi = new JMenuItem(fo.getNameExt());
                    }
                    jmi.putClientProperty(FO_PROP, fo);
                    jmi.addActionListener(this);
                    menu.add(jmi);
                }
            } catch (MalformedURLException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
    
    /** Opens recently closed file, using OpenFile support.
     *
     * Note that method works as action handler for individual submenu items
     * created in fillSubMenu, not for whole RecentFileAction.
     */
    public void actionPerformed(ActionEvent evt) {
        JMenuItem source = (JMenuItem) evt.getSource();
        FileObject fo = (FileObject) source.getClientProperty(FO_PROP);
        if (fo != null) {
        
            try {
                DataObject.find(fo).getCookie(OpenCookie.class).open();
            } catch (Exception ex)
            {
                // do nothing...
            }
        }
    }
    
//    /** Menu that checks its enabled state just before is populated */
//    private static class UpdatingMenu extends JMenu implements DynamicMenuContent {
//
//        private final JComponent[] content = new JComponent[] { this };
//
//        public UpdatingMenu (Action action) {
//            super(action);
//        }
//
//        public JComponent[] getMenuPresenters() {
//            setEnabled(!RecentFiles.getRecentFiles().isEmpty());
//
//            PopupMenuListener[] listeners = getPopupMenu().getPopupMenuListeners();
//            for (int i=0; i<listeners.length; ++i)
//            {
//                System.out.println("popupWillBecomeVisible...on " + listeners[i]);
//                System.out.flush();
//                listeners[i].popupMenuWillBecomeInvisible(null);
//                System.out.println("content: " + getSubElements());
//                System.out.flush();
//            }
//            this.updateUI();
//            return content;
//        }
//
//        public JComponent[] synchMenuPresenters(JComponent[] items) {
//            return getMenuPresenters();
//        }
//    }
    
}

