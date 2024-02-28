#!/bin/bash
# Compile (skipping tests) the AimTrainer program.

cd $(dirname "$0")

mvn clean package -Dmaven.test.skip
rm dependency-reduced-pom.xml
mv target/AimTrainer-1.0.0.jar .
mv AimTrainer-1.0.0.jar AimTrainer.jar
rm *.log
