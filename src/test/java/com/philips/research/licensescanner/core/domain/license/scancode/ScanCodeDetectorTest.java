package com.philips.research.licensescanner.core.domain.license.scancode;

import com.philips.research.licensescanner.core.domain.Package;
import com.philips.research.licensescanner.core.domain.Scan;
import com.philips.research.licensescanner.core.domain.download.AnonymousHandler;
import com.philips.research.licensescanner.core.domain.license.Detector;
import com.philips.research.licensescanner.core.domain.license.License;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ScanCodeDetectorTest {
    private static final int THRESHOLD = 50;
    private static final Package PACKAGE = new Package("ns", "name", "version");
    private static final URI LOCATION = URI.create("here");

    private final Detector detector = new ScanCodeDetector();
    private final Scan scan = new Scan(PACKAGE, LOCATION);

    private Path tempDir;

    @BeforeEach
    public void beforeEach() throws Exception {
        tempDir = Files.createTempDirectory("test");
        var location = Path.of("src", "test", "resources", "sample.zip").toUri();
        new AnonymousHandler().download(tempDir, location);
    }

    @AfterEach
    public void afterEach() throws Exception {
        FileSystemUtils.deleteRecursively(tempDir);
    }

    @Test
    void decompressesAndScansDirectory() {
        detector.scan(tempDir, scan, THRESHOLD);

        assertThat(scan.getLicense()).isNotEqualTo(License.NONE);
    }
}
