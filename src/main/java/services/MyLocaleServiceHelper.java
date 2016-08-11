package services;

import java.util.Map;

import static java.util.Optional.ofNullable;

public class MyLocaleServiceHelper {

    private Map<String, String> map;
    private String servletName;
    private long localeId;

    MyLocaleServiceHelper(String servletName, long localeId, Map<String, String> map) {
        this.map = map;
        this.servletName = servletName;
        this.localeId = localeId;
    }

    public String get(String field) {
        //return ofNullable(map.get(field)).orElse(servletName + "!" + localeId + "!" + field);
        return ofNullable(map.get(field)).orElse("!" + field);
    }
}
