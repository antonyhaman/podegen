package io.github.antonyhaman.podegen.core.data.enums;

import io.github.antonyhaman.podegen.core.flavors.Flavorable;
import io.github.antonyhaman.podegen.core.flavors.SelenideFlavor;
import io.github.antonyhaman.podegen.core.flavors.SeleniumFlavor;

public enum Flavors {

    Selenium(SeleniumFlavor.class),
    Selenide(SelenideFlavor.class);

    private final Class<? extends Flavorable> flavor;

    Flavors(Class<? extends Flavorable> flavor) {
        this.flavor = flavor;
    }

    public Class<? extends Flavorable> getFlavor() {
        return flavor;
    }
}
