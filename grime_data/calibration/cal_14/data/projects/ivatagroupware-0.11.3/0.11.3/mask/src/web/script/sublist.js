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
////////////////////////////////////////////////////////////////////////////////
//
// This script is useful for adding and removing sublist contents.
//
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2001 - 2005 ivata limited.
// All rights reserved.
// =========================================================
// ivata groupware may be redistributed under the GNU General Public
// License as published by the Free Software Foundation;
// version 2 of the License.
//
// These programs are free software; you can redistribute them and/or
// modify them under the terms of the GNU General Public License
// as published by the Free Software Foundation; version 2 of the License.
//
// These programs are distributed in the hope that they will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//
// See the GNU General Public License in the file LICENSE.txt for more
// details.
//
// If you would like a copy of the GNU General Public License write to
//
// Free Software Foundation, Inc.
// 59 Temple Place - Suite 330
// Boston, MA 02111-1307, USA.
//
//
// To arrange commercial support and licensing, contact ivata at
//                  http://www.ivata.com/contact.jsp
//
////////////////////////////////////////////////////////////////////////////////
//
// $Log: sublist.js,v $
// Revision 1.2  2005/04/09 17:19:51  colinmacleod
// Changed copyright text to GPL v2 explicitly.
//
// Revision 1.1.1.1  2005/03/10 17:49:57  colinmacleod
// Restructured ivata op around Hibernate/PicoContainer.
// Renamed ivata groupware.
//
// Revision 1.1  2004/12/29 14:09:33  colinmacleod
// Changed subproject name from masks to mask
//
// Revision 1.1  2004/11/11 13:22:20  colinmacleod
// Split off javascript from layout.jsp.
//
////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// extract all the text from a child node
function getText(node) {
    var text = "";
    for (var i = 0; i < node.childNodes.length; ++i) {
        var childNode = node.childNodes.item(i);
        text = text + childNode.nodeValue;
    }
    return text;
}
////////////////////////////////////////////////////////////////////////////////
// this method locates a value object element in the xml by its id
function findValueObject(root, id) {
    // get all the value object elements
    var valueObjects = root.getElementsByTagName("valueObject");
    var found = false;
    // look for the currently selected one
    for (var valueObjectIndex = 0; valueObjectIndex <  valueObjects.length; ++valueObjectIndex) {
        var valueObject = valueObjects.item(valueObjectIndex);
        var thisId = valueObject.getAttribute("id");
        if (thisId == null) {
            thisId = ''
        }
        if (thisId != id) {
            continue;
        }
        return valueObject;
    }
    return null;
}
////////////////////////////////////////////////////////////////////////////////
// this method is called when a sublist's value changes
function onChangeSublist(name) {
    var sublist = document.getElementById(name + "_sublist");
    var hidden = document.getElementById(name);
    var index = document.getElementById(name + "_index");
    // parse the xml
    var parser = new DOMImplementation();
    var domDocument = parser.loadXML(hidden.value);
    // get the root element
    var root = domDocument.getDocumentElement();

    var valueObjects = root.getElementsByTagName("valueObject");
    var valueObject = valueObjects.item(sublist.selectedIndex);
    if (valueObject != null) {
        var idField = document.getElementById(name + "_id");
        idField.value = sublist.options[sublist.selectedIndex].value;
        index.value = sublist.selectedIndex;

        // go thro' all the fields and set the values in the fields
        var fields = valueObject.getElementsByTagName("field");
        for (var fieldIndex = 0; fieldIndex <  fields.length; ++fieldIndex) {
            var field = fields.item(fieldIndex);
            var fieldName = name + "_" + field.getAttribute("id");
            var fieldControl = document.getElementById(fieldName);
            // see if this is a select
            var value = getText(field);
            if (fieldControl.options != null) {
                for (var optionIndex = 0; optionIndex < fieldControl.options.length; ++optionIndex) {
                    if (fieldControl.options[optionIndex].value == value) {
                        fieldControl.options[optionIndex].selected = true;
                    } else {
                        fieldControl.options[optionIndex].selected = false;
                    }
                }
            } else {
                fieldControl.value = value;
            }
        }
    }

    // if there is no id, we're adding something, otherwise we're amending
    // TODO i18n
    var confirmButton = document.getElementById(name + "_confirm");
    var removeButton = document.getElementById(name + "_remove");
    if ((index.value == '')
            || (index.value ==  (sublist.options.length - 1))) {
        confirmButton.value = "Add";
        // you can't remove a new entry
        removeButton.disabled = true;
    } else {
        confirmButton.value = "Amend";
        removeButton.disabled = false;
    }
}
////////////////////////////////////////////////////////////////////////////////
// this method is called when the confirm (add/amend) button is pressed
function onConfirm(name) {
    var sublist = document.getElementById(name + "_sublist");
    var id = document.getElementById(name + "_id");
    var index = document.getElementById(name + "_index");
    var hidden = document.getElementById(name);
    // parse the xml
    var parser = new DOMImplementation();
    var domDocument = parser.loadXML(hidden.value);
    // get the root element
    var root = domDocument.getDocumentElement();

    // default to the [New] option, if none is selected...
    if (index.value == '') {
        index.value = sublist.options.length - 1;
        sublist.selectedIndex = index.value;
    }

    // is this a new entry ?
    var valueObjects = root.getElementsByTagName("valueObject");
    var valueObject = valueObjects.item(index.value);
    // oops...
    if (valueObject == null) {
        alert("Error: could not locate item with id '" + id.value + "'");
        return;
    }

    var fields = valueObject.getElementsByTagName("field");

    // if this is a new item, add an empty value object for the next
    // new one
    if (index.value == (sublist.options.length - 1)) {
        var newValueObject = valueObject.cloneNode(true);
        // this value object should not be marked as a schema (as is the last
        // one)
        valueObject.removeAttribute("schema");
        root.appendChild(newValueObject);
        valueObject = newValueObject;
        index.value = sublist.options.length - 1;
        // add a new option to the end of the list which is a copy of the last
        // list option, the [New] option
        var text = sublist.options[index.value].text;
        var value = sublist.options[index.value].value;
        sublist.options[sublist.length] = new Option(text, value);
    }

    // go thro' all the fields and update the values...
    var displayValue = "";
    for (var fieldIndex = 0; fieldIndex <  fields.length; ++fieldIndex) {
        if (displayValue != "") {
            displayValue += " ";
        }
        var field = fields.item(fieldIndex);
        var fieldName = name + "_" + field.getAttribute("id");
        var fieldControl = document.getElementById(fieldName);
        // see if this is a select
        var value;
        if (fieldControl.options != null) {
            value = fieldControl.options[fieldControl.selectedIndex].value
            displayValue += fieldControl.options[fieldControl.selectedIndex].text;
        } else {
            value = fieldControl.value;
            displayValue += fieldControl.value;
        }
        if (field.childNodes.length == 1) {
            field.childNodes.item(0).setNodeValue(value);
        } else {
            // remove all previous contents
            for (var childIndex = field.childNodes.getLength() -1; childIndex >= 0; --childIndex) {
                field.removeChild(field.childNodes.item(childIndex));
            }
            // add a text node with the right value
            field.appendChild(domDocument.createTextNode(value));
        }
    }
    sublist.options[index.value].text = displayValue;
    hidden.value = root;
    sublist.selectedIndex = index.value;
    onChangeSublist(name);
}
////////////////////////////////////////////////////////////////////////////////
// this method is called when the confirm (add/amend) button is pressed
function onRemove(name) {
    var sublist = document.getElementById(name + "_sublist");
    var id = document.getElementById(name + "_id");
    var index = document.getElementById(name + "_index");
    var hidden = document.getElementById(name);

    // sanity check - this should not really happen, as the button should be
    // disabled in this case
    if ((index.value == '')
            || (index.value == (sublist.options.length - 1))) {
        alert("Please select an item to delete first.");
    }

    // parse the xml
    var parser = new DOMImplementation();
    var domDocument = parser.loadXML(hidden.value);
    // get the root element
    var root = domDocument.getDocumentElement();

    // remove the item from the combo list
    sublist.options[index.value] = null;

    // now get the value object to be deleted
    var valueObjects = root.getElementsByTagName("valueObject");
    var valueObject = valueObjects.item(index.value);
    root.removeChild(valueObject);
    hidden.value = root;

    // default to the [New] option
    index.value = sublist.options.length - 1;
    sublist.selectedIndex = index.value;
    onChangeSublist(name);
}
////////////////////////////////////////////////////////////////////////////////

