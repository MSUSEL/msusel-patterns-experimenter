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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;


/** Http operation parameter. */
public abstract class HttpOpParam<E extends Enum<E> & HttpOpParam.Op>
    extends EnumParam<E> {
  /** Parameter name. */
  public static final String NAME = "op";

  /** Default parameter value. */
  public static final String DEFAULT = NULL;

  /** Http operation types */
  public static enum Type {
    GET, PUT, POST, DELETE;
  }

  /** Http operation interface. */
  public static interface Op {
    /** @return the Http operation type. */
    public Type getType();

    /** @return true if the operation will do output. */
    public boolean getDoOutput();

    /** @return true if the operation will be redirected. */
    public boolean getRedirect();

    /** @return true the expected http response code. */
    public int getExpectedHttpResponseCode();

    /** @return a URI query string. */
    public String toQueryString();
  }

  /** Expects HTTP response 307 "Temporary Redirect". */
  public static class TemporaryRedirectOp implements Op {
    static final TemporaryRedirectOp CREATE = new TemporaryRedirectOp(
        PutOpParam.Op.CREATE);
    static final TemporaryRedirectOp APPEND = new TemporaryRedirectOp(
        PostOpParam.Op.APPEND);
    static final TemporaryRedirectOp OPEN = new TemporaryRedirectOp(
        GetOpParam.Op.OPEN);
    static final TemporaryRedirectOp GETFILECHECKSUM = new TemporaryRedirectOp(
        GetOpParam.Op.GETFILECHECKSUM);
    
    static final List<TemporaryRedirectOp> values
        = Collections.unmodifiableList(Arrays.asList(
            new TemporaryRedirectOp[]{CREATE, APPEND, OPEN, GETFILECHECKSUM}));
    
    /** Get an object for the given op. */
    public static TemporaryRedirectOp valueOf(final Op op) {
      for(TemporaryRedirectOp t : values) {
        if (op == t.op) {
          return t;
        }
      }
      throw new IllegalArgumentException(op + " not found.");
    }

    private final Op op;

    private TemporaryRedirectOp(final Op op) {
      this.op = op;
    }

    @Override
    public Type getType() {
      return op.getType();
    }

    @Override
    public boolean getDoOutput() {
      return op.getDoOutput();
    }

    @Override
    public boolean getRedirect() {
      return false;
    }

    /** Override the original expected response with "Temporary Redirect". */
    @Override
    public int getExpectedHttpResponseCode() {
      return Response.Status.TEMPORARY_REDIRECT.getStatusCode();
    }

    @Override
    public String toQueryString() {
      return op.toQueryString();
    }
  }

  HttpOpParam(final Domain<E> domain, final E value) {
    super(domain, value);
  }
}