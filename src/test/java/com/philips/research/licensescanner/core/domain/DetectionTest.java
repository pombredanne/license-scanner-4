package com.philips.research.licensescanner.core.domain;

import com.philips.research.licensescanner.core.domain.license.License;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class DetectionTest {
    private static final License LICENSE = License.of("License");
    private static final int SCORE = 73;
    private static final File FILE = new File("path/to/file.txt");
    private static final int START_LINE = 10;
    private static final int END_LINE = 20;

    private final Detection detection = new Detection(LICENSE);

    @Test
    void createsInstance() {
        assertThat(detection.getLicense()).isEqualTo(LICENSE);
        assertThat(detection.getFilePath()).isNotNull();
        assertThat(detection.getScore()).isZero();
    }

    @Test
    void confirmsDetection() {
        detection.addEvidence(SCORE, FILE, START_LINE, END_LINE);

        assertThat(detection.getScore()).isEqualTo(73);
        assertThat(detection.getConfirmations()).isEqualTo(1);
        assertThat(detection.getFilePath()).isEqualTo(FILE);
        assertThat(detection.getStartLine()).isEqualTo(START_LINE);
        assertThat(detection.getEndLine()).isEqualTo(END_LINE);
    }

    @Test
    void onlyCountsLowerAndEqualScoringEvidence() {
        detection.addEvidence(SCORE, FILE, START_LINE, END_LINE);

        detection.addEvidence(SCORE, new File("other.txt"), 666, 666);
        detection.addEvidence(SCORE - 1, new File("other.txt"), 666, 666);

        assertThat(detection.getScore()).isEqualTo(SCORE);
        assertThat(detection.getFilePath()).isEqualTo(FILE);
        assertThat(detection.getConfirmations()).isEqualTo(3);
    }

    @Test
    void implementsEquals() {
        EqualsVerifier.forClass(Detection.class)
                .withOnlyTheseFields("license")
                .withPrefabValues(License.class, License.of("x"), License.of("y"))
                .verify();
    }
}