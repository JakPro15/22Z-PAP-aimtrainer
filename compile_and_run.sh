#!/bin/bash
# Set up database, compile and run the AimTrainer program.

cd $(dirname "$0")
source Scripts/mysql_variables.sh

if ! which java >>/dev/null; then
    echo "Java needs to be installed to launch the program."
    exit 1
fi

if [ ! -f $mysqld ]; then
    echo "MySQL server needs to be installed to launch the program."
    exit 1
fi

# launch MySQL
$mysqld --defaults-file="$HOME/pap22Z_z03_mysql/mysql_config.cnf" &
sleep 2
# set up user and database
$mysql --host=localhost \
       --socket="$HOME/pap22Z_z03_mysql/mysqld.sock" \
       -u root \
       < Scripts/setup_user.sql
$mysql --host=localhost \
       --socket="$HOME/pap22Z_z03_mysql/mysqld.sock" \
       -u pap22Z_z03 -ppap.2022.PAP \
       < Scripts/setup_database.sql

mvn package
rm dependency-reduced-pom.xml
mv target/AimTrainer-1.0.0.jar .
mv AimTrainer-1.0.0.jar AimTrainer.jar

java --module-path Others/openjfx/lib \
     --add-modules=javafx.controls,javafx.fxml \
     --add-opens java.base/java.lang=ALL-UNNAMED \
     --add-exports=javafx.graphics/com.sun.glass.utils=ALL-UNNAMED \
     --add-exports=javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED \
     --add-exports=javafx.base/com.sun.javafx=ALL-UNNAMED \
     -cp AimTrainer.jar z03.pap22z.App
$mysqladmin --host=localhost --socket="$HOME/pap22Z_z03_mysql/mysqld.sock" -u root shutdown
