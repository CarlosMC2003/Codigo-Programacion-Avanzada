package ec.edu.utpl.computacion;

public class Person {
    private int Id;
    private String First_name;
    private String Last_name;
    private int Age;

    public Person(int Ide, String Fname, String Lname, int Ag) {
        Id = Ide;
        First_name = Fname;
        Last_name = Lname;
        Age = Ag;
    }

    public void establecerId (int n) {
        Id = n;
    }

    public void establecerFirstName (String n) {
        First_name = n;
    }

    public void establecerLastName (String n) {
        Last_name = n;
    }

    public void establecerEdad (int n) {
        Age = n;
    }

    public int obtenerId() {
        return Id;
    }

    public String obtenerFirstName() {
        return First_name;
    }

    public String obtenerLastName() {
        return Last_name;
    }

    public int obtenerEdad() {
        return Age;
    }

    public String toString(){
        return String.format("ID : %d - " +
                        "Nombre: %s - " +
                        "Apellido: %s - " +
                        "Edad: %d\n",
                        obtenerId(),
                obtenerFirstName(),
                obtenerLastName(),
                obtenerEdad());
        }
}
