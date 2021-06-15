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

    Key Events: A Really Simple Key Handler
    
*/

KeyEvents = {
    handled: false,
    handleF1: function() {
        replaceChildNodes('specialMessage', 'You invoked the special F1 handler!');
    },
    handleEscape: function() {
        replaceChildNodes('specialMessage', 'You invoked the special Escape handler!');
    },
    updateModifiers: function(e) {
        var modifiers = e.modifier();
        replaceChildNodes('shift', modifiers.shift);
        replaceChildNodes('ctrl', modifiers.ctrl);
        replaceChildNodes('alt', modifiers.alt);
        replaceChildNodes('meta', modifiers.meta);
    }
};

KeyEvents.specialKeyMap = {
    'KEY_F1': KeyEvents.handleF1,
    'KEY_ESCAPE': KeyEvents.handleEscape
};

connect(document, 'onkeydown', 
    function(e) {
        if (getElement('stopBox').checked == true) {
            e.preventDefault();
        }
        
        // We're storing a handled flag to work around a Safari bug: 
        // http://bugs.webkit.org/show_bug.cgi?id=3387
        if (!KeyEvents.handled) {
            var key = e.key();
            var fn = KeyEvents.specialKeyMap[key.string];
            if (fn) {
                fn();
            }
            replaceChildNodes('onkeydown_code', key.code);
            replaceChildNodes('onkeydown_string', key.string);
            KeyEvents.updateModifiers(e);
        }
        KeyEvents.handled = true;
    });
    
connect(document, 'onkeyup', 
    function(e) {
        if (getElement('stopBox').checked == true) {
            e.preventDefault();
        }
        
        KeyEvents.handled = false;
        var key = e.key();
        replaceChildNodes('onkeyup_code', key.code);
        replaceChildNodes('onkeyup_string', key.string);
        KeyEvents.updateModifiers(e);
    });

connect(document, 'onkeypress', 
    function(e) {
        if (getElement('stopBox').checked == true) {
            e.preventDefault();
        }
        
        var key = e.key();
        replaceChildNodes('onkeypress_code', key.code);
        replaceChildNodes('onkeypress_string', key.string);
        KeyEvents.updateModifiers(e);
    });

connect(window, 'onload',
    function() {        
        var elems = getElementsByTagAndClassName("A", "view-source");
        var page = "key_events/";
        for (var i = 0; i < elems.length; i++) {
            var elem = elems[i];
            var href = elem.href.split(/\//).pop();
            elem.target = "_blank";
            elem.href = "../view-source/view-source.html#" + page + href;
        }
    });