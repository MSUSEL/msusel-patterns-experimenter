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
Ext.menu.RangeMenu = function(config){
	Ext.menu.RangeMenu.superclass.constructor.call(this, config);
  
	this.updateTask = new Ext.util.DelayedTask(this.fireUpdate, this);

	var cfg = this.fieldCfg;
	var cls = this.fieldCls;
	var fields = this.fields = Ext.applyIf(this.fields || {}, {
		'gt': new Ext.menu.EditableItem({
			icon:  this.icons.gt,
			editor: new cls(typeof cfg == "object" ? cfg.gt || '' : cfg)
    }),
		'lt': new Ext.menu.EditableItem({
			icon:  this.icons.lt,
			editor: new cls(typeof cfg == "object" ? cfg.lt || '' : cfg)
    }),
		'eq': new Ext.menu.EditableItem({
			icon:   this.icons.eq, 
			editor: new cls(typeof cfg == "object" ? cfg.gt || '' : cfg)
    })
	});
	this.add(fields.gt, fields.lt, '-', fields.eq);
	
	for(var key in fields) {
		fields[key].on('keyup', this.onKeyUp.createDelegate(this, [fields[key]], true), this);
  }
  
	this.addEvents('update');
};

Ext.extend(Ext.menu.RangeMenu, Ext.menu.Menu, {
	fieldCls:     Ext.form.NumberField,
	fieldCfg:     '',
	updateBuffer: 500,
	icons: {
		gt: '/img/small_icons/greater_then.png', 
		lt: '/img/small_icons/less_then.png',
		eq: '/img/small_icons/equals.png'
  },
		
	fireUpdate: function() {
		this.fireEvent("update", this);
	},
	
	setValue: function(data) {
		for(var key in this.fields) {
			this.fields[key].setValue(data[key] !== undefined ? data[key] : '');
    }
		this.fireEvent("update", this);
	},
	
	getValue: function() {
		var result = {};
		for(var key in this.fields) {
			var field = this.fields[key];
			if(field.isValid() && String(field.getValue()).length > 0) { 
				result[key] = field.getValue();
      }
		}
		
		return result;
	},
  
  onKeyUp: function(event, input, notSure, field) {
    if(event.getKey() == event.ENTER && field.isValid()) {
	    this.hide(true);
	    return;
	  }
	
	  if(field == this.fields.eq) {
	    this.fields.gt.setValue(null);
	    this.fields.lt.setValue(null);
	  } else {
	    this.fields.eq.setValue(null);
	  }
	  
	  this.updateTask.delay(this.updateBuffer);
  }
});
