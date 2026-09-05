package com.abikananda.ipo.source;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty; import org.springframework.boot.context.event.ApplicationReadyEvent; import org.springframework.context.event.EventListener; import org.springframework.scheduling.annotation.Async; import org.springframework.stereotype.Component;
@Component @ConditionalOnProperty(name="ipo.sources.fetch-on-startup",havingValue="true",matchIfMissing=true)
public class LiveDiscoveryStartup {
 private final SourceIngestionService ingestion;
 public LiveDiscoveryStartup(SourceIngestionService ingestion){this.ingestion=ingestion;}
 @Async("applicationTaskExecutor") @EventListener(ApplicationReadyEvent.class) public void discover(){ingestion.discover();}
}
