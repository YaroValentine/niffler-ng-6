package guru.qa.niffler.data.entity.spend;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "category")
public class CategoryEntity implements Serializable {

  private UUID id;

  private String name;

  private String username;

  private boolean archived;

}
