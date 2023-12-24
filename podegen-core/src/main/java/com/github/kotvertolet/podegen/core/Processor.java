package com.github.kotvertolet.podegen.core;

import com.github.kotvertolet.podegen.core.annotations.PageObject;
import com.github.kotvertolet.podegen.core.builder.CodeGeneratorBuilder;
import com.github.kotvertolet.podegen.core.data.PageObjectTemplate;
import com.github.kotvertolet.podegen.core.exceptions.PodegenException;
import com.github.kotvertolet.podegen.core.parsers.JsonParser;
import com.github.kotvertolet.podegen.core.parsers.Parser;
import com.github.kotvertolet.podegen.core.parsers.YamlParser;
import com.github.kotvertolet.podegen.core.utils.Config;
import com.github.kotvertolet.podegen.core.utils.FileLogger;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.github.kotvertolet.podegen.core.utils.PathUtils.getFileExtension;


@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({
        "com.github.kotvertolet.podegen.core.annotations.PageObject"
})
@AutoService(javax.annotation.processing.Processor.class)
public class Processor extends AbstractProcessor {

    public static FileLogger logger;
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    //private static Logger logger = LoggerFactory.getLogger(Processor.class);

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        logger = new FileLogger();
        logger.log("Starting processing:");
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
            ScanResult scanResult = new ClassGraph()
                    .acceptModules()
                    .disableJarScanning()
                    .scan();

            try (scanResult) {
                searchForPageObjectTemplateFiles(scanResult)
                        .stream()
                        .map(this::processPageObjectTemplateFile)
                        .forEach(this::generateCode);
            }
            return true;
        }
        return false;
    }

    private ResourceList searchForPageObjectTemplateFiles(ScanResult scanResult) {
        ResourceList rawTemplateFiles = scanResult.getResourcesMatchingPattern(Pattern.compile(Config.getInstance().getSupportedFilesPattern()))
                .filter(resource -> resource.getURI().getPath().contains("target"));

        // Filtering duplicate resources
        rawTemplateFiles = new ResourceList(rawTemplateFiles.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(()
                        -> new TreeSet<>(Comparator.comparing(resource -> resource.getURI().getPath()))), ArrayList::new)));

        if (!rawTemplateFiles.isEmpty()) {
            return rawTemplateFiles;
        } else {
            throw new PodegenException("No suitable page object template files were found");
        }
    }

    private void generateCode(PageObjectTemplate pageObjectTemplate) {
        Config conf = Config.getInstance();
        TypeSpec pageObjectClass = CodeGeneratorBuilder.builder()
                .addFlavour(conf.getFlavourType())
                .addStrategy(conf.getStrategyType())
                .addPageObjectTemplate(pageObjectTemplate)
                .generateCode();
        try {
            String packageName = conf.getOwnerPackage() + "." + pageObjectTemplate.packages();
            JavaFile.builder(packageName, pageObjectClass)
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
        return switch (getFileExtension(path)) {
            case YML, YAML -> new YamlParser();
            case JSON -> new JsonParser();
        };
    }
}
