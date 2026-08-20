public class AirlineReservation {

    static class Flight {
        String flightNumber;
        String source;
        String destination;
        int totalSeats;
        int availableSeats;
        double baseFare;

        Flight(String flightNumber, String source, String destination,
               int totalSeats, double baseFare) {

            this.flightNumber = flightNumber;
            this.source = source;
            this.destination = destination;
            this.totalSeats = totalSeats;
            this.availableSeats = totalSeats;
            this.baseFare = baseFare;
        }
    }

    static class Booking {
        String bookingId;
        String passengerName;
        String passengerType;
        String travelClass;
        int baggageKg;
        double fare;
        boolean active;

        Booking(String bookingId, String passengerName,
                String passengerType, String travelClass,
                int baggageKg, double fare) {

            this.bookingId = bookingId;
            this.passengerName = passengerName;
            this.passengerType = passengerType;
            this.travelClass = travelClass;
            this.baggageKg = baggageKg;
            this.fare = fare;
            this.active = true;
        }
    }

    // Flight search
    public static boolean searchFlight(
            Flight flight, String source, String destination) {

        return flight.source.equalsIgnoreCase(source)
                && flight.destination.equalsIgnoreCase(destination);
    }

    // Dynamic fare calculation
    public static double calculateFare(
            Flight flight,
            String travelClass,
            String passengerType,
            int baggageKg,
            int daysUntilTravel) {

        if (flight.availableSeats <= 0) {
            throw new IllegalStateException(
                    "Flight is fully booked");
        }

        double fare = flight.baseFare;

        // Class multiplier
        if (travelClass.equalsIgnoreCase("Economy")) {
            fare *= 1.0;
        }
        else if (travelClass.equalsIgnoreCase("Business")) {
            fare *= 1.75;
        }
        else if (travelClass.equalsIgnoreCase("First")) {
            fare *= 2.5;
        }
        else {
            throw new IllegalArgumentException(
                    "Invalid travel class");
        }

        // Passenger type adjustment
        if (passengerType.equalsIgnoreCase("Child")) {
            fare *= 0.75;
        }
        else if (passengerType.equalsIgnoreCase("Senior")) {
            fare *= 0.85;
        }
        else if (passengerType.equalsIgnoreCase("Adult")) {
            fare *= 1.0;
        }
        else {
            throw new IllegalArgumentException(
                    "Invalid passenger type");
        }

        // Dynamic pricing based on available seats
        double occupancy =
                ((double) (flight.totalSeats - flight.availableSeats)
                        / flight.totalSeats) * 100;

        if (occupancy >= 80) {
            fare *= 1.40;
        }
        else if (occupancy >= 50) {
            fare *= 1.20;
        }
        else {
            fare *= 1.00;
        }

        // Dynamic pricing based on travel date
        if (daysUntilTravel <= 3) {
            fare *= 1.30;
        }
        else if (daysUntilTravel <= 7) {
            fare *= 1.15;
        }

        // Baggage charge
        if (baggageKg < 0) {
            throw new IllegalArgumentException(
                    "Invalid baggage weight");
        }

        if (baggageKg > 20) {
            fare += (baggageKg - 20) * 500;
        }

        return fare;
    }

    // Passenger booking
    public static Booking bookPassenger(
            Flight flight,
            String bookingId,
            String passengerName,
            String passengerType,
            String travelClass,
            int baggageKg,
            int daysUntilTravel) {

        if (passengerName == null ||
                passengerName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Invalid passenger name");
        }

        if (bookingId == null ||
                bookingId.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Invalid booking ID");
        }

        if (flight.availableSeats <= 0) {
            throw new IllegalStateException(
                    "Flight is fully booked");
        }

        double fare = calculateFare(
                flight,
                travelClass,
                passengerType,
                baggageKg,
                daysUntilTravel);

        flight.availableSeats--;

        return new Booking(
                bookingId,
                passengerName,
                passengerType,
                travelClass,
                baggageKg,
                fare);
    }

    // Cancellation and refund
    public static double cancelBooking(
            Flight flight, Booking booking,
            int daysUntilTravel) {

        if (booking == null || !booking.active) {
            throw new IllegalStateException(
                    "Invalid or already cancelled booking");
        }

        double refundPercentage;

        if (daysUntilTravel >= 15) {
            refundPercentage = 0.90;
        }
        else if (daysUntilTravel >= 7) {
            refundPercentage = 0.75;
        }
        else if (daysUntilTravel >= 3) {
            refundPercentage = 0.50;
        }
        else {
            refundPercentage = 0.25;
        }

        double refund =
                booking.fare * refundPercentage;

        booking.active = false;

        flight.availableSeats++;

        return refund;
    }

    public static void main(String[] args) {

        // Built-in input
        Flight flight = new Flight(
                "AI101",
                "Chennai",
                "Delhi",
                100,
                5000);

        String passengerName = "Kavin";
        String passengerType = "Adult";
        String travelClass = "Economy";
        int baggageKg = 25;
        int daysUntilTravel = 10;

        try {

            // Flight search
            boolean found = searchFlight(
                    flight,
                    "Chennai",
                    "Delhi");

            System.out.println(
                    "========== AIRLINE RESERVATION ==========");

            System.out.println("Flight Search     : "
                    + (found ? "FOUND" : "NOT FOUND"));

            // Booking
            Booking booking = bookPassenger(
                    flight,
                    "B001",
                    passengerName,
                    passengerType,
                    travelClass,
                    baggageKg,
                    daysUntilTravel);

            System.out.println("Booking ID        : "
                    + booking.bookingId);

            System.out.println("Passenger         : "
                    + booking.passengerName);

            System.out.println("Passenger Type    : "
                    + booking.passengerType);

            System.out.println("Class             : "
                    + booking.travelClass);

            System.out.println("Baggage           : "
                    + booking.baggageKg + " kg");

            System.out.printf(
                    "Dynamic Fare      : Rs.%.2f\n",
                    booking.fare);

            System.out.println("Available Seats   : "
                    + flight.availableSeats);

            // Demonstrate cancellation/refund
            double refund = cancelBooking(
                    flight,
                    booking,
                    daysUntilTravel);

            System.out.printf(
                    "Refund            : Rs.%.2f\n",
                    refund);

            System.out.println("Seats After Cancel: "
                    + flight.availableSeats);

            System.out.println(
                    "==========================================");

        } catch (Exception e) {

            System.out.println(
                    "Error: " + e.getMessage());
        }
    }
}