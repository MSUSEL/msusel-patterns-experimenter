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
package org.apache.james.transport;
import javax.mail.MessagingException;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.james.core.MatcherConfigImpl;
import org.apache.mailet.MailetContext;
import org.apache.mailet.MailetException;
import org.apache.mailet.Matcher;
/**
 * Loads Matchers for use inside James.
 *
 */
public class MatchLoader extends Loader implements Configurable {
        /**
     * @see org.apache.avalon.framework.configuration.Configurable#configure(Configuration)
     */
    public void configure(Configuration conf) throws ConfigurationException {
           getPackages(conf,MATCHER_PACKAGE);
           configureMailetClassLoader();
    }


    /**
     * Get a new Matcher with the specified name acting
     * in the specified context.
     *
     * @param matchName the name of the matcher to be loaded
     * @param context the MailetContext to be passed to the new
     *                matcher
     * @throws MessagingException if an error occurs
     */
    public Matcher getMatcher(String matchName, MailetContext context) throws MessagingException {
        try {
            String condition = (String) null;
            int i = matchName.indexOf('=');
            if (i != -1) {
                condition = matchName.substring(i + 1);
                matchName = matchName.substring(0, i);
            }
            for (i = 0; i < packages.size(); i++) {
                String className = (String) packages.elementAt(i) + matchName;
                try {
                    MatcherConfigImpl configImpl = new MatcherConfigImpl();
                    configImpl.setMatcherName(matchName);
                    configImpl.setCondition(condition);
                    configImpl.setMailetContext(context);
                    Matcher matcher = (Matcher) mailetClassLoader.loadClass(className).newInstance();
                    matcher.init(configImpl);
                    return matcher;
                } catch (ClassNotFoundException cnfe) {
                    //do this so we loop through all the packages
                }
            }
            StringBuffer exceptionBuffer =
                new StringBuffer(128)
                    .append("Requested matcher not found: ")
                    .append(matchName)
                    .append(".  looked in ")
                    .append(packages.toString());
            throw new ClassNotFoundException(exceptionBuffer.toString());
        } catch (MessagingException me) {
            throw me;
        } catch (Exception e) {
            StringBuffer exceptionBuffer =
                new StringBuffer(128).append("Could not load matcher (").append(matchName).append(
                    ")");
            throw new MailetException(exceptionBuffer.toString(), e);
        }
    }
}
