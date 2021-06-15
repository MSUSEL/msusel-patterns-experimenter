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
(function(){tinymce.create("tinymce.plugins.Save",{init:function(a,b){var c=this;c.editor=a;a.addCommand("mceSave",c._save,c);a.addCommand("mceCancel",c._cancel,c);a.addButton("save",{title:"save.save_desc",cmd:"mceSave"});a.addButton("cancel",{title:"save.cancel_desc",cmd:"mceCancel"});a.onNodeChange.add(c._nodeChange,c);a.addShortcut("ctrl+s",a.getLang("save.save_desc"),"mceSave")},getInfo:function(){return{longname:"Save",author:"Moxiecode Systems AB",authorurl:"http://tinymce.moxiecode.com",infourl:"http://wiki.moxiecode.com/index.php/TinyMCE:Plugins/save",version:tinymce.majorVersion+"."+tinymce.minorVersion}},_nodeChange:function(b,a,c){var b=this.editor;if(b.getParam("save_enablewhendirty")){a.setDisabled("save",!b.isDirty());a.setDisabled("cancel",!b.isDirty())}},_save:function(){var c=this.editor,a,e,d,b;a=tinymce.DOM.get(c.id).form||tinymce.DOM.getParent(c.id,"form");if(c.getParam("save_enablewhendirty")&&!c.isDirty()){return}tinyMCE.triggerSave();if(e=c.getParam("save_onsavecallback")){if(c.execCallback("save_onsavecallback",c)){c.startContent=tinymce.trim(c.getContent({format:"raw"}));c.nodeChanged()}return}if(a){c.isNotDirty=true;if(a.onsubmit==null||a.onsubmit()!=false){a.submit()}c.nodeChanged()}else{c.windowManager.alert("Error: No form element found.")}},_cancel:function(){var a=this.editor,c,b=tinymce.trim(a.startContent);if(c=a.getParam("save_oncancelcallback")){a.execCallback("save_oncancelcallback",a);return}a.setContent(b);a.undoManager.clear();a.nodeChanged()}});tinymce.PluginManager.add("save",tinymce.plugins.Save)})();