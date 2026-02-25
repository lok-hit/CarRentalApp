package infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "rate-limits")
public class RateLimitProperties {

    private Map<String, Limit> limits = new HashMap<>();

    public Map<String, Limit> getLimits() {
        return limits;
    }

    public void setLimits(Map<String, Limit> limits) {
        this.limits = limits;
    }

    public static class Limit {
        private int replenishRate;
        private int burstCapacity;

        public int getReplenishRate() {
            return replenishRate;
        }

        public void setReplenishRate(int replenishRate) {
            this.replenishRate = replenishRate;
        }

        public int getBurstCapacity() {
            return burstCapacity;
        }

        public void setBurstCapacity(int burstCapacity) {
            this.burstCapacity = burstCapacity;
        }
    }
}

