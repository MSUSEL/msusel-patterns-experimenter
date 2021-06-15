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
tx.Exporter = function(){
	var lists = tx.data.conn.query('select * from list');
	
	var doc = new runtime.flash.xml.XMLDocument();
	
	var root = doc.createElement('simple-tasks');
	doc.appendChild(root);
	
	root.attributes['version'] = '2.0';
	
	for(var i = 0, len = lists.length; i < len; i++){
		var list = lists[i];
		
		var listNode = doc.createElement('list');
		root.appendChild(listNode);
		
		for(var k in list){
			if(list.hasOwnProperty(k)){
				listNode.attributes[k] = String(list[k]);
			}
		}
		
		var tasks = tx.data.conn.queryBy('select * from task where listId = ?', [list.listId]);
		for(var j = 0, jlen = tasks.length; j < jlen; j++){
			var task = tasks[j];
			
			var taskNode = doc.createElement('task');
			listNode.appendChild(taskNode);
			
			for(var t in task){
				if(task.hasOwnProperty(t)){
					taskNode.attributes[t] = String(task[t]);
				}
			}
		}
	}
	
	var file = new air.File(air.File.documentsDirectory.nativePath + air.File.separator + 'tasks.xml');
	
	file.addEventListener('select', function(e){
		var target = e.target;
		var stream = new air.FileStream();
        stream.open(target, air.FileMode.WRITE);
		stream.writeUTFBytes('<?xml version="1.0" encoding="UTF-8"?>');
        stream.writeUTFBytes(doc.toString());
        stream.close();
	});
	
	// I wonder why no filter for Save As?
	// var filter = new air.FileFilter("Tasks XML File", "*.xml");
	file.browseForSave('Save As');
};
