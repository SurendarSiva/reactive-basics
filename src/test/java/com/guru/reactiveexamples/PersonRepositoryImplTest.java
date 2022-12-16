package com.guru.reactiveexamples;

import com.guru.reactiveexamples.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

class PersonRepositoryImplTest {

    PersonRepositoryImpl personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new PersonRepositoryImpl();
    }

    @Test
    void getByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);
        Person person = personMono.block();
        System.out.println(person);
    }

    @Test
    void getByIdSubscribe(){
        Mono<Person> personMono = personRepository.getById(1);
        personMono.subscribe(person -> System.out.println(person));
    }

    @Test
    void getByIdSubscribeNotFound(){
        Mono<Person> personMono = personRepository.getById(7);
        personMono.subscribe(person -> System.out.println(person));
    }

    @Test
    void getByIdMapFunction() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.map(person -> {
           // System.out.println(person);
            return person.getFirstName();
        }).subscribe(fName->{
            System.out.println("from map: " +fName);

        });

    }

    @Test
    void fluxTestBlockFirst() {
        Flux<Person> personFlux = personRepository.findAll();
        Person person = personFlux.blockFirst();
        System.out.println(person);
    }

    @Test
    void testFluxSubscribe() {
        Flux<Person> personFlux = personRepository.findAll();
        personFlux.subscribe(person -> System.out.println(person));
    }

    @Test
    void testFluxtoListMono() {
        Flux<Person> personFlux = personRepository.findAll();

        Mono<List<Person>> personListMono = personFlux.collectList();

        personListMono.subscribe(list -> {
            list.forEach(System.out::println);

        });

    }

    @Test
    void testFindPersonById() {

        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 3;

        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).next();

        personMono.subscribe(person-> System.out.println(person));
    }

    @Test
    void testFindPersonByIdNotFound() {

        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 5;

        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).next();

        personMono.subscribe(person-> System.out.println(person));
    }

    @Test
    void testFindPersonByIdNotFoundWithException() {

        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 6;

        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).single();

        personMono.doOnError(throwable -> {
            System.out.println("Errored out here");
        }).onErrorReturn(Person.builder().id(id).build()).subscribe(person-> {
            System.out.println(person);
        });
    }
}

