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
package com.jaspersoft.ireport.designer.jrctx;

import com.jaspersoft.ireport.designer.utils.FileEncodingQueryImpl;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.SaveAsCapable;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class JRCTXDataObject extends MultiDataObject {

    final InstanceContent ic;

    public InstanceContent getIc() {
        return ic;
    }
   private AbstractLookup lookup;

   private JRCTXEditorSupport dataEditorSupport = null;

    public JRCTXDataObject(FileObject pf, JRCTXDataLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        ic = new InstanceContent();
        lookup = new AbstractLookup(ic);
        dataEditorSupport = JRCTXEditorSupport.create(this);
        ic.add(dataEditorSupport);
        ic.add(this);
        ic.add( new FileEncodingQueryImpl());
        ic.add( getPrimaryFile() );
    }

    @Override
    protected Node createNodeDelegate() {
        return new JRCTXDataNode(this, getLookup());
    }



     @Override
    public Lookup getLookup() {
        return lookup;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Node.Cookie> T getCookie(Class<T> type) {

        Object o = lookup.lookup(type);
        if (o == null && Node.Cookie.class.isAssignableFrom(type)) // try to look in the super cookie...
        {
           o = super.getCookie(type);
        }

        return o instanceof Node.Cookie ? (T)o : null;
    }

    /**
     * @return the dataEditorSupport
     */
    public JRCTXEditorSupport getDataEditorSupport() {
        return dataEditorSupport;
    }

    /**
     * @param dataEditorSupport the dataEditorSupport to set
     */
    public void setDataEditorSupport(JRCTXEditorSupport dataEditorSupport) {
        this.dataEditorSupport = dataEditorSupport;
    }
}