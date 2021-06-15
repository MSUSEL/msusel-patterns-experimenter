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
package org.archive.crawler.framework.exceptions;

import org.archive.crawler.framework.exceptions.InitializationException;

/** ConfigurationExceptions should be thrown when a configuration file
 *   is missing data, or contains uninterpretable data, at runtime.  Fatal
 *   errors (that should cause the program to exit) should be thrown as
 *   FatalConfigurationExceptions.
 *
 *   You may optionally note the
 *
 * @author Parker Thompson
 *
 */
public class ConfigurationException extends InitializationException {

    private static final long serialVersionUID = -9078913414698851380L;

    // optionally store the file name and element so the catcher
    // can report the information and/or take other actions based on it
    protected String file = null;
    protected String element = null;

    /**
     * default constructor
     */
    public ConfigurationException() {
        super();
    }

    /** Create a ConfigurationException
     * @param message
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /** Create a ConfigurationException
     * @param cause
     */
    public ConfigurationException(Throwable cause) {
        super(cause);
    }

    /** Create ConfigurationException
     * @param message
     * @param filename
     * @param elementname
     */
    public ConfigurationException(String message, String filename, String elementname){
        super(message);
        file = filename;
        element = elementname;
    }

    /**  Create ConfigurationException
     * @param message
     * @param cause
     * @param filename
     * @param elementname
     */
    public ConfigurationException(String message, Throwable cause, String filename, String elementname){
        super(message, cause);
        file = filename;
        element = elementname;
    }

    /** Create ConfigurationException
     * @param cause
     * @param filename
     * @param elementname
     */
    public ConfigurationException(Throwable cause, String filename, String elementname){
        super(cause);
        file = filename;
        element = elementname;
    }

    /** Store the name of the configuration file that was being parsed
     *  when this exception occured.
     * @param name
     */
    public void setFile(String name){
        file = name;
    }

    /**
     * @return name of configuration file being parsed when this exception occurred
     */
    public String getFile(){
        return file;
    }

    /** Set the name of the element that was being parsed
     *   when this exception occured.
     * @param target
     */
    public void setElement(String target){
        element = target;
    }
    /**
     * @return name of the element being parsed when this exception occurred
     */
    public String getElement(){
        return element;
    }

}
