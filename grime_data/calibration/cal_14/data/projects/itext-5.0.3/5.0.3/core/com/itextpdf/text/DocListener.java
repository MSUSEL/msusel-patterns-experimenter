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
package com.itextpdf.text;

/**
 * A class that implements <CODE>DocListener</CODE> will perform some
 * actions when some actions are performed on a <CODE>Document</CODE>.
 *
 * @see		ElementListener
 * @see		Document
 * @see		DocWriter
 */

public interface DocListener extends ElementListener {
    
    // methods
    
	/**
	 * Signals that the <CODE>Document</CODE> has been opened and that
	 * <CODE>Elements</CODE> can be added.
	 */
    
    public void open(); // [L1]
    
    /**
     * Signals that the <CODE>Document</CODE> was closed and that no other
     * <CODE>Elements</CODE> will be added.
     * <P>
     * The outputstream of every writer implementing <CODE>DocListener</CODE> will be closed.
     */
        
    public void close(); // [L2] 
    
    /**
     * Signals that an new page has to be started.
     *
     * @return	<CODE>true</CODE> if the page was added, <CODE>false</CODE> if not.
     */
        
    public boolean newPage(); // [L3]
    
    /**
     * Sets the pagesize.
     *
     * @param	pageSize	the new pagesize
     * @return	a <CODE>boolean</CODE>
     */
        
    public boolean setPageSize(Rectangle pageSize); // [L4]
        
    /**
     * Sets the margins.
     *
     * @param	marginLeft		the margin on the left
     * @param	marginRight		the margin on the right
     * @param	marginTop		the margin on the top
     * @param	marginBottom	the margin on the bottom
     * @return	a <CODE>boolean</CODE>
     */
        
    public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom);  // [L5]
        
    /**
     * Parameter that allows you to do left/right  margin mirroring (odd/even pages)
     * @param marginMirroring
     * @return true if successful
     */
    public boolean setMarginMirroring(boolean marginMirroring); // [L6]
    
    /**
     * Parameter that allows you to do top/bottom margin mirroring (odd/even pages)
     * @param marginMirroringTopBottom
     * @return true if successful
     * @since	2.1.6
     */
    public boolean setMarginMirroringTopBottom(boolean marginMirroringTopBottom); // [L6]
        
    /**
     * Sets the page number.
     *
     * @param	pageN		the new page number
     */
        
    public void setPageCount(int pageN); // [L7]
    
    /**
     * Sets the page number to 0.
     */
        
    public void resetPageCount(); // [L8]

}