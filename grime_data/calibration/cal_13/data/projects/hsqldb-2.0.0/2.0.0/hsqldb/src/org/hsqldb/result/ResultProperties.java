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

package org.hsqldb.result;

/*
 * Execute properties for SELECT statements.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class ResultProperties {

    //
    final static int idx_returnable = 0;
    final static int idx_holdable   = 1;
    final static int idx_scrollable = 2;
    final static int idx_updatable  = 3;
    final static int idx_sensitive  = 4;

    //
    public static final int defaultPropsValue   = 0;
    public static final int updatablePropsValue = 1 << idx_updatable;

    // uses SQL constants - no JDBC
    public static int getProperties(int sensitive, int updatable,
                                    int scrollable, int holdable,
                                    int returnable) {

        int combined = (sensitive << idx_sensitive)
                       | (updatable << idx_updatable)
                       | (scrollable << idx_scrollable)
                       | (holdable << idx_holdable)
                       | (returnable << idx_returnable);

        return combined;
    }

    public static int getJDBCHoldability(int props) {
        return isHoldable(props) ? ResultConstants.HOLD_CURSORS_OVER_COMMIT
                                 : ResultConstants.CLOSE_CURSORS_AT_COMMIT;
    }

    public static int getJDBCConcurrency(int props) {
        return isReadOnly(props) ? ResultConstants.CONCUR_READ_ONLY
                                 : ResultConstants.CONCUR_UPDATABLE;
    }

    public static int getJDBCScrollability(int props) {
        return isScrollable(props) ? ResultConstants.TYPE_SCROLL_INSENSITIVE
                                   : ResultConstants.TYPE_FORWARD_ONLY;
    }

    public static int getValueForJDBC(int type, int concurrency,
                                      int holdability) {

        int scrollable = type == ResultConstants.TYPE_FORWARD_ONLY ? 0
                                                                   : 1;
        int updatable  = concurrency == ResultConstants.CONCUR_UPDATABLE ? 1
                                                                         : 0;
        int holdable = holdability == ResultConstants.HOLD_CURSORS_OVER_COMMIT
                       ? 1
                       : 0;
        int prop = (updatable << idx_updatable)
                   | (scrollable << idx_scrollable)
                   | (holdable << idx_holdable);

        return prop;
    }

    public static boolean isUpdatable(int props) {
        return (props & (1 << idx_updatable)) == 0 ? false
                                                   : true;
    }

    public static boolean isScrollable(int props) {
        return (props & (1 << idx_scrollable)) == 0 ? false
                                                    : true;
    }

    public static boolean isHoldable(int props) {
        return (props & (1 << idx_holdable)) == 0 ? false
                                                  : true;
    }

    public static boolean isSensitive(int props) {
        return (props & (1 << idx_sensitive)) == 0 ? false
                                                   : true;
    }

    public static boolean isReadOnly(int props) {
        return (props & (1 << idx_updatable)) == 0 ? true
                                                   : false;
    }

    public static int addUpdatable(int props, boolean flag) {
        return flag ? props | ((1) << idx_updatable)
                    : props & (~(1 << idx_updatable));
    }

    public static int addHoldable(int props, boolean flag) {
        return flag ? props | ((1) << idx_holdable)
                    : props & (~(1 << idx_holdable));
    }

    public static int addScrollable(int props, boolean flag) {
        return flag ? props | ((1) << idx_scrollable)
                    : props & (~(1 << idx_scrollable));
    }
}
