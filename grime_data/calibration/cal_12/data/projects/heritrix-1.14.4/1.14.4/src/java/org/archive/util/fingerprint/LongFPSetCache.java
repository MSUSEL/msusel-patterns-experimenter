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



/**
 * Like a MemLongFPSet, but with fixed capacity and maximum size.
 * When an add would expand past the maximum size, an old entry
 * is deleted via a clock/counter algorithm.
 *
 * @author gojomo
 *
 */
public class LongFPSetCache extends MemLongFPSet {
    
    private static final long serialVersionUID = -5307436423975825566L;

    long sweepHand = 0;

    public LongFPSetCache() {
        super();
    }

    public LongFPSetCache(int capacityPowerOfTwo, float loadFactor) {
        super(capacityPowerOfTwo, loadFactor);
    }

    protected void noteAccess(long index) {
        if(slots[(int)index]<Byte.MAX_VALUE) {
            slots[(int)index]++;
        }
    }

    protected void makeSpace() {
        discard(1);
    }

    private void discard(int i) {
        int toDiscard = i;
        while(toDiscard>0) {
            if(slots[(int)sweepHand]==0) {
                removeAt(sweepHand);
                toDiscard--;
            } else {
                if (slots[(int)sweepHand]>0) {
                    slots[(int)sweepHand]--;
                }
            }
            sweepHand++;
            if (sweepHand==slots.length) {
                sweepHand = 0;
            }
        }
    }
}
