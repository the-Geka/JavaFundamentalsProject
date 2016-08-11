package model;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class MyServlet {
    private final long id;

    @NotNull
    @Size(max=50)
    private final String name;
}
