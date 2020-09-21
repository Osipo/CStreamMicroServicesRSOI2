package ru.osipov.deploy.entities;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.UUID;
import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "genres")
public class Genre {
	 @Id
	 @Column(name= "gid", nullable = false)
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long gid;

	 @Column(name = "gname", nullable = false)
	 private String name;

	 @Column(name = "remarks")//allows null values.
	 private String remarks;

     @Transient
     private String hashId;
     
     public Genre(){
         this.hashId = UUID.randomUUID().toString();
     }
     
	 @Override
	 public boolean equals(Object o) {
		 if (this == o) return true;
	     if (o == null || getClass() != o.getClass()) return false;
	        Genre g = (Genre) o;
	        return Objects.equal(hashId, g.hashId);
	 }

	 @Override
	 public int hashCode() {
		 return Objects.hashCode(hashId);
	 }

	 @Override
	 public String toString() {
		 return com.google.common.base.MoreObjects.toStringHelper(this)
				 .add("name", name)
				 .add("remarks", remarks)
				 .toString();
	 }
}
