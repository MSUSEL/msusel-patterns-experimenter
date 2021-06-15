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
tx.Importer = function(){
	function chooseFile(callback){
		var file = new air.File(air.File.documentsDirectory.nativePath);
		var filter = new air.FileFilter("Tasks XML File", "*.xml");
		
		file.addEventListener('select', function(e){
			doImport(e.target, callback);
		});
		
		file.browseForOpen('Open', [filter]);
	}
	
	
	/*
	 * This import function used to be clean and simple. The addition of asynchronous import and
	 * a progress bar changed that quickly. :) 
	 */
	function doImport(file, callback){
		
		Ext.Msg.progress('Import', 'Reading file...');
		
		var listTable = tx.data.conn.getTable('list', 'listId');
		var taskTable = tx.data.conn.getTable('task', 'taskId');
		var taskCount = 0;
		var visibleIndex = 0;
		var doUpdate = true;
		var f = String.format;
		
		function getProgress(index){
			if(taskCount > 0){
				return (.8 * index) / taskCount;
			}else{
				return .8;
			}
		}
		
		function readFile(){
			var stream = new air.FileStream();
			stream.open(file, air.FileMode.READ);
			var xml = stream.readUTFBytes(stream.bytesAvailable);
			stream.close();
					
			Ext.Msg.updateProgress(.1, 'Parsing...');
			parse.defer(10, null, [xml]);
		}
		
		function parse(xml){
			try {
				var doc = new runtime.flash.xml.XMLDocument();
				doc.ignoreWhite = true;
				doc.parseXML(xml);
				
				var lists = doc.firstChild.childNodes;
				var count = lists.length;
				
				for (var i = 0; i < count; i++) {
					taskCount += lists[i].childNodes.length;
				}	
				Ext.Msg.updateProgress(.15, '', 'Loading Tasks...');
				loadLists(lists, 0);
			}catch(e){
				air.trace(e);
				alert('An error occured while trying to import the selected file.');
			}			
		}
		
		function loadLists(lists, index){
			if(index < lists.length){
				var list = lists[index];
				listTable.save(list.attributes);
				nextTask(list.childNodes, 0, lists, index);
			}
			else {
				Ext.Msg.updateProgress(1, '', 'Completing import...');
				callback.defer(10);
			}				
		}		
		
		function nextTask(tasks, index, lists, listIndex){
			if(index < tasks.length){
				Ext.Msg.updateProgress(
					getProgress(++visibleIndex),
					f('{0} of {1}', visibleIndex, taskCount)
				);
				loadTasks.defer(250, window, [tasks, index, lists, listIndex]);
			}
			else {
				loadLists(lists, ++listIndex);
			}
		}
		
		function loadTasks(tasks, index, lists, listIndex){
			taskTable.save(tasks[index].attributes);
			nextTask(tasks, ++index, lists, listIndex);
		}
		
		readFile.defer(10);
	}
	
	this.doImport = function(callback){
		chooseFile(callback);
	}
};
