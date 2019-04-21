package com.github.stathvaille.marketimports.imports.domain.location;

import lombok.Data;

/**
 * The destination where items will be imported into
 */
@Data
public class ImportLocation {
    long stationId;
    String stationName;
//    long systemId;
//    String systemName;
    long regionId;
    String regionName;
}
