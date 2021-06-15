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
<%@ page contentType='text/html;charset=UTF-8'%>
<%@ page isErrorPage="true"%>

<%@ page import='com.ivata.mask.util.StringHandling'%>
<%@ page import='com.ivata.mask.util.ThrowableHandling'%>
<%@ page import='com.ivata.mask.validation.ValidationErrors' %>
<%@ page import='com.ivata.mask.validation.ValidationException' %>
<%@ page import='com.ivata.mask.web.struts.ValidationErrorsConvertor' %>
<%@ page import="com.ivata.mask.web.RewriteHandling"%>

<%@ page import='org.apache.log4j.Logger'%>
<%@ page import='org.apache.struts.Globals'%>
<%@ page import='org.apache.struts.action.ActionMessage'%>
<%@ page import='org.apache.struts.action.ActionMessages'%>
<%@ page import='org.apache.struts.taglib.TagUtils'%>

<%@ page import='java.util.Arrays'%>
<%@ page import='java.util.Calendar'%>
<%@ page import='java.util.Iterator'%>
<%@ page import='java.util.Locale'%>
<%@ page import='java.util.Properties'%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.io.StringWriter"%>

<%@ page import='javax.mail.internet.InternetAddress'%>
<%@ page import='javax.mail.internet.MimeMessage'%>
<%@ page import='javax.mail.Address'%>
<%@ page import='javax.mail.Message'%>
<%@ page import='javax.mail.Transport'%>
<%@ page import="com.ivata.mask.web.tag.theme.ThemeConstants" %>

<%--
////////////////////////////////////////////////////////////////////////////////
// $Id: error.jsp,v 1.4.2.2 2005/10/08 17:29:12 colinmacleod Exp $
//
// This is where you'll end up if the intranet can't locate the page you're
// looking for.
//
// Since: ivata groupware 0.9 (2002-09-25)
// Author: Colin MacLeod <colin.macleod@ivata.com>
// $Revision: 1.4.2.2 $
//
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2001 - 2005 ivata limited.
// All rights reserved.
// =========================================================
// ivata groupware may be redistributed under the GNU General Public
// License as published by the Free Software Foundation;
// version 2 of the License.
//
// These programs are free software; you can redistribute them and/or
// modify them under the terms of the GNU General Public License
// as published by the Free Software Foundation; version 2 of the License.
//
// These programs are distributed in the hope that they will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//
// See the GNU General Public License in the file LICENSE.txt for more
// details.
//
// If you would like a copy of the GNU General Public License write to
//
// Free Software Foundation, Inc.
// 59 Temple Place - Suite 330
// Boston, MA 02111-1307, USA.
//
//
// To arrange commercial support and licensing, contact ivata at
//                  http://www.ivata.com/contact.jsp
//
////////////////////////////////////////////////////////////////////////////////
//
// $Log: error.jsp,v $
// Revision 1.4.2.2  2005/10/08 17:29:12  colinmacleod
// Added extra logging.
//
// Revision 1.4.2.1  2005/10/04 16:54:24  colinmacleod
// Fixed error handling of unavailable jcom.dll on Windows.
// Affects recent versions of Tomcat which need the library in the tomcat
// bin directory. Improved install docu to match.
//
// Revision 1.3  2005/10/02 10:24:09  colinmacleod
// Fixed type in RightViolationException class name.
//
// Revision 1.2  2005/09/29 13:48:53  colinmacleod
// Added field error and user right messages to application resources.
//
// Revision 1.1  2005/09/14 15:28:36  colinmacleod
// Moved from package/war for reuse in ivata cms.
//
// Revision 1.4  2005/04/28 18:47:06  colinmacleod
// Fixed XHMTL, styles and resin compatibility.
// Added support for URL rewriting.
//
// Revision 1.3  2005/04/10 20:10:11  colinmacleod
// Added new themes.
// Changed id type to String.
// Changed i tag to em and b tag to strong.
// Improved PicoContainerFactory with NanoContainer scripts.
//
// Revision 1.2  2005/04/09 17:19:54  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:49:21  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.8  2004/12/31 19:18:33  colinmacleod
// Simplified exception routine by using ThrowableHandling class.
//
// Revision 1.7  2004/12/27 14:52:02  colinmacleod
// Removed unused references to RequestUtils.
// Updated other references to Struts 1.2.4 class TagUtils.
//
// Revision 1.6  2004/12/23 21:01:29  colinmacleod
// Updated Struts to v1.2.4.
// Changed base classes to use ivata masks.
//
// Revision 1.5  2004/11/03 16:02:24  colinmacleod
// Changed fieldPath to resourceFieldPath.
//
// Revision 1.4  2004/07/14 22:50:26  colinmacleod
// Replaced bean:define with extended igw:bean.
//
// Revision 1.3  2004/07/13 19:48:09  colinmacleod
// Moved project to POJOs from EJBs.
// Applied PicoContainer to services layer (replacing session EJBs).
// Applied Hibernate to persistence layer (replacing entity EJBs).
//
// Revision 1.2  2004/03/21 21:16:33  colinmacleod
// Shortened name to ivata op.
//
// Revision 1.1.1.1  2004/01/27 20:59:19  colinmacleod
// Moved ivata op to sourceforge.
//
// Revision 1.4  2003/12/16 15:09:08  jano
// fixing functionaality in buil.xml
//
// Revision 1.3  2003/12/12 11:10:11  jano
// fixing functionaality
//
// Revision 1.2  2003/10/17 14:14:21  jano
// changed names intranet -> portal
//
// Revision 1.1.1.1  2003/10/13 20:47:24  colin
// Restructured portal into subprojects
//
// Revision 1.11  2003/06/10 05:56:38  peter
// img and html:img replaced with size-aware imhtml:img
//
// Revision 1.10  2003/02/20 10:20:45  colin
// now displays last URL
//
// Revision 1.9  2003/02/20 09:45:42  colin
// added checkSite tag
//
// Revision 1.8  2003/01/27 07:17:52  colin
// update copyright notice
// changed cssClass to styleClass
//
// Revision 1.7  2003/01/18 20:04:40  colin
// fixed page not found message
//
// Revision 1.6  2003/01/14 10:31:29  colin
// internationalization - setting charset and changing <html...> to <imhtml:html locale='true'>
//
// Revision 1.5  2003/01/08 20:21:12  colin
// brought over debugging improvements from ivataShop
//
// Revision 1.4  2002/11/28 16:56:26  peter
// i18n finished
//
// Revision 1.3  2002/11/17 19:21:22  colin
// cosmetic changes - improved appearance and warning message
//
// Revision 1.2  2002/11/12 10:27:38  colin
// moved tag libraries to /WEB-INF/tag
//
// Revision 1.1  2002/09/26 08:15:35  colin
// first version of error hadnling page with link to login
//
////////////////////////////////////////////////////////////////////////////////
--%>
<%
  Logger logger = Logger.getLogger(this.getClass());
  logger.error ("error.jsp - start. Exception passed to page: " + exception, exception);
