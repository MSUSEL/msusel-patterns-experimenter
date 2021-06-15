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
package com.ivata.groupware.business.library.topic;

import com.ivata.groupware.container.persistence.BaseDO;


/**
 * <p>Represents a topic which can be used for a library item. Each library item
 * in ivata groupware must have a topic associated with it, and this is important
 * as access to each item is granted or denied on the basis of the topic
 * associated with it.</p>
 *
 * @since   2002-11-26
 * @author  jano
 * @version $Revision: 1.4 $
 *
 * @hibernate.class
 *      table="library_topic"
 */
public class TopicDO extends BaseDO implements Comparable {

    /**
     * <p>Briefly describes the topic in one line.</p>
     */
    private String caption;

    /**
     * <p>Name of image file.</p>
     */
    private String image;
    /**
     * <p>Comparison method. See if the object supplied is a topic dependent
     * object and, if so, whether or not its contents are greater than, the same
     * or less than this one.</p>
     *
     * <p>This method sorts by the caption first then the id.</p>
     *
     * @param compare the object to compare with this one.
     * @return a positive number if the object supplied in <code>compare</code>
     *     is greater than this one, <code>0</code> if they are the same,
     *     otherwise a negative number.
     */
    public int compareTo(final Object compare) {
        // first check it is non-null and the class is right
        if ((compare == null) || !(this.getClass().isInstance(compare))) {
            return 1;
        }

        TopicDO topicDO = (TopicDO) compare;
        Integer id = getId();
        Integer topicId = topicDO.getId();

        // see the ids are the same
        if (((id == null) ? (topicId == null) : id.equals(topicId))) {
            return 0;
        }

        // see if the captions are equal - if so, use the id
        if (((caption == null) ? (topicDO.caption == null)
                                   : caption.equals(topicDO.caption))) {
            // if the id is null and the other id is not null, return +ve
            if (id == null) {
                return 1;
            }

            return 0;
        }

        // if the name is null and the other name is not null, return +ve
        if (caption == null) {
            return 1;
        }

        // otherwise, compare the names
        return caption.compareTo(topicDO.caption);
    }

    /**
     * <p>Briefly describes the topic in one line.</p>
     *
     * @return current value of caption.
     * @hibernate.property
     */
    public final String getCaption() {
        return caption;
    }
    /**
     * <p>Name of image file.</p>
     *
     * @return image file name.
     * @hibernate.property
     */
    public String getImage() {
        return image;
    }

    /**
     * <p>Briefly describes the topic in one line.</p>
     *
     * @param caption new value of caption.
     */
    public final void setCaption(final String caption) {
        this.caption = caption;
    }

    /**
     * <p>Name of image file.</p>
     *
     * @param image new image file name.
     */
    public final void setImage(final String image) {
        this.image = image;
    }
}
