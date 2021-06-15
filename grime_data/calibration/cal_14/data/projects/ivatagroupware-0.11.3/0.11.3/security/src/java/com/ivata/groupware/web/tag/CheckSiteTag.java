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

import java.io.File;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <p>Check that the site is okay and is not being worked on.
 *  The presence of a file in the location
 * <code>/var/lock/ivata groupware/site_down{prefix}_down
 * indicates the site should not be displayed.<p>
 *
 * <p>{prefix} = context path of the site</p>
 *
 * <p>If the site should not be displayed, the contents of
 * <code>/maintenance.jsp</code> are shown.</p>
 *
 * @since 2003-02-20
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */
public class CheckSiteTag extends TagSupport {

    /**
     * <p>This method is called after the JSP engine finished processing
     * the tag.</p>
     *
     * @return <code>SKIP_PAGE</code> if the site is being
     * worked on, otherwise <code>EVAL_PAGE</code>.
     * @throws JspException if there is an error accessing the
     * maintenance page for sites which are not active.
     */
    public int doEndTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String jndiPrefix = "";
        // TODO:
        //jndiPrefix = applicationServer.getJndiPrefix();
        File siteDownFile = new File("/var/lock/ivata groupware/site_down/" +
            jndiPrefix + "_down");

        // store the last visited URL in the session - good for the error page
        String requestURL = request.getRequestURL().toString();
        if (requestURL.indexOf("error.jsp") == -1) {
            request.getSession().setAttribute("lastURL", requestURL);
        }

        // if there is no file, evaluate the rest of the page as normal
        if (!siteDownFile.exists()) {
            return EVAL_PAGE;
        }

        try {
            // if it gets here, just show the contents of the maintenance page
            // and get out
            RequestDispatcher dispatcher = pageContext.getServletContext().getRequestDispatcher("/maintenance.jsp?jndiPrefix=" + jndiPrefix);
                dispatcher.forward(request, pageContext.getResponse());
        } catch (IOException e) {
            throw new JspException(e);
        } catch (ServletException e) {
            throw new JspException(e);
        }
        return SKIP_PAGE;
    }
}
