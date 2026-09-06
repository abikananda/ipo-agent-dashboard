package com.abikananda.ipo.extraction;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExtractionPreviewService {
 private final ExtractionTemplateLoader templates; private final SafeSourceContentFetcher fetcher; private final TemplateDrivenOllamaExtractor extractor;
 public ExtractionPreviewService(ExtractionTemplateLoader templates,SafeSourceContentFetcher fetcher,TemplateDrivenOllamaExtractor extractor){this.templates=templates;this.fetcher=fetcher;this.extractor=extractor;}
 public PreviewResult preview(String templateId){ExtractionTemplate template=templates.load(templateId);Instant started=Instant.now();List<SourcePreview> results=new ArrayList<>();for(var source:template.sources()){try{var fetched=fetcher.fetch(source);try{JsonNode data=extractor.extract(template,source,fetched.text());results.add(new SourcePreview(source.sourceId(),source.sourceName(),fetched.url(),"EXTRACTED",fetched.originalCharacters(),fetched.detailPagesFetched(),previewText(fetched.text()),data,null));}catch(Exception e){results.add(new SourcePreview(source.sourceId(),source.sourceName(),fetched.url(),"FETCHED_EXTRACTION_FAILED",fetched.originalCharacters(),fetched.detailPagesFetched(),previewText(fetched.text()),null,root(e)));}}catch(Exception e){results.add(new SourcePreview(source.sourceId(),source.sourceName(),source.effectiveUrl(),"FETCH_FAILED",0,0,null,null,root(e)));}}return new PreviewResult(template.templateId(),template.datasetType(),template.version(),started,Instant.now(),results);}
 private String previewText(String text){return text.substring(0,Math.min(text.length(),1500));}
 private String root(Throwable e){Throwable x=e;while(x.getCause()!=null)x=x.getCause();return x.getMessage()==null?x.getClass().getSimpleName():x.getMessage();}
 public record PreviewResult(String templateId,String datasetType,Integer templateVersion,Instant startedAt,Instant completedAt,List<SourcePreview> sources){}
 public record SourcePreview(String sourceId,String sourceName,String url,String status,int fetchedCharacters,int detailPagesFetched,String contentPreview,JsonNode extractedData,String error){}
}
