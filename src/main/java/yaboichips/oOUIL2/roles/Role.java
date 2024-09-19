package yaboichips.oOUIL2.roles;

import java.util.ArrayList;
import java.util.List;

public class Role {

    private int value;
    private String name;
    public static Role MAYOR;
    public static Role DOCTOR;
    public static Role SEER;
    public static Role DETECTIVE;
    public static Role TRAP;

    //neutral
    public static Role SWAPPER;
    public static Role JURY;
    public static Role NURSE;
    public static Role WATCHER;
    public static Role BODYGUARD;

    //bad
    public static Role TESTIFICATE;
    public static Role ESPUR;
    public static Role ACCOMPLICE;
    public static Role LIAR;
    public static Role JESTER;
    public static Role ASSASSIN;

    public static Role ANGEL;

    public static List<Role> roles = new ArrayList<>();
    public Role(int value, String name) {
        this.value = value;
        this.name = name;
    }
    static {

        //good
        MAYOR = createRole(1, "Mayor");
        DOCTOR = createRole(2, "Doctor");
        SEER = createRole(3, "Seer");
        DETECTIVE = createRole(4, "Detective");
        TRAP = createRole(5, "Trap");
        ANGEL = createRole(18, "Angel");

        //neutral
        SWAPPER = createRole(7, "Swapper");
        JURY = createRole(8, "Jury");
        NURSE = createRole(9, "Nurse");
        WATCHER = createRole(10, "Watcher");
        BODYGUARD = createRole(11, "Bodyguard");

        //bad
        TESTIFICATE = createRole(12, "Testificate");
        ESPUR = createRole(13, "Espur");
        ACCOMPLICE = createRole(14, "Accomplice");
        LIAR = createRole(15, "Liar");
        JESTER = createRole(16, "Jester");
        ASSASSIN = createRole(17, "Assassin");
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Role getRoleByName(String name) {
        for (Role role : roles) {
            if (role.getName().equals(name)){
                return role;
            }
        }
        return null;
    }

    public static Role getRoleByValue(int value){
        for (Role role : roles){
            if (role.getValue() == value){
                return role;
            }
        }
        return null;
    }

    public static Role createRole(int value, String name) {
        Role role = new Role(value, name);
        roles.add(role);
        return role;
    }
    public static void init(){
        System.out.println("Roles init");
    }
}