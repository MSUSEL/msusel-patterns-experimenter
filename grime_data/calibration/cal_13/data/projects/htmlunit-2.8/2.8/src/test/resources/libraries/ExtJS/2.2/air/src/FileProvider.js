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
/**
 * @class Ext.air.FileProvider
 * @extends Ext.state.Provider
 * 
 * An Ext state provider implementation for Adobe AIR that stores state in the application 
 * storage directory.
 * 
 * @constructor
 * @param {Object} config
 */
Ext.air.FileProvider = function(config){
    Ext.air.FileProvider.superclass.constructor.call(this);
	
	this.defaultState = {
		mainWindow : {
			width:780,
			height:580,
			x:10,
			y:10
		}
	};
	
    Ext.apply(this, config);
    this.state = this.readState();
	
	var provider = this;
	air.NativeApplication.nativeApplication.addEventListener('exiting', function(){
		provider.saveState();
	});
};

Ext.extend(Ext.air.FileProvider, Ext.state.Provider, {
	/**
	 * @cfg {String} file
	 * The file name to use for the state file in the application storage directory
	 */
	file: 'extstate.data',
	
	/**
	 * @cfg {Object} defaultState
	 * The default state if no state file can be found
	 */
	// private
    readState : function(){
		var stateFile = air.File.applicationStorageDirectory.resolvePath(this.file);
		if(!stateFile.exists){
			return this.defaultState || {};
		}
		
		var stream = new air.FileStream();
		stream.open(stateFile, air.FileMode.READ);
		
		var stateData = stream.readObject();
		stream.close();
		
		return stateData || this.defaultState || {};
    },

    // private
    saveState : function(name, value){
        var stateFile = air.File.applicationStorageDirectory.resolvePath(this.file);
		var stream = new air.FileStream();
		stream.open(stateFile, air.FileMode.WRITE);
		stream.writeObject(this.state);
		stream.close();
    }
});
