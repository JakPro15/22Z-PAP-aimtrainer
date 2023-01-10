#!/bin/bash
# Check whether Java and MySQL are installed.

cd $(dirname "$0")
source mysql_variables.sh

if ! which java >>/dev/null; then
    echo "Java needs to be installed to launch the program."
    exit 1
fi

if [ ! -f $mysqld ]; then
    echo "MySQL server needs to be installed to launch the program."
    exit 1
fi
