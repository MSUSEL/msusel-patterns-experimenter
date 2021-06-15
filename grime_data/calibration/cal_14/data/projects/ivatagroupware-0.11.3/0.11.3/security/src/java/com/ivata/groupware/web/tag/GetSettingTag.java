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
package com.ivata.groupware.web.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;
import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.util.SystemException;


/**
 * <p>This tag tries first to get the setting for the user specified
 * by the session attribute 'userName'. If that has not been set (a
 * la
 * login page), or  there is no setting for this user, then the
 * default
 * setting is retrieved.</p>
 *
 * <p>Normally there is a settings object in the session called
 * 'SettingsRemote' and this is used internally to access the
 * ivata groupware settings system.</p>
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
 *     <td>setting</td>
 *     <td>true</td>
 *     <td><code>String</code></td>
 *     <td>The name of the setting to retrieve from the ivata groupware
 * system.</td>
 *   </tr>
 *   <tr class='TableRowColor'>
 *     <td>userName</td>
 *     <td>false</td>
 *     <td><code>String</code></td>
 *     <td>The name of the user for whom to retrieve the
 * ivata groupware
 * system
 * setting.</td>
 *   </tr>
 * </table>
 * </p>
 *
 *
 * @since 2002-02-02
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 */
public class GetSettingTag extends TagSupport {
    /**
     * <p>
     * Identifier for page attribute. If this attribute is not set, then
     * the setting is output to the page.
     * </p>
     */
    private String id = null;
    /**
     * <p>
     * Scope to which the page attribute is set, if <code>id</code> is set.
     * </p>
     */
    private String scope = "page";
    /**
     * <p>Property declaration for tag attribute: setting.</p>
     */
    private String setting = null;
    /**
     * <p>Class of the setting to be retrieved - this must match the setting
     * type.</p>
     */
    private String type = null;

    /**
     * <p>This method is called when the JSP engine encounters the start
     * tag, after the attributes are processed.<p>
     *
     * <p>Scripting variables (if any) have their values set here.</p>
     *
     * <p><strong>Technical implementation:</strong><br/> We locate a {@link
     * com.ivata.groupware.admin.setting.SettingsRemote SettingsRemote} object
     * in
     * the session (attribute) 'SettingsRemote'. We then use this to get
     * the setting specified. If it is not null, we send this to the
     * <code>JspWriter</code> from the <code>pageContext</code>.</p>
     *
     * @return <code>SKIP_BODY</code> since this tag has no body
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
        // before we do anything else, get the session
        HttpSession session = pageContext.getSession();
        SecuritySession securitySession = (SecuritySession)
            session.getAttribute("securitySession");
        PicoContainer container;
        UserDO user;
        // if there is no security session (= timeout), then we'll default to
        // the global system setting
        if (securitySession == null) {
            try {
                container = PicoContainerFactory.getInstance().getGlobalContainer();
            } catch (SystemException e) {
                throw new JspException(e);
            }
            user = null;
        } else {
            container = securitySession.getContainer();
            user = securitySession.getUser();
        }
        Settings settings = (Settings) container.getComponentInstance(
                Settings.class);

        Object value;
        try {
            value = settings.getSetting(securitySession, setting,
                    user);
        } catch(SystemException eRemote) {
            throw new JspException(eRemote);
        }

        // check the type matches, if specified
        if (type != null) {
            Class typeClass;
            try {
                typeClass = Class.forName(type);
            } catch (ClassNotFoundException e) {
                throw new JspException(e);
            }
            if ((value != null) 
                    && (!typeClass.isAssignableFrom(value.getClass()))) {
                throw new JspException("ERROR: setting '"
                    + setting
                    + "' is type '"
                    + value.getClass().getName()
                    + "', expected '"
                    + type
                    + "'");
            }
        }


        // if we got a value, try sending this to the page
        if(value != null) {
            if (id == null) {
                try {
                    JspWriter out = pageContext.getOut();
                    out.print(value);
                } catch(IOException eIo) {
                    throw new JspException(eIo);
                }
            } else {
                // if there _is_ an id, set the value to the page
                HttpServletRequest request = (HttpServletRequest)
                    pageContext.getRequest();
                int pageScope = TagUtils.getInstance().getScope(scope);
                pageContext.setAttribute(id, value, pageScope);
            }
        }
        // indicates that the body should not be evaluated - this tag has no body
        return SKIP_BODY;
    }
    /**
     * <p>
     * Identifier for page attribute. If this attribute is not set, then
     * the setting is output to the page.
     * </p>
     *
     * @return current identifier for page attribute.
     */
    public final String getId() {
        return id;
    }

    /**
     * <p>
     * Scope to which the page attribute is set, if <code>id</code> is set.
     * </p>
     *
     * @return scope to which the page attribute is set.
     */
    public final String getScope() {
        return scope;
    }

    /**
     * <p>Get the value supplied to the attribute 'setting'.</p>
     *
     * <p>This attribute represents the name of the setting to retrieve
     * from the ivata groupware system.</p>
     *
     * @return the value supplied to the attribute 'setting'
     */
    public final String getSetting() {
        return setting;
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
     * Identifier for page attribute. If this attribute is not set, then
     * the setting is output to the page.
     * </p>
     *
     * @param string new identifier for page attribute.
     */
    public final void setId(final String string) {
        id = string;
    }

    /**
     * <p>
     * Scope to which the page attribute is set, if <code>id</code> is set.
     * </p>
     *
     * @param string scope to which the page attribute is set.
     */
    public final void setScope(final String string) {
        scope = string;
    }

    /**
     * <p>Set the value supplied to the attribute 'setting'.</p>
     *
     * <p>This attribute represents the name of the setting to retrieve
     * from the ivata groupware system.</p>
     *
     * @param sSetting the value supplied to the attribute 'setting'
     */
    public final void setSetting(final String setting) {
        this.setting = setting;
    }

    /**
     * <p>Class of the setting to be retrieved - this must match the setting
     * type.</p>
     *
     * @param string class of the setting to be retrieved.
     */
    public final void setType(final String string) {
        type = string;
    }

}
