/*
 * Copyright (c) 2020-2021, Koninklijke Philips N.V., https://www.philips.com
 * SPDX-License-Identifier: MIT
 */

package com.philips.research.licensescanner.persistence;

import pl.tlinkowski.annotation.basic.NullOr;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.net.URI;

/**
 * JPA converter for storing an URI as a String.
 */
@Converter(autoApply = true)
@SuppressWarnings({"unused", "RedundantSuppression"})
class PurlConverter implements AttributeConverter<URI, String> {
    @Override
    public @NullOr String convertToDatabaseColumn(@NullOr URI uri) {
        return (uri != null) ? uri.toString() : null;
    }

    @Override
    public @NullOr URI convertToEntityAttribute(@NullOr String uri) {
        return (uri != null) ? URI.create(uri) : null;
    }
}
