package com.abikananda.ipo.document;

import com.abikananda.ipo.domain.Ipo;
import com.abikananda.ipo.domain.IpoDocument;
import com.abikananda.ipo.repository.IpoDocumentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class DocumentIngestionService {
 private final WebClient web;
 private final PdfDocumentService pdf;
 private final IpoDocumentRepository docs;
 private final Set<String> allowedHosts;
 private final int maxDocumentBytes;

 public DocumentIngestionService(WebClient.Builder builder,PdfDocumentService pdf,IpoDocumentRepository docs,
   @Value("${ipo.documents.allowed-hosts:sebi.gov.in,www.sebi.gov.in,nseindia.com,www.nseindia.com,bseindia.com,www.bseindia.com}") String hosts,
   @Value("${ipo.documents.max-size-bytes:52428800}") int maxDocumentBytes){
  if(maxDocumentBytes<262144)throw new IllegalArgumentException("ipo.documents.max-size-bytes must be at least 262144");
  this.maxDocumentBytes=maxDocumentBytes;
  this.web=builder.clone().codecs(configurer->configurer.defaultCodecs().maxInMemorySize(maxDocumentBytes)).build();
  this.pdf=pdf;this.docs=docs;this.allowedHosts=new HashSet<>(Arrays.asList(hosts.split(",")));
 }

 public IpoDocument ingest(Ipo ipo,String url,IpoDocument.DocumentType type) throws Exception {
  URI uri=URI.create(url);
  if(!"https".equalsIgnoreCase(uri.getScheme())||!allowedHosts.contains(uri.getHost()))throw new IllegalArgumentException("Document host is not allowlisted");
  byte[] bytes=web.get().uri(uri).exchangeToMono(response->{
   if(!response.statusCode().is2xxSuccessful())return response.createError();
   long contentLength=response.headers().contentLength().orElse(-1);
   if(contentLength>maxDocumentBytes)return Mono.error(new IllegalArgumentException("PDF exceeds configured maximum of "+maxDocumentBytes+" bytes"));
   return response.bodyToMono(byte[].class);
  }).block();
  if(bytes==null)throw new IllegalArgumentException("Document response was empty");
  var result=pdf.extract(bytes);
  return docs.save(IpoDocument.builder().ipo(ipo).documentType(type).sourceUrl(url).sha256(hex(MessageDigest.getInstance("SHA-256").digest(bytes))).pageCount(result.pageCount()).extractedText(result.text()).retrievedAt(Instant.now()).build());
 }

 private String hex(byte[] bytes){return java.util.HexFormat.of().formatHex(bytes);}
}
