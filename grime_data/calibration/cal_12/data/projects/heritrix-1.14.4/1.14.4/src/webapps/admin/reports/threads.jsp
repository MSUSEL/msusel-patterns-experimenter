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

<%
    String title = "Threads report";
    int tab = 4;
    String reportName = request.getParameter("name");
    
    String action = request.getParameter("action");
    String message = null;
    if(action != null && action.equals("killkillkill")){
        // Kill thread.
        try {
            if (handler.getCurrentJob() != null) {
                String threadNumber =request.getParameter("threadNumber");
                handler.getCurrentJob().
                    killThread(Integer.parseInt(threadNumber),
                        (request.getParameter("replace")!=null && 
                            request.getParameter("replace").equals("replace")));
                message = "Kill message sent to thread #" +
                    request.getParameter("threadNumber");
            }
        } catch(NumberFormatException e){
            message = "Kill operation failed";
        }
    }
%>

<%@include file="/include/head.jsp"%>
    <script type="text/javascript">
        function doKill(){
            thread = document.frmThread.threadNumber.value;
            if(confirm("Are you sure you wish to kill thread number " +
                            thread + "?\nThe action is irreversible " +
                            " and can potentially destablize the crawler."))
            {
                document.frmThread.action.value = "killkillkill";
                document.frmThread.submit();
            }
        }
    </script>
    <pre><%
    if (handler.getCurrentJob() != null) {
    	java.io.PrintWriter writer = new java.io.PrintWriter(out);
    	handler.getCurrentJob().writeThreadsReport(reportName,writer);
    	writer.flush();
    } else {
%> No current job <%
    }
    %></pre>
    <hr>
    <form name="frmThread" method="post" action="threads.jsp">
        <input type="hidden" name="action">
        <b>Thread number:</b> <input name="threadNumber" size="3"> <input type="checkbox" name="replace" value="replace"> Replace thread <input type="button" onClick="doKill()" value="Kill thread">
    </form>

<%@include file="/include/foot.jsp"%>
