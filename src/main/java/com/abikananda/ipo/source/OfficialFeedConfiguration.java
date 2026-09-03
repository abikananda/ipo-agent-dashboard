package com.abikananda.ipo.source;
import org.springframework.beans.factory.annotation.Value; import org.springframework.context.annotation.*; import org.springframework.web.reactive.function.client.WebClient;
@Configuration public class OfficialFeedConfiguration {
 @Bean IpoSourceAdapter sebiAdapter(WebClient.Builder b,@Value("${ipo.sources.sebi-url:}") String u){return new ConfiguredJsonSourceAdapter(b,"SEBI",u);}
 @Bean IpoSourceAdapter nseAdapter(WebClient.Builder b,@Value("${ipo.sources.nse-url:}") String u){return new ConfiguredJsonSourceAdapter(b,"NSE",u);}
 @Bean IpoSourceAdapter bseAdapter(WebClient.Builder b,@Value("${ipo.sources.bse-url:}") String u){return new ConfiguredJsonSourceAdapter(b,"BSE",u);}
}
