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
(function(){tinymce.create("tinymce.plugins.AutoResizePlugin",{init:function(a,c){var d=this;if(a.getParam("fullscreen_is_enabled")){return}function b(){var h=a.getDoc(),e=h.body,j=h.documentElement,g=tinymce.DOM,i=d.autoresize_min_height,f;f=tinymce.isIE?e.scrollHeight:j.offsetHeight;if(f>d.autoresize_min_height){i=f}g.setStyle(g.get(a.id+"_ifr"),"height",i+"px");if(d.throbbing){a.setProgressState(false);a.setProgressState(true)}}d.editor=a;d.autoresize_min_height=a.getElement().offsetHeight;a.onInit.add(function(f,e){f.setProgressState(true);d.throbbing=true;f.getBody().style.overflowY="hidden"});a.onChange.add(b);a.onSetContent.add(b);a.onPaste.add(b);a.onKeyUp.add(b);a.onPostRender.add(b);a.onLoadContent.add(function(f,e){b();setTimeout(function(){b();f.setProgressState(false);d.throbbing=false},1250)});a.addCommand("mceAutoResize",b)},getInfo:function(){return{longname:"Auto Resize",author:"Moxiecode Systems AB",authorurl:"http://tinymce.moxiecode.com",infourl:"http://wiki.moxiecode.com/index.php/TinyMCE:Plugins/autoresize",version:tinymce.majorVersion+"."+tinymce.minorVersion}}});tinymce.PluginManager.add("autoresize",tinymce.plugins.AutoResizePlugin)})();