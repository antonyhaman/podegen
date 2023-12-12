package com.github.kotvertolet.podegen;


import com.github.kotvertolet.podegen.annotations.PageObject;
import com.github.kotvertolet.podegen.data.enums.Flavours;
import com.github.kotvertolet.podegen.data.enums.Strategies;

@PageObject(flavour = Flavours.Selenide, strategy = Strategies.PageFactory)
public class Main {

    public static void main(String[] args) {

    }
}