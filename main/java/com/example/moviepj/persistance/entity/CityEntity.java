package com.example.moviepj.persistance.entity;

import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "city", schema = "movie_copy")
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @OneToMany(mappedBy = "city")
    private List<AddressEntity> addresses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Cacheable("cities")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Cacheable("countries")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<AddressEntity> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressEntity> addresses) {
        this.addresses = addresses;
    }

    public CityEntity(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public CityEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CityEntity)) return false;
        CityEntity city = (CityEntity) o;
        return Objects.equals(getName(), city.getName()) && Objects.equals(getCountry(), city.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCountry());
    }
}
