package com.abikananda.ipo.analysis;
import com.abikananda.ipo.domain.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
class IpoScoringServiceTest {
 @Test void rewardsGrowthAndPositiveCashFlow(){
  Ipo ipo=Ipo.builder().companyName("Test").sector("Tech").issueSizeCrore(new BigDecimal("100")).freshIssueCrore(new BigDecimal("80")).build();
  FinancialPeriod a=FinancialPeriod.builder().periodEnd(LocalDate.of(2025,3,31)).revenueCrore(new BigDecimal("100")).patCrore(BigDecimal.TEN).build();
  FinancialPeriod b=FinancialPeriod.builder().periodEnd(LocalDate.of(2026,3,31)).revenueCrore(new BigDecimal("140")).patCrore(new BigDecimal("20")).operatingCashFlowCrore(new BigDecimal("18")).build();
  Recommendation r=new IpoScoringService().score(ipo,List.of(a,b),null);
  assertThat(r.scoreBreakdown().get("financialQuality")).isEqualTo(100);
  assertThat(r.positiveFactors()).contains("Revenue is growing","Positive operating cash flow");
 }
 @Test void insufficientDataDoesNotPretendToBeConfident(){
  Recommendation r=new IpoScoringService().score(Ipo.builder().build(),List.of(),null);
  assertThat(r.recommendation()).isEqualTo("INSUFFICIENT_DATA"); assertThat(r.confidenceScore()).isLessThan(50);
 }
 @Test void criticalRiskForcesAvoidAndCapsScore(){
  Ipo ipo=Ipo.builder().sector("Tech").issueSizeCrore(BigDecimal.valueOf(100)).freshIssueCrore(BigDecimal.valueOf(100)).rhpUrl("https://sebi.gov.in/rhp.pdf").build();
  FinancialPeriod a=FinancialPeriod.builder().revenueCrore(BigDecimal.valueOf(100)).patCrore(BigDecimal.TEN).build();
  FinancialPeriod b=FinancialPeriod.builder().revenueCrore(BigDecimal.valueOf(200)).patCrore(BigDecimal.valueOf(30)).operatingCashFlowCrore(BigDecimal.valueOf(25)).build();
  MarketSnapshot m=MarketSnapshot.builder().gmp(BigDecimal.valueOf(50)).totalSubscription(BigDecimal.valueOf(20)).build();
  IpoValuation v=IpoValuation.builder().peRatio(BigDecimal.valueOf(20)).sectorMedianPe(BigDecimal.valueOf(25)).build();
  IpoRisk risk=IpoRisk.builder().severity(IpoRisk.Severity.CRITICAL).category("Governance").description("Material regulatory action").hardOverride(true).build();
  Recommendation r=new IpoScoringService().score(ipo,List.of(a,b),m,v,List.of(risk));
  assertThat(r.recommendation()).isEqualTo("AVOID");assertThat(r.overallScore()).isLessThanOrEqualTo(44);
 }
}
