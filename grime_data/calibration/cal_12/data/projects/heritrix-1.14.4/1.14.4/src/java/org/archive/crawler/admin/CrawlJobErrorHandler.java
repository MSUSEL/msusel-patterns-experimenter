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
package org.archive.crawler.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.archive.crawler.settings.ValueErrorHandler;
import org.archive.crawler.settings.Constraint.FailedCheck;


/**
 * An implementation of the ValueErrorHandler for the UI.
 *
 * <p>The UI uses this class to trap errors in the settings of it's jobs and
 * profiles and manage their presentation to the user.
 *
 * @author Kristinn Sigurdsson
 *
 * @see org.archive.crawler.settings.ValueErrorHandler
 */
public class CrawlJobErrorHandler implements ValueErrorHandler {
    /** All encountered errors */
    HashMap<String,FailedCheck> errors = null;
    Level level = Level.INFO;
    Level highestEncounteredLevel = Level.OFF;

    public CrawlJobErrorHandler(){
        errors = new HashMap<String,FailedCheck>();
    }

    public CrawlJobErrorHandler(Level level){
        this();
        this.level = level;
    }

    public void handleValueError(FailedCheck error) {
        String key = error.getOwner().getAbsoluteName() +
            "/" + error.getDefinition().getName();
        errors.put(key,error);
        if(error.getLevel().intValue()>highestEncounteredLevel.intValue()){
            highestEncounteredLevel = error.getLevel();
        }
    }

    /**
     * Get error for a specific attribute.
     *
     * <p>Uses currently set error level
     *
     * @param absoluteName The absolute name of the attribute
     * @return error for a specific attribute at or above current error
     *           level. null if no matching error is found.
     */
    public FailedCheck getError(String absoluteName){
        return getError(absoluteName,level);
    }

    /**
     * Get error for a specific attribute
     * 
     * @param absoluteName
     *            The absolute name of the attribute.
     * @param level
     *            Limit errors to those at this or higher level.
     * @return error for a specific attribute at or above specified error level.
     *         null if no matching error is found.
     */
    public FailedCheck getError(String absoluteName, Level level) {
        FailedCheck fc = (FailedCheck) errors.get(absoluteName);
        if (fc != null && fc.getLevel().intValue() >= level.intValue()) {
            return fc;
        }
        return null;
    }

    /**
     * Has there been an error with severity (level) equal to or higher then
     * this handlers set level.
     * @return has there ben an error.
     */
    public boolean hasError(){
        return hasError(level);
    }

    /**
     * Has there been an error with severity (level) equal to or higher then
     * specified.
     * @param level The severity.
     * @return has there ben an error.
     */
    public boolean hasError(Level level){
        return highestEncounteredLevel.intValue() >= level.intValue();
    }

    /**
     * @return Returns the level.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * @param level The level to set.
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * Reset handler.
     *
     * <p>Delets all encountered errors of any level.
     */
    public void clearErrors(){
        errors = new HashMap<String,FailedCheck>();
    }

    /**
     * Get an List of all the encountered errors.
     *
     * <p>The List contains a set of
     * {@link org.archive.crawler.settings.Constraint.FailedCheck
     * FailedCheck} objects.
     *
     * @return an list of all encountered errors (with level equal to
     *         or higher then current level).
     *
     * @see org.archive.crawler.settings.Constraint.FailedCheck
     */
    public List getErrors(){
        return getErrors(level);
    }

    /**
     * Get an List of all the encountered errors.
     *
     * <p>The List contains a set of
     * {@link org.archive.crawler.settings.Constraint.FailedCheck
     * FailedCheck} objects.
     *
     * @param level Get all errors of this level or higher
     *
     * @return an list of all encountered errors (with level equal to
     *         or higher then specified level).
     *
     * @see org.archive.crawler.settings.Constraint.FailedCheck
     */
    public List getErrors(Level level){
        ArrayList<FailedCheck> list = new ArrayList<FailedCheck>(errors.size());
        for (FailedCheck fc: errors.values()) {
            if(fc.getLevel().intValue() >= level.intValue()){
                list.add(fc);
            }
        }
        return list;
    }
}
