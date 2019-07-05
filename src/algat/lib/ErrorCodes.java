package algat.lib;

public enum ErrorCodes {
    OVERFLOW("Il valore inserito è troppo grande!"),
    NEGATIVE("Il valore deve essere un intero positivo!"),
    EMPTY_VALUE("La stringa non può essere vuota!"),
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
