import java.util.List;

public class AveragePricePerSqmStrategy {
    public static double calculate(List<Apartment> apartments, List<Integer> centerAddress, int radius) {
        List<Apartment> nearbyApartments = ApartmentSearchHelper.getApartmentsInRadius(apartments, centerAddress, radius);
        if (nearbyApartments.isEmpty()) return 0.0;

        double totalPricePerSqm = nearbyApartments.stream()
                .mapToDouble(Apartment::getPricePerSquareMeter)
                .sum();

        return totalPricePerSqm / nearbyApartments.size();
    }
}
