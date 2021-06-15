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
package com.ivata.groupware.web.tree.person;

import java.util.HashMap;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.struts.taglib.TagUtils;

import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.business.addressbook.person.group.tree.PersonTreeNode;
import com.ivata.groupware.web.tree.DefaultTreeNodeRenderer;
import com.ivata.groupware.web.tree.TreeNode;
import com.ivata.mask.util.StringHandling;


/**
 * <p>Overrides methods <code>setAdditionalProperties</code> and
 * <code>initialize</code>. We need to know which check box is checked
 * and which is not.</p>
 *
 * @since   2002-05-27
 * @author  jano
 * @version $Revision: 1.2 $
 * @see     DefaultTreeNodeRenderer
 */
public class PersonTreeNodeRenderer extends DefaultTreeNodeRenderer {
    private java.util.Vector checked = null;
    PageContext pageContext = null;

    /**
     * <p>Overridden method to set up <code>check</code> property of the
     * check box in the leaf node.</p>
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
    public java.util.Properties setAdditionalProperties(final TreeNode treeNode,
            final int level,
            final java.util.Properties properties) throws JspException {
        PersonDO person = (PersonDO) ((PersonTreeNode) treeNode).getPerson();

        if (person != null) {
            // see if person Id is in checked vector
            if ((checked !=null) && (person != null) &&
                (checked.indexOf(person.getId()) != -1)) {
                properties.setProperty("checked", "checked");
            }
            properties.setProperty("value", person.getId().toString());
            properties.setProperty("email",
                StringHandling.getNotNull(person.getEmailAddress(),
                                        "[none]"));
            properties.setProperty("fileAs",
                StringHandling.getNotNull(person.getFileAs()));
        } else {
            try {
                HashMap map = new HashMap();
                map.put("page",
                        "/mask/find.action?deleteKey=group.alert.delete"
                    + "&menuFrameURI=%2FaddressBook%2FgroupTree.action"
                    + "%3FgroupTreeRefresh%3Dtrue%26mode%3Dgroup&"
                    + "idString=" + treeNode.getId().toString()
                    + "&inputMask=imGroupInputMaskAction"
                    + "&baseClass=com.ivata.groupware.business.addressbook"
                    + ".person.group.GroupDO&bundle=addressBook"
                    + "&resourceFieldPath=group&menuFrameName=ivataGroupList");
                String URL = TagUtils.getInstance().computeURL(pageContext, "utilLoading",
                        null, null, null, null, map, null, true);
                properties.setProperty("groupFindAction", URL);
            } catch (java.net.MalformedURLException e) {
                throw new JspException(e);
            }
        }

        return properties;
    }

    /**
     * <p>This method is called by the tree tag during
     * <code>doStartTag</code> to allow the renderer to open or close
     * folders as appropriate.</p>
     *
     * <p>It gets <code>Vector checkedAttendee</code> from the
     * session.</p>
     *
     * @param session the current session which can be used to retrieve
     * settings.
     * @param request the current servlet request which can be used to
     * retrieve settings.
     * @param out jsp writer which can be used to output HTML.
     * @param pageContext the current <code>PageContext</code>
     * @throws JspException not thrown by this class but can be thrown by
     * subclasses
     * who experience an error on initialization.
     */
    public void initialize(final HttpSession session,
            final HttpServletRequest request,
            final JspWriter out,
            final PageContext pageContext) throws JspException {
        this.pageContext = pageContext;
        try {
            out.print(getTreeTag().getTheme().parseSection("initializePersonTree", new Properties()));
        } catch (java.io.IOException e) {
            throw new JspException(e);
        }

        super.initialize(session,request,out, pageContext);
        this.checked = (java.util.Vector) session.getAttribute("checkedAttendees");

    }

    /**
     * @return the current value of checked.

     */
    public final java.util.Vector getChecked() {
        return checked;
    }

    /**
     * @param checked the new value of checked.

     */
    public final void setChecked(final java.util.Vector checked) {
        this.checked=checked;
    }
}
