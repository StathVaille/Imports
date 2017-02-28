package com.github.stathvaille.marketimports.domain.location;

import lombok.Data;

/**
 * The destination where items will be imported into
 */
@Data
public class ImportLocation {
    long stationId;
    String stationName;
    long regionId;
    String regionName;
}
