package com.example.moviepj.persistance.entity;

import javax.persistence.*;
import org.springframework.cache.annotation.Cacheable;

@Entity
@Table(name = "address", schema = "movie_copy")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id")
    private CityEntity city;

    @OneToOne(mappedBy = "address")
    private UserEntity user;

    public AddressEntity(String name, CityEntity city) {

        this.name = name;
        this.city = city;
    }

    public AddressEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Cacheable("addresses")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

}
