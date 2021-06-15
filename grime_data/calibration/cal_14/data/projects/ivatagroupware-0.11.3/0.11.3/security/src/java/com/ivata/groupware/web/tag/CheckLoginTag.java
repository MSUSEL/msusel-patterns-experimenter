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
package com.ivata.groupware.web.tag;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.taglib.TagUtils;
import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.mask.web.field.FieldWriterFactory;


/**
 *
 * <p>Check that the user is logged into the system and that the
 * session has not timed. If the session <em>has</em> timed out, then
 * this tag forwards to the login page, returning to the page where
 * this tag was upon successful login</p>
 *
 * <p>This tag should appear as the first tag in each page where we
 * want to confirm that the user is logged in. In particular, this tag
 * should appear before any object is read from the session
 * (user/settings).</p>
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
 *     <td>forward</td>
 *     <td>true</td>
 *     <td><code>String</code></td>
 *     <td>Stores the action forward we should pass control when the
 * user logs back in after a timeout.</td>
 *   </tr>
 *   <tr class='TableRowColor'>
 *     <td>forwardFormName</td>
 *     <td>true</td>
 *     <td><code>String</code></td>
 *     <td>Specifies a form name to store while the user logs back in.
 * The form is looked for in request and session scopes, in that
 * order.</td>
 *   </tr>
 * </table>
 * </p>
 *
 * @since 2002-09-25
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class CheckLoginTag extends TagSupport {
    /**
     * Refer to {@link Logger}.
     */
    private static Logger log = Logger.getLogger(CheckLoginTag.class);
    /**
     * <p>Stores the action forward we should pass control when the user
     * logs back in after a timeout.</p>
     */
    private String forward = null;
    /**
     * <p>Value to be returned by <code>doEndTag()</code>.</p>
     */
    int endTagReturn = EVAL_PAGE;

    /**
     * <p>Checks the user is logged into the system and forwards to the
     * login page if the user is not..</p>
     *
     * <p>This method is called when the JSP engine encounters the start
     * tag, after the attributes are processed.<p>
     *
     * <p>Scripting variables (if any) have their values set here.</p>
     *
     * @return <code>SKIP_BODY</code> since there is no tag body to
     * evaluate.
     * @exception JspException if there is problem forwarding to the login
     * page.
     */
    public int doStartTag() throws JspException {
        // get the http session & request first of all
        HttpSession session = pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

        // see if we have a user name and a settings object in the session
        // if not, we will have to forward to the login page
        SecuritySession securitySession = (SecuritySession)
            session.getAttribute("securitySession");
        FieldWriterFactory fieldWriterFactory = (FieldWriterFactory)
            TagUtils.getInstance().lookup(pageContext,
                FieldWriterFactory.APPLICATION_ATTRIBUTE,
                "application");
        if ((securitySession == null)
                || (fieldWriterFactory == null)) {
            RequestDispatcher dispatcher = pageContext.getServletContext()
                .getRequestDispatcher("/loginGuest.action");

            try {
                dispatcher.forward(request, pageContext.getResponse());
                return SKIP_BODY;
            } catch (Exception e) {
                log.error("CheckLoginTag: could not forward to /loginGuest.action: "
                    + e.getMessage(),
                    e);
            }
        }
        PicoContainer container = securitySession.getContainer();
        Settings settings = (Settings) container.getComponentInstance(Settings.class);
        // indicates that the body should not be evaluated - this tag has no body
        return SKIP_BODY;
    }

    /**
     * <p>Get the action forward we should pass control when the user logs
     * back in after a timeout.</p>
     *
     * @return the current value of forward to pass control to after a
     * login.
     */
    public final String getForward() {
        return forward;
    }

    /**
     * <p>Set the action forward we should pass control when the user logs
     * back in after a timeout.</p>
     *
     * @param forward new value of forward to pass control to after a
     * login.
     */
    public final void setForward(final String forward) {
        this.forward = forward;
    }

    /**
     * <p>We decide here if we should display the rest of the page or not.
     * If  the session times out, we display the contents of the login
     * page
     * instead. This method is called after the JSP engine finished
     * processing
     * the tag.</p>
     *
     * @exception JspException encapsulates any exception when calling
     * <code>out.println</code>
     * @return <code>EVAL_PAGE</code> if we want to evaluate
     * the page after this tag, otherwise <code>SKIP_PAGE</code>.
     */
    public int doEndTag() throws JspException {
        // return value set in doStartTag
        return endTagReturn;
    }
}
