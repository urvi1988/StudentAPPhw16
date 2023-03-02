package com.studentapp.studentinfo;

import com.studentapp.constants.EndPoints;
import com.studentapp.model.StudentPojo;
import com.studentapp.testbase.TestBase;
import com.studentapp.utils.TestUtils;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;
@RunWith(SerenityRunner.class)
public class StudentCRUDTest extends TestBase {

  static String firstName = "urvi"+ TestUtils.getRandomValue();
  static String lastName = "patel"+ TestUtils.getRandomValue();
  static String email = "uv"+ TestUtils.getRandomValue()+ "@gmail.com";
  static String programme = "sft testing";
  static Object studentId;


    @Title("This will create a new student")
    @Test
    public void test001(){

        List<String>courses= new ArrayList<>();
        courses.add("BBA");
        courses.add("MBA");

        StudentPojo studentPojo= new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);

        SerenityRest.given()
                .log().all()
                .header("Content-Type","application/json")
                .body(studentPojo)
                .when()
                .post()
                .then().log().all().statusCode(201);
    }

    @Title("Verify if student was created")
    @Test
    public void test002(){
        String part1 ="findAll{it.firstName=='";
        String part2 = "'}.get(0)";

        HashMap<String,?>studentMapData =SerenityRest.given()
                .log().all()
                .when()
                .get(EndPoints.GET_ALL_STUDENT)
                .then().statusCode(200).extract().path(part1+ firstName + part2); //findAll{it.firstName=='akshit69136'}.get(0)
        Assert.assertThat(studentMapData, hasValue(firstName) );
         studentId=  studentMapData.get("id");
        System.out.println(studentId);

    }

    @Title("Update the user and verify the updated information")
    @Test
    public void test003(){

       firstName= firstName+"Queen";

        List<String>courses= new ArrayList<>();
        courses.add("3Years");
        courses.add("2Years");

        StudentPojo studentPojo= new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);

        SerenityRest.given()
                .log().all()
                .header("Content-Type","application/json")
                .pathParam("studentID",studentId)
                .body(studentPojo)
                .when()
                .put(EndPoints.UPDATE_STUDENT_BY_ID)
                .then().log().all().statusCode(200);

        String part1 ="findAll{it.firstName=='";
        String part2 = "'}.get(0)";

        HashMap<String,?>studentMapData =SerenityRest.given()
                .when()
                .get(EndPoints.GET_ALL_STUDENT)
                .then().statusCode(200).extract().path(part1+ firstName + part2);
        Assert.assertThat(studentMapData, hasValue(firstName) );

    }
    @Title("Delete the student and verify if the student is deleted")
    @Test
    public void test004(){

        SerenityRest.given()
                .pathParam("studentID",studentId)
                .when()
                .delete(EndPoints.DELETE_STUDENT_BY_ID)
                .then().log().all().statusCode(204);

        SerenityRest.given()
                .pathParam("studentID",studentId)
                .when()
                .get(EndPoints.GET_SINGLE_STUDENT_BY_ID)
                .then().log().all().statusCode(404);

    }

}
