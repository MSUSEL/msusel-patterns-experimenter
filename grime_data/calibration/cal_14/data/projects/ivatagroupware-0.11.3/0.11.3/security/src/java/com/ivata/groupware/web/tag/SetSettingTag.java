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

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.Security;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.mask.util.SystemException;


/**
 * <p>This tag tries to set the setting for the user specified by the
 * attribute 'userName'. If that has not been set, then the default
 * setting is
 * altered.</p>
 *
 * <p>Normally there is a settings object in the session called
 * 'SettingsRemote'
 * and this is used internally to access the ivata groupware settings
 * system.</p>
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
 *     <td>The name of the setting to change in the ivata groupware
 * system.</td>
 *   </tr>
 *   <tr class='TableRowColor'>
 *     <td>userName</td>
 *     <td>false</td>
 *     <td><code>String</code></td>
 *     <td>The name of the user for whom the setting will be make. If
 * this
 * attribute is not set, then the default setting is altered.</td>
 *   </tr>
 *   <tr class='TableRowColor'>
 *     <td>value</td>
 *     <td>true</td>
 *     <td><code>Object</code></td>
 *     <td>The new value for the setting specified. This object
 * should
 * be
 * of type <code>Integer</code> or
 * <code>String</code>
 * or <code>Boolean</code> and the type will be noted in
 * the
 * ivata groupware settings.</td>
 *   </tr>
 * </table>
 * </p>
 *
 * @since 2002-02-02
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class SetSettingTag extends TagSupport {
    /**
     * <p>Property declaration for tag attribute: setting.</p>
     */
    private String setting = null;
    /**
     * <p>Property declaration for tag attribute: userName.</p>
     */
    private String userName = null;
    /**
     * <p>Property declaraion for tag attribute: value.</p>
     */
    private Object value = null;

    /**
     * <p>Default constructor.</p>

     */
    public SetSettingTag() {
        super();
    }

    /**
     * <p>This method is called after the JSP engine finished processing
     * the tag.</p>
     *
     * @return <code>EVAL_PAGE</code> since we always want to evaluate the
     * rest of the page
     */
    public int doEndTag() {
        // we want the rest of the page after this tag to be evaluated...
        return EVAL_PAGE;
    }

    /**
     * <p>This method is called when the JSP engine encounters the start
     * tag, after the attributes are processed.<p>
     *
     * <p>Scripting variables (if any) have their values set here.</p>
     *
     * @return <code>SKIP_BODY</code> since this tag has no body
     * @throws JspException if there is no attribute in the session
     * called 'SettingsRemote'
     * @throws JspException if there is a
     * <code>java.rmi.RemoteException</code> assigning the setting
     */
    public int doStartTag() throws JspException {
        // before we do anything else, get the session
        HttpSession session = pageContext.getSession();
        SecuritySession securitySession = (SecuritySession)
            session.getAttribute("securitySession");

        if(securitySession == null) {
            throw new JspException("Error in SetSettingTag: no security session object was set in the  servlet session");
        }
        PicoContainer container = securitySession.getContainer();
        Settings settings = (Settings) container.getComponentInstance(Settings.class);
        Security security = (Security) container.getComponentInstance(Security.class);

        try {
            UserDO user = userName == null ? null : security.findUserByName(securitySession, userName);
            settings.amendSetting(securitySession, setting, value, user);
        } catch(SystemException e) {
            throw new JspException(e);
        }
        // indicates that the body should not be evaluated - this tag has no body
        return SKIP_BODY;
    }

    /**
     * <p>Get the value supplied to the attribute 'setting'.</p>
     *
     * <p>This attribute represents the name of the setting to change in
     * the ivata groupware system.</p>
     *
     * @return the value supplied to the attribute 'setting'
     */
    public final String getSetting() {
        return setting;
    }

    /**
     * <p>Get the value supplied to the attribute 'userName'.</p>
     *
     * <p>This attribute represents the name of the user for whom the
     * setting will be made. If this attribute is not set, then the
     * default setting is altered.</p>
     *
     * @return the value supplied to the tag attribute 'userName'
     */
    public final String getUserName() {
        return userName;
    }

    /**
     * <p>Get the value supplied to the attribute 'value'.</p>
     *
     * <p>This attribute represents the new value for the setting
     * specified. This object should be of type
     * <code>Integer</code>, <code>String</code> or
     * <code>Boolean</code> and the type will be noted in the
     * ivata groupware settings.</p>
     *
     * @return the value supplied to the attribute 'value'
     */
    public final Object getValue() {
        return value;
    }

    /**
     * <p>Set the value supplied to the attribute 'setting'.</p>
     *
     * <p>This attribute represents the name of the setting to change in
     * the ivata groupware system.</p>
     *
     * @param setting the value supplied to the attribute 'setting'
     */
    public final void setSetting(final String setting) {
        this.setting = setting;
    }

    /**
     * <p>Set the value supplied to the tag attribute 'userName'</p>
     *
     * <p>This attribute represents the name of the user for whom the
     * setting will be made. If this attribute is not set, then the
     * default setting is altered.</p>
     *
     * @param userName the value supplied to the tag attribute 'userName'
     */
    public final void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * <p>Set the value supplied to the attribute 'value'.</p>
     *
     * <p>This attribute represents the new value for the setting
     * specified. This object should be of type
     * <code>Integer</code>, <code>String</code> or
     * <code>Boolean</code> and the type will be noted in the
     * ivata groupware settings.</p>
     *
     * @param value the value supplied to the attribute 'value'
     */
    public final void setValue(final Object value) {
        this.value = value;
    }
}
