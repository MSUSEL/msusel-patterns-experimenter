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
Ext.air.NativeWindowManager.getTaskWindow = function(taskId){
	var win, winId = 'task' + taskId;
	if(win = this.get(winId)) {
		win.instance.orderToFront();
	} else {
		win = new Ext.air.NativeWindow({
			id: winId,
			file: 'task.html?taskId=' + taskId,
			width: 500,
			height:350
		});
	}
	return win;
}

Ext.air.NativeWindowManager.getReminderWindow = function(taskId){
	var win, winId = 'reminder' + taskId;
	if(win = this.get(winId)) {
		win.instance.orderToFront();
	} else {
		win = new Ext.air.NativeWindow({
			id: winId,
			file: 'reminder.html?taskId=' + taskId,
			width:400,
			height:140,
			maximizable: false,
			resizable: false
		});
	}
	return win;
}

Ext.air.NativeWindowManager.getAboutWindow = function(){
	var win, winId = 'about';
	if(win = this.get(winId)) {
		win.instance.orderToFront();
	} else {
		win = new Ext.air.NativeWindow({
			id: winId,
			file: 'about.html',
			width:350,
			height:300,
			resizable: false,
            type:'utility'
        });
	}
	return win;
}

Ext.air.NativeWindowManager.getPrefWindow = function(){
	var win, winId = 'prefs';
	if(win = this.get(winId)) {
		win.instance.orderToFront();
	} else {
		win = new Ext.air.NativeWindow({
			id: winId,
			file: 'preferences.html',
			width:240,
			height:150,
			resizable: false,
            type:'utility'
        });
	}
	return win;
}
