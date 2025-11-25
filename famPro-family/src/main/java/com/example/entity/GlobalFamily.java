//package com.example.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.Set;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
//
//public class GlobalFamily {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGlob")
//    @SequenceGenerator(
//            name = "seqGlob",
//            sequenceName = "globFamily", initialValue = 1)
//    Long id;
//
//    @OneToMany (mappedBy = "globalFamily")
//    Set<Family> members;
//
//    @Column(name = "count")
//    Integer number;
//
//    @ManyToMany
//    @JoinTable(name = "global_family_guard",
//            joinColumns = @JoinColumn(name = "global_family_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "guard_id", referencedColumnName = "id"))
//    private Set<Guard> guard;
//}
