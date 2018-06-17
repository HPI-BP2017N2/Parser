package de.hpi.parser.persistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.hpi.parser.dto.CrawledPage;
import de.hpi.parser.service.OfferAttribute;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

import static de.hpi.parser.service.OfferAttribute.*;

@Document
@Getter
@Setter(AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParsedOffer {

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date crawlingTimestamp;

    private long shopID;

    @Id
    private String url;

    @Indexed
    private String ean;

    @Indexed
    private String han;

    @Indexed
    private String imageUrl;

    private String sku;

    private String title;

    private String price;

    private String description;

    private String brandName;

    private String category;

    public ParsedOffer(Map<OfferAttribute, String> extractedData, CrawledPage page) {
        setCrawlingTimestamp(page.getFetchedDate());
        setUrl(page.getUrl());
        setEan(extractedData.get(EAN));
        setHan(extractedData.get(HAN));
        setSku(extractedData.get(SKU));
        setTitle(extractedData.get(TITLE));
        setPrice(extractedData.get(PRICE));
        setDescription(extractedData.get(DESCRIPTION));
        setBrandName(extractedData.get(BRAND));
        setCategory(extractedData.get(CATEGORY));
        setImageUrl(extractedData.get(IMAGE_URLS));
        setShopID(page.getShopId());
    }
}