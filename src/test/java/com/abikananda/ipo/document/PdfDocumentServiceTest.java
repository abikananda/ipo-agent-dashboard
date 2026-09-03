package com.abikananda.ipo.document; import org.junit.jupiter.api.Test; import static org.assertj.core.api.Assertions.assertThatThrownBy;
class PdfDocumentServiceTest {@Test void rejectsNonPdf(){assertThatThrownBy(()->new PdfDocumentService().extract("not-a-pdf".getBytes())).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("not a PDF");}}
