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

function Tabs() { }

//Tabs.removeSelectedRegex = new RegExp("(^|\\s)linkSelected(\\s|$)", 'g');
//Tabs.removeUnselectedRegex = new RegExp("(^|\\s)linkUnselected(\\s|$)", 'g');

/**
 * 
 */
Tabs.init = function(tabListId) {
  Tabs.tabLinks = document.getElementById(tabListId).getElementsByTagName("A");

  var link, tabId, tab;
  for (var i = 0; i < Tabs.tabLinks.length; i++) {
    link = Tabs.tabLinks[i];
    tabId = link.getAttribute("tabId");
    if (!tabId) alert("Expand link does not have a tabId element: " + link.innerHTML);
    tab = document.getElementById(tabId);
    if (!tab) alert("tabId does not exist: " + tabId);

    if (i == 0) {
      tab.style.display = "block";
      link.className = "linkSelected " ;//+ link.className.replace(Tabs.removeUnselectedRegex, '');
    }
    else {
      tab.style.display = "none";
      link.className = "linkUnselected " ;//+ link.className.replace(Tabs.removeSelectedRegex, '');
    }

    link.onclick = function() {
      var tabId = this.getAttribute("tabId");
      for (var i = 0; i < Tabs.tabLinks.length; i++) {
        var link = Tabs.tabLinks[i];
        var loopId = link.getAttribute("tabId");
        if (loopId == tabId) {
          document.getElementById(loopId).style.display = "block";
          link.className = "linkSelected " ;//+ link.className.replace(Tabs.removeUnselectedRegex, '');
        }
        else {
          document.getElementById(loopId).style.display = "none";
          link.className = "linkUnselected " ;//+ link.className.replace(Tabs.removeSelectedRegex, '');
        }
      }
      if (this.blur) this.blur();
      return false;
    }
  }
}
