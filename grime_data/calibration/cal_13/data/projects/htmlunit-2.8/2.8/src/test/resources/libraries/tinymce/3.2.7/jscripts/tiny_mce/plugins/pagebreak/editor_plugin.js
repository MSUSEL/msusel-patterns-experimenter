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
(function(){tinymce.create("tinymce.plugins.PageBreakPlugin",{init:function(b,d){var f='<img src="'+d+'/img/trans.gif" class="mcePageBreak mceItemNoResize" />',a="mcePageBreak",c=b.getParam("pagebreak_separator","<!-- pagebreak -->"),e;e=new RegExp(c.replace(/[\?\.\*\[\]\(\)\{\}\+\^\$\:]/g,function(g){return"\\"+g}),"g");b.addCommand("mcePageBreak",function(){b.execCommand("mceInsertContent",0,f)});b.addButton("pagebreak",{title:"pagebreak.desc",cmd:a});b.onInit.add(function(){if(b.settings.content_css!==false){b.dom.loadCSS(d+"/css/content.css")}if(b.theme.onResolveName){b.theme.onResolveName.add(function(g,h){if(h.node.nodeName=="IMG"&&b.dom.hasClass(h.node,a)){h.name="pagebreak"}})}});b.onClick.add(function(g,h){h=h.target;if(h.nodeName==="IMG"&&g.dom.hasClass(h,a)){g.selection.select(h)}});b.onNodeChange.add(function(h,g,i){g.setActive("pagebreak",i.nodeName==="IMG"&&h.dom.hasClass(i,a))});b.onBeforeSetContent.add(function(g,h){h.content=h.content.replace(e,f)});b.onPostProcess.add(function(g,h){if(h.get){h.content=h.content.replace(/<img[^>]+>/g,function(i){if(i.indexOf('class="mcePageBreak')!==-1){i=c}return i})}})},getInfo:function(){return{longname:"PageBreak",author:"Moxiecode Systems AB",authorurl:"http://tinymce.moxiecode.com",infourl:"http://wiki.moxiecode.com/index.php/TinyMCE:Plugins/pagebreak",version:tinymce.majorVersion+"."+tinymce.minorVersion}}});tinymce.PluginManager.add("pagebreak",tinymce.plugins.PageBreakPlugin)})();