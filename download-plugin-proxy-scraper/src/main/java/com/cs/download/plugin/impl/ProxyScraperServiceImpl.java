package com.cs.download.plugin.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.download.api.plugin.service.host.LifecycleResult;
import com.cs.download.api.plugin.service.host.LifecycleState;
import com.cs.download.api.plugin.service.proxy.ProxyProviderService;
import com.cs.download.api.plugin.service.proxy.ProxyRequest;
import com.cs.download.api.plugin.service.proxy.ProxyResult;

public class ProxyScraperServiceImpl implements ProxyProviderService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProxyScraperServiceImpl.class);

  private static final String FREEPROXYLISTS = "https://free-proxy-list.net/anonymous-proxy.html";
  private static final String IPV4_PATTERN = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";
  private static final Pattern VERIFY_PATTERN = Pattern.compile(IPV4_PATTERN);

  private List<Proxy> mProxies;

  private ScheduledFuture<?> mFuture;

  private LifecycleState mState;
  @Override
  public LifecycleResult install() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public LifecycleResult start() {
    ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
    
    mFuture = pool.scheduleAtFixedRate(() -> scrape(), 0, 10, TimeUnit.MINUTES);
    
    mState = LifecycleState.RUNNING;
    return new LifecycleResult(mState);
  }

  @Override
  public LifecycleResult stop() {
    mFuture.cancel(true);
    mState = LifecycleState.STOPPED;
    return new LifecycleResult(mState);
  }

  @Override
  public LifecycleResult status() {
    return new LifecycleResult(mState);
  }

  public void scrape() {

    ArrayList proxies = new ArrayList<>();

    // @formatter:off 
//    try{proxies.addAll(updateByTable("https://premproxy.com/list/08.htm", "//table[contains(@id,'proxylistt')]", 0, 0));                            }catch(Exception e) {System.out.println(e.getMessage());}
    try{proxies.addAll(updateSpys());                                                                                                               }catch(Exception e) {System.out.println(e.getMessage());}
//    try{proxies.addAll(updateClarketm());                                                                                                           }catch(Exception e) {System.out.println(e.getMessage());}
//    try{proxies.addAll(updateByTable(FREEPROXYLISTS, "//table[contains(@class,'DataGrid')]", 0, 1));                                                }catch(Exception e) {System.out.println(e.getMessage());}
//    try{proxies.addAll(updateByTable("https://www.netzwelt.de/proxy/index.html", "//table", 0, 1));                                                 }catch(Exception e) {System.out.println(e.getMessage());}
////    try{proxies.addAll(updateByTable("http://free-proxy.cz/en/proxylist/country/all/all/ping/all", "//table[contains(@id,'proxy_list')]", 0, 1));   }catch(Exception e) {System.out.println(e.getMessage());}
////    try{proxies.addAll(updateByTable("http://free-proxy.cz/en/proxylist/country/all/all/ping/all/2", "//table[contains(@id,'proxy_list')]", 0, 1)); }catch(Exception e) {System.out.println(e.getMessage());}
////    try{proxies.addAll(updateByTable("http://free-proxy.cz/en/proxylist/country/all/all/ping/all/3", "//table[contains(@id,'proxy_list')]", 0, 1)); }catch(Exception e) {System.out.println(e.getMessage());}
////    try{proxies.addAll(updateByTable("http://free-proxy.cz/en/proxylist/country/all/all/ping/all/4", "//table[contains(@id,'proxy_list')]", 0, 1)); }catch(Exception e) {System.out.println(e.getMessage());}
////    try{proxies.addAll(updateByTable("http://free-proxy.cz/en/proxylist/country/all/all/ping/all/5", "//table[contains(@id,'proxy_list')]", 0, 1)); }catch(Exception e) {System.out.println(e.getMessage());}
//    try{proxies.addAll(updateByTable("https://raidrush.net/pages/proxy-liste/", "//table[contains(@id,'proxy_table')]", 1, 2));                     }catch(Exception e) {System.out.println(e.getMessage());}
//    try{proxies.addAll(updateByTable("https://proxyservers.pro/", "//table[contains(@class,'table')]", 1, 2));                                      }catch(Exception e) {System.out.println(e.getMessage());}
//    try{proxies.addAll(updateByTable("https://premproxy.com/list/", "//table[contains(@id,'proxylistt')]", 0, 0));                                  }catch(Exception e) {System.out.println(e.getMessage());}
//    try{proxies.addAll(updateByTable("https://premproxy.com/list/01.htm", "//table[contains(@id,'proxylistt')]", 0, 0));                            }catch(Exception e) {System.out.println(e.getMessage());}
//    try{proxies.addAll(updateByTable("https://premproxy.com/list/02.htm", "//table[contains(@id,'proxylistt')]", 0, 0));                            }catch(Exception e) {System.out.println(e.getMessage());}
//    try{proxies.addAll(updateByTable("https://premproxy.com/list/03.htm", "//table[contains(@id,'proxylistt')]", 0, 0));                            }catch(Exception e) {System.out.println(e.getMessage());}
//    try{proxies.addAll(updateByTable("https://premproxy.com/list/04.htm", "//table[contains(@id,'proxylistt')]", 0, 0));                            }catch(Exception e) {System.out.println(e.getMessage());}
//    try{proxies.addAll(updateByTable("https://premproxy.com/list/05.htm", "//table[contains(@id,'proxylistt')]", 0, 0));                            }catch(Exception e) {System.out.println(e.getMessage());}
//    try{proxies.addAll(updateByTable("https://premproxy.com/list/06.htm", "//table[contains(@id,'proxylistt')]", 0, 0));                            }catch(Exception e) {System.out.println(e.getMessage());}
//    try{proxies.addAll(updateByTable("https://premproxy.com/list/07.htm", "//table[contains(@id,'proxylistt')]", 0, 0));                            }catch(Exception e) {System.out.println(e.getMessage());}
    // proxies.addAll(updatePubproxy(proxies));
    // @formatter:on

    mProxies=proxies;
  }

  public String table2csv(String table) {
    String csv = "";

    if (null == table || table.isEmpty())
      return "";

    Document doc = Jsoup.parseBodyFragment(table);
    Elements rows = doc.getElementsByTag("tr");

    for (Element row : rows) {
      Elements cells = row.getElementsByTag("td");
      for (Element cell : cells) {
        csv += (cell.text().trim().concat(";"));
      }
      csv += "\n";
    }
    return csv;
  }

  public static boolean isValidIP(final String ip) {
    Matcher matcher = VERIFY_PATTERN.matcher(ip);
    return matcher.matches();
  }

  @SuppressWarnings("deprecation")
  public LinkedBlockingDeque<String> updateByTable(String url, String query, int ipCol, int portCol) throws MalformedURLException, IOException {
    LinkedBlockingDeque<String> retVal = new LinkedBlockingDeque<String>();
    LinkedBlockingDeque<String> proxies = new LinkedBlockingDeque<String>();

    Elements tbl = Jsoup.connect(url).get().select(query);
    String table = table2csv(tbl.html());

    String[] line = table.split("\n");
    for (int i = 1; i < line.length; i++) {
      String[] elem = line[i].split(";");
      if ((null != elem) && (elem.length >= Math.max(ipCol, portCol))) {
        String ip = elem[ipCol];

        if (ipCol == portCol) {
          ip = ip.split(":")[0];
        }

        if (isValidIP(ip)) {

          if (ipCol != portCol) {
            ip += ":" + elem[portCol];
          } else
            ip = elem[ipCol];

          proxies.add(ip);
        }
      }
    }

    retVal.addAll(proxies);

    return retVal;
  }

  public List<Proxy> updateSpys() throws MalformedURLException, IOException {

    List<Proxy> ret = new ArrayList<Proxy>();

    Document document = Jsoup.connect("https://spys.one/en/anonymous-proxy-list/").get();
    String text = document.select("body > script:nth-child(2)").html();
    List<String> csv = Arrays.stream(text.split(";")).collect(Collectors.toList());
    Map<String, String> lut = csv.stream().map(e -> e.split("=")).collect(Collectors.toMap(str -> str[0], str -> str[1]));

    Map<String, String> tmp = lut.entrySet().stream().filter(e -> e.getValue().contains("^"))
        .map(e -> new AbstractMap.SimpleEntry<String, String>(e.getKey(), spysDecode(lut, e.getValue())))
        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    lut.putAll(tmp);

    Elements ipE = document.getElementsByClass("spy14");
    ipE.forEach(e -> {
      String t = e.text(), t1 = e.html();
      Matcher m = VERIFY_PATTERN.matcher(t);
      if (m.find()) {
        String ip = m.group(0);

        if (!t1.contains("+"))
          return;

        t1 = t1.substring(t1.indexOf("+") + 1);
        t1 = t1.replace("document.write(\"<font class=spy2>:<\\/font>\"+", "").replace("(", "").replace(")", "").replace("</script>", "");
        String[] split = t1.split("\\+");
        String strport = "";
        for (int i = 0; i < split.length; i++) {
          strport += spysDecode(lut, split[i]);
        }

        String type = e.getElementsByClass("spy1").text();
        System.out.println(type);

        ret.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, Integer.parseInt(strport))));
      }
    });

    return ret;
  }

  private String spysDecode(Map<String, String> collect, String value) {
    String[] split = value.split("\\^");
    int base = 0;
    int exp = 0;

    try {
      base = Integer.valueOf(split[0]);
    } catch (Exception ex) {
      base = Integer.valueOf(collect.get(split[0]));
    }
    try {
      exp = Integer.valueOf(split[1]);
    } catch (Exception ex) {
      exp = Integer.valueOf(collect.get(split[1]));
    }
    return Integer.toString(base ^ exp);
  }

  @Override
  public ProxyResult next(ProxyRequest request) {
    return new ProxyResult(System.currentTimeMillis(), mProxies);
  }
}
