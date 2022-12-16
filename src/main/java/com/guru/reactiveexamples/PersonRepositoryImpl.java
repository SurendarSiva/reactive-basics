package com.guru.reactiveexamples;

import com.guru.reactiveexamples.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonRepositoryImpl implements PersonRepository {

    Person michael = new Person(1,"Michael","Phelps");
    Person bolt = new Person(2,"Usian","Bolt");
    Person sachin = new Person(3,"Sachin","Tendulkar");
    Person jesse = new Person(4,"Jesse","Pinkman");



    @Override
    public Mono<Person> getById(final Integer id) {
        return findAll().filter(person -> person.getId() == id).next();
    }

    @Override
    public Flux<Person> findAll() {

        return Flux.just(michael,bolt,sachin,jesse);
    }
}
