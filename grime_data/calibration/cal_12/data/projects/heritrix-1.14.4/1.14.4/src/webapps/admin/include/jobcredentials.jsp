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
<%@ page import="org.archive.crawler.settings.*" %>
<%@ page import="org.archive.util.TextUtils" %>

<%@ page import="java.util.List" %>
<%@ page import="javax.management.MBeanInfo"%>
<%@ page import="javax.management.MBeanAttributeInfo"%>

<%!
    /**
     * This include page contains methods used by the job credential pages,
     * (global - not yet), override and refinements.
     *
     * @author Kristinn Sigurdsson
     */
     
    /**
     * Builds the HTML to edit a map of Credentials
     *
     * @param map The map to edit
     * @param availibleOptions List of availible modules that can be added to the map
     *                         (full class names as Strings)
     * @param name domain the CrawlerSettings for the current domain
     *
     * @return the HTML to edit the specified modules map
     */
    public String buildModuleMap(ComplexType map, List availibleOptions, CrawlerSettings domain){
        StringBuffer ret = new StringBuffer();
        
        ret.append("<table cellspacing='0' cellpadding='2'>");
        
        MBeanInfo mapInfo = map.getMBeanInfo(domain);
        MBeanAttributeInfo m[] = mapInfo.getAttributes();
            
        // Printout modules in map.
        boolean alt = true;
        for(int n=0; n<m.length; n++) {
            ModuleAttributeInfo att = (ModuleAttributeInfo)m[n]; //The attributes of the current attribute.
            Object currentAttribute = null;
            Object localAttribute = null;

            try {
                currentAttribute = map.getAttribute(domain,att.getName());
                localAttribute = map.getLocalAttribute(domain,att.getName());
            } catch (Exception e1) {
                ret.append(e1.toString() + " " + e1.getMessage());
            }
    
            ret.append("<tr");
            if(alt){
                ret.append(" bgcolor='#EEEEFF'");
            }
            ret.append(">");
            
            if(localAttribute == null){
                // Inherited. Print for display only
                ret.append("<td><i>" + att.getName() + "</i></td><td><i>&nbsp;"+att.getType()+"</i></td>");
                ret.append("<td></td>");
                ret.append("<td><a href=\"javascript:alert('");
                ret.append(TextUtils.escapeForHTMLJavascript(att.getDescription()));
                ret.append("')\">Info</a></td>\n");
                ret.append("</tr>");
            } else {
                ret.append("<td>" + att.getName() + "</td><td>&nbsp;"+att.getType()+"</td>");
                ret.append("<td><a href=\"javascript:doRemove('"+att.getName()+"')\">Remove</a></td>");
                ret.append("<td><a href=\"javascript:alert('");
                ret.append(TextUtils.escapeForHTMLJavascript(att.getDescription()));
                ret.append("')\">Info</a></td>\n");
                ret.append("</tr>");
            }
            alt = !alt;
        }
        
        // Find out which aren't being used.
        if(availibleOptions.size() > 0 ){
            ret.append("<tr><td>");
            ret.append("<input name='name'>");
            ret.append("</td><td>");
            ret.append("<select name='cboAdd'>");
            for(int i=0 ; i<availibleOptions.size() ; i++){
                String curr = ((Class)availibleOptions.get(i)).getName();
                ret.append("<option value='"+curr+"'>"+curr+"</option>");
            }
            ret.append("</select>");
            ret.append("</td><td>");
            ret.append("<input type='button' value='Add' onClick=\"doAdd()\">");
            ret.append("</td></tr>");
        }
        ret.append("</table>");
        return ret.toString();
    }

%>
