/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
//$Id$
package org.hibernate.test.annotations.collectionelement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

/**
 * @author Emmanuel Bernard
 */
@Entity
@AttributeOverrides({
		@AttributeOverride( name="characters.element", column = @Column(name="fld_character") ),
		@AttributeOverride( name="scorePerNickName.element", column = @Column(name="fld_score") ),
		@AttributeOverride( name="favoriteToys.element.brand.surname", column = @Column(name = "fld_surname"))}
)
@Table(name="tbl_Boys")
public class Boy {
	private Integer id;
	private String firstName;
	private String lastName;
	private Set<String> nickNames = new HashSet<String>();
	private Set<String> hatedNames = new HashSet<String>();
	private Set<String> preferredNames = new HashSet<String>();
	private Map<String, Integer> scorePerNickName = new HashMap<String, Integer>();
	private Map<String, Integer> scorePerPreferredName = new HashMap<String, Integer>();
	private int[] favoriteNumbers;
	private Set<Toy> favoriteToys = new HashSet<Toy>();
	private Set<Character> characters = new HashSet<Character>();
	private Map<String, FavoriteFood> foods = new HashMap<String,FavoriteFood>();
	private Set<CountryAttitude> countryAttitudes = new HashSet<CountryAttitude>();

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@ElementCollection
	public Set<String> getNickNames() {
		return nickNames;
	}

	public void setNickNames(Set<String> nickName) {
		this.nickNames = nickName;
	}

	@ElementCollection //default column names
	public Set<String> getHatedNames() {
		return hatedNames;
	}

	public void setHatedNames(Set<String> hatedNames) {
		this.hatedNames = hatedNames;
	}

	@ElementCollection //default column names
	@Column
	public Set<String> getPreferredNames() {
		return preferredNames;
	}

	public void setPreferredNames(Set<String> preferredNames) {
		this.preferredNames = preferredNames;
	}

	@ElementCollection	
	@MapKeyColumn(nullable=false)
	public Map<String, Integer> getScorePerPreferredName() {
		return scorePerPreferredName;
	}

	public void setScorePerPreferredName(Map<String, Integer> scorePerPreferredName) {
		this.scorePerPreferredName = scorePerPreferredName;
	}

	@ElementCollection
	@CollectionTable(name = "ScorePerNickName", joinColumns = @JoinColumn(name = "BoyId"))
	@Column(name = "score", nullable = false)
	@MapKeyColumn(nullable=false)
	public Map<String, Integer> getScorePerNickName() {
		return scorePerNickName;
	}

	public void setScorePerNickName(Map<String, Integer> scorePerNickName) {
		this.scorePerNickName = scorePerNickName;
	}

	@ElementCollection
	@CollectionTable(
			name = "BoyFavoriteNumbers",
			joinColumns = @JoinColumn(name = "BoyId")
	)
	@Column(name = "favoriteNumber", nullable = false)
	@OrderColumn(name = "nbr_index")
	public int[] getFavoriteNumbers() {
		return favoriteNumbers;
	}

	public void setFavoriteNumbers(int[] favoriteNumbers) {
		this.favoriteNumbers = favoriteNumbers;
	}
	@ElementCollection
	@AttributeOverride(name = "element.serial", column = @Column(name = "serial_nbr"))
	public Set<Toy> getFavoriteToys() {
		return favoriteToys;
	}

	public void setFavoriteToys(Set<Toy> favoriteToys) {
		this.favoriteToys = favoriteToys;
	}

	@ElementCollection
	@Enumerated(EnumType.STRING)
    @Column(name = "`characters`")
	public Set<Character> getCharacters() {
		return characters;
	}

	public void setCharacters(Set<Character> characters) {
		this.characters = characters;
	}

	@ElementCollection
	@Enumerated(EnumType.STRING)
	@MapKeyColumn(nullable=false)
	public Map<String, FavoriteFood> getFavoriteFood() {
		return foods;
	}

	public void setFavoriteFood(Map<String, FavoriteFood>foods) {
		this.foods = foods;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	//@Where(clause = "b_likes=false")
	public Set<CountryAttitude> getCountryAttitudes() {
		return countryAttitudes;
	}

	public void setCountryAttitudes(Set<CountryAttitude> countryAttitudes) {
		this.countryAttitudes = countryAttitudes;
	}
}

