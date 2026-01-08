public class PreMoveCleaning extends ApartmentService {
    public PreMoveCleaning(Apartment apartment) {
        super(apartment);
    }

    @Override
    protected double getServiceCost() {
        return 500;
    }

    @Override
    protected String getServiceDescription() {
        return "Pre-move cleaning";
    }
}