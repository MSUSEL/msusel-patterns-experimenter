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

import java.net.HttpURLConnection;

/** Http POST operation parameter. */
public class PutOpParam extends HttpOpParam<PutOpParam.Op> {
  /** Put operations. */
  public static enum Op implements HttpOpParam.Op {
    CREATE(true, HttpURLConnection.HTTP_CREATED),

    MKDIRS(false, HttpURLConnection.HTTP_OK),
    RENAME(false, HttpURLConnection.HTTP_OK),
    SETREPLICATION(false, HttpURLConnection.HTTP_OK),

    SETOWNER(false, HttpURLConnection.HTTP_OK),
    SETPERMISSION(false, HttpURLConnection.HTTP_OK),
    SETTIMES(false, HttpURLConnection.HTTP_OK),
    
    RENEWDELEGATIONTOKEN(false, HttpURLConnection.HTTP_OK),
    CANCELDELEGATIONTOKEN(false, HttpURLConnection.HTTP_OK),
    
    NULL(false, HttpURLConnection.HTTP_NOT_IMPLEMENTED);

    final boolean doOutputAndRedirect;
    final int expectedHttpResponseCode;

    Op(final boolean doOutputAndRedirect, final int expectedHttpResponseCode) {
      this.doOutputAndRedirect = doOutputAndRedirect;
      this.expectedHttpResponseCode = expectedHttpResponseCode;
    }

    @Override
    public HttpOpParam.Type getType() {
      return HttpOpParam.Type.PUT;
    }

    @Override
    public boolean getDoOutput() {
      return doOutputAndRedirect;
    }

    @Override
    public boolean getRedirect() {
      return doOutputAndRedirect;
    }

    @Override
    public int getExpectedHttpResponseCode() {
      return expectedHttpResponseCode;
    }

    @Override
    public String toQueryString() {
      return NAME + "=" + this;
    }
  }

  private static final Domain<Op> DOMAIN = new Domain<Op>(NAME, Op.class);

  /**
   * Constructor.
   * @param str a string representation of the parameter value.
   */
  public PutOpParam(final String str) {
    super(DOMAIN, DOMAIN.parse(str));
  }

  @Override
  public String getName() {
    return NAME;
  }
}