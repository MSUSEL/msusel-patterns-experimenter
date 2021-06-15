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
package org.apache.james.imapserver.commands;

import org.apache.avalon.framework.CascadingRuntimeException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.logger.LogEnabled;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory for ImapCommand instances, provided based on the command name.
 * Command instances are created on demand, when first accessed.
 *
 *
 * @version $Revision: 1.5.2.3 $
 */
public class ImapCommandFactory
        extends AbstractLogEnabled
{
    private Map _imapCommands;

    public ImapCommandFactory()
    {
        _imapCommands = new HashMap();

        // Commands valid in any state
        // CAPABILITY, NOOP, and LOGOUT
        _imapCommands.put( CapabilityCommand.NAME, CapabilityCommand.class );
        _imapCommands.put( NoopCommand.NAME, NoopCommand.class );
        _imapCommands.put( LogoutCommand.NAME, LogoutCommand.class );

        // Commands valid in NON_AUTHENTICATED state.
        // AUTHENTICATE and LOGIN
        _imapCommands.put( AuthenticateCommand.NAME, AuthenticateCommand.class );
        _imapCommands.put( LoginCommand.NAME, LoginCommand.class );

        // Commands valid in AUTHENTICATED or SELECTED state.
        // RFC2060: SELECT, EXAMINE, CREATE, DELETE, RENAME, SUBSCRIBE, UNSUBSCRIBE, LIST, LSUB, STATUS, and APPEND
        _imapCommands.put( SelectCommand.NAME, SelectCommand.class );
        _imapCommands.put( ExamineCommand.NAME, ExamineCommand.class );
        _imapCommands.put( CreateCommand.NAME, CreateCommand.class );
        _imapCommands.put( DeleteCommand.NAME, DeleteCommand.class );
        _imapCommands.put( RenameCommand.NAME, RenameCommand.class );
        _imapCommands.put( SubscribeCommand.NAME, SubscribeCommand.class );
        _imapCommands.put( UnsubscribeCommand.NAME, UnsubscribeCommand.class );
        _imapCommands.put( ListCommand.NAME, ListCommand.class );
        _imapCommands.put( LsubCommand.NAME, LsubCommand.class );
        _imapCommands.put( StatusCommand.NAME, StatusCommand.class );
        _imapCommands.put( AppendCommand.NAME, AppendCommand.class );

//        // RFC2342 NAMESPACE
//        _imapCommands.put( "NAMESPACE", NamespaceCommand.class );

        // RFC2086 GETACL, SETACL, DELETEACL, LISTRIGHTS, MYRIGHTS
//        _imapCommands.put( "GETACL", GetAclCommand.class );
//        _imapCommands.put( "SETACL", SetAclCommand.class );
//        _imapCommands.put( "DELETEACL", DeleteAclCommand.class );
//        _imapCommands.put( "LISTRIGHTS", ListRightsCommand.class );
//        _imapCommands.put( "MYRIGHTS", MyRightsCommand.class );


        // Commands only valid in SELECTED state.
        // CHECK, CLOSE, EXPUNGE, SEARCH, FETCH, STORE, COPY, and UID
        _imapCommands.put( CheckCommand.NAME, CheckCommand.class );
        _imapCommands.put( CloseCommand.NAME, CloseCommand.class );
        _imapCommands.put( ExpungeCommand.NAME, ExpungeCommand.class );
        _imapCommands.put( CopyCommand.NAME, CopyCommand.class );
        _imapCommands.put( SearchCommand.NAME, SearchCommand.class );
        _imapCommands.put( FetchCommand.NAME, FetchCommand.class );
        _imapCommands.put( StoreCommand.NAME, StoreCommand.class );
        _imapCommands.put( UidCommand.NAME, UidCommand.class );
    }

    public ImapCommand getCommand( String commandName )
    {
        Class cmdClass = ( Class ) _imapCommands.get( commandName.toUpperCase() );

        if ( cmdClass == null ) {
            return null;
        }
        else {
            return createCommand( cmdClass );
        }
    }

    private ImapCommand createCommand( Class commandClass )
    {
        try {
            ImapCommand cmd = ( ImapCommand ) commandClass.newInstance();
            if ( cmd instanceof LogEnabled ) {
                ( ( LogEnabled ) cmd ).enableLogging( getLogger() );
            }
            if ( cmd instanceof UidCommand ) {
                ( ( UidCommand) cmd ).setCommandFactory( this );
            }
            return cmd;
        }
        catch ( Exception e ) {
            throw new CascadingRuntimeException( "Could not create command instance: " + commandClass.getName(), e );
        }
    }

}
