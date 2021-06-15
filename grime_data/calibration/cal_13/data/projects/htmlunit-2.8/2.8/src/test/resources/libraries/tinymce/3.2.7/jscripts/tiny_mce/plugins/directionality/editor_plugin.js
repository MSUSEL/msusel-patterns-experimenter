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
(function(){tinymce.create("tinymce.plugins.Directionality",{init:function(a,b){var c=this;c.editor=a;a.addCommand("mceDirectionLTR",function(){var d=a.dom.getParent(a.selection.getNode(),a.dom.isBlock);if(d){if(a.dom.getAttrib(d,"dir")!="ltr"){a.dom.setAttrib(d,"dir","ltr")}else{a.dom.setAttrib(d,"dir","")}}a.nodeChanged()});a.addCommand("mceDirectionRTL",function(){var d=a.dom.getParent(a.selection.getNode(),a.dom.isBlock);if(d){if(a.dom.getAttrib(d,"dir")!="rtl"){a.dom.setAttrib(d,"dir","rtl")}else{a.dom.setAttrib(d,"dir","")}}a.nodeChanged()});a.addButton("ltr",{title:"directionality.ltr_desc",cmd:"mceDirectionLTR"});a.addButton("rtl",{title:"directionality.rtl_desc",cmd:"mceDirectionRTL"});a.onNodeChange.add(c._nodeChange,c)},getInfo:function(){return{longname:"Directionality",author:"Moxiecode Systems AB",authorurl:"http://tinymce.moxiecode.com",infourl:"http://wiki.moxiecode.com/index.php/TinyMCE:Plugins/directionality",version:tinymce.majorVersion+"."+tinymce.minorVersion}},_nodeChange:function(b,a,e){var d=b.dom,c;e=d.getParent(e,d.isBlock);if(!e){a.setDisabled("ltr",1);a.setDisabled("rtl",1);return}c=d.getAttrib(e,"dir");a.setActive("ltr",c=="ltr");a.setDisabled("ltr",0);a.setActive("rtl",c=="rtl");a.setDisabled("rtl",0)}});tinymce.PluginManager.add("directionality",tinymce.plugins.Directionality)})();