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
(function(){var a=tinymce.dom.Event,c=tinymce.each,b=tinymce.DOM;tinymce.create("tinymce.plugins.ContextMenu",{init:function(d){var f=this;f.editor=d;f.onContextMenu=new tinymce.util.Dispatcher(this);d.onContextMenu.add(function(g,h){if(!h.ctrlKey){f._getMenu(g).showMenu(h.clientX,h.clientY);a.add(g.getDoc(),"click",e);a.cancel(h)}});function e(){if(f._menu){f._menu.removeAll();f._menu.destroy();a.remove(d.getDoc(),"click",e)}}d.onMouseDown.add(e);d.onKeyDown.add(e)},getInfo:function(){return{longname:"Contextmenu",author:"Moxiecode Systems AB",authorurl:"http://tinymce.moxiecode.com",infourl:"http://wiki.moxiecode.com/index.php/TinyMCE:Plugins/contextmenu",version:tinymce.majorVersion+"."+tinymce.minorVersion}},_getMenu:function(h){var l=this,f=l._menu,i=h.selection,e=i.isCollapsed(),d=i.getNode()||h.getBody(),g,k,j;if(f){f.removeAll();f.destroy()}k=b.getPos(h.getContentAreaContainer());j=b.getPos(h.getContainer());f=h.controlManager.createDropMenu("contextmenu",{offset_x:k.x+h.getParam("contextmenu_offset_x",0),offset_y:k.y+h.getParam("contextmenu_offset_y",0),constrain:1});l._menu=f;f.add({title:"advanced.cut_desc",icon:"cut",cmd:"Cut"}).setDisabled(e);f.add({title:"advanced.copy_desc",icon:"copy",cmd:"Copy"}).setDisabled(e);f.add({title:"advanced.paste_desc",icon:"paste",cmd:"Paste"});if((d.nodeName=="A"&&!h.dom.getAttrib(d,"name"))||!e){f.addSeparator();f.add({title:"advanced.link_desc",icon:"link",cmd:h.plugins.advlink?"mceAdvLink":"mceLink",ui:true});f.add({title:"advanced.unlink_desc",icon:"unlink",cmd:"UnLink"})}f.addSeparator();f.add({title:"advanced.image_desc",icon:"image",cmd:h.plugins.advimage?"mceAdvImage":"mceImage",ui:true});f.addSeparator();g=f.addMenu({title:"contextmenu.align"});g.add({title:"contextmenu.left",icon:"justifyleft",cmd:"JustifyLeft"});g.add({title:"contextmenu.center",icon:"justifycenter",cmd:"JustifyCenter"});g.add({title:"contextmenu.right",icon:"justifyright",cmd:"JustifyRight"});g.add({title:"contextmenu.full",icon:"justifyfull",cmd:"JustifyFull"});l.onContextMenu.dispatch(l,f,d,e);return f}});tinymce.PluginManager.add("contextmenu",tinymce.plugins.ContextMenu)})();