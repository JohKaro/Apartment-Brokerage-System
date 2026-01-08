import java.util.List;
import java.util.stream.Collectors;

public class SoldApartmentsStrategy implements SearchStrategy {
    @Override
    public List<Apartment> search(List<Apartment> apartments, List<Integer> centerAddress, int radius) {
        return ApartmentSearchHelper.getApartmentsInRadius(apartments, centerAddress, radius)
                .stream()
                .filter(Apartment::isSold)
                .collect(Collectors.toList());
    }
}
