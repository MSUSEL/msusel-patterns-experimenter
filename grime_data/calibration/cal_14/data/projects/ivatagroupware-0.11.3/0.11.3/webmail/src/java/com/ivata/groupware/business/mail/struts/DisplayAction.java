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
package com.ivata.groupware.business.mail.struts;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.picocontainer.PicoContainer;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.admin.security.user.UserDO;
import com.ivata.groupware.admin.setting.Settings;
import com.ivata.groupware.business.mail.Mail;
import com.ivata.groupware.business.mail.message.MessageDO;
import com.ivata.groupware.business.mail.server.NoMailServerException;
import com.ivata.groupware.business.mail.session.MailSession;
import com.ivata.groupware.container.PicoContainerFactory;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;


/**
 * <p>This <code>Action</code> is invoked when displaying a message.
 * This action also handles replying to, forwarding or editing a mail
 * for resending.</p>
 *
 * @since 2002-11-11
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class DisplayAction extends MaskAction {
    Mail mail;
    Settings settings;
    /**
     * TODO
     * @param mail
     * @param settings
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public DisplayAction(Mail mail, Settings settings,
            MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        this.mail = mail;
        this.settings = settings;
    }

    /**
     * <p>Build the form for <code>/mail/display.jsp</code> from an
     * existing mail.</p>
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param userName valid, non-null user name from session.
     * @param settings valid, non-null settings from session.
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     * @return this method returns the string used to identify the correct
     * <em>Struts</em> <code>ActionForward</code> which should follow this
     * page, or <code>null</code> if it should return to the input.
     */
    public String execute(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session) throws SystemException {
        // Was this transaction cancelled? For us, that means the close button
        // was pressed
        if (isCancelled(request)) {
            return "mailIndex";
        }

        DisplayForm displayForm = (DisplayForm) form;
        MessageDO message = displayForm.getMessage();

        // request parameters override the form ones
        if (request.getParameter("folderName") != null) {
            displayForm.setFolderName(request.getParameter("folderName"));
        }

        if (request.getParameter("id") != null) {
            displayForm.setId(request.getParameter("id"));
        }

        // used if we forward or delete the message
        Vector idVector = new Vector();
        idVector.add(message.getMessageID());

        // is there a new thread (forward/reply) from this message?
        MailSession mailSession;
        try {
            mailSession = (MailSession) session.getAttribute("securitySession");
        } catch (ClassCastException e) {
            session.setAttribute("mailServerException",
                new NoMailServerException(e));
            return "serverError";
        }
        UserDO user = mailSession.getUser();

        if (displayForm.getThread() != null) {
            SecuritySession securitySession = (SecuritySession)
                    session.getAttribute("securitySession");
            PicoContainer picoContainer = securitySession.getContainer();
            ComposeForm composeForm = (ComposeForm)
                    PicoContainerFactory.getInstance()
                        .instantiateOrOverride(picoContainer,
                            ComposeForm.class);

            MessageDO newMessage = mail.createThreadMessage(mailSession,
                    displayForm.getFolderName(), idVector,
                    displayForm.getThread());
            composeForm.setMessage(newMessage);
            session.setAttribute("mailComposeForm", composeForm);

            return "mailCompose";
        }

        // move the current message to trash?
        if (displayForm.getDelete()) {
            String trashFolderName = settings.getStringSetting(mailSession,
                    "emailFolderTrash",
                    user);
            mail.moveMessages(mailSession, displayForm.getFolderName(),
                idVector, trashFolderName);
            request.setAttribute("refreshOpener", "1");

            return "success";
        }

        // delete the current message from trash
        if (displayForm.getDeleteTrash()) {
            mail.deleteMessagesFromTrash(mailSession, idVector);
        }

        // ok, so get the message we actually want to show
        message = mail.findMessageByFolderMessageId(mailSession,
                displayForm.getFolderName(), displayForm.getId());

        // if we didn't find message forward to page with info text
        if (message == null) {
            return "noMessage";
        }

        displayForm.setMessage(message);

        // down here means we've found our message and want to display it
        return "mailDisplay";
    }
}
