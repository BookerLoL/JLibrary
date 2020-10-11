# Apache Maven
## Resources / References
- https://maven.apache.org/guides/getting-started/index.html

## Introduction
Open source project management framework.
Simplifies build process and automates project management tasks.

## Popularity
- Standarized directory structure
- Handles dependencies 
- Plug-in based (easy to customize)
- Archtypes
- Open Source

## Versions
- 1.0, 2004
- 2.0, 2005
- 3.0, 2010

## Other Build Tools
- Gradle
- Ant + Ivy
- Other


## Setting Up
- Windows
    1) https://maven.apache.org/download.cgi
        - Binary zip archive
        - Unzip and place it somewhere to be found easily
    2) Set up System variables
        - M2_HOME = pathToUnzippedFolder
        - M2 = %M2_HOME%\bin
        - MAVEN_OPTS = -Xms256m -Xmx512m
            - This deals with extending memory usage
    3) Test if installed
        - Open command prompt
        - mvn -v 
            - should get the maven version, java version, etc

## Get Dependencies
- https://mvnrepository.com/
    - Look up the package you want to use
    - copy the dependency 
    - put it within your pom.xml file
- There are other websites too, or just visit source for the info
```XML
<!-- exmple -->
<dependencies>
	<dependency>
		<groupId>junit</groupId>
		<artifactId>junit</artifactId>
		<version>4.13</version>
		<scope>test</scope>
	</dependency>
</dependencies>
```


45