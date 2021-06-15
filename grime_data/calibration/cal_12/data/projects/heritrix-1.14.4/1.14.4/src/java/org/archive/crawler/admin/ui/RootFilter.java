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
package org.archive.crawler.admin.ui;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter that redirects accesses to 'index.jsp'.
 * @author stack
 * @version $Date: 2005-08-29 21:52:36 +0000 (Mon, 29 Aug 2005) $, $Revision: 3771 $
 */
public class RootFilter implements Filter {
    private FilterConfig filterConfig = null;
    
    public void init(FilterConfig config) {
        this.filterConfig = config;
    }
    
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain)
    throws IOException, ServletException {
        if (this.filterConfig == null) {
            return;
        }
        if (req instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest)req;
            String path = httpRequest.getRequestURI();
            if (path == null || path.equals(httpRequest.getContextPath()) ||
                    (path.equals(httpRequest.getContextPath() + "/"))) {
                String tgt = this.filterConfig.
                    getInitParameter("rootFilter.redirectTo");
                ((HttpServletResponse)res).sendRedirect((tgt == null)?
                    httpRequest.getContextPath() + "/index.jsp":
                    httpRequest.getContextPath() + tgt);
                return;
            }
        }
        chain.doFilter(req, res);
    }

    public void destroy() {
        this.filterConfig = null;
    }
}
