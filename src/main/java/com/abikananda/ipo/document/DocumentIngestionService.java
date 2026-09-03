package com.abikananda.ipo.document;
import com.abikananda.ipo.domain.*; import com.abikananda.ipo.repository.*; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service; import org.springframework.web.reactive.function.client.WebClient; import java.net.URI; import java.security.MessageDigest; import java.time.Instant; import java.util.*;
@Service public class DocumentIngestionService {
 private final WebClient web; private final PdfDocumentService pdf; private final IpoDocumentRepository docs; private final Set<String> allowedHosts;
 public DocumentIngestionService(WebClient.Builder b,PdfDocumentService p,IpoDocumentRepository d,@Value("${ipo.documents.allowed-hosts:sebi.gov.in,www.sebi.gov.in,nseindia.com,www.nseindia.com,bseindia.com,www.bseindia.com}") String hosts){web=b.build();pdf=p;docs=d;allowedHosts=new HashSet<>(Arrays.asList(hosts.split(",")));}
 public IpoDocument ingest(Ipo ipo,String url,IpoDocument.DocumentType type) throws Exception {URI uri=URI.create(url);if(!"https".equalsIgnoreCase(uri.getScheme())||!allowedHosts.contains(uri.getHost()))throw new IllegalArgumentException("Document host is not allowlisted");byte[] bytes=web.get().uri(uri).retrieve().bodyToMono(byte[].class).block();var result=pdf.extract(bytes);return docs.save(IpoDocument.builder().ipo(ipo).documentType(type).sourceUrl(url).sha256(hex(MessageDigest.getInstance("SHA-256").digest(bytes))).pageCount(result.pageCount()).extractedText(result.text()).retrievedAt(Instant.now()).build());}
 private String hex(byte[] b){return java.util.HexFormat.of().formatHex(b);}
}

