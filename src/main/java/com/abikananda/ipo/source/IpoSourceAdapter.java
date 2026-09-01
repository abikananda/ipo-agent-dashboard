package com.abikananda.ipo.source;
import com.abikananda.ipo.domain.Ipo; import java.time.Instant; import java.util.List;
public interface IpoSourceAdapter {
 String name(); boolean configured(); List<DiscoveredIpo> discover();
 record DiscoveredIpo(String externalId,String companyName,Ipo.IpoType type,Ipo.IpoStatus status,String sourceUrl,Instant retrievedAt){}
}

