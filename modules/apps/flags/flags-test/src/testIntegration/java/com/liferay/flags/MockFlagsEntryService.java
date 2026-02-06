/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.flags;

import com.liferay.flags.service.FlagsEntryService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;

/**
 * @author Ankita Malik
 */
public class MockFlagsEntryService implements FlagsEntryService {

	@Override
	public void addEntry(
			String className, long classPK, String reporterEmailAddress,
			long reportedUserId, String contentTitle, String contentURL,
			String reason, ServiceContext serviceContext)
		throws PortalException {

		_reporterEmailAddress = reporterEmailAddress;
		_reportedUserId = reportedUserId;
	}

	@Override
	public String getOSGiServiceIdentifier() {
		return null;
	}

	public long getReportedUserId() {
		return _reportedUserId;
	}

	public String getReporterEmailAddress() {
		return _reporterEmailAddress;
	}

	private long _reportedUserId;
	private String _reporterEmailAddress;

}