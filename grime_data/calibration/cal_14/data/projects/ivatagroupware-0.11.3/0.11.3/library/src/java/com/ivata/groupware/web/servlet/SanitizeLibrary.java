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
package com.ivata.groupware.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.library.Library;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.util.SystemException;

/**
 * <p>
 * TODO
 * </p>
 *
 * @since Aug 9, 2004
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */

public class SanitizeLibrary extends HttpServlet {
    /**
     * <p>
     * <strong>Log4J</strong> logger.
     * </p>
     */
    private static Category log = Category.getInstance(SanitizeLibrary.class);

    /**
     * <p>Clean up resources.</p>
     */
    public void destroy() {}

    /**
     * <p>TODO: add a comment here.</p>
     */
    public void doGet(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        log.info("START Sanitize library.");

        PicoContainer container;
        try {
            container = PicoContainerFactory.getInstance()
                .getGlobalContainer();
        } catch (SystemException e) {
            throw new ServletException(e);
        }
        Security security = (Security) container.getComponentInstance(Security.class);
        try {
            // get a session container - we need a session for the date formatter
            SecuritySession session = security.loginGuest();
            container = session.getContainer();
            Library library = (Library) container.getComponentInstance(Library.class);
            library.sanitize(session);
        } catch (SystemException e) {
            e.printStackTrace();
            log.error(e);
        }

        log.info("END Sanitize library.");
    }

    /**
     * <p>Initialize global variables.</p>
     */
    public void init() throws ServletException {}

}
