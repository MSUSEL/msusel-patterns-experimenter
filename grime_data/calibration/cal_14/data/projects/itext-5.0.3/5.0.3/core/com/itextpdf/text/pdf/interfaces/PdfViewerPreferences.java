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
package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;

/**
 * Viewer preferences are described in section 3.6.1 and 8.1 of the
 * PDF Reference 1.7 (Table 3.25 on p139-142 and Table 8.1 on p579-581).
 * They are explained in section 13.1 of the book 'iText in Action'.
 * The values of the different  preferences were originally stored
 * in class PdfWriter, but they have been moved to this separate interface
 * for reasons of convenience.
 */

public interface PdfViewerPreferences {	
		
    /**
     * Sets the page layout and page mode preferences by ORing one or two of these constants.
     * <p>
     * <ul>
     * <li>The page layout to be used when the document is opened (choose one).
     *   <ul>
     *   <li><b>PageLayoutSinglePage</b> - Display one page at a time. (default)
     *   <li><b>PageLayoutOneColumn</b> - Display the pages in one column.
     *   <li><b>PageLayoutTwoColumnLeft</b> - Display the pages in two columns, with
     *       odd-numbered pages on the left.
     *   <li><b>PageLayoutTwoColumnRight</b> - Display the pages in two columns, with
     *       odd-numbered pages on the right.
     *   <li><b>PageLayoutTwoPageLeft</b> - Display the pages two at a time, with
     *       odd-numbered pages on the left.
     *   <li><b>PageLayoutTwoPageRight</b> - Display the pages two at a time, with
     *       odd-numbered pages on the right.
     *   </ul>
     * <li>The page mode how the document should be displayed
     *     when opened (choose one).
     *   <ul>
     *   <li><b>PageModeUseNone</b> - Neither document outline nor thumbnail images visible. (default)
     *   <li><b>PageModeUseOutlines</b> - Document outline visible.
     *   <li><b>PageModeUseThumbs</b> - Thumbnail images visible.
     *   <li><b>PageModeFullScreen</b> - Full-screen mode, with no menu bar, window
     *       controls, or any other window visible.
     *   <li><b>PageModeUseOC</b> - Optional content group panel visible
     *   <li><b>PageModeUseAttachments</b> - Attachments panel visible
     *   </ul>
     * </ul>
     * For backward compatibility these values are also supported,
     * but it's better to use method <code>addViewerPreference(key, value)</code>
     * if you want to change the following preferences:
     * <ul>
     * <li><b>HideToolbar</b> - A flag specifying whether to hide the viewer application's tool
     *     bars when the document is active.
     * <li><b>HideMenubar</b> - A flag specifying whether to hide the viewer application's
     *     menu bar when the document is active.
     * <li><b>HideWindowUI</b> - A flag specifying whether to hide user interface elements in
     *     the document's window (such as scroll bars and navigation controls),
     *     leaving only the document's contents displayed.
     * <li><b>FitWindow</b> - A flag specifying whether to resize the document's window to
     *     fit the size of the first displayed page.
     * <li><b>CenterWindow</b> - A flag specifying whether to position the document's window
     *     in the center of the screen.
     * <li><b>DisplayDocTitle</b> - A flag specifying whether to display the document's title
     *     in the top bar.
     * <li>The predominant reading order for text. This entry has no direct effect on the
     *     document's contents or page numbering, but can be used to determine the relative
     *     positioning of pages when displayed side by side or printed <i>n-up</i> (choose one).
     *   <ul>
     *   <li><b>DirectionL2R</b> - Left to right
     *   <li><b>DirectionR2L</b> - Right to left (including vertical writing systems such as
     *       Chinese, Japanese, and Korean)
     *   </ul>
     * <li>The document's page mode, specifying how to display the
     *     document on exiting full-screen mode. It is meaningful only
     *     if the page mode is <b>PageModeFullScreen</b> (choose one).
     *   <ul>
     *   <li><b>NonFullScreenPageModeUseNone</b> - Neither document outline nor thumbnail images
     *       visible
     *   <li><b>NonFullScreenPageModeUseOutlines</b> - Document outline visible
     *   <li><b>NNonFullScreenPageModeUseThumbs</b> - Thumbnail images visible
     *   <li><b>NonFullScreenPageModeUseOC</b> - Optional content group panel visible
     *   </ul>
     * <li><b>PrintScalingNone</b> - Indicates that the print dialog should reflect no page scaling.
     * </ul>
     * @param preferences the viewer preferences
	 * @see PdfViewerPreferences#addViewerPreference
     */
    public void setViewerPreferences(int preferences);
    
