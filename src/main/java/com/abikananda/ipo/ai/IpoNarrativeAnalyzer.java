package com.abikananda.ipo.ai;
import java.util.List;
public interface IpoNarrativeAnalyzer { NarrativeResult analyze(String citedDocumentText); boolean available(); record NarrativeResult(String summary,List<String> risks,List<Integer> pages,String provider,String model){} }

