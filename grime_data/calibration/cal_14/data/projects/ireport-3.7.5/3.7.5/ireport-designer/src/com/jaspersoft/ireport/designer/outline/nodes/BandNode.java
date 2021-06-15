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
package com.jaspersoft.ireport.designer.outline.nodes;

import com.jaspersoft.ireport.designer.sheet.properties.GroupExpressionProperty;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.NotRealElementNode;
import com.jaspersoft.ireport.designer.actions.AddAnotherDetailBandAction;
import com.jaspersoft.ireport.designer.actions.AddAnotherGroupFooterBandAction;
import com.jaspersoft.ireport.designer.actions.AddAnotherGroupHeaderBandAction;
import com.jaspersoft.ireport.designer.actions.DeleteBandAction;
import com.jaspersoft.ireport.designer.actions.DeleteGroupAction;
import com.jaspersoft.ireport.designer.actions.MaximizeBackgroundAction;
import com.jaspersoft.ireport.designer.actions.MaximizeBandAction;
import com.jaspersoft.ireport.designer.actions.MoveGroupDownAction;
import com.jaspersoft.ireport.designer.actions.MoveGroupUpAction;
import com.jaspersoft.ireport.designer.dnd.DnDUtilities;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.sheet.properties.BandPrintWhenExpressionProperty;
import com.jaspersoft.ireport.designer.sheet.properties.BooleanProperty;
import com.jaspersoft.ireport.designer.sheet.properties.ByteProperty;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.base.JRBaseBand;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import org.openide.ErrorManager;
import org.openide.actions.PasteAction;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.actions.NodeAction;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class BandNode  extends IRIndexedNode implements PropertyChangeListener, GroupNode {

    private final JasperDesign jd;
    private final JRDesignBand band;
    private final JRDesignGroup group;
    
     public BandNode(JasperDesign jd, JRDesignBand band, Lookup doLkp) {
        this(new ElementContainerChildren(jd, band, doLkp), jd, band, doLkp);
    }

    public BandNode(ElementContainerChildren pc, JasperDesign jd, JRDesignBand band,Lookup doLkp) {
        super(pc, pc.getIndex(), new ProxyLookup( Lookups.fixed(jd, band), doLkp) );
        this.jd = jd;
        this.band = band;
        
        if (ModelUtils.isGroupHeader(band, jd))
        {
            setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/groupheader-16.png");
        }
        else if (ModelUtils.isGroupFooter(band, jd))
        {
            setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/groupfooter-16.png");
        }
        else
        {
            setIconBaseWithExtension("com/jaspersoft/ireport/designer/resources/band-16.png");
        }
        
        this.band.getEventSupport().addPropertyChangeListener(this);
        
        if (band.getOrigin().getBandType() == JROrigin.DETAIL)
        {
            ((JRDesignSection)jd.getDetailSection()).getEventSupport().addPropertyChangeListener(this);
        }

        this.group = ModelUtils.getGroupFromBand(jd, band);
        
        if (group != null)
        {
             group.getEventSupport().addPropertyChangeListener(this);
             ((JRDesignSection)group.getGroupHeaderSection()).getEventSupport().addPropertyChangeListener(this);
             ((JRDesignSection)group.getGroupFooterSection()).getEventSupport().addPropertyChangeListener(this);
        }
        
        setDisplayName ( ModelUtils.nameOf(band, jd));
        
        this.addNodeListener(new NodeListener() {

            public void childrenAdded(NodeMemberEvent ev) {}
            public void childrenRemoved(NodeMemberEvent ev) {}
            public void nodeDestroyed(NodeEvent ev) {}
            public void propertyChange(PropertyChangeEvent evt) {}

            @SuppressWarnings("unchecked")
            public void childrenReordered(NodeReorderEvent ev) {
                // Fire an event now...

                List elements = getBand().getChildren();
                int[] permutations = ev.getPermutation();
                
                Object[] elementsArray = new Object[elements.size()];
                for (int i=0; i<elementsArray.length; ++i)
                {
                    elementsArray[permutations[i]] = elements.get(i);
                }
                elements.clear();
                for (int i=0; i<elementsArray.length; ++i)
                {
                    elements.add(elementsArray[i]);
                }
                
                getBand().getEventSupport().firePropertyChange( JRDesignBand.PROPERTY_CHILDREN, null, getBand().getChildren());
            }
        });
    }
    
    @Override
    public String getHtmlDisplayName()
    {
        return getDisplayName();
    }

    public JRDesignDataset getDataset() {
       return jd.getMainDesignDataset();
    }

    public JRDesignGroup getGroup() {

        if (band.getOrigin().getGroupName() != null)
        {
            return (JRDesignGroup) getDataset().getGroupsMap().get(band.getOrigin().getGroupName());
        }
        return null;
    }

    /*
    @Override
    @SuppressWarnings("unchecked")
    public Cookie getCookie(Class clazz) {
        Children ch = getChildren();

        if (clazz.isInstance(ch)) {
            return (Cookie) ch;
        }
        
        if (clazz.isAssignableFrom( GenericCookie.JRDesignBandCookie.class ))
        {
            return new GenericCookie.JRDesignBandCookie(this.getBand());
        }
        
        return super.getCookie(clazz);
    }
    */
    
    
    
    

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        
        Sheet.Set bandPropertiesSet = Sheet.createPropertiesSet();
        bandPropertiesSet.setName("BAND_PROPERTIES");
        bandPropertiesSet.setDisplayName(I18n.getString("BandNode.Property.Bandproperties"));
        bandPropertiesSet.put(new HeightProperty(band, jd));
        bandPropertiesSet.put(new BandPrintWhenExpressionProperty(band, jd.getMainDesignDataset()));
        bandPropertiesSet.put(new SplitTypeProperty(band));
        bandPropertiesSet.put(new SplitAllowedProperty(band));
        

        sheet.put(bandPropertiesSet);
        
        if (group != null)
        {
            Sheet.Set groupPropertiesSet = Sheet.createPropertiesSet();
            groupPropertiesSet.setName("GROUP_PROPERTIES");
            groupPropertiesSet.setDisplayName(I18n.getString("BandNode.Property.Groupproperties"));
            groupPropertiesSet = fillGroupPropertySet(groupPropertiesSet, jd.getMainDesignDataset(), group);
        
            sheet.put(groupPropertiesSet);
        }
        return sheet;
    }
    
    
    public static Sheet.Set fillGroupPropertySet(Sheet.Set groupPropertiesSet, JRDesignDataset dataset, JRDesignGroup group)
    {
        
        groupPropertiesSet.put(new GroupNameProperty(group, dataset));
        groupPropertiesSet.put(new GroupExpressionProperty(group, dataset));
        
        if (dataset.isMainDataset())
        {
            groupPropertiesSet.put(new StartNewPageProperty(group));
            groupPropertiesSet.put(new StartNewColumnProperty(group));
            groupPropertiesSet.put(new ResetPageNumberProperty(group));
            groupPropertiesSet.put(new ReprintHeaderProperty(group));
            groupPropertiesSet.put(new MinHeightToStartNewPageProperty(group));
            groupPropertiesSet.put(new MinHeightToStartNewPageProperty(group));
            groupPropertiesSet.put(new FooterPositionProperty(group));
            groupPropertiesSet.put(new KeepTogetherProperty(group));
            
        }
        
        return groupPropertiesSet;
    }
    
    public JRDesignBand getBand() {
        return band;
    }
    
    public JasperDesign getJasperDesign() {
        return this.jd;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
        if (evt.getPropertyName() == null) return;
        
        if (ModelUtils.containsProperty(  this.getPropertySets(), evt.getPropertyName()))
        {
            if (evt.getPropertyName().equals(JRBaseBand.PROPERTY_SPLIT_TYPE))
            {
                this.firePropertyChange(JRBaseBand.PROPERTY_SPLIT_ALLOWED, evt.getOldValue(), evt.getNewValue() );
            }
            this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
        }
        
        if (evt.getPropertyName().equals(  JRDesignGroup.PROPERTY_NAME) ||
            evt.getPropertyName().equals( JRDesignSection.PROPERTY_BANDS))
        {
            String s = ModelUtils.nameOf(band, jd);
            setDisplayName( s );
            this.fireNameChange(null, getDisplayName());
        }

        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void createPasteTypes(Transferable t, List s) {
        super.createPasteTypes(t, s);
        PasteType paste = getDropType(t, DnDConstants.ACTION_MOVE, -1);
        if (null != paste) {
            s.add(paste);
        }
    }

    
    @Override
    public Action[] getActions(boolean popup) {
        java.util.List<Action> list = new ArrayList<Action>();
        
        list.add( SystemAction.get(PasteAction.class));
        list.add( SystemAction.get(RefreshNodes.class));
        
        if (getBand().getOrigin().getBandType() == JROrigin.BACKGROUND)
        {
            list.add(SystemAction.get(MaximizeBackgroundAction.class));
        }
        else
        {
            list.add(SystemAction.get(MaximizeBandAction.class));
        }

        if (getBand().getOrigin().getBandType() == JROrigin.DETAIL)
        {
            list.add(SystemAction.get(AddAnotherDetailBandAction.class));
        }

        if (group != null)
        {
            list.add( null );
            list.add( SystemAction.get(MoveGroupUpAction.class));
            list.add( SystemAction.get(MoveGroupDownAction.class));
            list.add( DeleteGroupAction.getInstance() );
            list.add(null);
            list.add( SystemAction.get(AddAnotherGroupHeaderBandAction.class));
            list.add( SystemAction.get(AddAnotherGroupFooterBandAction.class));
        }
        
        list.add( DeleteBandAction.getInstance());
        
        return list.toArray(new Action[list.size()]);
    }

    public static final class RefreshNodes extends NodeAction {

            public String getName() {
                return I18n.getString("BandNode.Property.Refreshnodes");
            }

            public HelpCtx getHelpCtx() {
                return null;
            }

            protected void performAction(Node[] activatedNodes) {
                ((ElementContainerChildren)activatedNodes[0].getChildren()).recalculateKeys();
            }

            protected boolean enable(Node[] activatedNodes) {
                return activatedNodes.length == 1 && activatedNodes[0] instanceof BandNode;
            }
    }

    @Override
    public boolean canDestroy() {
        return false;
    }
    
    /*
     * @return false to signal that the customizer should not be used.
     *  Subclasses can override this method to enable customize action
     *  and use customizer provided by this class.
     */
    @Override
    public boolean hasCustomizer() {
        return true;
    }
    
    /**
     *  We can add element groups and new elements here.
     */
    //@Override
    //public NewType[] getNewTypes()
    //{
    //  return NewTypesUtils.getNewType( NewTypesUtils.FIELD, this);
    //}
    
    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {

        Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        Node[] dropNodes = NodeTransfer.nodes(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        int dropAction = DnDUtilities.getTransferAction(t);

        if (dropNode == null)
        {
            ElementPasteType.setLastPastedNodes(dropNodes);
        }
        
        if (null != dropNode && !(dropNode instanceof NotRealElementNode)) {
            JRDesignElement element = dropNode.getLookup().lookup(JRDesignElement.class);
            
            if (null != element) {
                
                return new ElementPasteType( element.getElementGroup(),
                                             (JRElementGroup)getBand(),
                                             element,dropAction,this);
            }
            
            if (dropNode instanceof ElementGroupNode)
            {
                JRDesignElementGroup g = ((ElementGroupNode)dropNode).getElementGroup();
                return new ElementPasteType( g.getElementGroup(),
                                             (JRElementGroup)getBand(),
                                             g,dropAction,this);
            }
            else
            {
                
            }
        }
        return null;
    }
    
    /***************  SHEET PROPERTIES DEFINITIONS **********************/
    
    /**
     *  Class to manage the JRDesignBand.PROPERTY_HEIGHT property
     */
    private static final class HeightProperty extends PropertySupport
    {
            private final JasperDesign jasperDesign;
            private final JRDesignBand band;
        
            @SuppressWarnings("unchecked")
            public HeightProperty(JRDesignBand band, JasperDesign jd)
            {
                super(JRDesignBand.PROPERTY_HEIGHT,Integer.class, I18n.getString("BandNode.Property.Bandheight"), I18n.getString("BandNode.Property.Bandheightdetail"), true, true);
                this.jasperDesign = jd;
                this.band = band;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return band.getHeight();
            }

            // TODO: check page height consistency
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Integer)
                {
                    Integer oldValue = band.getHeight();
                    Integer newValue = (Integer)val;
                    
                    // Check if the height is too big....
                    int maxBandHeight = ModelUtils.getMaxBandHeight(band, jasperDesign);
                    if (newValue < 0 || newValue > maxBandHeight)
                    {
                        IllegalArgumentException iae = annotateException(I18n.getString("BandNode.Property.bandheightmessagge",maxBandHeight )); 
                        throw iae; 
                    }
                    
                    band.setHeight(newValue);
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                band,
                                I18n.getString("Global.Property.Height"), 
                                Integer.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
                }
            }
            
            public IllegalArgumentException annotateException(String msg)
            {
                IllegalArgumentException iae = new IllegalArgumentException(msg); 
                ErrorManager.getDefault().annotate(iae, 
                                        ErrorManager.EXCEPTION,
                                        msg,
                                        msg, null, null); 
                return iae;
            }
    }
    
    
    /**
     *  Class to manage the JasperDesign.PROPERTY_IGNORE_PAGINATION property
     */
    @SuppressWarnings("deprecation")
    private final class SplitAllowedProperty extends PropertySupport
    {
            private final JRDesignBand band;
        
            @SuppressWarnings("unchecked")
            public SplitAllowedProperty(JRDesignBand band)
            {
                super(JRBaseBand.PROPERTY_SPLIT_ALLOWED,Boolean.class, I18n.getString("BandNode.Property.Splitallowed"), I18n.getString("BandNode.Property.Splitalloweddetail"), true, true);
                this.band = band;
            }

        @Override
        public String getHtmlDisplayName() {
            return "<html><s>" + getDisplayName()+"</s>";
        }


            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return band.isSplitAllowed();
            }

            @Override
            public boolean isDefaultValue() {
                return band.isSplitAllowed() == true;
            }

            @Override
            public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
                setPropertyValue(true);
            }

            @Override
            public boolean supportsDefaultValue() {
                return true;
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Boolean)
                {
                    setPropertyValue((Boolean)val);
                }
            }
            
            private void setPropertyValue(boolean val)
            {
                Boolean oldValue = band.isSplitAllowed();
                Boolean newValue = val;
                band.setSplitAllowed(newValue);
                
                ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                band,
                                "SplitAllowed", 
                                Boolean.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                BandNode.this.firePropertyChange(JRDesignBand.PROPERTY_SPLIT_TYPE, oldValue, newValue);
                IReportManager.getInstance().addUndoableEdit(urob);
            }
    }
    
    
    
    /**
     *  Class to manage the JRDesignParameter.PROPERTY_NAME property
     */
    public static final class GroupNameProperty extends PropertySupport.ReadWrite {

        private final JRDesignGroup group;
        private final JRDesignDataset dataset;

        @SuppressWarnings("unchecked")
        public GroupNameProperty(JRDesignGroup group, JRDesignDataset dataset)
        {
            super(JRDesignGroup.PROPERTY_NAME, String.class,
                  I18n.getString("BandNode.Property.Name"),
                  I18n.getString("BandNode.Property.Namedetail"));
            this.group = group;
            this.dataset = dataset;
            this.setValue("oneline", Boolean.TRUE);
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return group.getName();
        }

        @SuppressWarnings("unchecked")
        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

            if (val == null || val.equals(""))
            {
                IllegalArgumentException iae = annotateException(I18n.getString("BandNode.Property.NameMessage")); 
                throw iae; 
            }

            String s = val+"";

            List<JRDesignGroup> currentGroups = null;
            currentGroups = (List<JRDesignGroup>)dataset.getGroupsList();
            for (JRDesignGroup g : currentGroups)
            {
                if (g != group && g.getName().equals(s))
                {
                    IllegalArgumentException iae = annotateException(I18n.getString("BandNode.Property.Nameexist")); 
                    throw iae; 
                }
            }
            
            String oldName = group.getName();
            dataset.getGroupsMap().remove(oldName);
            dataset.getGroupsMap().put(s, group);
            group.setName(s);

            JRDesignVariable var = (JRDesignVariable) dataset.getVariablesMap().get(oldName + "_COUNT");
            var.setName(s + "_COUNT");
            dataset.getVariablesMap().remove(oldName + "_COUNT");
            dataset.getVariablesMap().put(s + "_COUNT", var);

            dataset.getEventSupport().firePropertyChange(JRDesignDataset.PROPERTY_VARIABLES, null, null);


            ObjectPropertyUndoableEdit opue = new ObjectPropertyUndoableEdit(
                    group, "Name", String.class, oldName, group.getName());

            IReportManager.getInstance().addUndoableEdit(opue);

        }

        public IllegalArgumentException annotateException(String msg)
        {
            IllegalArgumentException iae = new IllegalArgumentException(msg); 
            ErrorManager.getDefault().annotate(iae, 
                                    ErrorManager.EXCEPTION,
                                    msg,
                                    msg, null, null); 
            return iae;
        }
    }
    
    
    /**
     *  Class to manage the JRDesignGroup.PROPERTY_START_NEW_PAGE property
     */
    private static final class StartNewPageProperty extends PropertySupport
    {
            private final JRDesignGroup group;
        
            @SuppressWarnings("unchecked")
            public StartNewPageProperty(JRDesignGroup group)
            {
                super(JRDesignGroup.PROPERTY_START_NEW_PAGE,Boolean.class, I18n.getString("BandNode.Property.NewPage"), I18n.getString("BandNode.Property.NewPagedetail"), true, true);
                this.group = group;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return group.isStartNewPage();
            }

            @Override
            public boolean isDefaultValue() {
                return group.isStartNewPage() == false;
            }

            @Override
            public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
                setPropertyValue(false);
            }

            @Override
            public boolean supportsDefaultValue() {
                return true;
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Boolean)
                {
                    setPropertyValue((Boolean)val);
                }
            }
            
            private void setPropertyValue(boolean val)
            {
                Boolean oldValue = group.isStartNewPage();
                Boolean newValue = val;
                group.setStartNewPage(newValue);
                
                ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                group,
                                "StartNewPage", 
                                Boolean.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
            }
    }
    
    /**
     *  Class to manage the JRDesignGroup.PROPERTY_START_NEW_PAGE property
     */
    private static final class StartNewColumnProperty extends PropertySupport
    {
            private final JRDesignGroup group;
        
            @SuppressWarnings("unchecked")
            public StartNewColumnProperty(JRDesignGroup group)
            {
                super(JRDesignGroup.PROPERTY_START_NEW_COLUMN,Boolean.class, I18n.getString("BandNode.Property.NewCol"), I18n.getString("BandNode.Property.NewColdetail"), true, true);
                this.group = group;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return group.isStartNewColumn();
            }

            @Override
            public boolean isDefaultValue() {
                return group.isStartNewColumn() == false;
            }

            @Override
            public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
                setPropertyValue(false);
            }

            @Override
            public boolean supportsDefaultValue() {
                return true;
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Boolean)
                {
                    setPropertyValue((Boolean)val);
                }
            }
            
            private void setPropertyValue(boolean val)
            {
                Boolean oldValue = group.isStartNewColumn();
                Boolean newValue = val;
                group.setStartNewColumn(newValue);
                
                ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                group,
                                "StartNewColumn", 
                                Boolean.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
            }
    }
    
    /**
     *  Class to manage the JRDesignGroup.PROPERTY_RESET_PAGE_NUMBER property
     */
    private static final class ResetPageNumberProperty extends PropertySupport
    {
            private final JRDesignGroup group;
        
            @SuppressWarnings("unchecked")
            public ResetPageNumberProperty(JRDesignGroup group)
            {
                super(JRDesignGroup.PROPERTY_RESET_PAGE_NUMBER,Boolean.class, I18n.getString("BandNode.Property.ResetPageNumber"), I18n.getString("BandNode.Property.ResetPageNumberdetail"), true, true);
                this.group = group;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return group.isResetPageNumber();
            }

            @Override
            public boolean isDefaultValue() {
                return group.isResetPageNumber() == false;
            }

            @Override
            public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
                setPropertyValue(false);
            }

            @Override
            public boolean supportsDefaultValue() {
                return true;
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Boolean)
                {
                    setPropertyValue((Boolean)val);
                }
            }
            
            private void setPropertyValue(boolean val)
            {
                Boolean oldValue = group.isResetPageNumber();
                Boolean newValue = val;
                group.setResetPageNumber(newValue);
                
                ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                group,
                                "ResetPageNumber", 
                                Boolean.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
            }
    }
    
    /**
     *  Class to manage the JRDesignGroup.PROPERTY_REPRINT_HEADER_ON_EACH_PAGE property
     */
    private static final class ReprintHeaderProperty extends PropertySupport
    {
            private final JRDesignGroup group;
        
            @SuppressWarnings("unchecked")
            public ReprintHeaderProperty(JRDesignGroup group)
            {
                super(JRDesignGroup.PROPERTY_REPRINT_HEADER_ON_EACH_PAGE,Boolean.class, I18n.getString("BandNode.Property.ReprintHeader"), I18n.getString("BandNode.Property.ReprintHeaderdetail"), true, true);
                this.group = group;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return group.isReprintHeaderOnEachPage();
            }

            @Override
            public boolean isDefaultValue() {
                return group.isReprintHeaderOnEachPage() == false;
            }

            @Override
            public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
                setPropertyValue(false);
            }

            @Override
            public boolean supportsDefaultValue() {
                return true;
            }

            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Boolean)
                {
                    setPropertyValue((Boolean)val);
                }
            }
            
            private void setPropertyValue(boolean val)
            {
                Boolean oldValue = group.isReprintHeaderOnEachPage();
                Boolean newValue = val;
                group.setReprintHeaderOnEachPage(newValue);
                
                ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                group,
                                "ReprintHeaderOnEachPage", 
                                Boolean.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
            }
    }
    
    
    /**
     *  Class to manage the JRDesignBand.PROPERTY_HEIGHT property
     */
    private static final class MinHeightToStartNewPageProperty extends PropertySupport
    {
            private final JRDesignGroup group;
        
            @SuppressWarnings("unchecked")
            public MinHeightToStartNewPageProperty(JRDesignGroup group)
            {
                super(JRDesignGroup.PROPERTY_MIN_HEIGHT_TO_START_NEW_PAGE,Integer.class, I18n.getString("BandNode.Property.MinHeight"), I18n.getString("BandNode.Property.MinHeightdetail"), true, true);
                this.group = group;
            }
            
             @Override
            public boolean isDefaultValue() {
                return group.getMinHeightToStartNewPage() == 0;
            }

            @Override
            public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
                setPropertyValue(0);
            }

            @Override
            public boolean supportsDefaultValue() {
                return true;
            }
            
            public Object getValue() throws IllegalAccessException, InvocationTargetException {
                return group.getMinHeightToStartNewPage();
            }

            // TODO: check page height consistency
            public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (val instanceof Integer)
                {
                    setPropertyValue((Integer)val);
                }
            }
            
            private void setPropertyValue(int val) throws IllegalArgumentException
            {
                    Integer oldValue = group.getMinHeightToStartNewPage();
                    Integer newValue = val;
                    
                    // Check if the height is too big....
                    if (newValue < 0)
                    {
                        IllegalArgumentException iae = annotateException(I18n.getString("BandNode.Property.Nodemessage")); 
                        throw iae; 
                    }
                    
                    group.setMinHeightToStartNewPage(newValue);
                    ObjectPropertyUndoableEdit urob =
                            new ObjectPropertyUndoableEdit(
                                group,
                                "MinHeightToStartNewPage", 
                                Integer.TYPE,
                                oldValue,newValue);
                    // Find the undoRedo manager...
                    IReportManager.getInstance().addUndoableEdit(urob);
            }
            
            public IllegalArgumentException annotateException(String msg)
            {
                IllegalArgumentException iae = new IllegalArgumentException(msg); 
                ErrorManager.getDefault().annotate(iae, 
                                        ErrorManager.EXCEPTION,
                                        msg,
                                        msg, null, null); 
                return iae;
            }
    }

    
    private static final class SplitTypeProperty extends PropertySupport
    {

        private final JRDesignBand band;
        private ComboBoxPropertyEditor editor;

        @SuppressWarnings(value = "unchecked")
        public SplitTypeProperty(JRDesignBand band) {
            super(JRDesignBand.PROPERTY_SPLIT_TYPE, Byte.class,
                    I18n.getString("band.property.splitType.name"),
                    I18n.getString("band.property.splitType.description"), true, true);
            this.band = band;
            setValue("suppressCustomEditor", Boolean.TRUE);
        }

        @Override
        @SuppressWarnings(value = "unchecked")
        public PropertyEditor getPropertyEditor() {
            if (editor == null) {
                ArrayList l = new ArrayList();
                l.add(new Tag(null, "<Default>"));
                l.add(new Tag(new Byte(JRDesignBand.SPLIT_TYPE_IMMEDIATE), I18n.getString("band.property.splitType.immediate")));
                l.add(new Tag(new Byte(JRDesignBand.SPLIT_TYPE_PREVENT), I18n.getString("band.property.splitType.prevent")));
                l.add(new Tag(new Byte(JRDesignBand.SPLIT_TYPE_STRETCH), I18n.getString("band.property.splitType.stretch")));
                editor = new ComboBoxPropertyEditor(false, l);
            }
            return editor;
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return band.getSplitType();
        }

        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

            if (IReportManager.getPreferences().getBoolean("designer_debug_mode", false))
            {
                System.out.println(new java.util.Date() + ": setting SplitType to: " + val + ". If the value is unattended or null, please report this notification to http://jasperforge.org/plugins/mantis/view.php?id=4139");
                Thread.dumpStack();
            }
            
            if (val == null || val instanceof Byte) {

                Byte oldValue = band.getSplitType();
                Byte newValue = (Byte) val;
                band.setSplitType(newValue);
                ObjectPropertyUndoableEdit urob = new ObjectPropertyUndoableEdit(band, "SplitType", Byte.class, oldValue, newValue);
                IReportManager.getInstance().addUndoableEdit(urob);
            }
        }

        @Override
        public boolean isDefaultValue() {
            return band.getSplitType() == null;
        }

        @Override
        public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
            setValue(null);
        }

        @Override
        public boolean supportsDefaultValue() {
            return true;
        }
    }
    
    private static final class FooterPositionProperty extends ByteProperty
    {
        private final JRDesignGroup group;

        @SuppressWarnings("unchecked")
        public FooterPositionProperty(JRDesignGroup group)
        {
            super(group);
            this.group = group;
        }
        @Override
        public String getName()
        {
            return JRDesignGroup.PROPERTY_FOOTER_POSITION;
        }

        @Override
        public String getDisplayName()
        {
            return I18n.getString("Global.Property.FooterPosition");
        }

        @Override
        public String getShortDescription()
        {
            return I18n.getString("Global.Property.FooterPosition.description");
        }

        @Override
        public List getTagList()
        {
            List tags = new java.util.ArrayList();
            tags.add(new Tag(new Byte(JRDesignGroup.FOOTER_POSITION_NORMAL), I18n.getString("Global.Property.FooterPosition.normal")));
            tags.add(new Tag(new Byte(JRDesignGroup.FOOTER_POSITION_STACK_AT_BOTTOM), I18n.getString("Global.Property.FooterPosition.stackAtBottom")));
            tags.add(new Tag(new Byte(JRDesignGroup.FOOTER_POSITION_FORCE_AT_BOTTOM), I18n.getString("Global.Property.FooterPosition.forceAtBottom")));
            tags.add(new Tag(new Byte(JRDesignGroup.FOOTER_POSITION_COLLATE_AT_BOTTOM), I18n.getString("Global.Property.FooterPosition.collateAtBottom")));
            
            return tags;
        }

        @Override
        public Byte getByte()
        {
            return group.getFooterPosition();
        }

        @Override
        public Byte getOwnByte()
        {
            return group.getFooterPosition();
        }

        @Override
        public Byte getDefaultByte()
        {
            return JRDesignGroup.FOOTER_POSITION_NORMAL;
        }

        @Override
        public void setByte(Byte position)
        {
            group.setFooterPosition(position);
        }

    }


    /**
     *  Class to manage the JRDesignGroup.PROPERTY_START_NEW_PAGE property
     */
    private static final class KeepTogetherProperty extends BooleanProperty
    {

        private final JRDesignGroup group;

        @SuppressWarnings("unchecked")
        public KeepTogetherProperty(JRDesignGroup group)
        {
            super(group);
            this.group = group;
        }

         @Override
        public String getName()
        {
            return JRDesignGroup.PROPERTY_KEEP_TOGETHER;
        }

        @Override
        public String getDisplayName()
        {
            return I18n.getString("Global.Property.KeepTogether");
        }

        @Override
        public String getShortDescription()
        {
            return I18n.getString("Global.Property.KeepTogether.description");
        }

        @Override
        public Boolean getBoolean() {
            return getOwnBoolean();
        }

        @Override
        public Boolean getOwnBoolean() {
            return group.isKeepTogether();
        }

        @Override
        public Boolean getDefaultBoolean() {
            return false;
        }

        @Override
        public void setBoolean(Boolean value) {
            if (value == null)
            {
                value = false;
            }
            group.setKeepTogether(value);
        }

    }



    
}
