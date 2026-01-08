import java.util.List;
import java.util.stream.Collectors;

public class PricePerSqmStrategy implements SearchStrategy {
    private final double referencePrice;
    private final String comparison;

    public PricePerSqmStrategy(double referencePrice, String comparison) {
        this.referencePrice = referencePrice;
        this.comparison = comparison.toLowerCase();
    }

    @Override
    public List<Apartment> search(List<Apartment> apartments, List<Integer> centerAddress, int radius) {
        return ApartmentSearchHelper.getApartmentsInRadius(apartments, centerAddress, radius)
                .stream()
                .filter(apartment -> comparePrices(apartment.getPricePerSquareMeter(), referencePrice, comparison))
                .collect(Collectors.toList());
    }

    private boolean comparePrices(double price, double reference, String comparison) {
        return switch (comparison) {
            case "higher" -> price > reference;
            case "lower" -> price < reference;
            case "equal" -> Math.abs(price - reference) < 0.01;
            default -> false;
        };
    }
}
