package model;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class MyServletFieldLocale {
    @NotNull
    private final long servletFieldId;

    @NotNull
    private final long localeId;

    @NotNull
    @Size(max=1000)
    private final String text;
}
