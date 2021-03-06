package pl.jsolve.sweetener.criteria;

import org.junit.Test;

import pl.jsolve.sweetener.collection.Collections;
import pl.jsolve.sweetener.collection.data.Address;
import pl.jsolve.sweetener.collection.data.Company;
import pl.jsolve.sweetener.collection.data.Person;
import pl.jsolve.sweetener.criteria.Restriction.RestrictionLevel;

import java.util.Collection;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static pl.jsolve.sweetener.criteria.Restrictions.and;
import static pl.jsolve.sweetener.criteria.Restrictions.or;

public class CriteriaTest {

    @Test
    public void shouldSortListOfRestrictionByRestrictionLevel() {
        // given
        Criteria criteria = Criteria.newCriteria().add(Restrictions.equals("name", "John"))
                .add(Restrictions.isNotNull("lastName")).add(Restrictions.notEquals("age", 35))
                .add(Restrictions.isNull("company"));

        // when
        List<Restriction> sortedRestrictions = criteria.getSortedRestrictions();

        // then
        assertThat(sortedRestrictions).onProperty("restrictionLevel").containsExactly(RestrictionLevel.HIGH,
                RestrictionLevel.HIGH, RestrictionLevel.MEDIUM, RestrictionLevel.MEDIUM);
    }

