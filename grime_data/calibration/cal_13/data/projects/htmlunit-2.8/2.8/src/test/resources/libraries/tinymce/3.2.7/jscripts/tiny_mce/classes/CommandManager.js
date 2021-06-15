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
(function(tinymce) {
	tinymce.CommandManager = function() {
		var execCommands = {}, queryStateCommands = {}, queryValueCommands = {};

		function add(collection, cmd, func, scope) {
			if (typeof(cmd) == 'string')
				cmd = [cmd];

			tinymce.each(cmd, function(cmd) {
				collection[cmd.toLowerCase()] = {func : func, scope : scope};
			});
		};

		tinymce.extend(this, {
			add : function(cmd, func, scope) {
				add(execCommands, cmd, func, scope);
			},

			addQueryStateHandler : function(cmd, func, scope) {
				add(queryStateCommands, cmd, func, scope);
			},

			addQueryValueHandler : function(cmd, func, scope) {
				add(queryValueCommands, cmd, func, scope);
			},

			execCommand : function(scope, cmd, ui, value, args) {
				if (cmd = execCommands[cmd.toLowerCase()]) {
					if (cmd.func.call(scope || cmd.scope, ui, value, args) !== false)
						return true;
				}
			},

			queryCommandValue : function() {
				if (cmd = queryValueCommands[cmd.toLowerCase()])
					return cmd.func.call(scope || cmd.scope, ui, value, args);
			},

			queryCommandState : function() {
				if (cmd = queryStateCommands[cmd.toLowerCase()])
					return cmd.func.call(scope || cmd.scope, ui, value, args);
			}
		});
	};

	tinymce.GlobalCommands = new tinymce.CommandManager();
})(tinymce);