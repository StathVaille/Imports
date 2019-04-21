package com.github.stathvaille.marketimports.imports.domain;

import lombok.Data;

@Data
public class EveMarketeerValuation {
    private long typeId;
    private Value buy;
    private Value sell;

    @Data
    private class Value {
        private long volume;
        private double avg;
        private double percentile;
    }
}
