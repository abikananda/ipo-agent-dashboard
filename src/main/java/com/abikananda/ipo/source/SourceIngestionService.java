package com.abikananda.ipo.source;
import com.abikananda.ipo.domain.*; import com.abikananda.ipo.repository.*; import org.springframework.scheduling.annotation.Scheduled; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.time.Instant; import java.util.*;
@Service public class SourceIngestionService {
 private final List<IpoSourceAdapter> adapters; private final IpoRepository ipos; private final IpoSourceRepository sources;
 public SourceIngestionService(List<IpoSourceAdapter> a,IpoRepository i,IpoSourceRepository s){adapters=a;ipos=i;sources=s;}
 @Scheduled(cron="${ipo.schedules.discovery:0 0 */6 * * *}") @Transactional public void scheduledDiscovery(){discover();}
 public DiscoveryResult discover(){int imported=0;List<String> unavailable=new ArrayList<>(),errors=new ArrayList<>();for(var a:adapters){if(!a.configured()){unavailable.add(a.name());continue;}try{for(var row:a.discover()){String slug=slug(row.companyName());Ipo ipo=ipos.findBySlug(slug).orElseGet(()->Ipo.builder().slug(slug).companyName(row.companyName()).type(row.type()).status(row.status()).updatedAt(Instant.now()).build());ipo.setStatus(row.status());ipo.setUpdatedAt(Instant.now());ipo=ipos.save(ipo);sources.save(IpoSource.builder().ipo(ipo).sourceName(a.name()).sourceUrl(row.sourceUrl()).sourceType(IpoSource.SourceType.AGGREGATOR).reliability(IpoSource.Reliability.HIGH).retrievedAt(row.retrievedAt()).build());imported++;}}catch(Exception e){errors.add(a.name()+": "+e.getMessage());}}return new DiscoveryResult(imported,unavailable,errors,Instant.now());}
 private String slug(String s){return s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+","-").replaceAll("(^-|-$)","");}
 public record DiscoveryResult(int imported,List<String> unavailable,List<String> errors,Instant completedAt){}
}

