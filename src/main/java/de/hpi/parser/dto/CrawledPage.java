package de.hpi.parser.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CrawledPage {

    private Date fetchedDate;

    private long shopID;

    private String content;

    private String url;

}
