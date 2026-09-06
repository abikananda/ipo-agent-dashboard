package com.abikananda.ipo.api;

import com.abikananda.ipo.extraction.ExtractionPreviewService;
import com.abikananda.ipo.extraction.ExtractionTemplate;
import com.abikananda.ipo.extraction.ExtractionTemplateLoader;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/extractions/templates")
public class ExtractionPreviewController {
 private final ExtractionTemplateLoader templates; private final ExtractionPreviewService previews;
 public ExtractionPreviewController(ExtractionTemplateLoader templates,ExtractionPreviewService previews){this.templates=templates;this.previews=previews;}
 @GetMapping public List<String> templates(){return templates.ids();}
 @GetMapping("/{templateId}") public ExtractionTemplate template(@PathVariable String templateId){return templates.load(templateId);}
 @PostMapping("/{templateId}/preview") public ExtractionPreviewService.PreviewResult preview(@PathVariable String templateId){return previews.preview(templateId);}
}
