package ua.yutin.CurrencyRates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.yutin.CurrencyRates.models.Rate;

import java.util.List;
import java.util.Optional;


@Repository
public interface RatesRepository extends JpaRepository<Rate, Integer> {
}