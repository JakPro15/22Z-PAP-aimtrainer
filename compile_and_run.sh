#!/bin/bash
# Set up database, compile and run the AimTrainer program.

if ! which java >> /dev/null
then
    echo "Java needs to be installed to launch the program."
    exit 1
fi

if [ ! -f "$HOME/pap22Z_z03_mysql/mysql/bin/mysqld" ];
then
    echo "MySQL server needs to be installed to launch the program."
    exit 1
fi

mysql="$HOME/pap22Z_z03_mysql/mysql/bin"

# launch MySQL
"$mysql/mysqld" --defaults-file="$HOME/pap22Z_z03_mysql/mysql_config.cnf" &
sleep 2
# set up user and database
"$mysql/mysql" --host=localhost --socket="$HOME/pap22Z_z03_mysql/mysqld.sock" -u root < Scripts/setup_user.sql
"$mysql/mysql" --host=localhost --socket="$HOME/pap22Z_z03_mysql/mysqld.sock" -u pap22Z_z03 -ppap.2022.PAP < Scripts/setup_database.sql

mvn package
rm dependency-reduced-pom.xml
mv target/AimTrainer-1.0.0.jar .
mv AimTrainer-1.0.0.jar AimTrainer.jar

java --module-path Others/openjfx/lib --add-modules=javafx.controls,javafx.fxml --add-opens java.base/java.lang=ALL-UNNAMED -cp AimTrainer.jar z03.pap22z.App
"$mysql/mysqladmin" --host=localhost --socket="$HOME/pap22Z_z03_mysql/mysqld.sock" -u root shutdown