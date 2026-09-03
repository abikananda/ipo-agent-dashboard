package com.abikananda.ipo.api;

import com.abikananda.ipo.analysis.*;
import com.abikananda.ipo.domain.*;
import com.abikananda.ipo.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController @RequestMapping("/api/v1/ipos")
public class IpoController {
  private final IpoRepository ipos; private final FinancialPeriodRepository financials;
  private final MarketSnapshotRepository markets; private final IpoScoringService scoring;
  private final IpoValuationRepository valuations; private final IpoRiskRepository risks; private final IpoSourceRepository sources; private final IpoDocumentRepository documents; private final AnalysisRecordRepository history; private final AnalysisOrchestrator orchestrator;
  public IpoController(IpoRepository i, FinancialPeriodRepository f, MarketSnapshotRepository m, IpoScoringService s,IpoValuationRepository v,IpoRiskRepository r,IpoSourceRepository so,IpoDocumentRepository d,AnalysisRecordRepository h,AnalysisOrchestrator o){ipos=i;financials=f;markets=m;scoring=s;valuations=v;risks=r;sources=so;documents=d;history=h;orchestrator=o;}
  @GetMapping public List<IpoSummary> all(){ return ipos.findAll().stream().map(this::summary).toList(); }
  @GetMapping("/{slug}") public IpoDetail one(@PathVariable String slug){
    Ipo ipo=ipos.findBySlug(slug).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"IPO not found"));
    var f=financials.findByIpoIdOrderByPeriodEndAsc(ipo.getId()); var m=markets.findTop30ByIpoIdOrderByObservedAtDesc(ipo.getId());
    return new IpoDetail(ipo,f,m,valuations.findByIpoId(ipo.getId()).orElse(null),risks.findByIpoIdOrderBySeverityAsc(ipo.getId()),sources.findByIpoIdOrderByRetrievedAtDesc(ipo.getId()),documents.findByIpoIdOrderByRetrievedAtDesc(ipo.getId()),scoring.score(ipo,f,m.stream().findFirst().orElse(null),valuations.findByIpoId(ipo.getId()).orElse(null),risks.findByIpoIdOrderBySeverityAsc(ipo.getId())));
  }
  @GetMapping("/{slug}/recommendation-history") public List<AnalysisRecord> history(@PathVariable String slug){return history.findTop20ByIpoIdOrderByAnalyzedAtDesc(required(slug).getId());}
  @PostMapping("/{slug}/analyze") @ResponseStatus(HttpStatus.ACCEPTED) public AnalysisJob analyze(@PathVariable String slug){return orchestrator.queue(required(slug).getId());}
  @GetMapping("/compare") public List<IpoSummary> compare(@RequestParam List<Long> ids){if(ids.size()>4)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Compare at most four IPOs");return ipos.findAllById(ids).stream().map(this::summary).toList();}
  private IpoSummary summary(Ipo ipo){
    var f=financials.findByIpoIdOrderByPeriodEndAsc(ipo.getId()); var m=markets.findTop30ByIpoIdOrderByObservedAtDesc(ipo.getId());
    return new IpoSummary(ipo,scoring.score(ipo,f,m.stream().findFirst().orElse(null),valuations.findByIpoId(ipo.getId()).orElse(null),risks.findByIpoIdOrderBySeverityAsc(ipo.getId())),m.stream().findFirst().orElse(null));
  }
  private Ipo required(String slug){return ipos.findBySlug(slug).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"IPO not found"));}
  public record IpoSummary(Ipo ipo, Recommendation analysis, MarketSnapshot latestMarket){}
  public record IpoDetail(Ipo ipo,List<FinancialPeriod> financials,List<MarketSnapshot> marketHistory,IpoValuation valuation,List<IpoRisk> risks,List<IpoSource> sources,List<IpoDocument> documents,Recommendation analysis){}
}
