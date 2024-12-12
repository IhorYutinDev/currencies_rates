package ua.yutin.CurrencyRates.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


@Data
@Entity
@Table(name = "assets")
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    public Asset() {
    }

    public Asset(String name) {
        this.name = name;
    }

    public Asset(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return Objects.equals(name, asset.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
