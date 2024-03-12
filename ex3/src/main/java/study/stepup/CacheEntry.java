package study.stepup;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter
public class CacheEntry {
    @Setter
    private Long expireTime;
    private final Object object;
}
