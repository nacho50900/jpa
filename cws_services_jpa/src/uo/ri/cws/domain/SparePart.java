package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TSPAREPARTS")
public class SparePart extends BaseEntity 
{
	// natural attributes
	@Column(unique=true) private String code;
	@Basic(optional=false) private String description;
	private double price;
	private int stock;
	private int minStock;
	private int maxStock;

	// accidental attributes
	@OneToMany (mappedBy="sparePart")
	private Set<Substitution> substitutions = new HashSet<>();
	
	SparePart(){}

	public SparePart(String code, String description, double price, int stock,
			int minStock, int maxStock) {
		
		ArgumentChecks.isNotBlank(code, "code can not be blank");
		ArgumentChecks.isNotBlank(description, "description can not be blank");
		ArgumentChecks.isTrue(price>=0, "price can not be negative");
		ArgumentChecks.isTrue(stock>=0, "stock can not be negative");
		ArgumentChecks.isTrue(minStock>=0, "minStock can not be negative");
		ArgumentChecks.isTrue(maxStock>=minStock,
				"maxStock can not be lower than mistock");
		this.code = code;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.minStock = minStock;
		this.maxStock = maxStock;
	}

	public SparePart(String code, String description, double price) {
		this(code, description, price, 0, 0, 1);
		
	}

	public SparePart(String code) {
		this(code, "no-description", 0, 0, 0, 1);
	}

	public Set<Substitution> getSubstitutions() {
		return new HashSet<>( substitutions );
	}

	Set<Substitution> _getSubstitutions() {
		return substitutions;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public double getPrice() {
		return price;
	}

	public double getStock() {
		return stock;
	}

	public int getMinStock() {
		return minStock;
	}

	public int getMaxStock() {
		return maxStock;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SparePart other = (SparePart) obj;
		return Objects.equals(code, other.code);
	}

	@Override
	public String toString() {
	    return "SparePart[code=" + code + ", description=" + description + 
	           ", price=" + price + ", stock=" + stock + 
	           ", minStock=" + minStock + ", maxStock=" + maxStock + "]";
	}

}
