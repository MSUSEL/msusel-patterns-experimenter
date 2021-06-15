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
    boolean shutdown = request.getParameter("shutdown")!=null
                       && request.getParameter("shutdown").equals("doit");
                       
    String title = "Shut down program";
    int tab = 0;
%>

<%@include file="/include/head.jsp"%>

    <% if(shutdown){ %>
        <p> 
            <b>Heritrix software has been shut down!</b>
        <p>
            This web access is no longer functioning. The software must 
            be relaunched via command line for it to be accessible again.
        <p>
            <i>Thank you for using Heritrix</i>
    <% } else { %>        
        <script type="text/javascript">
            function doShutDown(){
                if (confirm("Shut Heritrix software down?")){
                    document.frmShutDown.shutdown.value = "doit";
                    document.frmShutDown.submit();
                }
            }
        </script>
        
        <form name="frmShutDown" method="post" action="shutdown.jsp">
            <input type="hidden" name="shutdown">
        </form>
    
        <p>
            <b>Are you sure you wan't to shut Heritrix down?</b>
        <p>
            <span class="warning"><b>Warning:</b> Doing so will end any current job 
            and terminate this web access.<br> The program can only be restarted via
            command line launching</span>
        <p>
            <input type="button" value="I'm sure, shut it down" onClick="doShutDown()">
            <input type="button" value="Cancel" onClick="document.location='<%=request.getContextPath()%>/index.jsp'">

    <% } %>
<%@include file="/include/foot.jsp"%>

<% 
    if(shutdown){
        Thread temp = new Thread(){
            public void run(){
                try {
                    synchronized(this){
                        wait(200); // Wait a moment so we can finish displaying 'good bye' page
                    }
                } catch( InterruptedException e ) {
                    // We can ignore it.
                }
                Heritrix.prepareHeritrixShutDown();
                try {
                    synchronized(this){
                        wait(1000); // Wait for those threads that can terminate quickly.
                    }
                } catch( InterruptedException e ) {
                    // We can ignore it.
                }
                Heritrix.performHeritrixShutDown();
            }
        };
        temp.start();
    } 
%>
