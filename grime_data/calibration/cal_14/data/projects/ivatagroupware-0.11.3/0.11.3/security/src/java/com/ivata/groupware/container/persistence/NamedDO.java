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
package com.ivata.groupware.container.persistence;

/**
 * <p>
 * Any data object which has a unique name for display in lists or choices
 * should extend this class.
 * </p>
 *
 * @since 2004-06-06
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public abstract class NamedDO extends TimestampDO implements Comparable {

    /**
     * <p>Comparison method. See if the object supplied is a person data
     * object and, if so, whether or not its contents are greater than, the same
     * or less than this one.</p>
     *
     * <p>This method sorts by the name first then the id.</p>
     *
     * @param compare the object to compare with this one.
     * @return a positive number if the object supplied in <code>compare</code>
     *     is greater than this one, <code>0</code> if they are the same,
     *     otherwise a negative number.
     */
    public int compareTo(final Object compare) {
        // first check it is non-null and the class is right
        if ((compare == null) ||
            !(this.getClass().isInstance(compare))) {
            return 1;
        }
        NamedDO namedDO = (NamedDO) compare;

        // see if the ids are the same
        if ((getId() != null)
                && (namedDO.getId() != null)) {
            return getId().compareTo(namedDO.getId());
        }

        // watch for null names
        String thisName = getName();
        String otherName = namedDO.getName();
        if (thisName == null) {
            return otherName == null ? 0 : 1;
        }

        // otherwise, compare the names
        return thisName.compareTo(otherName);
    }

    /**
     * <p>Comparison method. See if the two objects are equivalent.</p>
     *
     * @param compare the object to compare with this one.
     * @return <code>true</code> if the objects are the same.
     */
    public boolean equals(final Object compare) {
        return compareTo(compare) == 0;
    }
    /**
     * Just returns the name.
     * Refer to {@link #getName}.
     * @return Refer to {@link #getName}.
     */
    public String getDisplayValue() {
        return getName();
    }

    /**
     * <p>
     * You must override this method to output the usual name for this data
     * object.
     * </p>
     *
     * @return name of this data object, used to clearly identifiy it if no id
     * has been set yet.
     */
    public abstract String getName();

    /**
     * <p>
     * Provide helpful debugging info about this data object.
     * </p>
     *
     * @return clear text describing this object.
     */
    public String toString() {
        return super.toString()
            + " "
            + getName();
    }
}
