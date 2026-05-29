/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.web.internal.portlet.action;

import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.document.library.web.internal.display.context.helper.DLPortletInstanceSettingsHelper;
import com.liferay.document.library.web.internal.display.context.helper.IGRequestHelper;
import com.liferay.document.library.web.internal.helper.DLTrashHelper;
import com.liferay.document.library.web.internal.util.DLFolderUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.repository.model.Folder;

import jakarta.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = {
		"jakarta.portlet.name=" + DLPortletKeys.MEDIA_GALLERY_DISPLAY,
		"mvc.command.name=/image_gallery_display/view_slide_show"
	},
	service = MVCRenderCommand.class
)
public class ViewSlideShowMVCRenderCommand extends BaseFolderMVCRenderCommand {

	@Override
	protected DLTrashHelper getDLTrashHelper() {
		return _dlTrashHelper;
	}

	@Override
	protected String getPath() {
		return "/image_gallery_display/view_slide_show.jsp";
	}

	@Override
	protected void validateFolder(
			HttpServletRequest httpServletRequest, Folder folder)
		throws PortalException {

		DLPortletInstanceSettingsHelper dlPortletInstanceSettingsHelper =
			new DLPortletInstanceSettingsHelper(
				new IGRequestHelper(httpServletRequest));

		DLFolderUtil.validateFolder(
			folder, dlPortletInstanceSettingsHelper.getRootFolderId());
	}

	@Reference
	private DLTrashHelper _dlTrashHelper;

}