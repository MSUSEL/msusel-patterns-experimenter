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
FeedPanel = function() {
    FeedPanel.superclass.constructor.call(this, {
        id:'feed-tree',
        region:'west',
        title:'Feeds',
        split:true,
        width: 225,
        minSize: 175,
        maxSize: 400,
        collapsible: true,
        margins:'0 0 5 5',
        cmargins:'0 5 5 5',
        rootVisible:false,
        lines:false,
        autoScroll:true,
        root: new Ext.tree.TreeNode('Feed Viewer'),
        collapseFirst:false,

        tbar: [{
            iconCls:'add-feed',
            text:'Add Feed',
            handler: this.showWindow,
            scope: this
        },{
            id:'delete',
            iconCls:'delete-icon',
            text:'Remove',
            handler: function(){
                var s = this.getSelectionModel().getSelectedNode();
                if(s){
                    this.removeFeed(s.attributes.url);
                }
            },
            scope: this
        }]
    });

    this.feeds = this.root.appendChild(
        new Ext.tree.TreeNode({
            text:'My Feeds',
            cls:'feeds-node',
            expanded:true
        })
    );

    this.getSelectionModel().on({
        'beforeselect' : function(sm, node){
             return node.isLeaf();
        },
        'selectionchange' : function(sm, node){
            if(node){
                this.fireEvent('feedselect', node.attributes);
            }
            this.getTopToolbar().items.get('delete').setDisabled(!node);
        },
        scope:this
    });

    this.addEvents({feedselect:true});

    this.on('contextmenu', this.onContextMenu, this);
};

Ext.extend(FeedPanel, Ext.tree.TreePanel, {

    onContextMenu : function(node, e){
        if(!this.menu){ // create context menu on first right click
            this.menu = new Ext.menu.Menu({
                id:'feeds-ctx',
                items: [{
                    id:'load',
                    iconCls:'load-icon',
                    text:'Load Feed',
                    scope: this,
                    handler:function(){
                        this.ctxNode.select();
                    }
                },{
                    text:'Remove',
                    iconCls:'delete-icon',
                    scope: this,
                    handler:function(){
                        this.ctxNode.ui.removeClass('x-node-ctx');
                        this.removeFeed(this.ctxNode.attributes.url);
                        this.ctxNode = null;
                    }
                },'-',{
                    iconCls:'add-feed',
                    text:'Add Feed',
                    handler: this.showWindow,
                    scope: this
                }]
            });
            this.menu.on('hide', this.onContextHide, this);
        }
        if(this.ctxNode){
            this.ctxNode.ui.removeClass('x-node-ctx');
            this.ctxNode = null;
        }
        if(node.isLeaf()){
            this.ctxNode = node;
            this.ctxNode.ui.addClass('x-node-ctx');
            this.menu.items.get('load').setDisabled(node.isSelected());
            this.menu.showAt(e.getXY());
        }
    },

    onContextHide : function(){
        if(this.ctxNode){
            this.ctxNode.ui.removeClass('x-node-ctx');
            this.ctxNode = null;
        }
    },

    showWindow : function(btn){
        if(!this.win){
            this.win = new FeedWindow();
            this.win.on('validfeed', this.addFeed, this);
        }
        this.win.show(btn);
    },

    selectFeed: function(url){
        this.getNodeById(url).select();
    },

    removeFeed: function(url){
        var node = this.getNodeById(url);
        if(node){
            node.unselect();
            Ext.fly(node.ui.elNode).ghost('l', {
                callback: node.remove, scope: node, duration: .4
            });
        }
    },

    addFeed : function(attrs, inactive, preventAnim){
        var exists = this.getNodeById(attrs.url);
        if(exists){
            if(!inactive){
                exists.select();
                exists.ui.highlight();
            }
            return;
        }
        Ext.apply(attrs, {
            iconCls: 'feed-icon',
            leaf:true,
            cls:'feed',
            id: attrs.url
        });
        var node = new Ext.tree.TreeNode(attrs);
        this.feeds.appendChild(node);
        if(!inactive){
            if(!preventAnim){
                Ext.fly(node.ui.elNode).slideIn('l', {
                    callback: node.select, scope: node, duration: .4
                });
            }else{
                node.select();
            }
        }
        return node;
    },

    // prevent the default context menu when you miss the node
    afterRender : function(){
        FeedPanel.superclass.afterRender.call(this);
        this.el.on('contextmenu', function(e){
            e.preventDefault();
        });
    }
});
