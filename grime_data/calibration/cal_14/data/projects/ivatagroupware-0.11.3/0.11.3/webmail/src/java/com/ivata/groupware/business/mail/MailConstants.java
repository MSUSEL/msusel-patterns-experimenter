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
package com.ivata.groupware.business.mail;


/**
 * <p>Constants within the mail subsystem, to indicate how mail
 * messages relate to one another.</p>
 *
 * @since 2002-11-10
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */
public class MailConstants {
    /**
     * <p>Default state for a message - indicates a new message with no
     * association to previous messages.</p>
     */
    public static final Integer THREAD_NEW = new Integer(0);

    /**
     * <p>Indicates that the new message is a reply to an existing mail,
     * addressed only to that mail's sender (not the other
     * recipients).</p>
     */
    public static final Integer THREAD_REPLY = new Integer(1);

    /**
     * <p>Indicates that the new message is a reply to an existing mail,
     * addressed both to that mail's sender and the other (CC)
     * recipients.</p>
     */
    public static final Integer THREAD_REPLY_ALL = new Integer(2);

    /**
     * <p>Indicates that the new message is created by forwarding other
     * messages.</p>
     */
    public static final Integer THREAD_FORWARD = new Integer(3);

    /**
     * <p>Indicates that the new message is created by editing an existing
     * message.</p>
     */
    public static final Integer THREAD_DRAFT = new Integer(4);

    /**
     * <p>Indicates a list of messages should be sorted by the message
     * folder.</p>
     */
    public static final Integer SORT_FOLDER = new Integer(0);

    /**
     * <p>Indicates a list of messages should be sorted by the message
     * id.</p>
     */
    public static final Integer SORT_ID = new Integer(1);

    /**
     * <p>Indicates a list of messages should be sorted by the message
     * text.</p>
     */
    public static final Integer SORT_TEXT = new Integer(2);

    /**
     * <p>Indicates a list of messages should be sorted by the message
     * subject.</p>
     */
    public static final Integer SORT_SUBJECT = new Integer(3);

    /**
     * <p>Indicates a list of messages should be sorted by the message
     * recipients.</p>
     */
    public static final Integer SORT_RECIPIENTS = new Integer(4);

    /**
     * <p>Indicates a list of messages should be sorted by the "Carbon
     * Copy" recipients.</p>
     */
    public static final Integer SORT_RECIPIENTS_CC = new Integer(5);

    /**
     * <p>Indicates a list of messages should be sorted by the "Blind
     * Carbon Copy" recipients.</p>
     */
    public static final Integer SORT_RECIPIENTS_BCC = new Integer(6);

    /**
     * <p>Indicates a list of messages should be sorted by the message
     * sent date.</p>
     */
    public static final Integer SORT_SENT = new Integer(7);

    /**
     * <p>Indicates a list of messages should be sorted by the message
     * senders.</p>
     */
    public static final Integer SORT_SENDERS = new Integer(8);

    /**
     * <p>Indicates a list of messages should be sorted by the message
     * received date.</p>
     */
    public static final Integer SORT_RECEIVED = new Integer(9);

    /**
     * <p>Indicates a list of messages should be sorted by the message
     * size in bytes.</p>
     */
    public static final Integer SORT_SIZE = new Integer(10);
}
