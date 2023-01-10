#!/bin/bash
# Run unit and/or integration tests for the AimTrainer program. Resets the database.

cd $(dirname "$0")
Scripts/check_java_mysql.sh || exit 1
source Scripts/mysql_variables.sh

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

mvn clean test "-Dtest=$1"

$mysqladmin --host=localhost --socket="$HOME/pap22Z_z03_mysql/mysqld.sock" -u root shutdown
rm *.log
