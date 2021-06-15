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
Script: Class.js
	Contains the Class Function for easily creating, extending, and implementing reusable Classes.

License:
	MIT-style license.
*/

var Class = new Native({

	name: 'Class',

	initialize: function(properties){
		properties = properties || {};
		var klass = function(){
			for (var key in this){
				if ($type(this[key]) != 'function') this[key] = $unlink(this[key]);
			}
			this.constructor = klass;
			if (Class.prototyping) return this;
			var instance = (this.initialize) ? this.initialize.apply(this, arguments) : this;
			if (this.options && this.options.initialize) this.options.initialize.call(this);
			return instance;
		};

		for (var mutator in Class.Mutators){
			if (!properties[mutator]) continue;
			properties = Class.Mutators[mutator](properties, properties[mutator]);
			delete properties[mutator];
		}

		$extend(klass, this);
		klass.constructor = Class;
		klass.prototype = properties;
		return klass;
	}

});

Class.Mutators = {

	Extends: function(self, klass){
		Class.prototyping = klass.prototype;
		var subclass = new klass;
		delete subclass.parent;
		subclass = Class.inherit(subclass, self);
		delete Class.prototyping;
		return subclass;
	},

	Implements: function(self, klasses){
		$splat(klasses).each(function(klass){
			Class.prototying = klass;
			$extend(self, ($type(klass) == 'class') ? new klass : klass);
			delete Class.prototyping;
		});
		return self;
	}

};

Class.extend({

	inherit: function(object, properties){
		var caller = arguments.callee.caller && !Browser.Features.air; // caller support is broken in air 1.5
		for (var key in properties){
			var override = properties[key];
			var previous = object[key];
			var type = $type(override);
			if (previous && type == 'function'){
				if (override != previous){
					if (caller){
						override.__parent = previous;
						object[key] = override;
					} else {
						Class.override(object, key, override);
					}
				}
			} else if(type == 'object'){
				object[key] = $merge(previous, override);
			} else {
				object[key] = override;
			}
		}

		if (caller) object.parent = function(){
			return arguments.callee.caller.__parent.apply(this, arguments);
		};

		return object;
	},

	override: function(object, name, method){
		var parent = Class.prototyping;
		if (parent && object[name] != parent[name]) parent = null;
		var override = function(){
			var previous = this.parent;
			this.parent = parent ? parent[name] : object[name];
			var value = method.apply(this, arguments);
			this.parent = previous;
			return value;
		};
		object[name] = override;
	}

});

Class.implement({

	implement: function(){
		var proto = this.prototype;
		$each(arguments, function(properties){
			Class.inherit(proto, properties);
		});
		return this;
	}

});
