package com.github.stathvaille.marketimports.ui.domain;

import lombok.Data;

import java.util.List;

/**
 * Data for the bootstrap-table url
 */

@Data
public class BootstrapTableData {
    private int total;
    private List<BootstrapTableRow> rows;


}

