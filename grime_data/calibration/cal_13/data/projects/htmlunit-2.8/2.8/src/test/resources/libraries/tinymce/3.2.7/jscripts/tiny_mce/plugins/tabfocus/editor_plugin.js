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
(function(){var c=tinymce.DOM,a=tinymce.dom.Event,d=tinymce.each,b=tinymce.explode;tinymce.create("tinymce.plugins.TabFocusPlugin",{init:function(f,g){function e(i,j){if(j.keyCode===9){return a.cancel(j)}}function h(l,p){var j,m,o,n,k;function q(i){o=c.getParent(l.id,"form");n=o.elements;if(o){d(n,function(s,r){if(s.id==l.id){j=r;return false}});if(i>0){for(m=j+1;m<n.length;m++){if(n[m].type!="hidden"){return n[m]}}}else{for(m=j-1;m>=0;m--){if(n[m].type!="hidden"){return n[m]}}}}return null}if(p.keyCode===9){k=b(l.getParam("tab_focus",l.getParam("tabfocus_elements",":prev,:next")));if(k.length==1){k[1]=k[0];k[0]=":prev"}if(p.shiftKey){if(k[0]==":prev"){n=q(-1)}else{n=c.get(k[0])}}else{if(k[1]==":next"){n=q(1)}else{n=c.get(k[1])}}if(n){if(l=tinymce.EditorManager.get(n.id||n.name)){l.focus()}else{window.setTimeout(function(){window.focus();n.focus()},10)}return a.cancel(p)}}}f.onKeyUp.add(e);if(tinymce.isGecko){f.onKeyPress.add(h);f.onKeyDown.add(e)}else{f.onKeyDown.add(h)}f.onInit.add(function(){d(c.select("a:first,a:last",f.getContainer()),function(i){a.add(i,"focus",function(){f.focus()})})})},getInfo:function(){return{longname:"Tabfocus",author:"Moxiecode Systems AB",authorurl:"http://tinymce.moxiecode.com",infourl:"http://wiki.moxiecode.com/index.php/TinyMCE:Plugins/tabfocus",version:tinymce.majorVersion+"."+tinymce.minorVersion}}});tinymce.PluginManager.add("tabfocus",tinymce.plugins.TabFocusPlugin)})();