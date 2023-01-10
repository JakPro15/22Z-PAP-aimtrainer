#!/bin/bash
# Install and set up MySQL database and run the AimTrainer program.

cd $(dirname "$0")

if ! which java >> /dev/null
then
    echo "Java needs to be installed to launch the program."
    exit 1
fi

source Scripts/mysql_variables.sh

if [ ! -f $mysqld ];
then
    if [ ! -f "$HOME/pap22Z_z03_mysql/mysql.tar.xz" ];
    then
        echo "MySQL archive needs to be downloaded to '~/pap22Z_z03_mysql' under the name 'mysql.tar.xz'"
        exit 1
    fi
    echo "Unzipping MySQL archive"
    tar -xf "$HOME/pap22Z_z03_mysql/mysql.tar.xz" -C "$HOME/pap22Z_z03_mysql"
    if [ -d "$HOME/pap22Z_z03_mysql/mysql-8.0.31-linux-glibc2.12-i686" ];
    then
        mv "$HOME/pap22Z_z03_mysql/mysql-8.0.31-linux-glibc2.12-i686" "$HOME/pap22Z_z03_mysql/mysql"
    else
        mv "$HOME/pap22Z_z03_mysql/mysql-8.0.31-linux-glibc2.12-x86_64" "$HOME/pap22Z_z03_mysql/mysql"
    fi
    echo "Finished"
    if [ ! -f $mysqld ];
    then
        echo "Failed to unzip the MySQL archive."
        exit 1
    fi
    echo "Setting up MySQL"
    # prepare MySQL configuration file
    cp Others/mysql_config.cnf "$HOME/pap22Z_z03_mysql"
    sed -i "s#HOME#$HOME#" "$HOME/pap22Z_z03_mysql/mysql_config.cnf"
    sed -i "s#USER#$USER#" "$HOME/pap22Z_z03_mysql/mysql_config.cnf"
fi
$mysqld --initialize-insecure --datadir="$HOME/pap22Z_z03_mysql/mysql/data"
# launch MySQL
$mysqld --defaults-file="$HOME/pap22Z_z03_mysql/mysql_config.cnf" &
sleep 2
# set up user and database
$mysql -u root < Scripts/setup_user.sql
$mysql -u pap22Z_z03 -ppap.2022.PAP < Scripts/setup_database.sql

java --module-path Others/openjfx/lib \
     --add-modules=javafx.controls,javafx.fxml \
     --add-opens java.base/java.lang=ALL-UNNAMED \
     --add-exports=javafx.graphics/com.sun.glass.utils=ALL-UNNAMED \
     --add-exports=javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED \
     --add-exports=javafx.base/com.sun.javafx=ALL-UNNAMED \
     -cp AimTrainer.jar z03.pap22z.App

$mysqladmin -u root shutdown
