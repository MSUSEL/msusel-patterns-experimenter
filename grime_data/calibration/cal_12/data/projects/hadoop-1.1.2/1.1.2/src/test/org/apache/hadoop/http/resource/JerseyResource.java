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
package org.apache.hadoop.http.resource;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.util.ajax.JSON;

/**
 * A simple Jersey resource class TestHttpServer.
 * The servlet simply puts the path and the op parameter in a map
 * and return it in JSON format in the response.
 */
@Path("")
public class JerseyResource {
  static final Log LOG = LogFactory.getLog(JerseyResource.class);

  public static final String PATH = "path";
  public static final String OP = "op";

  @GET
  @Path("{" + PATH + ":.*}")
  @Produces({MediaType.APPLICATION_JSON})
  public Response get(
      @PathParam(PATH) @DefaultValue("UNKNOWN_" + PATH) final String path,
      @QueryParam(OP) @DefaultValue("UNKNOWN_" + OP) final String op
      ) throws IOException {
    LOG.info("get: " + PATH + "=" + path + ", " + OP + "=" + op);

    final Map<String, Object> m = new TreeMap<String, Object>();
    m.put(PATH, path);
    m.put(OP, op);
    final String js = JSON.toString(m);
    return Response.ok(js).type(MediaType.APPLICATION_JSON).build();
  }
}