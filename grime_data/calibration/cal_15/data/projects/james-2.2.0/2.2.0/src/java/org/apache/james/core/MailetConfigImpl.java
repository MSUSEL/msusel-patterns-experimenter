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
package org.apache.james.core;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.mailet.MailetConfig;
import org.apache.mailet.MailetContext;

import java.util.Iterator;

/**
 * Implements the configuration object for a Mailet.
 *
 * @version CVS $Revision: 1.3.4.6 $ $Date: 2004/03/15 03:54:15 $
 */
public class MailetConfigImpl implements MailetConfig {

    /**
     * The mailet MailetContext
     */
    private MailetContext mailetContext;

    /**
     * The mailet name
     */
    private String name;

    //This would probably be better.
    //Properties params = new Properties();
    //Instead, we're tied to the Configuration object
    /**
     * The mailet Avalon Configuration
     */
    private Configuration configuration;

    /**
     * No argument constructor for this object.
     */
    public MailetConfigImpl() {
    }

    /**
     * Get the value of an parameter stored in this MailetConfig.  Multi-valued
     * parameters are returned as a comma-delineated string.
     *
     * @param name the name of the parameter whose value is to be retrieved.
     *
     * @return the parameter value
     */
    public String getInitParameter(String name) {
        try {
            String result = null;

            final Configuration[] values = configuration.getChildren( name );
            for ( int i = 0; i < values.length; i++ ) {
                if (result == null) {
                    result = "";
                } else {
                    result += ",";
                }
                Configuration conf = values[i];
                result += conf.getValue();
            }
            return result;
        } catch (ConfigurationException ce) {
            throw new RuntimeException("Embedded configuration exception was: " + ce.getMessage());
        }

    }

    /**
     * Returns an iterator over the set of configuration parameter names.
     *
     * @return an iterator over the set of configuration parameter names.
     */
    public Iterator getInitParameterNames() {
        return new Iterator () {
            Configuration[] children;
            int count = 0;
            {
                children = configuration.getChildren();
            }

            public boolean hasNext() {
                return count < children.length;
            }

            public Object next() {
                return children[count++].getName();
            }

            public void remove() {
                throw new UnsupportedOperationException ("remove not supported");
            }
        };
    }

    /**
     * Get the value of an (XML) attribute stored in this MailetConfig.
     *
     * @param name the name of the attribute whose value is to be retrieved.
     *
     * @return the attribute value or null if missing
     */
    public String getInitAttribute(String name) {
        return configuration.getAttribute(name, null);
    }

    /**
     * Get the mailet's MailetContext object.
     *
     * @return the MailetContext for the mailet
     */
    public MailetContext getMailetContext() {
        return mailetContext;
    }

    /**
     * Get the mailet's Avalon Configuration object.
     *
     * @return the Configuration for the mailet
     */
    public void setMailetContext(MailetContext newContext) {
        mailetContext = newContext;
    }

    /**
     * Set the Avalon Configuration object for the mailet.
     *
     * @param newConfiguration the new Configuration for the mailet
     */
    public void setConfiguration(Configuration newConfiguration) {
        configuration = newConfiguration;
    }

    /**
     * Get the name of the mailet.
     *
     * @return the name of the mailet
     */
    public String getMailetName() {
        return name;
    }

    /**
     * Set the name for the mailet.
     *
     * @param newName the new name for the mailet
     */
    public void setMailetName(String newName) {
        name = newName;
    }
}
