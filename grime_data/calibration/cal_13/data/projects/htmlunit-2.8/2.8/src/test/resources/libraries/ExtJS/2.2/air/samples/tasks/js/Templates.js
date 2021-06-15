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
// In AIR, XTemplates must be created at load time
Templates = {
	categoryCombo: new Ext.XTemplate(
		'<tpl for="."><div class="x-combo-list-item">{listName}</div></tpl>'
	),
	timeField: new Ext.XTemplate(
		'<tpl for="."><div class="x-combo-list-item">{text}</div></tpl>'
	),

	gridHeader : new Ext.Template(
        '<table border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
        '<thead><tr class="x-grid3-hd-row">{cells}</tr></thead>',
        '<tbody><tr class="new-task-row">',
            '<td><div id="new-task-icon"></div></td>',
            '<td><div class="x-small-editor" id="new-task-title"></div></td>',
            '<td><div class="x-small-editor" id="new-task-cat"></div></td>',
            '<td><div class="x-small-editor" id="new-task-due"></div></td>',
        '</tr></tbody>',
        "</table>"
    )
};
