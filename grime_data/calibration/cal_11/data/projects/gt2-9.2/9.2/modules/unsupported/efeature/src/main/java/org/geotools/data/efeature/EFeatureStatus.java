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
package org.geotools.data.efeature;

/**
 * Interface for {@link EFeature} status.
 * 
 * @author kengu, 22. apr. 2011
 * 
 *
 * @source $URL$
 */
public interface EFeatureStatus {
    public static final int SUCCESS = 0;

    public static final int WARNING = 2;

    public static final int FAILURE = 255;

    /**
     * Get status type
     */
    public int getType();

    /**
     * Get status message
     */
    public String getMessage();

    /**
     * Get {@link Throwable thowable} cause
     */
    public Throwable getCause();

    /**
     * Get {@link Throwable#getStackTrace() stack trace}
     */
    public StackTraceElement[] getStackTrace();
    
    /**
     * Get status source
     */
    public Object getSource();

    /**
     * Check if status is given type
     */
    public boolean isType(int type);

    /**
     * Check if status is {@link #SUCCESS}
     */
    public boolean isSuccess();

    /**
     * Check if status is {@link #WARNING}
     */
    public boolean isWarning();

    /**
     * Check if status is {@link #FAILURE}
     */
    public boolean isFailure();

    /**
     * Create new status instance with given message.
     * 
     * @param message - new message
     * @return a new {@link EFeatureStatus} instance
     */
    public EFeatureStatus clone(String message);
    
    /**
     * Create new status instance with given message.
     * 
     * @param message - new message
     * @param cause - (optional) {@link Throwable thowable} or 
     * {@link Throwable#getStackTrace() stack trace} cause
     * @return a new {@link EFeatureStatus} instance
     * @throws IllegalArgumentException If cause is not <code>null</code>,
     * and not a {@link Throwable} or {@link StackTraceElement} array. 
     */
    public EFeatureStatus clone(String message, Object cause);    

}
