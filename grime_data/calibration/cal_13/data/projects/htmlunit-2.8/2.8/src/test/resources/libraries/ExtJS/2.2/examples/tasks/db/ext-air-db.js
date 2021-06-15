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
 Ext.data.AirDB = Ext.extend(Ext.data.SqlDB, {
	open : function(db, cb, scope){
		this.conn = new air.SQLConnection();

		var file = air.File.applicationResourceDirectory.resolve(db);

		this.conn.addEventListener(air.SQLEvent.OPEN, this.onOpen.createDelegate(this, [cb, scope]));
		this.conn.addEventListener(air.SQLEvent.CLOSE, this.onClose.createDelegate(this));
		this.conn.open(file, true);
	},

	close : function(){
		this.conn.close();
	},

	onOpen : function(cb, scope){
		this.openState = true;
		Ext.callback(cb, scope, [this]);
		this.fireEvent('open', this);
	},

	onClose : function(){
		this.fireEvent('close', this);
	},

	onError : function(e, stmt, type, cb, scope){
		Ext.callback(cb, scope, [false, e, stmt]);
	},

	onResult : function(e, stmt, type, cb, scope){
		if(type == 'exec'){
			Ext.callback(cb, scope, [true, e, stmt]);
		}else{
			var r = [];
			var result = stmt.getResult();
			if(result && result.data){
		        var len = result.data.length;
		        for(var i = 0; i < len; i++) {
		            r[r.length] = result.data[i];
		        }
		    }
			Ext.callback(cb, scope, [r, e, stmt]);
		}
	},

	createStatement : function(type, cb, scope){

		var stmt = new air.SQLStatement();

		stmt.addEventListener(air.SQLErrorEvent.ERROR, this.onError.createDelegate(this, [stmt, type, cb, scope], true));
		stmt.addEventListener(air.SQLEvent.RESULT, this.onResult.createDelegate(this, [stmt, type, cb, scope], true));

		stmt.sqlConnection = this.conn;

		return stmt;
	},

	exec : function(sql, cb, scope){
		var stmt = this.createStatement('exec', cb, scope);
		stmt.text = sql;
		stmt.execute();
	},

	execBy : function(sql, args, cb, scope){
		var stmt = this.createStatement('exec', cb, scope);
		stmt.text = sql;
		this.addParams(stmt, args);
		stmt.execute();
	},

	query : function(sql, cb, scope){
		var stmt = this.createStatement('query', cb, scope);
		stmt.text = sql;
		stmt.execute(this.maxResults);
	},

	queryBy : function(sql, args, cb, scope){
		var stmt = this.createStatement('query', cb, scope);
		stmt.text = sql;
		this.addParams(stmt, args);
		stmt.execute(this.maxResults);
	},

    addParams : function(stmt, args){
		if(!args){ return; }
		for(var key in args){
			if(args.hasOwnProperty(key)){
				if(!isNaN(key)){
					stmt.parameters[parseInt(key)+1] = args[key];
				}else{
					stmt.parameters[':' + key] = args[key];
				}
			}
		}
		return stmt;
	}
});
