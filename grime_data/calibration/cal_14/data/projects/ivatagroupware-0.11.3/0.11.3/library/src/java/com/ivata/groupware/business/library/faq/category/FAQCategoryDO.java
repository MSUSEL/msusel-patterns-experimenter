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
package com.ivata.groupware.business.library.faq.category;

import java.util.List;

import com.ivata.groupware.business.library.item.LibraryItemDO;
import com.ivata.groupware.container.persistence.BaseDO;


/**
 * <p>Contains one category of questions and answers in a frequently asked
 * questions library item.</p>
 *
 * @since   2002-06-28
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 *
 * @hibernate.class
 *      table="library_faq_category"
 */
public class FAQCategoryDO extends BaseDO {

    /**
     * <p>Text describing the function of this category. This is
     * optional.</p>
     */
    private String description;

    /**
     * <p>All of the questions/answers in this category.</p>
     */
    private List fAQs;

    /**
     * <p>Store the library Item, the owner of this category.</p>
     */
    private LibraryItemDO libraryItem;
    /**
     * <p>The name for this category. This should be a clear-text name
     * which can contain punctuation characters and spaces. This name should
     * uniquely identify the category within the library item it is in.</p>
     */
    private String name;

    /**
     * <p>Comparison method. See if the object supplied is a faq category
     * dependent object and, if so, whether or not its contents are the same as
     * this one. Only the <code>id</code> fields are compared.</p>
     *
     * @param compare the object to compare with this one.
     * @return <code>true</code> if the object supplied in <code>compare</code>
     *     is effectively the same as this one, otherwise false.
     */
    public boolean equals(final Object compare) {
        // first check it is non-null and the class is right
        if ((compare == null) || !(this.getClass().isInstance(compare))) {
            return false;
        }

        FAQCategoryDO categoryDO = (FAQCategoryDO) compare;
        Integer id = getId();
        Integer categoryId = categoryDO.getId();

        // check that the ids are the same
        return (((id == null)
                ? (categoryId == null)
                : id.equals(categoryId)));
    }

    /**
     * <p>Get the text describing the function of this category. This is
     * optional and may be set to null.</p>
     *
     * @return optional text describing the function and purpose of the
     *     category.
     * @hibernate.property
     */
    public final String getDescription() {
        return description;
    }

    /**
     * <p>Get all of the questions and answers in this category as a
     * <code>List</code> of <code>FAQDO</code> instances.</p>
     *
     * @return all of the questions and answers in this category as a
     * <code>List</code> of <code>FAQDO</code> instances.
     *
     * @hibernate.bag
     *      cascade="all"
     * @hibernate.collection-key
     *      column="library_faq_category"
     * @hibernate.collection-one-to-many
     *      class="com.ivata.groupware.business.library.faq.FAQDO"
     */
    public List getFAQs() {
        return fAQs;
    }
    /**
     * <p>Get the library item.</p>
     *
     * @return the library item.
     * @hibernate.many-to-one
     *      column="library_item"
     */
    public LibraryItemDO getLibraryItem() {
        return libraryItem;
    }

    /**
     * <p>Get this name for this category. This should be a clear-text name
     * which can contain punctuation characters and spaces. This name should
     * uniquely identify the category within the library item it is in.</p>
     *
     * @return clear-text name which identifies this category within the
     *     library item.
     * @hibernate.property
     */
    public final String getName() {
        return name;
    }

    /**
     * <p>Set the text describing the function of this category. This is
     * optional.</p>
     *
     * @param description optional text describing the function and purpose of
     *     the category.
     */
    public final void setDescription(final String description) {
        this.description = description;
    }

    /**
     * <p>Set all of the questions and answers in this category as a
     * <code>List</code> of <code>FAQDO</code> instances.</p>
     *
     * @param faqs all of the questions and answers in this category as a
     * <code>List</code> of <code>FAQDO</code> instances.
     */
    public final void setFAQs(final List faqs) {
        this.fAQs = faqs;
    }

    /**
     * <p>Set the library item for this category.</p>
     *
     * @param libraryItem new value of library item.
     */
    public final void setLibraryItem(final LibraryItemDO libraryItem) {
        this.libraryItem = libraryItem;
    }

    /**
     * <p>Set this name for this category. This should be a clear-text name
     * which can contain punctuation characters and spaces. This name should
     * uniquely identify the category within the library item it is in.</p>
     *
     * @param name clear-text name which identifies this category within the
     *     library item.
     */
    public final void setName(final String name) {
        this.name = name;
    }
}