%>
<%--
we cannot guarantee that we have this class in the class path - ivata cms
uses this page but doesn't use the mail submodule
--%>
<%
Class noMailServerExceptionClass;
try {
  noMailServerExceptionClass = Class.forName("com.ivata.groupware.business.mail.server.NoMailServerException");
} catch (Exception e) {
  noMailServerExceptionClass = null;
}
%>
<%@taglib uri = '/WEB-INF/tag/jstl/c-rt.tld' prefix='c'%>
<%@taglib uri = '/WEB-INF/tag/struts/struts-bean.tld' prefix='bean'%>
<%@taglib uri = '/WEB-INF/tag/struts/struts-logic.tld' prefix='logic'%>
<%-- error codes are passed for some pages --%>
<bean:parameter id='errorCode' name='code' value='[none]'/>
<%--
  look in the request for an exception from Struts
--%>
<%
  Throwable cause = exception;
  logger.error ("error.jsp - Error code is: " + errorCode);
%>
<c:if test='<%=request.getAttribute("exception") != null%>'>
  <%
    cause = (Throwable) request.getAttribute("exception");
    logger.error ("error.jsp - Got 'exception' attribute: " + cause, cause);
  %>
</c:if>
<c:if test='<%=request.getAttribute(Globals.EXCEPTION_KEY) != null%>'>
  <%
    cause = (Throwable) request.getAttribute(Globals.EXCEPTION_KEY);
    logger.error ("error.jsp - Got Struts exception: " + cause, cause);
  %>
</c:if>
<%-- get the original cause of any system exception --%>
<c:catch>
  <%cause = ThrowableHandling.getCause(cause);%>
</c:catch>
<%logger.error ("error.jsp - Original cause: " + cause, cause);%>

<%-- forward any mail server errors to the special page for those --%>
<c:if test='<%=((noMailServerExceptionClass != null) && (noMailServerExceptionClass.isInstance(cause)))%>'>
  <%session.setAttribute("mailServerException", cause);%>
  <logic:forward name="mailServerError"/>
</c:if>

<%-- /include/tags.jspf checks the login but we don't want to do that --%>
<%@taglib uri = '/WEB-INF/tag/ivata/ivatagroupware.tld' prefix='igw'%>
<c:catch>
  <igw:checkSite/>
</c:catch>
<%@taglib uri = '/WEB-INF/tag/ivata/ivatathemedef.tld' prefix='imthemedef'%>
<%@taglib uri = '/WEB-INF/tag/ivata/ivatatheme.tld' prefix='imtheme'%>
<%@taglib uri = '/WEB-INF/tag/struts/struts-bean.tld' prefix='bean'%>
<%@taglib uri = '/WEB-INF/tag/struts/struts-html.tld' prefix='html'%>