    /**
     * Adds a viewer preference.
     * <ul>
     * <li>In case the key is one of these values:
     * 		<ul>
     * 			<li>PdfName.<b>HIDETOOLBAR</b>
     * 			<li>PdfName.<b>HIDEMENUBAR</b>
     * 			<li>PdfName.<b>HIDEWINDOWUI</b>
     * 			<li>PdfName.<b>FITWINDOW</b>
     * 			<li>PdfName.<b>CENTERWINDOW</b>
     * 			<li>PdfName.<b>DISPLAYDOCTITLE</b>
     * 		</ul>
     * The value must be a of type PdfBoolean (true or false).
     * <li>In case the key is PdfName.<b>NONFULLSCREENPAGEMODE</b>,
     * the value must be one of these names:
     * 		<ul>
     * 			<li>PdfName.<b>USENONE</b>
     * 			<li>PdfName.<b>USEOUTLINES</b>
     * 			<li>PdfName.<b>USETHUMBS</b>
     * 			<li>PdfName.<b>USEOC</b>
     * 		</ul>
     * <li>In case the key is PdfName.DIRECTION,
     * the value must be one of these names:
     * 		<ul>
     * 			<li>PdfName.<b>L2R</b>
     * 			<li>PdfName.<b>R2L</b>
     * 		</ul>
     * <li>In case the key is one of these values:
     * 		<ul>
     * 			<li>PdfName.<b>VIEWAREA</b>
     * 			<li>PdfName.<b>VIEWCLIP</b>
     * 			<li>PdfName.<b>PRINTAREA</b>
     * 			<li>PdfName.<b>PRINTCLIP</b>
     * 		</ul>
     * 	The value must be one of these names:
     * 		<ul>
     * 			<li>PdfName.<b>MEDIABOX</b>
     * 			<li>PdfName.<b>CROPBOX</b>
     * 			<li>PdfName.<b>BLEEDBOX</b>
     * 			<li>PdfName.<b>TRIMBOX</b>
     * 			<li>PdfName.<b>ARTBOX</b>
     * 		</ul>
     * <li>In case the key is PdfName.<b>PRINTSCALING</b>, the value can be
     * 		<ul>
     * 			<li>PdfName.<b>APPDEFAULT</b>
     * 			<li>PdfName.<b>NONE</b>
     * 		</ul>
     * <li>In case the key is PdfName.<b>DUPLEX</b>, the value can be:
     * 		<ul>
     * 			<li>PdfName.<b>SIMPLEX</b>
     * 			<li>PdfName.<b>DUPLEXFLIPSHORTEDGE</b>
     * 			<li>PdfName.<b>DUPLEXFLIPLONGEDGE</b>
     * 		</ul>
     * <li>In case the key is PdfName.<b>PICKTRAYBYPDFSIZE</b>, the value must be of type PdfBoolean.
     * <li>In case the key is PdfName.<b>PRINTPAGERANGE</b>, the value must be of type PdfArray.
     * <li>In case the key is PdfName.<b>NUMCOPIES</b>, the value must be of type PdfNumber.
     * <ul>
     * </ul>
     * @param key	the name of the viewer preference
     * @param value	the value of the viewer preference
	 * @see PdfViewerPreferences#setViewerPreferences
     */
    public void addViewerPreference(PdfName key, PdfObject value);    	
}