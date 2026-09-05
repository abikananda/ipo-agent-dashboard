package com.abikananda.ipo.source;
import com.abikananda.ipo.domain.*; import com.abikananda.ipo.repository.*; import org.springframework.scheduling.annotation.Scheduled; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.time.Instant; import java.util.*;
@Service public class SourceIngestionService {
 private final List<IpoSourceAdapter> adapters; private final IpoRepository ipos; private final IpoSourceRepository sources; private volatile DiscoveryResult lastResult=new DiscoveryResult(0,List.of("not run"),List.of(),null);
 public SourceIngestionService(List<IpoSourceAdapter> a,IpoRepository i,IpoSourceRepository s){adapters=a;ipos=i;sources=s;}
 @Scheduled(cron="${ipo.schedules.discovery:0 0 */6 * * *}") @Transactional public void scheduledDiscovery(){discover();}
 public synchronized DiscoveryResult discover(){int imported=0;List<String> unavailable=new ArrayList<>(),errors=new ArrayList<>();for(var a:adapters){if(!a.configured()){unavailable.add(a.name());continue;}try{for(var row:a.discover()){String slug=slug(row.companyName());Ipo ipo=ipos.findBySlug(slug).orElseGet(()->Ipo.builder().slug(slug).companyName(row.companyName()).type(row.type()).status(row.status()).updatedAt(Instant.now()).build());ipo.setCompanyName(row.companyName());ipo.setType(row.type());ipo.setStatus(row.status());ipo.setUpdatedAt(Instant.now());ipo=ipos.save(ipo);sources.save(IpoSource.builder().ipo(ipo).sourceName(a.name()).sourceUrl(row.sourceUrl()).sourceType(a.sourceType()).reliability(IpoSource.Reliability.AUTHORITATIVE).retrievedAt(row.retrievedAt()).build());imported++;}}catch(Exception e){errors.add(a.name()+": "+rootMessage(e));}}lastResult=new DiscoveryResult(imported,unavailable,errors,Instant.now());return lastResult;}
 public DiscoveryResult status(){return lastResult;}
 private String rootMessage(Throwable e){Throwable x=e;while(x.getCause()!=null)x=x.getCause();return x.getMessage()==null?x.getClass().getSimpleName():x.getMessage();}
 private String slug(String s){return s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+","-").replaceAll("(^-|-$)","");}
 public record DiscoveryResult(int imported,List<String> unavailable,List<String> errors,Instant completedAt){}
}
