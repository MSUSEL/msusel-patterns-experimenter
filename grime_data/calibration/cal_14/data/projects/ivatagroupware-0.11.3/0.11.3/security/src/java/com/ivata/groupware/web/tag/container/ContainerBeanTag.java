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
package com.ivata.groupware.web.tag.container;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;
import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;


/**
 * <p>This tag retrieves an instance of the provided class from the current
 * session contianer.</p>
 *
 * <p><strong>Tag attributes:</strong><br/>
 * <table cellpadding='2' cellspacing='5' border='0' align='center'
 * width='85%'>
 *   <tr class='TableHeadingColor'>
 *     <th>attribute</th>
 *     <th>reqd.</th>
 *     <th>param. class</th>
 *     <th width='100%'>description</th>
 *   </tr>
 *   <tr class='TableRowColor'>
 *     <td>id</td>
 *     <td>true</td>
 *     <td><code>String</code></td>
 *     <td>Id of the bean to be created.</td>
 *   </tr>
 *   <tr class='TableRowColor'>
 *     <td>type</td>
 *     <td>true</td>
 *     <td><code>String</code></td>
 *     <td>Class for which to retrieve an instance.</td>
 *   </tr>
 *   <tr class='TableRowColor'>
 *     <td>scope</td>
 *     <td>false</td>
 *     <td><code>String</code></td>
 *     <td>
 *      Scope into which to write the bean. Must be one of:
 *      <ul>
 *        <li>page (default)</li>
 *        <li>request</li>
 *        <li>session</li>
 *        <li>application</li>
 *      </ul>
 *     </td>
 *   </tr>
 * </table>
 * </p>
 *
 * @since 2004-06-12
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 */
public class ContainerBeanTag extends BodyTagSupport {
    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger
            .getLogger(ContainerBeanTag.class);

    private final static String [] SCOPES = new String[] {
            "application",
            "session",
            "request",
            "page"
    };

    /**
     * <p>
     * Contains the contents of the tag body, or <code>null</code> if the body
     * is empty.
     * </p>
     */
    private String body;
    /**
     * <p>
     * Identifier for page attribute.
     * </p>
     */
    private String id = null;

    /**
     * <p>
     * Name of a bean to use from the scope specified. This defaults to the
     * value of <code>id</code>.
     * </p>
     */
    private String name;

    /**
     * <p>
     * Name of a property in the bean called <code>name</code>. This will act
     * like <code>name.getProperty()</code>.
     * </p>
     */
    private String property;
    /**
     * <p>
     * Scope from which the page attribute is read.
     * </p>
     */
    private String scope;

    /**
     * <p>
     * Scope to which the page attribute is set. Defaults to the value for
     * <code>scope</code>.
     * </p>
     */
    private String toScope;

    /**
     * <p>Class of the setting to be retrieved - this must match the setting
     * type. Defaults to &quot;String&quot;</p>
     */
    private String type = null;

    /**
     * <p>
     * The value to set the object to, if set as an attribute. If the value is
     * a string, you can also use the tag body.
     * </p>
     */
    private Object value;

    /**
     * <p>
     * Wrapper for <code>RequestUtils.lookup</p>. This method does not throw
     * an exception if the bean property does not exist - it merely returns
     * <code>null</code>.
     * </p>
     *
     * @param pageContext Page context being processed.
     * @param beanName Name of a bean to locate in the page context.
     * @param property Optional property name within the bean. Set to
     * <code>null</code> to return the bean itself.
     * @param beanScope one of <ul><li>application</li><li>session</li>
     * <li>request</li> or <li>page</li></ul>
     * @return value of the bean or bean property, or <code>null</code> if no
     * such bean exists in any scope.
     * @throws JspException Thrown by <code>RequestUtils.lookup</code>.
     */
    private Object beanLookup(final PageContext pageContext,
            final String beanName,
            final String property,
            final String beanScope)
            throws JspException {
        TagUtils tagUtils = TagUtils.getInstance();
        Object thisValue = tagUtils.lookup(pageContext, beanName, beanScope);
        // if there is a property, only find the value for that if there
        // is a bean in the scope requested (otherwise an exception is
        // thrown)
        if ((thisValue != null)
                && (property != null)) {
            thisValue = tagUtils.lookup(pageContext, beanName, property, beanScope);
        }
        return thisValue;
    }

