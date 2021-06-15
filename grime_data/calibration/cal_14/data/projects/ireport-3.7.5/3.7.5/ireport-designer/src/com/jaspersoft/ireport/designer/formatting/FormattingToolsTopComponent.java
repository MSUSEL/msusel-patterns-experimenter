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
package com.jaspersoft.ireport.designer.formatting;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.formatting.actions.AdaptToParentAction;
import com.jaspersoft.ireport.designer.formatting.actions.AdaptToParentHAction;
import com.jaspersoft.ireport.designer.formatting.actions.AdaptToParentVAction;
import com.jaspersoft.ireport.designer.formatting.actions.AlignBottomAction;
import com.jaspersoft.ireport.designer.formatting.actions.AlignHAxisAction;
import com.jaspersoft.ireport.designer.formatting.actions.AlignLeftAction;
import com.jaspersoft.ireport.designer.formatting.actions.AlignMarginBottomAction;
import com.jaspersoft.ireport.designer.formatting.actions.AlignMarginLeftAction;
import com.jaspersoft.ireport.designer.formatting.actions.AlignMarginRightAction;
import com.jaspersoft.ireport.designer.formatting.actions.AlignMarginTopAction;
import com.jaspersoft.ireport.designer.formatting.actions.AlignRightAction;
import com.jaspersoft.ireport.designer.formatting.actions.AlignTopAction;
import com.jaspersoft.ireport.designer.formatting.actions.AlignVAxisAction;
import com.jaspersoft.ireport.designer.formatting.actions.CenterHorizontallyAction;
import com.jaspersoft.ireport.designer.formatting.actions.CenterInParentAction;
import com.jaspersoft.ireport.designer.formatting.actions.CenterVerticallyAction;
import com.jaspersoft.ireport.designer.formatting.actions.DecreaseHSpaceAction;
import com.jaspersoft.ireport.designer.formatting.actions.DecreaseVSpaceAction;
import com.jaspersoft.ireport.designer.formatting.actions.EqualsHSpaceAction;
import com.jaspersoft.ireport.designer.formatting.actions.EqualsVSpaceAction;
import com.jaspersoft.ireport.designer.formatting.actions.IncreaseHSpaceAction;
import com.jaspersoft.ireport.designer.formatting.actions.IncreaseVSpaceAction;
import com.jaspersoft.ireport.designer.formatting.actions.JoinLeftAction;
import com.jaspersoft.ireport.designer.formatting.actions.JoinRightAction;
import com.jaspersoft.ireport.designer.formatting.actions.OrganizeAsTableAction;
import com.jaspersoft.ireport.designer.formatting.actions.RemoveHSpaceAction;
import com.jaspersoft.ireport.designer.formatting.actions.RemoveVSpaceAction;
import com.jaspersoft.ireport.designer.formatting.actions.SameHeightAction;
import com.jaspersoft.ireport.designer.formatting.actions.SameHeightMaxAction;
import com.jaspersoft.ireport.designer.formatting.actions.SameHeightMinAction;
import com.jaspersoft.ireport.designer.formatting.actions.SameSizeAction;
import com.jaspersoft.ireport.designer.formatting.actions.SameWidthAction;
import com.jaspersoft.ireport.designer.formatting.actions.SameWidthMaxAction;
import com.jaspersoft.ireport.designer.formatting.actions.SameWidthMinAction;
import com.jaspersoft.ireport.designer.outline.OutlineTopComponent;
import java.awt.BorderLayout;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.swing.ActionMap;
import org.openide.awt.UndoRedo;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.Utilities;

/**
 * Top component which displays something.
 */
final class FormattingToolsTopComponent extends TopComponent implements ExplorerManager.Provider {

    private static FormattingToolsTopComponent instance;
    private ToolsPanel toolsPanel = null;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    
    private static final String PREFERRED_ID = "FormattingToolsTopComponent"; // NOI18N

    private FormattingToolsTopComponent() {
        initComponents();
        setName(I18n.getString("CTL_FormattingToolsTopComponent")); // NOI18N
        setToolTipText(I18n.getString("HINT_FormattingToolsTopComponent")); // NOI18N
//        setIcon(Utilities.loadImage(ICON_PATH, true));
        
        toolsPanel = new ToolsPanel();
        //toolsPanel.setLayout(new ToolsLayoutManager());
        add(toolsPanel, BorderLayout.CENTER);
                
        toolsPanel.add( SystemAction.get(AlignTopAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(AlignBottomAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(AlignLeftAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(AlignRightAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(AlignHAxisAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(AlignVAxisAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(AlignMarginTopAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(AlignMarginBottomAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(AlignMarginLeftAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(AlignMarginRightAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(OrganizeAsTableAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(JoinLeftAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(JoinRightAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(EqualsHSpaceAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(IncreaseHSpaceAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(DecreaseHSpaceAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(RemoveHSpaceAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(EqualsVSpaceAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(IncreaseVSpaceAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(DecreaseVSpaceAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(RemoveVSpaceAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(SameWidthAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(SameWidthMinAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(SameWidthMaxAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(SameHeightAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(SameHeightMinAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(SameHeightMaxAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(SameSizeAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(AdaptToParentAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(AdaptToParentHAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(AdaptToParentVAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(CenterHorizontallyAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(CenterVerticallyAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        toolsPanel.add( SystemAction.get(CenterInParentAction.class).createContextAwareInstance(Utilities.actionsGlobalContext()));
        
        
        toolsPanel.setTextLabels(IReportManager.getPreferences().getBoolean("formatting_tools_show_labels",true));
        associateLookup( ExplorerUtils.createLookup(getExplorerManager(), new ActionMap()) );
        
    
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized FormattingToolsTopComponent getDefault() {
        if (instance == null) {
            instance = new FormattingToolsTopComponent();
        }
        return instance;
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();
    }

    @Override
    protected void componentDeactivated() {
        super.componentDeactivated();
    }

    /**
     * Obtain the FormattingToolsTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized FormattingToolsTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(FormattingToolsTopComponent.class.getName()).warning(
                    I18n.getString("FormattingToolsTopComponent.Message.Error", PREFERRED_ID));

            return getDefault();
        }
        if (win instanceof FormattingToolsTopComponent) {
            return (FormattingToolsTopComponent) win;
        }
        Logger.getLogger(FormattingToolsTopComponent.class.getName()).warning(
                  I18n.getString("FormattingToolsTopComponent.Message.Warning", PREFERRED_ID));

        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
    // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
    // TODO add custom code on component closing
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return FormattingToolsTopComponent.getDefault();
        }
    }

    public ExplorerManager getExplorerManager() {
        return OutlineTopComponent.getDefault().getExplorerManager();
    }
    
    @Override
    public UndoRedo getUndoRedo(){   
        if (IReportManager.getInstance().getActiveVisualView() != null) return IReportManager.getInstance().getActiveVisualView().getUndoRedo();
        return super.getUndoRedo();   
    } 
 
}
