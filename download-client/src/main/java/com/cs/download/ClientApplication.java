package com.cs.download;

import java.net.URI;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.download.server.api.DownloadCommand;
import com.cs.download.server.api.RequestResult;

import io.restassured.RestAssured;

public class ClientApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientApplication.class);
  
  private static HelpFormatter formatter;
  private static String mHeader;
  private Options mOptions;
  private static String mFooter;
  private boolean mB;
  private static Options options;

  static CommandLine buildCMD(String[] args) {
    options = new Options();
    CommandLineParser parser = new DefaultParser();
    CommandLine retVal = null;

    options.addOption(Option.builder("h").desc("Address of the host").required(true).hasArg().argName("host[:Port]").longOpt("host").build());
    options.addOption(Option.builder("a").desc("Add a new download to the queue. Returns downloadId").required(false).hasArg().argName("URI")
        .longOpt("add").build());
    options.addOption(
        Option.builder("c").desc("Request the current download status").required(false).optionalArg(true).argName("id").longOpt("status").build());
    options.addOption(Option.builder("s").desc("Start a download").required(false).optionalArg(true).hasArg().argName("id").longOpt("start").build());
    options.addOption(Option.builder("o").desc("Pass a cookie during add.").required(false).hasArg().argName("cookie").longOpt("cookie").build());
    options.addOption(Option.builder("e").desc("Get the endpoints.").required(false).longOpt("endpoints").build());

    options.addOption(Option.builder("l").desc("List the plugin services.").required(false).longOpt("plugin-services").build());
    options.addOption(Option.builder("f").desc("List the plugin files.").required(false).longOpt("plugin-files").build());
    options.addOption(Option.builder("d").desc("List the files in download folder.").required(false).longOpt("download-files").build());

    mHeader = "";
    String footer = "(c) 2023 Michael sonst";
    formatter = new HelpFormatter();

    try {
      retVal = parser.parse(options, args);
    } catch (org.apache.commons.cli.ParseException e) {
      System.out.print("Parse error: ");
      System.out.println(e.getMessage());
      printHelp();
      System.exit(1);
    }

    return retVal;
  }

  static void printHelp() {

    formatter.printHelp("download-client", mHeader, options, mFooter, true);

  }
  public static void main(String[] args)  {
    String opt, cookie = null;
    Long downloadid = null;
    URI addUri;
    RequestResult result = null;

    try {
      CommandLine cmd = ClientApplication.buildCMD(args);
      String strURI = null;

      for (int i = 0; i < cmd.getOptions().length; i++) {
        Option option = cmd.getOptions()[i];

        switch (option.getId()) {
          case 'a': {//ADD
            addUri = new URI(option.getValue().trim().replace("\"", ""));
            result = RestAssured.given().param("url", addUri.toASCIIString()).param("cookie", cookie).get(new URI(strURI + "/download/add")).as(RequestResult.class);
            break;
          }
          case 's': {//START
            downloadid = (option.hasArgs()) ? Integer.valueOf(option.getValue()) : downloadid;
            result = RestAssured.given().param("cmd", DownloadCommand.START).param("downloadId", downloadid)
                .get(new URI(strURI + "/download/control")).as(RequestResult.class);
            break;
          }
          case 'c': {//STATUS
            downloadid = (option.hasArgs()) ? Integer.valueOf(option.getValue()) : downloadid;
            result = RestAssured.given().param("downloadId", downloadid).get(new URI(strURI + "/download/status")).as(RequestResult.class);
            break;
          }
          case 'h'://HOST
            strURI = "http://" + cmd.getOptionValue('h');
            break;
          case 'o': //COOKIE
            cookie = option.getValue();
            break;
          case 'e': {//ENDPOINTS
            System.out.println("Result" + RestAssured.given().get(new URI(strURI + "/introspect/endpoints")).asPrettyString());
            break;
          }

          case 'l': {//SERVICES
            System.out.println("Result" + RestAssured.given().get(new URI(strURI + "/plugin/services")).asPrettyString());
            break;
          }
          case 'f': {//PLUGIN FILES
            System.out.println("Result" + RestAssured.given().get(new URI(strURI + "/plugin/files")).asPrettyString());
            break;
          }
          case 'd': {//DOWNLOAD FILES
            System.out.println("Result" + RestAssured.given().get(new URI(strURI + "/download/files")).asPrettyString());
            break;
          }
          default: {
            throw new IllegalArgumentException("Unexpected value: " + (char) option.getId());
          }
        }

        if (null != result) {

          if (result.getStatus().isOK()) {

            downloadid = result.getDownloadId();//STORE ID  
            LOGGER.debug("ID {}, result={}", downloadid, result.getStatus());
          }
          System.out.println("ID " + downloadid + " result=" + result.getStatus());
        }
      }
    } catch (Exception e) {
      System.out.println("ERROR occurred. e=" + e.getMessage());
      printHelp();
      System.exit(1);
    }

  }
//  public static void main(String[] args) {
//    SpringApplication.run(Application.class, args);
//  }

}