<%@include file='/include/theme.jspf'%>

<%StringWriter stringWriter = null;%>

<%!
boolean debug = true;
String siteAdminEmail = "admin@ivata.com", emailHost, siteTheme = "classic";
%>
<%--
  this catch prevents it failing on any database error (in which case no email
  will be sent, and the error is displayed on the screen)
--%>
<c:catch>
  <igw:getSetting id='siteDebug' setting='siteDebug' type='java.lang.Boolean'/>
  <igw:getSetting id='siteAdminEmailSetting' setting='siteAdminEmail' type='java.lang.String'/>
  <igw:getSetting id='emailHostSetting' setting='emailHostSmtp' type='java.lang.String'/>
  <igw:getSetting id='siteThemeSetting' setting='siteTheme' type='java.lang.String'/>
  <%
    // if we're in debug mode, we'll send the errors to the screen and not send
    // emails out at all - change this to false for production environment
    debug = ((siteDebug == null) || siteDebug.booleanValue());
    siteAdminEmail = siteAdminEmailSetting;
    emailHost = emailHostSetting;
    siteTheme = siteThemeSetting;
  %>
</c:catch>

<jsp:include page="/generateCSS"/>
<html:html locale='true'>
  <head>
    <imhtml:base/>
    <meta http-equiv='Content-Type' content='text/html;charset=UTF-8; charset=utf-8'/>
    <meta http-equiv='Content-Style-Type' content='text/css' />
    <link rel='stylesheet' href='<%=RewriteHandling.getContextPath(request) + "/style/default.css"%>' type='text/css' />
    <link rel='stylesheet' href='<%=RewriteHandling.getContextPath(request) + "/style/" + siteTheme + ".css"%>' type='text/css' />
    <link rel='stylesheet' href='<%=RewriteHandling.getContextPath(request)%>/style/login.css' type='text/css' />
    <title><bean:message key='error.title' /></title>
    <%--
      if there is an error, make this a top-level page and force the user to
      login again (greater chance things will 'reset' themselves)
      note - we don't do this in debug mode (it's annoying!)
    --%>
    <c:if test='<%=!("404".equals(errorCode) || debug)%>'>
      <%if (logger.isDebugEnabled()) {logger.debug ("error.jsp - adding javascript to keep on top");}%>
      <script language='JavaScript' type='text/javascript'>
        <!--
          if (window != top) {
            top.location.href = location.href ;
          }
        // -->
      </script>
    </c:if>
  </head>
  <body>
    <div align='center'>
      <br/><br/><br/><br/>
      <igw:bean id='windowTitle' type='java.lang.String'><bean:message key='error.title.window'/></igw:bean>
      <imtheme:window title='<%=windowTitle%>'>
        <imtheme:frame>
          <imtheme:framePane styleClass='error'>
            <table cellpadding='0' cellspacing='0' width='100%'>
              <tr>
                <td>
                  <imhtml:img align='left' page='/images/error.gif'/>
                </td>
                <td><img align='left' width='20' height='1' page='/images/empty.gif' /></td>
                <c:choose>
                  <c:when test='<%="404".equals(errorCode)%>'>
                    <%logger.error ("error.jsp - displaying 'page not found' message.");%>
                    <td>
                      <bean:message key='error.label.notfound'/>
                    </td>
                  </c:when>
                  <c:when test='<%=cause instanceof ValidationException%>'>
                    <%logger.error ("error.jsp - displaying 'validation exception' message.");%>
                    <td>
                      <h3>
                        The action failed due to the following
                        validation error(s):
                      </h3>
                      <%
                        Locale locale = (Locale)
                                session.getAttribute(Globals.LOCALE_KEY);
                        ActionMessages actionErrors =
                          ValidationErrorsConvertor.toActionErrors(
                            ((ValidationException)cause)
                                .getErrors(),
                              locale);
                        Iterator iterator = actionErrors.get();
                        TagUtils tagUtils = TagUtils.getInstance();
                        String errorString;
                        while(iterator.hasNext()) {
                          ActionMessage errorMessage = (ActionMessage) iterator.next();
                          errorString = tagUtils.message(
                            pageContext,
                            null,
                            locale.toString(),
                            errorMessage.getKey(),
                            errorMessage.getValues());
                      %>
                      <c:choose>
                        <c:when test='<%=errorString == null%>'>
                           Error code <%=errorMessage.getKey()%>(
                           <c:if test='<%=errorMessage.getValues() == null%>'>
                             <%=Arrays.asList(errorMessage.getValues())%>
                           </c:if>
                        </c:when>
                        <c:otherwise>
                          <%=errorString%>
                        </c:otherwise>
                      </c:choose>
                      <br/>
                      <%}%>
                    </td>
                  </c:when>
                  <c:otherwise>
                    <td>
                      <c:choose>
                        <%-- usually when we have an UnsatisfiedLinkError, that's a missing jcom dll on Windows --%>
                        <c:when test='<%=cause instanceof UnsatisfiedLinkError%>'>
                          <%logger.error ("error.jsp - displaying 'unsatisfied link' message.");%>
                          <%debug = false;%>
                          <bean:message key='error.label.jcom'
                              arg0='<%=StringHandling.getNotNull(cause.getMessage(), "[none]")%>'
                              arg1='<%=siteAdminEmail%>'/>
                        </c:when>
                        <c:when test='<%=debug%>'>
                          <%logger.error ("error.jsp - displaying 'standard debug' message.");%>
                          <bean:message key='error.label.debug'
                              arg0='<%=RewriteHandling.getContextPath(request) + "/login.jsp"%>'
                              arg1='<%=siteAdminEmail%>'/>
                        </c:when>
                        <c:otherwise>
                          <%logger.error ("error.jsp - displaying 'standard default' message.");%>
                          <bean:message key='error.label.error'
                              arg0='<%=RewriteHandling.getContextPath(request) + "/login.jsp"%>'/>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <%
                      stringWriter = new StringWriter(256);
                      PrintWriter content = new PrintWriter(stringWriter, true);
                      content.write("An error occurred in application " + RewriteHandling.getContextPath(request) + " on ");
                      content.write(new java.util.Date().toString());
                    %>
                    <c:catch>
                      <%if (logger.isDebugEnabled()) {logger.debug ("error.jsp - preparing message to send.");}%>
                      <%-- ignore errors in the following (probably null exception --%>
                      <%
                        content.write("\n\nsessionID: " + session.getId());
                        content.write("\n\nsession.isNew: " + session.isNew());
                        content.write("\n\nCause exception: " + cause);
                        content.write("\n\nClass: ");
                        content.write((cause != null)
                            ? cause.getClass().toString()
                            : "[none]");
                        content.write("\nMessage: ");
                        content.write(((cause != null) && (cause.getMessage() != null))
                            ? cause.getMessage()
                            : "[none]");
                        cause.printStackTrace(content);
                        content.write("\n\nActually thrown exception: " + exception);
                        content.write("\n\nClass: ");
                        content.write((exception != null)
                            ? exception.getClass().toString()
                            : "[none]");
                        content.write("\n\nStack trace: ");
                        exception.printStackTrace(content);
                        exception.printStackTrace(System.out);
                     %>
                    </c:catch>
                    <%-- if this is not debug mode, send messages --%>
                    <c:if test='<%=!debug%>'>
                      <%if (logger.isDebugEnabled()) {logger.debug ("error.jsp - sending message to '" + siteAdminEmail + "'");}%>
                      <c:catch>
                      <%--
                        note: this uses JavaMail directly, since we want this
                        as simple/error free as possible
                        TODO: - wrap this in tag?
                      --%>
                      <%
                        Properties propertiesMail = new java.util.Properties(  );
                        propertiesMail.setProperty("mail.host", emailHost);
                        propertiesMail.setProperty("mail.smtp.host", emailHost);

                        javax.mail.Session mailSession = javax.mail.Session.getDefaultInstance(propertiesMail, null);
                        MimeMessage message = new MimeMessage(mailSession);
                        Address [] recipients = {new InternetAddress(siteAdminEmail)};
                        message.setRecipients(Message.RecipientType.TO, recipients);
                        message.setContent(stringWriter.toString(), "text/plain");
                        message.setFrom(new InternetAddress(siteAdminEmail));
                        message.setSentDate(Calendar.getInstance().getTime());

                        System.err.println("ERROR:");
                        System.err.println(stringWriter.toString());
                        System.err.println(" ----");
                        System.err.println("Now sending message to " + siteAdminEmail + ":");
                        System.err.println(" ----");
                        Transport.send(message);
                     %>
                      </c:catch>
                    </c:if>
                  </c:otherwise>
                </c:choose>
              </tr>
            </table>
          </imtheme:framePane>
        </imtheme:frame>
      </imtheme:window>

      <%-- if we're in debug mode, show the error to the screen --%>
      <c:if test='<%=debug && (stringWriter != null)%>'>
        <%logger.error ("error.jsp displaying debug error message on screen:\n" + stringWriter);%>
        <br/>
        <imtheme:window title="debug info" style='width: auto;'>
          <imtheme:frame>
            <imtheme:framePane>
              <pre><%=stringWriter%></pre>
            </imtheme:framePane>
          </imtheme:frame>
        </imtheme:window>
      </c:if>

    </div>
  </body>
</html:html>
<%logger.error ("error.jsp - end");%>


