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

tx.data.ListStore = Ext.extend(Ext.data.Store, {
	constructor: function(){
		tx.data.ListStore.superclass.constructor.call(this, {
	        sortInfo:{field: 'listName', direction: "ASC"},
	        reader: new Ext.data.JsonReader({
	            id: 'listId',
				fields: tx.data.List
	        })
	    });
		this.boundTrees = {};
	    this.conn = tx.data.conn;
	    this.proxy = new Ext.sql.Proxy(tx.data.conn, 'list', 'listId', this);
	},
	
    getName : function(id){
		var l = this.data.map[id];
		return l ? l.data.listName : '';
	},
	
	addList : function(name, id, isFolder, parentId){
		var l = this.findList(name);
		if(!l){
			var id = id || Ext.uniqueId();
			l = new tx.data.List({listId: id, listName: name, isFolder: isFolder === true, parentId: parentId || 'root'}, id);
			this.add(l);
		}
		return l;
	},
	
	newList : function(isFolder, parentId){
		var i = 1;
		var text = isFolder ? 'New Folder ' : 'New List '; 
		while(this.findList(text + i)){
			i++;
		}
		return this.addList(text + i, undefined, isFolder, parentId);
	},
	
	findList : function(name){
		var d = this.data;
		for(var i = 0, len = d.length; i < len; i++){
			if(d.items[i].data.listName === name){
				return d.items[i];
			}
		}
		return null;
	},
	
	loadDemoLists: function(){
		this.addList('Personal', 'personal', true, 'root');
		this.addList('Family', 'family', false, 'personal');
		this.addList('Bills', 'bills', false, 'personal');
		this.addList('Fun', 'fun', false, 'personal');
		this.addList('Other Stuff', 'personal-misc', false, 'personal');
		this.addList('Work', 'work', true, 'root');
		this.addList('Ext 2.x', 'ext2', false, 'work');
		this.addList('Ext 1.x', 'ext1', false, 'work');
		this.addList('Meetings', 'meetings', false, 'work');
		this.addList('Miscellaneous', 'work-misc', false, 'work');
	},
	
	bindTree : function(tree){
		this.boundTrees[tree.id] = {
			add: function(ls, records){
				var pnode = tree.getNodeById(records[0].data.parentId);
				if(pnode){
					pnode.reload();
				}
			},
			
			remove: function(ls, record){
				var node = tree.getNodeById(record.id);
				if(node && node.parentNode){
					node.parentNode.removeChild(node);
				}
			},
			
			update: function(ls, record){
				var node = tree.getNodeById(record.id);
				if(node){
					node.setText(record.data.listName);
				}
			}
		};
		
		this.on(this.boundTrees[tree.id]);
	},
	
	unbindTree : function(tree){
		var h = this.boundTrees[tree.id];
		if (h) {
			this.un('add', h.add);
			this.un('remove', h.remove);
			this.un('update', h.update);
		}
	},
	
	prepareTable : function(){
        try{
        this.createTable({
            name: 'list',
            key: 'listId',
            fields: tx.data.List.prototype.fields
        });
        }catch(e){console.log(e);}
    }
});
