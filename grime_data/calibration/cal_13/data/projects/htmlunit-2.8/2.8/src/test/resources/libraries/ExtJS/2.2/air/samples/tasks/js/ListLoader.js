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
ListLoader = function(config){
	Ext.apply(this, config);
};

Ext.extend(ListLoader, Ext.util.Observable, {
	keyAttribute: 'id',
	keyField: 'parentId',
	
	load: function(node, callback){
		var key = this.keyField;
		var v = node.attributes[this.keyAttribute];
		var rs = this.store.queryBy(function(r){
			return r.data[key] === v;
		});
		node.beginUpdate();
        for (var i = 0, d = rs.items, len = d.length; i < len; i++) {
			var n = this.createNode(d[i]);
			if (n) {
				node.appendChild(n);
			}
		}
		node.endUpdate();
		if(typeof callback == "function"){
            callback(this, node);
        }
	},
	
	createNode : function(record){
		var d = record.data, n;
		if(d.isFolder){
			n = new Ext.tree.AsyncTreeNode({
				loader: this,
				id: record.id,
				text: d.listName,
				leaf: false,
				iconCls: 'icon-folder',
				editable: true,
				expanded: true,
				isFolder: true
			});
		}else{
			n = new Ext.tree.TreeNode({
				id: record.id,
				text: d.listName,
				leaf: true,
				iconCls: 'icon-list',
				editable: true
			});
		}
		return n;
	}
});
