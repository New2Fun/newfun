#!/bin/sh

if [ "${JAVA_HOME}" == "" ]; then
[ -f "${JAVA_HOME}/bin/java.exe" ]
if [ "x$?" != "x0" ] ; then
    echo "Error: JAVA_HOME environment variable not set, 2dcode not started."
	exit $?
fi
fi

"${JAVA_HOME}/bin/java" -jar 2dcode.jar $*