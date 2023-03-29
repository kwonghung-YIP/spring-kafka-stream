package hung.poc.kafka.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Quote {
    private String ticker;
    private BigDecimal price;
}
