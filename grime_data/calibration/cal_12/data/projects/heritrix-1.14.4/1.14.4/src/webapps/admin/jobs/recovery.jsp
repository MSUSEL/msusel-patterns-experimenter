<%--

    The MIT License (MIT)

    MSUSEL Arc Framework
    Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
    Software Engineering Laboratory and Idaho State University, Informatics and
    Computer Science, Empirical Software Engineering Laboratory

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

--%>
<%@include file="/include/handler.jsp"%>

<%@ page import="org.archive.crawler.admin.CrawlJob" %>
<%@ page import="org.archive.crawler.admin.CrawlJobHandler" %>
<%@ page import="org.archive.crawler.datamodel.Checkpoint" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.Collection" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>

<%!
    /**
     * Display list of recovery options.
     * Display checkpoint directories and recovery logs.  Don't display
     * anything if neither option is available.
     */
    public String printJobList(List jobs, HttpServletRequest request)
    throws Exception {
        if(jobs == null) {
            return null;
        }
        StringBuffer ret = new StringBuffer();
        for (int i = jobs.size() - 1 ; i >= 0 ; i--) {
            CrawlJob tmp = (CrawlJob)jobs.get(i);
            Collection cps = tmp.scanCheckpoints();
            // Are there valid cps -- that have bdbje logs in them?
            boolean foundValidCp = false;
            for (final Iterator it = cps.iterator(); it.hasNext(); ) {
                Checkpoint cp = (Checkpoint)it.next();
                if (cp.isValid() && cp.hasBdbjeLogs()) {
                    foundValidCp = true;
                    break;
                }
            }
            File recoverlog = null;
            try {
                recoverlog = new File(tmp.getLogPath("recover.gz"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            boolean isRecover = recoverlog.exists();
            if (!isRecover) {
                // Check for non-gzipped recover.
                try {
                    recoverlog = new File(tmp.getLogPath("recover"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                isRecover = recoverlog.exists();
            }
            if (!isRecover && !foundValidCp) {
                // Skip.  No recovery option.
                continue;
            }
            ret.append("<li>");
            ret.append(tmp.getJobName());
            ret.append(" [" + tmp.getUID() + "]");
            ret.append(":");
            if (isRecover) {
                ret.append(" <a href=\"");
                ret.append(request.getContextPath());
                ret.append("/jobs/new.jsp?job=");
                ret.append(tmp.getUID());
                ret.append("&recover=");
                ret.append(CrawlJobHandler.RECOVER_LOG);
                ret.append("\">recovery-log</a>");
            }
            for (final Iterator it = cps.iterator(); it.hasNext(); ) {
                Checkpoint cp = (Checkpoint)it.next();
                if (!cp.isValid() || !cp.hasBdbjeLogs()) {
                    continue;
                }
                ret.append(" <a href=\"");
                ret.append(request.getContextPath());
                ret.append("/jobs/new.jsp?job=");
                ret.append(tmp.getUID());
                ret.append("&recover=");
                ret.append(cp.getName());
                ret.append("\">");
                ret.append(cp.getName());
                ret.append(".");
                ret.append(cp.getTimestamp());
                ret.append("</a>");
            }
            ret.append("</li>");
        }
        return ret.toString();
    }
%>
<%
    String title = "New Job Based on a Recovery";
    int tab = 1;
%>

<%@include file="/include/head.jsp"%>
<p>Below we list all jobs and the recovery options available for each job
listed.  Options will vary with each job: i.e. whether recovery
logging was enabled on a particular crawl and whether or not checkpoints
were invoked during the life of the crawl. A job will not be listed if it does
not have a recovery option.  Recovery log is tried-and-true.
Checkpoint recovers are experimental but are a more comprehensive recovery,
and should run faster than their recovery log counterpart.</p>
    <b>Select a recovery source:</b>
<p>
    <ul>
<%
out.println(printJobList(handler.getCompletedJobs(), request));
%>    
    </ul>

        
<%@include file="/include/foot.jsp"%>
