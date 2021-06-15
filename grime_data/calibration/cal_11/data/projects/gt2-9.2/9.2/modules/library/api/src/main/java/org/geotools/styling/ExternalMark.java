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
package org.geotools.styling;

import javax.swing.Icon;

import org.opengis.metadata.citation.OnLineResource;

/**
 * Specify a mark using an image files (svg, png, gif) or using mark index a true type font file.
 * <p>
 * Please note that not all render can handle all image file formats; please organize your marks
 * into a preferred order with the most specific (say SVG) followed by common formats (PNG, GIF) and ending
 * with an appropriate WellKnownName.
 *
 *
 *
 * @source $URL$
 */
public interface ExternalMark extends org.opengis.style.ExternalMark {

    /**
     * Online resource defined by an URI.
     * <p>
     * Only one of OnlineResource or InlineContent can be supplied.
     *
     * @return OnlineResource or <code>null</code>
     */
    OnLineResource getOnlineResource();
    
    /**
     * @param resource Online resource with format defined by getFormat()
     */
    void setOnlineResource( OnLineResource resource );
    
    /**
     * Inline content.
     *
     * Only one of OnlineResource or InlineContent can be supplied.
     *
     * @return InlineContent or <code>null</code>
     */
    Icon getInlineContent();

    /**
     * Icon to use for inline content.
     * <p>
     * This is often a SwingImageIcon with a format defined by getFormat()
     * 
     * @param inline
     */
    void setInlineContent(Icon inline);

    /**
     * @deprecated use {@link #setInlineContent(Icon)}
     */
    void getInlineContent(Icon inline);
    
    /**
     * Mime type of the onlineResource/InlineContent
     * <p>
     * Common examples:
     * <ul>
     * <li>image/svg</li>
     * <li>image/png</li>
     * <li>image/gif</li>
     * </ul>
     * This information is used by a renderer to determine if it can support the
     * image format being supplied.
     * 
     * @return mime type
     */
    String getFormat();
    
    /**
     * 
     * @param mimeType Mime type of external (or internal) resource
     */
    void setFormat( String mimeType);
    
    /**
     * Returns an integer value that can used for accessing a particular
     * Font character in a TTF file or a catalog for example.
     *
     * @return integer
     */
    int getMarkIndex();
    
    /**
     * Mark index used to specify true type font character; or frame of an animated gif.
     * @param markIndex
     */
    void setMarkIndex( int markIndex );
}
