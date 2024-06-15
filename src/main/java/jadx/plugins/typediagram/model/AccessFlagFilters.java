package jadx.plugins.typediagram.model;

import jadx.core.dex.info.AccessInfo;

public enum AccessFlagFilters {
	All,
	PublicOnly,
	PublicPackage,
	PublicPackageProtected;

	public static boolean accept(AccessInfo accessInfo, AccessFlagFilters filter) {
		switch(filter) {
			case All:
				return true;
			case PublicOnly:
				return accessInfo.isPublic();
			case PublicPackage:
				return accessInfo.isPublic() || accessInfo.isPackagePrivate();
			case PublicPackageProtected:
				return accessInfo.isPublic() || accessInfo.isPackagePrivate() || accessInfo.isProtected();
			default:
				return false;
		}
	}
}
