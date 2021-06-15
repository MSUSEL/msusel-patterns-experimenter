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

    Drag: A Really Simple Drag Handler
    
*/
Drag = {
    _move: null,
    _down: null,
    
    start: function(e) {
        e.stop();
        
        // We need to remember what we're dragging.
        Drag._target = e.target();
        
        /*
            There's no cross-browser way to get offsetX and offsetY, so we
            have to do it ourselves. For performance, we do this once and
            cache it.
        */
        Drag._offset = Drag._diff(
            e.mouse().page,
            getElementPosition(Drag._target));
        
        Drag._move = connect(document, 'onmousemove', Drag._drag);
        Drag._down = connect(document, 'onmouseup', Drag._stop);
    },

    _offset: null,
    _target: null,
    
    _diff: function(lhs, rhs) {
        return new MochiKit.Style.Coordinates(lhs.x - rhs.x, lhs.y - rhs.y);
    },
        
    _drag: function(e) {
        e.stop();
        setElementPosition(
            Drag._target,
            Drag._diff(e.mouse().page, Drag._offset));
    },
    
    _stop: function(e) {
        disconnect(Drag._move);
        disconnect(Drag._down);
    }
};

connect(window, 'onload',   
    function() {
        /*
            Find all DIVs tagged with the draggable class, and connect them to
            the Drag handler.
        */
        var d = getElementsByTagAndClassName('DIV', 'draggable');
        forEach(d,
            function(elem) {
                connect(elem, 'onmousedown', Drag.start);
            });
                        
    });
    
connect(window, 'onload',
    function() {
        var elems = getElementsByTagAndClassName("A", "view-source");
        var page = "draggable/";
        for (var i = 0; i < elems.length; i++) {
            var elem = elems[i];
            var href = elem.href.split(/\//).pop();
            elem.target = "_blank";
            elem.href = "../view-source/view-source.html#" + page + href;
        } 
    });
