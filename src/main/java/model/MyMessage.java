package model;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Value
public class MyMessage {

    private final long userIdFrom;
    private final long userIdTo;

    private final Date datetime;

    @NotNull
    @Size(max=128)
    private final String text;

    private final boolean isRead;

    private final boolean isFrom;



}
