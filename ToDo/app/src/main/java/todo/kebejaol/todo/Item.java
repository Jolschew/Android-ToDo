package todo.kebejaol.todo;

/**
 * Created by Jan on 18.06.16.
 */
public class Item {
    private String id;
    private String todoName;
    private String description;
    private String expirationDate;
    private String isFavourite;
    private String isFinished;

    public Item(String name, String expirationDate, String isFavourite,  String isFinished) {
        super();
        this.todoName = name;
        this.expirationDate = expirationDate;
        this.isFavourite = isFavourite;
        this.isFinished = isFinished;
    }

    public String getTodoName() {
        return todoName;
    }

    public void setTodoName(String todoName) {
        this.todoName = todoName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }
}
