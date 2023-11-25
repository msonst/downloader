[![Test on Checkin](${GITBASE}/downloader/actions/workflows/test.yml/badge.svg)](${GITBASE}/downloade-server/actions/workflows/test.yml) 
[![Maven Central & GitHub](${GITBASE}/downloade-server/actions/workflows/publish.yml/badge.svg)](${GITBASE}/downloader/actions/workflows/publish.yml)
# Stand-alone File Downloader with REST API and Plugins. (Resuming)


## Introduction



## Features


## Usage

### Prerequisites

- Java Development Kit (JDK) 8 or later.

### Maven Dependency

Synchronous download library. (Multithreading)

	<dependency>
	  <groupId>${GROUP}</groupId>
	  <artifactId>${project.name}</artifactId>
	  <version>${VERSION}</version>
	</dependency>	

### Run the Server

	java -jar download-server-<VERSION>.jar
	
### Run the (Demo) Client

	usage: download-client [-a <url>] [-c] -h <host[:Port]> [-o <cookie>] [-s <id>]
		-a,--add <url>            Add a new download to the queue. Returns downloadId
		-c,--status               Request the current status
		-h,--host <host[:Port]>   Address of the host
		-o,--cookie <cookie>      Pass a cookie during add.
		-s,--start <id>           Start a download
	
	Example: 
	java -jar download-client-<VERSION>.jar -h localhost:9090 -a "https://github.com/jamesward/play-load-tests/raw/master/public/10mb.txt" -s


### Clone the repository:
	
	git clone ${GITBASE}/downloader.git
   
### Build the project:

	cd downloader
	./gradlew build

# Contributing
Contributions are welcome! Please follow the [Contribution Guidelines](${GITBASE}/downloade-server/blob/main/CONTRIBUTING.md) .

# Acknowledgements

*   Special thanks to contributors and the open-source community.
*	If you find this project helpful, consider giving it a star!
