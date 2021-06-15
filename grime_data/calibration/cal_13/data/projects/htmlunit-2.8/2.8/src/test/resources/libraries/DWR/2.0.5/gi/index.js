/*
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

function giLoaded() {
  OpenAjax.subscribe("gidemo", "corporation", objectPublished);
  dwr.engine.setActiveReverseAjax(true);
}

function objectPublished(prefix, name, handlerData, corporation) {
  var matrix = giApp.getJSXByName("matrix");
  var inserted = matrix.getRecordNode(corporation.jsxid);
  matrix.insertRecord(corporation, null, inserted == null);

  // There are many ways to get a table to repaint.
  // One easy way is to ask it to repaint:
  // matrix.repaintData();

  // But we are going for a fancy option that does highlighting
  for (var property in corporation) {
    if (property != "jsxid") {
      var ox = matrix.getContentElement(corporation.jsxid, property);
      if (ox) {
        var current = ox.innerHTML;
        if (current != "" + corporation[property]) {
          ox.style.backgroundColor = "#FDE4EB";
          var callback = function(ele) {
            return function() { ele.style.backgroundColor = "#FFFFFF"; };
          }(ox);
          setTimeout(callback, 1000);
          ox.innerHTML = corporation[property];
        }
      }
    }
  }
}
