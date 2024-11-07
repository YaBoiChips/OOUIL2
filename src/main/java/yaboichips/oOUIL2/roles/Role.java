package yaboichips.oOUIL2.roles;

import java.util.ArrayList;
import java.util.List;

public class Role {

    public static String MAYOR;
    public static String DOCTOR;
    public static String SEER;
    public static String DETECTIVE;
    public static String TRAP;

    //neutral
    public static String SWAPPER;
    public static String JURY;
    public static String NURSE;
    public static String WATCHER;
    public static String BODYGUARD;

    //bad
    public static String TESTIFICATE = "Testificate";
    public static String ESPUR;
    public static String ACCOMPLICE;
    public static String LIAR;
    public static String JESTER;
    public static String ASSASSIN;

    public static String ANGEL;
    public static String SERIAL_KILLER;


    public static List<String> roles = new ArrayList<>();

    public Role() {
    }

    static {
        //good
        MAYOR = createRole("Mayor");
        DOCTOR = createRole("Doctor");
        SEER = createRole("Seer");
        DETECTIVE = createRole("Detective");
        TRAP = createRole("Trap");
        ANGEL = createRole("Angel");

        //neutral
        SWAPPER = createRole("Swapper");
        JURY = createRole("Jury");
        NURSE = createRole("Nurse");
        WATCHER = createRole("Watcher");
        BODYGUARD = createRole("Bodyguard");

        //bad
        ESPUR = createRole("Espur");
        ACCOMPLICE = createRole("Accomplice");
        LIAR = createRole("Liar");
        JESTER = createRole("Jester");
        ASSASSIN = createRole("Assassin");
        SERIAL_KILLER = createRole("Serial Killer");
    }

    public static String getRoleByName(String name) {
        for (String role : roles) {
            if (role.equals(name)) {
                return role;
            }
        }
        return null;
    }

    public static String createRole(String name) {
        roles.add(name);
        return name;
    }

    public static void init() {
        System.out.println("Roles init");
    }
}