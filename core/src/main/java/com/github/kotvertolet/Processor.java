package com.github.kotvertolet;

import com.github.kotvertolet.annotations.PageObject;
import com.github.kotvertolet.builder.PageFactoryBuilder;
import com.github.kotvertolet.data.PageObjectRecord;
import com.github.kotvertolet.data.enums.Extension;
import com.github.kotvertolet.parsers.JsonParser;
import com.github.kotvertolet.parsers.Parser;
import com.github.kotvertolet.parsers.YamlParser;
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
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({
        "com.github.kotvertolet.annotations.PageObject"
})
@AutoService(javax.annotation.processing.Processor.class)
public class Processor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    public final static String SUPPORTED_FILE_PREFIX = "POGE_";
    public final static String SUPPORTED_FORMATS_PATTERN = "(\\.yaml|\\.yml|\\.json)";
    public final static String POGE_FILES_PATTERN = String.format("^.*(%s.*%s)", SUPPORTED_FILE_PREFIX, SUPPORTED_FORMATS_PATTERN);

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
            ScanResult scanResult = new ClassGraph().acceptModules().disableJarScanning().disableNestedJarScanning()
                    .disableRuntimeInvisibleAnnotations().scan();
            if (scanResult != null) {
                try (scanResult) {
                    scanForPodegenFiles(scanResult).stream().map(this::processPageObjTemplateFile).forEach(this::generateCode);
                }
            } else {
                System.out.println("No scan results were found");
            }
            return true;
        }
        return false;
    }

    private ResourceList scanForPodegenFiles(ScanResult scanResult) {
        ResourceList rawPageFiles = scanResult.getAllResources().filter(resource ->
                resource.getPath().matches(POGE_FILES_PATTERN));
        if (!rawPageFiles.isEmpty()) {
            return rawPageFiles;
        } else {
            System.out.println("No page files were found");
            return null;
        }
    }

    private void generateCode(PageObjectRecord pageObjectRecord) {
        TypeSpec pageObjectClass = new PageFactoryBuilder(pageObjectRecord)
                .addFields(pageObjectRecord.elements())
                .addConstructor()
                .addGetters()
                .build();
        try {
            JavaFile.builder(this.getClass().getPackageName() + "." + pageObjectRecord.packages(), pageObjectClass)
                    .build()
                    .writeTo(filer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PageObjectRecord processPageObjTemplateFile(Resource rawPogeFile) {
        return getAppropriateParser(rawPogeFile.getPath()).parse(rawPogeFile);
    }

    private Parser getAppropriateParser(String path) {
        return switch (getFileExtension(path)) {
            case YML, YAML -> new YamlParser();
            case JSON -> new JsonParser();
        };
    }

    private Extension getFileExtension(String path) {
        String[] splitPath = path.split("\\.");
        if (splitPath.length == 2) {
            return Extension.get(splitPath[1]);
        } else throw new RuntimeException("Incorrect path supplied: " + path);
    }
}
