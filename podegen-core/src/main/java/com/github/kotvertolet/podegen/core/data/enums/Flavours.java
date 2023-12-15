package com.github.kotvertolet.podegen.core.data.enums;

import com.github.kotvertolet.podegen.core.flavours.Flavourable;
import com.github.kotvertolet.podegen.core.flavours.SelenideFlavour;
import com.github.kotvertolet.podegen.core.flavours.SeleniumFlavour;

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
