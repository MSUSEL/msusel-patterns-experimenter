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

import java.util.EventObject;

/**
 * An event that will be fired when a WebWindow changes.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 */
public final class WebWindowEvent extends EventObject {
    private static final long serialVersionUID = 6693838158619061745L;
    private final Page oldPage_;
    private final Page newPage_;
    private final int type_;

    /** A window has opened. */
    public static final int OPEN = 1;

    /** A window has closed. */
    public static final int CLOSE = 2;

    /** The content of the window has changed. */
    public static final int CHANGE = 3;

    /**
     * Creates an instance.
     *
     * @param webWindow the WebWindow that caused the event
     * @param type the type - one of {@link #OPEN}, {@link #CLOSE} or {@link #CHANGE}
     * @param oldPage the old contents of the web window
     * @param newPage the new contents of the web window
     */
    public WebWindowEvent(
            final WebWindow webWindow,
            final int type,
            final Page oldPage,
            final Page newPage) {
        super(webWindow);
        oldPage_ = oldPage;
        newPage_ = newPage;

        switch(type) {
            case OPEN:
            case CLOSE:
            case CHANGE:
                type_ = type;
                break;

            default:
                throw new IllegalArgumentException(
                    "type must be one of OPEN, CLOSE, CHANGE but got " + type);
        }
    }

    /**
     * Returns true if the two objects are equal.
     *
     * @param object the object to compare against
     * @return true if the two objects are equal
     */
    @Override
    public boolean equals(final Object object) {
        if (getClass() == object.getClass()) {
            final WebWindowEvent event = (WebWindowEvent) object;
            return isEqual(getSource(), event.getSource())
                && getEventType() == event.getEventType()
                && isEqual(getOldPage(), event.getOldPage())
                && isEqual(getNewPage(), event.getNewPage());
        }
        return false;
    }

    /**
     * Returns the hash code for this object.
     * @return the hash code for this object
     */
    @Override
    public int hashCode() {
        return source.hashCode();
    }

    /**
     * Returns the oldPage.
     * @return the page or null if the window has no page
     */
    public Page getOldPage() {
        return oldPage_;
    }

    /**
     * Returns the oldPage.
     * @return the page or null if the window has no page
     */
    public Page getNewPage() {
        return newPage_;
    }

    /**
     * Returns the web window that fired the event.
     * @return the web window that fired the event
     */
    public WebWindow getWebWindow() {
        return (WebWindow) getSource();
    }

    private boolean isEqual(final Object object1, final Object object2) {
        final boolean result;

        if (object1 == null && object2 == null) {
            result = true;
        }
        else if (object1 == null || object2 == null) {
            result = false;
        }
        else {
            result = object1.equals(object2);
        }

        return result;
    }

    /**
     * Returns a string representation of this event.
     * @return a string representation of this event
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder(80);
        buffer.append("WebWindowEvent(source=[");
        buffer.append(getSource());
        buffer.append("] type=[");
        switch(type_) {
            case OPEN:
                buffer.append("OPEN");
                break;
            case CLOSE:
                buffer.append("CLOSE");
                break;
            case CHANGE:
                buffer.append("CHANGE");
                break;
            default:
                buffer.append(type_);
                break;
        }
        buffer.append("] oldPage=[");
        buffer.append(getOldPage());
        buffer.append("] newPage=[");
        buffer.append(getNewPage());
        buffer.append("])");

        return buffer.toString();
    }

    /** @return the event type */
    public int getEventType() {
        return type_;
    }
}
