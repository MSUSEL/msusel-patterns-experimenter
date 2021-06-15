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
package com.jaspersoft.ireport.designer.tools;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author gtoffoli
 */
public class MaskedPlainDocument extends  PlainDocument {
    
    public static final int NO_MAX_LENGTH = 0;
    
    public static final String COLOR_MASK = "(#?)(([0-9]|[a-f]|[A-F]){0,6})";
    private String mask = null;
    private int maxLength = 0;
    
    /**
     * Create a MaskedPlainDocument based on the given regex espression
     */
    public MaskedPlainDocument(String mask, int maxLength)
    {
        this.mask = mask;
        this.maxLength = maxLength;
    }
    
    /**
     * Same as MaskedPlainDocument(String mask, NO_MAX_LENGTH)
     *
     */
    public MaskedPlainDocument(String mask)
    {
        this(mask, NO_MAX_LENGTH);
    }
    
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            
            if (str == null)
                    return;
            if (maxLength > 0 && offset >= maxLength) {
                    return;
            }
            // does the insertion exceed the max length
            if (maxLength > 0 && str.length() > maxLength) {
                    str = str.substring(0, maxLength);
            }
            
            // Create the final string...
            try {
                
            String currentString = getText(0, offset);
            currentString += str;
            
            //System.out.println("Getting text " + offset +  "  ==> " + getLength());
            
            if (offset < getLength())
            {
                currentString +=  getText(offset, getLength()-offset);
            }
            // remove the last '\n' if any...
            //if (currentString.endsWith("\n")) currentString = currentString.substring(0, currentString.length()-1);
            currentString = currentString.trim();
            //System.out.println(currentString + " " + currentString.matches(mask) +  "  ==> " + mask);
            
            if (currentString.matches(mask))
            {
                str = str.toUpperCase();
                super.insertString(offset, str, attr);
            }
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
    
}
