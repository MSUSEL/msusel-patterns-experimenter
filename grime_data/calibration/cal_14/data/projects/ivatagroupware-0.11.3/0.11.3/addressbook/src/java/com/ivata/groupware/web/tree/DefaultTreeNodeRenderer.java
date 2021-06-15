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
// Source file: h:/cvslocal/ivata groupware/src/com.ivata.groupware/jsp/tree/DefaultTreeNodeRenderer.java
package com.ivata.groupware.web.tree;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.struts.taglib.TagUtils;

import com.ivata.groupware.web.tag.webgui.tree.TreeTag;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.web.theme.Theme;
import com.ivata.mask.web.theme.ThemeParseException;


/**
 * <p>Create a default tree node renderer to draw your tree. This
 * renderer
 * calls {@link com.ivata.groupware.web.theme.Theme#parseSection
 * Theme.parseSection}
 * to parse the standard tree theme sections 'treeOpen', 'treeClosed',
 * 'treeNoChildren' and 'treeLeaf'.</p>
 *
 * <p>The object you provide must implement {@link
 * com.ivata.groupware.web.DefaultTreeNode
 * TreeNode}.</p>
 *
 * <p>In each of these sections, the property 'caption' is parsed out
 * to the
 * {@link com.ivata.groupware.web.DefaultTreeNode#getName(  ) getName}
 * value
 * of the node
 * provided, and the property 'id' is parsed out to the value of
 * {@link com.ivata.groupware.web.DefaultTreeNode#getName(  )
 * getId}.</p>
 *
 * @since   2002-05-16
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 * @see     TreeNodeRenderer
 * @see     com.ivata.groupware.web.theme.Theme#parseSection
 */
public class DefaultTreeNodeRenderer extends TreeNodeRenderer {

    /**
     * <p>The URL of the current page, used to create open folder/ close
     * folder
     * links.</p>
     */
    private String URL;

    /**
     * <p>valueholder foor the current <code>PageContext</code></p>
     */
    private PageContext pageContext;

