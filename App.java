package ec.edu.utpl.computacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Connection conn =
                DriverManager.getConnection("jdbc:h2:~/dbpa", "sa", "");

        //1. CREAR TABLA
        //createTable(conn);

        //2. INSERTAR LOS DATOS
        //insertData(conn);

        //3. CONSULTAR LOS DATOS
        //getAllData(conn);

        //4. PROMEDIO EDADES
        //System.out.println(getAverageAGE(conn));

        //5. CONSULTAR NAME QUE INICIAN CON "E"
        //getEName(conn);

        //6. BUSCAR POR ID
        /*Scanner lector = new Scanner(System.in);
        System.out.print("INGRESE EL ID: ");
        String idSearch = lector.nextLine();
        searchValueId(idSearch, conn);*/

        //7. INSERTAR LISTA DE OBJETOS TIPO PERSON
        /*List<Person> people = List.of(
                new Person(0, "Tais", "Valarezo", 18),
                new Person(0 , "Kevin", "Regalado", 22),
                new Person(0, "Ronin", "Montero", 20),
                new Person(0, "Hermin", "Espinoza", 20),
                new Person(0, "Jeremy", "Escudero", 23),
                new Person(0, "Oliver", "Chuquimarca", 21)
        );
        insertDataPerson(people, conn);*/

        //8. DEVOLVER LISTA DE OBJETOS PERSONA
        System.out.println(convertList(conn));

        conn.close();
    }

    private static void getAllData(Connection conn) throws SQLException{
        String select = "SELECT ID, FIRST_NAME, LAST_NAME, AGE FROM REGISTRATION";
        try(Statement stsmt = conn.createStatement();
            ResultSet rs = stsmt.executeQuery(select)
        ) {
            while (rs.next()) {
                System.out.printf("%d - %s %s - (%d)\n",
                        rs.getInt("ID"),
                        rs.getString("LAST_NAME"),
                        rs.getString("FIRST_NAME"),
                        rs.getInt("AGE"));
            }

        }
    }

    private static void insertData(Connection conn) throws SQLException {
        var data = """
            INSERT INTO REGISTRATION VALUES (1, 'CARLOS', 'MOROCHO', 19);
            INSERT INTO REGISTRATION VALUES (2, 'LEONARDO', 'CHUQUIMARCA', 19);
            INSERT INTO REGISTRATION VALUES (3, 'PABLO', 'REYES', 18);
            INSERT INTO REGISTRATION VALUES (4, 'RENE', 'ELIZALDE', 40);
            INSERT INTO REGISTRATION VALUES (5, 'ELIZABETH', 'CADME', 43);
            """;
        try (Statement stmt = conn.createStatement()) {
            int count = stmt.executeUpdate(data);
            System.out.println(count);
        }
    }

    private static void createTable(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            var sqlCreateTable = """
                CREATE TABLE IF NOT EXISTS REGISTRATION
                (
                ID INTEGER NOT NULL,
                FIRST_NAME VARCHAR(255),
                LAST_NAME VARCHAR(255),
                AGE INTEGER,
                CONSTRAINT REGISTRATION_pkey PRIMARY KEY (ID)
                );
                """;

            stmt.executeUpdate(sqlCreateTable);
            System.out.println("TABLA CREADA");
        }
    }

    private static void getEName(Connection conn) throws SQLException {
        var selectName = """
            SELECT FIRST_NAME
            FROM REGISTRATION
            WHERE FIRST_NAME LIKE 'E%'""";
        try (Statement stsmt = conn.createStatement();
             ResultSet rs = stsmt.executeQuery(selectName))
        { while (rs.next()) {
            System.out.printf("%s\n",
                    rs.getString("FIRST_NAME"));
        }
        }
    }

    private static double getAverageAGE(Connection conn) throws SQLException {
        var avgAge = """
            SELECT AVG(AGE)
            FROM REGISTRATION
            """;

        double output = -1;
        try(Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(avgAge)
        ) {
            rs.next();
            output = rs.getDouble("AVG(AGE)");
        }

        return output;
    }

    //INYECCION DE SQL
    /*private static void searchValueId(String id, Connection conn) throws SQLException{
    var selectBase = """
    SELECT ID, FIRST_NAME, LAST_NAME, AGE
    FROM REGISTRATION
    WHERE ID = %s
    """;
    var select = String.format(selectBase, id);

    try(Statement stsmt = conn.createStatement();
    ResultSet rs = stsmt.executeQuery(select)
    ) {
    while (rs.next()) {
        System.out.printf("%d - %s %s - (%d)\n",
            rs.getInt("ID"),
            rs.getString("LAST_NAME"),
            rs.getString("FIRST_NAME"),
            rs.getInt("AGE"));
    }

    }
    }*/

    //SIN PODER INYECTAR SQL
    private static void searchValueId(String id, Connection conn) throws SQLException{
        var select = """
            SELECT ID, FIRST_NAME, LAST_NAME, AGE
            FROM REGISTRATION
            WHERE ID = ?
            """;
        try(PreparedStatement pStmt = conn.prepareStatement(select)) {
            pStmt.setString(1, id);
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%d - %s %s - (%d)\n",
                            rs.getInt("ID"),
                            rs.getString("LAST_NAME"),
                            rs.getString("FIRST_NAME"),
                            rs.getInt("AGE"));
                }
            }
        }
    }

    private static void insertDataPerson(List<Person> people, Connection conn) throws SQLException {
        String data = """
            INSERT INTO REGISTRATION 
            VALUES (?, ?, ?, ?);
            """;

        try (PreparedStatement stmt = conn.prepareStatement(data)) {
            for (int i = 0; i < people.size(); i++) {
                stmt.setInt(1, (getId(conn) + 1) );
                stmt.setString(2, people.get(i).obtenerFirstName());
                stmt.setString(3, people.get(i).obtenerLastName());
                stmt.setInt(4, people.get(i).obtenerEdad());
                stmt.executeUpdate();
            }
        }
    }

    private static int getId(Connection conn) throws SQLException{
        var selectIdMax = "SELECT MAX(ID) FROM REGISTRATION";
        try(Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectIdMax)){
            rs.next();
            return rs.getInt("MAX(ID)");
        }
    }

    private static List<Person> convertList(Connection conn) throws SQLException{
        var select = """
            SELECT ID, FIRST_NAME, LAST_NAME, AGE
            FROM REGISTRATION
            """;
        try(PreparedStatement pStmt = conn.prepareStatement(select)) {
            try (ResultSet rs = pStmt.executeQuery()) {
                List<Person> people = new ArrayList<>();
                while (rs.next()) {
                    people.add(new Person(rs.getInt("ID"),
                            rs.getString("LAST_NAME"),
                            rs.getString("FIRST_NAME"),
                            rs.getInt("AGE")));
                }
                return people;
            }
        }
    }

}