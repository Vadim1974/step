package study.stepup;


import lombok.Getter;

@Getter
public class CacheEntry {
    private Long expireTime;
    private final Object object;

    public CacheEntry(Long cacheVal, Object object) {
        setExpireTime(cacheVal);
        this.object = object;
    }

    public Boolean isExpired(){
        //Флаг для очистки устарелых данных. Если время 0, то по ТЗ кэш вечный
        return expireTime != 0 && expireTime < System.currentTimeMillis();
    }

    public void setExpireTime(Long cacheVal){
        expireTime = cacheVal > 0 ? cacheVal + System.currentTimeMillis() : 0;
    }
}