    /**
     * <p>Render a standard tree using the theme sections 'treeOpen',
     * 'treeClosed',
     * 'treeNoChildren' and 'treeLeaf'.</p>
     * <p>The following properties will be defined:<br/>
     * <table cellpadding='2' cellspacing='5' border='0' align='center'
     * width='85%'>
     *   <tr class='TableHeadingColor'>
     *     <th>property</th>
     *     <th>description</th>
     *   </tr>
     *   <tr class='TableRowColor'>
     *     <td>caption</td>
     *     <td>The value returned by <code>node.toString(  )</code>.</td>
     *   </tr>
     *   <tr class='TableRowColor'>
     *     <td>id</td>
     *     <td>The id of the node.</td>
     *   </tr>
     *   <tr class='TableRowColor'>
     *     <td>last</td>
     *     <td>'Last' if this is the last node in the current branch,
     * otherwise
     * this property is not set.</td>
     *   </tr>
     * </table>
     * </p>
     * <p> These properties are evaluated by calling {@link
     * com.ivata.groupware.web.theme.Theme#parseSection(String
     * sName,
     * java.util.Properties properties)
     * Theme.parseSection(String sName, java.util.Properties
     * properties)}
     * for each of the relevant theme sections.</p>
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
     * @throws JspException if the theme sections 'treeOpen',
     * 'treeClosed'
     * 'treeNoChildren' or 'treeLeaf' have not been defined.
     *
     *
     */
    public String render(javax.swing.tree.TreeModel model, Object node, boolean expanded, boolean leaf, int level, boolean last, Theme theme, java.util.Properties properties) throws JspException {
        try {
            TreeNode treeNode = (TreeNode) node;
            HashMap URLProperties = new HashMap();

            // set the caption property
            properties.setProperty("caption", treeNode.getName());
            // set the id property
            if (treeNode.getId() != null) {
                properties.setProperty("id", treeNode.getId().toString());
            }
            // is this the last node? if so, set the last property
            if (last) {
                properties.setProperty("last", "Last");
            }
            // is this a leaf = no children?
            if (leaf) {
                return theme.parseSection("treeLeaf",
                        setAdditionalProperties(treeNode, level, properties));
            }
            // see if this node has no children
            if (model.getChildCount(node) == 0) {
                return theme.parseSection("treeNoChildren",
                        setAdditionalProperties(treeNode, level, properties));
            }
            // options: it is either open or closed and if we will submit the form or we will make a link
            TreeTag treeTag = getTreeTag();
            if (expanded) {
                if (treeTag.getFormName() != null) {
                    properties.setProperty("folderURL", "javascript:" + treeTag.getFormName() +
                        ".closeFolder.value=" + treeNode.getId() + ";" +
                        treeTag.getFormName() + ".submit();");
                    return theme.parseSection("treeOpen",
                            setAdditionalProperties(treeNode, level, properties));
                } else {
                    URLProperties.put("closeFolder", treeNode.getId().toString());
                    try {
                        properties.setProperty("folderURL",
                            TagUtils.getInstance().computeURL(pageContext, null, URL,
                                null, null, null, URLProperties, null, true));
                    } catch (java.net.MalformedURLException e) {
                        throw new JspException(e);
                    }
                    return theme.parseSection("treeOpen",
                            setAdditionalProperties(treeNode, level, properties));
                }
            } else {
                if (treeTag.getFormName() != null) {
                    properties.setProperty("folderURL", "javascript:" + treeTag.getFormName() +
                        ".openFolder.value=" + treeNode.getId() + ";" +
                        treeTag.getFormName() + ".submit();");
                    return theme.parseSection("treeClosed",
                            setAdditionalProperties(treeNode, level, properties));
                } else {
                    URLProperties.put("openFolder", treeNode.getId().toString());
                    try {
                        properties.setProperty("folderURL",
                                TagUtils.getInstance().computeURL(pageContext, null,
                                        URL, null,  null, null, URLProperties,
                                        null, true));
                    } catch (java.net.MalformedURLException e) {
                        throw new JspException(e);
                    }
                    return theme.parseSection("treeClosed",
                            setAdditionalProperties(treeNode, level, properties));
                }
            }
        } catch (ThemeParseException e) {
            throw new RuntimeException(e);
        }
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
     * @param pageContext the current <code>PageContext</code>
     * @throws JspException not thrown by this class but can be thrown by
     * subclasses
     * who experience an error on initialization.
     */
    public void initialize(HttpSession session, HttpServletRequest request, JspWriter out, PageContext pageContext) throws JspException {
        String openFolder = request.getParameter("openFolder");
        TreeTag treeTag = getTreeTag();

        if (!StringHandling.isNullOrEmpty(openFolder)) {
            treeTag.setOpenFolder(StringHandling.integerValue(openFolder));
        }
        String closeFolder = request.getParameter("closeFolder");

        if (!StringHandling.isNullOrEmpty(closeFolder)) {
            treeTag.setCloseFolder(StringHandling.integerValue(closeFolder));
        }
        URL = request.getRequestURL().toString();
        this.pageContext = pageContext;
    }

    /**
     * <p>Can be overridden by super-class to provide addtional property
     * information
     * for a node.</p>
     *
     * @param treeNode the current node in the tree being drawn.
     * @param level the depth of this node within the tree, with 0 being
     * root.
     * @param properties all the properties are already defined. New
     * properties
     * should be added to this instance and returned.
     * @return all of the properties which should be evaluated in the
     * client theme
     * section.
     * @throws JspException thrown by subclasses if there is a formatting
     * error.
     */
    public java.util.Properties setAdditionalProperties(TreeNode treeNode, int level, java.util.Properties properties) throws JspException {
        // this default implementation does nothing...
        return properties;
    }

    /**
     * <p>valueholder foor the current <code>PageContext</code></p>
     *
     * @return the current value of pageContext.
     */
    public PageContext getPageContext() {
        return pageContext;
    }

    /**
     * <p>valueholder foor the current <code>PageContext</code></p>
     *
     * @param pageContext the new value of pageContext.
     */
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }
}
