package com.github.kotvertolet.podegen.data.enums;

import com.github.kotvertolet.podegen.strategies.PageFactoryStrategy;
import com.github.kotvertolet.podegen.strategies.PageObjectStrategy;
import com.github.kotvertolet.podegen.strategies.Strategy;

public enum Strategies {

    PageFactory(PageFactoryStrategy.class),
    PageObject(PageObjectStrategy.class);

    private final Class<? extends Strategy> strategy;

    Strategies(Class<? extends Strategy> strategy) {
        this.strategy = strategy;
    }

    public Class<? extends Strategy> getStrategy() {
        return strategy;
    }
}
