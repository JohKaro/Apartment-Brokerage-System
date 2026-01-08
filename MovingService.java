public class MovingService extends ApartmentService {
    public MovingService(Apartment apartment) {
        super(apartment);
    }

    @Override
    protected double getServiceCost() {
        return 1500;
    }

    @Override
    protected String getServiceDescription() {
        return "Moving services";
    }
}