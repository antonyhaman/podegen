package io.github.antonyhaman.podegen.core.data.enums;

import io.github.antonyhaman.podegen.core.strategies.PageFactoryStrategy;
import io.github.antonyhaman.podegen.core.strategies.PageObjectStrategy;
import io.github.antonyhaman.podegen.core.strategies.Strategy;

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
