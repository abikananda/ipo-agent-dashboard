package com.abikananda.ipo.ai;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty; import org.springframework.stereotype.Component; import java.util.List;
@Component @ConditionalOnProperty(name="ipo.ai.provider",havingValue="disabled",matchIfMissing=true) public class DisabledNarrativeAnalyzer implements IpoNarrativeAnalyzer {
 public NarrativeResult analyze(String text){return new NarrativeResult("AI analysis is not configured",List.of(),List.of(),"disabled","none");}
 public boolean available(){return false;}
}
