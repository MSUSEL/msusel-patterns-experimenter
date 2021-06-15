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
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.geotools.swing.testutils;

import java.awt.Rectangle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapViewport;

/**
 *
 * @author michael
 *
 * @source $URL$
 */
public class WaitingViewport extends MapViewport {
    
    private CountDownLatch boundsLatch;
    private CountDownLatch screenAreaLatch;

    public void setExpected(WaitingMapContent.Type type) {
        switch (type) {
            case BOUNDS:
                boundsLatch = new CountDownLatch(1);
                break;
                
            case SCREEN_AREA:
                screenAreaLatch = new CountDownLatch(1);
                break;
        }
    }
    
    public boolean await(WaitingMapContent.Type type, long millisTimeout) {
        boolean result = false;
        try {
            switch (type) {
                case BOUNDS:
                    boundsLatch.await(millisTimeout, TimeUnit.MILLISECONDS);
                    break;
                    
                case SCREEN_AREA:
                    screenAreaLatch.await(millisTimeout, TimeUnit.MILLISECONDS);
                    break;
            }
            
        } catch (InterruptedException ex) {
            // do nothing
        } finally {
            return result;
        }
    }
    
    @Override
    public void setBounds(ReferencedEnvelope requestedBounds) {
        super.setBounds(requestedBounds);
        if (boundsLatch != null) {
            boundsLatch.countDown();
        }
    }

    @Override
    public void setScreenArea(Rectangle screenArea) {
        super.setScreenArea(screenArea);
        if (screenAreaLatch != null) {
            screenAreaLatch.countDown();
        }
    }

}
