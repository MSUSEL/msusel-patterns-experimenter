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
package org.apache.hadoop.metrics;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.http.HttpServer;
import org.apache.hadoop.metrics.spi.OutputRecord;
import org.apache.hadoop.metrics.spi.AbstractMetricsContext.MetricMap;
import org.apache.hadoop.metrics.spi.AbstractMetricsContext.TagMap;
import org.mortbay.util.ajax.JSON;
import org.mortbay.util.ajax.JSON.Output;

/**
 * A servlet to print out metrics data.  By default, the servlet returns a 
 * textual representation (no promises are made for parseability), and
 * users can use "?format=json" for parseable output.
 * @deprecated in favor of <code>org.apache.hadoop.metrics2</code> usage.
 */
@Deprecated
@InterfaceAudience.Private
@InterfaceStability.Evolving
public class MetricsServlet extends HttpServlet {
  
  /**
   * A helper class to hold a TagMap and MetricMap.
   */
  static class TagsMetricsPair implements JSON.Convertible {
    final TagMap tagMap;
    final MetricMap metricMap;
    
    public TagsMetricsPair(TagMap tagMap, MetricMap metricMap) {
      this.tagMap = tagMap;
      this.metricMap = metricMap;
    }

    @SuppressWarnings("unchecked")
    public void fromJSON(Map map) {
      throw new UnsupportedOperationException();
    }

    /** Converts to JSON by providing an array. */
    public void toJSON(Output out) {
      out.add(new Object[] { tagMap, metricMap });
    }
  }
  
  /**
   * Collects all metric data, and returns a map:
   *   contextName -> recordName -> [ (tag->tagValue), (metric->metricValue) ].
   * The values are either String or Number.  The final value is implemented
   * as a list of TagsMetricsPair.
   */
   Map<String, Map<String, List<TagsMetricsPair>>> makeMap(
       Collection<MetricsContext> contexts) throws IOException {
    Map<String, Map<String, List<TagsMetricsPair>>> map = 
      new TreeMap<String, Map<String, List<TagsMetricsPair>>>();

    for (MetricsContext context : contexts) {
      Map<String, List<TagsMetricsPair>> records = 
        new TreeMap<String, List<TagsMetricsPair>>();
      map.put(context.getContextName(), records);
    
      for (Map.Entry<String, Collection<OutputRecord>> r : 
          context.getAllRecords().entrySet()) {
        List<TagsMetricsPair> metricsAndTags = 
          new ArrayList<TagsMetricsPair>();
        records.put(r.getKey(), metricsAndTags);
        for (OutputRecord outputRecord : r.getValue()) {
          TagMap tagMap = outputRecord.getTagsCopy();
          MetricMap metricMap = outputRecord.getMetricsCopy();
          metricsAndTags.add(new TagsMetricsPair(tagMap, metricMap));
        }
      }
    }
    return map;
  }
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // Do the authorization
    if (!HttpServer.hasAdministratorAccess(getServletContext(), request,
        response)) {
      return;
    }

    PrintWriter out = new PrintWriter(response.getOutputStream());
    String format = request.getParameter("format");
    Collection<MetricsContext> allContexts = 
      ContextFactory.getFactory().getAllContexts();
    if ("json".equals(format)) {
      // Uses Jetty's built-in JSON support to convert the map into JSON.
      out.print(new JSON().toJSON(makeMap(allContexts)));
    } else {
      printMap(out, makeMap(allContexts));
    }
    out.close();
  }
  
  /**
   * Prints metrics data in a multi-line text form.
   */
  void printMap(PrintWriter out, Map<String, Map<String, List<TagsMetricsPair>>> map) {
    for (Map.Entry<String, Map<String, List<TagsMetricsPair>>> context : map.entrySet()) {
      out.println(context.getKey());
      for (Map.Entry<String, List<TagsMetricsPair>> record : context.getValue().entrySet()) {
        indent(out, 1);
        out.println(record.getKey());
        for (TagsMetricsPair pair : record.getValue()) {
          indent(out, 2);
          // Prints tag values in the form "{key=value,key=value}:"
          out.print("{");
          boolean first = true;
          for (Map.Entry<String, Object> tagValue : pair.tagMap.entrySet()) {
            if (first) {
              first = false;
            } else {
              out.print(",");
            }
            out.print(tagValue.getKey());
            out.print("=");
            out.print(tagValue.getValue().toString());
          }
          out.println("}:");
          
          // Now print metric values, one per line
          for (Map.Entry<String, Number> metricValue : 
              pair.metricMap.entrySet()) {
            indent(out, 3);
            out.print(metricValue.getKey());
            out.print("=");
            out.println(metricValue.getValue().toString());
          }
        }
      }
    }    
  }
  
  private void indent(PrintWriter out, int indent) {
    for (int i = 0; i < indent; ++i) {
      out.append("  ");
    }
  }
}
