package de.luckydev.blueark.detect;

import de.luckydev.blueark.detect.movement.FlyDetection;

public enum DetectionManager {
    FLY_DETECT(new FlyDetection());

    public Detection detection;
    DetectionManager(Detection detection) {
        this.detection = detection;
    }
}
