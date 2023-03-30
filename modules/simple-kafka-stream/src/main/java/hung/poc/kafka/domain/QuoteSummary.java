package hung.poc.kafka.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class QuoteSummary {

    public QuoteSummary(Double v) {}
    private String ticker;
    private BigDecimal latest;
    private BigDecimal change;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal open;
}
