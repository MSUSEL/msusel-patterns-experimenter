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
package com.ivata.groupware.admin.security.user;

import com.ivata.groupware.business.event.Event;

/**
 * <p>To implement a specific event with detail specific to your system, you
 * need to extend this class.</p>
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 */
public class UserEvent extends Event {
    /**
     * <p>Stores the user name for the user this event relates to.</p>
     */
    private String userName = null;

    /**
     * <p>Construct an event and set the name of the <em>JMS</em> topic associated
     * with this event.</p>
     *
     * @param userName the name of the user with whom this event is associated.
     * @param topic set to one of the constants in <code>UserEventConstants</code>.
     * @see com.ivata.groupware.business.addressbook.person.group.right.RightConstants
     */
    public UserEvent(String userNameParam, String topic) {
        super(topic);
        userName = userNameParam;
    }

    /**
     * <p>Get  the user name for the user this event relates to.</p>
     *
     * @return  the user name for the user this event relates to.</p>
     */
    public final String getUserName() {
        return userName;
    }

}
