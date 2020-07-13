package com.philips.research.licensescanner.core;

import java.net.URI;
import java.util.Optional;

/**
 * License related use-cases.
 */
public interface LicenseService {
    /**
     * Provides license per package.
     *
     * @param origin  Package manager
     * @param name    Package identifier
     * @param version Package version name
     * @return License information if the package is known.
     */
    Optional<LicenseInfo> licenseFor(String origin, String name, String version);

    /**
     * Queues package for scanning.
     *
     * @param origin  Package manager
     * @param name    Package identifier
     * @param version Package version name
     * @param vcsId   Version control coordinates
     */
    void scanLicense(String origin, String name, String version, URI vcsId);

    /**
     * @return All current scanning errors.
     */
    Iterable<ErrorReport> scanErrors();

    class LicenseInfo {
        public final String license;
        public final URI vcsUri;

        public LicenseInfo(String license, URI vcsUri) {
            this.license = license;
            this.vcsUri = vcsUri;
        }
    }

    class ErrorReport {
        public final String packageId;
        public final String version;
        public final String message;

        public ErrorReport(String name, String version, String message) {
            this.packageId = name;
            this.version = version;
            this.message = message;
        }
    }
}

