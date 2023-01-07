#!/bin/bash
# Compile (skipping tests) the AimTrainer program.

mvn package -Dmaven.test.skip
rm dependency-reduced-pom.xml
mv target/AimTrainer-1.0.0.jar .
mv AimTrainer-1.0.0.jar AimTrainer.jar
