package com.abikananda.ipo.source;
import com.abikananda.ipo.domain.IpoSource; import org.springframework.beans.factory.annotation.Value; import org.springframework.context.annotation.*;
@Configuration public class OfficialFeedConfiguration {
 @Bean IpoSourceAdapter sebiAdapter(@Value("${ipo.sources.sebi-url}") String u){return new OfficialPublicIssuesAdapter("SEBI",u,IpoSource.SourceType.SEBI);}
 @Bean IpoSourceAdapter nseAdapter(@Value("${ipo.sources.nse-url}") String u){return new OfficialPublicIssuesAdapter("NSE",u,IpoSource.SourceType.NSE);}
 @Bean IpoSourceAdapter bseAdapter(@Value("${ipo.sources.bse-url}") String u){return new OfficialPublicIssuesAdapter("BSE",u,IpoSource.SourceType.BSE);}
}
