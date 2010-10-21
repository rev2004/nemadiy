/**
 * 
 */
package org.imirsel.nema.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.imirsel.nema.util.StringUtil;

/**
 * @author gzhu1
 *
 */
@Embeddable
public class Address implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4689112758904703240L;
    private String streetLine1,
            streetLine2,
            streetLine3;
    private String city;
    private String region;
    private String postcode;
    private String country;

    @Column(name = "address_streat_1")
    public String getStreetLine1() {
        return streetLine1;
    }

    public void setStreetLine1(String streetLine1) {
        this.streetLine1 = streetLine1;
    }

    @Column(name = "address_streat_2")
    public String getStreetLine2() {
        return streetLine2;
    }

    public void setStreetLine2(String streetLine2) {
        this.streetLine2 = streetLine2;
    }

    @Column(name = "address_streat_3")
    public String getStreetLine3() {
        return streetLine3;
    }

    public void setStreetLine3(String streetLine3) {
        this.streetLine3 = streetLine3;
    }

    @Column(name = "address_city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "address_region")
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Column(name = "address_postcode")
    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Column(name = "address_country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if ((o == null) || (!(o instanceof Address))) {
            return false;
        } else {
            final Address that = (Address) o;
            return (StringUtil.same(this.country, that.getCountry())
                    && StringUtil.same(this.region, that.getRegion())
                    && StringUtil.same(this.city, that.getCity())
                    && StringUtil.same(this.streetLine1, that.getStreetLine1())
                    && StringUtil.same(this.streetLine2, that.getStreetLine2())
                    && StringUtil.same(this.streetLine3, that.getStreetLine3()));
        }
    }
    static int cachedHashCode = 0;

    @Override
    public int hashCode() {
        if (cachedHashCode == 0) {
            String total = StringUtil.nonNull(streetLine1) + StringUtil.nonNull(streetLine2)
                    + StringUtil.nonNull(streetLine3) + StringUtil.nonNull(city)
                    + StringUtil.nonNull(region) + StringUtil.nonNull(country);
            cachedHashCode = total.hashCode();
        }
        return cachedHashCode;
    }

    @Override
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("Street:", this.streetLine1).append(this.streetLine2).append(this.streetLine3).append("City:", this.city).append("Region:", this.region).append("Postcode:", this.postcode).append("Country:", this.country);
        return sb.toString();
    }
}
