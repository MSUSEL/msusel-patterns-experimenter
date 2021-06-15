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
package org.archive.crawler.settings;

import java.util.List;

import javax.management.InvalidAttributeValueException;

/**
 * Superclass of all modules that should be configurable.
 *
 * @author John Erik Halse
 */
public class ModuleType extends ComplexType {

    private static final long serialVersionUID = 3686678928531236811L;

    /** Creates a new ModuleType.
     *
     * This constructor is made to help implementors of subclasses. It is an
     * requirement that subclasses at the very least implements a constructor
     * that takes only the name as an argument.
     *
     * @param name the name of the module.
     * @param description the description of the module.
     */
    public ModuleType(String name, String description) {
        super(name, description);
    }

    /** Every subclass should implement this constructor
     *
     * @param name of the module
     */
    public ModuleType(String name) {
        super(name, name);
    }

    public Type addElement(CrawlerSettings settings, Type type)
            throws InvalidAttributeValueException {
        if (isInitialized()) {
            throw new IllegalStateException(
                    "Not allowed to add elements to modules after"
                            + " initialization. (Module: " + getName()
                            + ", Element: " + type.getName() + ", Settings: "
                            + settings.getName() + " (" + settings.getScope()
                            + ")");
        }
        return super.addElement(settings, type);
    }

    /**
     * Those Modules that use files on disk should list them all when this
     * method is called.
     *
     * <p>Each file (as a string name with full path) should be added to the
     * provided list.
     *
     * <p>Modules that do not use any files can safely ignore this method.
     *
     * @param list The list to add files to.
     */
    protected void listUsedFiles(List<String> list){
        // By default do nothing
    }
}