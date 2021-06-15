/**
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
/**
	A wrapper around Flash 8's ExternalInterface; this is needed so that we
	can do a Flash 6 implementation of ExternalInterface, and be able
	to support having a single codebase that uses DojoExternalInterface
	across Flash versions rather than having two seperate source bases,
	where one uses ExternalInterface and the other uses DojoExternalInterface.
	
	@author Brad Neuberg, bkn3@columbia.edu
*/
import flash.external.ExternalInterface;

class DojoExternalInterface{
	public static var available:Boolean;
	
	public static function initialize(){
		// set whether communication is available
		DojoExternalInterface.available = ExternalInterface.available;
		DojoExternalInterface.call("loaded");
	}
	
	public static function addCallback(methodName:String, instance:Object, 
										 								 method:Function) : Boolean{
		return ExternalInterface.addCallback(methodName, instance, method);									 
	}
	
	public static function call(methodName:String) : Object{
		// we might have any number of optional arguments, so we have to 
		// pass them in dynamically
		return ExternalInterface.call.apply(ExternalInterface, arguments);
	}
	
	/** 
			Called by Flash to indicate to JavaScript that we are ready to have
			our Flash functions called. Calling loaded()
			will fire the dojo.flash.loaded() event, so that JavaScript can know that
			Flash has finished loading and adding its callbacks, and can begin to
			interact with the Flash file.
	*/
	public static function loaded(){
		DojoExternalInterface.call("dojo.flash.loaded");
	}
}

// vim:ts=4:noet:tw=0:
