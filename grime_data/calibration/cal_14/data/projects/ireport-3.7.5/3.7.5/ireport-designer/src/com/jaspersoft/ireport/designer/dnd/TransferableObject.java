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
package com.jaspersoft.ireport.designer.dnd;

/**
 *
 * @author  Administrator
 */
public class TransferableObject implements java.awt.datatransfer.Transferable {
    
    Object obj;
    java.awt.datatransfer.DataFlavor thisFlavor;
    /** Creates a new instance of TransferableObject */
    public TransferableObject(Object obj) {
        this.obj = obj;
        thisFlavor = new java.awt.datatransfer.DataFlavor(obj.getClass(), obj.getClass().getName());
    }
    
    public Object getTransferData(java.awt.datatransfer.DataFlavor flavor) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
        if (flavor.equals( thisFlavor ))
        {
            return obj;
       }
       // GDN new code start
       else
       if (flavor.equals( java.awt.datatransfer.DataFlavor.stringFlavor )) {
            return new String();    // anything non-null
       }
       else
       // GDN new code end
            return null;
    }
    
    public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() {
        // GDN new code start
        java.awt.datatransfer.DataFlavor stringFlavor = java.awt.datatransfer.DataFlavor.stringFlavor;
        return new java.awt.datatransfer.DataFlavor[] { thisFlavor,
                                                        stringFlavor };
        // GDN new code end
        
        // GDN comment out-->return new java.awt.datatransfer.DataFlavor[]{thisFlavor};
    }
    
    public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor) {
// GDN comment out
//        if (flavor != null && flavor.equals( thisFlavor ))
//        {
//            return true;
//        }
//        
//        return false;
// GDN comment out
        // GDN new code begin
        if (flavor == null)
            return false;
        else
        if (flavor.equals( thisFlavor ))
            return true;
        else
        if (flavor.equals( java.awt.datatransfer.DataFlavor.stringFlavor ))
            return true;
        else
            return false;
        // GDN new code end
    }
    
}
