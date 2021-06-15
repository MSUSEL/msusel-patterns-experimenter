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
	
	var d = new Date().clearTime(true);
	d = d.add('mi', opener.Ext.state.Manager.get('defaultReminder'));
	
	var time = new Ext.get('time');
	time.dom.value = d.format('g:i A');	
	
	var ok = new Ext.Button({
		text: 'OK',
		minWidth: 80,
		renderTo: 'btns',
		handler: function(){
			var t = Date.parseDate(time.getValue(), 'g:i A');
			if(t){
				var m = t.getMinutes() + (t.getHours()*60);
				opener.Ext.state.Manager.set('defaultReminder', m);
			}
			win.close();
		}
	});
	
	var close = new Ext.Button({
		text: 'Cancel',
		minWidth: 80,
		renderTo: 'btns',
		handler: function(){
			win.close();
		}
	});
	
	win.visible = true;
	win.activate();
});

    

