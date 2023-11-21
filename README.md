[![Test on Checkin](https://github.com/msonst/downloader/actions/workflows/test.yml/badge.svg)](https://github.com/msonst/downloader/actions/workflows/test.yml) 
[![Maven Central & GitHub](https://github.com/msonst/downloader/actions/workflows/publish.yml/badge.svg)](https://github.com/msonst/downloader/actions/workflows/publish.yml)
# File downloader library for Java. (Resuming)

## Introduction

This Java project provides a resumable file download mechanism with support for parallel processing using multiple threads. It allows for efficient and flexible downloading of large files while providing resumable capabilities in case of interruptions.

## Features

- Parallel downloading with configurable thread count.
- Support for resuming downloads.
- Track and display download progress.
- Customizable download status listeners.
- JMX-based statistics tracking.

## Note on "multiple threads for the same download"
In Java, it usually does not make sense to download a single file using multiple threads. This is because the download is typically limited by network speed, and splitting it into multiple threads can lead to contention situations that do not necessarily improve efficiency.
It is often more effective to use a single thread for downloading and reserve other threads for tasks such as data processing or handling, to enhance the overall performance of the program.
However, since each thread establishes its own connection, in conjunction with rotating proxy chains, this can both enhance anonymity and improve data throughput due to the limited bandwidth of the chain.

## Usage

### Prerequisites

- Java Development Kit (JDK) 8 or later.

### Maven Dependency

	<dependency>
	  <groupId>com.corporate-startup</groupId>
	  <artifactId>downloader</artifactId>
	  <version>1.2.0</version>
	</dependency>

### Clone the repository:
	
	git clone https://github.com/msonst/Downloader.git
   
### Build the project:

	cd Downloader
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
	DownloadManager dlm = new DownloadManager(mProxy, SAVE_PATH, 1);
	Download download = dlm.addDownload(new URL("url"), cookie);
	uut.start(download);
	uut.waitForCompletion();

### Monitoring
A JMX Object is created for each download

# Contributing
Contributions are welcome! Please follow the [Contribution Guidelines](https://github.com/msonst/downloader/blob/main/CONTRIBUTING.md) .

# Acknowledgements

*   Special thanks to contributors and the open-source community.
*	If you find this project helpful, consider giving it a star!
