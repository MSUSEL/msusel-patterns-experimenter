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
package org.archive.crawler.util;

import java.io.Serializable;

import org.archive.util.ArchiveUtils;
import org.archive.util.fingerprint.LongFPSet;

import st.ata.util.FPGenerator;

/**
 * UriUniqFilter storing 64-bit UURI fingerprints, using an internal LongFPSet
 * instance. 
 * 
 * The passed LongFPSet internal instance may be disk or memory based. Accesses
 * to the underlying LongFPSet are synchronized.
 *
 * @author gojomo
 */
public class FPUriUniqFilter extends SetBasedUriUniqFilter 
implements Serializable {
    // Be robust against trivial implementation changes
    private static final long serialVersionUID =
        ArchiveUtils.classnameBasedUID(FPUriUniqFilter.class, 1);
    
//    private static Logger logger =
//        Logger.getLogger(FPUriUniqFilter.class.getName());
    
    private LongFPSet fpset;
    private transient FPGenerator fpgen = FPGenerator.std64;
    
    /**
     * Create FPUriUniqFilter wrapping given long set
     * 
     * @param fpset
     */
    public FPUriUniqFilter(LongFPSet fpset) {
        this.fpset = fpset;
    }
    
    private long getFp(CharSequence canonical) {
        return fpgen.fp(canonical);
    }

    protected boolean setAdd(CharSequence uri) {
        return fpset.add(getFp(uri));
    }

    protected long setCount() {
        return fpset.count();
    }

    protected boolean setRemove(CharSequence uri) {
        return fpset.remove(getFp(uri));
    }
}