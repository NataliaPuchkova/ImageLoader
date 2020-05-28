package alfa.house.ImageLoader.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name="images")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="datecreation", nullable = false)
    private OffsetDateTime date;

    @Column(name="type", nullable = false)
    private int type;

    @Column(name="path", nullable = false)
    private String path;

    @Column(name="parent", nullable = false)
    private String parent;

    @Column(name="ismain", nullable = false)
    private boolean isMain;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public Image setDate(OffsetDateTime date) {
        this.date = date;
        return this;
    }

    public int getType() {
        return type;
    }

    public Image setType(int type) {
        this.type = type;
        return this;
    }

    public String getPath() {
        return path;
    }

    public Image setPath(String path) {
        this.path = path;
        return this;
    }

    public String getParent() {
        return parent;
    }

    public Image setParent(String parent) {
        this.parent = parent;
        return this;
    }

    public boolean isMain() {
        return isMain;
    }

    public Image setMain(boolean main) {
        isMain = main;
        return this;
    }


}
