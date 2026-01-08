public class InteriorDesign extends ApartmentService {
    public InteriorDesign(Apartment apartment) {
        super(apartment);
    }

    @Override
    protected double getServiceCost() {
        return 3000;
    }

    @Override
    protected String getServiceDescription() {
        return "Interior design services";
    }
}
