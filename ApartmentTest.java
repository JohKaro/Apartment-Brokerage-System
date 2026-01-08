import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;




public class ApartmentTest {

    @Test
    public void testApartmentCreation() {
        // יצירת דירה עם כתובת [4,5], גודל 100 מ"ר, ומחיר 500,000$
        Apartment apartment = new Apartment(List.of(4, 5), 100, 500000);

        // בדיקות לוודא שהערכים נשמרו נכון
        assertEquals(List.of(4, 5), apartment.getAddress()); // כתובת תקינה
        assertEquals(100, apartment.getSize()); // גודל תקין
        assertEquals(500000, apartment.getPrice()); // מחיר תקין
    }


    @Test
    public void testCannotCreateDuplicateApartment() {
        List<Apartment> allApartments = new ArrayList<>();
        allApartments.add(new Apartment(List.of(5, 5), 100, 500000));

        // קלט מדומה עבור יצירת דירה עם כתובת שכבר קיימת
        String input = "5-5\n5\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        // תפיסת הפלט של המערכת
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // הפעלת המתודה
        Apartment.addNewApartment(scanner, allApartments);

        // החזרת הפלט למצב הרגיל
        System.setOut(originalOut);

        // בדיקה שהפלט מכיל את הודעת השגיאה
        String output = outputStream.toString();
        assertTrue(output.contains("Error: An apartment with this exact address already exists."));
    }



        private static final List<Integer> TEST_ADDRESS = List.of(1000, 1000); // כתובת מיוחדת לבדיקה
        private static final double INITIAL_SIZE = 100.0;
        private static final double INITIAL_PRICE = 100000.0;

        @Test
        public void testCreateDeleteApartmentAndNotifyBroker() {
            // יצירת מוכר ומתווך
            Seller seller = new Seller("David");
            Broker broker = new Broker("John");

            // הוספת המתווך כמאזין למחיקות של המוכר
            seller.addObserver(broker);

            // טעינת הדירות מקובץ ה-CSV
            List<Apartment> allApartments = ApartmentCSVHandler.loadApartments();

            // יצירת דירה לבדיקה
            Apartment testApartment = new Apartment(TEST_ADDRESS, INITIAL_SIZE, INITIAL_PRICE);
            allApartments.add(testApartment);
            ApartmentCSVHandler.saveApartments(allApartments);

            // ** בדיקה שהדירה קיימת בקובץ לפני מחיקה**
            List<Apartment> updatedApartments = ApartmentCSVHandler.loadApartments();
            assertTrue(updatedApartments.contains(testApartment), "Test apartment should be in the list before deletion");

            // **לכידת הפלט של המערכת**
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));

            // ** מדמים קלט למחיקת הדירה**
            String deleteInput = "1000-1000\n";
            System.setIn(new ByteArrayInputStream(deleteInput.getBytes()));
            Scanner scanner = new Scanner(System.in);

            // ** מחיקת הדירה באמצעות המוכר**
            Apartment.deleteApartment(scanner, allApartments, seller);
            ApartmentCSVHandler.saveApartments(allApartments);

            // * טעינה מחדש של הדירות מהקובץ לווידוא שהדירה נמחקה**
            List<Apartment> finalApartments = ApartmentCSVHandler.loadApartments();
            assertFalse(finalApartments.contains(testApartment), "Test apartment should not be in the list after deletion");

            // ** בדיקה שהמתווך קיבל הודעה על מחיקת הדירה מהפלט**
            String consoleOutput = outputStream.toString();
            assertTrue(consoleOutput.contains("Broker " + broker.getName() + " has been notified"),
                    "Broker should receive a deletion notification");
            assertTrue(consoleOutput.contains("was deleted"),
                    "Broker's last notification should confirm apartment deletion.");
        }

    private static final List<Integer> CENTER_ADDRESS2 = List.of(600, 600);
    private static final int SEARCH_RADIUS2 = 1;

    private List<Apartment> testApartmentsForSearch;

    @BeforeEach
    public void setUpForSearchTests() {

        testApartmentsForSearch = new ArrayList<>();


        testApartmentsForSearch.add(new Apartment(List.of(600, 600), 100, 500000)); // 5000$ למ"ר
        testApartmentsForSearch.add(new Apartment(List.of(601, 600), 120, 600000)); // 5000$ למ"ר
        testApartmentsForSearch.add(new Apartment(List.of(599, 600), 80, 320000));  // 4000$ למ"ר
        testApartmentsForSearch.add(new Apartment(List.of(600, 601), 90, 450000));  // 5000$ למ"ר


        testApartmentsForSearch.add(new Apartment(List.of(605, 605), 200, 1000000)); // 5000$ למ"ר (אבל מחוץ לרדיוס)
    }

    @Test
    public void testCalculateAveragePricePerSqm() {
        // מחיר ממוצע צפוי למ"ר: (5000 + 5000 + 4000 + 5000) / 4
        double expectedAverage = (5000 + 5000 + 4000 + 5000) / 4.0;

        // חישוב בפועל באמצעות AveragePricePerSqmStrategy
        double actualAverage = AveragePricePerSqmStrategy.calculate(testApartmentsForSearch, CENTER_ADDRESS2, SEARCH_RADIUS2);

        // בדיקה שהתוצאה שקיבלנו תואמת את המחיר הצפוי
        assertEquals(expectedAverage, actualAverage, 0.01, "Average price per sqm should be calculated correctly");
    }



    @Test
    public void testGetApartmentsByPricePerSqm_Higher() {
        SearchStrategy strategy = new PricePerSqmStrategy(4500, "higher");
        List<Apartment> result = (List<Apartment>) strategy.search(testApartmentsForSearch, CENTER_ADDRESS2, SEARCH_RADIUS2);

        assertEquals(3, result.size(), "Should return 3 apartments with price per sqm higher than 4500");
        assertTrue(result.contains(testApartmentsForSearch.get(0)), "Apartment 600-600 should be included");
        assertTrue(result.contains(testApartmentsForSearch.get(1)), "Apartment 601-600 should be included");
        assertTrue(result.contains(testApartmentsForSearch.get(3)), "Apartment 600-601 should be included");
    }

    @Test
    public void testGetApartmentsByPricePerSqm_Lower() {
        SearchStrategy strategy = new PricePerSqmStrategy(4500, "lower");
        List<Apartment> result = (List<Apartment>) strategy.search(testApartmentsForSearch, CENTER_ADDRESS2, SEARCH_RADIUS2);

        assertEquals(1, result.size(), "Should return 1 apartment with price per sqm lower than 4500");
        assertTrue(result.contains(testApartmentsForSearch.get(2)), "Apartment 599-600 should be included");
    }

    @Test
    public void testGetApartmentsByPricePerSqm_Equal() {
        SearchStrategy strategy = new PricePerSqmStrategy(5000, "equal");
        List<Apartment> result = (List<Apartment>) strategy.search(testApartmentsForSearch, CENTER_ADDRESS2, SEARCH_RADIUS2);

        assertEquals(3, result.size(), "Should return 3 apartments with price per sqm exactly 5000");
        assertTrue(result.contains(testApartmentsForSearch.get(0)), "Apartment 600-600 should be included");
        assertTrue(result.contains(testApartmentsForSearch.get(1)), "Apartment 601-600 should be included");
        assertTrue(result.contains(testApartmentsForSearch.get(3)), "Apartment 600-601 should be included");
    }


}