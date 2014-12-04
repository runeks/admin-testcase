package no.valg.eva.admin.common;

import static java.lang.String.format;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains a 6 digit borough id (bydelsnr)
 */
public class BoroughId {

    private static final String BOROUGH_ID_REGEX = "^(\\d{6})$";
    private static final Pattern BOROUGH_ID_PATTERN = Pattern.compile(BOROUGH_ID_REGEX);

    private final String id;

    public BoroughId(String id) {
        Matcher matcher = BOROUGH_ID_PATTERN.matcher(id);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(format("illegal id <%s>, must match <%s>", id, BOROUGH_ID_PATTERN));
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