    /**
     * TODO
     *
     * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
     */
    public int doAfterBody() throws JspException {
        String thisBody;
        if ((bodyContent != null)
                && ((thisBody = bodyContent.getString()) != null)
                && ((thisBody = thisBody.trim()).length() > 0)) {
            body = thisBody;
        }
        return SKIP_BODY;

    }

    public int doEndTag() throws JspException {
        // before we do anything else, get the request and session
        HttpSession session = pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest)
            pageContext.getRequest();

        Object thisValue = null;
        String actualType = this.type;
        if (actualType == null) {
            // if there is no name specified, assume that the tag contents are
            // used
            if ((name == null) || (value != null)) {
                actualType = "java.lang.String";
            } else {
                actualType = "java.lang.Object";
            }
        }

        if (body != null) {
            // check both attribute and body were not supplied!
            if (value != null) {
                throw new JspException("ERROR in ContainerBeanTag(id "
                        + id
                        + "): you cannot specify both tag body ('"
                        + body
                        + "') and value attribute ('"
                        + value
                        + "'");
            }
            thisValue = body;
            actualType = "java.lang.String";
        } else {
            thisValue = value;
        }

        // finally look to see if there is a default in the chosen scope
        if (thisValue == null) {
            // if no name specified, use the id as name
            String beanName = name == null ? id : name;
            String beanScope = scope;

            if (scope == null) {
                Class searchClass;
                try {
                    searchClass = Class.forName(actualType);
                } catch (ClassNotFoundException e) {
                    throw new JspException(e);
                }


                // if there was no appropriate bean at application scope, go
                // thro' the others too
                for (int i = 0; i < SCOPES.length; i++) {
                    thisValue = beanLookup(pageContext, beanName,
                            property, beanScope = SCOPES[i]);
                    // if we found a valid bean, get out here...
                    if ((thisValue != null)
                            && searchClass.isAssignableFrom(
                                    thisValue.getClass())) {
                        break;
                    }
                }
            } else {
                thisValue = beanLookup(pageContext, beanName,
                        property, beanScope);
            }
        }

        // if we have a string, default it to a request parameter, if one is
        // available; request parameters always override the value for igw beans
        // :-)
        if ((thisValue == null)
                && "java.lang.String".equals(actualType)
                && !StringHandling.isNullOrEmpty(request.getParameter(id))) {
            thisValue = request.getParameter(id);
        }

        // if the value is _still_ null, try to create a new bean from the
        // container
        if (thisValue == null) {
            // initialize primitive type wrappers
            if ("java.lang.Boolean".equals(actualType)) {
                thisValue = Boolean.FALSE;
            } else if ("java.lang.Byte".equals(actualType)) {
                thisValue = new Byte((byte)0);
            } else if ("java.lang.Character".equals(actualType)) {
                thisValue = new Character((char)0);
            } else if ("java.lang.double".equals(actualType)) {
                thisValue = new Double(0L);
            } else if ("java.lang.Float".equals(actualType)) {
                thisValue = new Float(0L);
            } else if ("java.lang.Integer".equals(actualType)) {
                thisValue = new Integer(0);
            } else if ("java.lang.Long".equals(actualType)) {
                thisValue = new Long(0L);
            } else if ("java.lang.Short".equals(actualType)) {
                thisValue = new Short((short)0);
            } else {
                // really use the container
                SecuritySession securitySession = (SecuritySession)
                session.getAttribute("securitySession");
                PicoContainer container;

                // if there is no security session yet, use the default
                // container
                // this should only really happen in /login.jsp or /index.jsp
                PicoContainerFactory factory;
                try {
                    factory = PicoContainerFactory.getInstance();
                } catch (SystemException e) {
                    throw new JspException(e);
                }
                if (securitySession == null) {
                    container = factory.getGlobalContainer();
                } else {
                    container = securitySession.getContainer();
                }
                // override - we want fresh instances
                try {
                    container = factory.override(container);
                    thisValue =
                        factory.instantiateOrOverride(container,
                                actualType);
                } catch (SystemException e) {
                    throw new JspException(e);
                }
            }
        }

        if (thisValue == null) {
            throw new JspException("ERROR: could not instantiate id '"
                    + id
                    + "' for class '"
                    + actualType
                    + "'");
        }

