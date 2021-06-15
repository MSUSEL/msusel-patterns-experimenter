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
// Unique task ids, if the time isn't unique enough, the addition 
// of random chars should be
Ext.uniqueId = function(){
	var t = String(new Date().getTime()).substr(4);
	var s = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	for(var i = 0; i < 4; i++){
		t += s.charAt(Math.floor(Math.random()*26));
	}
	return t;
}

// Define the Task data type
tx.data.Task = Ext.data.Record.create([
    {name: 'taskId', type:'string'},
    {name: 'listId', type:'string'},
    {name: 'title', type:'string'},
    {name: 'description', type:'string'},
    {name: 'dueDate', type:'date', dateFormat: Ext.sql.Proxy.DATE_FORMAT, defaultValue: ''},
    {name: 'completed', type:'boolean'},
    {name: 'completedDate', type:'date', dateFormat: Ext.sql.Proxy.DATE_FORMAT, defaultValue: ''},
    {name: 'reminder', type:'date', dateFormat: Ext.sql.Proxy.DATE_FORMAT, defaultValue: ''}
]);

// Define the List data type
tx.data.List = Ext.data.Record.create([
    {name: 'listId', type:'string'},
    {name: 'parentId', type:'string'},
    {name: 'listName', type:'string'},
    {name: 'isFolder', type:'boolean'}
]);

tx.data.conn = Ext.sql.Connection.getInstance();

tx.data.tasks = new tx.data.TaskStore();
tx.data.lists = new tx.data.ListStore();


tx.data.getDefaultReminder = function(task){
	var s = task.data.dueDate ? task.data.dueDate.clearTime(true) : new Date().clearTime();
	s = s.add('mi', Ext.state.Manager.get('defaultReminder'));
	return s;
};


tx.data.getActiveListId = function(){
    var id = tx.data.tasks.activeList;
    if(!id){
        var first = tx.data.lists.getAt(0);
        if(first){
            id = first.id;
        }else{
            id = tx.data.lists.newList().id;
        }
    }
    return id;
};

