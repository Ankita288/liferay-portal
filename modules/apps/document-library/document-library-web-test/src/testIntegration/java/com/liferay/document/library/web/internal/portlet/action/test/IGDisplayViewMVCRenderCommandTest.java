/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.web.internal.portlet.action.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.test.util.DLAppTestUtil;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletRenderRequest;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletRenderResponse;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import jakarta.portlet.PortletPreferences;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Akhash Ramprakash
 */
@RunWith(Arquillian.class)
public class IGDisplayViewMVCRenderCommandTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_company = _companyLocalService.getCompany(_group.getCompanyId());

		_layout = LayoutTestUtil.addTypePortletLayout(_group);

		_rootFolder = DLAppTestUtil.addFolder(_group.getGroupId());

		_childFolder = _dlAppLocalService.addFolder(
			null, TestPropsValues.getUserId(), _group.getGroupId(),
			_rootFolder.getFolderId(), RandomTestUtil.randomString(),
			RandomTestUtil.randomString(),
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId()));

		_folderOutsideRootFolder = DLAppTestUtil.addFolder(_group.getGroupId());

		PortletPreferences portletPreferences =
			PortletPreferencesFactoryUtil.getStrictPortletSetup(
				_layout, DLPortletKeys.MEDIA_GALLERY_DISPLAY);

		portletPreferences.setValue(
			"rootFolderExternalReferenceCode",
			_rootFolder.getExternalReferenceCode());

		portletPreferences.store();
	}

	@Test
	public void testRender() throws Exception {
		Assert.assertEquals(
			"/image_gallery_display/view.jsp",
			_render(
				_igDisplayViewMVCRenderCommand, _childFolder.getFolderId()));
		Assert.assertEquals(
			"/image_gallery_display/view.jsp",
			_render(_igDisplayViewMVCRenderCommand, _rootFolder.getFolderId()));
		Assert.assertEquals(
			"/document_library/error.jsp",
			_render(
				_igDisplayViewMVCRenderCommand,
				_folderOutsideRootFolder.getFolderId()));
	}

	private ThemeDisplay _getThemeDisplay() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(_company);
		themeDisplay.setLayout(_layout);
		themeDisplay.setPermissionChecker(
			PermissionThreadLocal.getPermissionChecker());
		themeDisplay.setRealUser(TestPropsValues.getUser());
		themeDisplay.setScopeGroupId(_group.getGroupId());
		themeDisplay.setUser(TestPropsValues.getUser());

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		portletDisplay.setId(DLPortletKeys.MEDIA_GALLERY_DISPLAY);

		return themeDisplay;
	}

	private String _render(MVCRenderCommand mvcRenderCommand, long folderId)
		throws Exception {

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay());
		mockHttpServletRequest.setParameter(
			"folderId", String.valueOf(folderId));

		return mvcRenderCommand.render(
			new MockLiferayPortletRenderRequest(mockHttpServletRequest),
			new MockLiferayPortletRenderResponse());
	}

	private Folder _childFolder;
	private Company _company;

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject
	private DLAppLocalService _dlAppLocalService;

	private Folder _folderOutsideRootFolder;

	@DeleteAfterTestRun
	private Group _group;

	@Inject(
		filter = "component.name=com.liferay.document.library.web.internal.portlet.action.IGDisplayViewMVCRenderCommand"
	)
	private MVCRenderCommand _igDisplayViewMVCRenderCommand;

	private Layout _layout;
	private Folder _rootFolder;

}