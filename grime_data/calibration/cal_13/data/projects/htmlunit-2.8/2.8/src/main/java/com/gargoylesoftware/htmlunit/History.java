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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Representation of the navigation history of a single window.
 *
 * @version $Revision: 5658 $
 * @author Daniel Gredler
 */
public class History implements Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = 2913698177338034112L;

    /** The window to which this navigation history belongs. */
    private final WebWindow window_;

    /**
     * The URLs of the pages in this navigation history; stored as Strings instead of java.net.URLs
     * because "about:blank" URLs don't serialize correctly.
     */
    private final List<String> urls_ = new ArrayList<String>();

    /**
     * Whether or not to ignore calls to {@link #addPage(Page)}; this is a bit hackish (we should probably be using
     * explicit boolean parameters in the various methods that load new pages), but it does the job for now -- without
     * any new API cruft.
     */
    private transient ThreadLocal<Boolean> ignoreNewPages_;

    /** The current index within the list of pages which make up this navigation history. */
    private int index_ = -1;

    /**
     * Creates a new navigation history for the specified window.
     * @param window the window which owns the new navigation history
     */
    public History(final WebWindow window) {
        window_ = window;
        initTransientFields();
    }

    /**
     * Initializes the transient fields.
     */
    private void initTransientFields() {
        ignoreNewPages_ = new ThreadLocal<Boolean>();
    }

    /**
     * Returns the length of the navigation history.
     * @return the length of the navigation history
     */
    public int getLength() {
        return urls_.size();
    }

    /**
     * Returns the current (zero-based) index within the navigation history.
     * @return the current (zero-based) index within the navigation history
     */
    public int getIndex() {
        return index_;
    }

    /**
     * Returns the URL at the specified index in the navigation history, or <tt>null</tt> if the index is not valid.
     * @param index the index of the URL to be returned
     * @return the URL at the specified index in the navigation history, or <tt>null</tt> if the index is not valid
     */
    public URL getUrl(final int index) {
        if (index >= 0 && index < urls_.size()) {
            return UrlUtils.toUrlSafe(urls_.get(index));
        }
        return null;
    }

    /**
     * Goes back one step in the navigation history, if possible.
     * @return this navigation history, after going back one step
     * @throws IOException if an IO error occurs
     */
    public History back() throws IOException {
        if (index_ > 0) {
            index_--;
            goToUrlAtCurrentIndex();
        }
        return this;
    }

    /**
     * Goes forward one step in the navigation history, if possible.
     * @return this navigation history, after going forward one step
     * @throws IOException if an IO error occurs
     */
    public History forward() throws IOException {
        if (index_ < urls_.size() - 1) {
            index_++;
            goToUrlAtCurrentIndex();
        }
        return this;
    }

    /**
     * Goes forward or backwards in the navigation history, according to whether the specified relative index
     * is positive or negative. If the specified index is <tt>0</tt>, this method reloads the current page.
     * @param relativeIndex the index to move to, relative to the current index
     * @return this navigation history, after going forwards or backwards the specified number of steps
     * @throws IOException if an IO error occurs
     */
    public History go(final int relativeIndex) throws IOException {
        final int i = index_ + relativeIndex;
        if (i < urls_.size() && i >= 0) {
            index_ = i;
        }
        goToUrlAtCurrentIndex();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return urls_.toString();
    }

    /**
     * Adds a new page to the navigation history.
     * @param page the page to add to the navigation history
     */
    protected void addPage(final Page page) {
        final Boolean ignoreNewPages = ignoreNewPages_.get();
        if (ignoreNewPages != null && ignoreNewPages) {
            return;
        }
        index_++;
        while (urls_.size() > index_) {
            urls_.remove(index_);
        }
        urls_.add(page.getWebResponse().getWebRequest().getUrl().toExternalForm());
    }

    /**
     * Loads the URL at the current index into the window to which this navigation history belongs.
     * @throws IOException if an IO error occurs
     */
    private void goToUrlAtCurrentIndex() throws IOException {
        final URL url = UrlUtils.toUrlSafe(urls_.get(index_));
        final WebRequest wrs = new WebRequest(url);
        final Boolean old = ignoreNewPages_.get();
        try {
            ignoreNewPages_.set(true);
            window_.getWebClient().getPage(window_, wrs);
        }
        finally {
            ignoreNewPages_.set(old);
        }
    }

    /**
     * Re-initializes transient fields when an object of this type is deserialized.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        initTransientFields();
    }

}
