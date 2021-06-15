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

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hdfs.web.JsonUtil;
import org.apache.hadoop.ipc.RemoteException;
import org.apache.hadoop.security.authorize.AuthorizationException;

import com.sun.jersey.api.ParamException;
import com.sun.jersey.api.container.ContainerException;

/** Handle exceptions. */
@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {
  public static final Log LOG = LogFactory.getLog(ExceptionHandler.class);

  private static Exception toCause(Exception e) {
    final Throwable t = e.getCause();
    if (t != null && t instanceof Exception) {
      e = (Exception)e.getCause();
    }
    return e;
  }

  private @Context HttpServletResponse response;

  @Override
  public Response toResponse(Exception e) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("GOT EXCEPITION", e);
    }

    //clear content type
    response.setContentType(null);

    //Convert exception
    if (e instanceof ParamException) {
      final ParamException paramexception = (ParamException)e;
      e = new IllegalArgumentException("Invalid value for webhdfs parameter \""
          + paramexception.getParameterName() + "\": "
          + e.getCause().getMessage(), e);
    }
    if (e instanceof ContainerException) {
      e = toCause(e);
    }
    if (e instanceof RemoteException) {
      e = ((RemoteException)e).unwrapRemoteException();
    }

    //Map response status
    final Response.Status s;
    if (e instanceof SecurityException) {
      s = Response.Status.UNAUTHORIZED;
    } else if (e instanceof AuthorizationException) {
      s = Response.Status.UNAUTHORIZED;
    } else if (e instanceof FileNotFoundException) {
      s = Response.Status.NOT_FOUND;
    } else if (e instanceof IOException) {
      s = Response.Status.FORBIDDEN;
    } else if (e instanceof UnsupportedOperationException) {
      s = Response.Status.BAD_REQUEST;
    } else if (e instanceof IllegalArgumentException) {
      s = Response.Status.BAD_REQUEST;
    } else {
      LOG.warn("INTERNAL_SERVER_ERROR", e);
      s = Response.Status.INTERNAL_SERVER_ERROR;
    }
 
    final String js = JsonUtil.toJsonString(e);
    return Response.status(s).type(MediaType.APPLICATION_JSON).entity(js).build();
  }
}
