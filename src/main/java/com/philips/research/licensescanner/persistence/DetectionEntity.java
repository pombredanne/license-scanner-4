/*
 * Copyright (c) 2020-2021, Koninklijke Philips N.V., https://www.philips.com
 * SPDX-License-Identifier: MIT
 */

package com.philips.research.licensescanner.persistence;

import com.philips.research.licensescanner.core.domain.Detection;
import com.philips.research.licensescanner.core.domain.license.License;
import pl.tlinkowski.annotation.basic.NullOr;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@SuppressWarnings("JpaDataSourceORMInspection")
@Table(name = "detections")
public class DetectionEntity extends Detection {
    @Id
    @GeneratedValue
    @SuppressWarnings({"unused", "RedundantSuppression"})
    private @NullOr Long id;

    public DetectionEntity() {
        this(License.NONE);
    }

    public DetectionEntity(License license) {
        super(license);
    }
}
