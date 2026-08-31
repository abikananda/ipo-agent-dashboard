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
  public IpoController(IpoRepository i, FinancialPeriodRepository f, MarketSnapshotRepository m, IpoScoringService s){ipos=i;financials=f;markets=m;scoring=s;}
  @GetMapping public List<IpoSummary> all(){ return ipos.findAll().stream().map(this::summary).toList(); }
  @GetMapping("/{slug}") public IpoDetail one(@PathVariable String slug){
    Ipo ipo=ipos.findBySlug(slug).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"IPO not found"));
    var f=financials.findByIpoIdOrderByPeriodEndAsc(ipo.getId()); var m=markets.findTop30ByIpoIdOrderByObservedAtDesc(ipo.getId());
    return new IpoDetail(ipo,f,m,scoring.score(ipo,f,m.stream().findFirst().orElse(null)));
  }
  private IpoSummary summary(Ipo ipo){
    var f=financials.findByIpoIdOrderByPeriodEndAsc(ipo.getId()); var m=markets.findTop30ByIpoIdOrderByObservedAtDesc(ipo.getId());
    return new IpoSummary(ipo,scoring.score(ipo,f,m.stream().findFirst().orElse(null)),m.stream().findFirst().orElse(null));
  }
  public record IpoSummary(Ipo ipo, Recommendation analysis, MarketSnapshot latestMarket){}
  public record IpoDetail(Ipo ipo,List<FinancialPeriod> financials,List<MarketSnapshot> marketHistory,Recommendation analysis){}
}