    @Test
    public void shouldFilterGivenCollectionByNotNullRestriction() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.isNotNull("company")));

        // then
        assertThat(filteredList).hasSize(2);
    }

    @Test
    public void shouldFilterGivenCollectionByNotNullRestrictionForNestedObject() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.isNotNull("company.name")));

        // then
        assertThat(filteredList).hasSize(2);
    }

    @Test
    public void shouldFilterGivenCollectionByNotNullRestrictionForNestedObject2() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(
                people,
                Criteria.newCriteria().add(Restrictions.isNotNull("company.name"))
                        .add(Restrictions.isNull("company.address.city")));

        // then
        assertThat(filteredList).hasSize(1);
    }

    @Test
    public void shouldFilterGivenCollectionByEqWithIgnoringCase() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.equals("name", "Marry")));

        // then
        assertThat(filteredList).hasSize(1);
    }

    @Test
    public void shouldFilterGivenCollectionByEq() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.equals("age", 27)));

        // then
        assertThat(filteredList).hasSize(1);
        assertThat(filteredList).onProperty("name").contains("John");
        assertThat(filteredList).onProperty("lastName").contains("Wolf");
    }

    @Test
    public void shouldFilterGivenCollectionByNotEqWithIgnoringCase() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.notEquals("name", "marry", true)));

        // then
        assertThat(filteredList).hasSize(3);
        assertThat(filteredList).onProperty("name").contains("John", "John", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Wolf", "Sky", "Hunt");
    }

    @Test
    public void shouldFilterGivenCollectionByGreater() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.greater("age", 27)));

        // then
        assertThat(filteredList).hasSize(3);
        assertThat(filteredList).onProperty("name").contains("John", "Marry", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Sky", "Duke", "Hunt");
    }

    @Test
    public void shouldFilterGivenCollectionByLess() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.less("age", 27)));

        // then
        assertThat(filteredList).hasSize(0);
    }

    @Test
    public void shouldFilterGivenCollectionByEqOrGreater() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.greaterOrEquals("age", 31)));

        // then
        assertThat(filteredList).hasSize(3);
        assertThat(filteredList).onProperty("name").contains("John", "Marry", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Sky", "Duke", "Hunt");
    }

    @Test
    public void shouldFilterGivenCollectionByEqOrLess() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.lessOrEquals("age", 27)));

        // then
        assertThat(filteredList).hasSize(1);
        assertThat(filteredList).onProperty("name").contains("John");
        assertThat(filteredList).onProperty("lastName").contains("Wolf");
    }

    @Test
    public void shouldFilterGivenCollectionByLike() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.like("name", "ar")));

        // then
        assertThat(filteredList).hasSize(1);
        assertThat(filteredList).onProperty("name").contains("Marry");
        assertThat(filteredList).onProperty("lastName").contains("Duke");
    }

    @Test
    public void shouldFilterGivenCollectionByNotLike() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.notLike("name", "ar")));

        // then
        assertThat(filteredList).hasSize(3);
        assertThat(filteredList).onProperty("name").contains("John", "John", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Wolf", "Sky", "Hunt");
    }

    @Test
    public void shouldFilterGivenCollectionByContainsForList() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.contains("categoriesOfDrivingLicense", "B")));

        // then
        assertThat(filteredList).hasSize(3);
        assertThat(filteredList).onProperty("name").contains("John", "Marry", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Sky", "Duke", "Hunt");
    }

    @Test
    public void shouldFilterGivenCollectionByContainsForArray() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.containsAny("children", "Aston")));

        // then
        assertThat(filteredList).hasSize(1);
        assertThat(filteredList).onProperty("name").contains("Peter");
        assertThat(filteredList).onProperty("lastName").contains("Hunt");
    }

    @Test
    public void shouldFilterGivenCollectionByContainsForPrimitiveArray() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.containsAny("luckyNumbers", 15)));

        // then
        assertThat(filteredList).hasSize(1);
        assertThat(filteredList).onProperty("name").contains("John");
        assertThat(filteredList).onProperty("lastName").contains("Sky");
    }

    @Test
    public void shouldFilterGivenCollectionByContainsForAnyObject() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.containsAny("categoriesOfDrivingLicense", "A", "D")));

        // then
        assertThat(filteredList).hasSize(2);
        assertThat(filteredList).onProperty("name").contains("Marry", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Duke", "Hunt");
    }

    @Test
    public void shouldFilterGivenCollectionByNotContains() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.notContains("categoriesOfDrivingLicense", "A", "B")));

        // then
        assertThat(filteredList).hasSize(3);
        assertThat(filteredList).onProperty("name").contains("John", "John", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Wolf", "Sky", "Hunt");
    }

    @Test
    public void shouldFilterGivenCollectionByNotContainsForAnyObjects() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.notContainsAny("categoriesOfDrivingLicense", "A", "B")));

        // then
        assertThat(filteredList).hasSize(1);
        assertThat(filteredList).onProperty("name").contains("John");
        assertThat(filteredList).onProperty("lastName").contains("Wolf");
    }

    @Test
    public void shouldFilterGivenCollectionByTwoConditionsJoinedByOr() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(
                people,
                Criteria.newCriteria().add(
                        or(Restrictions.equals("age", 27), Restrictions.equals("age", 31),
                                Restrictions.equals("age", 41))));

        // then
        assertThat(filteredList).hasSize(3);
        assertThat(filteredList).onProperty("name").contains("John", "John", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Wolf", "Sky", "Hunt");
    }

    @Test
    public void shouldFilterGivenCollectionByTwoConditionsJoinedByAnd() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(and(Restrictions.equals("name", "John"), Restrictions.equals("age", 31))));

        // then
        assertThat(filteredList).hasSize(1);
        assertThat(filteredList).onProperty("name").contains("John");
        assertThat(filteredList).onProperty("lastName").contains("Sky");
    }

    @Test
    public void shouldFilterGivenCollectionByComplexConditions() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(
                people,
                Criteria.newCriteria().add(
                        or(and(Restrictions.equals("name", "John"), Restrictions.equals("age", 31)),
                                or(Restrictions.isNotNull("categoriesOfDrivingLicense"),
                                        Restrictions.isNotNull("company")))));

        // then
        assertThat(filteredList).hasSize(3);
        assertThat(filteredList).onProperty("name").contains("John", "John", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Sky", "Duke", "Hunt");
    }

    @Test
    public void shouldFilterGivenCollectionByBetweenRestriction() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.between("age", 27, 33)));

        // then
        assertThat(filteredList).hasSize(2);
        assertThat(filteredList).onProperty("name").contains("John", "John");
        assertThat(filteredList).onProperty("lastName").contains("Wolf", "Sky");
    }

    @Test
    public void shouldFilterGivenCollectionByBetweenRestriction2() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.between("age", 27, 27)));

        // then
        assertThat(filteredList).hasSize(1);
        assertThat(filteredList).onProperty("name").contains("John");
        assertThat(filteredList).onProperty("lastName").contains("Wolf");
    }

    @Test
    public void shouldFilterGivenCollectionByBetweenRestriction3() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.between("age", 27, 41, false, false)));

        // then
        assertThat(filteredList).hasSize(1);
        assertThat(filteredList).onProperty("name").contains("John");
        assertThat(filteredList).onProperty("lastName").contains("Sky");
    }

    @Test
    public void shouldFilterGivenCollectionByBetweenRestriction4() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.between("age", 27, 41, true, false)));

        // then
        assertThat(filteredList).hasSize(2);
        assertThat(filteredList).onProperty("name").contains("John", "John");
        assertThat(filteredList).onProperty("lastName").contains("Wolf", "Sky");
    }

    @Test
    public void shouldFilterGivenCollectionByBetweenRestriction5() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.between("age", 27, 41, false, true)));

        // then
        assertThat(filteredList).hasSize(2);
        assertThat(filteredList).onProperty("name").contains("John", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Hunt", "Sky");
    }

    @Test
    public void shouldFilterGivenCollectionByBetweenRestriction6() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.between("age", 27, 41, true, true)));

        // then
        assertThat(filteredList).hasSize(3);
        assertThat(filteredList).onProperty("name").contains("John", "John", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Wolf", "Hunt", "Sky");
    }

    @Test
    public void shouldFilterGivenCollectionByNotBetweenRestriction() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.notBetween("age", 27, 33)));

        // then
        assertThat(filteredList).hasSize(2);
        assertThat(filteredList).onProperty("name").contains("Marry", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Duke", "Hunt");
    }

    @Test
    public void shouldFilterGivenCollectionByNotBetweenRestriction2() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.notBetween("age", 27, 27)));

        // then
        assertThat(filteredList).hasSize(3);
        assertThat(filteredList).onProperty("name").contains("John", "Marry", "Peter");

        assertThat(filteredList).onProperty("lastName").contains("Sky", "Duke", "Hunt");
    }

    @Test
    public void shouldFilterGivenCollectionByNotBetweenRestriction3() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.notBetween("age", Integer.MIN_VALUE, Integer.MAX_VALUE)));

        // then
        assertThat(filteredList).hasSize(0);
    }

    @Test
    public void shouldFilterGivenCollectionByNotBetweenRestriction4() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.notBetween("age", 27, 41, false, false)));

        // then
        assertThat(filteredList).hasSize(3);
        assertThat(filteredList).onProperty("name").contains("John", "Marry", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Wolf", "Duke", "Hunt");
    }

    @Test
    public void shouldFilterGivenCollectionByNotBetweenRestriction5() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.notBetween("age", 27, 41, true, false)));

        // then
        assertThat(filteredList).hasSize(2);
        assertThat(filteredList).onProperty("name").contains("Marry", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Duke", "Hunt");
    }

    @Test
    public void shouldFilterGivenCollectionByNotBetweenRestriction6() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.notBetween("age", 27, 41, false, true)));

        // then
        assertThat(filteredList).hasSize(2);
        assertThat(filteredList).onProperty("name").contains("John", "Marry");
        assertThat(filteredList).onProperty("lastName").contains("Wolf", "Duke");
    }

    @Test
    public void shouldFilterGivenCollectionByNotBetweenRestriction7() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.notBetween("age", 27, 41, true, true)));

        // then
        assertThat(filteredList).hasSize(1);
        assertThat(filteredList).onProperty("name").contains("Marry");
        assertThat(filteredList).onProperty("lastName").contains("Duke");
    }

    @Test
    public void shouldFilterGivenCollectionByInRestriction() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.in("name", "John", "Tom", "Lucas")));

        // then
        assertThat(filteredList).hasSize(2);
        assertThat(filteredList).onProperty("name").contains("John", "John");
        assertThat(filteredList).onProperty("lastName").contains("Sky", "Wolf");
    }

    @Test
    public void shouldFilterGivenCollectionByInRestriction2() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.in("age", 25, 26, 27, 28, 29, 30, 31)));

        // then
        assertThat(filteredList).hasSize(2);
        assertThat(filteredList).onProperty("name").contains("John", "John");
        assertThat(filteredList).onProperty("lastName").contains("Sky", "Wolf");
    }

    @Test
    public void shouldFilterGivenCollectionByInRestriction3() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections
                .filter(people, Criteria.newCriteria().add(Restrictions.in("age")));

        // then
        assertThat(filteredList).isEmpty();
    }

    @Test
    public void shouldFilterGivenCollectionByInRestriction4() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.in("company", new Object[] {null})));

        // then
        assertThat(filteredList).hasSize(2);
        assertThat(filteredList).onProperty("name").contains("John", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Wolf", "Hunt");
    }

    @Test
    public void shouldFilterGivenCollectionByNotInRestriction() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.notIn("name", "John", "Tom", "Lucas")));

        // then
        assertThat(filteredList).hasSize(2);
        assertThat(filteredList).onProperty("name").contains("Marry", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Hunt", "Duke");
    }

    @Test
    public void shouldFilterGivenCollectionByNotInRestriction2() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.notIn("age", 25, 26, 27, 28, 29, 30, 31)));

        // then
        assertThat(filteredList).hasSize(2);
        assertThat(filteredList).onProperty("name").contains("Marry", "Peter");
        assertThat(filteredList).onProperty("lastName").contains("Hunt", "Duke");
    }

    @Test
    public void shouldFilterGivenCollectionByNotInRestriction3() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.notIn("age", 25, 26, 27, 31, 40, 41, 45)));

        // then
        assertThat(filteredList).isEmpty();
    }

    @Test
    public void shouldFilterGivenCollectionByNotInRestriction4() {
        // given
        List<Person> people = prepareListOfPeople();

        // when
        Collection<Person> filteredList = Collections.filter(people,
                Criteria.newCriteria().add(Restrictions.notIn("company", new Object[] {null})));

        // then
        assertThat(filteredList).hasSize(2);
        assertThat(filteredList).onProperty("name").contains("John", "Marry");
        assertThat(filteredList).onProperty("lastName").contains("Sky", "Duke");
    }

    private List<Person> prepareListOfPeople() {
        List<Person> people = Collections.newArrayList();

        people.add(new Person("John", "Wolf", 27, null, null, null, new int[] {7, 11, 16}));
        people.add(new Person("John", "Sky", 31, new Company("EA", new Address("street1", "city1")),
                prepareListOfCategories("B"), new String[] {"Kate"}, new int[] {7, 15}));
        people.add(new Person("Marry", "Duke", 45, new Company("Oracle", new Address("street2", null)),
                prepareListOfCategories("A", "B"), new String[] {"Ainsley", "Ash"}, new int[] {13}));
        people.add(new Person("Peter", "Hunt", 41, null, prepareListOfCategories("B", "D"), new String[] {"Aston"},
                null));
        return people;
    }

    private List<String> prepareListOfCategories(String... values) {
        List<String> categories = Collections.newArrayList();
        for (String s : values) {
            categories.add(s);
        }
        return categories;
    }

}
