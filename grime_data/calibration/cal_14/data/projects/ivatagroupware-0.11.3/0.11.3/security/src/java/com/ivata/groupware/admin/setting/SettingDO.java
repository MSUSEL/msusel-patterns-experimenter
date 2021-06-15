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
package com.ivata.groupware.admin.setting;


import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.container.persistence.NamedDO;

/**
 * <p>This class represents a single setting within the system. Each setting can
 * either be global, or overridden for a single user. You can have integer,
 * string or boolean settings. These are identified by the type constants
 * defined in {@link SettingConstants SettingConstants}.</p>
 *
 * @since 2001-04-20
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @hibernate.class
 *      table="setting"
 * @hibernate.cache
 *      usage="read-write"
 */
public class SettingDO  extends NamedDO {
    /**
     * <p>Clear text description of the purpose of the setting and what
     * elements of the intranet system this setting affects.</p>
     */
    private String description;

    /**
     * <p>Defines whether or not this setting is currently enabled.</p>
     */
    private boolean enabled = true;

    /**
     * <p>Name of this setting. This is a clear text identifier which
     * should be unique within the system. Use alphanumeric characters with no
     * spaces.</p>
     */
    private String name;

    /**
     * <p>Type of the settting. This can be any of the 'TYPE_...'
     * constants defined in {@link SettingConstant SettingConstant}.</p>
     */
    private int type;

    /**
     * <p>User this setting applies to, or <code>null</code> if it is
     * the default setting.</p>
     */
    private UserDO user;

    /**
     * <p>Value of the setting. Whilst all settings are stored as
     * strings, the content should represent the type of the setting so that
     * integer types will only contain numeric strings, and boolean types will
     * only contain 'true' or 'false'.</p>
     */
    private String value;
    /**
     * <p>Get a clear text description of the purpose of the setting and what
     * elements of the intranet system this setting affects.</p>
     *
     * @return a clear text description of the purpose of the setting and what
     *     elements of the intranet system this setting affects.
     *
     * @hibernate.property
     */
    public final String getDescription() {
        return description;
    }
    /**
     * <p>Get the name of this setting. This is a clear text identifier which
     * should be unique within the system. Use alphanumeric characters with no
     * spaces.</p>
     *
     * @return the name of the setting, a clear text identifier.
     *
     * @hibernate.property
     */
    public final String getName() {
        return name;
    }
    /**
     * <p>Get the type of the settting. This can be any of the 'TYPE_...'
     * constants defined in {@link SettingConstant SettingConstant}.</p>
     *
     * @return the type of the settting, one of the 'TYPE_...' constants defined
     *      in {@link SettingConstant SettingConstant}.
     *
     * @hibernate.property
     */
    public final int getType() {
        return type;
    }

    /**
     * <p>Get the user this setting applies to, or <code>null</code> if it is
     * the default setting.</p>
     *
     * @return the user this setting applies to, or <code>null</code> if it is
     *     the default setting.
     *
     * @hibernate.many-to-one
     *      column="person_user"
     */
    public UserDO getUser() {
        return user;
    }
    /**
     * <p>Get the value of the setting. Whilst all settings are stored as
     * strings, the content should represent the type of the setting so that
     * integer types will only contain numeric strings, and boolean types will
     * only contain 'true' or 'false'.</p>
     *
     * @return the value of the setting.
     *
     * @hibernate.property
     */
    public final String getValue() {
        return value;
    }
    /**
     * <p>Get whether or not this setting is currently enabled.</p>
     *
     * @return <code>true</code> if the setting may be used currently, otherwise
     *      <code>false</code>.
     *
     * @hibernate.property
     *      column="enable"
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * <p>Set a clear text description of the purpose of the setting and what
     * elements of the intranet system this setting affects.</p>
     *
     * @param description a clear text description of the purpose of the setting
     *      and what elements of the intranet system this setting affects.
     */
    public final void setDescription(final String description) {
        this.description = description;
    }

    /**
     * <p>Set this setting to be currently enabled.</p>
     *
     * @param enable <code>true</code> if the setting may be used currently,
     *     otherwise <code>false</code>.
     */
    public final void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * <p>Set the name of this setting. This is a clear text identifier which
     * should be unique within the system. Use alphanumeric characters with no
     * spaces.</p>
     *
     * @param name the name of the setting, a clear text identifier.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * <p>Set the type of the setting. This can be any of the 'TYPE_...'
     * constants defined in {@link SettingConstant SettingConstant}.</p>
     *
     * @param type the type of the settting, one of the 'TYPE_...' constants
     *     defined in {@link SettingConstant SettingConstant}.
     */
    public final void setType(final int type) {
        this.type = type;
    }

    /**
     * <p>Set the user this setting applies to, or <code>null</code> if it is
     * the default setting.</p>
     *
     * @param user the user this setting applies to, or <code>null</code> if it
     *     is the default setting.
     */
    public final void setUser(final UserDO user) {
        this.user = user;
    }

    /**
     * <p>Set the value of the setting. Whilst all settings are stored as
     * strings, the content should represent the type of the setting so that
     * integer types will only contain numeric strings, and boolean types will
     * only contain 'true' or 'false'.</p>
     *
     * @param value the value of the setting.
     */
    public final void setValue(final String value) {
        this.value = value;
    }
}