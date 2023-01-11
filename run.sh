#!/bin/bash
# Run the installed AimTrainer program.

cd $(dirname "$0")
Scripts/check_java_mysql.sh || exit 1
source Scripts/mysql_variables.sh

$mysqld --defaults-file="$HOME/pap22Z_z03_mysql/mysql_config.cnf" &>/dev/null &
sleep 2

java --module-path Others/openjfx/lib \
     --add-modules=javafx.controls,javafx.fxml \
     --add-opens java.base/java.lang=ALL-UNNAMED \
     --add-exports=javafx.graphics/com.sun.glass.utils=ALL-UNNAMED \
     --add-exports=javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED \
     --add-exports=javafx.base/com.sun.javafx=ALL-UNNAMED \
     -cp AimTrainer.jar z03.pap22z.App

$mysqladmin --host=localhost --socket="$HOME/pap22Z_z03_mysql/mysqld.sock" -u root shutdown
rm *.log &> /dev/null
