#!/bin/bash
# Run the installed AimTrainer program.

cd $(dirname "$0")
source Scripts/mysql_variables.sh
$mysqld --defaults-file="$HOME/pap22Z_z03_mysql/mysql_config.cnf" &> /dev/null &
sleep 2
java --module-path Others/openjfx/lib --add-modules=javafx.controls,javafx.fxml --add-opens java.base/java.lang=ALL-UNNAMED -cp AimTrainer.jar z03.pap22z.App
$mysqladmin -u root shutdown
