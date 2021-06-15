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
 * Created on 03.11.2004
 */
package net.sourceforge.ganttproject.gui.zoom;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.time.TimeUnitPair;
import net.sourceforge.ganttproject.time.TimeUnitStack;

/**
 * @author bard
 */
public class ZoomManager {
    public static class ZoomState {
        final TimeUnitPair myTimeUnitPair;

        final int myBottomUnitWidth;

        final private int myStateNumber;

        ZoomState(TimeUnitPair timeUnitPair, int bottomUnitWidth,
                int stateNumber) {
            myTimeUnitPair = timeUnitPair;
            myBottomUnitWidth = bottomUnitWidth;
            myStateNumber = stateNumber;
        }

        public String getPersistentName() {
            return myTimeUnitPair.getTimeUnitStack().getName() + ":"
                    + myStateNumber;
        }

        public TimeUnitPair getTimeUnitPair() {
            return myTimeUnitPair;
        }

        public int getBottomUnitWidth() {
            return myBottomUnitWidth;
        }
    }

    private int myZooming = 2;

    private List myListeners = new ArrayList();

    private TimeUnitStack myTimeUnitStack;

    private ZoomState[] myZoomStates;

    public ZoomManager(TimeUnitStack timeUnitStack) {
        myTimeUnitStack = timeUnitStack;
        TimeUnitPair[] unitPairs = myTimeUnitStack.getTimeUnitPairs();
        myZoomStates = new ZoomManager.ZoomState[unitPairs.length];
        int width1 = 60;
        int width2 = 40;
        for (int i = 0; i < unitPairs.length; i++) {
            myZoomStates[i] = new ZoomManager.ZoomState(unitPairs[i], width1, i);
            // myZoomStates[i*2+1] = new ZoomState(unitPairs[i], width2);
            float scale = 2 / 3;
            if (i < unitPairs.length - 1) {
                float defaults1 = unitPairs[i].getBottomTimeUnit()
                        .getAtomCount(myTimeUnitStack.getDefaultTimeUnit());
                float defaults2 = unitPairs[i + 1].getBottomTimeUnit()
                        .getAtomCount(myTimeUnitStack.getDefaultTimeUnit());
                scale = (2 * defaults2) / (3 * defaults1);
            }
            width1 = (int) (width1 * scale);
        }
    }

    public boolean canZoomIn() {
        return myZooming > 0;
    }

    public boolean canZoomOut() {
        return myZooming < myZoomStates.length - 1;
    }

    public void zoomIn() {
        int oldValue = myZooming--;
        fireZoomingChanged(oldValue, myZooming);
    }

    public void zoomOut() {
        int oldValue = myZooming++;
        fireZoomingChanged(oldValue, myZooming);
    }

    public void addZoomListener(ZoomListener listener) {
        myListeners.add(listener);
        listener.zoomChanged(new ZoomEvent(this, myZoomStates[myZooming]));
    }

    public void removeZoomListener(ZoomListener listener) {
        myListeners.remove(listener);
    }

    private void fireZoomingChanged(int oldZoomValue, int newZoomValue) {
        ZoomEvent e = new ZoomEvent(this, myZoomStates[newZoomValue]);
        for (int i = 0; i < myListeners.size(); i++) {
            ZoomListener nextListener = (ZoomListener) myListeners.get(i);
            nextListener.zoomChanged(e);
        }
    }

    public void setZoomState(String persistentName) {
        for (int i = 0; i < myZoomStates.length; i++) {
            if (myZoomStates[i].getPersistentName().equals(persistentName)) {
                myZooming = i;
                fireZoomingChanged(0, myZooming);
                break;
            }
        }
    }

    public ZoomState getZoomState() {
        return myZoomStates[myZooming];
    }

}
