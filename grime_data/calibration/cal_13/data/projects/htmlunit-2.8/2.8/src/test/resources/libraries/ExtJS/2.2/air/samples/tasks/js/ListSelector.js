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
// Implementation class for created the tree powered form field
ListSelector = Ext.extend(Ext.ux.TreeSelector, {
	maxHeight:200,
	listenForLoad: false,
    initComponent : function(){
		
		this.tree = new Ext.tree.TreePanel({
			animate:false,
			border:false,
			width: this.treeWidth || 180,
			autoScroll:true,
			useArrows:true,
			selModel: new Ext.tree.ActivationModel(),
			loader : new ListLoader({store: this.store})		
		});
		
		var root = new Ext.tree.AsyncTreeNode({
	        text: 'All Lists',
			id: 'root',
			leaf: false,
			iconCls: 'icon-folder',
			expanded: true,
			isFolder: true
	    });
	    this.tree.setRootNode(root);

        this.tree.on('render', function(){
            this.store.bindTree(this.tree);
        }, this);
		
        ListSelector.superclass.initComponent.call(this);
		
		// selecting folders is not allowed, so filter them
		this.tree.getSelectionModel().on('beforeselect', this.beforeSelection, this);
		
		// if being rendered before the store is loaded, reload when it is loaded
		if(this.listenForLoad) {
			this.store.on('load', function(){
				root.reload();
			}, this, {
				single: true
			});
		}
    },
	
	beforeSelection : function(tree, node){
		if(node && node.attributes.isFolder){
			node.toggle();
			return false;
		}
	}
});
