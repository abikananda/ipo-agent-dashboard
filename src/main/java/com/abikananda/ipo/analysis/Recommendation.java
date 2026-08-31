package com.abikananda.ipo.analysis;

import java.util.List;
import java.util.Map;

public record Recommendation(
    String recommendation, int overallScore, int confidenceScore,
    String listingGainRecommendation, String longTermRecommendation,
    String summary, Map<String,Integer> scoreBreakdown,
    List<String> positiveFactors, List<String> negativeFactors,
    List<String> missingInformation) {}

