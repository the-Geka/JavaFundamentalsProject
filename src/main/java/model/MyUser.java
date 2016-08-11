package model;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Value
public class MyUser {
    private final long id;

    @NotNull (message = "Error. Email. Not present")
    @Pattern(regexp = "^\\w+@\\w+\\.[a-zA-Z]{2,6}$",
            message = "Error. Email. Regexp")
    @Size(max = 256, min = 6, message = "Error. Email. Size")
    private final String email;

    @NotNull (message = "Error. Password. Not present")
    @Size(max = 16, min = 4, message = "Error. Password. Size")
    private final String password;

    @NotNull (message = "Error. LastName. Not present")
    @Size(max = 30, min = 2, message = "Error. LastName. Size")
    private final String lastName;

    @NotNull (message = "Error. FirstName. Not present")
    @Size(max = 30, min = 2, message = "Error. firstName. Size")
    private final String firstName;

    private final long localeId;
}
