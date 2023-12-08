package com.github.kotvertolet.podegen;


import com.github.kotvertolet.podegen.annotations.PageObject;
import org.junit.jupiter.api.Test;

@PageObject
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    @Test
    public void test() {
        System.gc();
    }
}