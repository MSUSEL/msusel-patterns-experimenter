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
package com.ivata.groupware.business.library.item;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.ivata.groupware.business.drive.file.FileContentDO;
import com.ivata.groupware.business.library.topic.TopicDO;
import com.ivata.groupware.container.persistence.TimestampDO;

/**
 * <p>Represents a single item within the library. The item can be one of six
 * types:<br/>
 * <ul>
 *   <li>document</li>
 *   <li>Faq (Frequently Asked Question)</li>
 *   <li>memo</li>
 *   <li>note</li>
 *   <li>news item</li>
 *   <li>meeting agenda/minutes</li>
 * </ul></p>
 *
 * <p>This is a dependent value class, used to pass data back from the.</p>
 * {@link LibraryItemBean LibraryItemBean} to a client application.</p>
 *
 * <p><strong>Note:</strong> This class provides data from {@link LibraryItemBean LibraryItemBean}.
 * This is no local copy of the bean class, however, and changes here
 * will not be automatically reflected in {@link LibraryItemBean LibraryItemBean}.</p>
 *
 * @since   2002-06-14
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 * @see LibraryItemBean
 *
 * @hibernate.class
 *      table="library_item"
 */
public class LibraryItemDO extends TimestampDO {

    /**
     * <p>This method converts an XML file to a LibraryItemDO.</p>
     *
     * @param fileContent contains the XML to be converted
     * @return valid library item.
     */
    public static LibraryItemDO convertFromFile(FileContentDO fileContent) {
        XMLDecoder decoder = new XMLDecoder(fileContent.getContent().getInputStream());
        LibraryItemDO itemDO = (LibraryItemDO) decoder.readObject();

        decoder.close();
        return itemDO;
    }

    /**
     * <p>If the item is frequently asked questions, store all the categoreies
     * of questions here.</p>
     */
    private List fAQCategories;

    /**
     * <p>Store the directory to store images in for this library item.</p>
     */
    private String imageDirectory;

    /**
     * <p>Store all the pages of this item, if it has pages.</p>
     */
    private List pages;

    /**
     * <p>Store the summary text. This summary will appear on the noticeboard page,
     * giving an overview of the content of the item.</p>
     */
    private String summary;


    /**
     * <p>Store the title for this item. Is usually used in the titlebar of the
     * window which displays the item.</p>
     */
    private String title;

    /**
     * <p>Topics are used to group items together and apply rights.</p>
     */
    private TopicDO topic;
    /**
     * <p>Store the type of the item, to one of the values found in {@link LibraryItemConstants
     * LibraryItemConstants}.</p>
     */
    private Integer type;

    /**
     * <p>If the item is frequently asked questions, set all the categoreies
     * of questions here.</p>
     *
     * @return fAQCategories <code>List</code> of <code>FAQCategoryDO</code>
     * instances.
     *
     * @hibernate.bag
     *      cascade="all"
     * @hibernate.collection-key
     *      column="library_item"
     * @hibernate.collection-one-to-many
     *      class="com.ivata.groupware.business.library.faq.category.FAQCategoryDO"
     */
    public List getFAQCategories() {
        return fAQCategories;
    }
    /**
     * <p>Get the directory to store images in for this library item.</p>
     *
     * @return the directory to store images in for this library item.
     * @hibernate.property
     *      column="image_directory"
     */
    public final String getImageDirectory() {
        return imageDirectory;
    }

    /**
     * <p>Set all the pages of this item, as a <code>List</code> of
     * <code>PageDO</code> instances.</p>
     *
     * <p>
     * <strong>Note:</strong> not all types have pages. Use this method only if this
     * library item represents a document, news item or meeting minutes.</p>
     *
     * @return page texts as a set of DO instances.
     *
     * @hibernate.bag
     *      cascade="all"
     * @hibernate.collection-key
     *      column="library_item"
     * @hibernate.collection-one-to-many
     *      class="com.ivata.groupware.business.library.page.PageDO"
     */
    public List getPages() {
        return pages;
    }

    /**
     * <p>Set the summary text. This summary will appear on the noticeboard page,
     * giving an overview of the content of the item.</p>
     *
     * @return the summary text.
     * @hibernate.property
     */
    public final String getSummary() {
        return summary;
    }

    /**
     * <p>Set the title for this item. Is usually used in the titlebar of the
     * window which displays the item.</p>
     *
     * @return the title for this item. Is usually used in the titlebar of the
     * window which displays the item.
     * @hibernate.property
     */
    public final String getTitle() {
        return title;
    }

    /**
     * <p>Topics are used to group items together and apply rights.</p>
     *
     * @return current value of topic.
     * @hibernate.many-to-one
     *      column="library_topic"
     */
    public final TopicDO getTopic() {
        return topic;
    }

    /**
     * <p>Get the type of the item, as one of the values found in {@link LibraryItemConstants
     * LibraryItemConstants}.</p>
     * @hibernate.property
     */
    public final Integer getType() {
        return type;
    }

    /**
     * <p>This method will convert a library item to a to a file.
     * In this case it will be an XML file.</p>
     *
     * @return name of the file this library item was saved to.
     */
    public String saveToFile()
            throws IOException {
        String returnFileName = "";

        // create temporary file
        File tmpFile = File.createTempFile("drive", "xml");

        returnFileName = tmpFile.getPath();
        // write itemDO to file
        FileOutputStream file = new FileOutputStream(returnFileName);
        XMLEncoder encoder = new XMLEncoder(file);

        encoder.writeObject(this);
        encoder.close();
        file.close();

        return returnFileName;
    }

    /**
     * <p>If the item is frequently asked questions, set all the categoreies
     * of questions here.</p>
     *
     * @param fAQCategories <code>List</code> of <code>FAQCategoryDO</code>
     * instances.
     */
    public final void setFAQCategories(final List fAQCategories) {
        this.fAQCategories = fAQCategories;
    }

    /**
     * <p>Set the directory to store images in for this library item.</p>
     *
     * @param imageDirectory new value of image directory.
     */
    public final void setImageDirectory(final String imageDirectory) {
        this.imageDirectory = imageDirectory;
    }

    /**
     * <p>Set all the pages of this item, as a <code>List</code> of
     * <code>PageDO</code> instances.</p>
     *
     * <p>
     * <strong>Note:</strong> not all types have pages. Use this method only if this
     * library item represents a document, news item or meeting minutes.</p>
     *
     * @param pages new value of page texts as a set of DO instances.
     */
    public final void setPages(final List pages) {
        this.pages = pages;
    }

    /**
     * <p>Set the summary text. This summary will appear on the noticeboard page,
     * giving an overview of the content of the item.</p>
     *
     * @param summary summary of the item's contents.
     */
    public final void setSummary(final String summary) {
        this.summary = summary;
    }


    /**
     * <p>Set the title for this item. Is usually used in the titlebar of the
     * window which displays the item.</p>
     *
     * @param title new value of title.
     */
    public final void setTitle(final String title) {
        this.title = title;
    }

    /**
     * <p>Topics are used to group items together and apply rights.</p>
     *
     * @param topic new value of topic.
     */
    public final void setTopic(final TopicDO topic) {
        this.topic = topic;
    }

    /**
     * <p>Set the type of the item, to one of the values found in {@link LibraryItemConstants
     * LibraryItemConstants}.</p>
     */
    public final void setType(final Integer type) {
        this.type = type;
    }

}
