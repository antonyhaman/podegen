package io.github.antonyhaman.podegen.core;

import com.codeborne.selenide.Selenide;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import io.github.antonyhaman.podegen.core.annotations.PageObject;
import io.github.antonyhaman.podegen.core.builder.CodeGeneratorBuilder;
import io.github.antonyhaman.podegen.core.data.PageObjectTemplate;
import io.github.antonyhaman.podegen.core.exceptions.PodegenException;
import io.github.antonyhaman.podegen.core.flavors.SelenideFlavor;
import io.github.antonyhaman.podegen.core.parsers.JsonParser;
import io.github.antonyhaman.podegen.core.parsers.Parser;
import io.github.antonyhaman.podegen.core.parsers.YamlParser;
import io.github.antonyhaman.podegen.core.utils.Config;
import io.github.antonyhaman.podegen.core.utils.PathUtils;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ResourceList;
import io.github.classgraph.ScanResult;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({
        "io.github.antonyhaman.podegen.core.annotations.PageObject"
})
@AutoService(javax.annotation.processing.Processor.class)
public class PodegenProcessor extends AbstractProcessor {
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        var annotationsSet = roundEnv.getElementsAnnotatedWith(PageObject.class);
        if (!annotationsSet.isEmpty()) {
            if (annotationsSet.size() > 1) {
                throw new PodegenException("More than one PageObject annotations aren't allowed");
            }
            Config.initConfig(annotationsSet.stream().findAny().get());

            try (ScanResult scanResult = scanForResources()) {
                searchForPageObjectTemplateFiles(scanResult)
                        .stream()
                        .map(this::processPageObjectTemplateFile)
                        .forEach(this::generateCode);
            }
            return true;
        }
        return false;
    }

    private ScanResult scanForResources() {
        return new ClassGraph()
                .acceptModules()
                .disableJarScanning()
                .scan();
    }

    private ResourceList searchForPageObjectTemplateFiles(ScanResult scanResult) {
        ResourceList rawTemplateFiles = scanResult.getResourcesMatchingPattern(Config.getInstance().getSupportedFileFormatsPattern())
                .filter(resource -> resource.getURI().getPath().contains("target"));

        // Filtering out duplicate resources
        rawTemplateFiles = new ResourceList(rawTemplateFiles.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(resource -> resource.getURI().getPath()))), ArrayList::new)));

        if (!rawTemplateFiles.isEmpty()) {
            return rawTemplateFiles;
        } else {
            throw new PodegenException("No suitable page object template files were found");
        }
    }

    private void generateCode(PageObjectTemplate pageObjectTemplate) {
        Config conf = Config.getInstance();
        TypeSpec pageObjectClass = CodeGeneratorBuilder.builder()
                .addFlavor(conf.getFlavorType())
                .addStrategy(conf.getStrategyType())
                .addPageObjectTemplate(pageObjectTemplate)
                .generateCode();
        try {
            String packageName = conf.getOwnerPackage() + "." + pageObjectTemplate.packages();
            JavaFile.Builder builder = JavaFile.builder(packageName, pageObjectClass);
            if (conf.getFlavorType().equals(SelenideFlavor.class)) {
                builder.addStaticImport(Selenide.class, "*");
            }
            builder
                    .build()
                    .writeTo(filer);
        } catch (IOException e) {
            throw new PodegenException(e);
        }
    }

    private PageObjectTemplate processPageObjectTemplateFile(Resource rawTemplateFile) {
        return getAppropriateParser(rawTemplateFile.getPath()).parse(rawTemplateFile);
    }

    private Parser getAppropriateParser(String path) {
        return switch (PathUtils.getFileExtension(path)) {
            case YML, YAML -> new YamlParser();
            case JSON -> new JsonParser();
        };
    }
}
