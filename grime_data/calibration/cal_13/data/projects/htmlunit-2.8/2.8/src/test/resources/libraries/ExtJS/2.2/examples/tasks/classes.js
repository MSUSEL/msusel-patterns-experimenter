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
Ext.BLANK_IMAGE_URL = 'images/s.gif';
    
Task = Ext.data.Record.create([
    {name: 'taskId', type:'string'},
    {name: 'title', type:'string'},
    {name: 'category', type:'string'},
    {name: 'description', type:'string'},
    {name: 'dueDate', type:'date', dateFormat: 'Y-m-d H:i:s'},
    {name: 'completed', type:'boolean'}
]);

Task.nextId = function(){
	// if the time isn't unique enough, the addition 
	// of random chars should be
	var t = String(new Date().getTime()).substr(4);
	var s = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	for(var i = 0; i < 4; i++){
		t += s.charAt(Math.floor(Math.random()*26));
	}
	return t;
}

// The main grid's store
TaskStore = function(conn){
	TaskStore.superclass.constructor.call(this, {
        sortInfo:{field: 'dueDate', direction: "ASC"},
        groupField:'dueDate',
        taskFilter: 'all',
        reader: new Ext.data.JsonReader({
            idProperty: 'taskId'
        }, Task)
    });

    this.proxy = new Ext.data.SqlDB.Proxy(conn, 'task', 'taskId', this);

    if(window.google){ // google needs the table created
        this.proxy.on('beforeload', this.prepareTable, conn);
    }

    this.addEvents({newcategory: true});
};

Ext.extend(TaskStore, Ext.data.GroupingStore, {
    applyFilter : function(filter){
    	if(filter !== undefined){
    		this.taskFilter = filter;
    	}
        var value = this.taskFilter;
        if(value == 'all'){
            return this.clearFilter();
        }
        return this.filterBy(function(item){
            return item.data.completed === value;
        });
    },

    addTask : function(data){
        this.suspendEvents();
        this.clearFilter();
        this.resumeEvents();
        this.loadData([data], true);
        this.suspendEvents();
        this.applyFilter();
        this.applyGrouping(true);
        this.resumeEvents();
        this.fireEvent('datachanged', this);
        this.fireEvent('newcategory', data.category);
    },

    prepareTable : function(){
        try{
        this.createTable({
            name: 'task',
            key: 'taskId',
            fields: Task.prototype.fields
        });
        }catch(e){console.log(e)}
    }
});

// The store for Categories
CategoryStore = function(){
    CategoryStore.superclass.constructor.call(this, {
        expandData: true,
        data: [],
        fields:[{name: 'text', type:'string'}],
        sortInfo:{field:'text', direction:'ASC'},
        id: 0
    });
}

Ext.extend(CategoryStore, Ext.data.SimpleStore, {
    init : function(store){
        var cats = store.collect('category', false, true);
        this.loadData(cats);
    },

    addCategory : function(cat){
        if(cat && this.indexOfId(cat) === -1){
            this.clearFilter(true);
            this.loadData([cat], true);
            this.applySort();
        }
    }
});

// Grid column plugin that does the complete/active button in the left-most column
CompleteColumn = function(){
    var grid;

    function getRecord(t){
        var index = grid.getView().findRowIndex(t);
        return grid.store.getAt(index);
    }

    function onMouseDown(e, t){
        if(Ext.fly(t).hasClass('task-check')){
            e.stopEvent();
            var record = getRecord(t);
            record.set('completed', !record.data.completed);
            grid.store.applyFilter();
        }
    }

    function onMouseOver(e, t){
        if(Ext.fly(t).hasClass('task-check')){
            Ext.fly(t.parentNode).addClass('task-check-over');
        }
    }

    function onMouseOut(e, t){
        if(Ext.fly(t).hasClass('task-check')){
            Ext.fly(t.parentNode).removeClass('task-check-over');
        }
    }

    Ext.apply(this, {
        width: 22,
        header: '<div class="task-col-hd"></div>',
        menuDisabled:true,
        fixed: true,
        id: 'task-col',
        renderer: function(){
            return '<div class="task-check"></div>';
        },
        init : function(xg){
            grid = xg;
            grid.on('render', function(){
                var view = grid.getView();
                view.mainBody.on('mousedown', onMouseDown);
                view.mainBody.on('mouseover', onMouseOver);
                view.mainBody.on('mouseout', onMouseOut);
            });
        }
    });
};
