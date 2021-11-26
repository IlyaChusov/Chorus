package com.johnny.chorus.model;

public enum SongType {
    TROPAR {
        public String getRussian() {
            return "Тропарь";
        }
    },
    STIKHIRA {
        public String getRussian() {
            return "Стихира";
        }
    };
    public abstract String getRussian();
}
