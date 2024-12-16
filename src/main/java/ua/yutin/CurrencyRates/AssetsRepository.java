package ua.yutin.CurrencyRates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.yutin.CurrencyRates.models.Asset;

@Repository
public interface AssetsRepository extends JpaRepository<Asset, Integer> {
}
