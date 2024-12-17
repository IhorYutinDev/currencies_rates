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
import ua.yutin.CurrencyRates.models.Asset;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@Testcontainers
@DataJpaTest
public class AssetsRepositoryTest {
    @Autowired
    private AssetsRepository assetsRepository;


    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");;


    @Test
    public void checkConnectionTest() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }


    @Test
    public void findAllTest() {
        List<Asset> assets = assetsRepository.findAll();
        assertThat(assets).isNotNull();
        assertThat(assets.size()).isEqualTo(1);
    }


    @Test
    public void findByNameTest() {
        // when
        Asset asset = assetsRepository.findById(1).orElse(null);

        // then
        assertThat(asset).isNotNull();
        assertThat(asset.getId()).isEqualTo(1);
        assertThat(asset.getName()).isEqualTo("UAH");
    }


    @Test
    public void saveTest() throws Exception {
        // given
        Asset asset = new Asset("USD");

        // when
        assetsRepository.save(asset);

        // then
        Asset findAsset = assetsRepository.findById(asset.getId()).orElse(null);

        assertThat(findAsset).isNotNull();

        assertThat(findAsset.getName()).isEqualTo("USD");
        assertThat(findAsset.getId()).isEqualTo(2);
    }


    @Test
    public void checkConstraintSaveTest() throws Exception {
        Asset asset = new Asset("USD");
        assetsRepository.save(asset);

        Asset findAsset = assetsRepository.findById(asset.getId()).orElse(null);

        assertThat(findAsset).isNotNull();

        assertThat(findAsset.getName()).isEqualTo("USD");

        assertThatThrownBy(() -> assetsRepository.save(new Asset("USD")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
