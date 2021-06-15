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
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.util;


/**
 * A sub progress monitor, used to delegate a portion of work to a separate process.
 * <p>
 * Example:<pre><code>
 * if( progress == null ) progress = new NullProgressListener();
 * progress.started();
 * progress.setDecsription("Connect");
 * ..connect to data store and obtain feature collection...
 * progress.progress( 20 ); // connecting represents 20% of the work
 * progress.setDescription("Process features");
 * featureCollection.accepts( visitor, new SubProgress( progress, 80 ) );
 * progress.completed();
 * </code></pre>
 * 
 * @author Jody
 *
 *
 *
 * @source $URL$
 */
public class SubProgressListener extends DelegateProgressListener {
    /** Initial starting value */
    float start;
    
    /** Amount of work we have been asked to perform */
    float amount;
    
    /** Scale between subprogress and delegate */
    float scale;
    
    /** running total of amount we have worked thus far */
    float progress;
   
    /**
     * Create a sub progress monitor, used to delegate work to a separate
     * process.
     * @param progress parent progress to notify as we get work done
     * @param amount amount of progress represented
     */
    public SubProgressListener( org.opengis.util.ProgressListener progress, float amount ) {
        super(progress);
        this.start = progress.getProgress();
        this.amount = (amount > 0.0f) ? amount : 0.0f;
        this.scale = this.amount / 100.0f;
    }
    public void started() {
        progress = 0.0f;
    }
    public void complete() {
        delegate.progress( start + amount );        
        progress = 100.0f;
    }
    public float getProgress() {
        return progress;
    }
    public void progress( float progress ) {
        this.progress = progress;
        super.progress(start + (scale * progress));
    }
}
