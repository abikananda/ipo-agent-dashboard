package com.abikananda.ipo.analysis;

import com.abikananda.ipo.domain.*;
import org.springframework.stereotype.Service;
import java.math.*;
import java.util.*;

@Service
public class IpoScoringService {
  public Recommendation score(Ipo ipo, List<FinancialPeriod> periods, MarketSnapshot market) {
    return score(ipo, periods, market, null, List.of());
  }
  public Recommendation score(Ipo ipo, List<FinancialPeriod> periods, MarketSnapshot market, IpoValuation valuationData, List<IpoRisk> risks) {
    Map<String,Integer> scores = new LinkedHashMap<>();
    List<String> positives = new ArrayList<>(), negatives = new ArrayList<>(), missing = new ArrayList<>();
    int financial = financialScore(periods, positives, negatives, missing);
    int valuation = valuationScore(valuationData, positives, negatives, missing);
    int business = ipo.getSector() == null ? 40 : 60;
    int issue = issueScore(ipo, positives, negatives);
    int subscription = market == null || market.getTotalSubscription() == null ? 0 : clamp(market.getTotalSubscription().multiply(BigDecimal.TEN).intValue());
    int sentiment = market == null || market.getGmp() == null || ipo.getPriceMax() == null ? 0 : clamp(market.getGmp().multiply(BigDecimal.valueOf(100)).divide(ipo.getPriceMax(),2,RoundingMode.HALF_UP).intValue() * 2);
    if (market == null) missing.add("Current GMP and subscription data");
    scores.put("financialQuality", financial); scores.put("growthQuality", financial);
    scores.put("valuation", valuation); scores.put("businessQuality", business);
    scores.put("governance", 50); scores.put("issueStructure", issue);
    scores.put("subscriptionDemand", subscription); scores.put("marketSentiment", sentiment);
    int governance = governanceScore(risks, negatives); int riskAdjustment = riskScore(risks);
    scores.put("governance",governance); scores.put("riskAdjustment",riskAdjustment);
    int overall = Math.round(financial*.25f + financial*.15f + valuation*.20f + business*.10f + governance*.10f + issue*.05f + subscription*.05f + sentiment*.05f + riskAdjustment*.05f);
    boolean hardOverride=risks.stream().anyMatch(r->r.isHardOverride()||r.getSeverity()==IpoRisk.Severity.CRITICAL);
    if(hardOverride) overall=Math.min(overall,44);
    int present = 3 + (periods.size() >= 3 ? 3 : periods.size()) + (market == null ? 0 : 2) + (ipo.getRhpUrl() == null ? 0 : 1) + (valuationData==null?0:1);
    int confidence = Math.min(100, present * 10);
    String verdict = confidence < 50 ? "INSUFFICIENT_DATA" : hardOverride ? "AVOID" : overall >= 70 ? "APPLY" : overall >= 60 ? "APPLY_WITH_CAUTION" : "AVOID";
    String listing = sentiment >= 65 && subscription >= 65 ? "APPLY" : verdict;
    return new Recommendation(verdict, overall, confidence, listing, verdict,
        summary(verdict, positives, negatives), scores, positives, negatives, missing);
  }
  private int valuationScore(IpoValuation v,List<String> pos,List<String> neg,List<String> missing){
    if(v==null||v.getPeRatio()==null||v.getSectorMedianPe()==null){missing.add("Peer valuation data");return 50;}
    int cmp=v.getPeRatio().compareTo(v.getSectorMedianPe()); if(cmp<=0){pos.add("Valuation is at or below the sector median");return 75;}
    BigDecimal premium=v.getPeRatio().subtract(v.getSectorMedianPe()).multiply(BigDecimal.valueOf(100)).divide(v.getSectorMedianPe(),2,RoundingMode.HALF_UP);
    if(premium.compareTo(BigDecimal.valueOf(40))>0){neg.add("Valuation is materially above the sector median");return 25;} return 50;
  }
  private int governanceScore(List<IpoRisk> risks,List<String> neg){int score=80;for(IpoRisk r:risks){score-=switch(r.getSeverity()){case CRITICAL->60;case HIGH->25;case MEDIUM->10;case LOW->3;};}if(score<50&&!risks.isEmpty())neg.add("Governance or disclosure risks reduce the score");return clamp(score);}
  private int riskScore(List<IpoRisk> risks){int score=100;for(IpoRisk r:risks)score-=switch(r.getSeverity()){case CRITICAL->70;case HIGH->30;case MEDIUM->12;case LOW->4;};return clamp(score);}
  private int financialScore(List<FinancialPeriod> p, List<String> pos, List<String> neg, List<String> missing) {
    if (p.size() < 2) { missing.add("At least two comparable financial periods"); return 30; }
    FinancialPeriod first=p.getFirst(), last=p.getLast(); int score=50;
    if (grows(first.getRevenueCrore(),last.getRevenueCrore())) { score+=20; pos.add("Revenue is growing"); } else { score-=15; neg.add("Revenue growth is weak or negative"); }
    if (last.getPatCrore()!=null && last.getPatCrore().signum()>0) { score+=15; pos.add("Latest period is profitable"); } else { score-=20; neg.add("Latest profitability is negative or unavailable"); }
    if (last.getOperatingCashFlowCrore()!=null && last.getOperatingCashFlowCrore().signum()>0) { score+=15; pos.add("Positive operating cash flow"); }
    return clamp(score);
  }
  private int issueScore(Ipo ipo,List<String> pos,List<String> neg) {
    if (ipo.getIssueSizeCrore()==null || ipo.getIssueSizeCrore().signum()==0) return 40;
    BigDecimal fresh=Optional.ofNullable(ipo.getFreshIssueCrore()).orElse(BigDecimal.ZERO);
    int share=fresh.multiply(BigDecimal.valueOf(100)).divide(ipo.getIssueSizeCrore(),0,RoundingMode.HALF_UP).intValue();
    if(share>=60) pos.add("Most proceeds are from a fresh issue"); else neg.add("Offer-for-sale forms a large part of the issue");
    return clamp(30+share/2);
  }
  private boolean grows(BigDecimal a,BigDecimal b){ return a!=null&&b!=null&&b.compareTo(a)>0; }
  private int clamp(int n){ return Math.max(0,Math.min(100,n)); }
  private String summary(String v,List<String> p,List<String> n){ return v.replace('_',' ')+" based on available source-backed information. "+(p.isEmpty()?"":p.getFirst()+". ")+(n.isEmpty()?"":n.getFirst()+"."); }
}
