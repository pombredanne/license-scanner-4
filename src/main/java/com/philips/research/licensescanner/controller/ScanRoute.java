/*
 * This software and associated documentation files are
 *
 * Copyright © 2020-2020 Koninklijke Philips N.V.
 *
 * and is made available for use within Philips and/or within Philips products.
 *
 * All Rights Reserved
 */

package com.philips.research.licensescanner.controller;


import com.philips.research.licensescanner.core.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.tlinkowski.annotation.basic.NullOr;

import java.time.Instant;
import java.util.UUID;

/**
 * REST API for interacting with scan results.
 */
@RestController
@Validated
@CrossOrigin(origins = "*")
@RequestMapping("/scans")
public class ScanRoute {
    private final LicenseService service;

    @Autowired
    public ScanRoute(LicenseService service) {
        this.service = service;
    }

    /**
     * Lists all successful scans in the given period.
     *
     * @param start (Optional) start timestamp
     * @param end   (Optional) end timestamp: defaults to "now"
     */
    @GetMapping()
    SearchResultJson<ScanInfoJson> latestScans(@NullOr @RequestParam(required = false) Instant start,
                                               @NullOr @RequestParam(required = false) Instant end,
                                               @RequestParam(name = "q", required = false, defaultValue = "") String query) {
        final var stats = service.statistics();

        if (query.startsWith("error")) {
            return new SearchResultJson<>(stats, ScanInfoJson.toStream(service.findErrors()));
        }
        if (query.startsWith("contest")) {
            return new SearchResultJson<>(stats, ScanInfoJson.toStream(service.findContested()));
        }
        final var scans = service.findScans(
                start != null ? start : Instant.EPOCH,
                end != null ? end : Instant.now());

        return new SearchResultJson<>(stats, ScanInfoJson.toStream(scans));
    }

    @GetMapping("{uuid}")
    ScanInfoJson getScanById(@PathVariable UUID uuid) {
        final var scan = service.getScan(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(uuid));

        return new ScanInfoJson(scan);
    }

    @PutMapping("{uuid}")
    void confirmLicense(@PathVariable UUID uuid, @RequestBody LicenseJson body) {
        service.curateLicense(uuid, body.license);
    }

    @PostMapping("{uuid}/contest")
    void contestScan(@PathVariable UUID uuid, @RequestBody(required = false) @NullOr LicenseJson body) {
        final @NullOr String license = (body != null) ? body.license : null;

        service.contest(uuid, license);
    }

    @PostMapping("{uuid}/ignore/{license}")
    void ignoreDetection(@PathVariable UUID uuid, @PathVariable String license,
                         @RequestParam(required = false) boolean revert) {
        if (!revert) {
            service.ignore(uuid, license);
        } else {
            service.restore(uuid, license);
        }
    }

    @GetMapping("{uuid}/source/{license}")
    FragmentJson detectionSource(@PathVariable UUID uuid, @PathVariable String license,
                                 @RequestParam(required = false, defaultValue = "5") int margin) {
        final var dto = service.sourceFragment(uuid, license, margin)
                .orElseThrow(() -> new ResourceNotFoundException("" + uuid + "/" + license));
        return new FragmentJson(dto);
    }
}
