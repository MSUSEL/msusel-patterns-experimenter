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
Ext.CallBroker = function(config){
    if(!config.reader){
        this.reader = new Ext.data.JsonReader({}, [
            'id', 'type', 'call', 'args'
        ]);
    }
    Ext.CallBroker.superclass.constructor.call(this, config);
};

Ext.extend(Ext.CallBroker, Ext.data.Store, {
    loadRecords : function(o, options, success){
        Ext.CallBroker.superclass.loadRecords.apply(this, arguments);
        if(o && success){
            this.data.each(this.delegateCall, this);
        }
    },

    delegateCall : function(c){
        var o = this[c.type](c.data);
        o[c.call][c.args instanceof Array ? 'apply' : 'call'](o, c.args);
    },

    store : function(c){
        return Ext.StoreMgr.lookup(c.id);
    },

    component : function(c){
        return Ext.getCmp(c.id);
    },

    element : function(c){
        return Ext.get(c.id);
    },

    object : function(c){
        return new Function('return '+c.id)();
    }
});
