package com.github.kotvertolet.podegen.core.utils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathUtils {

    private final String[] qualifiedClassNameArr;
    private String qualifiedClassName;
    private String className;
    private String pkg;

    public PathUtils(String path) {
        Config conf = Config.getInstance();
        qualifiedClassNameArr = path.replaceAll(conf.getSupportedFilesFormats(), "")
                .replaceAll(conf.getTemplateFilePrefix(), "")
                .replace("/", ".")
                .split("\\.");
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