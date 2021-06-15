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
if (typeof(dojo) != 'undefined') { dojo.require('MochiKit.Logging'); }
if (typeof(JSAN) != 'undefined') { JSAN.use('MochiKit.Logging'); }
if (typeof(tests) == 'undefined') { tests = {}; }

tests.test_Logging = function (t) {
  
    // just in case
    logger.clear();

    t.is( logLevelAtLeast('DEBUG')('INFO'), false, 'logLevelAtLeast false' );
    t.is( logLevelAtLeast('WARNING')('INFO'), false, 'logLevelAtLeast true' );
    t.ok( logger instanceof Logger, "global logger installed" );

    var allMessages = [];
    logger.addListener("allMessages", null,
        bind(allMessages.push, allMessages));

    var fatalMessages = [];
    logger.addListener("fatalMessages", "FATAL",
        bind(fatalMessages.push, fatalMessages));

    var firstTwo = [];
    logger.addListener("firstTwo", null,
        bind(firstTwo.push, firstTwo));

    
    log("foo");
    var msgs = logger.getMessages();
    t.is( msgs.length, 1, 'global log() put one message in queue' );
    t.is( compare(allMessages, msgs), 0, "allMessages listener" );
    var msg = msgs.pop();
    t.is( compare(msg.info, ["foo"]), 0, "info matches" );
    t.is( msg.level, "INFO", "level matches" );

    logDebug("debugFoo");
    t.is( msgs.length, 0, 'getMessages() returns copy' );
    msgs = logger.getMessages();
    t.is( compare(allMessages, msgs), 0, "allMessages listener" );
    t.is( msgs.length, 2, 'logDebug()' );
    msg = msgs.pop();
    t.is( compare(msg.info, ["debugFoo"]), 0, "info matches" );
    t.is( msg.level, "DEBUG", "level matches" );

    logger.removeListener("firstTwo");

    logError("errorFoo");
    msgs = logger.getMessages();
    t.is( compare(allMessages, msgs), 0, "allMessages listener" );
    t.is( msgs.length, 3, 'logError()' );
    msg = msgs.pop();
    t.is( compare(msg.info, ["errorFoo"]), 0, "info matches" );
    t.is( msg.level, "ERROR", "level matches" );

    logWarning("warningFoo");
    msgs = logger.getMessages();
    t.is( compare(allMessages, msgs), 0, "allMessages listener" );
    t.is( msgs.length, 4, 'logWarning()' );
    msg = msgs.pop();
    t.is( compare(msg.info, ["warningFoo"]), 0, "info matches" );
    t.is( msg.level, "WARNING", "level matches" );

    logFatal("fatalFoo");
    msgs = logger.getMessages();
    t.is( compare(allMessages, msgs), 0, "allMessages listener" );
    t.is( msgs.length, 5, 'logFatal()' );
    msg = msgs.pop();
    t.is( compare(fatalMessages, [msg]), 0, "fatalMessages listener" );
    t.is( compare(msg.info, ["fatalFoo"]), 0, "info matches" );
    t.is( msg.level, "FATAL", "level matches" );
    msgs = logger.getMessages(1);
    t.is( compare(fatalMessages, msgs), 0, "getMessages with limit returns latest" );

    logger.removeListener("allMessages");
    logger.removeListener("fatalMessages");

    t.is( compare(firstTwo, logger.getMessages().slice(0, 2)), 0, "firstTwo" );
    
    logger.clear();
    msgs = logger.getMessages();
    t.is(msgs.length, 0, "clear removes existing messages");

    logger.baseLog(LogLevel.INFO, 'infoFoo');
    msg = logger.getMessages().pop();
    t.is(msg.level, "INFO", "baseLog converts level")
    logger.baseLog(45, 'errorFoo');
    msg = logger.getMessages().pop();
    t.is(msg.level, "ERROR", "baseLog converts ad-hoc level")
};
