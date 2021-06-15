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
package org.apache.hadoop.hdfs.web.resources;

import org.apache.hadoop.fs.permission.FsPermission;

/** Permission parameter, use a Short to represent a FsPermission. */
public class PermissionParam extends ShortParam {
  /** Parameter name. */
  public static final String NAME = "permission";
  /** Default parameter value. */
  public static final String DEFAULT = NULL;

  private static final Domain DOMAIN = new Domain(NAME, 8);

  private static final short DEFAULT_PERMISSION = 0755;
  
  /**
   * Constructor.
   * @param value the parameter value.
   */
  public PermissionParam(final FsPermission value) {
    super(DOMAIN, value == null? null: value.toShort(), null, null);
  }

  /**
   * Constructor.
   * @param str a string representation of the parameter value.
   */
  public PermissionParam(final String str) {
    super(DOMAIN, DOMAIN.parse(str), (short)0, (short)0777);
  }

  @Override
  public String getName() {
    return NAME;
  }

  /** @return the represented FsPermission. */
  public FsPermission getFsPermission() {
    final Short v = getValue();
    return new FsPermission(v != null? v: DEFAULT_PERMISSION);
  }
}