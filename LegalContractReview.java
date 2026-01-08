public class LegalContractReview extends ApartmentService {
    public LegalContractReview(Apartment apartment) {
        super(apartment);
    }

    @Override
    protected double getServiceCost() {
        return 1000; // מחיר השירות
    }

    @Override
    protected String getServiceDescription() {
        return "Legal contract review";
    }
}