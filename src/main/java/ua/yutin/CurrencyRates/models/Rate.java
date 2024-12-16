package ua.yutin.CurrencyRates.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Table(name = "rates")
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double value;

    private int assetId;

    public Rate() {
    }

    public Rate(double value, int assetId) {
        this.value = value;
        this.assetId = assetId;
    }
}
