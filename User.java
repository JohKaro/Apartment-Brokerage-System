public abstract class User {
    protected String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // פעולה לצפייה בנתוני הנכס
    public void viewApartment(Apartment apartment) {
        if (apartment == null) {
            System.out.println("No apartment to view.");
            return;
        }
        // למשל, רק מציגים את toString()
        System.out.println(apartment);
    }
}
