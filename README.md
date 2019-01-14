[![KDoc](https://img.shields.io/badge/KDoc-read-green.svg)](https://animatedledstrip.github.io/AnimatedLEDStripKotlinPi/animatedledstrip-kotlin-pi/)

# AnimatedLEDStripKotlinPi

A port of the [AnimatedLEDStrip Library](https://github.com/maxnz/AnimatedLEDStrip) to Kotlin for Raspberry Pis

## Uses Java 9
Because we use the dokka plugin to generate our documentation, we must use Java <=9
> https://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase9-3934878.html

## Maven Coordinates/Dependency
Use the following dependency to use this library in your project
> ```
> <dependency>
>   <groupId>io.github.animatedledstrip</groupId>
>   <artifactId>animatedledstrip-kotlin-pi</artifactId>
>   <version>0.1.1</version>
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
>     <version>0.2-SNAPSHOT</version>
>   </dependency>
> </dependencies>
