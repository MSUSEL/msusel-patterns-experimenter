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
package com.jaspersoft.ireport.designer;

import com.jaspersoft.ireport.locale.I18n;

/**
 *
 * @author gtoffoli
 */
public class IRFont {
    
    private java.awt.Font font;
    
    private java.lang.String file;
    
    /** Creates a new instance of IRFont */
    public IRFont() {
    }
    
     public IRFont(java.awt.Font font, java.lang.String file) {
         this.font = font;
         this.file = file;
    }
    
    /** Getter for property file.
     * @return Value of property file.
     *
     */
    public java.lang.String getFile() {
        return file;
    }
    
    /** Setter for property file.
     * @param file New value of property file.
     *
     */
    public void setFile(java.lang.String file) {
        this.file = file;
    }
    
    /** Getter for property font.
     * @return Value of property font.
     *
     */
    public java.awt.Font getFont() {
        return font;
    }
    
    /** Setter for property font.
     * @param font New value of property font.
     *
     */
    public void setFont(java.awt.Font font) {
        this.font = font;
    }
    
    
    @Override
    public String toString()
    {
        if (font == null || file == null) return I18n.getString("IRFont.Message.NotInitializedFont");
        return font.getFontName()+" ("+file+")";
    }    
}
