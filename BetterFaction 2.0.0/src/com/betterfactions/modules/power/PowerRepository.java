package com.betterfactions.modules.power;

import java.util.Optional;

public interface PowerRepository {

    void setPower(String faction, double power);

    Optional<Double> getPower(String faction);
}
