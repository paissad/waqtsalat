#!/bin/bash

#set -x

DirName=$(dirname $0)

# Set HOME

if [[ "x$WS_Home" = "x" ]]; then
    WS_Home=$(cd $DirName; pwd)
fi
export WS_Home

# Setup the JVM
if [[ "x$Java" = "x" ]]; then
    if [[ "x$JAVA_HOME" != "x" ]]; then
        Java="$JAVA_HOME/bin/java"
    else
        Java="java"
    fi
fi

# Setup the classpath
WS_Jars="$WS_Home/waqtsalat.jar"

# Execute the JVM (1st solution)
#exec \
    #"$Java" \
    #$Java_Opts \
    #-Xmx768M -Xss16M \
    #-Dfile.encoding=UTF-8 \
    #-classpath "$WS_Jars" \
    #net/waqtsalat/WaqtSalat \
    #"$@"

# Execute the JVM (2nd solution)
exec \
    "$Java" \
    $Java_Opts \
    -Xmx768M -Xss16M \
    -Dfile.encoding=UTF-8 \
    -jar "$WS_Jars" \
    "$@"

