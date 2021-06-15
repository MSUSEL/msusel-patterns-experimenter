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
/*
	Copyright (c) 2004-2005, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/


dojo.provide("dojo.widget.EditorTreeContextMenu");
dojo.provide("dojo.widget.EditorTreeMenuItem");

dojo.require("dojo.event.*");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.Menu2");


dojo.widget.tags.addParseTreeHandler("dojo:EditorTreeContextMenu");
dojo.widget.tags.addParseTreeHandler("dojo:EditorTreeMenuItem");



dojo.widget.EditorTreeContextMenu = function() {
	dojo.widget.PopupMenu2.call(this);


}


dojo.inherits(dojo.widget.EditorTreeContextMenu, dojo.widget.PopupMenu2);

dojo.lang.extend(dojo.widget.EditorTreeContextMenu, {

	widgetType: "EditorTreeContextMenu",

	open: function(x, y, parentMenu, explodeSrc){

		var result = dojo.widget.PopupMenu2.prototype.open.apply(this, arguments);

		/* publish many events here about structural changes */
		dojo.event.topic.publish(this.eventNames.open, { menu:this });

		return result;
	},

	bindToTree: function(tree) {
		/* add context menu to all nodes that exist already */
		var nodes = tree.getDescendants();

		for(var i=0; i<nodes.length; i++) {
			if (!nodes[i].isTreeNode) continue;
			this.bindDomNode(nodes[i].labelNode);
		}


		/* bind context menu to all nodes that will be created in the future (e.g loaded from server)*/
		var _this = this;
		dojo.event.topic.subscribe(tree.eventNames.nodeCreate,
			function(message) { _this.bindDomNode(message.source.labelNode); }
		);
	}




});






dojo.widget.EditorTreeMenuItem = function() {
	dojo.widget.MenuItem2.call(this);

}


dojo.inherits(dojo.widget.EditorTreeMenuItem, dojo.widget.MenuItem2);


dojo.lang.extend(dojo.widget.EditorTreeMenuItem, {

	widgetType: "EditorTreeMenuItem",

	// treeActions menu item performs following actions (to be checked for permissions)
	treeActions: "",

	initialize: function(args, frag) {

		this.treeActions = this.treeActions.split(",");
		for(var i=0; i<this.treeActions.length; i++) {
			this.treeActions[i] = this.treeActions[i].toUpperCase();
		}

	},

	getTreeNode: function() {
		var menu = this;

		while (! (menu instanceof dojo.widget.EditorTreeContextMenu) ) {
			menu = menu.parent;
		}

		var source = menu.getTopOpenEvent().target;

		while (!source.getAttribute('treeNode') && source.tagName != 'body') {
			source = source.parentNode;
		}
		if (source.tagName == 'body') {
			dojo.raise("treeNode not detected");
		}
		var treeNode = dojo.widget.manager.getWidgetById(source.getAttribute('treeNode'));

		return treeNode;
	},


	menuOpen: function(message) {
		var treeNode = this.getTreeNode();

		this.setDisabled(false); // enable by default

		var _this = this;
		dojo.lang.forEach(_this.treeActions,
			function(action) {
				_this.setDisabled( treeNode.actionIsDisabled(action) );
			}
		);

	},

	toString: function() {
		return "["+this.widgetType+" node "+this.getTreeNode()+"]";
	}

});


