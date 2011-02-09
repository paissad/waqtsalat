#!/bin/bash

# Exit on most errors.
set -e

libdir=lib
maven="mvn -e"
localRepo="maven-repository"

# jargs
${maven} install:install-file \
    -Dfile=${libdir}/jargs.jar \
    -DgroupId=jargs \
    -DartifactId=jargs \
    -Dversion=0 \
    -Dpackaging=jar \
    -DlocalRepositoryPath=${localRepo}

# jl (javazoom player)
${maven} install:install-file \
    -Dfile=${libdir}/jl1.0.jar \
    -DgroupId=jl \
    -DartifactId=jl \
    -Dversion=1.0 \
    -Dpackaging=jar \
    -DlocalRepositoryPath=${localRepo}


# geoip
${maven} install:install-file \
    -Dfile=${libdir}/geoip-1.2.4.jar \
    -DgroupId=geoip \
    -DartifactId=geoip \
    -Dversion=1.2.4 \
    -Dpackaging=jar \
    -DlocalRepositoryPath=${localRepo}


# cron4j
${maven} install:install-file \
    -Dfile=${libdir}/cron4j-2.2.2.jar \
    -DgroupId=cron4j \
    -DartifactId=cron4j \
    -Dversion=2.2.2 \
    -Dpackaging=jar \
    -DlocalRepositoryPath=${localRepo}


