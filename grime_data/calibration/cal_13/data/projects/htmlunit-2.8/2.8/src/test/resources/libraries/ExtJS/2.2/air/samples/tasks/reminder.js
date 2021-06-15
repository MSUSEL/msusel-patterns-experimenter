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
Ext.onReady(function(){
	var win = window.nativeWindow;
	var opener = Ext.air.NativeWindow.getRootHtmlWindow();
	var taskId = String(window.location).split('=')[1];
	
	var store = opener.tx.data.tasks;
	var task = store.lookup(taskId);
	
	win.title = 'Reminder - ' + Ext.util.Format.ellipsis(task.data.title, 40);
	
	bulkUpdate({
		'task-title' : Ext.util.Format.ellipsis(task.data.title, 80),
		'task-due' : task.data.dueDate ? task.data.dueDate.format('F d, Y') : 'None'
	});
	
	function bulkUpdate(o){
		for(var id in o){
			Ext.fly(id).update(o[id]);
		}
	}
		
	var dismiss = new Ext.Button({
		text: 'Dismiss',
		minWidth: 80,
		renderTo: 'btns',
		handler: function(){
			win.close();
		}
	});
	
	var snooze = new Ext.Button({
		text: 'Snooze',
		minWidth: 80,
		renderTo: 'btns',
		handler: function(){
			var min = parseInt(Ext.get('snooze-time').getValue(), 10);
			var reminder = new Date().add('mi', min);
			var o = store.getById(taskId);
			if(o){
				o.set('reminder', reminder);
			}else{
				store.proxy.table.updateBy({reminder: reminder}, 'where taskId = ?', [taskId]);
			}
			win.close();
		}
	});
	
	win.visible = true;
	win.activate();
	win.notifyUser('informational');
	
	Ext.air.Sound.play('beep.mp3', 10500);
});

    

