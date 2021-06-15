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
Ext.SwitchButton = Ext.extend(Ext.Component, {
	initComponent : function(){
		Ext.SwitchButton.superclass.initComponent.call(this);
		
		var mc = new Ext.util.MixedCollection();
		mc.addAll(this.items);
		this.items = mc;
		
		this.addEvents('change');
		
		if(this.handler){
			this.on('change', this.handler, this.scope || this);
		}
	},
	
	onRender : function(ct, position){
		
		var el = document.createElement('table');
		el.cellSpacing = 0;
		el.className = 'x-rbtn';
		el.id = this.id;
		
		var row = document.createElement('tr');
		el.appendChild(row);
		
		var count = this.items.length;
		var last = count - 1;
		this.activeItem = this.items.get(this.activeItem);
		
		for(var i = 0; i < count; i++){
			var item = this.items.itemAt(i);
			
			var cell = row.appendChild(document.createElement('td'));
			cell.id = this.id + '-rbi-' + i;
			
			var cls = i == 0 ? 'x-rbtn-first' : (i == last ? 'x-rbtn-last' : 'x-rbtn-item');
			item.baseCls = cls;
			
			if(this.activeItem == item){
				cls += '-active';
			}
			cell.className = cls;
			
			var button = document.createElement('button');
			button.innerHTML = '&#160;';
			button.className = item.iconCls;
			button.qtip = item.tooltip;
			
			cell.appendChild(button);
			
			item.cell = cell;
		}
		
		this.el = Ext.get(ct.dom.appendChild(el));
		
		this.el.on('click', this.onClick, this);
	},
	
	getActiveItem : function(){
		return this.activeItem;
	},
	
	setActiveItem : function(item){
		if(typeof item != 'object' && item !== null){
			item = this.items.get(item);
		}
		var current = this.getActiveItem();
		if(item != current){
			if(current){
				Ext.fly(current.cell).removeClass(current.baseCls + '-active');
			}
			if(item) {
				Ext.fly(item.cell).addClass(item.baseCls + '-active');
			}
			this.activeItem = item;
			this.fireEvent('change', this, item);
		}
		return item;
	},
	
	onClick : function(e){
		var target = e.getTarget('td', 2);
		if(!this.disabled && target){
			this.setActiveItem(parseInt(target.id.split('-rbi-')[1], 10));
		}
	}
});

Ext.reg('switch', Ext.SwitchButton);
