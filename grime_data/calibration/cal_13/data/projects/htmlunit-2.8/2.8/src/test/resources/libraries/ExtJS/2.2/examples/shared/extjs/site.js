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
Ext.onReady(function(){

	var activeMenu;

	function createMenu(name){
		var el = Ext.get(name+'-link');
		var tid = 0, menu, doc = Ext.getDoc();

		var handleOver = function(e, t){
			if(t != el.dom && t != menu.dom && !e.within(el) && !e.within(menu)){
				hideMenu();
			}
		};

		var hideMenu = function(){
			if(menu){
				menu.hide();
				el.setStyle('text-decoration', '');
				doc.un('mouseover', handleOver);
				doc.un('mousedown', handleDown);
			}
		}

		var handleDown = function(e){
			if(!e.within(menu)){
				hideMenu();
			}
		}

		var showMenu = function(){
			clearTimeout(tid);
			tid = 0;

			if (!menu) {
				menu = new Ext.Layer({shadow:'sides',hideMode: 'display'}, name+'-menu');
			}
			menu.hideMenu = hideMenu;

			menu.el = el;
			if(activeMenu && menu != activeMenu){
				activeMenu.hideMenu();
			}
			activeMenu = menu;

			if (!menu.isVisible()) {
				menu.show();
				menu.alignTo(el, 'tl-bl?');
				menu.sync();
				el.setStyle('text-decoration', 'underline');

				doc.on('mouseover', handleOver, null, {buffer:150});
				doc.on('mousedown', handleDown);
			}
		}

		el.on('mouseover', function(e){
			if(!tid){
				tid = showMenu.defer(150);
			}
		});

		el.on('mouseout', function(e){
			if(tid && !e.within(el, true)){
				clearTimeout(tid);
				tid = 0;
			}
		});
	}

	createMenu('products');
	createMenu('support');
	createMenu('store');

	// expanders
	Ext.getBody().on('click', function(e, t){
		t = Ext.get(t);
		e.stopEvent();

		var bd = t.next('div.expandable-body');
		bd.enableDisplayMode();
		var bdi = bd.first();
		var expanded = bd.isVisible();

		if(expanded){
			bd.hide();
		}else{
			bdi.hide();
			bd.show();
			bdi.slideIn('l', {duration:.2, stopFx: true, easing:'easeOut'});
		}

		t.update(!expanded ? 'Hide details' : 'Show details');

	}, null, {delegate:'a.expander'});
});
