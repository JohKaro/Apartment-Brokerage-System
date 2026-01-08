import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApartmentCSVHandler {
    private static final String FILE_NAME = "apartments.csv";
    private static final String HEADER = "address,size,price,isSold"; // כותרות העמודות

    // פונקציה ראשית לשמירת רשימת דירות (כולל דירות פנימיות) לקובץ CSV
    public static void saveApartments(List<Apartment> apartments) {
        // נבצע "flatten" לרשימת הדירות, כך שכולן (כולל הדירות הפנימיות) יהיו באותה רשימה
        List<Apartment> allApartments = new ArrayList<>();
        for (Apartment apt : apartments) {
            flattenApartment(apt, allApartments);
        }

        // כתיבת הנתונים לקובץ
        try {
            File file = new File(FILE_NAME);
            boolean isNewFile = !file.exists();
            // יוצרים את הקובץ אם לא קיים
            if (isNewFile) {
                file.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // כותבים כותרת אם הקובץ חדש
                writer.write(HEADER);
                writer.newLine();

                // כותבים את כל הנתונים
                for (Apartment apt : allApartments) {
                    // כתובת בפורמט 4-5-1, גודל, מחיר, סטטוס
                    String addressStr = convertAddressToString(apt.getAddress());
                    double size = apt.getSize();
                    double price = apt.getPrice();
                    boolean isSold = apt.isSold();

                    String line = String.format("%s,%.2f,%.2f,%b", addressStr, size, price, isSold);
                    writer.write(line);
                    writer.newLine();
                }
            }
            System.out.println("Apartments saved to " + FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // פונקציה שטוחה (flatten) - מוציאה את כל הדירות הפנימיות לרשימה אחת
    private static void flattenApartment(Apartment root, List<Apartment> collector) {
        collector.add(root);
        // עוברים רקורסיבית על תתי-הדירות
        for (Apartment sub : root.getSubUnits()) {
            flattenApartment(sub, collector);
        }
    }

    // המרה של הכתובת לרצף טקסטואלי (4-5-1)
    private static String convertAddressToString(List<Integer> address) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < address.size(); i++) {
            sb.append(address.get(i));
            if (i < address.size() - 1) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    // פונקציה לקריאת כל הדירות מהקובץ ובניית המבנה ההיררכי מחדש
    public static List<Apartment> loadApartments() {
        List<ApartmentData> flatApartments = new ArrayList<>();
        File file = new File(FILE_NAME);

        // במידה והקובץ לא קיים, ניצור אותו ונתייחס לרשימת דירות ריקה
        if (!file.exists()) {
            System.out.println("No apartments.csv file found. Creating a new file...");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // קוראים כותרת
            // מצפים לכותרת: address,size,price,isSold

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) continue;

                String addressStr = parts[0];
                double size = Double.parseDouble(parts[1]);
                double price = Double.parseDouble(parts[2]);
                boolean isSold = Boolean.parseBoolean(parts[3]);

                // ממירים את הכתובת לרשימת מספרים
                List<Integer> address = convertStringToAddress(addressStr);

                flatApartments.add(new ApartmentData(address, size, price, isSold));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // כעת יש לנו רשימה "שטוחה" של דירות - צריך לבנות מהן את העץ (subUnits)
        return rebuildApartmentsHierarchy(flatApartments);
    }

    // ממיר את המחרוזת 4-5-1 לרשימת [4, 5, 1]
    private static List<Integer> convertStringToAddress(String addressStr) {
        String[] nums = addressStr.split("-");
        List<Integer> address = new ArrayList<>();
        for (String num : nums) {
            address.add(Integer.parseInt(num));
        }
        return address;
    }

    // בניית עץ הדירות לפי כתובות
    private static List<Apartment> rebuildApartmentsHierarchy(List<ApartmentData> flatApts) {
        // ממיינים לפי אורך הכתובת כדי שנעבד קודם את הדירות ה"ראשיות"
        flatApts.sort(Comparator.comparingInt(o -> o.address.size()));

        // נשמור דירות לפי מחרוזת כתובת
        Map<String, Apartment> map = new HashMap<>();
        List<Apartment> topLevelApartments = new ArrayList<>();

        for (ApartmentData data : flatApts) {
            Apartment apt = new Apartment(data.address, data.size, data.price);
            apt.setSold(data.isSold);

            String addrStr = convertAddressToString(data.address);
            map.put(addrStr, apt);

            // אם יש 2 או פחות איברים בכתובת, זה נכס עליון
            if (data.address.size() <= 2) {
                topLevelApartments.add(apt);
            } else {
                // מציאת הכתובת של ההורה
                List<Integer> parentAddress = data.address.subList(0, data.address.size() - 1);
                String parentAddressStr = convertAddressToString(parentAddress);
                Apartment parent = map.get(parentAddressStr);
                if (parent != null) {
                    parent.getSubUnits().add(apt);
                } else {
                    // אם לא מוצאים הורה, זו כנראה טופ-לבל
                    topLevelApartments.add(apt);
                }
            }
        }

        return topLevelApartments;
    }

    // מבנה עזר לשמירת מידע שטוח על דירה
    static class ApartmentData {
        List<Integer> address;
        double size;
        double price;
        boolean isSold;

        public ApartmentData(List<Integer> address, double size, double price, boolean isSold) {
            this.address = address;
            this.size = size;
            this.price = price;
            this.isSold = isSold;
        }
    }
}