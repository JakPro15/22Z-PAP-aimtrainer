#!/bin/bash
# Set up variables for easier access to MySQL binaries mysqld, mysql and mysqladmin.

mysqld="$HOME/pap22Z_z03_mysql/mysql/bin/mysqld"
mysql="$HOME/pap22Z_z03_mysql/mysql/bin/mysql --host=localhost --socket=$HOME/pap22Z_z03_mysql/mysqld.sock"
mysqladmin="$HOME/pap22Z_z03_mysql/mysql/bin/mysqladmin --host=localhost --socket=$HOME/pap22Z_z03_mysql/mysqld.sock"
