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
(function(){var a=tinymce.dom.Event;tinymce.create("tinymce.plugins.NonEditablePlugin",{init:function(d,e){var f=this,c,b;f.editor=d;c=d.getParam("noneditable_editable_class","mceEditable");b=d.getParam("noneditable_noneditable_class","mceNonEditable");d.onNodeChange.addToTop(function(h,g,k){var j,i;j=h.dom.getParent(h.selection.getStart(),function(l){return h.dom.hasClass(l,b)});i=h.dom.getParent(h.selection.getEnd(),function(l){return h.dom.hasClass(l,b)});if(j||i){f._setDisabled(1);return false}else{f._setDisabled(0)}})},getInfo:function(){return{longname:"Non editable elements",author:"Moxiecode Systems AB",authorurl:"http://tinymce.moxiecode.com",infourl:"http://wiki.moxiecode.com/index.php/TinyMCE:Plugins/noneditable",version:tinymce.majorVersion+"."+tinymce.minorVersion}},_block:function(c,d){var b=d.keyCode;if((b>32&&b<41)||(b>111&&b<124)){return}return a.cancel(d)},_setDisabled:function(d){var c=this,b=c.editor;tinymce.each(b.controlManager.controls,function(e){e.setDisabled(d)});if(d!==c.disabled){if(d){b.onKeyDown.addToTop(c._block);b.onKeyPress.addToTop(c._block);b.onKeyUp.addToTop(c._block);b.onPaste.addToTop(c._block)}else{b.onKeyDown.remove(c._block);b.onKeyPress.remove(c._block);b.onKeyUp.remove(c._block);b.onPaste.remove(c._block)}c.disabled=d}}});tinymce.PluginManager.add("noneditable",tinymce.plugins.NonEditablePlugin)})();