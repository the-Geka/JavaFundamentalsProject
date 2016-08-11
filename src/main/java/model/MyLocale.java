package model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.script.Compilable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ToString
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public final class MyLocale {
    private final long id;

    @NotNull
    @Size(max = 50)
    private final String name;

    @NotNull
    @Size(max = 8)
    private final String language;

    @Size(max = 3)
    private final String country;

    @Size(max = 4)
    private final String script;

    @Size(max = 8)
    private final String variant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyLocale myLocale = (MyLocale) o;

        return language.equals(myLocale.language) && (country != null ? country.equals(myLocale.country) : myLocale.country == null && (script != null ? script.equals(myLocale.script) : myLocale.script == null && (variant != null ? variant.equals(myLocale.variant) : myLocale.variant == null)));
    }

    @Override
    public int hashCode() {
        int result = language.hashCode();
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (script != null ? script.hashCode() : 0);
        result = 31 * result + (variant != null ? variant.hashCode() : 0);
        return result;
    }
}
