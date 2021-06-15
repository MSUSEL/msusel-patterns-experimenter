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
/*
Script: Element.Style.js
	Specification Examples of Element.Style.js.

License:
	MIT-style license.
*/

describe('Element.set `opacity`', {

	'should set the opacity of an Element': function() {
		var el = new Element('div').set('opacity', 0.4);
		if (Browser.Engine.trident) value_of(el.style.filter).should_be('alpha(opacity=40)');
		value_of(el.style.opacity).should_be(0.4);
	},

	'should return the opacity of an Element': function() {
		value_of(new Element('div').set('opacity', 0.4).get('opacity')).should_be(0.4);
	}

});

describe('Element.getStyle', {

	'should get a six digit hex code from a three digit hex code': function() {
		var el = new Element('div').set('html', '<div style="color:#00ff00"></div>');
		value_of(el.getElement('div').getStyle('color')).should_be('#00ff00');
	},

	'should getStyle a six digit hex code from an RGB value': function() {
		var el = new Element('div').set('html', '<div style="color:rgb(0, 255, 0)"></div>');
		value_of(el.getElement('div').getStyle('color')).should_be('#00ff00');
	},

	'should `getStyle` with a dash in it': function() {
		var el = new Element('div').set('html', '<div style="list-style-type:square"></div>');
		value_of(el.getElement('div').getStyle('list-style-type')).should_be('square');
	}

});

describe('Element.setStyle', {

	'should set the `styles` property on an Element using the Element constructor': function() {
		value_of(new Element('div', {styles:{'color':'#00ff00'}}).getStyle('color')).should_be('#00ff00');
	},

	'should `setStyle` on an Element': function() {
		value_of(new Element('div').setStyle('color','#00ff00').getStyle('color')).should_be('#00ff00');
	},

	'should properly `setStyle` for a property with a dash in it': function() {
		value_of(new Element('div').setStyle('list-style-type', 'square').getStyle('list-style-type')).should_be('square');
	}

});

describe('Element.getStyles', {

	'should return multiple styles': function() {
		var el = new Element('div').set('html', '<div style="color:#00ff00;list-style-type:square"></div>');
		value_of(el.getElement('div').getStyles('color', 'list-style-type')).should_be({color:'#00ff00', 'list-style-type':'square'});
	}

});

describe('Element.setStyles', {

	'should set multiple styles': function() {
		value_of(new Element('div').setStyles({'list-style-type':'square', 'color':'#00ff00'}).getStyles('list-style-type', 'color')).should_be({'list-style-type':'square', color:'#00ff00'});
	}

});