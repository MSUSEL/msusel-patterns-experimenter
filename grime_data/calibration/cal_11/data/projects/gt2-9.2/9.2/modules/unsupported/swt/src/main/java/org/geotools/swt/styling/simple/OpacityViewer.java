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
/*
 *    uDig - User Friendly Desktop Internet GIS client
 *    http://udig.refractions.net
 *    (C) 2004, Refractions Research Inc.
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.geotools.swt.styling.simple;

import java.text.MessageFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.StyleBuilder;
import org.geotools.swt.utils.Messages;

/**
 * Allows editing/viewing of Opacity Element.
 * <p>
 * Here is the pretty picture: <pre><code>
 *          +----------------+
 *   Label: |       opacity\/|
 *          +----------------+
 * </code></pre>
 * </p>
 * <p>
 * Workflow:
 * <ol>
 * <li>createControl( parent ) - set up controls
 * <li>set( raster, mode ) - provide content from SimpleStyleConfigurator
 *    <ol>
 *    <li> Symbolizer values copied into fields based on mode
 *    <li> fields copied into controls
 *    <li> controls enabled based on mode & fields
 *    </ol>
 * <li>Listener.widgetSelected/modifyText - User performs an "edit"
 * <li>Listener.sync( SelectionEvent ) - update fields with values of controls
 * <li>fire( SelectionSevent ) - notify SimpleStyleConfigurator of change
 * <li>get( StyleBuilder ) - construct based on fields
 * </ul>
 * </p>  
 * @author Emily Gouge (Refractions Research, Inc.)
 * @since 1.0.0
 *
 *
 *
 * @source $URL$
 */
public class OpacityViewer {
    double opacity = Double.NaN;

    private Combo percent;
    private SelectionListener listener;

    private class Listener implements ModifyListener {
        public void modifyText( ModifyEvent e ) {
            sync(AbstractSimpleConfigurator.selectionEvent(e));
        };

        private void sync( SelectionEvent e ) {
            try {
                try {
                    String ptext = OpacityViewer.this.percent.getText();
                    if (ptext.endsWith("%")) { //$NON-NLS-1$
                        ptext = ptext.substring(0, ptext.length() - 1);
                        OpacityViewer.this.opacity = Double.parseDouble(ptext);
                        OpacityViewer.this.opacity /= 100.0;
                    } else {
                        OpacityViewer.this.opacity = Double.parseDouble(ptext);
                        if (OpacityViewer.this.opacity > 1) {
                            OpacityViewer.this.opacity /= 100.0;
                        }
                    }
                } catch (NumberFormatException nan) {
                    // well lets just leave opacity alone
                }
                fire(e); // everything worked
            } catch (Throwable t) {
            }
        }

    };
    Listener sync = new Listener();

    /**
     * TODO summary sentence for createControl ...
     * 
     * @param parent
     * @param listener1 
     * @return Generated composite
     */
    public Composite createControl( Composite parent, KeyListener listener1 ) {
        Composite part = AbstractSimpleConfigurator.subpart(parent, Messages.getString("SimpleStyleConfigurator_raster_label"));

        this.percent = new Combo(part, SWT.DROP_DOWN);
        this.percent.setItems(new String[]{"0%", "25%", "50%", "75%", "100%"}); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
        this.percent.setTextLimit(4);
        this.percent.addKeyListener(listener1);
        this.percent.setToolTipText(Messages.getString("RasterViewer_percent_tooltip"));
        return part;
    }

    void listen( boolean listen ) {
        if (listen) {
            this.percent.addModifyListener(this.sync);
        } else {
            this.percent.removeModifyListener(this.sync);
        }
    }

    /**
     * Accepts a listener that will be notified when content changes.
     * @param listener1 
     */
    public void addListener( SelectionListener listener1 ) {
        this.listener = listener1;
    }

    /**
     * Remove listener.
     * @param listener1 
     */
    public void removeListener( SelectionListener listener1 ) {
        if (this.listener == listener1)
            this.listener = null;
    }

    /**
     * TODO summary sentence for fire ...
     * 
     * @param event
     */
    protected void fire( SelectionEvent event ) {
        if (this.listener == null)
            return;
        this.listener.widgetSelected(event);
    }

    /** 
     * Called to set up this "viewer" based on the provided symbolizer 
     * @param sym 
     */
    public void set( RasterSymbolizer sym2 ) {
        listen(false); // don't sync when setting up
        try {
            RasterSymbolizer sym = sym2;

            if (sym == null) {
                this.opacity = 1.0;
            } else {
                this.opacity = SLD.rasterOpacity(sym);
            }
            String text = MessageFormat.format("{0,number,#0%}", this.opacity); //$NON-NLS-1$
            this.percent.setText(text);
            this.percent.select(this.percent.indexOf(text));

        } finally {
            listen(true); // listen to user now
        }
    }

    /**
     * TODO summary sentence for getStroke ...
     * @param build 
     * 
     * @return Stroke defined by this model
     */
    public RasterSymbolizer get( StyleBuilder build ) {
        return (Double.isNaN(opacity)) ? null : build.createRasterSymbolizer(null, this.opacity);
    }

    public double getValue() {
        return this.opacity;
    }
}
