package com.springproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "venues_info")
@Entity
public class VenuesInfo {
    @jakarta.persistence.Id
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "city")
    private String city;
    @Column(name = "country")
    private String country;
    @Column(name = "capacity")
    private String capacity;
    @Column(name = "surface")
    private String surface;
    @Column(name = "image")
    private String image;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
