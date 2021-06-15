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
function init() {
	tinyMCEPopup.resizeToInnerSize();
	var inst = tinyMCEPopup.editor;
	var dom = inst.dom;
	var elm = inst.selection.getNode();
	var f = document.forms[0];
	var onclick = dom.getAttrib(elm, 'onclick');

	setFormValue('title', dom.getAttrib(elm, 'title'));
	setFormValue('id', dom.getAttrib(elm, 'id'));
	setFormValue('style', dom.getAttrib(elm, "style"));
	setFormValue('dir', dom.getAttrib(elm, 'dir'));
	setFormValue('lang', dom.getAttrib(elm, 'lang'));
	setFormValue('tabindex', dom.getAttrib(elm, 'tabindex', typeof(elm.tabindex) != "undefined" ? elm.tabindex : ""));
	setFormValue('accesskey', dom.getAttrib(elm, 'accesskey', typeof(elm.accesskey) != "undefined" ? elm.accesskey : ""));
	setFormValue('onfocus', dom.getAttrib(elm, 'onfocus'));
	setFormValue('onblur', dom.getAttrib(elm, 'onblur'));
	setFormValue('onclick', onclick);
	setFormValue('ondblclick', dom.getAttrib(elm, 'ondblclick'));
	setFormValue('onmousedown', dom.getAttrib(elm, 'onmousedown'));
	setFormValue('onmouseup', dom.getAttrib(elm, 'onmouseup'));
	setFormValue('onmouseover', dom.getAttrib(elm, 'onmouseover'));
	setFormValue('onmousemove', dom.getAttrib(elm, 'onmousemove'));
	setFormValue('onmouseout', dom.getAttrib(elm, 'onmouseout'));
	setFormValue('onkeypress', dom.getAttrib(elm, 'onkeypress'));
	setFormValue('onkeydown', dom.getAttrib(elm, 'onkeydown'));
	setFormValue('onkeyup', dom.getAttrib(elm, 'onkeyup'));
	className = dom.getAttrib(elm, 'class');

	addClassesToList('classlist', 'advlink_styles');
	selectByValue(f, 'classlist', className, true);

	TinyMCE_EditableSelects.init();
}

function setFormValue(name, value) {
	if(value && document.forms[0].elements[name]){
		document.forms[0].elements[name].value = value;
	}
}

function insertAction() {
	var inst = tinyMCEPopup.editor;
	var elm = inst.selection.getNode();

	tinyMCEPopup.execCommand("mceBeginUndoLevel");	
	setAllAttribs(elm);
	tinyMCEPopup.execCommand("mceEndUndoLevel");
	tinyMCEPopup.close();
}

function setAttrib(elm, attrib, value) {
	var formObj = document.forms[0];
	var valueElm = formObj.elements[attrib.toLowerCase()];
	var inst = tinyMCEPopup.editor;
	var dom = inst.dom;

	if (typeof(value) == "undefined" || value == null) {
		value = "";

		if (valueElm)
			value = valueElm.value;
	}

	if (value != "") {
		dom.setAttrib(elm, attrib.toLowerCase(), value);

		if (attrib == "style")
			attrib = "style.cssText";

		if (attrib.substring(0, 2) == 'on')
			value = 'return true;' + value;

		if (attrib == "class")
			attrib = "className";

		elm[attrib]=value;
	} else
		elm.removeAttribute(attrib);
}

function setAllAttribs(elm) {
	var f = document.forms[0];

	setAttrib(elm, 'title');
	setAttrib(elm, 'id');
	setAttrib(elm, 'style');
	setAttrib(elm, 'class', getSelectValue(f, 'classlist'));
	setAttrib(elm, 'dir');
	setAttrib(elm, 'lang');
	setAttrib(elm, 'tabindex');
	setAttrib(elm, 'accesskey');
	setAttrib(elm, 'onfocus');
	setAttrib(elm, 'onblur');
	setAttrib(elm, 'onclick');
	setAttrib(elm, 'ondblclick');
	setAttrib(elm, 'onmousedown');
	setAttrib(elm, 'onmouseup');
	setAttrib(elm, 'onmouseover');
	setAttrib(elm, 'onmousemove');
	setAttrib(elm, 'onmouseout');
	setAttrib(elm, 'onkeypress');
	setAttrib(elm, 'onkeydown');
	setAttrib(elm, 'onkeyup');

	// Refresh in old MSIE
//	if (tinyMCE.isMSIE5)
//		elm.outerHTML = elm.outerHTML;
}

function insertAttribute() {
	tinyMCEPopup.close();
}

tinyMCEPopup.onInit.add(init);
tinyMCEPopup.requireLangPack();
