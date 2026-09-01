export interface Ipo { id:number; slug:string; companyName:string; type:'MAINBOARD'|'SME'; status:string; sector:string; openDate:string; closeDate:string; listingDate:string; priceMin:number; priceMax:number; lotSize:number; issueSizeCrore:number; freshIssueCrore:number; ofsCrore:number; updatedAt:string; }
export interface Analysis { recommendation:string; overallScore:number; confidenceScore:number; listingGainRecommendation:string; longTermRecommendation:string; summary:string; scoreBreakdown:Record<string,number>; positiveFactors:string[]; negativeFactors:string[]; missingInformation:string[]; }
export interface Market { gmp:number; qibSubscription:number; niiSubscription:number; retailSubscription:number; totalSubscription:number; sourceName:string; sourceUrl:string; observedAt:string; }
export interface IpoSummary { ipo:Ipo; analysis:Analysis; latestMarket:Market|null; }
export interface Financial { periodEnd:string; revenueCrore:number; ebitdaCrore:number; patCrore:number; totalDebtCrore:number; operatingCashFlowCrore:number; }
export interface Risk { severity:string; category:string; description:string; documentPage:number|null; sourceUrl:string|null; hardOverride:boolean; }
export interface Source { sourceName:string; sourceUrl:string; sourceType:string; reliability:string; retrievedAt:string; }
export interface Valuation { peRatio:number|null; priceToBook:number|null; evEbitda:number|null; sectorMedianPe:number|null; valuationPremiumPct:number|null; }
export interface IpoDetail { ipo:Ipo; financials:Financial[]; marketHistory:Market[]; valuation:Valuation|null; risks:Risk[]; sources:Source[]; documents:unknown[]; analysis:Analysis; }

