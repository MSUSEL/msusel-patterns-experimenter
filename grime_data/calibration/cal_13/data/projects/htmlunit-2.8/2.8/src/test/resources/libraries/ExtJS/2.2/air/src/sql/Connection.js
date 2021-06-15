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
// Asbtract base class for Connection classes
Ext.sql.Connection = function(config){
	Ext.apply(this, config);
	Ext.sql.Connection.superclass.constructor.call(this);

	this.addEvents({
		open : true,
		close: true
	});
};

Ext.extend(Ext.sql.Connection, Ext.util.Observable, {
	maxResults: 10000,
	openState : false,

    // abstract methods
    open : function(file){
	},

	close : function(){
	},

    exec : function(sql){
	},

	execBy : function(sql, args){
	},

	query : function(sql){
	},

	queryBy : function(sql, args){
	},

    // protected/inherited method
    isOpen : function(){
		return this.openState;
	},

	getTable : function(name, keyName){
		return new Ext.sql.Table(this, name, keyName);
	},

	createTable : function(o){
		var tableName = o.name;
		var keyName = o.key;
		var fs = o.fields;
		if(!Ext.isArray(fs)){ // Ext fields collection
			fs = fs.items;
		}
		var buf = [];
		for(var i = 0, len = fs.length; i < len; i++){
			var f = fs[i], s = f.name;
			switch(f.type){
	            case "int":
	            case "bool":
	            case "boolean":
	                s += ' INTEGER';
	                break;
	            case "float":
	                s += ' REAL';
	                break;
	            default:
	            	s += ' TEXT';
	        }
	        if(f.allowNull === false || f.name == keyName){
	        	s += ' NOT NULL';
	        }
	        if(f.name == keyName){
	        	s += ' PRIMARY KEY';
	        }
	        if(f.unique === true){
	        	s += ' UNIQUE';
	        }

	        buf[buf.length] = s;
	    }
	    var sql = ['CREATE TABLE IF NOT EXISTS ', tableName, ' (', buf.join(','), ')'].join('');
        this.exec(sql);
	}
});


Ext.sql.Connection.getInstance = function(db, config){
    if(Ext.isAir){ // air
        return new Ext.sql.AirConnection(config);
    } else { // gears
        return new Ext.sql.GearsConnection(config);
    }
};
