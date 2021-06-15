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
// $Id$

function TestTestCase() {
    this.name = 'TestTestCase';

    this.setUp = function() {
        /* not in use here, didn't have to define it but this might be
           used as a reference
        */
    };

    this.testAssert = function() {
        this.assert(true);
        this.assertThrows(this.assert, undefined, this, false);
    };
        
    this.testAssertEquals = function() {
        this.assertEquals('foo', 'foo');
        this.assertThrows(this.assertEquals, undefined, this, 'foo', 'bar');
    };

    this.testAssertNotEquals = function() {
        this.assertNotEquals('foo', 'bar');
        this.assertThrows(this.assertNotEquals, undefined, this, 'foo', 'foo');
    };

    this.testAssertTrue = function() {
        this.assertTrue(1);
        this.assertTrue('foo');
        this.assertThrows(this.assertTrue, undefined, this, false);
    };

    this.testAssertFalse = function() {
        this.assertFalse(0);
        this.assertFalse('');
        this.assertThrows(this.assertFalse, undefined, this, true);
    };

    this.testAssertThrows = function() {
        this.assertThrows(function() {throw('foo')}, 'foo');
        this.assertThrows(function() {throw(new Array(1,2))}, new Array(1,2));
        this.assertThrows(function() {throw('bar')});
        this.assertThrows(this.assertThrows, undefined, this, 
                            function() {}, 'baz');
    };

    this.tearDown = function() {
        /* not in use here, didn't have to define it but this might be
           used as a reference
        */
    };
};

TestTestCase.prototype = new TestCase;

function TestTestCase2() {
    /* an example unit test */
    this.name = 'TestTestCase2';

    this.setUp = function() {
        function Foo() {
            this.returnfoo = function() {
              return 'foo';
            };
            this.throwfoo = function() {
              throw('foo');
            };
        };
        this.foo = new Foo();
    };

    this.testAssert = function() {
        this.assert(this.foo.returnfoo() == 'foo');
    };
        
    this.testAssertEquals = function() {
        this.assertEquals(this.foo.returnfoo(), 'foo');
    };

    this.testAssertNotEquals = function() {
        this.assertNotEquals(this.foo.returnfoo(), 'bar');
    };

    this.testAssertTrue = function() {
        this.assertTrue(this.foo.returnfoo() == 'foo');
    };

    this.testAssertFalse = function() {
        this.assertFalse(this.foo.returnfoo() == 'bar');
    };

    this.testAssertThrows = function() {
        this.assertThrows(this.foo.throwfoo, 'foo');
    };
};

TestTestCase2.prototype = new TestCase;
