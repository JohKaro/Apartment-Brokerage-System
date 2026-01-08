
import java.util.List;

class ApartmentFactory {
    public static Apartment createApartment(List<Integer> address, double size, double price) {
        return new Apartment(address, size, price);
    }
}
