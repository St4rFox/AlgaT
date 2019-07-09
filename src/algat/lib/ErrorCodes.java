package algat.lib;

public enum ErrorCodes {
    FULL_TABLE("La tabella è piena!"),
    KEY_NOT_FOUND("Chiave inesistente!");

    private String message;

    ErrorCodes(String message){
        this.message = message;
    }


    @Override
    public String toString() {
        return message;
    }
}
