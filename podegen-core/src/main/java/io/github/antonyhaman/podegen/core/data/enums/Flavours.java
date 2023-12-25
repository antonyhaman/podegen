package io.github.antonyhaman.podegen.core.data.enums;

import io.github.antonyhaman.podegen.core.flavours.Flavourable;
import io.github.antonyhaman.podegen.core.flavours.SelenideFlavour;
import io.github.antonyhaman.podegen.core.flavours.SeleniumFlavour;

public enum Flavours {

    Selenium(SeleniumFlavour.class),
    Selenide(SelenideFlavour.class);

    private final Class<? extends Flavourable> flavour;

    Flavours(Class<? extends Flavourable> flavour) {
        this.flavour = flavour;
    }

    public Class<? extends Flavourable> getFlavour() {
        return flavour;
    }
}
