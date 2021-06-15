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
Ext.sql.Table = function(conn, name, keyName){
	this.conn = conn;
	this.name = name;
	this.keyName = keyName;
};

Ext.sql.Table.prototype = {
	update : function(o){
		var clause = this.keyName + " = ?";
		return this.updateBy(o, clause, [o[this.keyName]]);
	},

	updateBy : function(o, clause, args){
		var sql = "UPDATE " + this.name + " set ";
		var fs = [], a = [];
		for(var key in o){
			if(o.hasOwnProperty(key)){
				fs[fs.length] = key + ' = ?';
				a[a.length] = o[key];
			}
		}
		for(var key in args){
			if(args.hasOwnProperty(key)){
				a[a.length] = args[key];
			}
		}
		sql = [sql, fs.join(','), ' WHERE ', clause].join('');
		return this.conn.execBy(sql, a);
	},

	insert : function(o){
		var sql = "INSERT into " + this.name + " ";
		var fs = [], vs = [], a = [];
		for(var key in o){
			if(o.hasOwnProperty(key)){
				fs[fs.length] = key;
				vs[vs.length] = '?';
				a[a.length] = o[key];
			}
		}
		sql = [sql, '(', fs.join(','), ') VALUES (', vs.join(','), ')'].join('');
        return this.conn.execBy(sql, a);
    },

	lookup : function(id){
		return this.selectBy('where ' + this.keyName + " = ?", [id])[0] || null;
	},

	exists : function(id){
		return !!this.lookup(id);
	},

	save : function(o){
		if(this.exists(o[this.keyName])){
            this.update(o);
        }else{
            this.insert(o);
        }
	},

	select : function(clause){
		return this.selectBy(clause, null);
	},

	selectBy : function(clause, args){
		var sql = "select * from " + this.name;
		if(clause){
			sql += ' ' + clause;
		}
		args = args || {};
		return this.conn.queryBy(sql, args);
	},

	remove : function(clause){
		this.deleteBy(clause, null);
	},

	removeBy : function(clause, args){
		var sql = "delete from " + this.name;
		if(clause){
			sql += ' where ' + clause;
		}
		args = args || {};
		this.conn.execBy(sql, args);
	}
};
