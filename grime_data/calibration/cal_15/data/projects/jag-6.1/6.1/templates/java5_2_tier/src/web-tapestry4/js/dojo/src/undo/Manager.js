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

dojo.provide("dojo.undo.Manager");
dojo.require("dojo.lang");

dojo.undo.Manager = function(parent) {
	this.clear();
	this._parent = parent;
};
dojo.lang.extend(dojo.undo.Manager, {
	_parent: null,
	_undoStack: null,
	_redoStack: null,
	_currentManager: null,

	canUndo: false,
	canRedo: false,

	isUndoing: false,
	isRedoing: false,

	// these events allow you to hook in and update your code (UI?) as necessary
	onUndo: function(manager, item) {},
	onRedo: function(manager, item) {},

	// fired when you do *any* undo action, which means you'll have one for every item
	// in a transaction. this is usually only useful for debugging
	onUndoAny: function(manager, item) {},
	onRedoAny: function(manager, item) {},

	_updateStatus: function() {
		this.canUndo = this._undoStack.length > 0;
		this.canRedo = this._redoStack.length > 0;
	},

	clear: function() {
		this._undoStack = [];
		this._redoStack = [];
		this._currentManager = this;

		this.isUndoing = false;
		this.isRedoing = false;

		this._updateStatus();
	},

	undo: function() {
		if(!this.canUndo) { return false; }

		this.endAllTransactions();

		this.isUndoing = true;
		var top = this._undoStack.pop();
		if(top instanceof this.constructor) {
			top.undoAll();
		} else {
			top.undo();
		}
		if(top.redo) {
			this._redoStack.push(top);
		}
		this.isUndoing = false;

		this._updateStatus();
		this.onUndo(this, top);
		if(!(top instanceof this.constructor)) {
			this.getTop().onUndoAny(this, top);
		}
		return true;
	},

	redo: function() {
		if(!this.canRedo) { return false; }

		this.isRedoing = true;
		var top = this._redoStack.pop();
		if(top instanceof this.constructor) {
			top.redoAll();
		} else {
			top.redo();
		}
		this._undoStack.push(top);
		this.isRedoing = false;

		this._updateStatus();
		this.onRedo(this, top);
		if(!(top instanceof this.constructor)) {
			this.getTop().onRedoAny(this, top);
		}
		return true;
	},

	undoAll: function() {
		while(this._undoStack.length > 0) {
			this.undo();
		}
	},

	redoAll: function() {
		while(this._redoStack.length > 0) {
			this.redo();
		}
	},

	push: function(undo, redo /* optional */, description /* optional */) {
		if(!undo) { return; }

		if(this._currentManager == this) {
			this._undoStack.push({
				undo: undo,
				redo: redo,
				description: description
			});
		} else {
			this._currentManager.push.apply(this._currentManager, arguments);
		}
		// adding a new undo-able item clears out the redo stack
		this._redoStack = [];
		this._updateStatus();
	},

	beginTransaction: function() {
		if(this._currentManager == this) {
			var mgr = new dojo.undo.Manager(this);
			this._undoStack.push(mgr);
			this._currentManager = mgr;
		} else {
			this._currentManager.beginTransaction.apply(this._currentManager, arguments);
		}
	},

	endTransaction: function() {
		if(this._currentManager == this) {
			if(this._parent) {
				this._parent._currentManager = this._parent;
				if(this._undoStack.length == 0) {
					// don't leave empty transactions hangin' around
					var idx = dojo.lang.find(this._parent._undoStack, this);
					if(idx >= 0) {
						this._parent._undoStack.splice(idx, 1);
					}
				}
			}
		} else {
			this._currentManager.endTransaction.apply(this._currentManager, arguments);
		}
	},

	endAllTransactions: function() {
		while(this._currentManager != this) {
			this.endTransaction();
		}
	},

	// find the top parent of an undo manager
	getTop: function() {
		if(this._parent) {
			return this._parent.getTop();
		} else {
			return this;
		}
	}
});
