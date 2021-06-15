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
package com.jaspersoft.ireport.designer.styles;

import com.jaspersoft.ireport.designer.dnd.ReportObjectPaletteTransferable;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.jrtx.TemplateNode;
import com.jaspersoft.ireport.designer.jrtx.TemplateReferenceNode;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTemplateReference;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import org.openide.actions.DeleteAction;
import org.openide.actions.NewAction;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.datatransfer.NewType;
import org.openide.util.datatransfer.PasteType;

/**
 *
 * @author gtoffoli
 */
public class LibraryTemplateReferenceNode extends TemplateReferenceNode {

    public LibraryTemplateReferenceNode(JRSimpleTemplate template, JRTemplateReference reference, Lookup doLkp)
    {
        super (template, reference, doLkp);
    }

    @Override
    public boolean canCut() {
        return true;
    }

    @Override
    public boolean canCopy() {
        return true;
    }

    @Override
    public Action[] getActions(boolean popup) {

        List<Action> actions = new ArrayList<Action>();
        actions.add(SystemAction.get( NewAction.class ));
        actions.add(null);
        actions.add(SystemAction.get( AddTemplateReferenceToReportAction.class ));
        //actions.add(null);
        //actions.add(SystemAction.get( CopyAction.class ));
        //actions.add(SystemAction.get( CutAction.class ));
        //actions.add(SystemAction.get( RenameAction.class ));
        actions.add(null);
        actions.add(SystemAction.get( DeleteAction.class ));

        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public Transferable drag() throws IOException {

        ExTransferable tras = (ExTransferable)super.drag();
        tras.put(new ReportObjectPaletteTransferable(
                    "com.jaspersoft.ireport.designer.styles.DragTemplateReferenceAction",
                    getReference()));

        return tras;
    }


    @Override
    public NewType[] getNewTypes()
    {
        if (getParentNode() instanceof TemplateNode)
        {
            return ((TemplateNode)getParentNode()).getNewTypes();
        }
        return super.getNewTypes();
    }

    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {

        final Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);

        if (null != dropNode) {
            final JRDesignStyle style = dropNode.getLookup().lookup(JRDesignStyle.class);
            final JRTemplateReference reference = dropNode.getLookup().lookup(JRTemplateReference.class);
            final JRSimpleTemplate template = (getParentNode() == null) ? null : getParentNode().getLookup().lookup(JRSimpleTemplate.class);
            if (null != style && template != null) {
                return new PasteType() {

                    @SuppressWarnings("unchecked")
                    public Transferable paste() throws IOException {


                        List list = new ArrayList();
                        JRStyle[] ss = template.getStyles();
                        for (int i=0; i<ss.length; ++i)
                        {
                            list.add((JRDesignStyle)ss[i]);
                        }


                        int currentIndex = -1; //Current position in the list

                        for (int i = 0; i < list.size(); ++i) {
                            JRDesignStyle s = (JRDesignStyle) list.get(i);
                            if (s == style) {
                                currentIndex = i;
                            }
                        }

                        if (currentIndex >= 0) return null;
                        // At this point lastSystemDefinedParameterIndex contains the first valid index
                        // to add a parameter and currentIndex contains the index of the parameter in the list
                        // if present

                            try {

                                // Look for the style in the current template...


                                JRDesignStyle newStyle = ModelUtils.cloneStyle(style);

                                // fix the name...
                                List names = new ArrayList();
                                String base = newStyle.getName();
                                JRStyle[] styles = getTemplate().getStyles();
                                for (int n=0; n<styles.length; ++n)
                                {
                                    names.add( styles[n].getName());
                                }

                                int i=1;
                                while (names.contains(base+i))
                                {
                                    i++;
                                }

                                newStyle.setName(base+i);


                                template.addStyle(newStyle);
                                //AddStyleUndoableEdit undo = new AddStyleUndoableEdit(style, getJasperDesign()); //newIndex
                                //IReportManager.getInstance().addUndoableEdit(undo);
                                ((StylesLibraryChildren)getParentNode().getChildren()).recalculateKeys();

                            } catch (JRException ex) {
                                Exceptions.printStackTrace(ex);
                            }

                        return null;
                    };
                };
            }
            else if (null != reference && template != null) {
                return new PasteType() {

                    @SuppressWarnings("unchecked")
                    public Transferable paste() throws IOException {

                        JRTemplateReference[] ss = template.getIncludedTemplates();
                        for (int i=0; i<ss.length; ++i)
                        {
                            if (ss[i].getLocation() != null &&
                                ss[i].getLocation().equals(reference.getLocation()))
                            {
                                return null;
                            }
                        }
                        template.addIncludedTemplate(reference.getLocation());
                        ((StylesLibraryChildren)getParentNode().getChildren()).recalculateKeys();


                        return null;
                    };
                };
            }
        }
        return null;
    }
}
