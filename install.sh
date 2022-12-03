if test $UID -ne 0
then
    echo 'This script should be launched with root privileges'
    exit 1
fi

if ! which java >> /dev/null
then
    echo "Java needs to be installed for the program to be launched."
    exit 1
fi

if ! which mysql >> /dev/null
then
    sudo apt install mysql-server
fi

sudo service mysql start
sudo mysql -u root < "Scripts/setup_user.sql"
sudo mysql -u "pap22Z_z03" "-ppap.2022.PAP" "pap22Z_z03" < "Scripts/setup_database.sql"

sudo chmod +x Others/maven/bin/mvn
Others/maven/bin/mvn package

mv target/AimTrainer-1.0.0.jar .
mv AimTrainer-1.0.0.jar AimTrainer.jar
