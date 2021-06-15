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
Ext.sql.Proxy = function(conn, table, keyName, store, readonly){
    Ext.sql.Proxy.superclass.constructor.call(this);
    this.conn = conn;
    this.table = this.conn.getTable(table, keyName);
    this.store = store;

	if (readonly !== true) {
		this.store.on('add', this.onAdd, this);
		this.store.on('update', this.onUpdate, this);
		this.store.on('remove', this.onRemove, this);
	}
};

Ext.sql.Proxy.DATE_FORMAT = 'Y-m-d H:i:s';

Ext.extend(Ext.sql.Proxy, Ext.data.DataProxy, {
    load : function(params, reader, callback, scope, arg){
    	if(!this.conn.isOpen()){ // assume that the connection is in the process of opening
    		this.conn.on('open', function(){
    			this.load(params, reader, callback, scope, arg);
    		}, this, {single:true});
    		return;
    	};
    	if(this.fireEvent("beforeload", this, params, reader, callback, scope, arg) !== false){
			var clause = params.where || '';
			var args = params.args || [];
			var group = params.groupBy;
			var sort = params.sort;
			var dir = params.dir;

			if(group || sort){
				clause += ' ORDER BY ';
				if(group && group != sort){
					clause += group + ' ASC, ';
				}
				clause += sort + ' ' + (dir || 'ASC');
			}

			var rs = this.table.selectBy(clause, args);
			this.onLoad({callback:callback, scope:scope, arg:arg, reader: reader}, rs);
        }else{
            callback.call(scope||this, null, arg, false);
        }
    },

    onLoad : function(trans, rs, e, stmt){
        if(rs === false){
    		this.fireEvent("loadexception", this, null, trans.arg, e);
            trans.callback.call(trans.scope||window, null, trans.arg, false);
            return;
    	}
    	var result = trans.reader.readRecords(rs);
        this.fireEvent("load", this, rs, trans.arg);
        trans.callback.call(trans.scope||window, result, trans.arg, true);
    },

    processData : function(o){
    	var fs = this.store.fields;
    	var r = {};
    	for(var key in o){
    		var f = fs.key(key), v = o[key];
			if(f){
				if(f.type == 'date'){
					r[key] = v ? v.format(Ext.sql.Proxy.DATE_FORMAT,10) : '';
				}else if(f.type == 'boolean'){
					r[key] = v ? 1 : 0;
				}else{
					r[key] = v;
				}
			}
		}
		return r;
    },

    onUpdate : function(ds, record){
    	var changes = record.getChanges();
    	var kn = this.table.keyName;
    	this.table.updateBy(this.processData(changes), kn + ' = ?', [record.data[kn]]);
    	record.commit(true);
    },

    onAdd : function(ds, records, index){
    	for(var i = 0, len = records.length; i < len; i++){
        	this.table.insert(this.processData(records[i].data));
    	}
    },

    onRemove : function(ds, record, index){
		var kn = this.table.keyName;
    	this.table.removeBy(kn + ' = ?', [record.data[kn]]);
    }
});
