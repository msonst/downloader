[![Test on Checkin](https://github.com/msonst/downloader/actions/workflows/test.yml/badge.svg)](https://github.com/msonst/downloade-server/actions/workflows/test.yml) 
[![Maven Central & GitHub](https://github.com/msonst/downloade-server/actions/workflows/publish.yml/badge.svg)](https://github.com/msonst/downloader/actions/workflows/publish.yml)
# Stand-alone File Downloader with REST API and Plugins. (Resuming)


## Introduction

This Java project provides a resumable stand-alone file download mechanism with support for parallel processing using multiple threads. It allows for efficient and flexible downloading of large files while providing resumable capabilities in case of interruptions.

## Features

- Accepts download requests via REST.
- Configurable parallel downloads.
- Parallel downloading with configurable thread count.
- Support for resuming downloads.
- Customizable download status listeners.

## Usage

### Prerequisites

- Java Development Kit (JDK) 8 or later.

### Maven Dependency

Synchronous download library. (Multithreading)

	<dependency>
	  <groupId>com.corporate-startup</groupId>
	  <artifactId>download-base</artifactId>
	  <version>2.0.0</version>
	</dependency>	

API for communication with the server
	
	<dependency>
	  <groupId>com.corporate-startup</groupId>
	  <artifactId>download-server-api</artifactId>
	  <version>2.0.0</version>
	</dependency>	

REST server for asynchronous downloads

	<dependency>
	  <groupId>com.corporate-startup</groupId>
	  <artifactId>download-server</artifactId>
	  <version>2.0.0</version>
	</dependency>	
	
Demo client
	
	<dependency>
	  <groupId>com.corporate-startup</groupId>
	  <artifactId>download-client</artifactId>
	  <version>2.0.0</version>
	</dependency>

Plugin API to add additional functionality. For example selenium-based actions such as cookie retrieval or link extraction.

	<dependency>
	  <groupId>com.corporate-startup</groupId>
	  <artifactId>download-plugin-api</artifactId>
	  <version>2.0.0</version>
	</dependency>	

Core plugin functionality such as plugin discovery during runtime

	<dependency>
	  <groupId>com.corporate-startup</groupId>
	  <artifactId>download-plugin-core</artifactId>
	  <version>2.0.0</version>
	</dependency>

### Clone the repository:
	
	git clone https://github.com/msonst/downloader.git
   
### Build the project:

	cd downloader
	./gradlew build

# Contributing
Contributions are welcome! Please follow the [Contribution Guidelines](https://github.com/msonst/downloade-server/blob/main/CONTRIBUTING.md) .

# Acknowledgements

*   Special thanks to contributors and the open-source community.
*	If you find this project helpful, consider giving it a star!
