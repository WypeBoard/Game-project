package io.github.wypeboard.architecturetest;


import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "io.github.wypeboard")
public class EngineSeparationTest {

    @ArchTest
    public static final ArchRule ENGINE_SEPARATION_TEST = noClasses()
            .that()
            .resideInAPackage("io.github.wypeboard.journey.engine..")
            .should()
            .accessClassesThat()
            .resideInAPackage("io.github.wypeboard.journey.game..");
}
