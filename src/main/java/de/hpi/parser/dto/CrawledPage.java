package de.hpi.parser.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@RequiredArgsConstructor
public class CrawledPage {

    private final Date fetchedDate;

    private final long shopID;

    private final String content;

    private final String url;

}
