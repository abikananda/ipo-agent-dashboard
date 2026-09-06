package com.abikananda.ipo.extraction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.URI;
import java.time.Duration;
import java.util.*;

@Component
public class SafeSourceContentFetcher {
 private final WebClient web; private final Set<String> hosts; private final int maxTextChars; private final Duration timeout;
 public SafeSourceContentFetcher(WebClient.Builder builder,
  @Value("${ipo.extraction.allowed-hosts:www.bseindia.com,bseindia.com,www.chittorgarh.com,chittorgarh.com,www.investorgain.com,investorgain.com,ipowatch.in,www.ipowatch.in}") String allowedHosts,
  @Value("${ipo.extraction.max-response-bytes:2097152}") int maxBytes,
  @Value("${ipo.extraction.max-text-chars:30000}") int maxTextChars,
  @Value("${ipo.extraction.fetch-timeout:45s}") Duration timeout){this.web=builder.clone().codecs(c->c.defaultCodecs().maxInMemorySize(maxBytes)).build();this.hosts=new HashSet<>(Arrays.asList(allowedHosts.split(",")));this.maxTextChars=maxTextChars;this.timeout=timeout;}

 public FetchedContent fetch(ExtractionTemplate.SourceDefinition source){String url=source.effectiveUrl();Document listing=fetchDocument(url);StringBuilder content=new StringBuilder(render(listing,url));int detailCount=0;var traversal=source.detailTraversal();if(traversal!=null&&traversal.isEnabled()){int max=Math.max(0,Optional.ofNullable(traversal.maxPages()).orElse(5));Set<String> links=new LinkedHashSet<>();for(Element link:listing.select(Optional.ofNullable(traversal.linkSelector()).orElse("a[href]"))){String absolute=link.absUrl("href");if(!absolute.isBlank()&&isAllowed(absolute))links.add(absolute);}for(String detail:links){if(detailCount>=max)break;try{content.append("\n\nDETAIL PAGE ").append(detail).append("\n").append(render(fetchDocument(detail),detail));detailCount++;}catch(Exception e){content.append("\n\nDETAIL FETCH ERROR ").append(detail).append(": ").append(root(e));}}}String text=content.substring(0,Math.min(content.length(),maxTextChars));return new FetchedContent(url,text,content.length(),detailCount);}

 private Document fetchDocument(String url){validate(url);String html=web.get().uri(url).headers(h->{h.set("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/131.0 Safari/537.36");h.set("Accept","text/html,application/xhtml+xml");h.set("Accept-Language","en-US,en;q=0.9");}).retrieve().bodyToMono(String.class).timeout(timeout).block();if(html==null||html.isBlank())throw new IllegalStateException("Source returned an empty response");return Jsoup.parse(html,url);}
 private String render(Document doc,String url){doc.select("script,style,noscript,svg").remove();StringBuilder out=new StringBuilder("PAGE ").append(url).append('\n').append(doc.text());for(Element a:doc.select("a[href]")){String label=a.text().replaceAll("\\s+"," ").trim();String href=a.absUrl("href");if(!label.isBlank()&&!href.isBlank())out.append("\nLINK: ").append(label).append(" -> ").append(href);}return out.toString();}
 private void validate(String value){URI uri=URI.create(value);if(!"https".equalsIgnoreCase(uri.getScheme())||uri.getHost()==null||!hosts.contains(uri.getHost().toLowerCase(Locale.ROOT)))throw new IllegalArgumentException("Source URL host is not allowlisted: "+value);}
 private boolean isAllowed(String value){try{validate(value);return true;}catch(Exception ignored){return false;}}
 private String root(Throwable e){Throwable x=e;while(x.getCause()!=null)x=x.getCause();return x.getMessage()==null?x.getClass().getSimpleName():x.getMessage();}
 public record FetchedContent(String url,String text,int originalCharacters,int detailPagesFetched){}
}
