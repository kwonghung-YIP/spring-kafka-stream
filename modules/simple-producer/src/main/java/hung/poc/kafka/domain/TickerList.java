package hung.poc.kafka.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Data
@Configuration
@ConfigurationProperties(prefix = "tickers")
public class TickerList {

    private MathContext mc = new MathContext(4, RoundingMode.HALF_UP);
    private Random rand = new Random();
    private List<String> tickers;
    private Map<String, BigDecimal> quotes;

    @PostConstruct
    public void postConfig() {
        this.tickers = List.copyOf(quotes.keySet());
    }

    public String getRandTicker() {
        return tickers.get(rand.nextInt(tickers.size()));
    }

    public BigDecimal genNextQuote(String ticker) {
        BigDecimal quote = quotes.get(ticker);
        if (quote != null) {
            BigDecimal change = BigDecimal.valueOf(rand.nextGaussian()*quote.doubleValue()*0.05);
            quote = quote.add(change,mc);
            quotes.put(ticker,quote);
        }
        return quote;
    }
}
