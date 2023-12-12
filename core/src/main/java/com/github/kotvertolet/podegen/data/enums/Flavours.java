package com.github.kotvertolet.podegen.data.enums;

import com.github.kotvertolet.podegen.flavours.Flavourable;
import com.github.kotvertolet.podegen.flavours.SelenideFlavour;
import com.github.kotvertolet.podegen.flavours.SeleniumFlavour;

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
