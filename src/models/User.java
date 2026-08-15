package models;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import enums.Gender;

public class User {
   private static final AtomicLong ID_GENERATOR = new AtomicLong();
   private final long id;
   private String firstName;
   private String lastName;
   private final LocalDate dateOfBirth;
   private final Gender gender;

   public User(String firstName, String lastName, LocalDate dateOfBirth, Gender gender){
      if(firstName == null || firstName.isBlank()) 
         throw new IllegalArgumentException("Имя должно быть заполнено.");

      if(lastName == null || lastName.isBlank()) 
         throw new IllegalArgumentException("Фамилия должна быть заполнена.");

      if(dateOfBirth == null) 
         throw new IllegalArgumentException("Дата рождения должна быть указана.");

      if(gender == null) 
         throw new IllegalArgumentException("Пол должен быть указан.");

      this.id = ID_GENERATOR.incrementAndGet();
      this.firstName = firstName;
      this.lastName = lastName;
      this.dateOfBirth = dateOfBirth;
      this.gender = gender;
   }

   public void setFirstName(String firstName){
      if(firstName == null || firstName.isBlank()) 
         throw new IllegalArgumentException("Имя должно быть заполнено.");

      this.firstName = firstName;
   }

   public void setLastName(String lastName){
      if(firstName == null || firstName.isBlank()) 
         throw new IllegalArgumentException("Фамилия должна быть заполнена.");

      this.lastName = lastName;
   }

   public String getFirstName(){
      return firstName;
   }

   public String getLastName(){
      return lastName;
   }

   public LocalDate getDateOfBirthday(){
      return dateOfBirth;
   }

   public Gender getGender(){
      return gender;
   }

   public String getStringGender(){
      return gender == Gender.MALE ? "Мужской" : "Женский";
   }

   public long getId(){
      return id;
   }
}