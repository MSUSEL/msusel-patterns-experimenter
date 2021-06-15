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
package com.ivata.groupware.navigation.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.navigation.menu.item.MenuItemDO;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.persistence.PersistenceManager;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.FindAction;
import com.ivata.mask.web.struts.InputMaskForm;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;

/**
 * Find a favorite, given it's id.
 *
 * @since ivata groupware 0.10 (2005-02-14)
 * @author Colin MacLeod
 * <a href="mailto:colin.macleod@ivata.com">colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */
public class FindFavoriteAction extends FindAction {
    public FindFavoriteAction(final PersistenceManager persistenceManagerParam,
            final MaskFactory maskFactoryParam,
            final MaskAuthenticator authenticatorParam) {
        super (persistenceManagerParam, maskFactoryParam, authenticatorParam);
    }

    /**
     * This does all the hard work of locating the favorite.
     * Refer to {@link MaskAction#execute}.
     *
     * @param mappingParam Refer to {@link MaskAction#execute}.
     * @param errorsParam Refer to {@link MaskAction#execute}.
     * @param formParam Refer to {@link MaskAction#execute}.
     * @param requestParam Refer to {@link MaskAction#execute}.
     * @param responseParam Refer to {@link MaskAction#execute}.
     * @param sessionParam Refer to {@link MaskAction#execute}.
     * @return Refer to {@link MaskAction#execute}.
     * @throws SystemException Refer to {@link MaskAction#execute}.
     */
    public String execute(final ActionMapping mappingParam,
            final ActionErrors errorsParam,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session)
            throws SystemException {
        setBaseClassName(MenuItemDO.class.getName());
        String forward =
            super.execute(mappingParam, errorsParam, form, request,
                response, session);

        // we don't want to warn on delete, and we don't want the new or apply
        // buttons to show, and we want to close on ok.
        InputMaskForm inputMaskForm = (InputMaskForm)
            request.getAttribute(InputMaskForm.REQUEST_ATTRIBUTE);
        assert (inputMaskForm != null);
        // we don't want a warning for deleting favorites!
        inputMaskForm.setDeleteWithoutWarn(true);
        // only delete/ok/cancel buttons needed
        inputMaskForm.setApplyButtonHidden(true);
        inputMaskForm.setClearButtonHidden(true);
        // close the pop-up on ok
        inputMaskForm.setDefaultForwardDelete("utilClosePopUp");
        inputMaskForm.setDefaultForwardOk("utilClosePopUp");
        // path to the field labels in the application resources
        inputMaskForm.setResourceFieldPath("navigationFavorite");
        // refresh the pop-up opener
        inputMaskForm.setRefreshOpener(true);
        return forward;
    }
}
