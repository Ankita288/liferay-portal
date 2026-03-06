/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

/**
 * @author Ankita Malik
 */

package com.liferay.fragment.internal.upgrade.v3_0_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.service.test.BaseTemplateUpgradeProcessTestCase;
import com.liferay.fragment.internal.upgrade.v3_0_0.FragmentEntryHtmlUpgradeProcess;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.model.FragmentEntryVersion;
import com.liferay.fragment.service.FragmentEntryLocalService;
import com.liferay.portal.test.rule.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class FragmentEntryHtmlUpgradeProcessTest
	extends BaseTemplateUpgradeProcessTestCase {

	@Test
	public void testFragmentEntryHtmlIsUpdatedFromVersion() throws Exception {

		addFragmentEntry(".v3_0_0/fragment-entry.html");

		FragmentEntry fragmentEntry = getFragmentEntry();

		FragmentEntryVersion fragmentEntryVersion =
			_fragmentEntryLocalService.fetchLatestVersion(fragmentEntry);

		String versionHtml = fragmentEntryVersion.getHtml();

		fragmentEntry.setHtml("OLD_HTML");

		_fragmentEntryLocalService.updateFragmentEntry(fragmentEntry);

		runTemplateUpgrade();

		FragmentEntry updatedFragmentEntry = getFragmentEntry();

		Assert.assertEquals(
			versionHtml, updatedFragmentEntry.getHtml());
	}

	@Override
	protected String getUpgradeStepClassName() {
		return FragmentEntryHtmlUpgradeProcess.class.getName();
	}

	@Inject
	private FragmentEntryLocalService _fragmentEntryLocalService;

}