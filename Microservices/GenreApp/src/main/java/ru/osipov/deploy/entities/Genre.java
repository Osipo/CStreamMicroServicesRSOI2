package ru.osipov.deploy.entities;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "genre")
public class Genre {
	 @Id
	 @Column(name= "gid", nullable = false)
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long gid;

	 @Column(name = "gname", nullable = false)
	 private String name;

	 @Column(name = "remarks")//allows null values.
	 private String remarks;

	 @Override
	 public boolean equals(Object o) {
		 if (this == o) return true;
	     if (o == null || getClass() != o.getClass()) return false;
	        Genre g = (Genre) o;
	        return Objects.equal(gid, g.gid) && Objects.equal(name, g.name) &&
                    (remarks == null) ? g.remarks == null : Objects.equal(remarks, g.remarks);
	 }

	 @Override
	 public int hashCode() {
		 return Objects.hashCode(gid, name, remarks != null ? remarks : 0);
	 }

	 @Override
	 public String toString() {
		 return com.google.common.base.MoreObjects.toStringHelper(this)
				 .add("name", name)
				 .add("remarks", remarks)
				 .toString();
	 }
}
