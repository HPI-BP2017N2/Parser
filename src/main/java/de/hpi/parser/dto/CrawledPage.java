package de.hpi.parser.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class CrawledPage {

    private Date fetchedDate;

    private long shopID;

    private String content;

    private String url;

}
