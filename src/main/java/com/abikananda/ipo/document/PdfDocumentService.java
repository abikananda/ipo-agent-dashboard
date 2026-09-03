package com.abikananda.ipo.document;
import org.apache.pdfbox.Loader; import org.apache.pdfbox.pdmodel.PDDocument; import org.apache.pdfbox.text.PDFTextStripper; import org.springframework.stereotype.Service; import java.io.IOException;
@Service public class PdfDocumentService {
 public ExtractedPdf extract(byte[] bytes) throws IOException {
  if(bytes==null||bytes.length<5||bytes.length>50*1024*1024) throw new IllegalArgumentException("PDF must be between 5 bytes and 50 MB");
  if(bytes[0]!='%'||bytes[1]!='P'||bytes[2]!='D'||bytes[3]!='F') throw new IllegalArgumentException("File is not a PDF");
  try(PDDocument doc=Loader.loadPDF(bytes)){PDFTextStripper s=new PDFTextStripper();return new ExtractedPdf(doc.getNumberOfPages(),s.getText(doc));}
 }
 public record ExtractedPdf(int pageCount,String text){}
}

