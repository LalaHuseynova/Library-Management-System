package user;



public class AlreadyTaken extends RuntimeException {
    public AlreadyTaken() {
        super("This book has already been taken by you.");
    }

    public AlreadyTaken(String message) {
        super(message);
    }
}
