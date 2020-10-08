/*
 * This software and associated documentation files are
 *
 * Copyright © 2020-2020 Koninklijke Philips N.V.
 *
 * and is made available for use within Philips and/or within Philips products.
 *
 * All Rights Reserved
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
