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
package org.apache.mailet;

import javax.mail.MessagingException;
import java.util.Collection;

/**
 * <p>GenericMatcher implements the Matcher and MatcherConfig interfaces.</p>
 * <p>GenericMatcher makes writing matchers easier. It provides simple versions of
 * the lifecycle methods init and destroy and of the methods in the MatcherConfig
 * interface. GenericMatcher also implements the log method, declared in the
 * MatcherContext interface.</p>
 * 
 * <p>To write a generic matcher, you need only override the abstract match method.</p>
 *
 * @version 1.0.0, 24/04/1999
 */
public abstract class GenericMatcher implements Matcher, MatcherConfig {
    MatcherConfig config = null;

    /**
     * Called by the mailet container to indicate to a matcher that the
     * matcher is being taken out of service.
     */
    public void destroy() {
        //Do nothing
    }

    /**
     * <p>Returns a String containing the value of the named initialization
     * parameter, or null if the parameter does not exist.</p>
     * 
     * <p>This method is supplied for convenience. It gets the value of the
     * named parameter from the matcher's MatcherConfig object.</p>
     *
     * @return String a String containing the value of the initalization parameter
     */
    public String getCondition() {
        return config.getCondition();
    }

    /**
     * Returns this matcher's MatcherConfig object.
     *
     * @return MatcherConfig the MatcherConfig object that initialized this matcher
     */
    public MatcherConfig getMatcherConfig() {
        return config;
    }

    /**
     * Returns a reference to the MailetContext in which this matcher is
     * running.
     *
     * @return MailetContext the MailetContext object passed to this matcher by the init method
     */
    public MailetContext getMailetContext() {
        return getMatcherConfig().getMailetContext();
    }

    /**
     * Returns information about the matcher, such as author, version, and
     * copyright.  By default, this method returns an empty string. Override
     * this method to have it return a meaningful value.
     *
     * @return String information about this matcher, by default an empty string
     */
    public String getMatcherInfo() {
        return "";
    }

    /**
     * Returns the name of this matcher instance.
     *
     * @return the name of this matcher instance
     */
    public String getMatcherName() {
        return config.getMatcherName();
    }


    /**
     * <p>Called by the matcher container to indicate to a matcher that the
     * matcher is being placed into service.</p>
     *
     * <p>This implementation stores the MatcherConfig object it receives from
     * the matcher container for alter use. When overriding this form of the
     * method, call super.init(config).</p>
     *
     * @param MatcherConfig config - the MatcherConfig object that contains
     *          configutation information for this matcher
     * @throws MessagingException
     *          if an exception occurs that interrupts the matcher's normal operation
     */
    public void init(MatcherConfig newConfig) throws MessagingException {
        config = newConfig;
        init();
    }

    /**
     * <p>A convenience method which can be overridden so that there's no
     * need to call super.init(config).</p>
     *
     * <p>Instead of overriding init(MatcherConfig), simply override this
     * method and it will be called by GenericMatcher.init(MatcherConfig config).
     * The MatcherConfig object can still be retrieved via getMatcherConfig().</p>
     *
     * @throws MatcherException
     *          if an exception occurs that interrupts the matcher's normal operation
     */
    public void init() throws MessagingException {
        //Do nothing... can be overridden
    }

    /**
     * Writes the specified message to a matcher log file, prepended by
     * the matcher's name.
     *
     * @param msg - a String specifying the message to be written to the log file
     */
    public void log(String message) {
        StringBuffer logBuffer = 
            new StringBuffer(256)
                    .append(getMatcherName())
                    .append(": ")
                    .append(message);
        getMailetContext().log(logBuffer.toString());
    }

    /**
     * Writes an explanatory message and a stack trace for a given Throwable
     * exception to the matcher log file, prepended by the matcher's name.
     *
     * @param message - a String that describes the error or exception
     * @param t - the java.lang.Throwable error or exception
     */
    public void log(String message, Throwable t) {
        StringBuffer logBuffer = 
            new StringBuffer(256)
                    .append(getMatcherName())
                    .append(": ")
                    .append(message);
        getMailetContext().log(logBuffer.toString(), t);
    }

    /**
     * <p>Called by the matcher container to allow the matcher to process a
     * message.</p>
     *
     * <p>This method is declared abstract so subclasses must override it.</p>
     *
     * @param mail - the Mail object that contains the MimeMessage and
     *          routing information
     * @return java.util.Collection - the recipients that the mailet container should have the
     *          mailet affect.
     * @throws javax.mail.MessagingException - if an exception occurs that interferes with the mailet's normal operation
     *          occurred
     */
    public abstract Collection match(Mail mail) throws MessagingException;
}
