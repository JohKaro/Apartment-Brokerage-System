import java.util.List;

public interface SearchStrategy {
    List<Apartment> search(List<Apartment> apartments, List<Integer> centerAddress, int radius);
}
