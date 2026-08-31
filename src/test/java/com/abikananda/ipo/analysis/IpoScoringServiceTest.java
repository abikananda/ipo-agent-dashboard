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
}
