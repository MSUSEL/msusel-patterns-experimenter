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
ListTree = function(config){
	ListTree.superclass.constructor.call(this, Ext.apply({
		id:'list-tree',
		animate:false,
		//rootVisible:false,
		region:'west',
		width:200,
		split:true,
		title:'My Lists',
		autoScroll:true,
		margins: '3 0 3 3',
		cmargins: '3 3 3 3',
		useArrows:true,
		collapsible:true,
		minWidth:120
	}, config));
	
	this.on('contextmenu', this.onContextMenu, this);
}
Ext.extend(ListTree, Ext.tree.TreePanel, {
	
	initComponent : function(){
		this.bbar = [
			tx.actions.newList, 
			tx.actions.deleteList, 
			'-', 
			tx.actions.newFolder,
			tx.actions.deleteFolder
		];
		
		this.loader = new ListLoader({
			store: tx.data.lists
		});
		ListTree.superclass.initComponent.call(this);
		
		var root = new Ext.tree.AsyncTreeNode({
	        text: 'All Lists',
			id: 'root',
			leaf: false,
			iconCls: 'icon-folder',
			expanded: true,
			isFolder: true,
			editable: false
	    });
	    this.setRootNode(root);
				
		this.editor = new Ext.tree.TreeEditor(this, {
	        allowBlank:false,
	        blankText:'A name is required',
	        selectOnFocus:true
	    });
        this.editor.shadow = false;

        this.editor.on('beforecomplete', function(ed, value, startValue){
			var node = ed.editNode;
			value = Ext.util.Format.htmlEncode(value);
			var r = this.store.getById(node.id);
			r.set('listName', value);
			//ed.editing = false;
            //ed.hide();
			//return false;
		}, this);
		
		this.sorter = new Ext.tree.TreeSorter(this, {
			folderSort: true
		});
	},
	
	getActiveFolderId : function(){
		var sm = this.selModel;
		var n = sm.getSelectedNode();
		if(n){
			return n.attributes.isFolder ? n.id : n.attributes.parentId;
		}
		return 'root';
	},
	
	onContextMenu : function(node, e){
        if(!this.menu){ // create context menu on first right click
            this.menu = new Ext.menu.Menu({
                id:'lists-ctx',
				listWidth: 200,
                items: [{
                    iconCls:'icon-edit',
                    text:'New Task',
                    scope: this,
                    handler:function(){
						this.ctxNode.select();
						tx.actions.newTask.execute();
                    }
                },{
                    iconCls:'icon-list-new',
                    text:'New List',
                    scope: this,
                    handler:function(){
						this.ctxNode.select();
						tx.actions.newList.execute();
                    }
                },{
                    iconCls:'icon-folder-new',
                    text:'New Folder',
                    scope: this,
                    handler:function(){
						this.ctxNode.select();
						tx.actions.newFolder.execute();
                    }
                },'-',{
					text:'Delete',
                    iconCls:'icon-list-delete',
                    scope: this,
                    handler:function(){
                        this.removeList(this.ctxNode);
                    }
                }]
            });
            this.menu.on('hide', this.onContextHide, this);
        }
        if(this.ctxNode){
            this.ctxNode.ui.removeClass('x-node-ctx');
            this.ctxNode = null;
        }
        this.ctxNode = node;
        this.ctxNode.ui.addClass('x-node-ctx');
		
		this.menu.items.get(1).setVisible(!!node.attributes.isFolder);
		this.menu.items.get(2).setVisible(!!node.attributes.isFolder);
		this.menu.items.get(0).setVisible(!node.attributes.isFolder);
		
		this.menu.showAt(e.getXY());
    },

    onContextHide : function(){
        if(this.ctxNode){
            this.ctxNode.ui.removeClass('x-node-ctx');
            this.ctxNode = null;
        }
    },
	
	startEdit : function(node, select){
		if(typeof node == 'string'){
			node = this.getNodeById(node);
		}
		if(select === true){
			node.select();
		}
		var ed = this.editor;
		setTimeout(function(){
			ed.editNode = node;
			ed.startEdit(node.ui.textNode);
		}, 10);
	},
	
	removeList : function(s){
		if (s && s.attributes.editable) {
			Ext.Msg.confirm('Confirm', 'Are you sure you want to delete "' + Ext.util.Format.htmlEncode(s.text) + '"?', function(btn){
				if (btn == 'yes') {
					if (s.nextSibling) {
						s.nextSibling.select();
					}
					else 
						if (s.previousSibling) {
							s.previousSibling.select();
						}
					s.parentNode.removeChild(s);
					tx.data.lists.remove(this.store.getById(s.id));
					tx.data.tasks.removeList(s.id);
				}
			}, this);
		}
	}
});

