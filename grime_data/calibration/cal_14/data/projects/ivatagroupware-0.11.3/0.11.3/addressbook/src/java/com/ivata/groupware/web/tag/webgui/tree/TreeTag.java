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
package com.ivata.groupware.web.tag.webgui.tree;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.swing.tree.TreeModel;

import com.ivata.groupware.web.tree.DefaultTreeNodeRenderer;
import com.ivata.groupware.web.tree.TreeNode;
import com.ivata.groupware.web.tree.TreeNodeRenderer;
import com.ivata.mask.util.CollectionHandling;
import com.ivata.mask.web.tag.webgui.ControlTag;
import com.ivata.mask.web.theme.ThemeUnsupportedException;


/**
 * <p>Create a tree from a {@link javax.swing.tree.TreeModel TreeModel}.</p>
 * <p>This tree can be displayed is displayed as an HTML table with links on
 * each
 * node.</p>
 * <p><strong>Tag attributes:</strong><br/>
 * <table cellpadding='2' cellspacing='5' border='0' align='center'
 * width='85%'>
 *   <tr class='TableHeadingColor'>
 *     <th>attribute</th>
 *     <th>reqd.</th>
 *     <th>param. class</th>
 *     <th width='100%'>description</th>
 *   </tr>
 *   <tr class='TableRowColor'>
 *     <td>defaultOpen</td>
 *     <td>true</td>
 *     <td>boolean</td>
 *     <td>Set to <code>true</code> if you want tree nodes to be open by
 * default. Otherwise they will be closed.</td>
 *   </tr>
 *   <tr class='TableRowColor'>
 *     <td>model</td>
 *     <td>true</td>
 *     <td>{@link javax.swing.tree.TreeModel javax.swing.tree.TreeModel}</td>
 *     <td>This model contains the data source for the tree. To use any
 * datasource
 * with this tree control, you should first create a class which implements
 * {@link javax.swing.tree,TreeModel TreeModel}.</td>
 *   </tr>
 *   <tr class='TableRowColor'>
 *     <td>renderer</td>
 *     <td>false</td>
 *     <td>{@link com.ivata.groupware.web.tree.TreeNodeRenderer
 * com.ivata.groupware.web.tree.TreeNodeRenderer}</td>
 *     <td>This object controls the appearance of each node in the tree,
 * usually by parsing sections from the {@link
 * com.ivata.groupware.web.theme.Theme
 * Theme}.<br/>
 * If you do not use this attribute, an instance of {@link
 * com.ivata.groupware.web.DefaultTreeNodeRenderer
 * DefaultTreeNodeRenderer} is created and applied.</td>
 *   </tr>
 *   <tr class='TableRowColor'>
 *     <td>treeName</td>
 *     <td>true</td>
 *     <td><code>String</code></td>
 *     <td>Specifies a unique identifier for this tree, which is used to store
 * the state of each foler (open/closed).</td>
 *   </tr>
 *   <tr class='TableRowColor'>
 *     <td>userName</td>
 *     <td>true</td>
 *     <td><code>String</code></td>
 *     <td>Name of the user for whom to draw the tree. The state of each node is
 * stored for this user and the appearance in recalled the next time the tree is
 * drawn.</td>
 *   </tr>
 * </table>
 * </p>
 *
 * @since   2001-12-15
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class TreeTag extends ControlTag {
    /**
     * <p>This is the special property used to identify the location
     * of the children in the open tag.</p>
     *
     * <p><strong>Note</strong> that this has to be specified exactly, with <u>no
     * spaces</u>.</p>
     */
    static final String childrenProperty = "treeChildren";
    /**
     * <p>Property declaration for tag attribute: treeName.</p>
     */
    private String treeName = null;
    /**
     * <p>Specifies the id of a folder you wish to open.</p>
     */
    private Integer openFolder = null;
    /**
     * <p>Specifies the id of a folder you wish to open.</p>
     */
    private Integer closeFolder = null;
    /**
     * <p>Decides whether folders should be open or closed by default.</p>
     */
    private boolean defaultOpen = false;
    /**
     * <p>if you set up name of form it will submit this form when you
     * open or close folder, otherwise it will make link to same page with
     * parameters.</p>
     */
    private String formName = null;
    /**
     * <p>Property declaration for tag attribute: model.</p>
     */
    private TreeModel model = null;
    /**
     * <p>This renderer actually draws each node of the tree. If you do
     * not set a
     * renderer, then a <code>DefaultTreeNodeRenderer</code> is created
     * and
     * applied.</p>
     */
    private TreeNodeRenderer renderer;

    /**
     * <p>Default constructor.</p>

     */
    public TreeTag() {
        super();
    }

    /**
     * <p>This method is called when the JSP engine encounters the start
     * tag,
     * after the attributes are processed.<p>
     *
     * <p>Scripting variables (if any) have their values set here.</p>
     *
     * @return <code>SKIP_BODY</code> if this tag has no body or it
     * should be skipped, otherwise <code>EVAL_BODY_BUFFERED</code>
     * @throws JspException if there is an error retrieving the
     * navigation
     * object.
     * @throws JspException if there is no settings object in the
     * session.
     * @throws JspException if there is an error wrting to
     * <code>out.print(
     * )</code>
     * @throws ThemeUnsupportedException if theme elements are missing.
     */
    public int doStartTag() throws JspException,ThemeUnsupportedException {
        super.doStartTag();
        try {
            // now do we have a valid renderer specified? if not, use the default
            if (renderer == null) {
                renderer = new DefaultTreeNodeRenderer();
            }
            renderer.setTreeTag(this);
            JspWriter out = pageContext.getOut();

            renderer.initialize(pageContext.getSession(),
                (javax.servlet.http.HttpServletRequest) pageContext.getRequest(),
                out, pageContext);
            // see if the selected string contains an array representation
            // TODO: see if we can't make two attributes - one a list and the other a string
            int nSelectedStringLength;

            // create the full tree ( null parent )
            out.println(createChildren(model.getRoot(), 0));
            renderer.finalize(pageContext.getSession(),
                (javax.servlet.http.HttpServletRequest) pageContext.getRequest(),
                out);
        } catch (IOException ioException) {
            throw new JspException(
            "Error in TreeTag: IOException whilst printing select: "
            + ioException.getMessage(),
            ioException);
        } catch (Exception e) {
            throw new JspException(e);
        }
        // this tag has no body
        return SKIP_BODY;
    }

    /**
     * <p>This is the method which performs the clever stuff and actually
     * creates the tree by recursing on itself, for a table mode
     * tree.</p>
     *
     * @param parent the node for which to display all the children.
     * @param level the depth of this node within the tree, with 0 being
     * root.
     * @return the parsed HTML tree as
     * @throws ThemeUnsupportedException when the theme sections required
     * by the
     * renderer were not set.
     * @throws JspException if there is any RemoteException or
     * IOException
     * .
     */
    private String createChildren(final Object parent,
            final int level) throws ThemeUnsupportedException,JspException {
        String returnString = "";
        int totalNodes = model.getChildCount(parent);

        for (int nodeNumber = 0; nodeNumber < totalNodes; ++nodeNumber) {
            // make a fresh copy of the properties for each node
            Properties nodeProperties = CollectionHandling.splice(getProperties(), new Properties());
            TreeNode node = (TreeNode) model.getChild(parent, nodeNumber);
            // find out if this folder is open or closed
            Boolean isOpenObject = Boolean.TRUE;
            boolean isOpen;

            // if the folder state is still not set, default
            if (isOpenObject == null) {
                isOpen = defaultOpen;
            } else {
                // use the last setting from the user
                isOpen = isOpenObject.booleanValue();
            }
            // if this node has children and is open and is not leaf, then we have to parse the children
            // first so we can set the property for the children
            if ((model.getChildCount(node) > 0) && isOpen && !model.isLeaf(node)) {
                nodeProperties.setProperty(childrenProperty, createChildren(node,
                        level + 1));
            }
            // now we just use the renderer and return whatever that gives us
            boolean isLeaf = model.isLeaf(node);
            try {
                returnString += renderer.render(model, node, isOpen,
                            isLeaf,
                            level,
                            (nodeNumber == (totalNodes - 1)),
                            getTheme(), nodeProperties);
            } catch(JspException e) {
                // catch it to throw it again :-)
                throw e;
            } catch(Exception e) {
                throw new JspException(e);
            }
        }
        return returnString;
    }

    /**
     * <p>Get the value supplied to the attribute 'model'.</p>
     *
     * <p>This model contains the data source for the tree. To use any
     * datasource
     * with this tree control, you should first create a class which
     * implements
     * {@link javax.swing.tree,TreeModel TreeModel}.</p>
     *
     * @return the value supplied to the tag attribute 'model'.
     *
     */
    public final TreeModel getModel() {
        return model;
    }

    /**
     * <p>Set the value supplied to the attribute 'model'.</p>
     *
     * <p>This model contains the data source for the tree. To use any
     * datasource
     * with this tree control, you should first create a class which
     * implements
     * {@link javax.swing.tree,TreeModel TreeModel}.</p>
     *
     * @param model the new value supplied to the tag attribute 'model'.
     *
     */
    public final void setModel(final TreeModel model) {
        this.model = model;
    }

    /**
     * <p>Get the value supplied to the attribute 'renderer'.</p>
     *
     * <p>This object controls the appearance of each node in the tree,
     * usually
     * by
     * parsing sections from the {@link
     * com.ivata.groupware.web.theme.Theme
     * Theme}.<br/>
     * If you do not use this attribute, an instance of {@link
     * com.ivata.groupware.web.DefaultTreeNodeRenderer
     * DefaultTreeNodeRenderer} is created and applied.</p>
     *
     * @return the value supplied to the tag attribute 'renderer'.
     *
     */
    public final TreeNodeRenderer getRenderer() {
        return renderer;
    }

    /**
     * <p>Set the value supplied to the attribute 'renderer'.</p>
     *
     * <p>This object controls the appearance of each node in the tree,
     * usually
     * by
     * parsing sections from the {@link
     * com.ivata.groupware.web.theme.Theme
     * Theme}.<br/>
     * If you do not use this attribute, an instance of {@link
     * com.ivata.groupware.web.DefaultTreeNodeRenderer
     * DefaultTreeNodeRenderer} is created and applied.</p>
     *
     * @param renderer the new value supplied to the tag attribute
     * 'renderer'.
     *
     */
    public final void setRenderer(final TreeNodeRenderer renderer) {
        this.renderer = renderer;
    }

    /**
     * <p>Get the value supplied to the attribute 'treeName'.</p>
     *
     * <p>This attribute specifies a unique identifier for this tree,
     * which is
     * used to store the state of each foler (open/closed).</p>
     *
     * @return the value supplied to the tag attribute 'treeName'.
     *
     */
    public final String getTreeName() {
        return treeName;
    }

    /**
     * <p>Set the value supplied to the attribute 'treeName'.</p>
     *
     * <p>This attribute specifies a unique identifier for this tree,
     * which is
     * used to store the state of each foler (open/closed).</p>
     *
     * @param treeName the new value supplied to the tag attribute
     * 'treeName'.
     *
     */
    public final void setTreeName(final String treeName) {
        this.treeName = treeName;
    }

    /**
     * <p>Specifies the id of a folder you wish to open.</p>
     *
     * @return the current value of the folder which will be opened, or
     * <code>null</code> if no folder will be opened.
     */
    public final Integer getOpenFolder() {
        return openFolder;
    }

    /**
     * <p>Specifies the id of a folder you wish to open.</p>
     *
     * @param openFolder the new value of the folder you wish to open. Not
     * setting
     * or setting to <code>null</code> results in no folder being opened.
     */
    public final void setOpenFolder(final Integer openFolder) {
        this.openFolder = openFolder;
    }

    /**
     * <p>Specifies the id of a folder you wish to close.</p>
     *
     * @return the current value of the folder which will be closed, or
     * <code>null</code> if no folder will be closed.
     */
    public final Integer getCloseFolder() {
        return closeFolder;
    }

    /**
     * <p>Specifies the id of a folder you wish to close.</p>
     *
     * @param openFolder the new value of the folder you wish to open. Not
     * setting
     * or setting to <code>null</code> results in no folder being opened.
     */
    public final void setCloseFolder(final Integer closeFolder) {
        this.closeFolder = closeFolder;
    }

    /**
     * <p>Decides whether folders should be open or closed by default.</p>
     *
     * @return <code>true</code> if folders should be opened by default,
     * otherwise
     * <code>false>.
     */
    public final boolean getDefaultOpen() {
        return defaultOpen;
    }

    /**
     * <p>Decides whether folders should be open or closed by default.</p>
     *
     * @param defaultOpen set to <code>true</code> if folders should be
     * opened by
     * default, otherwise <code>false>.
     */
    public final void setDefaultOpen(final boolean defaultOpen) {
        this.defaultOpen = defaultOpen;
    }

    /**
     * <p>returning name of form which will submit when you open or close
     * folder</p>
     *
     * @return <code>String</code> name of form
     */
    public final String getFormName() {
        return this.formName;
    }

    /**
     * <p>set up the name of form form submit when you open or close
     * folder</p>
     *
     * @param <code>String formName</ocde> name of form
     */
    public final void setFormName(final String formName) {
        this.formName = formName;
    }
}
