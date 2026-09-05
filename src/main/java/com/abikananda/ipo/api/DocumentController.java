package com.abikananda.ipo.api;
import com.abikananda.ipo.document.DocumentIngestionService; import com.abikananda.ipo.domain.IpoDocument; import com.abikananda.ipo.repository.IpoRepository; import jakarta.validation.Valid; import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.NotNull; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/ipos") public class DocumentController {
 private final IpoRepository ipos; private final DocumentIngestionService ingestion;
 public DocumentController(IpoRepository ipos,DocumentIngestionService ingestion){this.ipos=ipos;this.ingestion=ingestion;}
 @PostMapping("/{slug}/documents") @ResponseStatus(HttpStatus.CREATED) IpoDocument ingest(@PathVariable String slug,@Valid @RequestBody DocumentRequest request) throws Exception {var ipo=ipos.findBySlug(slug).orElseThrow(()->new IllegalArgumentException("Unknown IPO: "+slug));return ingestion.ingest(ipo,request.url(),request.type());}
 public record DocumentRequest(@NotBlank String url,@NotNull IpoDocument.DocumentType type){}
}
