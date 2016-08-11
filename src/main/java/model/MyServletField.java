package model;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class MyServletField {
    private final long id;

    @NotNull
    private final long servletId;

    @NotNull
    @Size(max=50)
    private final String name;
}
