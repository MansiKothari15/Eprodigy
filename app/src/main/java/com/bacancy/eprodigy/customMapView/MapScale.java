package com.bacancy.eprodigy.customMapView;

public enum MapScale {
        LOW(1),
        HIGH(2);
        private int value;

        MapScale(int value) {
            this.value = value;
        }

    int getValue() {
            return value;
        }
    }