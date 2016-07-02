package todo.kebejaol.todo.ListViewAdapter;

/**
 * Created by Jan on 28.06.16.
 */
public class Contact {

    private String id;
    private String name;
    private String todoID;
    private String phoneNumber;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    private String mail;

    public String getTodoID() {
        return todoID;
    }

    public void setTodoID(String todoID) {
        this.todoID = todoID;
    }

    public String getHasPhone() {
        return hasPhone;
    }

    public void setHasPhone(String hasPhone) {
        this.hasPhone = hasPhone;
    }

    private String hasPhone;


    public Contact(String id, String name, String hasPhone, String todoID, String mail, String phoneNumber) {
        super();
        this.id = id;
        this.name = name;
        this.hasPhone = hasPhone;
        this.todoID = todoID;
        this.phoneNumber = phoneNumber;
        this.mail = mail;

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
