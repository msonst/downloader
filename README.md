[![Test on Checkin](https://github.com/msonst/downloader/actions/workflows/test.yml/badge.svg)](https://github.com/msonst/downloader/actions/workflows/test.yml) 
[![Maven Central & GitHub](https://github.com/msonst/downloader/actions/workflows/publish.yml/badge.svg)](https://github.com/msonst/downloader/actions/workflows/publish.yml)
# File downloader library for Java. (Resuming, Multipart, Multithreading)

## Introduction

This Java project provides a resumable file download mechanism with support for parallel processing using multiple threads. It allows for efficient and flexible downloading of large files while providing resumable capabilities in case of interruptions.

## Features

- Parallel downloading with configurable thread count.
- Support for resuming downloads.
- Track and display download progress.
- Customizable download status listeners.
- JMX-based statistics tracking.

## Usage

### Prerequisites

- Java Development Kit (JDK) 8 or later.

### Maven Dependency

	<dependency>
	  <groupId>com.corporate-startup</groupId>
	  <artifactId>downloader</artifactId>
	  <version>1.0.0</version>
	</dependency>

### Clone the repository:
	
	git clone https://github.com/msonst/ResumableDownload.git
   
### Build the project:

	cd ResumableDownload
	./gradlew build
	
### Example

	ChromeOptions options = new ChromeOptions();
	options.addArguments("--proxy-server=" + proxy);
	options.addArguments("--remote-allow-origins=*");
	options.addArguments("--window-size=1920,1080");
	options.addArguments("--disable-extensions");
	options.addArguments("--disable-gpu");
	options.addArguments("--disable-notifications");
	options.addArguments("--disable-infobars");
	options.addArguments("--disable-setuid-sandbox");
	options.addArguments("--disable-dev-shm-usage");
	options.addArguments("--ignore-certificate-errors");
	options.addArguments("--test-type");
	options.addArguments("--no-sandbox");
	options.addArguments("--dns-prefetch-disable");
	options.addArguments("--disable-logging");
	options.addArguments("--headless");
	
	WebDriver driver = new RemoteWebDriver(new URL(url), options);
	driver.manage().timeouts().implicitlyWait(Duration.ofMillis(60000));
	driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(60000));

	//use driver to log into a page and get a cookie

	String cookie = DownloadUtils.extractCookie(driver);
	driver.quit();

	// Download
	ResumableDownload downloader = new ResumableDownload(4, proxy, yourDownloadStatusListener);
	URL url = new URL("https://example.com/largefile.zip");
	String saveFileName = "downloadedFile.zip";
	
	DownloadStatusCode status = downloader.downloadSync(url, saveFileName, cookie);
	if (status.isOK()) {
	    System.out.println("Download successful!");
	} else {
	    System.out.println("Download failed with status: " + status);
	}

### Monitoring
A JMX Object is created for each download

# Contributing
Contributions are welcome! Please follow the [Contribution Guidelines](https://github.com/msonst/downloader/blob/main/CONTRIBUTING.md) .

# Acknowledgements

*   Special thanks to contributors and the open-source community.
*	If you find this project helpful, consider giving it a star!
