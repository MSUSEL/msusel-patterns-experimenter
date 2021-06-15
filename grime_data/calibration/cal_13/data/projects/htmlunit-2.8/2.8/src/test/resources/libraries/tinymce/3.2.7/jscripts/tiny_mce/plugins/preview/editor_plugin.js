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
(function(){tinymce.create("tinymce.plugins.Preview",{init:function(a,b){var d=this,c=tinymce.explode(a.settings.content_css);d.editor=a;tinymce.each(c,function(f,e){c[e]=a.documentBaseURI.toAbsolute(f)});a.addCommand("mcePreview",function(){a.windowManager.open({file:a.getParam("plugin_preview_pageurl",b+"/preview.html"),width:parseInt(a.getParam("plugin_preview_width","550")),height:parseInt(a.getParam("plugin_preview_height","600")),resizable:"yes",scrollbars:"yes",popup_css:c?c.join(","):a.baseURI.toAbsolute("themes/"+a.settings.theme+"/skins/"+a.settings.skin+"/content.css"),inline:a.getParam("plugin_preview_inline",1)},{base:a.documentBaseURI.getURI()})});a.addButton("preview",{title:"preview.preview_desc",cmd:"mcePreview"})},getInfo:function(){return{longname:"Preview",author:"Moxiecode Systems AB",authorurl:"http://tinymce.moxiecode.com",infourl:"http://wiki.moxiecode.com/index.php/TinyMCE:Plugins/preview",version:tinymce.majorVersion+"."+tinymce.minorVersion}}});tinymce.PluginManager.add("preview",tinymce.plugins.Preview)})();