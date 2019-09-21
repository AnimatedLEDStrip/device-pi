[![KDoc](https://img.shields.io/badge/KDoc-read-green.svg)](https://animatedledstrip.github.io/AnimatedLEDStripPi/animatedledstrip-pi/)
[![Build Status](https://travis-ci.com/AnimatedLEDStrip/AnimatedLEDStripPi.svg?branch=master)](https://travis-ci.com/AnimatedLEDStrip/AnimatedLEDStripPi)
[![codecov](https://codecov.io/gh/AnimatedLEDStrip/AnimatedLEDStripPi/branch/master/graph/badge.svg)](https://codecov.io/gh/AnimatedLEDStrip/AnimatedLEDStripPi)

# AnimatedLEDStripPi
A extension of the [AnimatedLEDStrip Library](https://github.com/maxnz/AnimatedLEDStrip) for Raspberry Pis.

## Uses Java 9
Because we use the dokka plugin to generate our documentation, we must use Java <=9
> https://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase9-3934878.html

## Maven Coordinates/Dependency
Use the following dependency to use this library in your project
> ```
> <dependency>
>   <groupId>io.github.animatedledstrip</groupId>
>   <artifactId>animatedledstrip-kotlin-pi</artifactId>
>   <version>0.4</version>
> </dependency>
> ```


## Snapshots
Development versions of the AnimatedLEDStrip library are available from the Sonatype snapshot repository:

> ```
> <repositories>
>    <repository>
>        <id>sonatype-snapshots</id>
>        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
>        <snapshots>
>            <enabled>true</enabled>
>        </snapshots>
>    </repository>
> </repositories>
> 
> <dependencies>
>   <dependency>
>     <groupId>io.github.animatedledstrip</groupId>
>     <artifactId>animatedledstrip-kotlin-pi</artifactId>
>     <version>0.5-SNAPSHOT</version>
>   </dependency>
> </dependencies>
