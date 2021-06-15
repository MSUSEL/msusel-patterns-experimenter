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
<%@ page import="java.io.BufferedOutputStream" %>
<%@ page import="java.io.FileOutputStream" %>

<%
    String title = "Frontier report";
    int tab = 4;
    String reportName = request.getParameter("name");
    String dumpFile = request.getParameter("dumpFile");
%>

<%@include file="/include/head.jsp"%>
        <pre>
<%
            if(handler.getCurrentJob() != null) {
                java.io.PrintWriter writer;
                if (dumpFile!=null) {
                    writer = new java.io.PrintWriter(new BufferedOutputStream(
                        new FileOutputStream(dumpFile)));
                    %> Report dumping to file '<%=dumpFile%>'... <%
                } else {
            	    writer = new java.io.PrintWriter(out);
            	}
            	handler.getCurrentJob().writeFrontierReport(reportName,writer);
            	writer.flush();
            	if (dumpFile!=null) {
            		writer.close();
            	    %> ...done. <%
            	}
            } else {
%> No current job <%
            }
%>      </pre>

<%@include file="/include/foot.jsp"%>
