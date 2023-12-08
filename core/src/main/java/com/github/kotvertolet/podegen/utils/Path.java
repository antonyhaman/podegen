package com.github.kotvertolet.podegen.utils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.kotvertolet.podegen.Processor.SUPPORTED_FILE_PREFIX;
import static com.github.kotvertolet.podegen.Processor.SUPPORTED_FORMATS_PATTERN;

public class Path {

    private final String[] qualifiedClassNameArr;
    private String qualifiedClassName;
    private String className;
    private String pkg;

    public Path(String path) {
        qualifiedClassNameArr = path.replaceAll(SUPPORTED_FORMATS_PATTERN, "")
                .replaceAll(SUPPORTED_FILE_PREFIX, "").replace("/", ".").split("\\.");
    }

    public String getQualifiedClassName() {
        if (qualifiedClassName == null) {
            qualifiedClassName = String.join(".", qualifiedClassNameArr);
        }
        return qualifiedClassName;
    }

    public String getClassName() {
        if (className == null) {
            className = qualifiedClassNameArr[qualifiedClassNameArr.length - 1];
        }
        return className;
    }

    public String getPackage() {
        if (pkg == null) {
            pkg = Stream.of(qualifiedClassNameArr).takeWhile(str -> !str.equals(getClassName()))
                    .collect(Collectors.joining("."));
        }
        return pkg;
    }
}
