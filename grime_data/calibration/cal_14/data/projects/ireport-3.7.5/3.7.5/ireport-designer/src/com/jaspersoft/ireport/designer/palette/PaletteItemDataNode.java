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
package com.jaspersoft.ireport.designer.palette;

import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.BeanInfo;
import java.io.IOException;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.datatransfer.ExTransferable;

public class PaletteItemDataNode extends DataNode {

    
    private PaletteItem paletteItem;
    
    public PaletteItemDataNode(PaletteItemDataObject obj, PaletteItem paletteItem) {
        super(obj, Children.LEAF);
        this.paletteItem = paletteItem;
        setName( paletteItem.getId() );
        setShortDescription( paletteItem.getComment());
    }
    
    public Image getIcon(int i) {
        if( i == BeanInfo.ICON_COLOR_16x16 ||
            i == BeanInfo.ICON_MONO_16x16 ) {
                return paletteItem.getSmallImage();
        }
        return paletteItem.getBigImage();
    }
    
    public String getDisplayName() {
        return paletteItem.getDisplayName();
    }

    @Override
    public Transferable drag() throws IOException {
        
        ExTransferable tras = ExTransferable.create(super.drag());
        tras.put(new ExTransferable.Single( PaletteUtils.PALETTE_ITEM_DATA_FLAVOR) {

            protected Object getData() throws IOException, UnsupportedFlavorException {
                return paletteItem;
            }
                
        }  );
        
        return tras;
    }
    
    
}