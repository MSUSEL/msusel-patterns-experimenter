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
<%@ page
  contentType="text/html; charset=UTF-8"
  import="java.io.*"
  import="java.security.PrivilegedExceptionAction"
  import="java.util.*"
  import="javax.servlet.*"
  import="javax.servlet.http.*"
  import="org.apache.hadoop.conf.Configuration"
  import="org.apache.hadoop.hdfs.*"
  import="org.apache.hadoop.hdfs.server.namenode.*"
  import="org.apache.hadoop.hdfs.server.datanode.*"
  import="org.apache.hadoop.hdfs.protocol.*"
  import="org.apache.hadoop.hdfs.security.token.delegation.*"
  import="org.apache.hadoop.io.Text"
  import="org.apache.hadoop.security.UserGroupInformation"
  import="org.apache.hadoop.security.token.Token"
  import="org.apache.hadoop.util.*"
  import="java.text.DateFormat"
  import="java.net.InetAddress"
  import="java.net.URLEncoder"
%>
<%!
  static String getDelegationToken(final NameNode nn,
                                   final UserGroupInformation ugi,
                                   HttpServletRequest request, Configuration conf) 
                                   throws IOException, InterruptedException {
    Token<DelegationTokenIdentifier> token =
      ugi.doAs(
              new PrivilegedExceptionAction<Token<DelegationTokenIdentifier>>()
          {
            public Token<DelegationTokenIdentifier> run() throws IOException {
              return nn.getDelegationToken(new Text(ugi.getUserName()));
            }
          });
    return token.encodeToUrlString();
  }

  public void redirectToRandomDataNode(
                            ServletContext context, 
                            HttpServletRequest request,
                            HttpServletResponse resp
                           ) throws IOException, InterruptedException {
    Configuration conf = (Configuration) context.getAttribute(JspHelper.CURRENT_CONF);
    NameNode nn = (NameNode)context.getAttribute("name.node");
    final UserGroupInformation ugi = JspHelper.getUGI(context, request, conf);
    String tokenString = null;
    if (UserGroupInformation.isSecurityEnabled()) {
      tokenString = getDelegationToken(nn, ugi, request, conf);
    }
    FSNamesystem fsn = nn.getNamesystem();
    String datanode = fsn.randomDataNode();
    String redirectLocation;
    String nodeToRedirect;
    int redirectPort;
    if (datanode != null) {
      redirectPort = Integer.parseInt(datanode.substring(datanode.indexOf(':')
                     + 1));
      nodeToRedirect = datanode.substring(0, datanode.indexOf(':'));
    }
    else {
      nodeToRedirect = nn.getHttpAddress().getHostName();
      redirectPort = nn.getHttpAddress().getPort();
    }
    String fqdn = InetAddress.getByName(nodeToRedirect).getCanonicalHostName();
    redirectLocation = "http://" + fqdn + ":" + redirectPort + 
                       "/browseDirectory.jsp?namenodeInfoPort=" + 
                       nn.getHttpAddress().getPort() +
                       "&dir=/" + 
                       (tokenString == null ? "" :
                        JspHelper.getDelegationTokenUrlParam(tokenString));
    resp.sendRedirect(redirectLocation);
  }
%>

<html>

<title></title>

<body>
<% 
  redirectToRandomDataNode(application, request, response); 
%>
<hr>

<h2>Local logs</h2>
<a href="/logs/">Log</a> directory

<%
out.println(ServletUtil.htmlFooter());
%>
