public class AirlineReservationQA {

    static int passed = 0;
    static int failed = 0;

    static void check(
            String testName, boolean condition) {

        if (condition) {
            System.out.println("[PASS] " + testName);
            passed++;
        } else {
            System.out.println("[FAIL] " + testName);
            failed++;
        }
    }

    static AirlineReservation.Flight flight(
            String number,
            int seats,
            double fare) {

        return new AirlineReservation.Flight(
                number,
                "Chennai",
                "Delhi",
                seats,
                fare);
    }

    public static void main(String[] args) {

        System.out.println(
                "======= AIRLINE RESERVATION QA =======\n");


        // 1. Successful booking
        try {

            AirlineReservation.Flight f =
                    flight("F1", 10, 5000);

            AirlineReservation.Booking b =
                    AirlineReservation.bookPassenger(
                            f, "B1", "Kavin",
                            "Adult", "Economy",
                            10, 30);

            check("Successful booking",
                    b != null &&
                    f.availableSeats == 9);

        } catch (Exception e) {
            check("Successful booking", false);
        }


        // 2. Double booking / two passengers
        try {

            AirlineReservation.Flight f =
                    flight("F2", 1, 5000);

            AirlineReservation.Booking b1 =
                    AirlineReservation.bookPassenger(
                            f, "B2", "Passenger1",
                            "Adult", "Economy",
                            10, 30);

            AirlineReservation.bookPassenger(
                    f, "B3", "Passenger2",
                    "Adult", "Economy",
                    10, 30);

            check("Double booking prevention", false);

        } catch (IllegalStateException e) {
            check("Double booking prevention", true);
        }


        // 3. Cancellation
        try {

            AirlineReservation.Flight f =
                    flight("F3", 10, 5000);

            AirlineReservation.Booking b =
                    AirlineReservation.bookPassenger(
                            f, "B4", "Passenger",
                            "Adult", "Economy",
                            10, 30);

            AirlineReservation.cancelBooking(
                    f, b, 30);

            check("Cancellation",
                    !b.active &&
                    f.availableSeats == 10);

        } catch (Exception e) {
            check("Cancellation", false);
        }


        // 4. Refund calculation
        try {

            AirlineReservation.Flight f =
                    flight("F4", 10, 5000);

            AirlineReservation.Booking b =
                    AirlineReservation.bookPassenger(
                            f, "B5", "Passenger",
                            "Adult", "Economy",
                            10, 30);

            double refund =
                    AirlineReservation.cancelBooking(
                            f, b, 30);

            double expected =
                    b.fare * 0.90;

            check("Refund calculation",
                    Math.abs(refund - expected) < 0.01);

        } catch (Exception e) {
            check("Refund calculation", false);
        }


        // 5. Fully booked flight
        try {

            AirlineReservation.Flight f =
                    flight("F5", 0, 5000);

            AirlineReservation.bookPassenger(
                    f, "B6", "Passenger",
                    "Adult", "Economy",
                    10, 30);

            check("Fully booked flight", false);

        } catch (IllegalStateException e) {
            check("Fully booked flight", true);
        }


        // 6. Invalid passenger
        try {

            AirlineReservation.Flight f =
                    flight("F6", 10, 5000);

            AirlineReservation.bookPassenger(
                    f, "B7", "",
                    "Adult", "Economy",
                    10, 30);

            check("Invalid passenger", false);

        } catch (IllegalArgumentException e) {
            check("Invalid passenger", true);
        }


        // 7. Excess baggage
        try {

            AirlineReservation.Flight f =
                    flight("F7", 10, 5000);

            AirlineReservation.Booking b =
                    AirlineReservation.bookPassenger(
                            f, "B8", "Passenger",
                            "Adult", "Economy",
                            30, 30);

            double expectedMinimum =
                    5000 + (10 * 500);

            check("Excess baggage charge",
                    b.fare >= expectedMinimum);

        } catch (Exception e) {
            check("Excess baggage charge", false);
        }


        // 8. Economy class
        try {

            AirlineReservation.Flight f =
                    flight("F8", 10, 5000);

            double fare =
                    AirlineReservation.calculateFare(
                            f, "Economy",
                            "Adult", 10, 30);

            check("Economy class",
                    Math.abs(fare - 5000) < 0.01);

        } catch (Exception e) {
            check("Economy class", false);
        }


        // 9. Business class
        try {

            AirlineReservation.Flight f =
                    flight("F9", 10, 5000);

            double fare =
                    AirlineReservation.calculateFare(
                            f, "Business",
                            "Adult", 10, 30);

            check("Business class",
                    Math.abs(fare - 8750) < 0.01);

        } catch (Exception e) {
            check("Business class", false);
        }


        // 10. First class
        try {

            AirlineReservation.Flight f =
                    flight("F10", 10, 5000);

            double fare =
                    AirlineReservation.calculateFare(
                            f, "First",
                            "Adult", 10, 30);

            check("First class",
                    Math.abs(fare - 12500) < 0.01);

        } catch (Exception e) {
            check("First class", false);
        }


        // 11. Child passenger
        try {

            AirlineReservation.Flight f =
                    flight("F11", 10, 5000);

            double fare =
                    AirlineReservation.calculateFare(
                            f, "Economy",
                            "Child", 10, 30);

            check("Child passenger pricing",
                    Math.abs(fare - 3750) < 0.01);

        } catch (Exception e) {
            check("Child passenger pricing", false);
        }


        // 12. Senior passenger
        try {

            AirlineReservation.Flight f =
                    flight("F12", 10, 5000);

            double fare =
                    AirlineReservation.calculateFare(
                            f, "Economy",
                            "Senior", 10, 30);

            check("Senior passenger pricing",
                    Math.abs(fare - 4250) < 0.01);

        } catch (Exception e) {
            check("Senior passenger pricing", false);
        }


        // 13. Flight search
        try {

            AirlineReservation.Flight f =
                    flight("F13", 10, 5000);

            boolean result =
                    AirlineReservation.searchFlight(
                            f,
                            "Chennai",
                            "Delhi");

            check("Flight search", result);

        } catch (Exception e) {
            check("Flight search", false);
        }


        // 14. Seat availability
        try {

            AirlineReservation.Flight f =
                    flight("F14", 5, 5000);

            AirlineReservation.bookPassenger(
                    f, "B14", "Passenger",
                    "Adult", "Economy",
                    10, 30);

            check("Seat availability",
                    f.availableSeats == 4);

        } catch (Exception e) {
            check("Seat availability", false);
        }


        // 15. High occupancy dynamic pricing
        try {

            AirlineReservation.Flight f =
                    flight("F15", 10, 5000);

            // Book 8 seats
            for (int i = 0; i < 8; i++) {
                AirlineReservation.bookPassenger(
                        f,
                        "X" + i,
                        "Passenger" + i,
                        "Adult",
                        "Economy",
                        10,
                        30);
            }

            double fare =
                    AirlineReservation.calculateFare(
                            f,
                            "Economy",
                            "Adult",
                            10,
                            30);

            check("High occupancy dynamic pricing",
                    fare > 5000);

        } catch (Exception e) {
            check("High occupancy dynamic pricing", false);
        }


        // 16. Near travel date dynamic pricing
        try {

            AirlineReservation.Flight f =
                    flight("F16", 10, 5000);

            double fare =
                    AirlineReservation.calculateFare(
                            f,
                            "Economy",
                            "Adult",
                            10,
                            2);

            check("Near travel date pricing",
                    fare > 5000);

        } catch (Exception e) {
            check("Near travel date pricing", false);
        }


        // 17. Invalid travel class
        try {

            AirlineReservation.Flight f =
                    flight("F17", 10, 5000);

            AirlineReservation.calculateFare(
                    f,
                    "InvalidClass",
                    "Adult",
                    10,
                    30);

            check("Invalid travel class", false);

        } catch (IllegalArgumentException e) {
            check("Invalid travel class", true);
        }


        // 18. Invalid baggage
        try {

            AirlineReservation.Flight f =
                    flight("F18", 10, 5000);

            AirlineReservation.calculateFare(
                    f,
                    "Economy",
                    "Adult",
                    -5,
                    30);

            check("Invalid baggage", false);

        } catch (IllegalArgumentException e) {
            check("Invalid baggage", true);
        }


        // 19. Invalid passenger type
        try {

            AirlineReservation.Flight f =
                    flight("F19", 10, 5000);

            AirlineReservation.calculateFare(
                    f,
                    "Economy",
                    "InvalidPassenger",
                    10,
                    30);

            check("Invalid passenger type", false);

        } catch (IllegalArgumentException e) {
            check("Invalid passenger type", true);
        }


        // 20. Cancellation restores seat
        try {

            AirlineReservation.Flight f =
                    flight("F20", 10, 5000);

            AirlineReservation.Booking b =
                    AirlineReservation.bookPassenger(
                            f,
                            "B20",
                            "Passenger",
                            "Adult",
                            "Economy",
                            10,
                            30);

            int seatsAfterBooking =
                    f.availableSeats;

            AirlineReservation.cancelBooking(
                    f, b, 30);

            check("Cancellation restores seat",
                    f.availableSeats ==
                    seatsAfterBooking + 1);

        } catch (Exception e) {
            check("Cancellation restores seat", false);
        }


        // Final result
        System.out.println(
                "\n========================================");

        System.out.println("Tests Passed : " + passed);
        System.out.println("Tests Failed : " + failed);
        System.out.println("Total Tests  : " + (passed + failed));

        if (failed == 0) {

            System.out.println(
                    "QA RESULT    : ALL TESTS PASSED");

        } else {

            System.out.println(
                    "QA RESULT    : SOME TESTS FAILED");

            // Makes Jenkins build fail
            System.exit(1);
        }

        System.out.println(
                "========================================");
    }
}