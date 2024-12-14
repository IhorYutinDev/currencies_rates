//package ua.yutin.CurrencyRates.repositories;
//
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import ua.yutin.CurrencyRates.models.Rate;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//public class RatesRepositoryTest {
//    @Autowired
//    private RatesRepository ratesRepository;
//
//    private Rate rate;
//
//    @BeforeEach
//    public void setUp() {
//        rate = new Rate();
//        rate.setAssetId(1);
//        rate.setValue(100.0);  // Assuming there's a 'value' field
//        ratesRepository.save(rate);  // Save the rate to the database for testing
//    }
//
//    @Test
//    public void testFindRateByAssetId() {
//        Optional<Rate> foundRate = ratesRepository.findRateByAssetId(1);
//        assertTrue(foundRate.isPresent(), "Rate should be present for asset ID 1");
//        assertEquals(1, foundRate.get().getAssetId(), "Asset ID should be 1");
//    }
//
//    @Test
//    public void testFindAll() {
//        List<Rate> rates = ratesRepository.findAll();
//        assertFalse(rates.isEmpty(), "The rates list should not be empty");
//    }
//
//    @Test
//    public void testFindRateByNonExistentAssetId() {
//        Optional<Rate> foundRate = ratesRepository.findRateByAssetId(999);
//        assertFalse(foundRate.isPresent(), "Rate should not be found for non-existent asset ID");
//    }
//}
