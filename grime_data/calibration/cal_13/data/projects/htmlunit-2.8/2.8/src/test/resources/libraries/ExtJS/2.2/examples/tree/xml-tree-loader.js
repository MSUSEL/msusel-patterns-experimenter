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

//
// Extend the XmlTreeLoader to set some custom TreeNode attributes specific to our application:
//
Ext.app.BookLoader = Ext.extend(Ext.ux.XmlTreeLoader, {
    processAttributes : function(attr){
        if(attr.first){ // is it an author node?
            
            // Set the node text that will show in the tree since our raw data does not include a text attribute:
            attr.text = attr.first + ' ' + attr.last;
            
            // Author icon, using the gender flag to choose a specific icon:
            attr.iconCls = 'author-' + attr.gender;
            
            // Override these values for our folder nodes because we are loading all data at once.  If we were
            // loading each node asynchronously (the default) we would not want to do this:
            attr.loaded = true;
            //attr.expanded = true;
        }
        else if(attr.title){ // is it a book node?
            
            // Set the node text that will show in the tree since our raw data does not include a text attribute:
            attr.text = attr.title + ' (' + attr.published + ')';
            
            // Book icon:
            attr.iconCls = 'book';
            
            // Tell the tree this is a leaf node.  This could also be passed as an attribute in the original XML,
            // but this example demonstrates that you can control this even when you cannot dictate the format of 
            // the incoming source XML:
            attr.leaf = true;
        }
    }
});

Ext.onReady(function(){
	
    var detailsText = '<i>Select a book to see more information...</i>';
    
	var tpl = new Ext.Template(
        '<h2 class="title">{title}</h2>',
        '<p><b>Published</b>: {published}</p>',
        '<p><b>Synopsis</b>: {innerText}</p>',
        '<p><a href="{url}" target="_blank">Purchase from Amazon</a></p>'
	);
    tpl.compile();
    
    new Ext.Panel({
        title: 'Reading List',
	    renderTo: 'tree',
        layout: 'border',
	    width: 500,
        height: 500,
        items: [{
            xtype: 'treepanel',
            id: 'tree-panel',
            region: 'center',
            margins: '2 2 0 2',
            autoScroll: true,
	        rootVisible: false,
	        root: new Ext.tree.AsyncTreeNode(),
            
            // Our custom TreeLoader:
	        loader: new Ext.app.BookLoader({
	            dataUrl:'xml-tree-data.xml'
	        }),
            
	        listeners: {
	            'render': function(tp){
                    tp.getSelectionModel().on('selectionchange', function(tree, node){
                        var el = Ext.getCmp('details-panel').body;
	                    if(node.leaf){
	                        tpl.overwrite(el, node.attributes);
	                    }else{
                            el.update(detailsText);
                        }
                    })
	            }
	        }
        },{
            region: 'south',
            title: 'Book Details',
            id: 'details-panel',
            autoScroll: true,
            collapsible: true,
            split: true,
            margins: '0 2 2 2',
            cmargins: '2 2 2 2',
            height: 220,
            html: detailsText
        }]
    });
});
