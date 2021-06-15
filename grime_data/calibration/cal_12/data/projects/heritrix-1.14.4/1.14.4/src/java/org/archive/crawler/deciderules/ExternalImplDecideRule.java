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
package org.archive.crawler.deciderules;

import java.util.logging.Logger;

import javax.management.AttributeNotFoundException;

import org.archive.crawler.settings.SimpleType;

/**
 * A rule that can be configured to take alternate implementations
 * of the ExternalImplInterface.
 * If no implementation specified, or none found, returns
 * configured decision.
 * @author stack
 * @version $Date: 2007-02-18 21:53:01 +0000 (Sun, 18 Feb 2007) $, $Revision: 4914 $
 */
public class ExternalImplDecideRule
extends PredicatedDecideRule {

    private static final long serialVersionUID = 7727715263469524372L;

    private static final Logger LOGGER =
        Logger.getLogger(ExternalImplDecideRule.class.getName());
    static final String ATTR_IMPLEMENTATION = "implementation-class";
    private ExternalImplInterface implementation = null;

    /**
     * @param name Name of this rule.
     */
    public ExternalImplDecideRule(String name) {
        super(name);
        setDescription("ExternalImplDecideRule. Rule that " +
            "instantiates implementations of the ExternalImplInterface. " +
            "The implementation needs to be present on the classpath. " +
            "On initialization, the implementation is instantiated (" +
            "assumption is that there is public default constructor).");
        addElementToDefinition(new SimpleType(ATTR_IMPLEMENTATION,
            "Name of implementation of ExternalImplInterface class to " +
            "instantiate.", ""));
    }
    
    protected boolean evaluate(Object obj) {
        ExternalImplInterface impl = getConfiguredImplementation(obj);
        return (impl != null)? impl.evaluate(obj): false;
    }
    
    /** 
     * Get implementation, if one specified.
     * If none specified, will keep trying to find one. Will be messy
     * if the provided class is not-instantiable or not implementation
     * of ExternalImplInterface.
     * @param o A context object.
     * @return Instance of <code>ExternalImplInterface</code> or null.
     */
    protected synchronized ExternalImplInterface
            getConfiguredImplementation(Object o) {
        if (this.implementation != null) {
            return this.implementation;
        }
        ExternalImplInterface result = null;
        try {
            String className =
                (String)getAttribute(o, ATTR_IMPLEMENTATION);
            if (className != null && className.length() != 0) {
                Object obj = Class.forName(className).newInstance();
                if (!(obj instanceof ExternalImplInterface)) {
                    LOGGER.severe("Implementation " + className + 
                        " does not implement ExternalImplInterface");
                }
                result = (ExternalImplInterface)obj;
                this.implementation = result;
            }
        } catch (AttributeNotFoundException e) {
            LOGGER.severe(e.getMessage());
        } catch (InstantiationException e) {
            LOGGER.severe(e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.severe(e.getMessage());
        } catch (ClassNotFoundException e) {
            LOGGER.severe(e.getMessage());
        }
        return result;
    }
}