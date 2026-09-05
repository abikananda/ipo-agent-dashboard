package com.abikananda.ipo.source;
import com.abikananda.ipo.domain.Ipo; import com.abikananda.ipo.domain.IpoSource; import io.github.resilience4j.retry.annotation.Retry; import org.springframework.core.ParameterizedTypeReference; import org.springframework.web.reactive.function.client.WebClient; import java.time.Instant; import java.util.*;
public class ConfiguredJsonSourceAdapter implements IpoSourceAdapter {
 private final WebClient client; private final String endpoint; private final String name;
 public ConfiguredJsonSourceAdapter(WebClient.Builder builder,String name,String endpoint){this.client=builder.build();this.name=name;this.endpoint=endpoint;}
 public String name(){return name;} public boolean configured(){return endpoint!=null&&!endpoint.isBlank();}
 public IpoSource.SourceType sourceType(){return IpoSource.SourceType.valueOf(name);}
 @Retry(name="ipoSource") public List<DiscoveredIpo> discover(){
  if(!configured()) return List.of();
  List<Map<String,Object>> rows=client.get().uri(endpoint).retrieve().bodyToMono(new ParameterizedTypeReference<List<Map<String,Object>>>(){}).block();
  if(rows==null)return List.of(); return rows.stream().map(this::map).toList();
 }
 private DiscoveredIpo map(Map<String,Object> x){return new DiscoveredIpo(String.valueOf(x.get("id")),String.valueOf(x.get("companyName")),Ipo.IpoType.valueOf(String.valueOf(x.getOrDefault("type","MAINBOARD"))),Ipo.IpoStatus.valueOf(String.valueOf(x.getOrDefault("status","UPCOMING"))),endpoint,Instant.now());}
}
