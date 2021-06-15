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
package com.ivata.groupware.web.tree;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import com.ivata.groupware.web.tag.webgui.tree.TreeTag;
import com.ivata.mask.web.theme.Theme;


/**
 * <p>This interface defines the methods of an HTML tree renderer as
 * used in
 * the ivata groupware {@link com.ivata.groupware.web.tag.webgui.TreeTag
 * tree
 * tag}.</p>
 *
 * @since   2002-05-16
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */
public abstract class TreeNodeRenderer {
    /**
     * <p>This tree tag reference is used to open and close folders.</p>
     */
    private TreeTag treeTag;

    /**
     * <p>Get a string which will be used in the tree representation of
     * the tree node. This can contain caption information and image
     * paths, for
     * example, as properties which are evaluated by {@link
     * com.ivata.groupware.web.theme.Theme#parseSection(String
     * name,
     * java.util.Properties properties)
     * Theme.parseSection(String name, java.util.Properties
     * properties)}.</p>
     * @param model <code>TreeModel</code> representing the data of the
     * tree.
     * @param node <code>Object</code> representing a node in the tree.
     * The
     * <code>toString(  )</code> of this node will be used as the
     * 'caption'
     * properties when parsing.
     * @param expanded <code>true</code> if this node is 'open', otherwise
     * <code>false</code>.
     * @param leaf <code>true</code> if this node is a leaf node,
     * otherwise
     * <code>false</code>.
     * A leaf node is one which can have no children, like a file in a
     * filesystem
     * tree.
     * @param level the depth of this node within the tree, with 0 being
     * root.
     * @param last <code>true</code> if this node is the last in the
     * current branch,
     * otherwise <code>false</code>.
     * @param theme this theme does the parsing.
     * @param properties existing properties to parse.
     * @return the parsed string.
     * @throws JspException if the theme sections required by the
     * renderer have not been defined.
     */
    public abstract String render(javax.swing.tree.TreeModel model, Object node, boolean expanded, boolean leaf, int level, boolean last, Theme theme, java.util.Properties properties) throws JspException;

    /**
     * <p>This method initializes the internal reference to the tree
     * tag.</p>
     *
     * @param treeTag internal reference to the treetag which owns this
     * renderer.
     */
    public void setTreeTag(TreeTag treeTag) {
        this.treeTag = treeTag;
    }

    /**
     * <p>This method is called by the tree tag during
     * <code>doStartTag</code> to
     * allow the renderer to open or close folders as apropriate.</p>
     *
     * @param session the current session which can be used to retrieve
     * settings.
     * @param request the current servlet request which can be used to
     * retrieve
     * settings.
     * @param out jsp writer which can be used to output HTML.
     * @param pageContext the current PageContext
     * @throws JspException not thrown by this class but can be thrown by
     * subclasses
     * who experience an error on initialization.
     */
    public abstract void initialize(HttpSession session, HttpServletRequest request, JspWriter out, PageContext pageContext) throws JspException;

    /**
     * <p>Is called by the tree tag after the tree has been drawn. Can be
     * overridden
     * by subclasses to provide finalization.</p>
     *
     * <p>This default implementation does nothing.</p>
     *
     * @param session the current session which can be used to retrieve
     * settings.
     * @param request the current servlet request which can be used to
     * retrieve
     * settings.
     * @param out jsp writer which can be used to output HTML.
     * @throws JspException not thrown by this class but can be thrown by
     * subclasses
     * who experience an error on finalization.
     */
    public void finalize(HttpSession session, HttpServletRequest request, JspWriter out) throws JspException {
    }

    /**
     * <p>Get the internal reference to the tree tag.</p>
     *
     * @return internal reference to the treetag which owns this renderer.
     */
    public TreeTag getTreeTag() {
        return treeTag;
    }
}
