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
if (typeof(dojo) != 'undefined') { dojo.require('MochiKit.Signal'); }
if (typeof(JSAN) != 'undefined') { JSAN.use('MochiKit.Signal'); }
if (typeof(tests) == 'undefined') { tests = {}; }

tests.test_DragAndDrop = function (t) {
    
    var drag1 = new MochiKit.DragAndDrop.Draggable('drag1', {'revert': true, 'ghosting': true});

    var drop1 = new MochiKit.DragAndDrop.Droppable('drop1', {'hoverclass': 'drop-hover'});
    drop1.activate();
    t.is(hasElementClass('drop1', 'drop-hover'), true, "hoverclass ok");
    drop1.deactivate();
    t.is(hasElementClass('drop1', 'drop-hover'), false, "remove hoverclass ok");
    drop1.destroy();
    
    t.is( isEmpty(MochiKit.DragAndDrop.Droppables.drops), true, "Unregister droppable ok");
    
    var onhover = function (element) {
        t.is(element, getElement('drag1'), 'onhover ok');
    };
    var drop2 = new MochiKit.DragAndDrop.Droppable('drop1', {'onhover': onhover});
    var pos = getElementPosition('drop1');
    pos = {"x": pos.x + 5, "y": pos.y + 5};
    MochiKit.DragAndDrop.Droppables.show({"page": pos}, getElement('drag1'));

    drag1.destroy();
    t.is( isEmpty(MochiKit.DragAndDrop.Draggables.drops), true, "Unregister draggable ok");
    
};

