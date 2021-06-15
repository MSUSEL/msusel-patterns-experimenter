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
package com.ivata.groupware.web.container;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.SecuritySession;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Apr 18, 2004
 * @version $Revision: 1.2 $
 */
public final class JSPPicoContainerFactory {
    /**
     * Get the current security session pico container from the JSP page context.
     *
     * @return current session container.
     */
    public static PicoContainer getContainerFromPageContext(PageContext pageContext) {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        return getContainerFromRequest(request);
    }
    /**
     * Get the current security session pico container from the JSP request.
     *
     * @return current session container.
     */
    public static PicoContainer getContainerFromRequest(HttpServletRequest request) {
        HttpSession session = request.getSession();
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
        return securitySession.getContainer();
    }
    /**
     * Convenience method. Get the PicoContainer from the page context and then
     * use it to get the implementation.
     *
     * @return valid instance from the current pico container.
     */
    public static Object getInstanceFromPageContext(PageContext pageContext, Object componentKey) {
        PicoContainer container = getContainerFromPageContext(pageContext);
        return container.getComponentInstance(componentKey);
    }
}
