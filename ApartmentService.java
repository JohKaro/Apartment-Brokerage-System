import java.util.ArrayList;
import java.util.List;

public abstract class ApartmentService extends Apartment {
    protected Apartment decoratedApartment;

    public ApartmentService(Apartment apartment) {
        super(apartment.getAddress(), apartment.getSize(), apartment.getPrice());
        this.decoratedApartment = apartment;
    }

    @Override
    public double getPrice() {
        return decoratedApartment.getPrice() + getServiceCost();
    }

    @Override
    public List<String> getServices() {
        List<String> services = new ArrayList<>(decoratedApartment.getServices());
        services.add(getServiceDescription());
        return services;
    }

    protected abstract double getServiceCost();
    protected abstract String getServiceDescription();
}