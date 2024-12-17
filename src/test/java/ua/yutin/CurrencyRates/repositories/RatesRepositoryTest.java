package ua.yutin.CurrencyRates.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.yutin.CurrencyRates.AssetsRepository;
import ua.yutin.CurrencyRates.RatesRepository;
import ua.yutin.CurrencyRates.models.Asset;
import ua.yutin.CurrencyRates.models.Rate;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@Testcontainers
@DataJpaTest
public class RatesRepositoryTest {
    @Autowired
    private RatesRepository ratesRepository;
    @Autowired
    private AssetsRepository assetsRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");


    @Test
    public void checkConnectionTest() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }


    @Test
    public void findAllTest() {
        List<Rate> rates = ratesRepository.findAll();
        assertThat(rates).isNotNull();
        assertThat(rates.size()).isEqualTo(1);
    }


    @Test
    public void findByAssetIdTest() {
        Rate rate = ratesRepository.findById(1).orElse(null);
        //
        assert rate != null;
        assertThat(rate.getId()).isEqualTo(1);
        assertThat(rate.getValue()).isEqualTo(43.84);
        assertThat(rate.getAssetId()).isEqualTo(1);
    }


    @Test
    public void saveTest() {
        Asset assetUSD = new Asset("USD");
        assetsRepository.save(assetUSD);
        int assetId = assetUSD.getId();

        assertThat(assetId).isEqualTo(2);

        Rate rate = new Rate(100.0, assetId);
        ratesRepository.save(rate);

        assertThat(rate.getAssetId()).isEqualTo(2);
        assertThat(rate.getValue()).isEqualTo(100.0);

        assertThatThrownBy(() -> ratesRepository.save(new Rate(100.0, assetId)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void checkConstraintSaveTest() throws Exception {
        assertThatThrownBy(() -> ratesRepository.save(new Rate(100.0, 1)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}