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
 * SpiderMonkey host environment
 */

if(djConfig["baseUrl"]){
	dojo.baseUrl = djConfig["baseUrl"];
}else{
	dojo.baseUrl = "./";
}

dojo._name = 'spidermonkey';
dojo.isSpidermonkey = true;
dojo.exit = function(exitcode){ 
	quit(exitcode); 
}

if(typeof print == "function"){
	console.debug = print;
}

if(typeof line2pc == 'undefined'){
	throw new Error("attempt to use SpiderMonkey host environment when no 'line2pc' global");
}

dojo._spidermonkeyCurrentFile = function(depth){
	//	
	//	This is a hack that determines the current script file by parsing a
	//	generated stack trace (relying on the non-standard "stack" member variable
	//	of the SpiderMonkey Error object).
	//	
	//	If param depth is passed in, it'll return the script file which is that far down
	//	the stack, but that does require that you know how deep your stack is when you are
	//	calling.
	//	
    var s = '';
    try{
		throw Error("whatever");
	}catch(e){
		s = e.stack;
	}
    // lines are like: bu_getCurrentScriptURI_spidermonkey("ScriptLoader.js")@burst/Runtime.js:101
    var matches = s.match(/[^@]*\.js/gi);
    if(!matches){ 
		throw Error("could not parse stack string: '" + s + "'");
	}
    var fname = (typeof depth != 'undefined' && depth) ? matches[depth + 1] : matches[matches.length - 1];
    if(!fname){ 
		throw Error("could not find file name in stack string '" + s + "'");
	}
    //print("SpiderMonkeyRuntime got fname '" + fname + "' from stack string '" + s + "'");
    return fname;
}

// print(dojo._spidermonkeyCurrentFile(0)); 

dojo._loadUri = function(uri){
	// spidermonkey load() evaluates the contents into the global scope (which
	// is what we want).
	// TODO: sigh, load() does not return a useful value. 
	// Perhaps it is returning the value of the last thing evaluated?
	var ok = load(uri);
	// console.debug("spidermonkey load(", uri, ") returned ", ok);
	return 1;
}

//Register any module paths set up in djConfig. Need to do this
//in the hostenvs since hostenv_browser can read djConfig from a
//script tag's attribute.
if(djConfig["modulePaths"]){
	for(var param in djConfig["modulePaths"]){
		dojo.registerModulePath(param, djConfig["modulePaths"][param]);
	}
}
