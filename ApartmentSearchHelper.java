import java.util.ArrayList;
import java.util.List;

public class ApartmentSearchHelper {
    public static List<Apartment> getApartmentsInRadius(List<Apartment> apartments, List<Integer> centerAddress, int radius) {
        int centerStreet = centerAddress.get(0);
        int centerAvenue = centerAddress.get(1);

        List<Apartment> result = apartments.stream()
                .filter(apartment -> {
                    int aptStreet = apartment.getAddress().get(0);
                    int aptAvenue = apartment.getAddress().get(1);
                    return Math.abs(aptStreet - centerStreet) <= radius && Math.abs(aptAvenue - centerAvenue) <= radius;
                })
                .toList();

        List<Apartment> allNearbyApartments = new ArrayList<>();
        for (Apartment apt : result) {
            collectSubApartments(apt, allNearbyApartments);
        }

        return allNearbyApartments;
    }

    private static void collectSubApartments(Apartment apartment, List<Apartment> list) {
        list.add(apartment);
        for (Apartment sub : apartment.getSubUnits()) {
            collectSubApartments(sub, list);
        }
    }
}
