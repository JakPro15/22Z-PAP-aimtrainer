#!/bin/bash

if test $UID -ne 0 >> /dev/null
then
    echo "This script should be launched with root privileges"
    exit 1
fi

cd $(dirname "$0")
source Scripts/find_package_manager.sh

if ! which java >> /dev/null
then
    $package_manager install openjdk-17-jre
fi

if ! which mysql >> /dev/null
then
    $package_manager install mysql-server
fi

service mysql start
if [ -z "$1" ];
then
    mysql -u root < "Scripts/setup_user.sql"
else
    if [ -z "$2" ];
    then
        mysql -u $1 < "Scripts/setup_user.sql"
    else
        mysql -u $1 "-p$2" < "Scripts/setup_user.sql"
    fi
fi
mysql -u "pap22Z_z03" "-ppap.2022.PAP" "pap22Z_z03" < "Scripts/setup_database.sql"

Others/maven/bin/mvn package
chown -R $SUDO_USER: target  # so that the non-root user is the owner of target
rm dependency-reduced-pom.xml
mv target/AimTrainer-1.0.0.jar .
mv AimTrainer-1.0.0.jar AimTrainer.jar

./run.sh
