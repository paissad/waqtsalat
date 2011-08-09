#!/bin/bash

#set -x

DirName=$(dirname $0)

# Set HOME

if [[ "x$WS_HOME" = "x" ]]; then
    WS_HOME=$(cd $DirName; pwd)
fi
export WS_HOME

# Setup the JVM
if [[ "x$Java" = "x" ]]; then
    if [[ "x$JAVA_HOME" != "x" ]]; then
        Java="$JAVA_HOME/bin/java"
    else
        Java="java"
    fi
fi

# Setup the classpath
WS_JARS="$WS_HOME/waqtsalat.jar"

# Execute the JVM (1st solution)
#exec \
    #"$Java" \
    #$Java_Opts \
    #-Xmx768M -Xss16M \
    #-Dfile.encoding=UTF-8 \
    #-classpath "$WS_JARS" \
    #net/waqtsalat/WaqtSalat \
    #"$@"

# Execute the JVM (2nd solution)
exec \
    "$Java" \
    $Java_Opts \
    -Xmx768M -Xss16M \
    -Dfile.encoding=UTF-8 \
    -jar "$WS_JARS" \
    "$@"

