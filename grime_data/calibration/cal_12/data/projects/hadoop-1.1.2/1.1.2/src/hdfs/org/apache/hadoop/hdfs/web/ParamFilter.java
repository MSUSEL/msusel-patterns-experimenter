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
package org.apache.hadoop.hdfs.web;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

/**
 * A filter to change parameter names to lower cases
 * so that parameter names are considered as case insensitive.
 */
public class ParamFilter implements ResourceFilter {
  private static final ContainerRequestFilter LOWER_CASE
      = new ContainerRequestFilter() {
    @Override
    public ContainerRequest filter(final ContainerRequest request) {
      final MultivaluedMap<String, String> parameters = request.getQueryParameters();
      if (containsUpperCase(parameters.keySet())) {
        //rebuild URI
        final URI lower = rebuildQuery(request.getRequestUri(), parameters);
        request.setUris(request.getBaseUri(), lower);
      }
      return request;
    }
  };

  @Override
  public ContainerRequestFilter getRequestFilter() {
    return LOWER_CASE;
  }

  @Override
  public ContainerResponseFilter getResponseFilter() {
    return null;
  }

  /** Do the strings contain upper case letters? */
  static boolean containsUpperCase(final Iterable<String> strings) {
    for(String s : strings) {
      for(int i = 0; i < s.length(); i++) {
        if (Character.isUpperCase(s.charAt(i))) {
          return true;
        }
      }
    }
    return false;
  }

  /** Rebuild the URI query with lower case parameter names. */
  private static URI rebuildQuery(final URI uri,
      final MultivaluedMap<String, String> parameters) {
    UriBuilder b = UriBuilder.fromUri(uri).replaceQuery("");
    for(Map.Entry<String, List<String>> e : parameters.entrySet()) {
      final String key = e.getKey().toLowerCase();
      for(String v : e.getValue()) {
        b = b.queryParam(key, v);
      }
    }
    return b.build();
  }
}