        // set the value to the page
        int writeScope;
        TagUtils tagUtils = TagUtils.getInstance();
        if (toScope != null) {
            writeScope = tagUtils.getScope(toScope);
        } else if (scope != null) {
            writeScope = tagUtils.getScope(scope);
        } else {
            writeScope = PageContext.PAGE_SCOPE;
        }
        pageContext.setAttribute(id, thisValue, writeScope);

        return EVAL_PAGE;
    }

    /**
     * <p>This method is called when the JSP engine encounters the start
     * tag, after the attributes are processed.<p>
     *
     * @return <code>EVAL_BODY_BUFFERED</code> since this tag may have a body
     * @exception JspException if there is a <code>NamingExcpetion</code>
     * getting the <code>InitialContext</code>
     * @exception JspException if the session applicationServer is not
     * set
     * @throws JspException if there is a problem creating the
     * SettingsRemote  EJB
     * @throws JspException if there is a
     * <code>java.rmi.RemoteException</code retrieving the setting
     * @throws JspException if there is an error wrting to
     * <code>out.print()</code>
     */
    public int doStartTag() throws JspException {
        return EVAL_BODY_BUFFERED;
    }


    /**
     * <p>
     * Identifier for page attribute.
     * </p>
     *
     * @return current identifier for page attribute.
     */
    public final String getId() {
        return id;
    }

    /**
     * <p>
     * Name of a bean to use from the scope specified. This defaults to the
     * value of <code>id</code>.
     * </p>
     *
     * @return current value of name.
     */
    public final String getName() {
        return name;
    }
    /**
     * <p>
     * Name of a property in the bean called <code>name</code>. This will act
     * like <code>name.getProperty()</code>.
     * </p>
     *
     * @return current value of property.
     */
    public final String getProperty() {
        return property;
    }

    /**
     * <p>
     * Scope to which the page attribute is set.
     * </p>
     *
     * @return scope to which the page attribute is set.
     */
    public final String getScope() {
        return scope;
    }
    /**
     * <p>
     * Scope to which the page attribute is set. Defaults to the value for
     * <code>scope</code>.
     * </p>
     *
     * @return current value of toScope.
     */
    public final String getToScope() {
        return toScope;
    }

    /**
     * <p>Class of the setting to be retrieved - this must match the setting
     * type.</p>
     *
     * @return class of the setting to be retrieved.
     */
    public final String getType() {
        return type;
    }
    /**
     * <p>
     * The value to set the object to, if set as an attribute. If the value is
     * a string, you can also use the tag body.
     * </p>
     *
     * @return current value of value.
     */
    public final Object getValue() {
        return value;
    }

    /**
     * <p>
     * Identifier for page attribute.
     * </p>
     *
     * @param string new identifier for page attribute.
     */
    public final void setId(final String string) {
        id = string;
    }
    /**
     * <p>
     * Name of a bean to use from the scope specified. This defaults to the
     * value of <code>id</code>.
     * </p>
     *
     * @param name new value of name.
     */
    public final void setName(final String name) {
        this.name = name;
    }
    /**
     * <p>
     * Name of a property in the bean called <code>name</code>. This will act
     * like <code>name.getProperty()</code>.
     * </p>
     *
     * @param property new value of property.
     */
    public final void setProperty(final String property) {
        this.property = property;
    }

    /**
     * <p>
     * Scope to which the page attribute is set.
     * </p>
     *
     * @param string scope to which the page attribute is set.
     */
    public final void setScope(final String string) {
        scope = string;
    }
    /**
     * <p>
     * Scope to which the page attribute is set. Defaults to the value for
     * <code>scope</code>.
     * </p>
     *
     * @param toScope new value of toScope.
     */
    public final void setToScope(final String toScope) {
        this.toScope = toScope;
    }

    /**
     * <p>Class of the setting to be retrieved.</p>
     *
     * @param string class of the setting to be retrieved.
     */
    public final void setType(final String string) {
        type = string;
    }
    /**
     * <p>
     * The value to set the object to, if set as an attribute. If the value is
     * a string, you can also use the tag body.
     * </p>
     *
     * @param value new value of value.
     */
    public final void setValue(final Object value) {
        this.value = value;
    }
}
