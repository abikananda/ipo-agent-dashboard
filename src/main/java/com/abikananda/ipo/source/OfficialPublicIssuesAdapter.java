package com.abikananda.ipo.source;

import com.abikananda.ipo.domain.Ipo;
import com.abikananda.ipo.domain.IpoSource;
import io.github.resilience4j.retry.annotation.Retry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Terms-compliant, best-effort parser for the three official public issue pages. */
public class OfficialPublicIssuesAdapter implements IpoSourceAdapter {
 private final String name;
 private final String endpoint;
 private final IpoSource.SourceType sourceType;

 public OfficialPublicIssuesAdapter(String name,String endpoint,IpoSource.SourceType sourceType){this.name=name;this.endpoint=endpoint;this.sourceType=sourceType;}
 public String name(){return name;}
 public IpoSource.SourceType sourceType(){return sourceType;}
 public boolean configured(){return endpoint!=null&&!endpoint.isBlank();}

 @Retry(name="ipoSource")
 public List<DiscoveredIpo> discover(){
  if(!configured())return List.of();
  try{
   Document doc=Jsoup.connect(endpoint)
     .userAgent("IPO-Analysis-Agent/1.0 (+research; contact repository owner)")
     .header("Accept","text/html,application/xhtml+xml")
     .timeout((int)Duration.ofSeconds(20).toMillis()).get();
   Map<String,DiscoveredIpo> rows=new LinkedHashMap<>();
   if(sourceType==IpoSource.SourceType.SEBI)parseSebi(doc,rows);else parseExchange(doc,rows);
   if(rows.isEmpty())throw new IllegalStateException("official page returned no parseable IPO rows; its markup or access policy may have changed");
   return List.copyOf(rows.values());
  }catch(Exception e){throw new IllegalStateException("cannot collect "+endpoint+": "+e.getMessage(),e);}
 }

 private void parseSebi(Document doc,Map<String,DiscoveredIpo> out){
  for(Element link:doc.select("a[href]")){
   String href=link.absUrl("href");String text=clean(link.text());
   if(!href.contains("sebi.gov.in")||!href.contains("/filings/public-issues/")||!looksLikeCompany(text))continue;
   add(out,text,href,status(text));
  }
 }

 private void parseExchange(Document doc,Map<String,DiscoveredIpo> out){
  for(Element tr:doc.select("table tr")){
   String text=clean(tr.text());if(text.isBlank())continue;
   Element company=tr.selectFirst("a[href]");
   String name=company==null?firstCell(tr):clean(company.text());
   if(!looksLikeCompany(name))continue;
   String url=company==null?endpoint:company.absUrl("href");
   add(out,name,url.isBlank()?endpoint:url,status(text));
  }
 }

 private String firstCell(Element row){Element cell=row.selectFirst("td");return cell==null?"":clean(cell.text());}
 private void add(Map<String,DiscoveredIpo> out,String company,String url,Ipo.IpoStatus status){
  String key=company.toLowerCase(Locale.ROOT);Ipo.IpoType type=key.contains(" sme")||key.contains("small and medium")?Ipo.IpoType.SME:Ipo.IpoType.MAINBOARD;
  out.putIfAbsent(key,new DiscoveredIpo(Integer.toHexString((name+url).hashCode()),company,type,status,url,Instant.now()));
 }
 private Ipo.IpoStatus status(String text){String x=text.toLowerCase(Locale.ROOT);if(x.contains("listed"))return Ipo.IpoStatus.LISTED;if(x.contains("closed")||x.contains("close"))return Ipo.IpoStatus.CLOSED;if(x.contains("open")||x.contains("live"))return Ipo.IpoStatus.OPEN;return Ipo.IpoStatus.UPCOMING;}
 private boolean looksLikeCompany(String text){String x=clean(text);if(x.length()<5||x.length()>220)return false;String l=x.toLowerCase(Locale.ROOT);return (l.contains("limited")||l.matches(".*\\bltd\\.?\\b.*"))&&!l.contains("guideline")&&!l.contains("regulation");}
 private String clean(String value){return value==null?"":value.replaceAll("\\s+"," ").trim();}
}
