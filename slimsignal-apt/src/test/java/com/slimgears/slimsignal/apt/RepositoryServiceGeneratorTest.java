package com.slimgears.slimsignal.apt;// Copyright 2015 Denis Itskovich
// Refer to LICENSE.txt for license details


import com.google.common.collect.Iterables;
import com.google.common.truth.Truth;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaSourcesSubject;
import com.google.testing.compile.JavaSourcesSubjectFactory;
import com.slimgears.slimapt.AnnotationProcessingTestBase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.annotation.processing.AbstractProcessor;
import javax.tools.JavaFileObject;

/**
 * Created by Denis on 03-Apr-15
 * <File Description>
 */
@RunWith(JUnit4.class)
public class RepositoryServiceGeneratorTest extends AnnotationProcessingTestBase {
    @Test
    public void forAbstractEntities_shouldGenerate_concreteEntities() {
        testAnnotationProcessing(
                processedWith(new GenerateEntityAnnotationProcessor(), new EntityAnnotationProcessor()),
                inputFiles("ExistingEntity.java", "AbstractRelatedEntity.java", "AbstractTestEntity.java"),
                expectedFiles("TestEntity.java", "RelatedEntity.java", "ExistingEntityMeta.java"));
    }

    @Test
    public void forExistingEntities_shouldGenerate_metaModel() {
        testAnnotationProcessing(
                processedWith(new GenerateEntityAnnotationProcessor(), new EntityAnnotationProcessor()),
                inputFiles("ExistingEntity.java", "AbstractRelatedEntity.java"),
                expectedFiles("ExistingEntityMeta.java"));
    }

    @Test
    public void forRepositoryInterface_shouldGenerate_implementationAndRepositoryService() {
        testAnnotationProcessing(
                processedWith(new GenerateEntityAnnotationProcessor(), new EntityAnnotationProcessor(), new RepositoryAnnotationProcessor()),
                inputFiles("TestRepository.java", "ExistingEntity.java", "AbstractRelatedEntity.java"),
                expectedFiles("GeneratedTestRepository.java", "TestRepositoryService.java", "GeneratedTestRepositoryService.java"));
    }

    @Test
    public void forCustomOrmRepository_shouldGenerate_customRepositoryImplementationAndService() {
        testAnnotationProcessing(
                processedWith(new RepositoryAnnotationProcessor()),
                inputFiles("CustomOrmRepository.java"),
                expectedFiles("GeneratedCustomOrmRepository.java", "CustomOrmRepositoryService.java", "GeneratedCustomOrmRepositoryService.java"));
    }

    @Test
    public void forInnerRepository_shouldGenerate_repositoryImplementationAndService() {
        testAnnotationProcessing(
                processedWith(new GenerateEntityAnnotationProcessor(), new EntityAnnotationProcessor(), new RepositoryAnnotationProcessor()),
                inputFiles("RepositoryContainer.java"),
                expectedFiles("GeneratedRepositoryContainer_InnerRepository.java", "RepositoryContainer_InnerRepositoryService.java", "GeneratedRepositoryContainer_InnerRepositoryService.java"));
    }

    @Override
    protected void testAnnotationProcessing(Iterable<AbstractProcessor> processor, Iterable<JavaFileObject> inputs, Iterable<JavaFileObject> expectedOutputs) {
        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(inputs)
                .processedWith(processor)
                .compilesWithoutError()
                .and()
                .generatesSources(
                        Iterables.getFirst(expectedOutputs, null),
                        Iterables.toArray(Iterables.skip(expectedOutputs, 1), JavaFileObject.class));
    }
}
