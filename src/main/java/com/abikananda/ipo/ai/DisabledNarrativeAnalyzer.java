package com.abikananda.ipo.ai;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean; import org.springframework.stereotype.Component; import java.util.List;
@Component @ConditionalOnMissingBean(IpoNarrativeAnalyzer.class) public class DisabledNarrativeAnalyzer implements IpoNarrativeAnalyzer {
 public NarrativeResult analyze(String text){return new NarrativeResult("AI analysis is not configured",List.of(),List.of(),"disabled","none");}
 public boolean available(){return false;}
}
