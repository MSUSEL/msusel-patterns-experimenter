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

    MochiRegExp: JavaScript Regular Expression Explorer

*/
RegExpManager = function () {
    this.timer = null;
    bindMethods(this);
};

RegExpManager.prototype.initialize = function () {
    /*
       Fill in the event handlers and sample text, and do the initial update
       The reason we do the sample text here is so that "clear" really does
       clear the fields.
    */
    setNodeAttribute("inp_text", "value", "matched with your pattern");
    connect("inp_text", "onkeyup", this, "updateSoon");
    connect("inp_text", "onchange", this, "update");

    setNodeAttribute("inp_regexp", "value", "/(pattern)/");
    connect("inp_regexp", "onkeyup", this, "updateSoon");
    connect("inp_regexp", "onchange", this, "update");

    connect("regexp_form", "onsubmit", this, "submit");

    this.update();
};

RegExpManager.prototype.updateSoon = function () {
    /*
        If the user stops typing for half a second, do one update.
    */
    this.cancelTimer();
    this.timer = callLater(0.5, this.update);
};

RegExpManager.prototype.cancelTimer = function () {
    /*
        Cancel the timer that waits for the user to idle for half a second.
    */
    if (this.timer) {
        this.timer.cancel();
    }
    this.timer = null;
};

RegExpManager.prototype.update = function () {
    /*
        Cancel the update timer and actually do an update of the
        RegExp
    */
    this.cancelTimer();
    var re;
    try {
        // Evaluate the regexp
        re = eval("(" + getElement("inp_regexp").value + ")");
    } catch (e) {
        // If invalid, color it red and stop
        addElementClass("lab_regexp", "error");
        return;
    }
    // Make sure that it's not red
    removeElementClass("lab_regexp", "error");

    // replace the contents of the tbody
    var match = getElement("inp_text").value.match(re);
    replaceChildNodes("result_body", this.getRows(match));
};

RegExpManager.prototype.getRows = function (match) {
    /*
        Return rows for the tbody given a match object
    */
    if (!match) {
        // No match, bail with a no match row
        return TR(null, TD({"colspan": "3"}, "No match!"));
    }
    var isAlternate = true;
    var res = [];
    for (var k in match) {
        isAlternate = !isAlternate;
        var trAttribs = isAlternate ? {"class": "alternate"} : null;
        var v = match[k];
        var result = v;
        // Highlight the result for the input property as three spans:
        //    [beforeMatch, duringMatch, afterMatch]
        if (k == "input") {
            var end = match.index + match[0].length;
            result = [
                SPAN(null, v.substr(0, match.index)),
                SPAN({"class": "highlight"}, v.substring(match.index, end)),
                SPAN(null, v.substr(end))
            ];
        }
        res.push(
            TR((isAlternate ? {"class": "alternate"} : null),
                TD(null, k),
                TD(null, result),
                TD(null, repr(v))
            )
        );
    }
    return res;
};

RegExpManager.prototype.submit = function () {
    this.update();
    return false;
};

regExpManager = new RegExpManager();
addLoadEvent(regExpManager.initialize);

// rewrite the view-source links
addLoadEvent(function () {
    var elems = getElementsByTagAndClassName("A", "view-source");
    var page = "mochiregexp/";
    for (var i = 0; i < elems.length; i++) {
        var elem = elems[i];
        var href = elem.href.split(/\//).pop();
        elem.target = "_blank";
        elem.href = "../view-source/view-source.html#" + page + href;
    }
});
