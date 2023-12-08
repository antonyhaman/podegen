package com.github.kotvertolet;

import com.github.kotvertolet.annotations.PageObject;

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