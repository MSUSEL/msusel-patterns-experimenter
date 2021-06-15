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
package org.archive.util.fingerprint;

import java.io.Serializable;
import java.util.logging.Logger;

import org.archive.util.AbstractLongFPSet;

/**
 * Open-addressing in-memory hash set for holding primitive long fingerprints.
 *
 * @author Gordon Mohr
 */
public class MemLongFPSet extends AbstractLongFPSet
implements LongFPSet, Serializable {
    
    
    private static final long serialVersionUID = -4301879539092625698L;


    private static Logger logger =
        Logger.getLogger(MemLongFPSet.class.getName());
    private static final int DEFAULT_CAPACITY_POWER_OF_TWO = 10;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    protected byte[] slots;
    protected long[] values;

    public MemLongFPSet() {
        this(DEFAULT_CAPACITY_POWER_OF_TWO, DEFAULT_LOAD_FACTOR);
    }

    /**
     * @param capacityPowerOfTwo The capacity as the exponent of a power of 2.
     *  e.g if the capacity is <code>4</code> this means <code>2^^4</code>
     * entries.
     * @param loadFactor The load factor as a fraction.  This gives the amount
     * of free space to keep in the Set.
     */
    public MemLongFPSet(int capacityPowerOfTwo, float loadFactor) {
        super(capacityPowerOfTwo, loadFactor);
        slots = new byte[1 << capacityPowerOfTwo];
        for(int i = 0; i < (1 << capacityPowerOfTwo); i++) {
            slots[i] = EMPTY; // flag value for unused
        }
        values = new long[1 << capacityPowerOfTwo];
    }

    protected void setAt(long i, long val) {
        slots[(int)i] = 1;
        values[(int)i] = val;
    }

    protected long getAt(long i) {
        return values[(int)i];
    }

    protected void makeSpace() {
        grow();
    }

    private void grow() {
        // Catastrophic event.  Log its occurance.
        logger.info("Doubling fingerprinting slots to "
            + (1 << this.capacityPowerOfTwo));
        long[] oldValues = values;
        byte[] oldSlots = slots;
        capacityPowerOfTwo++;
        values = new long[1 << capacityPowerOfTwo];
        slots = new byte[1 << capacityPowerOfTwo];
        for(int i = 0; i < (1 << capacityPowerOfTwo); i++) {
            slots[i]=EMPTY; // flag value for unused
        }
        count=0;
        for(int i = 0; i< oldValues.length; i++) {
            if(oldSlots[i]>=0) {
                add(oldValues[i]);
            }
        }
    }

    protected void relocate(long val, long oldIndex, long newIndex) {
        values[(int)newIndex] = values[(int)oldIndex];
        slots[(int)newIndex] = slots[(int)oldIndex];
        slots[(int)oldIndex] = EMPTY;
    }

    protected int getSlotState(long i) {
        return slots[(int)i];
    }

    protected void clearAt(long index) {
        slots[(int)index]=EMPTY;
        values[(int)index]=0;
    }

    public boolean quickContains(long fp) {
        return contains(fp);
    }
}