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
package com.ivata.groupware.web.tree.comment;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.admin.setting.SettingsDataTypeException;
import com.ivata.groupware.business.library.comment.CommentDO;
import com.ivata.groupware.business.library.right.LibraryRights;
import com.ivata.groupware.util.SettingDateFormatter;
import com.ivata.groupware.web.tree.DefaultTreeNodeRenderer;
import com.ivata.groupware.web.tree.TreeNode;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.format.CharacterEntityFormat;
import com.ivata.mask.web.format.DateFormatterException;
import com.ivata.mask.web.format.FormatConstants;
import com.ivata.mask.web.format.HTMLFormatter;
import com.ivata.mask.web.format.LineBreakFormat;


/**
 * <p>Create at tree node renderer for library item comments. This
 * renderer uses
 * the same theme sections as <code>DefaultTreeNodeRenderer</code>
 * though it
 * sets
 * new properties to be evaluated in a separate theme.</p>
 *
 * @since   2002-07-07
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 * @see     DefaultTreeNodeRenderer
 */
public class CommentTreeNodeRenderer extends DefaultTreeNodeRenderer {

    /**
     * <p>Applied to the content of the tree tag</p>
     */
    private CharacterEntityFormat characterEntityFormat = new CharacterEntityFormat();

    /**
     * <p>Used to format the modified by field.</p>
     */
    private SettingDateFormatter dateFormatter;

    /**
     * <p>Used to check whether or not the current user can edit the
     * current
     * node.</p>
     */
    private LibraryRights libraryRights;

    /**
     * <p>Used to convert line breaks in plain text comments.</p>
     */
    private LineBreakFormat lineBreakFormat = new LineBreakFormat();

    /**
     * <p>Used to initialize get the spacer width.</p>
     */
    private Settings settings;
    /**
     * <p>Stores the number of the top level comment (top level meaning a
     * comment
     * which is not a reply to another comment). This is used to set the
     * css
     * class.</p>
     */
    int threadNumber = 0;
    SecuritySession securitySession;

    /**
     * Constructor.
     */
    public CommentTreeNodeRenderer(SecuritySession securitySession,
            SettingDateFormatter dateFormatter, LibraryRights
            libraryRights, Settings settings) {
        this.securitySession = securitySession;
        this.dateFormatter = dateFormatter;
        this.libraryRights = libraryRights;
        this.settings = settings;
    }

    /**
     * <p>Overridden to close the table which surrounds comment trees.</p>
     *
     * @param session the current session which can be used to retrieve
     * settings.
     * @param request the current servlet request which can be used to
     * retrieve
     * settings.
     * @param out jsp writer which can be used to output HTML.
     * @throws JspException if there is an error parsing the theme or
     * writing the
     * output.
     * who experience an error on finalization.
     */
    public void finalize(final HttpSession session,
            final HttpServletRequest request,
            final JspWriter out) throws JspException {
        try {
            out.print(getTreeTag().getTheme().parseSection("finalizeCommentTree",
                    new Properties()));
        } catch (java.io.IOException e) {
            throw new JspException(e);
        }

        super.finalize(session, request, out);
    }

    /**
     * <p>Access the internal date formatter to change the date or time
     * format
     * applied.</p>
     *
     * @return the formatter which is used internally to convert the
     * modified field
     * to a string.
     */
    public final SettingDateFormatter getDateFormatter() {
        return dateFormatter;
    }

    /**
     * <p>This method has been overriden to initialize the settings in the
     * internal
     * date formatter, and to parse the comment surrounding table.</p>
     *
     * @param session the current session which can be used to retrieve
     * settings.
     * @param request the current servlet request which can be used to
     * retrieve
     * settings.
     * @param out jsp writer which can be used to output HTML.
     * @param pageContext the current PageContext
     * @throws JspException if there is an error parsing the theme or
     * writing the
     * output.
     * who experience an error on initialization.
     */
    public void initialize(final HttpSession session,
            final HttpServletRequest request,
            final JspWriter out,
            final PageContext pageContext) throws JspException {
        try {
            out.print(getTreeTag().getTheme().parseSection("initializeCommentTree",
                    new Properties()));
        } catch (java.io.IOException e) {
            throw new JspException(e);
        }

        super.initialize(session, request, out, pageContext);

        // if we apply it, we'll want the line break format to do its job ;-)
        lineBreakFormat.setConvertLineBreaks(true);
    }

    /**
     * <p>Overridden to add the comment text property.</p>
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
     * @throws JspException if there is a format error with the date.
     */
    public Properties setAdditionalProperties(final TreeNode treeNode,
            final int level,
            final Properties properties) throws JspException {
        // make the right formatter for the occasion
        HTMLFormatter formatter = new HTMLFormatter();
        CommentDO commentDO = (CommentDO) treeNode;

        // see if the user is allowed to edit this comment
        try {
            if (libraryRights.canAmendComment(securitySession, commentDO)) {
                properties.setProperty("canUserEdit", "true");
            }
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }

        // if this is a plain text message, convert character entities and line
        // breaks
        if (commentDO.getFormat() == FormatConstants.FORMAT_TEXT) {
            formatter.add(characterEntityFormat);
            formatter.add(lineBreakFormat);
        }

        properties.setProperty("commentSubject",
            formatter.format(commentDO.getSubject()));
        properties.setProperty("commentText",
            formatter.format(commentDO.getText()));
        properties.setProperty("commentUserName", commentDO.getCreatedBy().getName());

        try {
            properties.setProperty("commentModified",
                dateFormatter.format(commentDO.getModified()));
        } catch (DateFormatterException e1) {
            throw new RuntimeException(e1);
        }

        // how large the spacer is depends on the level
        try {
            properties.setProperty("spacer",
                new Integer(
                    level * settings.getIntegerSetting(securitySession,
                            "libraryCommentSpacer",
                            securitySession.getUser()).intValue()).toString());
        } catch (SettingsDataTypeException e) {
            throw new JspException(e);
        } catch (SystemException e) {
            throw new JspException(e);
        }

        // the css style for this row depends on the whether the thread number
        // is odd or even, and whether the reply number within that thread is
        // odd or even too.
        String threadClass;

        if ((threadNumber % 2) == 0) {
            if ((level % 2) == 0) {
                threadClass = "commentEvenThreadEvenReply";
            } else {
                threadClass = "commentEvenThreadOddReply";
            }
        } else if ((level % 2) == 0) {
            threadClass = "commentOddThreadEvenReply";
        } else {
            threadClass = "commentOddThreadOddReply";
        }

        properties.setProperty("threadClass", threadClass);

        // if this is a top level comment, then increase the thread counter
        if (level == 0) {
            ++threadNumber;
        }

        return properties;
    }
}
